package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.FTBLibModCommon;
import com.feed_the_beast.ftbl.api.config.IConfigContainer;
import com.feed_the_beast.ftbl.lib.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.lib.net.MessageToServer;
import com.feed_the_beast.ftbl.lib.util.LMNetUtils;
import com.feed_the_beast.ftbl.lib.util.LMUtils;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;

public class MessageEditConfigResponse extends MessageToServer<MessageEditConfigResponse>
{
    private JsonObject groupData;
    private NBTTagCompound extraNBT;

    public MessageEditConfigResponse()
    {
    }

    public MessageEditConfigResponse(@Nullable NBTTagCompound nbt, JsonObject json)
    {
        groupData = json;
        extraNBT = nbt;

        if(LMUtils.DEV_ENV)
        {
            LMUtils.DEV_LOGGER.info("TX Response: " + groupData);
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
        groupData = LMNetUtils.readJsonElement(io).getAsJsonObject();
        extraNBT = LMNetUtils.readTag(io);
    }

    @Override
    public void toBytes(ByteBuf io)
    {
        LMNetUtils.writeJsonElement(io, groupData);
        LMNetUtils.writeTag(io, extraNBT);
    }

    @Override
    public void onMessage(MessageEditConfigResponse m, EntityPlayerMP player)
    {
        IConfigContainer cc = FTBLibModCommon.TEMP_SERVER_CONFIG.get(player.getGameProfile().getId());

        if(cc != null)
        {
            if(LMUtils.DEV_ENV)
            {
                LMUtils.DEV_LOGGER.info("RX Response: " + m.groupData);
            }

            cc.saveConfig(player, m.extraNBT, m.groupData);
            FTBLibModCommon.TEMP_SERVER_CONFIG.remove(player.getGameProfile().getId());
        }
    }
}