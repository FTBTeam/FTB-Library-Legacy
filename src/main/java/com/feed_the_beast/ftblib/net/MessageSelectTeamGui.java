package com.feed_the_beast.ftblib.net;

import com.feed_the_beast.ftblib.client.teamsgui.GuiSelectTeam;
import com.feed_the_beast.ftblib.client.teamsgui.PublicTeamData;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.ftblib.lib.data.Universe;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.net.MessageToClient;
import com.feed_the_beast.ftblib.lib.net.NetworkWrapper;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author LatvianModder
 */
public class MessageSelectTeamGui extends MessageToClient<MessageSelectTeamGui>
{
	private Collection<PublicTeamData> teams;

	public MessageSelectTeamGui()
	{
	}

	public MessageSelectTeamGui(ForgePlayer player)
	{
		teams = new ArrayList<>();

		for (ForgeTeam team : Universe.get().getTeams())
		{
			teams.add(new PublicTeamData(team, team.isInvited(player)));
		}
	}

	@Override
	public NetworkWrapper getWrapper()
	{
		return FTBLibNetHandler.MY_TEAM;
	}

	@Override
	public void writeData(DataOut data)
	{
		data.writeCollection(teams, PublicTeamData.SERIALIZER);
	}

	@Override
	public void readData(DataIn data)
	{
		teams = data.readCollection(null, PublicTeamData.DESERIALIZER);
	}

	@Override
	public void onMessage(MessageSelectTeamGui m, EntityPlayer player)
	{
		new GuiSelectTeam(m.teams).openGuiLater();
	}
}