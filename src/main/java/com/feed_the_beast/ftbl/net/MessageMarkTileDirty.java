package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.api.net.MessageToClient;
import com.feed_the_beast.ftbl.api.tile.TileLM;
import com.feed_the_beast.ftbl.util.LMNBTUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class MessageMarkTileDirty extends MessageToClient<MessageMarkTileDirty>
{
    public int posX, posY, posZ;
    public NBTTagCompound data;

    public MessageMarkTileDirty()
    {
    }

    public MessageMarkTileDirty(TileLM t)
    {
        posX = t.getPos().getX();
        posY = t.getPos().getY();
        posZ = t.getPos().getZ();
        data = new NBTTagCompound();
        t.writeTileClientData(data);

        if(t.ownerID != null && t.useOwnerID())
        {
            LMNBTUtils.setUUID(data, "OID", t.ownerID, false);
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
        posX = io.readInt();
        posY = io.readInt();
        posZ = io.readInt();
        data = readTag(io);
    }

    @Override
    public void toBytes(ByteBuf io)
    {
        io.writeInt(posX);
        io.writeInt(posY);
        io.writeInt(posZ);
        writeTag(io, data);
    }

    @Override
    public void onMessage(MessageMarkTileDirty m, Minecraft mc)
    {
        TileEntity te = FTBLibClient.mc.theWorld.getTileEntity(new BlockPos(m.posX, m.posY, m.posZ));

        if(te instanceof TileLM)
        {
            TileLM t = (TileLM) te;
            t.ownerID = t.useOwnerID() ? LMNBTUtils.getUUID(data, "OID", false) : null;
            t.readTileClientData(m.data);
            t.onUpdatePacket();
        }
    }
}