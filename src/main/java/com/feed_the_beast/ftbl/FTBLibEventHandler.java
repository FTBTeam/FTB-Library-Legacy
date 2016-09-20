package com.feed_the_beast.ftbl;

import com.feed_the_beast.ftbl.api_impl.FTBLibAPI_Impl;
import com.feed_the_beast.ftbl.api_impl.ForgePlayer;
import com.feed_the_beast.ftbl.api_impl.Universe;
import com.latmod.lib.util.LMJsonUtils;
import com.latmod.lib.util.LMNBTUtils;
import com.latmod.lib.util.LMUtils;
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
                LMJsonUtils.toJson(new File(LMUtils.folderWorld, "world_data.json"), FTBLibAPI_Impl.INSTANCE.getSharedData(Side.SERVER).getSerializableElement());
                LMNBTUtils.writeTag(new File(LMUtils.folderWorld, "data/FTBLib.dat"), FTBLibAPI_Impl.INSTANCE.getUniverse().serializeNBT());
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
        ForgeAnalyticsConstants.CustomProperties.put("FTB_PackMode", FTBLibAPI_Impl.INSTANCE.getSharedData(event.side).getPackMode().getID());
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent e)
    {
        if(e.player instanceof EntityPlayerMP)
        {
            Universe world = FTBLibAPI_Impl.INSTANCE.getUniverse();

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
        if(e.player instanceof EntityPlayerMP && FTBLibAPI_Impl.INSTANCE.getUniverse() != null)
        {
            ForgePlayer p = FTBLibAPI_Impl.INSTANCE.getUniverse().getPlayer(e.player);

            if(p != null)
            {
                p.onLoggedOut();
            }
        }
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent e)
    {
        if(e.getEntity() instanceof EntityPlayerMP && FTBLibAPI_Impl.INSTANCE.getUniverse() != null)
        {
            FTBLibAPI_Impl.INSTANCE.getUniverse().getPlayer(e.getEntity()).onDeath();
        }
    }

    /*
    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent e)
    {
    }
    */
}