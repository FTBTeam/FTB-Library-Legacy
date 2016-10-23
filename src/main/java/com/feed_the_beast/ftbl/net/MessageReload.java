package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.FTBLibFinals;
import com.feed_the_beast.ftbl.FTBLibIntegrationInternal;
import com.feed_the_beast.ftbl.FTBLibLang;
import com.feed_the_beast.ftbl.api.ISyncData;
import com.feed_the_beast.ftbl.api.events.ReloadEvent;
import com.feed_the_beast.ftbl.api.events.ReloadType;
import com.feed_the_beast.ftbl.api_impl.FTBLibRegistries;
import com.feed_the_beast.ftbl.api_impl.PackMode;
import com.feed_the_beast.ftbl.api_impl.SharedData;
import com.feed_the_beast.ftbl.lib.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.lib.net.MessageToClient;
import com.feed_the_beast.ftbl.lib.util.LMNetUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MessageReload extends MessageToClient<MessageReload>
{
    private int typeID;
    private Map<String, NBTTagCompound> syncData;
    private String currentMode;
    private UUID universeID;

    public MessageReload()
    {
    }

    public MessageReload(ReloadType t, Map<String, NBTTagCompound> sync)
    {
        typeID = t.ordinal();
        syncData = sync;
        currentMode = SharedData.SERVER.getPackMode().getID();
        universeID = SharedData.SERVER.getUniverseID();
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

        io.writeShort(syncData.size());

        syncData.forEach((key, value) ->
        {
            LMNetUtils.writeString(io, key);
            LMNetUtils.writeTag(io, value);
        });

        LMNetUtils.writeString(io, currentMode);
        LMNetUtils.writeUUID(io, universeID);
    }

    @Override
    public void fromBytes(ByteBuf io)
    {
        typeID = io.readUnsignedByte();

        int s = io.readUnsignedShort();

        syncData = new HashMap<>(s);

        while(--s >= 0)
        {
            String key = LMNetUtils.readString(io);
            NBTTagCompound value = LMNetUtils.readTag(io);
            syncData.put(key, value);
        }

        currentMode = LMNetUtils.readString(io);
        universeID = LMNetUtils.readUUID(io);
    }

    @Override
    public void onMessage(MessageReload m)
    {
        long ms = System.currentTimeMillis();
        Minecraft mc = Minecraft.getMinecraft();

        ReloadType type = ReloadType.values()[m.typeID];

        SharedData.CLIENT.universeID = m.universeID;
        SharedData.CLIENT.currentMode = new PackMode(m.currentMode);

        m.syncData.forEach((key, value) ->
        {
            ISyncData nbt = FTBLibRegistries.INSTANCE.SYNCED_DATA.get(new ResourceLocation(key));

            if(nbt != null)
            {
                nbt.readSyncData(value);
            }
        });

        //TODO: new EventFTBWorldClient(ForgeWorldSP.inst).post();

        if(type.reload(Side.CLIENT))
        {
            MinecraftForge.EVENT_BUS.post(new ReloadEvent(Side.CLIENT, mc.thePlayer, type));

            if(type != ReloadType.LOGIN)
            {
                FTBLibLang.RELOAD_CLIENT.printChat(mc.thePlayer, (System.currentTimeMillis() - ms) + "ms");
            }

            FTBLibFinals.LOGGER.info("Current Mode: " + FTBLibIntegrationInternal.API.getClientData().getPackMode().getID());
        }
    }
}