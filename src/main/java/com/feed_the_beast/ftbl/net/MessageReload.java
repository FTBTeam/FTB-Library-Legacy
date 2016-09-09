package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.FTBLibLang;
import com.feed_the_beast.ftbl.FTBLibMod;
import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.events.ReloadEvent;
import com.feed_the_beast.ftbl.api.events.ReloadType;
import com.feed_the_beast.ftbl.api.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.api.net.MessageToClient;
import com.feed_the_beast.ftbl.api_impl.FTBLibAPI_Impl;
import com.latmod.lib.util.LMNetUtils;
import com.latmod.lib.util.LMUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Map;

public class MessageReload extends MessageToClient<MessageReload>
{
    private int typeID;
    private NBTTagCompound syncData;
    private NBTTagCompound sharedDataTag;

    public MessageReload()
    {
    }

    public MessageReload(ReloadType t)
    {
        typeID = t.ordinal();
        syncData = new NBTTagCompound();

        for(Map.Entry<ResourceLocation, INBTSerializable<NBTTagCompound>> entry : FTBLibAPI.get().getRegistries().syncedData().getEntrySet())
        {
            syncData.setTag(entry.getKey().toString(), entry.getValue().serializeNBT());
        }

        sharedDataTag = FTBLibAPI.get().getSharedData(Side.SERVER).serializeNBT();

        if(LMUtils.DEV_ENV)
        {
            LMUtils.DEV_LOGGER.info("Sending sync data: " + syncData);
        }
    }

    @Override
    public LMNetworkWrapper getWrapper()
    {
        return FTBLibNetHandler.NET;
    }

    @Override
    public void toBytes(ByteBuf io)
    {
        io.writeByte(typeID);
        LMNetUtils.writeTag(io, syncData);
        LMNetUtils.writeTag(io, sharedDataTag);
    }

    @Override
    public void fromBytes(ByteBuf io)
    {
        typeID = io.readUnsignedByte();
        syncData = LMNetUtils.readTag(io);
        sharedDataTag = LMNetUtils.readTag(io);
    }

    @Override
    public void onMessage(MessageReload m)
    {
        long ms = System.currentTimeMillis();
        Minecraft mc = Minecraft.getMinecraft();

        FTBLibAPI.get().getSharedData(Side.CLIENT).deserializeNBT(m.sharedDataTag);

        for(String s : m.syncData.getKeySet())
        {
            INBTSerializable<NBTTagCompound> nbt = FTBLibAPI.get().getRegistries().syncedData().get(new ResourceLocation(s));

            if(nbt != null)
            {
                nbt.deserializeNBT(m.syncData.getCompoundTag(s));
            }
        }

        ReloadType type = ReloadType.values()[m.typeID];

        //TODO: new EventFTBWorldClient(ForgeWorldSP.inst).post();

        if(type.reload(Side.CLIENT))
        {
            FTBLibAPI_Impl.get().reloadPackModes();
            MinecraftForge.EVENT_BUS.post(new ReloadEvent(Side.CLIENT, mc.thePlayer, type));

            if(type != ReloadType.LOGIN)
            {
                FTBLibLang.RELOAD_CLIENT.printChat(mc.thePlayer, (System.currentTimeMillis() - ms) + "ms");
            }

            FTBLibMod.logger.info("Current Mode: " + FTBLibAPI.get().getSharedData(Side.CLIENT).getPackMode().getID());
        }
    }
}