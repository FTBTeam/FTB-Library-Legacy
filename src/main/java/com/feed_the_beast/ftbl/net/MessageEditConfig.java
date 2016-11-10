package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.config.IConfigContainer;
import com.feed_the_beast.ftbl.api.config.IConfigTree;
import com.feed_the_beast.ftbl.api_impl.FTBLibRegistries;
import com.feed_the_beast.ftbl.lib.config.ConfigTree;
import com.feed_the_beast.ftbl.lib.gui.GuiEditConfig;
import com.feed_the_beast.ftbl.lib.net.LMNetworkWrapper;
import com.feed_the_beast.ftbl.lib.net.MessageToClient;
import com.feed_the_beast.ftbl.lib.util.LMNetUtils;
import com.feed_the_beast.ftbl.lib.util.LMUtils;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;
import java.util.UUID;

public class MessageEditConfig extends MessageToClient<MessageEditConfig> // MessageEditConfigResponse
{
    private IConfigTree group;
    private NBTTagCompound extraNBT;
    private ITextComponent title;

    public MessageEditConfig()
    {
    }

    public MessageEditConfig(UUID id, @Nullable NBTTagCompound nbt, IConfigContainer c)
    {
        FTBLibRegistries.INSTANCE.TEMP_SERVER_CONFIG.put(id, c);
        group = c.getConfigTree().copy();
        extraNBT = nbt;
        title = c.getTitle();

        if(LMUtils.DEV_ENV)
        {
            LMUtils.DEV_LOGGER.info("TX Send: " + group.getTree());
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
        group.readFromServer(io);
    }

    @Override
    public void toBytes(ByteBuf io)
    {
        LMNetUtils.writeTag(io, extraNBT);
        LMNetUtils.writeTextComponent(io, title);
        group.writeToServer(io);
    }

    @Override
    public void onMessage(final MessageEditConfig m)
    {
        if(LMUtils.DEV_ENV)
        {
            LMUtils.DEV_LOGGER.info("RX Send: " + m.group.getTree());
        }

        new GuiEditConfig(m.extraNBT, new IConfigContainer()
        {
            @Override
            public IConfigTree getConfigTree()
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