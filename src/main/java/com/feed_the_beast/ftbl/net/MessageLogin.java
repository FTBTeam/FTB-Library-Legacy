package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.FTBLibMod;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.ISyncData;
import com.feed_the_beast.ftbl.api.events.ReloadEvent;
import com.feed_the_beast.ftbl.api.events.ReloadType;
import com.feed_the_beast.ftbl.api.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.api.net.MessageToClient;
import com.feed_the_beast.ftbl.api_impl.ConfigManager;
import com.feed_the_beast.ftbl.api_impl.FTBLibAPI_Impl;
import com.feed_the_beast.ftbl.api_impl.FTBLibRegistries;
import com.latmod.lib.util.LMNetUtils;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;

import java.util.HashMap;
import java.util.Map;

public class MessageLogin extends MessageToClient<MessageLogin>
{
    private TIntObjectMap<String> configIDs;
    private TIntObjectMap<String> guiIDs;
    private TIntObjectMap<String> notificationIDs;
    private NBTTagCompound sharedDataTag;
    private Map<String, NBTTagCompound> syncData;

    public MessageLogin()
    {
    }

    public MessageLogin(EntityPlayerMP player, IForgePlayer forgePlayer)
    {
        configIDs = ConfigManager.INSTANCE.configValues().getIntIDs().serialize();
        guiIDs = FTBLibRegistries.INSTANCE.guis().getIntIDs().serialize();
        notificationIDs = FTBLibRegistries.INSTANCE.notifications().serialize();
        sharedDataTag = FTBLibAPI_Impl.INSTANCE.getSharedData(Side.SERVER).serializeNBT();
        syncData = new HashMap<>();

        for(Map.Entry<ResourceLocation, ISyncData> entry : FTBLibRegistries.INSTANCE.syncedData().getEntrySet())
        {
            syncData.put(entry.getKey().toString(), entry.getValue().writeSyncData(player, forgePlayer));
        }
    }

    @Override
    public LMNetworkWrapper getWrapper()
    {
        return FTBLibNetHandler.NET;
    }

    private static void writeMap(ByteBuf io, TIntObjectMap<String> map)
    {
        io.writeShort(map.size());

        map.forEachEntry((key, value) ->
        {
            io.writeShort(key);
            LMNetUtils.writeString(io, value);
            return true;
        });
    }

    @Override
    public void toBytes(ByteBuf io)
    {
        writeMap(io, configIDs);
        writeMap(io, guiIDs);
        writeMap(io, notificationIDs);
        LMNetUtils.writeTag(io, sharedDataTag);

        io.writeShort(syncData.size());

        syncData.forEach((key, value) ->
        {
            LMNetUtils.writeString(io, key);
            LMNetUtils.writeTag(io, value);
        });
    }

    private static TIntObjectMap<String> readMap(ByteBuf io)
    {
        int s = io.readUnsignedShort();

        TIntObjectMap<String> map = new TIntObjectHashMap<>(s);

        while(--s >= 0)
        {
            int key = io.readUnsignedShort();
            String value = LMNetUtils.readString(io);
            map.put(key, value);
        }

        return map;
    }

    @Override
    public void fromBytes(ByteBuf io)
    {
        configIDs = readMap(io);
        guiIDs = readMap(io);
        notificationIDs = readMap(io);
        sharedDataTag = LMNetUtils.readTag(io);

        int s = io.readUnsignedShort();

        syncData = new HashMap<>(s);

        while(--s >= 0)
        {
            String key = LMNetUtils.readString(io);
            NBTTagCompound value = LMNetUtils.readTag(io);
            syncData.put(key, value);
        }
    }

    @Override
    public void onMessage(MessageLogin m)
    {
        ConfigManager.INSTANCE.configValues().getIntIDs().deserialize(m.configIDs);
        FTBLibRegistries.INSTANCE.guis().getIntIDs().deserialize(m.guiIDs);
        FTBLibRegistries.INSTANCE.notifications().deserialize(m.notificationIDs);
        FTBLibAPI_Impl.INSTANCE.getSharedData(Side.CLIENT).deserializeNBT(m.sharedDataTag);

        m.syncData.forEach((key, value) ->
        {
            ISyncData nbt = FTBLibRegistries.INSTANCE.syncedData().get(new ResourceLocation(key));

            if(nbt != null)
            {
                nbt.readSyncData(value);
            }
        });

        //TODO: new EventFTBWorldClient(ForgeWorldSP.inst).post();

        FTBLibAPI_Impl.INSTANCE.reloadPackModes();
        MinecraftForge.EVENT_BUS.post(new ReloadEvent(Side.CLIENT, Minecraft.getMinecraft().thePlayer, ReloadType.LOGIN));
        FTBLibMod.logger.info("Current Mode: " + FTBLibAPI_Impl.INSTANCE.getSharedData(Side.CLIENT).getPackMode().getID());
    }
}