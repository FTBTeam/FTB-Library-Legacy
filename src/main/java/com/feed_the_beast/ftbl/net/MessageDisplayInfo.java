package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.lib.gui.misc.GuiInfo;
import com.feed_the_beast.ftbl.lib.info.InfoPage;
import com.feed_the_beast.ftbl.lib.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.lib.net.MessageToClient;
import com.feed_the_beast.ftbl.lib.util.LMNetUtils;
import com.google.gson.JsonElement;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

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
        json = page.getSerializableElement();
    }

    @Override
    public LMNetworkWrapper getWrapper()
    {
        return FTBLibNetHandler.NET;
    }

    @Override
    public void fromBytes(ByteBuf io)
    {
        infoID = LMNetUtils.readString(io);
        json = LMNetUtils.readJsonElement(io);
    }

    @Override
    public void toBytes(ByteBuf io)
    {
        LMNetUtils.writeString(io, infoID);
        LMNetUtils.writeJsonElement(io, json);
    }

    @Override
    public void onMessage(MessageDisplayInfo m, EntityPlayer player)
    {
        InfoPage page = new InfoPage(m.infoID);
        page.fromJson(m.json);
        new GuiInfo(page).openGui();
    }
}