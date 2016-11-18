package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.FTBLibMod;
import com.feed_the_beast.ftbl.api.EnumReloadType;
import com.feed_the_beast.ftbl.api.IFTBLibPlugin;
import com.feed_the_beast.ftbl.api.ISyncData;
import com.feed_the_beast.ftbl.api_impl.PackMode;
import com.feed_the_beast.ftbl.api_impl.SharedClientData;
import com.feed_the_beast.ftbl.api_impl.SharedServerData;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import com.feed_the_beast.ftbl.lib.internal.FTBLibIntegrationInternal;
import com.feed_the_beast.ftbl.lib.internal.FTBLibLang;
import com.feed_the_beast.ftbl.lib.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.lib.net.MessageToClient;
import com.feed_the_beast.ftbl.lib.util.LMNetUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
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
        currentMode = SharedServerData.INSTANCE.getPackMode().getID();
        universeID = SharedServerData.INSTANCE.getUniverseID();
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
        LMNetUtils.writeString(io, currentMode);
        LMNetUtils.writeUUID(io, universeID);
    }

    @Override
    public void fromBytes(ByteBuf io)
    {
        typeID = io.readUnsignedByte();
        syncData = LMNetUtils.readTag(io);
        currentMode = LMNetUtils.readString(io);
        universeID = LMNetUtils.readUUID(io);
    }

    @Override
    public void onMessage(MessageReload m)
    {
        long ms = System.currentTimeMillis();
        Minecraft mc = Minecraft.getMinecraft();

        EnumReloadType type = EnumReloadType.values()[m.typeID];

        SharedClientData.INSTANCE.universeID = m.universeID;
        SharedClientData.INSTANCE.currentMode = new PackMode(m.currentMode);

        for(String key : m.syncData.getKeySet())
        {
            ISyncData nbt = FTBLibMod.PROXY.SYNCED_DATA.get(key);

            if(nbt != null)
            {
                nbt.readSyncData(m.syncData.getCompoundTag(key));
            }
        }

        //TODO: new EventFTBWorldClient(ForgeWorldSP.inst).post();

        if(type.reload(Side.CLIENT))
        {
            for(IFTBLibPlugin plugin : FTBLibIntegrationInternal.API.getAllPlugins())
            {
                plugin.onReload(Side.CLIENT, mc.thePlayer, type);
            }

            if(type != EnumReloadType.LOGIN)
            {
                FTBLibLang.RELOAD_CLIENT.printChat(mc.thePlayer, (System.currentTimeMillis() - ms) + "ms");
            }

            FTBLibFinals.LOGGER.info("Current Mode: " + FTBLibIntegrationInternal.API.getClientData().getPackMode().getID());
        }
    }
}