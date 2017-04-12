package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.EnumTeamStatus;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.api.IUniverse;
import com.feed_the_beast.ftbl.client.teamsgui.GuiSelectTeam;
import com.feed_the_beast.ftbl.client.teamsgui.PublicTeamData;
import com.feed_the_beast.ftbl.lib.net.MessageToClient;
import com.feed_the_beast.ftbl.lib.net.NetworkWrapper;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

public class MessageSelectTeamGui extends MessageToClient<MessageSelectTeamGui>
{
    private List<PublicTeamData> teams;

    public MessageSelectTeamGui()
    {
    }

    public MessageSelectTeamGui(IUniverse universe, IForgePlayer player)
    {
        teams = new ArrayList<>();

        for(IForgeTeam team : universe.getTeams())
        {
            teams.add(new PublicTeamData(team, team.hasStatus(player, EnumTeamStatus.INVITED)));
        }
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
        teams = new ArrayList<>(s);
        while(--s >= 0)
        {
            teams.add(new PublicTeamData(io));
        }
    }

    @Override
    public void toBytes(ByteBuf io)
    {
        io.writeInt(teams.size());
        for(PublicTeamData t : teams)
        {
            t.write(io);
        }
    }

    @Override
    public void onMessage(MessageSelectTeamGui m, EntityPlayer player)
    {
        new GuiSelectTeam(m.teams).openGui();
    }
}