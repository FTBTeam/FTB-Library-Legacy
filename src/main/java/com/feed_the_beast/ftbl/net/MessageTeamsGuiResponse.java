package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.client.teamsgui.GuiTeams;
import com.feed_the_beast.ftbl.client.teamsgui.PlayerInst;
import com.feed_the_beast.ftbl.client.teamsgui.TeamInst;
import com.feed_the_beast.ftbl.lib.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.lib.net.MessageToClient;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.ArrayList;
import java.util.List;

public class MessageTeamsGuiResponse extends MessageToClient<MessageTeamsGuiResponse>
{
    private List<TeamInst> teams;
    private List<PlayerInst> players;

    public MessageTeamsGuiResponse()
    {
    }

    MessageTeamsGuiResponse(EntityPlayer player)
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
        int s = io.readInt();
        teams = new ArrayList<>(s);

        while(--s >= 0)
        {
            String name = ByteBufUtils.readUTF8String(io);
            String displayName = ByteBufUtils.readUTF8String(io);
            String description = ByteBufUtils.readUTF8String(io);
            teams.add(new TeamInst(name, displayName, description));
        }
    }

    @Override
    public void toBytes(ByteBuf io)
    {
    }

    @Override
    public void onMessage(MessageTeamsGuiResponse m, EntityPlayer player)
    {
        new GuiTeams(m.teams, m.players).openGui();
    }
}