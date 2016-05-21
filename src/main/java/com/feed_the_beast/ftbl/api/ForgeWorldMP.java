package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.api.events.ForgeWorldEvent;
import com.feed_the_beast.ftbl.util.FTBLib;
import com.feed_the_beast.ftbl.util.LMNBTUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.authlib.GameProfile;
import latmod.lib.LMJsonUtils;
import latmod.lib.LMUtils;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by LatvianModder on 09.02.2016.
 */
public final class ForgeWorldMP extends ForgeWorld
{
    public static ForgeWorldMP inst = null;
    public final File latmodFolder;

    public ForgeWorldMP(File f)
    {
        latmodFolder = f;
        currentMode = PackModes.instance().getDefault();
    }

    @Override
    public Side getSide()
    {
        return Side.SERVER;
    }

    @Override
    public World getMCWorld()
    {
        return FTBLib.getServer().getEntityWorld();
    }

    @Override
    public ForgeWorldMP toWorldMP()
    {
        return this;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ForgeWorldSP toWorldSP()
    {
        return null;
    }

    @Override
    public ForgePlayerMP getPlayer(Object o)
    {
        if(o instanceof FakePlayer)
        {
            return new ForgePlayerFake((FakePlayer) o);
        }
        ForgePlayer p = super.getPlayer(o);
        return (p == null) ? null : p.toPlayerMP();
    }

    public void load() throws Exception
    {
        JsonElement worldData = LMJsonUtils.fromJson(new File(latmodFolder.getParent(), "world_data.json"));

        if(worldData.isJsonObject())
        {
            JsonObject group = worldData.getAsJsonObject();
            worldID = group.has("world_id") ? LMUtils.fromString(group.get("world_id").getAsString()) : null;
            getID();

            currentMode = group.has("mode") ? PackModes.instance().getMode(group.get("mode").getAsString()) : PackModes.instance().getDefault();
        }

        NBTTagCompound nbt = LMNBTUtils.readTag(new File(latmodFolder, "LMWorld.dat"));

        playerMap.clear();

        if(capabilities != null && nbt != null)
        {
            capabilities.deserializeNBT(nbt.getCompoundTag("ForgeCaps"));
        }

        for(Map.Entry<String, NBTBase> entry : LMNBTUtils.entrySet(LMNBTUtils.readTag(new File(latmodFolder, "LMPlayers.dat"))))
        {
            NBTTagCompound tag = (NBTTagCompound) entry.getValue();
            UUID id = LMUtils.fromString(entry.getKey());

            if(id == null)
            {
                id = LMUtils.fromString(tag.getString("UUID"));
            }

            if(id != null)
            {
                ForgePlayerMP p = new ForgePlayerMP(new GameProfile(id, tag.getString("Name")));
                p.readFromServer(tag);
                playerMap.put(id, p);
            }
        }

        MinecraftForge.EVENT_BUS.post(new ForgeWorldEvent.OnPostLoaded(this));
    }

    public void save() throws Exception
    {
        JsonObject group = new JsonObject();
        group.add("world_id", new JsonPrimitive(LMUtils.fromUUID(getID())));
        group.add("mode", new JsonPrimitive(currentMode.getID()));
        LMJsonUtils.toJson(new File(latmodFolder.getParent(), "world_data.json"), group);

        FTBLib.dev_logger.info("ForgeWorldMP Saved: " + group);
        
		/*
        
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
		
		JsonObject group = new JsonObject();
		ForgeWorldMP.inst.save(group);
		LMJsonUtils.toJson(new File(ForgeWorldMP.inst.latmodFolder, "world.json"), group);
		
		NBTTagCompound tag = new NBTTagCompound();
		
		for(ForgePlayer p : LMMapUtils.values(ForgeWorldMP.inst.playerMap, null))
		{
			NBTTagCompound tag1 = new NBTTagCompound();
			p.toPlayerMP().writeToServer(tag1);
			tag1.setString("Name", p.getProfile().getName());
			tag.setTag(p.getStringUUID(), tag1);
		}
		
		LMNBTUtils.writeTag(new File(ForgeWorldMP.inst.latmodFolder, "LMPlayers.dat"), tag);
		
		// Export player list //
		
		try
		{
			ArrayList<String> l = new ArrayList<>();
			ArrayList<ForgePlayer> players1 = new ArrayList<>();
			players1.addAll(ForgeWorldMP.inst.playerMap.values());
			Collections.sort(players1);
			
			for(ForgePlayer p : players1)
			{
				l.add(p.getStringUUID() + " :: " + p.getProfile().getName());
			}
			
			LMFileUtils.save(new File(ForgeWorldMP.inst.latmodFolder, "LMPlayers.txt"), l);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		*/
    }

    public void writeDataToNet(NBTTagCompound tag, ForgePlayerMP self, boolean login)
    {
        tag.setLong("IDM", getID().getMostSignificantBits());
        tag.setLong("IDL", getID().getLeastSignificantBits());
        tag.setString("M", currentMode.getID());

        NBTTagCompound tag1, tag2;

        if(login)
        {
            tag1 = new NBTTagCompound();

            List<ForgePlayerMP> onlinePlayers = new ArrayList<>();
            for(ForgePlayer p : playerMap.values())
            {
                tag1.setString(p.getStringUUID(), p.getProfile().getName());
                if(p.isOnline() && !p.equalsPlayer(self))
                {
                    onlinePlayers.add(p.toPlayerMP());
                }
            }

            if(!tag1.hasNoTags())
            {
                tag.setTag("PM", tag1);
            }

            tag1 = new NBTTagCompound();

            for(ForgePlayerMP p : onlinePlayers)
            {
                tag2 = new NBTTagCompound();
                p.writeToNet(tag2, false);
                tag1.setTag(p.getStringUUID(), tag2);
            }

            tag2 = new NBTTagCompound();
            self.writeToNet(tag2, true);
            tag1.setTag(self.getStringUUID(), tag2);

            if(!tag1.hasNoTags())
            {
                tag.setTag("PMD", tag1);
            }
        }

        NBTTagCompound syncedData = new NBTTagCompound();
        MinecraftForge.EVENT_BUS.post(new ForgeWorldEvent.Sync(this, syncedData, self, login));

        if(!syncedData.hasNoTags())
        {
            tag.setTag("SY", syncedData);
        }
    }

    public List<ForgePlayerMP> getServerPlayers()
    {
        List<ForgePlayerMP> list = new ArrayList<>();
        for(ForgePlayer p : playerMap.values())
        { list.add(p.toPlayerMP()); }
        return list;
    }
}
