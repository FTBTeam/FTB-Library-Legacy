package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.config.IConfigContainer;
import com.feed_the_beast.ftbl.api.config.IConfigTree;
import com.google.gson.JsonObject;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class BasicConfigContainer implements IConfigContainer
{
    private final ITextComponent title;
    private final IConfigTree tree;

    public BasicConfigContainer(ITextComponent n, IConfigTree t)
    {
        title = n;
        tree = t;
    }

    @Override
    public IConfigTree getConfigTree()
    {
        return tree;
    }

    @Override
    public ITextComponent getTitle()
    {
        return title;
    }

    @Override
    public void saveConfig(ICommandSender sender, @Nullable NBTTagCompound nbt, JsonObject json)
    {
        getConfigTree().fromJson(json);
    }
}