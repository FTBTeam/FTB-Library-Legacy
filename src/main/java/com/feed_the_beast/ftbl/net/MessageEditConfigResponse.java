package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.config.ConfigContainer;
import com.feed_the_beast.ftbl.api.config.ConfigGroup;
import com.feed_the_beast.ftbl.api.config.ConfigRegistry;
import com.feed_the_beast.ftbl.api.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.api.net.MessageToServer;
import com.feed_the_beast.ftbl.util.FTBLib;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class MessageEditConfigResponse extends MessageToServer<MessageEditConfigResponse>
{
    public ResourceLocation id;
    public NBTTagCompound groupData;
    public NBTTagCompound extraNBT;

    public MessageEditConfigResponse()
    {
    }

    public MessageEditConfigResponse(ResourceLocation r, NBTTagCompound nbt, ConfigGroup group)
    {
        id = r;
        groupData = new NBTTagCompound();
        group.writeToNBT(groupData, false);
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
        groupData = readTag(io);
        extraNBT = readTag(io);
    }

    @Override
    public void toBytes(ByteBuf io)
    {
        writeResourceLocation(io, id);
        writeTag(io, groupData);
        writeTag(io, extraNBT);
    }

    @Override
    public void onMessage(MessageEditConfigResponse m, EntityPlayerMP ep)
    {
        if(FTBLib.DEV_ENV)
        {
            FTBLib.dev_logger.info("RX Response: " + m.id + " :: " + m.groupData);
        }

        ConfigContainer cc = ConfigRegistry.tempServerConfig.get(ep.getGameProfile().getId());

        if(cc != null && cc.getResourceLocation().equals(m.id))
        {
            ConfigGroup group = new ConfigGroup();
            group.readFromNBT(m.groupData, false);
            cc.saveConfig(ep, m.extraNBT, group);
            ConfigRegistry.tempServerConfig.remove(ep.getGameProfile().getId());
        }
    }
}