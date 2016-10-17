package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IGuiHandler;
import com.feed_the_beast.ftbl.api_impl.FTBLibRegistries;
import com.feed_the_beast.ftbl.api_impl.SharedData;
import com.feed_the_beast.ftbl.lib.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.lib.net.MessageToClient;
import com.feed_the_beast.ftbl.lib.util.LMNetUtils;
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
    private short guiID;
    private NBTTagCompound data;
    private int windowID;

    public MessageOpenGui()
    {
    }

    public MessageOpenGui(ResourceLocation key, @Nullable NBTTagCompound tag, int wid)
    {
        guiID = SharedData.SERVER.guiIDs.getIDFromKey(key);
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
        guiID = io.readShort();
        data = LMNetUtils.readTag(io);
        windowID = io.readUnsignedByte();
    }

    @Override
    public void toBytes(ByteBuf io)
    {
        io.writeShort(guiID);
        LMNetUtils.writeTag(io, data);
        io.writeByte(windowID);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onMessage(MessageOpenGui m)
    {
        ResourceLocation key = SharedData.CLIENT.guiIDs.getKeyFromID(m.guiID);

        if(key != null)
        {
            IGuiHandler handler = FTBLibRegistries.INSTANCE.GUIS.get(key);

            if(handler != null)
            {
                Minecraft mc = Minecraft.getMinecraft();
                Object go = handler.getGui(mc.thePlayer, m.data);

                GuiScreen g;

                if(go instanceof IGui)
                {
                    g = ((IGui) go).getWrapper();
                }
                else
                {
                    g = (GuiScreen) go;
                }

                if(g != null)
                {
                    mc.displayGuiScreen(g);
                    mc.thePlayer.openContainer.windowId = m.windowID;
                }
            }
        }
    }
}