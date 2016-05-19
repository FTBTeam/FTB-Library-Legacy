package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.info.InfoPage;
import com.feed_the_beast.ftbl.api.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.api.net.MessageToClient;
import com.feed_the_beast.ftbl.gui.info.GuiInfo;
import com.google.gson.JsonElement;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageDisplayInfo extends MessageToClient<MessageDisplayInfo>
{
    public String infoID;
    public JsonElement json;

    public MessageDisplayInfo()
    {
    }

    public MessageDisplayInfo(InfoPage page)
    {
        infoID = page.getID();
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
        infoID = readString(io);
        json = readJsonElement(io);
    }

    @Override
    public void toBytes(ByteBuf io)
    {
        writeString(io, infoID);
        writeJsonElement(io, json);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onMessage(MessageDisplayInfo m, Minecraft mc)
    {
        InfoPage page = new InfoPage(m.infoID);
        page.fromJson(m.json);
        FTBLibClient.openGui(new GuiInfo(null, page));
    }
}