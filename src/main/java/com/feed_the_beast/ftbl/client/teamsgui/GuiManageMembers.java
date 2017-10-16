package com.feed_the_beast.ftbl.client.teamsgui;

import com.feed_the_beast.ftbl.api.EnumTeamStatus;
import com.feed_the_beast.ftbl.api_impl.FTBLibTeamGuiActions;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.icon.Color4I;
import com.feed_the_beast.ftbl.lib.internal.FTBLibLang;
import com.feed_the_beast.ftbl.lib.util.ColorUtils;
import com.feed_the_beast.ftbl.lib.util.StringUtils;
import com.feed_the_beast.ftbl.lib.util.misc.MouseButton;
import com.feed_the_beast.ftbl.net.MessageMyTeamAction;
import com.feed_the_beast.ftbl.net.MessageMyTeamPlayerList;
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
			switch (entry.status)
			{
				case NONE:
					break;
				case MEMBER:
				case MOD:
					return ColorUtils.getChatFormattingColor(TextFormatting.DARK_GREEN.ordinal());
				case INVITED:
				case ALLY:
					return ColorUtils.getChatFormattingColor(TextFormatting.BLUE.ordinal());
				case REQUESTING_INVITE:
					return ColorUtils.getChatFormattingColor(TextFormatting.GOLD.ordinal());
			}

			return Color4I.BLACK;
		}

		@Override
		public void addMouseOverText(List<String> list)
		{
			if (!entry.status.isNone())
			{
				list.add(entry.status.getLangKey().translate());
			}

			if (entry.status == EnumTeamStatus.MEMBER)
			{
				list.add(FTBLibLang.TEAM_GUI_MEMBERS_KICK.translate());
			}
			else if (entry.status == EnumTeamStatus.INVITED)
			{
				list.add(FTBLibLang.TEAM_GUI_MEMBERS_CANCEL_INVITE.translate());
			}
			else if (entry.status == EnumTeamStatus.REQUESTING_INVITE)
			{
				list.add(FTBLibLang.TEAM_GUI_MEMBERS_REQUESTING_INVITE.translate());
			}

			if (entry.status == EnumTeamStatus.NONE || entry.status == EnumTeamStatus.REQUESTING_INVITE)
			{
				list.add(FTBLibLang.TEAM_GUI_MEMBERS_INVITE.translate());
			}

			if (entry.status == EnumTeamStatus.REQUESTING_INVITE)
			{
				list.add(FTBLibLang.TEAM_GUI_MEMBERS_DENY_REQUEST.translate());
			}
		}

		@Override
		public void onClicked(MouseButton button)
		{
			GuiHelper.playClickSound();
			NBTTagCompound data = new NBTTagCompound();
			data.setString("player", StringUtils.fromUUID(entry.uuid));

			if (entry.status == EnumTeamStatus.NONE)
			{
				data.setString("action", "invite");
				entry.status = EnumTeamStatus.INVITED;
			}
			else if (entry.status == EnumTeamStatus.MEMBER)
			{
				if (!button.isLeft())
				{
					data.setString("action", "kick");
					entry.status = EnumTeamStatus.REQUESTING_INVITE;
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
			else if (entry.status == EnumTeamStatus.REQUESTING_INVITE)
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