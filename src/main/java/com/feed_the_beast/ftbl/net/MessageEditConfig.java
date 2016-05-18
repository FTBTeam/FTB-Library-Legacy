package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.config.ConfigGroup;
import com.feed_the_beast.ftbl.api.config.ServerConfigProvider;
import com.feed_the_beast.ftbl.api.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.api.net.MessageToClient;
import com.feed_the_beast.ftbl.gui.GuiEditConfig;
import com.feed_the_beast.ftbl.util.FTBLib;
import com.feed_the_beast.ftbl.util.ReloadType;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageEditConfig extends MessageToClient<MessageEditConfig> // MessageEditConfigResponse
{
    public long token;
    public int typeID;
    public String groupID;
    public NBTTagCompound tag;
    
    public MessageEditConfig() { }
    
    public MessageEditConfig(long t, ReloadType reload, ConfigGroup group)
    {
        token = t;
        typeID = reload.ordinal();
        groupID = group.getID();
        tag = new NBTTagCompound();
        group.writeToNBT(tag, true);
        
        if(FTBLib.DEV_ENV) { FTBLib.dev_logger.info("TX Send: " + group.getSerializableElement()); }
    }
    
    @Override
    public LMNetworkWrapper getWrapper()
    { return FTBLibNetHandler.NET; }
    
    @Override
    public void fromBytes(ByteBuf io)
    {
        token = io.readLong();
        typeID = io.readUnsignedByte();
        groupID = readString(io);
        tag = readTag(io);
    }
    
    @Override
    public void toBytes(ByteBuf io)
    {
        io.writeLong(token);
        io.writeByte(typeID);
        writeString(io, groupID);
        writeTag(io, tag);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void onMessage(MessageEditConfig m, Minecraft mc)
    {
        ConfigGroup group = new ConfigGroup(m.groupID);
        group.readFromNBT(m.tag, true);
        
        if(FTBLib.DEV_ENV) { FTBLib.dev_logger.info("RX Send: " + group.getSerializableElement()); }
        
        FTBLibClient.openGui(new GuiEditConfig(FTBLibClient.mc.currentScreen, new ServerConfigProvider(m.token, ReloadType.values()[m.typeID], group)));
    }
}