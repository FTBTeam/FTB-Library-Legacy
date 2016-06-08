package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.config.ConfigContainer;
import com.feed_the_beast.ftbl.api.config.ConfigGroup;
import com.feed_the_beast.ftbl.api.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.api.net.MessageToClient;
import com.feed_the_beast.ftbl.gui.GuiEditConfig;
import com.feed_the_beast.ftbl.util.FTBLib;
import com.feed_the_beast.ftbl.util.JsonHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageEditConfig extends MessageToClient<MessageEditConfig> // MessageEditConfigResponse
{
    public ResourceLocation id;
    public NBTTagCompound groupData;
    public NBTTagCompound extraNBT;
    public ITextComponent title;

    public MessageEditConfig()
    {
    }

    public MessageEditConfig(NBTTagCompound nbt, ConfigContainer c)
    {
        id = c.getResourceLocation();
        groupData = new NBTTagCompound();
        c.createGroup().writeToNBT(groupData, true);
        extraNBT = nbt;
        title = c.getConfigTitle();

        if(FTBLib.DEV_ENV)
        {
            FTBLib.dev_logger.info("TX Send: " + id + " :: " + groupData);
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
        groupData = readTag(io);
        extraNBT = readTag(io);
        title = JsonHelper.deserializeICC(readJsonElement(io));
    }

    @Override
    public void toBytes(ByteBuf io)
    {
        writeResourceLocation(io, id);
        writeTag(io, groupData);
        writeTag(io, extraNBT);
        writeJsonElement(io, JsonHelper.serializeICC(title));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onMessage(final MessageEditConfig m, Minecraft mc)
    {
        if(FTBLib.DEV_ENV)
        {
            FTBLib.dev_logger.info("RX Send: " + m.id + " :: " + m.groupData);
        }

        mc.displayGuiScreen(new GuiEditConfig(mc.currentScreen, m.extraNBT, new ConfigContainer(m.id)
        {
            @Override
            public ConfigGroup createGroup()
            {
                ConfigGroup group = new ConfigGroup();
                group.readFromNBT(m.groupData, true);
                return group;
            }

            @Override
            public ITextComponent getConfigTitle()
            {
                return m.title;
            }

            @Override
            public void saveConfig(EntityPlayer player, NBTTagCompound nbt, ConfigGroup config)
            {
                new MessageEditConfigResponse(m.id, nbt, config).sendToServer();
            }
        }));
    }
}