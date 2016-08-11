package com.feed_the_beast.ftbl;

import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api_impl.FTBLibAPI_Impl;
import com.feed_the_beast.ftbl.api_impl.ForgePlayer;
import com.feed_the_beast.ftbl.api_impl.ForgeWorld;
import com.feed_the_beast.ftbl.util.FTBLib;
import com.latmod.lib.json.LMJsonUtils;
import com.latmod.lib.util.LMNBTUtils;
import com.tamashenning.forgeanalytics.client.ForgeAnalyticsConstants;
import com.tamashenning.forgeanalytics.events.AnalyticsEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ITickable;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FTBLibEventHandler implements ITickable
{
    public static final FTBLibEventHandler instance = new FTBLibEventHandler();
    public static final List<ServerTickCallback> callbacks = new ArrayList<>();
    public static final List<ServerTickCallback> pendingCallbacks = new ArrayList<>();

    public static class ServerTickCallback
    {
        public final int maxTick;
        public Runnable runnable;
        private int ticks = 0;

        public ServerTickCallback(int i, Runnable r)
        {
            maxTick = i;
            runnable = r;
        }

        public boolean incAndCheck()
        {
            ticks++;
            if(ticks >= maxTick)
            {
                runnable.run();
                return true;
            }

            return false;
        }
    }

    @SubscribeEvent
    public void onWorldSaved(WorldEvent.Save event)
    {
        if(event.getWorld().provider.getDimensionType() == DimensionType.OVERWORLD && event.getWorld() instanceof WorldServer)
        {
            try
            {
                LMJsonUtils.toJson(new File(FTBLib.folderWorld, "world_data.json"), FTBLibAPI_Impl.INSTANCE.getSharedData(Side.SERVER).getSerializableElement());
                LMNBTUtils.writeTag(new File(FTBLib.folderWorld, "data/FTBLib.dat"), FTBLibAPI.INSTANCE.getWorld().serializeNBT());
                FTBLib.dev_logger.info("ForgeWorldMP Saved");
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
        ForgeAnalyticsConstants.CustomProperties.put("FTB_PackMode", FTBLibAPI.INSTANCE.getSharedData(event.side).getMode().getID());
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent e)
    {
        if(e.player instanceof EntityPlayerMP)
        {
            ForgeWorld world = FTBLibAPI_Impl.INSTANCE.getWorld();

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

                p.setPlayer(ep);
                p.onLoggedIn(firstLogin);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent e)
    {
        if(e.player instanceof EntityPlayerMP && FTBLibAPI_Impl.INSTANCE.getWorld() != null)
        {
            ForgePlayer p = FTBLibAPI_Impl.INSTANCE.getWorld().getPlayer(e.player);

            if(p != null)
            {
                p.onLoggedOut();
            }
        }
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent e)
    {
        if(e.getEntity() instanceof EntityPlayerMP && FTBLibAPI_Impl.INSTANCE.getWorld() != null)
        {
            FTBLibAPI_Impl.INSTANCE.getWorld().getPlayer(e.getEntity()).onDeath();
        }
    }

    @Override
    public void update()
    {
        if(!pendingCallbacks.isEmpty())
        {
            callbacks.addAll(pendingCallbacks);
            pendingCallbacks.clear();
        }

        if(!callbacks.isEmpty())
        {
            for(int i = callbacks.size() - 1; i >= 0; i--)
            {
                if(callbacks.get(i).incAndCheck())
                {
                    callbacks.remove(i);
                }
            }
        }
    }

    /*
    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent e)
    {
    }
    */
}