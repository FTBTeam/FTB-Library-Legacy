package com.feed_the_beast.ftbl.client.teamsgui;

import com.feed_the_beast.ftbl.FTBLibModCommon;
import com.feed_the_beast.ftbl.api.EnumTeamStatus;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.api.IUniverse;
import com.feed_the_beast.ftbl.api_impl.ForgeTeam;
import com.feed_the_beast.ftbl.lib.FinalIDObject;
import com.feed_the_beast.ftbl.lib.internal.FTBLibPerms;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Created by LatvianModder on 05.02.2017.
 */
public class TeamInst extends FinalIDObject
{
    public String displayName, description;
    public PlayerInst owner;
    public List<PlayerInst> players;
    public List<ForgeTeam.Message> chatHistory;

    public TeamInst(ByteBuf io)
    {
        super(ByteBufUtils.readUTF8String(io));
        displayName = ByteBufUtils.readUTF8String(io);
        description = ByteBufUtils.readUTF8String(io);

        int s = io.readInt();
        players = new ArrayList<>();

        while(--s >= 0)
        {
            PlayerInst pi = new PlayerInst(io);
            players.add(pi);

            if(owner == null && pi.status == EnumTeamStatus.OWNER)
            {
                owner = pi;
            }
        }

        s = io.readInt();
        chatHistory = new ArrayList<>(s);

        while(--s >= 0)
        {
            chatHistory.add(new ForgeTeam.Message(io));
        }
    }

    public TeamInst(IUniverse universe, IForgeTeam team, IForgePlayer player, boolean privateData)
    {
        super(team.getName());
        displayName = team.getColor().getTextFormatting() + team.getTitle();
        description = team.getDesc();

        players = new ArrayList<>();
        chatHistory = new ArrayList<>();

        if(!privateData)
        {
            owner = new PlayerInst(team, team.getOwner(), EnumTeamStatus.OWNER, Collections.emptyList());
            players.add(owner);
            return;
        }

        UUID playerId = player.getProfile().getId();

        Collection<String> perms = team.hasPermission(playerId, FTBLibPerms.TEAM_EDIT_PERMISSIONS) ? FTBLibModCommon.VISIBLE_TEAM_PLAYER_PERMISSIONS : Collections.emptyList();
/* new ArrayList<>();
        if(perms.isEmpty())
        {
            if(team.hasPermission(playerId, FTBLibPerms.TEAM_MANAGE_ALLIES))
            {
                perms.add(FTBLibPerms.TEAM_IS_ALLY);
            }

            if(team.hasPermission(playerId, FTBLibPerms.TEAM_MANAGE_ENEMIES))
            {
                perms.add(FTBLibPerms.TEAM_IS_ENEMY);
            }
        }*/

        for(IForgePlayer p : universe.getPlayers())
        {
            EnumTeamStatus s = team.getHighestStatus(p);

            if(s != EnumTeamStatus.NONE)
            {
                PlayerInst pi = new PlayerInst(team, p, s, perms);
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

        io.writeInt(chatHistory.size());

        for(ForgeTeam.Message msg : chatHistory)
        {
            msg.write(io);
        }
    }
}