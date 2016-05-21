package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.events.ForgeWorldEvent;
import com.feed_the_beast.ftbl.util.FTBLib;
import com.feed_the_beast.ftbl.util.LMNBTUtils;
import com.mojang.authlib.GameProfile;
import latmod.lib.LMUtils;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

/**
 * Created by LatvianModder on 09.02.2016.
 */
public final class ForgeWorldSP extends ForgeWorld
{
    public static ForgeWorldSP inst = null;
    public final ForgePlayerSPSelf clientPlayer;
    public Collection<ResourceLocation> serverDataIDs;

    public ForgeWorldSP(GameProfile p)
    {
        clientPlayer = new ForgePlayerSPSelf(p);
        serverDataIDs = new HashSet<>();
    }

    @Override
    public Side getSide()
    {
        return Side.CLIENT;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public World getMCWorld()
    {
        return FTBLibClient.mc.theWorld;
    }

    @Override
    public ForgeWorldMP toWorldMP()
    {
        return null;
    }

    @Override
    public ForgeWorldSP toWorldSP()
    {
        return this;
    }

    @Override
    public ForgePlayerSP getPlayer(Object o)
    {
        ForgePlayer p = super.getPlayer(o);
        return (p == null) ? null : p.toPlayerSP();
    }

    public void setModeRaw(String mode)
    {
        currentMode = new PackMode(mode);
    }

    public void readDataFromNet(NBTTagCompound tag, boolean login)
    {
        worldID = new UUID(tag.getLong("IDM"), tag.getLong("IDL"));
        currentMode = new PackMode(tag.getString("M"));

        if(login)
        {
            playerMap.clear();

            //GameProfile gp = FTBLibClient.mc.getSession().getProfile();
            //TODO: Improve this

            NBTTagCompound tag1 = tag.getCompoundTag("PM");

            for(String s : tag1.getKeySet())
            {
                UUID uuid = LMUtils.fromString(s);
                String name = tag1.getString(s);

                if(uuid.equals(clientPlayer.getProfile().getId()))
                {
                    playerMap.put(uuid, clientPlayer);
                }
                else
                {
                    playerMap.put(uuid, new ForgePlayerSP(new GameProfile(uuid, name)));
                }
            }

            FTBLib.dev_logger.info("Client player ID: " + clientPlayer.getProfile().getId() + " and " + (playerMap.size() - 1) + " other players");

            Map<String, NBTBase> map1 = LMNBTUtils.toMap(tag.getCompoundTag("PMD"));

            clientPlayer.readFromNet((NBTTagCompound) map1.get(clientPlayer.getStringUUID()), true);
            map1.remove(clientPlayer.getStringUUID());

            for(Map.Entry<String, NBTBase> e : map1.entrySet())
            {
                ForgePlayerSP p = playerMap.get(LMUtils.fromString(e.getKey())).toPlayerSP();
                p.readFromNet((NBTTagCompound) e.getValue(), false);
            }
        }

        serverDataIDs.clear();
        MinecraftForge.EVENT_BUS.post(new ForgeWorldEvent.Sync(this, tag.getCompoundTag("SY"), clientPlayer, login));
    }
}