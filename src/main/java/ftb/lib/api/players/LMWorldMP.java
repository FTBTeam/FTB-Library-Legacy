package ftb.lib.api.players;

import com.google.gson.*;
import ftb.lib.api.GameModes;
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
public final class LMWorldMP extends LMWorld
{
	public static LMWorldMP inst = null;
	public final File latmodFolder;
	
	public LMWorldMP(File f)
	{
		super(Side.SERVER);
		latmodFolder = f;
		currentMode = GameModes.getGameModes().defaultMode;
	}
	
	public World getMCWorld()
	{ return MinecraftServer.getServer().getEntityWorld(); }
	
	public LMWorldMP toWorldMP()
	{ return this; }
	
	@SideOnly(Side.CLIENT)
	public LMWorldSP toWorldSP()
	{ return null; }
	
	public LMPlayerMP getPlayer(Object o)
	{
		if(o instanceof FakePlayer) return new LMPlayerFake((FakePlayer) o);
		LMPlayer p = super.getPlayer(o);
		return (p == null) ? null : p.toPlayerMP();
	}
	
	public void load(JsonObject group)
	{
		currentMode = group.has("mode") ? GameModes.getGameModes().get(group.get("mode").getAsString()) : GameModes.getGameModes().defaultMode;
		
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
					customGroup.add(d.ID, group1);
				}
			}
			
			if(!customGroup.entrySet().isEmpty())
			{
				group.add("custom", customGroup);
			}
		}
	}
	
	public void save(JsonObject group)
	{
		group.add("mode", new JsonPrimitive(currentMode.ID));
		
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
					customGroup.add(d.ID, group1);
				}
			}
			
			if(!customGroup.entrySet().isEmpty())
			{
				group.add("custom", customGroup);
			}
		}
	}
	
	public void writeDataToNet(NBTTagCompound tag, LMPlayerMP self)
	{
		tag.setString("M", currentMode.ID);
		NBTTagCompound tag1;
		
		if(self != null)
		{
			NBTTagCompound playerMapTag = new NBTTagCompound();
			
			List<LMPlayerMP> onlinePlayers = new ArrayList<>();
			for(LMPlayer p : playerMap.values())
			{
				playerMapTag.setString(p.getStringUUID(), p.getProfile().getName());
				if(p.isOnline() && !p.equalsPlayer(self)) onlinePlayers.add(p.toPlayerMP());
			}
			
			tag.setTag("PM", playerMapTag);
			
			playerMapTag = new NBTTagCompound();
			
			for(LMPlayerMP p : onlinePlayers)
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
	
	public List<LMPlayerMP> getServerPlayers()
	{
		List<LMPlayerMP> list = new ArrayList<>();
		for(LMPlayer p : playerMap.values())
			list.add(p.toPlayerMP());
		return list;
	}
}
