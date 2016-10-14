package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.FTBLibConfig;
import com.feed_the_beast.ftbl.FTBLibIntegrationInternal;
import com.feed_the_beast.ftbl.FTBLibMod;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.INotification;
import com.feed_the_beast.ftbl.api.ISyncData;
import com.feed_the_beast.ftbl.api.events.ReloadEvent;
import com.feed_the_beast.ftbl.api.events.ReloadType;
import com.feed_the_beast.ftbl.api_impl.FTBLibAPI_Impl;
import com.feed_the_beast.ftbl.api_impl.FTBLibRegistries;
import com.feed_the_beast.ftbl.lib.io.Bits;
import com.feed_the_beast.ftbl.lib.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.lib.net.MessageToClient;
import com.feed_the_beast.ftbl.lib.util.LMNetUtils;
import com.feed_the_beast.ftbl.lib.util.LMServerUtils;
import gnu.trove.map.hash.TShortObjectHashMap;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class MessageLogin extends MessageToClient<MessageLogin>
{
    private static final byte IS_OP = 1;
    private static final byte OPTIONAL_SERVER_MODS = 2;
    private static final byte USE_FTB_PREFIX = 4;

    private byte flags;
    private TShortObjectHashMap<String> configIDs;
    private TShortObjectHashMap<String> guiIDs;
    private TShortObjectHashMap<INotification> notificationIDs;
    private NBTTagCompound sharedDataTag;
    private Map<String, NBTTagCompound> syncData;
    private Collection<String> optionalServerMods;

    public MessageLogin()
    {
    }

    public MessageLogin(EntityPlayerMP player, IForgePlayer forgePlayer)
    {
        flags = 0;
        flags = Bits.setFlag(flags, IS_OP, LMServerUtils.isOP(player.getGameProfile()));
        flags = Bits.setFlag(flags, USE_FTB_PREFIX, FTBLibConfig.USE_FTB_COMMAND_PREFIX.getBoolean());
        configIDs = FTBLibRegistries.INSTANCE.CONFIG_VALUES.getIDs().serialize();
        guiIDs = FTBLibRegistries.INSTANCE.GUIS.getIDs().serialize();
        notificationIDs = FTBLibRegistries.INSTANCE.CACHED_NOTIFICATIONS;
        sharedDataTag = FTBLibIntegrationInternal.API.getSharedData(Side.SERVER).serializeNBT();
        syncData = new HashMap<>();

        for(ISyncData data : FTBLibRegistries.INSTANCE.SYNCED_DATA.values())
        {
            syncData.put(data.getID().toString(), data.writeSyncData(player, forgePlayer));
        }

        optionalServerMods = FTBLibRegistries.INSTANCE.OPTIONAL_SERVER_MODS;
        flags = Bits.setFlag(flags, OPTIONAL_SERVER_MODS, !optionalServerMods.isEmpty());
    }

    @Override
    public LMNetworkWrapper getWrapper()
    {
        return FTBLibNetHandler.NET;
    }

    @Override
    public void toBytes(ByteBuf io)
    {
        io.writeByte(flags);
        io.writeShort(configIDs.size());

        configIDs.forEachEntry((key, value) ->
        {
            io.writeShort(key);
            LMNetUtils.writeString(io, value);
            return true;
        });

        io.writeShort(guiIDs.size());

        guiIDs.forEachEntry((key, value) ->
        {
            io.writeShort(key);
            LMNetUtils.writeString(io, value);
            return true;
        });

        io.writeShort(notificationIDs.size());

        notificationIDs.forEachEntry((key, value) ->
        {
            io.writeShort(key);
            MessageNotifyPlayer.write(io, value);
            return true;
        });


        LMNetUtils.writeTag(io, sharedDataTag);

        io.writeShort(syncData.size());

        syncData.forEach((key, value) ->
        {
            LMNetUtils.writeString(io, key);
            LMNetUtils.writeTag(io, value);
        });

        if(!optionalServerMods.isEmpty())
        {
            io.writeShort(optionalServerMods.size());

            for(String s : optionalServerMods)
            {
                LMNetUtils.writeString(io, s);
            }
        }
    }

    @Override
    public void fromBytes(ByteBuf io)
    {
        flags = io.readByte();
        int s = io.readUnsignedShort();

        configIDs = new TShortObjectHashMap<>(s);

        while(--s >= 0)
        {
            short key = io.readShort();
            String value = LMNetUtils.readString(io);
            configIDs.put(key, value);
        }

        s = io.readUnsignedShort();

        guiIDs = new TShortObjectHashMap<>(s);

        while(--s >= 0)
        {
            short key = io.readShort();
            String value = LMNetUtils.readString(io);
            guiIDs.put(key, value);
        }

        notificationIDs = new TShortObjectHashMap<>();

        s = io.readUnsignedShort();

        while(--s >= 0)
        {
            short id = io.readShort();
            INotification n = MessageNotifyPlayer.read(io);
            notificationIDs.put(id, n);
        }

        sharedDataTag = LMNetUtils.readTag(io);

        s = io.readUnsignedShort();

        syncData = new HashMap<>(s);

        while(--s >= 0)
        {
            String key = LMNetUtils.readString(io);
            NBTTagCompound value = LMNetUtils.readTag(io);
            syncData.put(key, value);
        }

        if(Bits.getFlag(flags, OPTIONAL_SERVER_MODS))
        {
            s = io.readUnsignedShort();
            optionalServerMods = new HashSet<>(s);

            while(--s >= 0)
            {
                optionalServerMods.add(LMNetUtils.readString(io));
            }
        }
    }

    @Override
    public void onMessage(MessageLogin m)
    {
        FTBLibAPI_Impl.hasServer = true;
        FTBLibAPI_Impl.isClientPlayerOP = Bits.getFlag(m.flags, IS_OP);
        FTBLibAPI_Impl.useFTBPrefix = Bits.getFlag(m.flags, USE_FTB_PREFIX);
        FTBLibRegistries.INSTANCE.CONFIG_VALUES.getIDs().deserialize(m.configIDs);
        FTBLibRegistries.INSTANCE.OPTIONAL_SERVER_MODS_CLIENT.clear();
        FTBLibRegistries.INSTANCE.OPTIONAL_SERVER_MODS_CLIENT.addAll(m.optionalServerMods);
        FTBLibRegistries.INSTANCE.GUIS.getIDs().deserialize(m.guiIDs);
        FTBLibRegistries.INSTANCE.CACHED_NOTIFICATIONS.clear();
        FTBLibRegistries.INSTANCE.CACHED_NOTIFICATIONS.putAll(m.notificationIDs);
        FTBLibIntegrationInternal.API.getSharedData(Side.CLIENT).deserializeNBT(m.sharedDataTag);

        m.syncData.forEach((key, value) ->
        {
            ISyncData nbt = FTBLibRegistries.INSTANCE.SYNCED_DATA.get(new ResourceLocation(key));

            if(nbt != null)
            {
                nbt.readSyncData(value);
            }
        });

        //TODO: new EventFTBWorldClient(ForgeWorldSP.inst).post();

        FTBLibAPI_Impl.reloadPackModes();
        MinecraftForge.EVENT_BUS.post(new ReloadEvent(Side.CLIENT, Minecraft.getMinecraft().thePlayer, ReloadType.LOGIN));
        FTBLibMod.LOGGER.info("Current Mode: " + FTBLibIntegrationInternal.API.getSharedData(Side.CLIENT).getPackMode().getID());
    }
}