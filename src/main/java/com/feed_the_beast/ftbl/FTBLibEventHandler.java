package com.feed_the_beast.ftbl;

import com.feed_the_beast.ftbl.api.events.team.ForgeTeamCreatedEvent;
import com.feed_the_beast.ftbl.api.events.team.ForgeTeamPlayerJoinedEvent;
import com.feed_the_beast.ftbl.api_impl.ForgePlayer;
import com.feed_the_beast.ftbl.api_impl.ForgeTeam;
import com.feed_the_beast.ftbl.api_impl.SharedServerData;
import com.feed_the_beast.ftbl.api_impl.Universe;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import com.feed_the_beast.ftbl.lib.util.LMJsonUtils;
import com.feed_the_beast.ftbl.lib.util.LMNBTUtils;
import com.feed_the_beast.ftbl.lib.util.LMStringUtils;
import com.feed_the_beast.ftbl.lib.util.LMUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FTBLibEventHandler
{
    @SubscribeEvent
    public void onWorldSaved(WorldEvent.Save event)
    {
        if(event.getWorld().provider.getDimension() != 0 || !(event.getWorld() instanceof WorldServer))
        {
            return;
        }

        try
        {
            LMJsonUtils.toJson(new File(LMUtils.folderWorld, "world_data.json"), SharedServerData.INSTANCE.getSerializableElement());
            LMNBTUtils.writeTag(new File(LMUtils.folderWorld, "data/FTBLib.dat"), Universe.INSTANCE.serializeNBT());
            //FTBLib.dev_logger.info("ForgeWorldMP Saved");
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @SubscribeEvent
    @Optional.Method(modid = "mercurius")
    public void onAnalytics(net.minecraftforge.mercurius.binding.StatsCollectionEvent event)
    {
        Map<String, Object> map = new HashMap<>();
        map.put("FTB_PackMode", SharedServerData.INSTANCE.getPackMode().getName());
        event.addEventData(FTBLibFinals.MOD_ID, map);
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent e)
    {
        if(e.player instanceof EntityPlayerMP)
        {
            if(Universe.INSTANCE != null)
            {
                EntityPlayerMP ep = (EntityPlayerMP) e.player;

                ForgePlayer p = Universe.INSTANCE.getPlayer(ep);

                boolean firstLogin = p == null;

                if(firstLogin)
                {
                    p = new ForgePlayer(ep.getGameProfile());
                    Universe.INSTANCE.playerMap.put(p.getProfile().getId(), p);
                }
                else if(!p.getProfile().getName().equals(ep.getName()))
                {
                    p.setProfile(ep.getGameProfile());
                }

                p.onLoggedIn(ep, firstLogin);

                if(firstLogin && FTBLibConfig.AUTOCREATE_TEAMS.getBoolean())
                {
                    String id = p.getProfile().getName().toLowerCase();

                    if(Universe.INSTANCE.getTeam(id) != null)
                    {
                        id = LMStringUtils.fromUUID(p.getProfile().getId());
                    }

                    if(Universe.INSTANCE.getTeam(id) == null)
                    {
                        ForgeTeam team = new ForgeTeam(id);
                        team.changeOwner(p);
                        Universe.INSTANCE.teams.put(team.getName(), team);
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
        if(e.player instanceof EntityPlayerMP && Universe.INSTANCE != null)
        {
            ForgePlayer p = Universe.INSTANCE.getPlayer(e.player);

            if(p != null)
            {
                p.onLoggedOut();
            }
        }
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent e)
    {
        if(e.getEntity() instanceof EntityPlayerMP && Universe.INSTANCE != null)
        {
            EntityPlayerMP ep = (EntityPlayerMP) e.getEntity();
            ForgePlayer p = Universe.INSTANCE.getPlayer(ep);

            if(p != null)
            {
                p.onDeath(ep, e.getSource());
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