package com.feed_the_beast.ftblib.lib.data;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.FTBLibLang;
import com.feed_the_beast.ftblib.lib.EnumTeamStatus;
import com.feed_the_beast.ftblib.lib.config.IConfigCallback;
import com.feed_the_beast.ftblib.lib.gui.GuiIcons;
import com.feed_the_beast.ftblib.lib.gui.GuiLang;
import com.feed_the_beast.ftblib.net.MessageMyTeamPlayerList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.util.function.Predicate;

/**
 * @author LatvianModder
 */
public class FTBLibTeamGuiActions
{
	private static final Predicate<EnumTeamStatus> NO_ENEMIES_PREDICATE = status -> status != EnumTeamStatus.ENEMY;
	private static final Predicate<EnumTeamStatus> MEMBERS_PREDICATE = status -> status.isEqualOrGreaterThan(EnumTeamStatus.MEMBER);
	private static final Predicate<EnumTeamStatus> ALLIES_PREDICATE = MEMBERS_PREDICATE.negate().and(NO_ENEMIES_PREDICATE);
	private static final Predicate<EnumTeamStatus> ENEMIES_PREDICATE = status -> status == EnumTeamStatus.ENEMY || status == EnumTeamStatus.NONE;

	public static final TeamGuiAction CONFIG = new TeamGuiAction(new ResourceLocation(FTBLib.MOD_ID, "config"), GuiLang.SETTINGS.textComponent(null), GuiIcons.SETTINGS, -100)
	{
		@Override
		public boolean isAvailable(ForgeTeam team, ForgePlayer player, NBTTagCompound data)
		{
			return team.isModerator(player);
		}

		@Override
		public void onAction(ForgeTeam team, ForgePlayer player, NBTTagCompound data)
		{
			FTBLibAPI.editServerConfig(player.getPlayer(), team.getSettings(), IConfigCallback.DEFAULT);
		}
	};

	public static final TeamGuiAction INFO = new TeamGuiAction(new ResourceLocation(FTBLib.MOD_ID, "info"), GuiLang.INFO.textComponent(null), GuiIcons.INFO, 0)
	{
		@Override
		public boolean isAvailable(ForgeTeam team, ForgePlayer player, NBTTagCompound data)
		{
			return false;
		}

		@Override
		public void onAction(ForgeTeam team, ForgePlayer player, NBTTagCompound data)
		{
			//TODO: Open info gui
		}
	};

	public static final TeamGuiAction MEMBERS = new TeamGuiAction(new ResourceLocation(FTBLib.MOD_ID, "members"), FTBLibLang.TEAM_GUI_MEMBERS.textComponent(null), GuiIcons.FRIENDS, 30)
	{
		@Override
		public boolean isAvailable(ForgeTeam team, ForgePlayer player, NBTTagCompound data)
		{
			return team.isModerator(player) && team.universe.getRealPlayers().size() > 1;
		}

		@Override
		public void onAction(ForgeTeam team, ForgePlayer player, NBTTagCompound data)
		{
			if (data.hasNoTags())
			{
				new MessageMyTeamPlayerList(getId(), team, player, NO_ENEMIES_PREDICATE).sendTo(player.getPlayer());
				return;
			}

			ForgePlayer p = team.universe.getPlayer(data.getString("player"));

			if (p == null || p == player)
			{
				return;
			}

			switch (data.getString("action"))
			{
				case "kick":
				{
					if (team.isMember(p))
					{
						team.removeMember(p);
						team.setRequestingInvite(p, true);
					}

					break;
				}
				case "invite":
				{
					team.setStatus(p, EnumTeamStatus.INVITED);

					if (team.isRequestingInvite(p))
					{
						team.addMember(p);
					}

					break;
				}
				case "cancel_invite":
				{
					if (team.getHighestStatus(p) == EnumTeamStatus.INVITED)
					{
						team.setStatus(p, EnumTeamStatus.NONE);
					}

					break;
				}
				case "deny_request":
				{
					team.setRequestingInvite(p, false);
					break;
				}
			}
		}
	};

