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
public class GuiManageAllies extends GuiManagePlayersBase
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
			return entry.status.isEqualOrGreaterThan(EnumTeamStatus.ALLY) ? ColorUtils.getChatFormattingColor(TextFormatting.DARK_GREEN.ordinal()) : Color4I.BLACK;
		}

		@Override
		public void addMouseOverText(List<String> list)
		{
			list.add((entry.status.isEqualOrGreaterThan(EnumTeamStatus.ALLY) ? EnumTeamStatus.ALLY : EnumTeamStatus.MEMBER).getLangKey().translate());
		}

		@Override
		public void onClicked(MouseButton button)
		{
			GuiHelper.playClickSound();
			NBTTagCompound data = new NBTTagCompound();
			data.setString("player", StringUtils.fromUUID(entry.uuid));

			if (entry.status.isEqualOrGreaterThan(EnumTeamStatus.ALLY))
			{
				data.setBoolean("add", false);
				entry.status = EnumTeamStatus.NONE;
			}
			else
			{
				data.setBoolean("add", true);
				entry.status = EnumTeamStatus.ALLY;
			}

			new MessageMyTeamAction(FTBLibTeamGuiActions.MODERATORS.getId(), data).sendToServer();
			updateIcon();
		}
	}

	public GuiManageAllies(Collection<MessageMyTeamPlayerList.Entry> m)
	{
		super(FTBLibLang.TEAM_GUI_ALLIES.translate(), m, ButtonPlayer::new);
	}
}