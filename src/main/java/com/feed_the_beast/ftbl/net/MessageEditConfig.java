package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.config.ConfigContainer;
import com.feed_the_beast.ftbl.api.config.ConfigGroup;
import com.feed_the_beast.ftbl.api.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.api.net.MessageToClient;
import com.feed_the_beast.ftbl.gui.GuiEditConfig;
import com.feed_the_beast.ftbl.util.FTBLib;
import com.feed_the_beast.ftbl.util.JsonHelper;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageEditConfig extends MessageToClient<MessageEditConfig> // MessageEditConfigResponse
{
    public ResourceLocation id;
    public ConfigGroup group;
    public NBTTagCompound extraNBT;
    public ITextComponent title;

    public MessageEditConfig()
    {
    }

    public MessageEditConfig(NBTTagCompound nbt, ConfigContainer c)
    {
        id = c.getResourceLocation();
        group = c.createGroup();
        extraNBT = nbt;
        title = c.getConfigTitle();

        if(FTBLib.DEV_ENV)
        {
            FTBLib.dev_logger.info("TX Send: " + id + " :: " + group);
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
        id = readResourceLocation(io);
        group = new ConfigGroup();
        group.readData(io, true);
        extraNBT = readTag(io);
        title = JsonHelper.deserializeICC(readJsonElement(io));
    }

    @Override
    public void toBytes(ByteBuf io)
    {
        writeResourceLocation(io, id);
        group.writeData(io, true);
        writeTag(io, extraNBT);
        writeJsonElement(io, JsonHelper.serializeICC(title));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onMessage(final MessageEditConfig m, Minecraft mc)
    {
        if(FTBLib.DEV_ENV)
        {
            FTBLib.dev_logger.info("RX Send: " + m.id + " :: " + m.group);
        }

        new GuiEditConfig(m.extraNBT, new ConfigContainer(m.id)
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
                new MessageEditConfigResponse(m.id, nbt, json).sendToServer();
            }
        }).openGui();
    }
}