package com.feed_the_beast.ftbl.client.teamsgui;

import com.feed_the_beast.ftbl.api.EnumTeamStatus;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.api.IUniverse;
import com.feed_the_beast.ftbl.lib.FinalIDObject;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LatvianModder on 05.02.2017.
 */
public class TeamInst extends FinalIDObject
{
    public String displayName, description;
    public PlayerInst owner;
    public List<PlayerInst> players;

    public TeamInst(ByteBuf io)
    {
        super(ByteBufUtils.readUTF8String(io));
        displayName = ByteBufUtils.readUTF8String(io);
        description = ByteBufUtils.readUTF8String(io);

        int p = io.readInt();
        players = new ArrayList<>();

        while(--p >= 0)
        {
            PlayerInst pi = new PlayerInst(io);
            players.add(pi);

            if(owner == null && pi.status == EnumTeamStatus.OWNER)
            {
                owner = pi;
            }
        }
    }

    public TeamInst(IUniverse universe, IForgeTeam team)
    {
        super(team.getName());
        displayName = team.getColor().getTextFormatting() + team.getTitle();
        description = team.getDesc();

        players = new ArrayList<>();

        for(IForgePlayer p : universe.getPlayers())
        {
            EnumTeamStatus s = team.getHighestStatus(p);

            if(s != EnumTeamStatus.NONE)
            {
                PlayerInst pi = new PlayerInst(p, s);
                players.add(pi);

                if(owner == null && s == EnumTeamStatus.OWNER)
                {
                    owner = pi;
                }
            }
        }
    }

    public void write(ByteBuf io)
    {
        ByteBufUtils.writeUTF8String(io, getName());
        ByteBufUtils.writeUTF8String(io, displayName);
        ByteBufUtils.writeUTF8String(io, description);
        io.writeInt(players.size());

        for(PlayerInst p : players)
        {
            p.write(io);
        }
    }
}