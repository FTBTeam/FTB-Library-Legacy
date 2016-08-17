package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.FTBLibLang;
import com.feed_the_beast.ftbl.FTBLibMod;
import com.feed_the_beast.ftbl.FTBLibNotifications;
import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.events.ReloadEvent;
import com.feed_the_beast.ftbl.api.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.api.net.MessageToClient;
import com.feed_the_beast.ftbl.api_impl.FTBLibAPI_Impl;
import com.feed_the_beast.ftbl.api_impl.Notification;
import com.feed_the_beast.ftbl.client.ClientNotifications;
import com.feed_the_beast.ftbl.util.FTBLib;
import com.feed_the_beast.ftbl.util.ReloadType;
import com.latmod.lib.util.LMNetUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

        if(FTBLib.DEV_ENV)
        {
            FTBLib.DEV_LOGGER.info("Sending sync data: " + syncData);
        }
    }

    public static void reloadClient(long ms, ReloadType type)
    {
        if(ms == 0L)
        {
            ms = System.currentTimeMillis();
        }

        FTBLibAPI_Impl.get().reloadPackModes();
        EntityPlayer ep = FTBLibMod.proxy.getClientPlayer();
        MinecraftForge.EVENT_BUS.post(new ReloadEvent(Side.CLIENT, ep, type));

        if(type != ReloadType.LOGIN)
        {
            FTBLibLang.reload_client.printChat(ep, (System.currentTimeMillis() - ms) + "ms");
        }

        FTBLibMod.logger.info("Current Mode: " + FTBLibAPI.get().getSharedData(Side.CLIENT).getMode().getID());
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
    @SideOnly(Side.CLIENT)
    public void onMessage(MessageReload m, Minecraft mc)
    {
        long ms = System.currentTimeMillis();
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
            reloadClient(ms, type);
        }
        else if(type == ReloadType.SERVER_ONLY_NOTIFY_CLIENT)
        {
            Notification n = new Notification(FTBLibNotifications.RELOAD_CLIENT_CONFIG);
            n.addText(FTBLibLang.reload_client_config.textComponent());
            n.addText(new TextComponentString("/ftb reload_client"));
            n.setTimer(7000);
            n.setColor(0xFF333333);
            ClientNotifications.add(n);
        }
    }
}