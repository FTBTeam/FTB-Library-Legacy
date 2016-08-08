package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.info.impl.InfoPage;
import com.feed_the_beast.ftbl.api.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.api.net.MessageToClient;
import com.feed_the_beast.ftbl.gui.info.GuiInfo;
import com.feed_the_beast.ftbl.util.LMNetUtils;
import com.google.gson.JsonElement;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.AbstractMap;

public class MessageDisplayInfo extends MessageToClient<MessageDisplayInfo>
{
    public String infoID;
    public JsonElement json;

    public MessageDisplayInfo()
    {
    }

    public MessageDisplayInfo(String id, InfoPage page)
    {
        infoID = id;
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
    @SideOnly(Side.CLIENT)
    public void onMessage(MessageDisplayInfo m, Minecraft mc)
    {
        InfoPage page = new InfoPage();
        page.fromJson(m.json);
        new GuiInfo(null, new AbstractMap.SimpleEntry<>(m.infoID, page)).openGui();
    }
}