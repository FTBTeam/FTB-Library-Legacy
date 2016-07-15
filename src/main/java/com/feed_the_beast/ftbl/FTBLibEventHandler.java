package com.feed_the_beast.ftbl;

import com.feed_the_beast.ftbl.api.ForgePlayerMP;
import com.feed_the_beast.ftbl.api.ForgeWorld;
import com.feed_the_beast.ftbl.api.ForgeWorldMP;
import com.feed_the_beast.ftbl.api.ServerTickCallback;
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

import java.util.ArrayList;
import java.util.List;

public class FTBLibEventHandler implements ITickable
{
    public static final FTBLibEventHandler instance = new FTBLibEventHandler();
    public static final List<ServerTickCallback> callbacks = new ArrayList<>();
    public static final List<ServerTickCallback> pendingCallbacks = new ArrayList<>();

    @SubscribeEvent
    public void onWorldSaved(WorldEvent.Save event)
    {
        if(event.getWorld().provider.getDimensionType() == DimensionType.OVERWORLD && event.getWorld() instanceof WorldServer)
        {
            try
            {
                ForgeWorldMP.inst.save();
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
        ForgeWorld w = ForgeWorld.getFrom(event.side);

        if(w != null)
        {
            ForgeAnalyticsConstants.CustomProperties.put("FTB_PackMode", w.getMode().getID());
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent e)
    {
        if(e.player instanceof EntityPlayerMP && ForgeWorldMP.inst != null)
        {
            EntityPlayerMP ep = (EntityPlayerMP) e.player;

            ForgePlayerMP p = ForgeWorldMP.inst.getPlayer(ep);

            boolean firstLogin = p == null;

            if(firstLogin)
            {
                p = new ForgePlayerMP(ep.getGameProfile());
                ForgeWorldMP.inst.playerMap.put(p.getProfile().getId(), p);
            }
            else if(!p.getProfile().getName().equals(ep.getName()))
            {
                p.setProfile(ep.getGameProfile());
            }

            p.setPlayer(ep);
            p.onLoggedIn(firstLogin);
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent e)
    {
        if(e.player instanceof EntityPlayerMP && ForgeWorldMP.inst != null)
        {
            ForgePlayerMP p = ForgeWorldMP.inst.getPlayer(e.player);

            if(p != null)
            {
                p.onLoggedOut();
            }
        }
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent e)
    {
        if(e.getEntity() instanceof EntityPlayerMP && ForgeWorldMP.inst != null)
        {
            ForgeWorldMP.inst.getPlayer(e.getEntity()).onDeath();
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