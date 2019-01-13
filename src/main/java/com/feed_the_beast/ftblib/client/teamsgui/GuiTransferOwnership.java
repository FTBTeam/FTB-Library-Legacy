package com.feed_the_beast.ftblib.client.teamsgui;

import com.feed_the_beast.ftblib.lib.data.FTBLibTeamGuiActions;
import com.feed_the_beast.ftblib.lib.gui.GuiHelper;
import com.feed_the_beast.ftblib.lib.gui.Panel;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import com.feed_the_beast.ftblib.net.MessageMyTeamAction;
import com.feed_the_beast.ftblib.net.MessageMyTeamPlayerList;
import net.minecraft.client.Minecraft;
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

			getGui().openYesNo(I18n.format("team_action.ftblib.transfer_ownership") + "?", Minecraft.getMinecraft().getSession().getUsername() + " => " + entry.name, () ->
			{
				getGui().closeGui(false);
				NBTTagCompound data = new NBTTagCompound();
				data.setString("player", entry.name);
				new MessageMyTeamAction(FTBLibTeamGuiActions.TRANSFER_OWNERSHIP.getId(), data).sendToServer();
			});
		}
	}

	public GuiTransferOwnership(Collection<MessageMyTeamPlayerList.Entry> m)
	{
		super(I18n.format("team_action.ftblib.transfer_ownership"), m, ButtonPlayer::new);
	}
}