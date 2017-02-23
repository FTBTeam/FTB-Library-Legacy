package com.feed_the_beast.ftbl.client.teamsgui;

import com.feed_the_beast.ftbl.api.EnumTeamStatus;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.lib.internal.FTBLibStats;
import com.feed_the_beast.ftbl.lib.util.LMNetUtils;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.UUID;

/**
 * Created by LatvianModder on 05.02.2017.
 */
public class PlayerInst
{
    public static final PlayerInst ADD_NEW = new PlayerInst("Add New");

    public final GameProfile profile;
    public final long lastSeen;
    public final String displayName;
    public EnumTeamStatus status;

    private PlayerInst(String name)
    {
        profile = new GameProfile(new UUID(0, 0), name);
        lastSeen = 0;
        displayName = name;
        status = EnumTeamStatus.NONE;
    }

    PlayerInst(IForgePlayer player, EnumTeamStatus s)
    {
        profile = player.getProfile();
        lastSeen = player.isOnline() ? 0L : (System.currentTimeMillis() - FTBLibStats.getLastSeen(player.stats(), false));
        displayName = player.getProfile().getName();
        status = s;
    }

    PlayerInst(ByteBuf io)
    {
        UUID id = LMNetUtils.readUUID(io);
        String name = ByteBufUtils.readUTF8String(io);
        profile = new GameProfile(id, name);
        lastSeen = io.readLong();
        displayName = ByteBufUtils.readUTF8String(io);
        status = EnumTeamStatus.VALUES[io.readByte()];
    }

    public void write(ByteBuf io)
    {
        LMNetUtils.writeUUID(io, profile.getId());
        ByteBufUtils.writeUTF8String(io, profile.getName());
        io.writeLong(lastSeen);
        ByteBufUtils.writeUTF8String(io, displayName);
        io.writeByte(status.ordinal());
    }
}