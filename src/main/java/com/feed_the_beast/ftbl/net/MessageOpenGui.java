package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.FTBLibMod;
import com.feed_the_beast.ftbl.api.gui.GuiHandler;
import com.feed_the_beast.ftbl.api.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.api.net.MessageToClient;
import com.latmod.lib.util.LMNetUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageOpenGui extends MessageToClient<MessageOpenGui>
{
    public String modID;
    public int guiID;
    public NBTTagCompound data;
    public int windowID;

    public MessageOpenGui()
    {
    }

    public MessageOpenGui(String mod, int id, NBTTagCompound tag, int wid)
    {
        modID = mod;
        guiID = id;
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
        modID = LMNetUtils.readString(io);
        guiID = io.readInt();
        data = LMNetUtils.readTag(io);
        windowID = io.readUnsignedByte();
    }

    @Override
    public void toBytes(ByteBuf io)
    {
        LMNetUtils.writeString(io, modID);
        io.writeInt(guiID);
        LMNetUtils.writeTag(io, data);
        io.writeByte(windowID);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onMessage(MessageOpenGui m, Minecraft mc)
    {
        GuiHandler handler = GuiHandler.REGISTRY.get(m.modID);

        if(handler != null)
        {
            FTBLibMod.proxy.openClientGui(handler, mc.thePlayer, m.guiID, m.data, m.windowID);
        }
    }
}