package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.ForgePlayerMP;
import com.feed_the_beast.ftbl.api.ForgeWorldMP;
import com.feed_the_beast.ftbl.api.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.api.net.MessageToServer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;

public class MessageRequestSelfUpdate extends MessageToServer<MessageRequestSelfUpdate>
{
    public MessageRequestSelfUpdate()
    {
    }

    @Override
    public LMNetworkWrapper getWrapper()
    {
        return FTBLibNetHandler.NET;
    }

    @Override
    public void fromBytes(ByteBuf io)
    {
    }

    @Override
    public void toBytes(ByteBuf io)
    {
    }

    @Override
    public void onMessage(MessageRequestSelfUpdate m, EntityPlayerMP player)
    {
        ForgePlayerMP p = ForgeWorldMP.inst.getPlayer(player);
        new MessageLMPlayerUpdate(p, true).sendTo(player);
    }
}