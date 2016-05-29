package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.api.net.MessageToClient;
import com.feed_the_beast.ftbl.api.tile.IGuiTile;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageOpenGuiTile extends MessageToClient<MessageOpenGuiTile>
{
    public int posX, posY, posZ, windowID;
    public NBTTagCompound data;

    public MessageOpenGuiTile()
    {
    }

    public MessageOpenGuiTile(TileEntity t, NBTTagCompound tag, int wid)
    {
        posX = t.getPos().getX();
        posY = t.getPos().getY();
        posZ = t.getPos().getZ();
        data = tag;
        windowID = wid;
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
        windowID = io.readUnsignedByte();
    }

    @Override
    public void toBytes(ByteBuf io)
    {
        io.writeInt(posX);
        io.writeInt(posY);
        io.writeInt(posZ);
        writeTag(io, data);
        io.writeByte(windowID);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onMessage(MessageOpenGuiTile m, Minecraft mc)
    {
        TileEntity te = mc.theWorld.getTileEntity(new BlockPos(m.posX, m.posY, m.posZ));

        if(te != null && !te.isInvalid() && te instanceof IGuiTile)
        {
            GuiScreen gui = ((IGuiTile) te).getGui(mc.thePlayer, m.data);

            if(gui != null)
            {
                mc.displayGuiScreen(gui);
                mc.thePlayer.openContainer.windowId = m.windowID;
            }
        }
    }
}