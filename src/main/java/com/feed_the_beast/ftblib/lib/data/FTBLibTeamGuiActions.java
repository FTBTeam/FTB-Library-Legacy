package com.feed_the_beast.ftblib.lib.data;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.lib.EnumTeamStatus;
import com.feed_the_beast.ftblib.lib.gui.GuiIcons;
import com.feed_the_beast.ftblib.net.MessageMyTeamPlayerList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

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

	public static final Action CONFIG = new Action(new ResourceLocation(FTBLib.MOD_ID, "config"), new TextComponentTranslation("gui.settings"), GuiIcons.SETTINGS, -100)
	{
		@Override
		public Type getType(ForgePlayer player, NBTTagCompound data)
		{
			return player.team.isModerator(player) ? Type.ENABLED : Type.DISABLED;
		}

		@Override
		public void onAction(ForgePlayer player, NBTTagCompound data)
		{
			FTBLibAPI.editServerConfig(player.getPlayer(), player.team.getSettings(), player.team.configCallback);
		}
	};

	public static final Action INFO = new Action(new ResourceLocation(FTBLib.MOD_ID, "info"), new TextComponentTranslation("gui.info"), GuiIcons.INFO, 0)
	{
		@Override
		public Type getType(ForgePlayer player, NBTTagCompound data)
		{
			return Type.INVISIBLE;
		}

		@Override
		public void onAction(ForgePlayer player, NBTTagCompound data)
		{
			//TODO: Open info gui
		}
	};

	public static final Action MEMBERS = new Action(new ResourceLocation(FTBLib.MOD_ID, "members"), new TextComponentTranslation("ftblib.lang.team.gui.members"), GuiIcons.FRIENDS, 30)
	{
		@Override
		public Type getType(ForgePlayer player, NBTTagCompound data)
		{
			return (player.team.isModerator(player) && player.team.universe.getPlayers().size() > 1) ? Type.ENABLED : Type.DISABLED;
		}

		@Override
		public void onAction(ForgePlayer player, NBTTagCompound data)
		{
			if (data.hasNoTags())
			{
				new MessageMyTeamPlayerList(getId(), player, NO_ENEMIES_PREDICATE).sendTo(player.getPlayer());
				return;
			}

			ForgePlayer p = player.team.universe.getPlayer(data.getString("player"));

			if (p == null || p == player)
			{
				return;
			}

			switch (data.getString("action"))
			{
				case "kick":
				{
					if (player.team.isMember(p))
					{
						player.team.removeMember(p);
						player.team.setRequestingInvite(p, true);
					}

					break;
				}
				case "invite":
				{
					player.team.setStatus(p, EnumTeamStatus.INVITED);

					if (player.team.isRequestingInvite(p))
					{
						if (p.hasTeam())
						{
							player.team.setRequestingInvite(p, false);
						}
						else
						{
							player.team.addMember(p, false);
						}
					}

					break;
				}
				case "cancel_invite":
				{
					if (player.team.getHighestStatus(p) == EnumTeamStatus.INVITED)
					{
						player.team.setStatus(p, EnumTeamStatus.NONE);
					}

					break;
				}
				case "deny_request":
				{
					player.team.setRequestingInvite(p, false);
					break;
				}
			}
		}
	};

	public static final Action ALLIES = new Action(new ResourceLocation(FTBLib.MOD_ID, "allies"), new TextComponentTranslation("ftblib.lang.team.gui.allies"), GuiIcons.STAR, 40)
	{
		@Override
		public Type getType(ForgePlayer player, NBTTagCompound data)
		{
			return (player.team.isModerator(player) && player.team.universe.getPlayers().size() > 1) ? Type.ENABLED : Type.DISABLED;
		}

		@Override
		public void onAction(ForgePlayer player, NBTTagCompound data)
		{
			if (data.hasNoTags())
			{
				new MessageMyTeamPlayerList(getId(), player, ALLIES_PREDICATE).sendTo(player.getPlayer());
			}

			ForgePlayer p = player.team.universe.getPlayer(data.getString("player"));

			if (p != null && p != player)
			{
				player.team.setStatus(p, data.getBoolean("add") ? EnumTeamStatus.ALLY : EnumTeamStatus.NONE);
			}
		}
	};

	public static final Action MODERATORS = new Action(new ResourceLocation(FTBLib.MOD_ID, "moderators"), new TextComponentTranslation("ftblib.lang.team.gui.mods"), GuiIcons.SHIELD, 50)
	{
		@Override
		public Type getType(ForgePlayer player, NBTTagCompound data)
		{
			return (player.team.isOwner(player) && player.team.getMembers().size() > 1) ? Type.ENABLED : Type.DISABLED;
		}

		@Override
		public void onAction(ForgePlayer player, NBTTagCompound data)
		{
			if (data.hasNoTags())
			{
				new MessageMyTeamPlayerList(getId(), player, MEMBERS_PREDICATE).sendTo(player.getPlayer());
				return;
			}

			ForgePlayer p = player.team.universe.getPlayer(data.getString("player"));

			if (p != null && p != player)
			{
				player.team.setStatus(p, data.getBoolean("add") ? EnumTeamStatus.MOD : EnumTeamStatus.NONE);
			}
		}
	};

	public static final Action ENEMIES = new Action(new ResourceLocation(FTBLib.MOD_ID, "enemies"), new TextComponentTranslation("ftblib.lang.team.gui.enemies"), GuiIcons.CLOSE, 60)
	{
		@Override
		public Type getType(ForgePlayer player, NBTTagCompound data)
		{
			return (player.team.isModerator(player) && player.team.universe.getPlayers().size() > 1) ? Type.ENABLED : Type.DISABLED;
		}

		@Override
		public void onAction(ForgePlayer player, NBTTagCompound data)
		{
			if (data.hasNoTags())
			{
				new MessageMyTeamPlayerList(getId(), player, ENEMIES_PREDICATE).sendTo(player.getPlayer());
			}

			ForgePlayer p = player.team.universe.getPlayer(data.getString("player"));

			if (p != null && p != player)
			{
				player.team.setStatus(p, data.getBoolean("add") ? EnumTeamStatus.ENEMY : EnumTeamStatus.NONE);
			}
		}
	};

	public static final Action LEAVE = new Action(new ResourceLocation(FTBLib.MOD_ID, "leave"), new TextComponentTranslation("ftblib.lang.team.gui.leave"), GuiIcons.REMOVE, 10000)
	{
		@Override
		public Type getType(ForgePlayer player, NBTTagCompound data)
		{
			return (!player.team.isOwner(player) || player.team.getMembers().size() <= 1) ? Type.ENABLED : Type.INVISIBLE;
		}

		@Override
		public void onAction(ForgePlayer player, NBTTagCompound data)
		{
			player.team.removeMember(player);
			FTBLibAPI.sendCloseGuiPacket(player.getPlayer());
		}
	}.setRequiresConfirm();

	public static final Action TRANSFER_OWNERSHIP = new Action(new ResourceLocation(FTBLib.MOD_ID, "transfer_ownership"), new TextComponentTranslation("ftblib.lang.team.gui.transfer_ownership"), GuiIcons.RIGHT, 10000)
	{
		@Override
		public Type getType(ForgePlayer player, NBTTagCompound data)
		{
			return (!player.team.isOwner(player) || player.team.getMembers().size() <= 1) ? Type.INVISIBLE : Type.ENABLED;
		}

		@Override
		public void onAction(ForgePlayer player, NBTTagCompound data)
		{
			if (data.hasNoTags())
			{
				new MessageMyTeamPlayerList(getId(), player, MEMBERS_PREDICATE).sendTo(player.getPlayer());
			}

			ForgePlayer p = player.team.universe.getPlayer(data.getString("player"));

			if (p != null && p != player)
			{
				player.team.setStatus(p, EnumTeamStatus.OWNER);
			}
		}
	};
}