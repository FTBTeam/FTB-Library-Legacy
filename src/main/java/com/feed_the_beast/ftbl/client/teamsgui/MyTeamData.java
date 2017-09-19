package com.feed_the_beast.ftbl.client.teamsgui;

import com.feed_the_beast.ftbl.api.EnumTeamStatus;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.api.IUniverse;
import com.feed_the_beast.ftbl.lib.FinalIDObject;
import com.feed_the_beast.ftbl.lib.io.DataIn;
import com.feed_the_beast.ftbl.lib.io.DataOut;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class MyTeamData extends FinalIDObject
{
	public String displayName, description;
	public List<MyTeamPlayerData> players;
	public MyTeamPlayerData owner;
	public MyTeamPlayerData me;

	public MyTeamData(DataIn data)
	{
		super(data.readString());
		displayName = data.readString();
		description = data.readString();
		players = new ArrayList<>();
		data.readCollection(players, MyTeamPlayerData.DESERIALIZER);
		owner = players.get(data.readUnsignedShort());
		me = players.get(data.readUnsignedShort());
	}

	public MyTeamData(IUniverse universe, IForgeTeam team, IForgePlayer player)
	{
		super(team.getName());
		displayName = team.getColor().getTextFormatting() + team.getTitle();
		description = team.getDesc();

		players = new ArrayList<>();

		int i = 0;
		for (IForgePlayer p : universe.getPlayers())
		{
			EnumTeamStatus s = team.getHighestStatus(p);

			if (!s.isNone() && (s != EnumTeamStatus.INVITED || team.freeToJoin()))
			{
				MyTeamPlayerData pi = new MyTeamPlayerData(p, s);
				players.add(pi);

				if (s == EnumTeamStatus.OWNER)
				{
					owner = pi;
				}

				if (p.equalsPlayer(player))
				{
					me = pi;
				}

				i++;
			}
		}
	}

	public void write(DataOut data)
	{
		data.writeString(getName());
		data.writeString(displayName);
		data.writeString(description);
		data.writeCollection(players, MyTeamPlayerData.SERIALIZER);
		int ownerIndex = -1;
		int myIndex = -1;

		for (int i = 0; i < players.size(); i++)
		{
			MyTeamPlayerData p = players.get(i);

			if (p == me)
			{
				myIndex = i;
			}

			if (p.status == EnumTeamStatus.OWNER)
			{
				ownerIndex = i;
			}
		}

		data.writeInt(ownerIndex);
		data.writeInt(myIndex);
	}
}