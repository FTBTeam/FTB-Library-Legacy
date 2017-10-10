package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.EnumTeamStatus;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api_impl.ForgeTeam;
import com.feed_the_beast.ftbl.api_impl.Universe;
import com.feed_the_beast.ftbl.client.teamsgui.GuiSelectTeam;
import com.feed_the_beast.ftbl.client.teamsgui.PublicTeamData;
import com.feed_the_beast.ftbl.lib.io.DataIn;
import com.feed_the_beast.ftbl.lib.io.DataOut;
import com.feed_the_beast.ftbl.lib.net.MessageToClient;
import com.feed_the_beast.ftbl.lib.net.NetworkWrapper;
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

	public MessageSelectTeamGui(IForgePlayer player)
	{
		teams = new ArrayList<>();

		for (ForgeTeam team : Universe.INSTANCE.getTeams())
		{
			teams.add(new PublicTeamData(team, team.hasStatus(player, EnumTeamStatus.INVITED)));
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