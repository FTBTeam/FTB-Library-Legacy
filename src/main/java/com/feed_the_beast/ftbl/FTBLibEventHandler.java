package com.feed_the_beast.ftbl;

import com.feed_the_beast.ftbl.api.events.team.ForgeTeamCreatedEvent;
import com.feed_the_beast.ftbl.api.events.team.ForgeTeamPlayerJoinedEvent;
import com.feed_the_beast.ftbl.api_impl.ForgePlayer;
import com.feed_the_beast.ftbl.api_impl.ForgeTeam;
import com.feed_the_beast.ftbl.api_impl.SharedServerData;
import com.feed_the_beast.ftbl.api_impl.Universe;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import com.feed_the_beast.ftbl.lib.util.JsonUtils;
import com.feed_the_beast.ftbl.lib.util.LMUtils;
import com.feed_the_beast.ftbl.lib.util.StringUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
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
    public static void onWorldSaved(WorldEvent.Save event)
    {
        if(event.getWorld().provider.getDimension() != 0 || !(event.getWorld() instanceof WorldServer))
        {
            return;
        }

        try
        {
            JsonUtils.toJson(new File(LMUtils.folderWorld, "world_data.json"), SharedServerData.INSTANCE.getSerializableElement());
            Universe.INSTANCE.save(new File(LMUtils.folderWorld, "data/ftb_lib"));
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @SubscribeEvent
    @Optional.Method(modid = "mercurius")
    public static void onAnalytics(net.minecraftforge.mercurius.binding.StatsCollectionEvent event)
    {
        Map<String, Object> map = new HashMap<>();
        map.put("FTB_PackMode", SharedServerData.INSTANCE.getPackMode().getName());
        event.addEventData(FTBLibFinals.MOD_ID, map);
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent e)
    {
        if(!(e.player instanceof EntityPlayerMP) || Universe.INSTANCE == null)
        {
            return;
        }

        EntityPlayerMP ep = (EntityPlayerMP) e.player;
        ForgePlayer p = Universe.INSTANCE.getPlayer(ep);
        boolean firstLogin = p == null;

        if(firstLogin)
        {
            p = new ForgePlayer(ep.getGameProfile().getId(), ep.getGameProfile().getName());
            Universe.INSTANCE.playerMap.put(p.getId(), p);
        }
        else if(!p.getName().equals(ep.getName()))
        {
            p.setUsername(ep.getGameProfile().getName());
        }

        p.onLoggedIn(ep, firstLogin);

        if(firstLogin && FTBLibConfig.AUTOCREATE_TEAMS.getBoolean())
        {
            String id = p.getName().toLowerCase();

            if(Universe.INSTANCE.getTeam(id) != null)
            {
                id = StringUtils.fromUUID(p.getId());
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

        if(!p.hideTeamNotification() && p.getTeam() == null)
        {
            ITextComponent c = new TextComponentString("You haven't joined or created a team yet! ");
            ITextComponent b1 = new TextComponentString("[Click Here]");
            b1.getStyle().setColor(TextFormatting.GOLD);
            b1.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ftb team gui"));
            b1.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("TeamsGUI")));
            c.appendSibling(b1);
            c.appendText(" to open TeamsGUI or ");
            ITextComponent b2 = new TextComponentString("[Click Here]");
            b2.getStyle().setColor(TextFormatting.GOLD);
            b2.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ftb my_settings ftbl.hide_team_notification true"));
            b2.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("Hide This Message")));
            c.appendSibling(b2);
            c.appendText(" to hide this message.");
            ep.sendMessage(c);
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent e)
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
    public static void onPlayerDeath(LivingDeathEvent e)
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
    public static void onPlayerInteract(PlayerInteractEvent e)
    {
    }
    */
}