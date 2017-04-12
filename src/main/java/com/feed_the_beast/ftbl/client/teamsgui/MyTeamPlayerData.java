package com.feed_the_beast.ftbl.client.teamsgui;

import com.feed_the_beast.ftbl.api.EnumTeamStatus;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.lib.util.NetUtils;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.UUID;

/**
 * Created by LatvianModder on 05.02.2017.
 */
public class MyTeamPlayerData implements Comparable<MyTeamPlayerData>
{
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

    public MyTeamPlayerData(ByteBuf io)
    {
        playerId = NetUtils.readUUID(io);
        playerName = ByteBufUtils.readUTF8String(io);
        isOnline = io.readBoolean();
        status = EnumTeamStatus.VALUES[io.readByte()];
    }

    public void write(ByteBuf io)
    {
        NetUtils.writeUUID(io, playerId);
        ByteBufUtils.writeUTF8String(io, playerName);
        io.writeBoolean(isOnline);
        io.writeByte(status.ordinal());
    }

    @Override
    public int compareTo(MyTeamPlayerData o)
    {
        int i = o.status.getStatus() - status.getStatus();

        if(i == 0)
        {
            i = Boolean.compare(o.isOnline, isOnline);

            if(i == 0)
            {
                i = playerName.compareToIgnoreCase(o.playerName);
            }
        }

        return i;
    }
}