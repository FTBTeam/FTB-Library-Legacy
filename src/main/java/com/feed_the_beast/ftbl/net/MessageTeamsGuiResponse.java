package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.lib.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.lib.net.MessageToClient;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;

public class MessageTeamsGuiResponse extends MessageToClient<MessageTeamsGuiResponse>
{
    public MessageTeamsGuiResponse()
    {
    }

    MessageTeamsGuiResponse(EntityPlayerMP player)
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
    public void onMessage(MessageTeamsGuiResponse m)
    {
        //new GuiInfo(new InfoTeamsGUI()).openGui();
    }
}