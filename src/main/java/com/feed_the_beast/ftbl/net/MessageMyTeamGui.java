package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.api.IUniverse;
import com.feed_the_beast.ftbl.client.teamsgui.GuiMyTeam;
import com.feed_the_beast.ftbl.client.teamsgui.MyTeamData;
import com.feed_the_beast.ftbl.lib.net.MessageToClient;
import com.feed_the_beast.ftbl.lib.net.NetworkWrapper;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class MessageMyTeamGui extends MessageToClient<MessageMyTeamGui>
{
    private MyTeamData teamInfo;

    public MessageMyTeamGui()
    {
    }

    public MessageMyTeamGui(IUniverse universe, IForgeTeam team, IForgePlayer player)
    {
        teamInfo = new MyTeamData(universe, team, player);
    }

    @Override
    public NetworkWrapper getWrapper()
    {
        return FTBLibNetHandler.NET;
    }

    @Override
    public void fromBytes(ByteBuf io)
    {
        teamInfo = new MyTeamData(io);
    }

    @Override
    public void toBytes(ByteBuf io)
    {
        teamInfo.write(io);
    }

    @Override
    public void onMessage(MessageMyTeamGui m, EntityPlayer player)
    {
        new GuiMyTeam(m.teamInfo).openGui();
    }
}