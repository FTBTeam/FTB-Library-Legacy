package com.feed_the_beast.ftblib.client.teamsgui;

import com.feed_the_beast.ftblib.FTBLibLang;
import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.feed_the_beast.ftblib.lib.data.FTBLibTeamGuiActions;
import com.feed_the_beast.ftblib.lib.gui.GuiBase;
import com.feed_the_beast.ftblib.lib.gui.GuiHelper;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import com.feed_the_beast.ftblib.net.MessageMyTeamAction;
import com.feed_the_beast.ftblib.net.MessageMyTeamPlayerList;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Collection;
import java.util.List;

/**
 * @author LatvianModder
 */
public class GuiTransferOwnership extends GuiManagePlayersBase
{
	private static class ButtonPlayer extends ButtonPlayerBase
	{
		private ButtonPlayer(GuiBase gui, MessageMyTeamPlayerList.Entry m)
		{
			super(gui, m);
		}

		@Override
		public void addMouseOverText(List<String> list)
		{
		}

		@Override
		public void onClicked(MouseButton button)
		{
			GuiHelper.playClickSound();

			ClientUtils.MC.displayGuiScreen(new GuiYesNo((result, id) ->
			{
				if (result)
				{
					NBTTagCompound data = new NBTTagCompound();
					data.setString("player", entry.name);
					new MessageMyTeamAction(FTBLibTeamGuiActions.TRANSFER_OWNERSHIP.getId(), data).sendToServer();
				}

				gui.closeGui(!result);
			}, FTBLibLang.TEAM_GUI_TRANSFER_OWNERSHIP.translate() + "?", ClientUtils.MC.getSession().getUsername() + " => " + entry.name, 0));
		}
	}

	public GuiTransferOwnership(Collection<MessageMyTeamPlayerList.Entry> m)
	{
		super(FTBLibLang.TEAM_GUI_TRANSFER_OWNERSHIP.translate(), m, ButtonPlayer::new);
	}
}