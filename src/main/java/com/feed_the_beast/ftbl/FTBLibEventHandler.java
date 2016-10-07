package com.feed_the_beast.ftbl;

import com.feed_the_beast.ftbl.api.events.team.ForgeTeamCreatedEvent;
import com.feed_the_beast.ftbl.api.events.team.ForgeTeamPlayerJoinedEvent;
import com.feed_the_beast.ftbl.api_impl.FTBLibAPI_Impl;
import com.feed_the_beast.ftbl.api_impl.ForgePlayer;
import com.feed_the_beast.ftbl.api_impl.ForgeTeam;
import com.feed_the_beast.ftbl.api_impl.Universe;
import com.feed_the_beast.ftbl.lib.util.LMJsonUtils;
import com.feed_the_beast.ftbl.lib.util.LMNBTUtils;
import com.feed_the_beast.ftbl.lib.util.LMStringUtils;
import com.feed_the_beast.ftbl.lib.util.LMUtils;
import com.tamashenning.forgeanalytics.client.ForgeAnalyticsConstants;
import com.tamashenning.forgeanalytics.events.AnalyticsEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;
import java.util.Locale;

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
            Universe universe = FTBLibAPI_Impl.INSTANCE.getUniverse();

            if(universe != null)
            {
                EntityPlayerMP ep = (EntityPlayerMP) e.player;

                ForgePlayer p = universe.getPlayer(ep);

                boolean firstLogin = p == null;

                if(firstLogin)
                {
                    p = new ForgePlayer(ep.getGameProfile());
                    universe.playerMap.put(p.getProfile().getId(), p);
                }
                else if(!p.getProfile().getName().equals(ep.getName()))
                {
                    p.setProfile(ep.getGameProfile());
                }

                p.onLoggedIn(ep, firstLogin);

                if(firstLogin && FTBLibConfig.AUTOCREATE_TEAMS.getBoolean())
                {
                    String id = p.getProfile().getName().toLowerCase(Locale.ENGLISH);

                    if(universe.getTeam(id) != null)
                    {
                        id = LMStringUtils.fromUUID(p.getProfile().getId());
                    }

                    if(universe.getTeam(id) == null)
                    {
                        ForgeTeam team = new ForgeTeam(id);
                        team.changeOwner(p);
                        p.getUniverse().teams.put(team.getName(), team);
                        MinecraftForge.EVENT_BUS.post(new ForgeTeamCreatedEvent(team));
                        MinecraftForge.EVENT_BUS.post(new ForgeTeamPlayerJoinedEvent(team, p));
                    }
                }
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