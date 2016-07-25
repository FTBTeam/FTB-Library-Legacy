package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.config.ConfigContainer;
import com.feed_the_beast.ftbl.api.config.ConfigRegistry;
import com.feed_the_beast.ftbl.api.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.api.net.MessageToServer;
import com.feed_the_beast.ftbl.util.FTBLib;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public class MessageEditConfigResponse extends MessageToServer<MessageEditConfigResponse>
{
    public JsonObject groupData;
    public NBTTagCompound extraNBT;

    public MessageEditConfigResponse()
    {
    }

    public MessageEditConfigResponse(NBTTagCompound nbt, JsonObject json)
    {
        groupData = json;
        extraNBT = nbt;

        if(FTBLib.DEV_ENV)
        {
            FTBLib.dev_logger.info("TX Response: " + groupData);
        }
    }

    @Override
    public LMNetworkWrapper getWrapper()
    {
        return FTBLibNetHandler.NET;
    }

    @Override
    public void fromBytes(ByteBuf io)
    {
        groupData = readJsonElement(io).getAsJsonObject();
        extraNBT = readTag(io);
    }

    @Override
    public void toBytes(ByteBuf io)
    {
        writeJsonElement(io, groupData);
        writeTag(io, extraNBT);
    }

    @Override
    public void onMessage(MessageEditConfigResponse m, EntityPlayerMP ep)
    {
        ConfigContainer cc = ConfigRegistry.tempServerConfig.get(ep.getGameProfile().getId());

        if(cc != null)
        {
            if(FTBLib.DEV_ENV)
            {
                FTBLib.dev_logger.info("RX Response: " + m.groupData);
            }

            cc.saveConfig(ep, m.extraNBT, m.groupData);
            ConfigRegistry.tempServerConfig.remove(ep.getGameProfile().getId());
        }
    }
}