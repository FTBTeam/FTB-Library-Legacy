package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.client.teamsgui.GuiMyTeam;
import com.feed_the_beast.ftbl.client.teamsgui.MyTeamData;
import com.feed_the_beast.ftbl.lib.io.DataIn;
import com.feed_the_beast.ftbl.lib.io.DataOut;
import com.feed_the_beast.ftbl.lib.net.MessageToClient;
import com.feed_the_beast.ftbl.lib.net.NetworkWrapper;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author LatvianModder
 */
public class MessageMyTeamGui extends MessageToClient<MessageMyTeamGui>
{
	private MyTeamData teamInfo;

	public MessageMyTeamGui()
	{
	}

	public MessageMyTeamGui(IForgeTeam team, IForgePlayer player)
	{
		teamInfo = new MyTeamData(team, player);
	}

	@Override
	public NetworkWrapper getWrapper()
	{
		return FTBLibNetHandler.NET;
	}

	@Override
	public void writeData(DataOut data)
	{
		teamInfo.write(data);
	}

	@Override
	public void readData(DataIn data)
	{
		teamInfo = new MyTeamData(data);
	}

	@Override
	public void onMessage(MessageMyTeamGui m, EntityPlayer player)
	{
		new GuiMyTeam(m.teamInfo).openGui();
	}
}