package com.feed_the_beast.ftblib.client.teamsgui;

import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.feed_the_beast.ftblib.lib.data.FTBLibTeamGuiActions;
import com.feed_the_beast.ftblib.lib.gui.GuiHelper;
import com.feed_the_beast.ftblib.lib.gui.Panel;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import com.feed_the_beast.ftblib.net.MessageMyTeamAction;
import com.feed_the_beast.ftblib.net.MessageMyTeamPlayerList;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.resources.I18n;
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
		private ButtonPlayer(Panel panel, MessageMyTeamPlayerList.Entry m)
		{
			super(panel, m);
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

				getGui().closeGui(!result);
			}, I18n.format("ftblib.lang.team.gui.transfer_ownership") + "?", ClientUtils.MC.getSession().getUsername() + " => " + entry.name, 0));
		}
	}

	public GuiTransferOwnership(Collection<MessageMyTeamPlayerList.Entry> m)
	{
		super(I18n.format("ftblib.lang.team.gui.transfer_ownership"), m, ButtonPlayer::new);
	}
}