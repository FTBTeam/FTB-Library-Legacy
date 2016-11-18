package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.gui.IGuiProvider;
import com.feed_the_beast.ftbl.client.FTBLibModClient;
import com.feed_the_beast.ftbl.lib.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.lib.net.MessageToClient;
import com.feed_the_beast.ftbl.lib.util.LMNetUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class MessageOpenGui extends MessageToClient<MessageOpenGui>
{
    private ResourceLocation guiID;
    private BlockPos pos;
    private NBTTagCompound data;
    private int windowID;

    public MessageOpenGui()
    {
    }

    public MessageOpenGui(ResourceLocation key, BlockPos p, @Nullable NBTTagCompound tag, int wid)
    {
        guiID = key;
        pos = p;
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
        guiID = LMNetUtils.readResourceLocation(io);
        pos = LMNetUtils.readPos(io);
        data = LMNetUtils.readTag(io);
        windowID = io.readUnsignedByte();
    }

    @Override
    public void toBytes(ByteBuf io)
    {
        LMNetUtils.writeResourceLocation(io, guiID);
        LMNetUtils.writePos(io, pos);
        LMNetUtils.writeTag(io, data);
        io.writeByte(windowID);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onMessage(MessageOpenGui m)
    {
        IGuiProvider guiProvider = FTBLibModClient.getGui(m.guiID);

        if(guiProvider != null)
        {
            Minecraft mc = Minecraft.getMinecraft();
            GuiScreen g = guiProvider.getGui(mc.thePlayer, m.pos, m.data);

            if(g != null)
            {
                mc.displayGuiScreen(g);
                mc.thePlayer.openContainer.windowId = m.windowID;
            }
        }
    }
}