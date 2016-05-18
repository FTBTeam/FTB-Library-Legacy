package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.ForgePlayerMP;
import com.feed_the_beast.ftbl.api.ForgeWorldMP;
import com.feed_the_beast.ftbl.api.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.api.net.MessageToServer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.UUID;

public class MessageRequestPlayerInfo extends MessageToServer<MessageRequestPlayerInfo>
{
    public UUID playerID;
    
    public MessageRequestPlayerInfo() { }
    
    public MessageRequestPlayerInfo(UUID player)
    {
        playerID = player;
    }
    
    @Override
    public LMNetworkWrapper getWrapper()
    { return FTBLibNetHandler.NET; }
    
    @Override
    public void fromBytes(ByteBuf io)
    {
        playerID = readUUID(io);
    }
    
    @Override
    public void toBytes(ByteBuf io)
    {
        writeUUID(io, playerID);
    }
    
    @Override
    public void onMessage(MessageRequestPlayerInfo m, EntityPlayerMP ep)
    {
        ForgePlayerMP p = ForgeWorldMP.inst.getPlayer(m.playerID);
        
        if(p != null)
        {
            new MessageLMPlayerInfo(ForgeWorldMP.inst.getPlayer(ep), p).sendTo(ep);
        }
    }
}