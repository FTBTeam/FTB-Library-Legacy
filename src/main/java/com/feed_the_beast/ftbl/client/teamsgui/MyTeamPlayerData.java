package com.feed_the_beast.ftbl.client.teamsgui;

import com.feed_the_beast.ftbl.api.EnumTeamStatus;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.lib.io.DataIn;
import com.feed_the_beast.ftbl.lib.io.DataOut;

import java.util.UUID;

/**
 * @author LatvianModder
 */
public class MyTeamPlayerData implements Comparable<MyTeamPlayerData>
{
	public static final DataOut.Serializer<MyTeamPlayerData> SERIALIZER = (data, d) ->
	{
		data.writeUUID(d.playerId);
		data.writeString(d.playerName);
		data.writeBoolean(d.isOnline);
		data.writeByte(d.status.ordinal());
	};

	public static final DataIn.Deserializer<MyTeamPlayerData> DESERIALIZER = MyTeamPlayerData::new;

	public final UUID playerId;
	public final String playerName;
	public boolean isOnline;
	public EnumTeamStatus status;

	public MyTeamPlayerData(IForgePlayer player, EnumTeamStatus s)
	{
		playerId = player.getId();
		playerName = player.getName();
		isOnline = player.isOnline();
		status = s;
	}

	public MyTeamPlayerData(DataIn data)
	{
		playerId = data.readUUID();
		playerName = data.readString();
		isOnline = data.readBoolean();
		status = EnumTeamStatus.VALUES[data.readUnsignedByte()];
	}

	@Override
	public int compareTo(MyTeamPlayerData o)
	{
		int i = o.status.getStatus() - status.getStatus();

		if (i == 0)
		{
			i = Boolean.compare(o.isOnline, isOnline);

			if (i == 0)
			{
				i = playerName.compareToIgnoreCase(o.playerName);
			}
		}

		return i;
	}
}