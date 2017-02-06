package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.lib.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.lib.net.MessageToServer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class MessageTeamsGuiRequest extends MessageToServer<MessageTeamsGuiRequest>
{
    public MessageTeamsGuiRequest()
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
    public void onMessage(MessageTeamsGuiRequest m, EntityPlayer player)
    {
        new MessageTeamsGuiResponse(player).sendTo(player);
    }
}