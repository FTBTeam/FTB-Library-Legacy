package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.api.EnumTeamStatus;
import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.lib.config.IConfigCallback;
import com.feed_the_beast.ftbl.lib.gui.GuiIcons;
import com.feed_the_beast.ftbl.lib.gui.GuiLang;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import com.feed_the_beast.ftbl.lib.internal.FTBLibLang;
import com.feed_the_beast.ftbl.lib.util.misc.TeamGuiAction;
import com.feed_the_beast.ftbl.net.MessageMyTeamPlayerList;
import net.minecraft.nbt.NBTTagCompound;

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

	public static final TeamGuiAction CONFIG = new TeamGuiAction(FTBLibFinals.get("config"), FTBLibLang.MY_TEAM_SETTINGS.textComponent(), GuiIcons.SETTINGS, -100)
	{
		@Override
		public boolean isAvailable(IForgeTeam team, IForgePlayer player, NBTTagCompound data)
		{
			return team.isModerator(player);
		}

		@Override
		public void onAction(IForgeTeam team, IForgePlayer player, NBTTagCompound data)
		{
			FTBLibAPI.API.editServerConfig(player.getPlayer(), team.getSettings(), IConfigCallback.DEFAULT);
		}
	};

	public static final TeamGuiAction INFO = new TeamGuiAction(FTBLibFinals.get("info"), GuiLang.INFO.textComponent(), GuiIcons.INFO, 0)
	{
		@Override
		public boolean isAvailable(IForgeTeam team, IForgePlayer player, NBTTagCompound data)
		{
			return true;
		}

		@Override
		public void onAction(IForgeTeam team, IForgePlayer player, NBTTagCompound data)
		{
			//TODO: Open info gui
		}
	};

	public static final TeamGuiAction MEMBERS = new TeamGuiAction(FTBLibFinals.get("members"), FTBLibLang.TEAM_GUI_MEMBERS.textComponent(), GuiIcons.FRIENDS, 30)
	{
		@Override
		public boolean isAvailable(IForgeTeam team, IForgePlayer player, NBTTagCompound data)
		{
			return team.isModerator(player);
		}

		@Override
		public void onAction(IForgeTeam team, IForgePlayer player, NBTTagCompound data)
		{
			if (data.hasNoTags())
			{
				new MessageMyTeamPlayerList(getId(), team, player, NO_ENEMIES_PREDICATE).sendTo(player.getPlayer());
				return;
			}

			IForgePlayer p = FTBLibAPI.API.getUniverse().getPlayer(data.getString("player"));

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
						team.setStatus(p, EnumTeamStatus.REQUESTING_INVITE);
					}

					break;
				}
				case "invite":
				{
					if (!team.isInvited(p))
					{
						team.setStatus(p, EnumTeamStatus.INVITED);
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
					if (team.getHighestStatus(p) == EnumTeamStatus.REQUESTING_INVITE)
					{
						team.setStatus(p, EnumTeamStatus.NONE);
					}
				}
			}
		}
	};

	public static final TeamGuiAction ALLIES = new TeamGuiAction(FTBLibFinals.get("allies"), FTBLibLang.TEAM_GUI_ALLIES.textComponent(), GuiIcons.STAR, 40)
	{
		@Override
		public boolean isAvailable(IForgeTeam team, IForgePlayer player, NBTTagCompound data)
		{
			return team.isModerator(player);
		}

		@Override
		public void onAction(IForgeTeam team, IForgePlayer player, NBTTagCompound data)
		{
			if (data.hasNoTags())
			{
				new MessageMyTeamPlayerList(getId(), team, player, ALLIES_PREDICATE).sendTo(player.getPlayer());
			}

			IForgePlayer p = FTBLibAPI.API.getUniverse().getPlayer(data.getString("player"));

			if (p != null && p != player)
			{
				team.setStatus(p, data.getBoolean("add") ? EnumTeamStatus.ALLY : EnumTeamStatus.NONE);
			}
		}
	};

	public static final TeamGuiAction MODERATORS = new TeamGuiAction(FTBLibFinals.get("moderators"), FTBLibLang.TEAM_GUI_MODS.textComponent(), GuiIcons.SHIELD, 50)
	{
		@Override
		public boolean isAvailable(IForgeTeam team, IForgePlayer player, NBTTagCompound data)
		{
			return team.isOwner(player) && team.getMembers().size() > 1;
		}

		@Override
		public void onAction(IForgeTeam team, IForgePlayer player, NBTTagCompound data)
		{
			if (data.hasNoTags())
			{
				new MessageMyTeamPlayerList(getId(), team, player, MEMBERS_PREDICATE).sendTo(player.getPlayer());
				return;
			}

			IForgePlayer p = FTBLibAPI.API.getUniverse().getPlayer(data.getString("player"));

			if (p != null && p != player)
			{
				team.setStatus(p, data.getBoolean("add") ? EnumTeamStatus.MOD : EnumTeamStatus.NONE);
			}
		}
	};

	public static final TeamGuiAction ENEMIES = new TeamGuiAction(FTBLibFinals.get("enemies"), FTBLibLang.TEAM_GUI_ENEMIES.textComponent(), GuiIcons.CLOSE, 60)
	{
		@Override
		public boolean isAvailable(IForgeTeam team, IForgePlayer player, NBTTagCompound data)
		{
			return team.isModerator(player);
		}

		@Override
		public void onAction(IForgeTeam team, IForgePlayer player, NBTTagCompound data)
		{
			if (data.hasNoTags())
			{
				new MessageMyTeamPlayerList(getId(), team, player, ENEMIES_PREDICATE).sendTo(player.getPlayer());
			}

			IForgePlayer p = FTBLibAPI.API.getUniverse().getPlayer(data.getString("player"));

			if (p != null && p != player)
			{
				team.setStatus(p, data.getBoolean("add") ? EnumTeamStatus.ENEMY : EnumTeamStatus.NONE);
			}
		}
	};

	public static final TeamGuiAction LEAVE = new TeamGuiAction(FTBLibFinals.get("leave"), FTBLibLang.TEAM_GUI_LEAVE.textComponent(), GuiIcons.REMOVE, 10000)
	{
		@Override
		public boolean isAvailable(IForgeTeam team, IForgePlayer player, NBTTagCompound data)
		{
			return !team.isOwner(player) || team.getMembers().size() <= 1;
		}

		@Override
		public void onAction(IForgeTeam team, IForgePlayer player, NBTTagCompound data)
		{
			team.removeMember(player);
			FTBLibAPI.API.sendCloseGuiPacket(player.getPlayer());
		}
	}.setRequiresConfirm();

	public static final TeamGuiAction TRANSFER_OWNERSHIP = new TeamGuiAction(FTBLibFinals.get("transfer_ownership"), FTBLibLang.TEAM_GUI_TRANSFER_OWNERSHIP.textComponent(), GuiIcons.RIGHT, 10000)
	{
		@Override
		public boolean isAvailable(IForgeTeam team, IForgePlayer player, NBTTagCompound data)
		{
			return !LEAVE.isAvailable(team, player, data);
		}

		@Override
		public void onAction(IForgeTeam team, IForgePlayer player, NBTTagCompound data)
		{
			if (data.hasNoTags())
			{
				new MessageMyTeamPlayerList(getId(), team, player, MEMBERS_PREDICATE).sendTo(player.getPlayer());
			}

			IForgePlayer p = FTBLibAPI.API.getUniverse().getPlayer(data.getString("player"));

			if (p != null && p != player)
			{
				team.setStatus(p, EnumTeamStatus.OWNER);
			}
		}
	};
}