package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.ITeamMessage;
import com.feed_the_beast.ftbl.api_impl.ForgeTeam;
import com.feed_the_beast.ftbl.client.teamsgui.GuiMyTeam;
import com.feed_the_beast.ftbl.lib.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.lib.net.MessageToClient;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class MessageDisplayTeamMsg extends MessageToClient<MessageDisplayTeamMsg>
{
    private ITeamMessage message;

    public MessageDisplayTeamMsg()
    {
    }

    public MessageDisplayTeamMsg(ITeamMessage m)
    {
        message = m;
    }

    @Override
    public LMNetworkWrapper getWrapper()
    {
        return FTBLibNetHandler.NET;
    }

    @Override
    public void fromBytes(ByteBuf io)
    {
        message = new ForgeTeam.Message(io);
    }

    @Override
    public void toBytes(ByteBuf io)
    {
        ForgeTeam.Message.write(io, message);
    }

    @Override
    public void onMessage(MessageDisplayTeamMsg m, EntityPlayer player)
    {
        if(GuiMyTeam.INSTANCE != null)
        {
            GuiMyTeam.INSTANCE.printMessage(m.message);
        }
    }
}