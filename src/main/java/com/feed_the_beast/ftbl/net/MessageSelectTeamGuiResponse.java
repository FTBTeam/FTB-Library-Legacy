package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.api.IUniverse;
import com.feed_the_beast.ftbl.client.teamsgui.GuiSelectTeam;
import com.feed_the_beast.ftbl.client.teamsgui.TeamInst;
import com.feed_the_beast.ftbl.lib.internal.FTBLibPerms;
import com.feed_the_beast.ftbl.lib.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.lib.net.MessageToClient;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

public class MessageSelectTeamGuiResponse extends MessageToClient<MessageSelectTeamGuiResponse>
{
    private List<TeamInst> teams;

    public MessageSelectTeamGuiResponse()
    {
    }

    public MessageSelectTeamGuiResponse(IUniverse universe, IForgePlayer player)
    {
        teams = new ArrayList<>();

        for(IForgeTeam team : universe.getTeams())
        {
            if(team.hasPermission(player.getProfile().getId(), FTBLibPerms.TEAM_CAN_JOIN))
            {
                teams.add(new TeamInst(universe, team, false));
            }
        }
    }

    @Override
    public LMNetworkWrapper getWrapper()
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
            teams.add(new TeamInst(io));
        }
    }

    @Override
    public void toBytes(ByteBuf io)
    {
        io.writeInt(teams.size());
        for(TeamInst t : teams)
        {
            t.write(io);
        }
    }

    @Override
    public void onMessage(MessageSelectTeamGuiResponse m, EntityPlayer player)
    {
        new GuiSelectTeam(m.teams).openGui();
    }
}