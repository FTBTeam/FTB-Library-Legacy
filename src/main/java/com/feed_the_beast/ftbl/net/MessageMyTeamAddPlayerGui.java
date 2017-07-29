package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.client.teamsgui.GuiMyTeam;
import com.feed_the_beast.ftbl.client.teamsgui.MyTeamPlayerData;
import com.feed_the_beast.ftbl.lib.net.MessageToClient;
import com.feed_the_beast.ftbl.lib.net.NetworkWrapper;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
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
		return FTBLibNetHandler.NET;
	}

	@Override
	public void fromBytes(ByteBuf io)
	{
		int s = io.readInt();
		players = new ArrayList<>(s);

		while (--s >= 0)
		{
			players.add(new MyTeamPlayerData(io));
		}
	}

	@Override
	public void toBytes(ByteBuf io)
	{
		io.writeInt(players.size());

		for (MyTeamPlayerData d : players)
		{
			d.write(io);
		}
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