package com.feed_the_beast.ftbl.client.teamsgui;

import com.feed_the_beast.ftbl.api.EnumTeamStatus;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.api.ITeamMessage;
import com.feed_the_beast.ftbl.api.IUniverse;
import com.feed_the_beast.ftbl.api_impl.ForgeTeam;
import com.feed_the_beast.ftbl.lib.FinalIDObject;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class MyTeamData extends FinalIDObject
{
	public static int unreadMessages = 0;
	public static long lastMessageTime = 0;

	public String displayName, description;
	public MyTeamPlayerData owner;
	public List<MyTeamPlayerData> players;
	public List<ITeamMessage> chatHistory;
	public MyTeamPlayerData me;
	private int myPlayerIndex;

	public MyTeamData(ByteBuf io)
	{
		super(ByteBufUtils.readUTF8String(io));
		displayName = ByteBufUtils.readUTF8String(io);
		description = ByteBufUtils.readUTF8String(io);

		int s = io.readInt();
		players = new ArrayList<>();

		while (--s >= 0)
		{
			MyTeamPlayerData pi = new MyTeamPlayerData(io);
			players.add(pi);

			if (owner == null && pi.status == EnumTeamStatus.OWNER)
			{
				owner = pi;
			}
		}

		me = players.get(io.readInt());
		s = io.readInt();
		chatHistory = new ArrayList<>(s);

		while (--s >= 0)
		{
			chatHistory.add(new ForgeTeam.Message(io));
		}
	}

	public MyTeamData(IUniverse universe, IForgeTeam team, IForgePlayer player)
	{
		super(team.getName());
		displayName = team.getColor().getTextFormatting() + team.getTitle();
		description = team.getDesc();

		players = new ArrayList<>();
		chatHistory = team.getMessages();

		int i = 0;
		for (IForgePlayer p : universe.getPlayers())
		{
			EnumTeamStatus s = team.getHighestStatus(p);

			if (!s.isNone() && (s != EnumTeamStatus.INVITED || team.freeToJoin()))
			{
				MyTeamPlayerData pi = new MyTeamPlayerData(p, s);
				players.add(pi);

				if (owner == null && s == EnumTeamStatus.OWNER)
				{
					owner = pi;
				}

				if (p.equalsPlayer(player))
				{
					me = pi;
					myPlayerIndex = i;
				}

				i++;
			}
		}
	}

	public void write(ByteBuf io)
	{
		ByteBufUtils.writeUTF8String(io, getName());
		ByteBufUtils.writeUTF8String(io, displayName);
		ByteBufUtils.writeUTF8String(io, description);
		io.writeInt(players.size());

		for (MyTeamPlayerData p : players)
		{
			p.write(io);
		}

		io.writeInt(myPlayerIndex);
		io.writeInt(chatHistory.size());

		for (ITeamMessage msg : chatHistory)
		{
			ForgeTeam.Message.write(io, msg);
		}
	}
}