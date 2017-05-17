package com.feed_the_beast.ftbl.client.teamsgui;

import com.feed_the_beast.ftbl.api.EnumTeamColor;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.lib.FinalIDObject;
import com.feed_the_beast.ftbl.lib.util.NetUtils;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.UUID;

/**
 * @author LatvianModder
 */
public class PublicTeamData extends FinalIDObject implements Comparable<PublicTeamData>
{
    public final String displayName, description;
    public final EnumTeamColor color;
    public final UUID ownerId;
    public final String ownerName;
    public final boolean isInvited;

    public PublicTeamData(ByteBuf io)
    {
        super(ByteBufUtils.readUTF8String(io));
        displayName = ByteBufUtils.readUTF8String(io);
        description = ByteBufUtils.readUTF8String(io);
        color = EnumTeamColor.VALUES[io.readUnsignedByte()];
        ownerId = NetUtils.readUUID(io);
        ownerName = ByteBufUtils.readUTF8String(io);
        isInvited = io.readBoolean();
    }

    public PublicTeamData(IForgeTeam team, boolean c)
    {
        super(team.getName());
        displayName = team.getTitle();
        description = team.getDesc();
        color = team.getColor();
        ownerId = team.getOwner().getId();
        ownerName = team.getOwner().getName();
        isInvited = c;
    }

    public void write(ByteBuf io)
    {
        ByteBufUtils.writeUTF8String(io, getName());
        ByteBufUtils.writeUTF8String(io, displayName);
        ByteBufUtils.writeUTF8String(io, description);
        io.writeByte(color.ordinal());
        NetUtils.writeUUID(io, ownerId);
        ByteBufUtils.writeUTF8String(io, ownerName);
        io.writeBoolean(isInvited);
    }

    @Override
    public int compareTo(PublicTeamData o)
    {
        int i = Boolean.compare(o.isInvited, isInvited);

        if(i == 0)
        {
            i = displayName.compareToIgnoreCase(o.displayName);
        }

        return i;
    }
}