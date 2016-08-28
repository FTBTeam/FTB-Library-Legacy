package com.feed_the_beast.ftbl;

import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api_impl.FTBLibAPI_Impl;
import com.feed_the_beast.ftbl.api_impl.ForgePlayer;
import com.feed_the_beast.ftbl.api_impl.ForgeWorld;
import com.feed_the_beast.ftbl.util.FTBLib;
import com.latmod.lib.util.LMJsonUtils;
import com.latmod.lib.util.LMNBTUtils;
import com.tamashenning.forgeanalytics.client.ForgeAnalyticsConstants;
import com.tamashenning.forgeanalytics.events.AnalyticsEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;

public class FTBLibEventHandler
{
    @SubscribeEvent
    public void onWorldSaved(WorldEvent.Save event)
    {
        if(event.getWorld().provider.getDimensionType() == DimensionType.OVERWORLD && event.getWorld() instanceof WorldServer)
        {
            try
            {
                LMJsonUtils.toJson(new File(FTBLib.folderWorld, "world_data.json"), FTBLibAPI_Impl.get().getSharedData(Side.SERVER).getSerializableElement());
                LMNBTUtils.writeTag(new File(FTBLib.folderWorld, "data/FTBLib.dat"), FTBLibAPI.get().getWorld().serializeNBT());
                //FTBLib.dev_logger.info("ForgeWorldMP Saved");
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    @SubscribeEvent
    @Optional.Method(modid = "forgeanalytics")
    public void onAnalytics(AnalyticsEvent event)
    {
        ForgeAnalyticsConstants.CustomProperties.put("FTB_PackMode", FTBLibAPI.get().getSharedData(event.side).getMode().getID());
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent e)
    {
        if(e.player instanceof EntityPlayerMP)
        {
            ForgeWorld world = FTBLibAPI_Impl.get().getWorld();

            if(world != null)
            {
                EntityPlayerMP ep = (EntityPlayerMP) e.player;

                ForgePlayer p = world.getPlayer(ep);

                boolean firstLogin = p == null;

                if(firstLogin)
                {
                    p = new ForgePlayer(ep.getGameProfile());
                    world.playerMap.put(p.getProfile().getId(), p);
                }
                else if(!p.getProfile().getName().equals(ep.getName()))
                {
                    p.setProfile(ep.getGameProfile());
                }

                p.onLoggedIn(ep, firstLogin);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent e)
    {
        if(e.player instanceof EntityPlayerMP && FTBLibAPI_Impl.get().getWorld() != null)
        {
            ForgePlayer p = FTBLibAPI_Impl.get().getWorld().getPlayer(e.player);

            if(p != null)
            {
                p.onLoggedOut();
            }
        }
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent e)
    {
        if(e.getEntity() instanceof EntityPlayerMP && FTBLibAPI_Impl.get().getWorld() != null)
        {
            FTBLibAPI_Impl.get().getWorld().getPlayer(e.getEntity()).onDeath();
        }
    }

    /*
    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent e)
    {
    }
    */
}