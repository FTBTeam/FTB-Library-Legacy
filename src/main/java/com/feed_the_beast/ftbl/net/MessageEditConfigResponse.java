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
import net.minecraft.util.ResourceLocation;

public class MessageEditConfigResponse extends MessageToServer<MessageEditConfigResponse>
{
    public ResourceLocation id;
    public JsonObject groupData;
    public NBTTagCompound extraNBT;

    public MessageEditConfigResponse()
    {
    }

    public MessageEditConfigResponse(ResourceLocation r, NBTTagCompound nbt, JsonObject json)
    {
        id = r;
        groupData = json;
        extraNBT = nbt;

        if(FTBLib.DEV_ENV)
        {
            FTBLib.dev_logger.info("TX Response: " + id + " :: " + groupData);
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
        id = readResourceLocation(io);
        groupData = readJsonElement(io).getAsJsonObject();
        extraNBT = readTag(io);
    }

    @Override
    public void toBytes(ByteBuf io)
    {
        writeResourceLocation(io, id);
        writeJsonElement(io, groupData);
        writeTag(io, extraNBT);
    }

    @Override
    public void onMessage(MessageEditConfigResponse m, EntityPlayerMP ep)
    {
        ConfigContainer cc = ConfigRegistry.tempServerConfig.get(ep.getGameProfile().getId());

        if(cc != null && cc.getResourceLocation().equals(m.id))
        {
            if(FTBLib.DEV_ENV)
            {
                FTBLib.dev_logger.info("RX Response: " + m.id + " :: " + m.groupData);
            }

            cc.saveConfig(ep, m.extraNBT, m.groupData);
            ConfigRegistry.tempServerConfig.remove(ep.getGameProfile().getId());
        }
    }
}