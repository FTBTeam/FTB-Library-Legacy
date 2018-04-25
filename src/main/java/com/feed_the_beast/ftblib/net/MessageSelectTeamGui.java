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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author LatvianModder
 */
public class MessageSelectTeamGui extends MessageToClient<MessageSelectTeamGui>
{
	private Collection<PublicTeamData> teams;
	private boolean canCreate;

	public MessageSelectTeamGui()
	{
	}

	public MessageSelectTeamGui(ForgePlayer player, boolean c)
	{
		teams = new ArrayList<>();

		for (ForgeTeam team : Universe.get().getTeams())
		{
			PublicTeamData.Type type = PublicTeamData.Type.NEEDS_INVITE;

			if (team.isEnemy(player))
			{
				type = PublicTeamData.Type.ENEMY;
			}
			else if (team.isInvited(player))
			{
				type = PublicTeamData.Type.CAN_JOIN;
			}
			else if (team.isRequestingInvite(player))
			{
				type = PublicTeamData.Type.REQUESTING_INVITE;
			}

			teams.add(new PublicTeamData(team, type));
		}

		canCreate = c;
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
		data.writeBoolean(canCreate);
	}

	@Override
	public void readData(DataIn data)
	{
		teams = data.readCollection(null, PublicTeamData.DESERIALIZER);
		canCreate = data.readBoolean();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onMessage()
	{
		new GuiSelectTeam(teams, canCreate).openGui();
	}
}