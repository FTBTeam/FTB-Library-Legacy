package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.config.ConfigContainer;
import com.feed_the_beast.ftbl.api.config.ConfigGroup;
import com.feed_the_beast.ftbl.api.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.api.net.MessageToClient;
import com.feed_the_beast.ftbl.gui.GuiEditConfig;
import com.feed_the_beast.ftbl.util.FTBLib;
import com.google.gson.JsonObject;
import com.latmod.lib.io.ByteIOStream;
import com.latmod.lib.util.LMNetUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageEditConfig extends MessageToClient<MessageEditConfig> // MessageEditConfigResponse
{
    public ConfigGroup group;
    public NBTTagCompound extraNBT;
    public ITextComponent title;

    public MessageEditConfig()
    {
    }

    public MessageEditConfig(NBTTagCompound nbt, ConfigContainer c)
    {
        group = c.createGroup();
        extraNBT = nbt;
        title = c.getConfigTitle();

        if(FTBLib.DEV_ENV)
        {
            FTBLib.dev_logger.info("TX Send: " + group);
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
    @SideOnly(Side.CLIENT)
    public void onMessage(final MessageEditConfig m, Minecraft mc)
    {
        if(FTBLib.DEV_ENV)
        {
            FTBLib.dev_logger.info("RX Send: " + m.group);
        }

        new GuiEditConfig(m.extraNBT, new ConfigContainer()
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
            public void saveConfig(ICommandSender sender, NBTTagCompound nbt, JsonObject json)
            {
                new MessageEditConfigResponse(nbt, json).sendToServer();
            }
        }).openGui();
    }
}