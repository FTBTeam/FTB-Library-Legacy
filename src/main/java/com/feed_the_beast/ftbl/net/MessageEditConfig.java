package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.config.ConfigGroup;
import com.feed_the_beast.ftbl.api.config.IConfigContainer;
import com.feed_the_beast.ftbl.api.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.api.net.MessageToClient;
import com.feed_the_beast.ftbl.gui.GuiEditConfig;
import com.google.gson.JsonObject;
import com.latmod.lib.io.ByteIOStream;
import com.latmod.lib.util.LMNetUtils;
import com.latmod.lib.util.LMUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

public class MessageEditConfig extends MessageToClient<MessageEditConfig> // MessageEditConfigResponse
{
    private ConfigGroup group;
    private NBTTagCompound extraNBT;
    private ITextComponent title;

    public MessageEditConfig()
    {
    }

    public MessageEditConfig(NBTTagCompound nbt, IConfigContainer c)
    {
        group = c.createGroup();
        extraNBT = nbt;
        title = c.getConfigTitle();

        if(LMUtils.DEV_ENV)
        {
            LMUtils.DEV_LOGGER.info("TX Send: " + group);
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
        extraNBT = LMNetUtils.readTag(io);
        title = LMNetUtils.readTextComponent(io);
        group = new ConfigGroup();
        group.readData(LMNetUtils.readCompressedByteIOStream(io), true);
    }

    @Override
    public void toBytes(ByteBuf io)
    {
        LMNetUtils.writeTag(io, extraNBT);
        LMNetUtils.writeTextComponent(io, title);
        ByteIOStream stream = new ByteIOStream();
        group.writeData(stream, true);
        LMNetUtils.writeCompressedByteIOStream(io, stream);
    }

    @Override
    public void onMessage(final MessageEditConfig m)
    {
        if(LMUtils.DEV_ENV)
        {
            LMUtils.DEV_LOGGER.info("RX Send: " + m.group);
        }

        new GuiEditConfig(m.extraNBT, new IConfigContainer()
        {
            @Override
            public ConfigGroup createGroup()
            {
                return m.group;
            }

            @Override
            public ITextComponent getConfigTitle()
            {
                return m.title;
            }

            @Override
            public void saveConfig(ICommandSender sender, @Nullable NBTTagCompound nbt, JsonObject json)
            {
                new MessageEditConfigResponse(nbt, json).sendToServer();
            }
        }).openGui();
    }
}