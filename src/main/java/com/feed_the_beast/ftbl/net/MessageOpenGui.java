package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.gui.IGuiHandler;
import com.feed_the_beast.ftbl.api.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.api.net.MessageToClient;
import com.feed_the_beast.ftbl.api_impl.FTBLibRegistries;
import com.latmod.lib.util.LMNetUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class MessageOpenGui extends MessageToClient<MessageOpenGui>
{
    private int guiID;
    private NBTTagCompound data;
    private int windowID;

    public MessageOpenGui()
    {
    }

    public MessageOpenGui(ResourceLocation key, @Nullable NBTTagCompound tag, int wid)
    {
        guiID = FTBLibRegistries.INSTANCE.guis().getIDFromKey(key);
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
        guiID = io.readInt();
        data = LMNetUtils.readTag(io);
        windowID = io.readUnsignedByte();
    }

    @Override
    public void toBytes(ByteBuf io)
    {
        io.writeInt(guiID);
        LMNetUtils.writeTag(io, data);
        io.writeByte(windowID);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onMessage(MessageOpenGui m)
    {
        ResourceLocation key = FTBLibRegistries.INSTANCE.guis().getKeyFromID(m.guiID);

        if(key != null)
        {
            IGuiHandler handler = FTBLibRegistries.INSTANCE.guis().get(key);

            if(handler != null)
            {
                Minecraft mc = Minecraft.getMinecraft();
                GuiScreen g = handler.getGui(mc.thePlayer, m.data);

                if(g != null)
                {
                    mc.displayGuiScreen(g);
                    mc.thePlayer.openContainer.windowId = m.windowID;
                }
            }
        }
    }
}