package ftb.lib.api;

import com.google.gson.*;
import ftb.lib.FTBLib;
import ftb.lib.mod.FTBLibEventHandler;
import latmod.lib.json.UUIDTypeAdapterLM;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.*;

import java.io.File;
import java.util.*;

/**
 * Created by LatvianModder on 09.02.2016.
 */
public final class ForgeWorldMP extends ForgeWorld
{
	public static ForgeWorldMP inst = null;
	public final File latmodFolder;
	
	public ForgeWorldMP(File f)
	{
		super(Side.SERVER);
		latmodFolder = f;
		currentMode = GameModes.getGameModes().defaultMode;
	}
	
	public void init()
	{
		super.init();
		
		for(ForgeWorldData d : customData.values())
		{
			if(d instanceof IWorldTick)
			{
				FTBLibEventHandler.instance.ticking.add((IWorldTick) d);
			}
		}
	}
	
	public World getMCWorld()
	{ return MinecraftServer.getServer().getEntityWorld(); }
	
	public ForgeWorldMP toWorldMP()
	{ return this; }
	
	@SideOnly(Side.CLIENT)
	public ForgeWorldSP toWorldSP()
	{ return null; }
	
	public ForgePlayerMP getPlayer(Object o)
	{
		if(o instanceof FakePlayer) return new ForgePlayerFake((FakePlayer) o);
		ForgePlayer p = super.getPlayer(o);
		return (p == null) ? null : p.toPlayerMP();
	}
	
	public void load(JsonObject group)
	{
		worldID = group.has("world_id") ? UUIDTypeAdapterLM.getUUID(group.get("world_id").getAsString()) : null;
		getID();
		
		currentMode = group.has("mode") ? GameModes.getGameModes().get(group.get("mode").getAsString()) : GameModes.getGameModes().defaultMode;
		
		if(!customData.isEmpty() && group.has("custom"))
		{
			for(Map.Entry<String, JsonElement> entry : group.get("custom").getAsJsonObject().entrySet())
			{
				ForgeWorldData d = customData.get(entry.getKey());
				
				if(d != null)
				{
					d.loadData(entry.getValue().getAsJsonObject());
				}
			}
		}
		
		FTBLib.dev_logger.info("ForgeWorldMP Loaded: " + group);
	}
	
	public void save(JsonObject group)
	{
		group.add("world_id", new JsonPrimitive(UUIDTypeAdapterLM.getString(getID())));
		group.add("mode", new JsonPrimitive(currentMode.getID()));
		
		if(!customData.isEmpty())
		{
			JsonObject customGroup = new JsonObject();
			JsonObject group1;
			
			for(ForgeWorldData d : customData.values())
			{
				group1 = new JsonObject();
				d.saveData(group1);
				
				if(!group1.entrySet().isEmpty())
				{
					customGroup.add(d.getID(), group1);
				}
			}
			
			if(!customGroup.entrySet().isEmpty())
			{
				group.add("custom", customGroup);
			}
		}
		
		FTBLib.dev_logger.info("ForgeWorldMP Saved: " + group);
	}
	
	public void writeDataToNet(NBTTagCompound tag, ForgePlayerMP self)
	{
		tag.setLong("IDM", getID().getMostSignificantBits());
		tag.setLong("IDL", getID().getLeastSignificantBits());
		tag.setString("M", currentMode.getID());
		
		NBTTagCompound tag1;
		
		if(self != null)
		{
			NBTTagCompound playerMapTag = new NBTTagCompound();
			
			List<ForgePlayerMP> onlinePlayers = new ArrayList<>();
			for(ForgePlayer p : playerMap.values())
			{
				playerMapTag.setString(p.getStringUUID(), p.getProfile().getName());
				if(p.isOnline() && !p.equalsPlayer(self)) onlinePlayers.add(p.toPlayerMP());
			}
			
			tag.setTag("PM", playerMapTag);
			
			playerMapTag = new NBTTagCompound();
			
			for(ForgePlayerMP p : onlinePlayers)
			{
				tag1 = new NBTTagCompound();
				p.writeToNet(tag1, false);
				playerMapTag.setTag(p.getStringUUID(), tag1);
			}
			
			tag1 = new NBTTagCompound();
			self.writeToNet(tag1, false);
			playerMapTag.setTag(self.getStringUUID(), tag1);
			
			tag.setTag("PMD", playerMapTag);
		}
	}
	
	public List<ForgePlayerMP> getServerPlayers()
	{
		List<ForgePlayerMP> list = new ArrayList<>();
		for(ForgePlayer p : playerMap.values())
			list.add(p.toPlayerMP());
		return list;
	}
}
