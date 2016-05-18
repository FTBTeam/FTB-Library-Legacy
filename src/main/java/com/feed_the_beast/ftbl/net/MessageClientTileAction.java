package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.api.net.MessageToServer;
import com.feed_the_beast.ftbl.api.tile.TileClientAction;
import com.feed_the_beast.ftbl.api.tile.TileClientActionRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class MessageClientTileAction extends MessageToServer<MessageClientTileAction>
{
    public int posX, posY, posZ;
    public ResourceLocation action;
    public NBTTagCompound data;
    
    public MessageClientTileAction() { }
    
    public MessageClientTileAction(TileEntity t, TileClientAction a, NBTTagCompound tag)
    {
        posX = t.getPos().getX();
        posY = t.getPos().getY();
        posZ = t.getPos().getZ();
        action = a.getResourceLocation();
        data = tag;
    }
    
    @Override
    public LMNetworkWrapper getWrapper()
    { return FTBLibNetHandler.NET; }
    
    @Override
    public void fromBytes(ByteBuf io)
    {
        posX = io.readInt();
        posY = io.readInt();
        posZ = io.readInt();
        action = readResourceLocation(io);
        data = readTag(io);
    }
    
    @Override
    public void toBytes(ByteBuf io)
    {
        io.writeInt(posX);
        io.writeInt(posY);
        io.writeInt(posZ);
        writeResourceLocation(io, action);
        writeTag(io, data);
    }
    
    @Override
    public void onMessage(MessageClientTileAction m, EntityPlayerMP ep)
    {
        TileClientAction action = TileClientActionRegistry.map.get(m.action);
        
        if(action != null)
        {
            TileEntity te = ep.worldObj.getTileEntity(new BlockPos(m.posX, m.posY, m.posZ));
            
            if(te != null)
            {
                action.onAction(te, m.data, ep);
            }
        }
    }
}