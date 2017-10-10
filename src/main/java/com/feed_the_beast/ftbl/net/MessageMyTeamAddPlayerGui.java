package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.client.teamsgui.GuiMyTeam;
import com.feed_the_beast.ftbl.client.teamsgui.MyTeamPlayerData;
import com.feed_the_beast.ftbl.lib.io.DataIn;
import com.feed_the_beast.ftbl.lib.io.DataOut;
import com.feed_the_beast.ftbl.lib.net.MessageToClient;
import com.feed_the_beast.ftbl.lib.net.NetworkWrapper;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Collection;

/**
 * @author LatvianModder
 */
public class MessageMyTeamAddPlayerGui extends MessageToClient<MessageMyTeamAddPlayerGui>
{
	private Collection<MyTeamPlayerData> players;

	public MessageMyTeamAddPlayerGui()
	{
	}

	public MessageMyTeamAddPlayerGui(Collection<MyTeamPlayerData> p)
	{
		players = p;
	}

	@Override
	public NetworkWrapper getWrapper()
	{
		return FTBLibNetHandler.MY_TEAM;
	}

	@Override
	public void writeData(DataOut data)
	{
		data.writeCollection(players, MyTeamPlayerData.SERIALIZER);
	}

	@Override
	public void readData(DataIn data)
	{
		players = data.readCollection(MyTeamPlayerData.DESERIALIZER);
	}

	@Override
	public void onMessage(MessageMyTeamAddPlayerGui m, EntityPlayer player)
	{
		if (GuiMyTeam.INSTANCE != null)
		{
			GuiMyTeam.INSTANCE.loadAllPlayers(m.players);
		}
	}
}