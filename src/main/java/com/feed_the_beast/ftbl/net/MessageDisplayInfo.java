package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.FTBLibMod;
import com.feed_the_beast.ftbl.lib.info.InfoPage;
import com.feed_the_beast.ftbl.lib.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.lib.net.MessageToClient;
import com.feed_the_beast.ftbl.lib.util.LMNetUtils;
import com.google.gson.JsonElement;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class MessageDisplayInfo extends MessageToClient<MessageDisplayInfo>
{
    private String infoID;
    private JsonElement json;

    public MessageDisplayInfo()
    {
    }

    public MessageDisplayInfo(InfoPage page)
    {
        infoID = page.getName();
        json = page.toJson();
    }

    @Override
    public LMNetworkWrapper getWrapper()
    {
        return FTBLibNetHandler.NET;
    }

    @Override
    public void fromBytes(ByteBuf io)
    {
        infoID = ByteBufUtils.readUTF8String(io);
        json = LMNetUtils.readJsonElement(io);
    }

    @Override
    public void toBytes(ByteBuf io)
    {
        ByteBufUtils.writeUTF8String(io, infoID);
        LMNetUtils.writeJsonElement(io, json);
    }

    @Override
    public void onMessage(MessageDisplayInfo m, EntityPlayer player)
    {
        FTBLibMod.PROXY.displayInfoGui(new InfoPage(m.infoID, null, m.json));
    }
}