	public static final TeamGuiAction ALLIES = new TeamGuiAction(new ResourceLocation(FTBLib.MOD_ID, "allies"), FTBLibLang.TEAM_GUI_ALLIES.textComponent(null), GuiIcons.STAR, 40)
	{
		@Override
		public boolean isAvailable(ForgeTeam team, ForgePlayer player, NBTTagCompound data)
		{
			return team.isModerator(player) && team.universe.getRealPlayers().size() > 1;
		}

		@Override
		public void onAction(ForgeTeam team, ForgePlayer player, NBTTagCompound data)
		{
			if (data.hasNoTags())
			{
				new MessageMyTeamPlayerList(getId(), team, player, ALLIES_PREDICATE).sendTo(player.getPlayer());
			}

			ForgePlayer p = team.universe.getPlayer(data.getString("player"));

			if (p != null && p != player)
			{
				team.setStatus(p, data.getBoolean("add") ? EnumTeamStatus.ALLY : EnumTeamStatus.NONE);
			}
		}
	};

	public static final TeamGuiAction MODERATORS = new TeamGuiAction(new ResourceLocation(FTBLib.MOD_ID, "moderators"), FTBLibLang.TEAM_GUI_MODS.textComponent(null), GuiIcons.SHIELD, 50)
	{
		@Override
		public boolean isAvailable(ForgeTeam team, ForgePlayer player, NBTTagCompound data)
		{
			return team.isOwner(player) && team.getMembers().size() > 1;
		}

		@Override
		public void onAction(ForgeTeam team, ForgePlayer player, NBTTagCompound data)
		{
			if (data.hasNoTags())
			{
				new MessageMyTeamPlayerList(getId(), team, player, MEMBERS_PREDICATE).sendTo(player.getPlayer());
				return;
			}

			ForgePlayer p = team.universe.getPlayer(data.getString("player"));

			if (p != null && p != player)
			{
				team.setStatus(p, data.getBoolean("add") ? EnumTeamStatus.MOD : EnumTeamStatus.NONE);
			}
		}
	};

	public static final TeamGuiAction ENEMIES = new TeamGuiAction(new ResourceLocation(FTBLib.MOD_ID, "enemies"), FTBLibLang.TEAM_GUI_ENEMIES.textComponent(null), GuiIcons.CLOSE, 60)
	{
		@Override
		public boolean isAvailable(ForgeTeam team, ForgePlayer player, NBTTagCompound data)
		{
			return team.isModerator(player) && team.universe.getRealPlayers().size() > 1;
		}

		@Override
		public void onAction(ForgeTeam team, ForgePlayer player, NBTTagCompound data)
		{
			if (data.hasNoTags())
			{
				new MessageMyTeamPlayerList(getId(), team, player, ENEMIES_PREDICATE).sendTo(player.getPlayer());
			}

			ForgePlayer p = team.universe.getPlayer(data.getString("player"));

			if (p != null && p != player)
			{
				team.setStatus(p, data.getBoolean("add") ? EnumTeamStatus.ENEMY : EnumTeamStatus.NONE);
			}
		}
	};

	public static final TeamGuiAction LEAVE = new TeamGuiAction(new ResourceLocation(FTBLib.MOD_ID, "leave"), FTBLibLang.TEAM_GUI_LEAVE.textComponent(null), GuiIcons.REMOVE, 10000)
	{
		@Override
		public boolean isAvailable(ForgeTeam team, ForgePlayer player, NBTTagCompound data)
		{
			return !team.isOwner(player) || team.getMembers().size() <= 1;
		}

		@Override
		public void onAction(ForgeTeam team, ForgePlayer player, NBTTagCompound data)
		{
			team.removeMember(player);
			FTBLibAPI.sendCloseGuiPacket(player.getPlayer());
		}
	}.setRequiresConfirm();

	public static final TeamGuiAction TRANSFER_OWNERSHIP = new TeamGuiAction(new ResourceLocation(FTBLib.MOD_ID, "transfer_ownership"), FTBLibLang.TEAM_GUI_TRANSFER_OWNERSHIP.textComponent(null), GuiIcons.RIGHT, 10000)
	{
		@Override
		public boolean isAvailable(ForgeTeam team, ForgePlayer player, NBTTagCompound data)
		{
			return !LEAVE.isAvailable(team, player, data);
		}

		@Override
		public void onAction(ForgeTeam team, ForgePlayer player, NBTTagCompound data)
		{
			if (data.hasNoTags())
			{
				new MessageMyTeamPlayerList(getId(), team, player, MEMBERS_PREDICATE).sendTo(player.getPlayer());
			}

			ForgePlayer p = team.universe.getPlayer(data.getString("player"));

			if (p != null && p != player)
			{
				team.setStatus(p, EnumTeamStatus.OWNER);
			}
		}
	};
}