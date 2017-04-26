package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.FTBLibMod;
import com.feed_the_beast.ftbl.lib.guide.GuidePage;
import com.feed_the_beast.ftbl.lib.net.MessageToClient;
import com.feed_the_beast.ftbl.lib.net.NetworkWrapper;
import com.feed_the_beast.ftbl.lib.util.NetUtils;
import com.google.gson.JsonElement;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class MessageDisplayGuide extends MessageToClient<MessageDisplayGuide>
{
    private String id;
    private JsonElement json;

    public MessageDisplayGuide()
    {
    }

    public MessageDisplayGuide(GuidePage page)
    {
        id = page.getName();
        json = page.toJson();
    }

    @Override
    public NetworkWrapper getWrapper()
    {
        return FTBLibNetHandler.NET;
    }

    @Override
    public void fromBytes(ByteBuf io)
    {
        id = ByteBufUtils.readUTF8String(io);
        json = NetUtils.readJsonElement(io);
    }

    @Override
    public void toBytes(ByteBuf io)
    {
        ByteBufUtils.writeUTF8String(io, id);
        NetUtils.writeJsonElement(io, json);
    }

    @Override
    public void onMessage(MessageDisplayGuide m, EntityPlayer player)
    {
        FTBLibMod.PROXY.displayGuide(new GuidePage(m.id, null, m.json));
    }
}