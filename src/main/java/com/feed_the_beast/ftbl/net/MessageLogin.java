package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.FTBLibConfig;
import com.feed_the_beast.ftbl.FTBLibFinals;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.INotification;
import com.feed_the_beast.ftbl.api.ISyncData;
import com.feed_the_beast.ftbl.api.events.ReloadEvent;
import com.feed_the_beast.ftbl.api.events.ReloadType;
import com.feed_the_beast.ftbl.api_impl.FTBLibRegistries;
import com.feed_the_beast.ftbl.api_impl.PackMode;
import com.feed_the_beast.ftbl.api_impl.SharedData;
import com.feed_the_beast.ftbl.lib.io.Bits;
import com.feed_the_beast.ftbl.lib.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.lib.net.MessageToClient;
import com.feed_the_beast.ftbl.lib.util.LMNetUtils;
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
import java.util.UUID;

public class MessageLogin extends MessageToClient<MessageLogin>
{
    private static final byte IS_OP = 1;
    private static final byte OPTIONAL_SERVER_MODS = 2;
    private static final byte USE_FTB_PREFIX = 4;

    private byte flags;
    private String currentMode;
    private UUID universeID;
    private TShortObjectHashMap<String> configIDs;
    private TShortObjectHashMap<String> guiIDs;
    private TShortObjectHashMap<INotification> notificationIDs;
    private Map<String, NBTTagCompound> syncData;
    private Collection<String> optionalServerMods;

    public MessageLogin()
    {
    }

    public MessageLogin(EntityPlayerMP player, IForgePlayer forgePlayer)
    {
        flags = 0;
        flags = Bits.setFlag(flags, IS_OP, SharedData.SERVER.isOP(player.getGameProfile()));
        flags = Bits.setFlag(flags, USE_FTB_PREFIX, FTBLibConfig.USE_FTB_COMMAND_PREFIX.getBoolean());
        currentMode = SharedData.SERVER.getPackMode().getID();
        universeID = SharedData.SERVER.getUniverseID();
        configIDs = SharedData.SERVER.getConfigIDs().serialize();
        guiIDs = SharedData.SERVER.guiIDs.serialize();
        notificationIDs = FTBLibRegistries.INSTANCE.CACHED_NOTIFICATIONS;
        syncData = new HashMap<>();

        for(ISyncData data : FTBLibRegistries.INSTANCE.SYNCED_DATA.values())
        {
            syncData.put(data.getID().toString(), data.writeSyncData(player, forgePlayer));
        }

        optionalServerMods = SharedData.SERVER.optionalServerMods;
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
        LMNetUtils.writeString(io, currentMode);
        LMNetUtils.writeUUID(io, universeID);

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
        currentMode = LMNetUtils.readString(io);
        universeID = LMNetUtils.readUUID(io);

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

        s = io.readUnsignedShort();
        notificationIDs = new TShortObjectHashMap<>(s);

        while(--s >= 0)
        {
            short id = io.readShort();
            INotification n = MessageNotifyPlayer.read(io);
            notificationIDs.put(id, n);
        }

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
        SharedData.CLIENT.reset();
        SharedData.hasServer = true;
        SharedData.isClientPlayerOP = Bits.getFlag(m.flags, IS_OP);
        SharedData.useFTBPrefix = Bits.getFlag(m.flags, USE_FTB_PREFIX);
        SharedData.clientGameProfile = Minecraft.getMinecraft().thePlayer.getGameProfile();
        SharedData.CLIENT.universeID = m.universeID;
        SharedData.CLIENT.currentMode = new PackMode(m.currentMode);
        SharedData.CLIENT.getConfigIDs().deserialize(m.configIDs);
        SharedData.CLIENT.optionalServerMods.addAll(m.optionalServerMods);
        SharedData.CLIENT.guiIDs.deserialize(m.guiIDs);
        FTBLibRegistries.INSTANCE.CACHED_NOTIFICATIONS.clear();
        FTBLibRegistries.INSTANCE.CACHED_NOTIFICATIONS.putAll(m.notificationIDs);

        m.syncData.forEach((key, value) ->
        {
            ISyncData nbt = FTBLibRegistries.INSTANCE.SYNCED_DATA.get(new ResourceLocation(key));

            if(nbt != null)
            {
                nbt.readSyncData(value);
            }
        });

        //TODO: new EventFTBWorldClient(ForgeWorldSP.inst).post();

        MinecraftForge.EVENT_BUS.post(new ReloadEvent(Side.CLIENT, Minecraft.getMinecraft().thePlayer, ReloadType.LOGIN));
        FTBLibFinals.LOGGER.info("Current Mode: " + m.currentMode);
    }
}