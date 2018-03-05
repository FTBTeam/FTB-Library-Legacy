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
public class GuiManageEnemies extends GuiManagePlayersBase
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
			return entry.status == EnumTeamStatus.ENEMY ? Color4I.getChatFormattingColor(TextFormatting.RED) : getDefaultPlayerColor();
		}

		@Override
		public void addMouseOverText(List<String> list)
		{
			list.add((entry.status == EnumTeamStatus.ENEMY ? EnumTeamStatus.ENEMY : EnumTeamStatus.NONE).getLangKey().translate());
		}

		@Override
		public void onClicked(MouseButton button)
		{
			GuiHelper.playClickSound();
			NBTTagCompound data = new NBTTagCompound();
			data.setString("player", entry.name);

			if (entry.status == EnumTeamStatus.ENEMY)
			{
				data.setBoolean("add", false);
				entry.status = EnumTeamStatus.NONE;
			}
			else
			{
				data.setBoolean("add", true);
				entry.status = EnumTeamStatus.ENEMY;
			}

			new MessageMyTeamAction(FTBLibTeamGuiActions.ENEMIES.getId(), data).sendToServer();
			updateIcon();
		}
	}

	public GuiManageEnemies(Collection<MessageMyTeamPlayerList.Entry> m)
	{
		super(FTBLibLang.TEAM_GUI_ENEMIES.translate(), m, ButtonPlayer::new);
	}
}