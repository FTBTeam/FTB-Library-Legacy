package com.feed_the_beast.ftblib.client.teamsgui;

import com.feed_the_beast.ftblib.FTBLibLang;
import com.feed_the_beast.ftblib.lib.EnumTeamStatus;
import com.feed_the_beast.ftblib.lib.data.FTBLibTeamGuiActions;
import com.feed_the_beast.ftblib.lib.gui.GuiBase;
import com.feed_the_beast.ftblib.lib.gui.GuiHelper;
import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import com.feed_the_beast.ftblib.net.MessageMyTeamAction;
import com.feed_the_beast.ftblib.net.MessageMyTeamPlayerList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;

import java.util.Collection;
import java.util.List;

/**
 * @author LatvianModder
 */
public class GuiManageMembers extends GuiManagePlayersBase
{
	private static class ButtonPlayer extends ButtonPlayerBase
	{
		private ButtonPlayer(GuiBase gui, MessageMyTeamPlayerList.Entry m)
		{
			super(gui, m);
		}

		@Override
		Color4I getPlayerColor()
		{
			if (entry.requestingInvite)
			{
				return Color4I.getChatFormattingColor(TextFormatting.GOLD);
			}

			switch (entry.status)
			{
				case NONE:
					return getDefaultPlayerColor();
				case MEMBER:
				case MOD:
					return Color4I.getChatFormattingColor(TextFormatting.DARK_GREEN);
				case INVITED:
					return Color4I.getChatFormattingColor(TextFormatting.BLUE);
				case ALLY:
					return Color4I.getChatFormattingColor(TextFormatting.DARK_AQUA);
			}

			return getDefaultPlayerColor();
		}

		@Override
		public void addMouseOverText(List<String> list)
		{
			if (!entry.status.isNone())
			{
				list.add(entry.status.getLangKey().translate());
			}
			else if (entry.requestingInvite)
			{
				list.add(FTBLibLang.TEAM_GUI_REQUESTING_INVITE.translate());
			}

			if (entry.requestingInvite)
			{
				list.add(FTBLibLang.TEAM_GUI_MEMBERS_REQUESTING_INVITE.translate());
			}
			else if (entry.status.isEqualOrGreaterThan(EnumTeamStatus.MEMBER))
			{
				list.add(FTBLibLang.TEAM_GUI_MEMBERS_KICK.translate());
			}
			else if (entry.status == EnumTeamStatus.INVITED)
			{
				list.add(FTBLibLang.TEAM_GUI_MEMBERS_CANCEL_INVITE.translate());
			}

			if (entry.status == EnumTeamStatus.NONE || entry.requestingInvite)
			{
				list.add(FTBLibLang.TEAM_GUI_MEMBERS_INVITE.translate());
			}

			if (entry.requestingInvite)
			{
				list.add(FTBLibLang.TEAM_GUI_MEMBERS_DENY_REQUEST.translate());
			}
		}

		@Override
		public void onClicked(MouseButton button)
		{
			GuiHelper.playClickSound();
			NBTTagCompound data = new NBTTagCompound();
			data.setString("player", entry.name);

			if (entry.requestingInvite)
			{
				if (button.isLeft())
				{
					data.setString("action", "invite");
					entry.status = EnumTeamStatus.MEMBER;
				}
				else
				{
					data.setString("action", "deny_request");
					entry.status = EnumTeamStatus.NONE;
				}

				entry.requestingInvite = false;
			}
			else if (entry.status == EnumTeamStatus.NONE)
			{
				data.setString("action", "invite");
				entry.status = EnumTeamStatus.INVITED;
			}
			else if (entry.status.isEqualOrGreaterThan(EnumTeamStatus.MEMBER))
			{
				if (!button.isLeft())
				{
					data.setString("action", "kick");
					entry.requestingInvite = true;
					entry.status = EnumTeamStatus.NONE;
				}
			}
			else if (entry.status == EnumTeamStatus.INVITED)
			{
				if (!button.isLeft())
				{
					data.setString("action", "cancel_invite");
					entry.status = EnumTeamStatus.NONE;
				}
			}

			if (data.hasKey("action"))
			{
				new MessageMyTeamAction(FTBLibTeamGuiActions.MEMBERS.getId(), data).sendToServer();
			}

			updateIcon();
		}
	}

	public GuiManageMembers(Collection<MessageMyTeamPlayerList.Entry> m)
	{
		super(FTBLibLang.TEAM_GUI_MEMBERS.translate(), m, ButtonPlayer::new);
	}
}