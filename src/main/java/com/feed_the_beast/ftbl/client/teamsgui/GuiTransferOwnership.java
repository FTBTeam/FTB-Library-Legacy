package com.feed_the_beast.ftbl.client.teamsgui;

import com.feed_the_beast.ftbl.api_impl.FTBLibTeamGuiActions;
import com.feed_the_beast.ftbl.lib.client.ClientUtils;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.icon.Color4I;
import com.feed_the_beast.ftbl.lib.internal.FTBLibLang;
import com.feed_the_beast.ftbl.lib.util.StringUtils;
import com.feed_the_beast.ftbl.lib.util.misc.MouseButton;
import com.feed_the_beast.ftbl.net.MessageMyTeamAction;
import com.feed_the_beast.ftbl.net.MessageMyTeamPlayerList;
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
		Color4I getPlayerColor()
		{
			return Color4I.BLACK;
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
					data.setString("player", StringUtils.fromUUID(entry.uuid));
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