package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.FTBLibMod;
import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.gui.IGuiHandler;
import com.feed_the_beast.ftbl.api.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.api.net.MessageToClient;
import com.latmod.lib.util.LMNetUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageOpenGui extends MessageToClient<MessageOpenGui>
{
    public int guiID;
    public NBTTagCompound data;
    public int windowID;

    public MessageOpenGui()
    {
    }

    public MessageOpenGui(ResourceLocation key, NBTTagCompound tag, int wid)
    {
        guiID = FTBLibAPI.get().getRegistries().guis().getIDFromKey(key);
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
    public void onMessage(MessageOpenGui m, Minecraft mc)
    {
        ResourceLocation key = FTBLibAPI.get().getRegistries().guis().getKeyFromID(m.guiID);

        if(key != null)
        {
            IGuiHandler handler = FTBLibAPI.get().getRegistries().guis().get(key);

            if(handler != null)
            {
                FTBLibMod.proxy.openClientGui(handler, mc.thePlayer, m.data, m.windowID);
            }
        }
    }
}