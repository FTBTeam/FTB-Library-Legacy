package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.FTBLibModCommon;
import com.feed_the_beast.ftbl.api.EnumReloadType;
import com.feed_the_beast.ftbl.api.ISyncData;
import com.feed_the_beast.ftbl.api_impl.PackMode;
import com.feed_the_beast.ftbl.api_impl.SharedClientData;
import com.feed_the_beast.ftbl.api_impl.SharedServerData;
import com.feed_the_beast.ftbl.lib.internal.FTBLibIntegrationInternal;
import com.feed_the_beast.ftbl.lib.net.MessageToClient;
import com.feed_the_beast.ftbl.lib.net.NetworkWrapper;
import com.feed_the_beast.ftbl.lib.util.NetUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

public class MessageReload extends MessageToClient<MessageReload>
{
    private int typeID;
    private NBTTagCompound syncData;
    private String currentMode;
    private UUID universeID;

    public MessageReload()
    {
    }

    public MessageReload(EnumReloadType t, NBTTagCompound sync)
    {
        typeID = t.ordinal();
        syncData = sync;
        currentMode = SharedServerData.INSTANCE.getPackMode().getName();
        universeID = SharedServerData.INSTANCE.getUniverseID();
    }

    @Override
    public NetworkWrapper getWrapper()
    {
        return FTBLibNetHandler.NET;
    }

    @Override
    public void toBytes(ByteBuf io)
    {
        io.writeByte(typeID);
        ByteBufUtils.writeTag(io, syncData);
        ByteBufUtils.writeUTF8String(io, currentMode);
        NetUtils.writeUUID(io, universeID);
    }

    @Override
    public void fromBytes(ByteBuf io)
    {
        typeID = io.readUnsignedByte();
        syncData = ByteBufUtils.readTag(io);
        currentMode = ByteBufUtils.readUTF8String(io);
        universeID = NetUtils.readUUID(io);
    }

    @Override
    public void onMessage(MessageReload m, EntityPlayer player)
    {
        EnumReloadType type = m.typeID >= EnumReloadType.VALUES.length ? EnumReloadType.MODE_CHANGED : EnumReloadType.VALUES[m.typeID];

        SharedClientData.INSTANCE.universeID = m.universeID;
        SharedClientData.INSTANCE.currentMode = new PackMode(m.currentMode);

        for(String key : m.syncData.getKeySet())
        {
            ISyncData nbt = FTBLibModCommon.SYNCED_DATA.get(key);

            if(nbt != null)
            {
                nbt.readSyncData(m.syncData.getCompoundTag(key));
            }
        }

        if(type != EnumReloadType.RELOAD_COMMAND)
        {
            FTBLibIntegrationInternal.API.reload(Side.CLIENT, player, type);
        }
    }
}