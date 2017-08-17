package com.feed_the_beast.ftbl;

import com.feed_the_beast.ftbl.api.EventHandler;
import com.feed_the_beast.ftbl.api.events.team.ForgeTeamCreatedEvent;
import com.feed_the_beast.ftbl.api.events.team.ForgeTeamPlayerJoinedEvent;
import com.feed_the_beast.ftbl.api_impl.ForgePlayer;
import com.feed_the_beast.ftbl.api_impl.ForgeTeam;
import com.feed_the_beast.ftbl.api_impl.SharedServerData;
import com.feed_the_beast.ftbl.api_impl.Universe;
import com.feed_the_beast.ftbl.lib.internal.FTBLibLang;
import com.feed_the_beast.ftbl.lib.util.CommonUtils;
import com.feed_the_beast.ftbl.lib.util.JsonUtils;
import com.feed_the_beast.ftbl.lib.util.StringUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.io.File;

/**
 * @author LatvianModder
 */
@EventHandler
public class FTBLibEventHandler
{
	@SubscribeEvent
	public static void onWorldSaved(WorldEvent.Save event)
	{
		if (event.getWorld().provider.getDimension() != 0 || !(event.getWorld() instanceof WorldServer))
		{
			return;
		}

		try
		{
			JsonUtils.toJson(new File(CommonUtils.folderWorld, "world_data.json"), SharedServerData.INSTANCE.getSerializableElement());
			Universe.INSTANCE.save(new File(CommonUtils.folderWorld, "data/ftb_lib"));
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent e)
	{
		if (!(e.player instanceof EntityPlayerMP) || Universe.INSTANCE == null)
		{
			return;
		}

		EntityPlayerMP ep = (EntityPlayerMP) e.player;

		if (!ep.mcServer.getPlayerList().canJoin(ep.getGameProfile()))
		{
			return;
		}

		ForgePlayer p = Universe.INSTANCE.getPlayer(ep);
		boolean firstLogin = p == null;

		if (firstLogin)
		{
			p = new ForgePlayer(ep.getGameProfile().getId(), ep.getGameProfile().getName());
			Universe.INSTANCE.playerMap.put(p.getId(), p);
		}
		else if (!p.getName().equals(ep.getName()))
		{
			p.setUsername(ep.getGameProfile().getName());
		}

		p.onLoggedIn(ep, firstLogin);

		if (firstLogin && FTBLibConfig.AUTOCREATE_TEAMS.getBoolean())
		{
			String id = p.getName().toLowerCase();

			if (Universe.INSTANCE.getTeam(id) != null)
			{
				id = StringUtils.fromUUID(p.getId());
			}

			if (Universe.INSTANCE.getTeam(id) == null)
			{
				ForgeTeam team = new ForgeTeam(id);
				team.changeOwner(p);
				Universe.INSTANCE.teams.put(team.getName(), team);
				new ForgeTeamCreatedEvent(team).post();
				new ForgeTeamPlayerJoinedEvent(team, p).post();
			}
		}

		if (!p.hideTeamNotification() && p.getTeam() == null)
		{
			ITextComponent b1 = FTBLibLang.CLICK_HERE.textComponent();
			b1.getStyle().setColor(TextFormatting.GOLD);
			b1.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ftb team gui"));
			b1.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString("TeamsGUI")));
			ITextComponent b2 = FTBLibLang.CLICK_HERE.textComponent();
			b2.getStyle().setColor(TextFormatting.GOLD);
			b2.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ftb my_settings ftbl.hide_team_notification toggle"));
			b2.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, FTBLibLang.TEAM_NOTIFICATION_HIDE.textComponent()));
			ep.sendMessage(FTBLibLang.TEAM_NOTIFICATION.textComponent(b1, b2));
		}
	}

	@SubscribeEvent
	public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent e)
	{
		if (e.player instanceof EntityPlayerMP && Universe.INSTANCE != null)
		{
			ForgePlayer p = Universe.INSTANCE.getPlayer(e.player);

			if (p != null)
			{
				p.onLoggedOut();
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerDeath(LivingDeathEvent e)
	{
		if (e.getEntity() instanceof EntityPlayerMP && Universe.INSTANCE != null)
		{
			EntityPlayerMP ep = (EntityPlayerMP) e.getEntity();
			ForgePlayer p = Universe.INSTANCE.getPlayer(ep);

			if (p != null)
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