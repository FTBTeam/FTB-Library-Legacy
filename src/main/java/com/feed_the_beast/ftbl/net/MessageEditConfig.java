package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.config.IConfigContainer;
import com.feed_the_beast.ftbl.api.config.IConfigTree;
import com.feed_the_beast.ftbl.api.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.api.net.MessageToClient;
import com.feed_the_beast.ftbl.gui.GuiEditConfig;
import com.google.gson.JsonObject;
import com.latmod.lib.config.ConfigTree;
import com.latmod.lib.io.ByteIOStream;
import com.latmod.lib.util.LMNetUtils;
import com.latmod.lib.util.LMUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;
import java.io.IOException;

public class MessageEditConfig extends MessageToClient<MessageEditConfig> // MessageEditConfigResponse
{
    private IConfigTree group;
    private NBTTagCompound extraNBT;
    private ITextComponent title;

    public MessageEditConfig()
    {
    }

    public MessageEditConfig(@Nullable NBTTagCompound nbt, IConfigContainer c)
    {
        group = c.getTree();
        extraNBT = nbt;
        title = c.getTitle();

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
        group = new ConfigTree();

        try
        {
            group.readData(LMNetUtils.readCompressedByteIOStream(io), true);
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void toBytes(ByteBuf io)
    {
        LMNetUtils.writeTag(io, extraNBT);
        LMNetUtils.writeTextComponent(io, title);
        ByteIOStream stream = new ByteIOStream();

        try
        {
            group.writeData(stream, true);
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }

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
            public IConfigTree getTree()
            {
                return m.group;
            }

            @Override
            public ITextComponent getTitle()
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