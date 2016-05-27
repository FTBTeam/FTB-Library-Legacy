package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.ForgePlayerMP;
import com.feed_the_beast.ftbl.api.ForgeWorldMP;
import com.feed_the_beast.ftbl.api.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.api.net.MessageToServer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.UUID;

public class MessageModifyTeam extends MessageToServer<MessageModifyTeam>
{
    public static final byte INVITE = 0;
    public static final byte JOIN = 1;
    public static final byte LEAVE = 2;

    public byte actionID;
    public UUID playerID;

    public MessageModifyTeam()
    {
    }

    public MessageModifyTeam(byte a, UUID id)
    {
        actionID = a;
        playerID = id;
    }

    @Override
    public LMNetworkWrapper getWrapper()
    {
        return FTBLibNetHandler.NET;
    }

    @Override
    public void fromBytes(ByteBuf io)
    {
        actionID = io.readByte();
        playerID = readUUID(io);
    }

    @Override
    public void toBytes(ByteBuf io)
    {
        io.writeByte(actionID);
        writeUUID(io, playerID);
    }

    @Override
    public void onMessage(MessageModifyTeam m, EntityPlayerMP ep)
    {
        ForgePlayerMP owner = ForgeWorldMP.inst.getPlayer(ep);
        ForgePlayerMP player = ForgeWorldMP.inst.getPlayer(m.playerID);

        switch(m.actionID)
        {
            case INVITE:
            {
                owner.sendUpdate();
                owner.sendInfoUpdate(owner);

                return;
            }
            case JOIN:
            {
                owner.sendUpdate();
                owner.sendInfoUpdate(owner);

                return;
            }
            case LEAVE:
            {
                if(owner.equalsPlayer(player))
                {
                    owner.sendInfoUpdate(owner);
                }
                else
                {
                }

                return;
            }
        }
    }
}