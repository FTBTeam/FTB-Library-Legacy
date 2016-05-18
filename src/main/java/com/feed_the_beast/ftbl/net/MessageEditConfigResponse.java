package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.config.ConfigFile;
import com.feed_the_beast.ftbl.api.config.ConfigGroup;
import com.feed_the_beast.ftbl.api.config.ConfigRegistry;
import com.feed_the_beast.ftbl.api.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.api.net.MessageToServer;
import com.feed_the_beast.ftbl.util.FTBLib;
import com.feed_the_beast.ftbl.util.LMAccessToken;
import com.feed_the_beast.ftbl.util.ReloadType;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public class MessageEditConfigResponse extends MessageToServer<MessageEditConfigResponse>
{
    public long token;
    public int typeID;
    public String groupID;
    public NBTTagCompound tag;
    
    public MessageEditConfigResponse() { }
    
    public MessageEditConfigResponse(long adminToken, ReloadType reload, ConfigGroup group)
    {
        token = adminToken;
        typeID = reload.ordinal();
        groupID = group.getID();
        tag = new NBTTagCompound();
        group.writeToNBT(tag, false);
        
        if(FTBLib.DEV_ENV) { FTBLib.dev_logger.info("Response TX: " + group.getSerializableElement()); }
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
    public void onMessage(MessageEditConfigResponse m, EntityPlayerMP ep)
    {
        if(!LMAccessToken.equals(ep, m.token, false)) { return; }
        
        ConfigFile file = ConfigRegistry.map.containsKey(m.groupID) ? ConfigRegistry.map.get(m.groupID) : ConfigRegistry.getTempConfig(m.groupID);
        if(file == null) { return; }
        
        ConfigGroup group = new ConfigGroup(m.groupID);
        group.readFromNBT(m.tag, false);
        
        if(file.loadFromGroup(group, true) > 0)
        {
            file.save();
            FTBLib.reload(ep, ReloadType.values()[m.typeID], false);
        }
        
        if(FTBLib.DEV_ENV) { FTBLib.dev_logger.info("Response RX: " + file.getSerializableElement()); }
    }
}