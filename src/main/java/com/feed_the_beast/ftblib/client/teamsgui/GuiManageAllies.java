package com.feed_the_beast.ftblib.client.teamsgui;

import com.feed_the_beast.ftblib.lib.EnumTeamStatus;
import com.feed_the_beast.ftblib.lib.data.FTBLibTeamGuiActions;
import com.feed_the_beast.ftblib.lib.gui.GuiHelper;
import com.feed_the_beast.ftblib.lib.gui.Panel;
import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import com.feed_the_beast.ftblib.net.MessageMyTeamAction;
import com.feed_the_beast.ftblib.net.MessageMyTeamPlayerList;
import net.minecraft.client.resources.I18n;
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
		private ButtonPlayer(Panel panel, MessageMyTeamPlayerList.Entry m)
		{
			super(panel, m);
		}

		@Override
		Color4I getPlayerColor()
		{
			return entry.status.isEqualOrGreaterThan(EnumTeamStatus.ALLY) ? Color4I.getChatFormattingColor(TextFormatting.DARK_AQUA) : getDefaultPlayerColor();
		}

		@Override
		public void addMouseOverText(List<String> list)
		{
			list.add(I18n.format((entry.status.isEqualOrGreaterThan(EnumTeamStatus.ALLY) ? EnumTeamStatus.ALLY : EnumTeamStatus.MEMBER).getLangKey()));
		}

		@Override
		public void onClicked(MouseButton button)
		{
			GuiHelper.playClickSound();
			NBTTagCompound data = new NBTTagCompound();
			data.setString("player", entry.name);

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

			new MessageMyTeamAction(FTBLibTeamGuiActions.ALLIES.getId(), data).sendToServer();
			updateIcon();
		}
	}

	public GuiManageAllies(Collection<MessageMyTeamPlayerList.Entry> m)
	{
		super(I18n.format("ftblib.lang.team.gui.allies"), m, ButtonPlayer::new);
	}
}