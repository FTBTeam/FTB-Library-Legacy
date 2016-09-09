package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.api.gui.GuiHelper;
import com.feed_the_beast.ftbl.api.gui.ISidebarButton;
import com.latmod.lib.EnumEnabled;
import com.latmod.lib.ITextureCoords;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

public abstract class SidebarButton implements ISidebarButton
{
    private final int priority;
    private final ITextureCoords icon;
    private final EnumEnabled configDefault;

    public SidebarButton(int p, ITextureCoords c, @Nullable EnumEnabled b)
    {
        priority = p;
        icon = c;
        configDefault = b;
    }

    @Override
    public int getPriority()
    {
        return priority;
    }

    @Override
    public ITextureCoords getIcon()
    {
        return icon;
    }

    @Override
    @Nullable
    public EnumEnabled getConfigDefault()
    {
        return configDefault;
    }

    @Override
    @Nullable
    public ITextComponent getDisplayNameOverride()
    {
        return null;
    }

    @Override
    public void render(int ax, int ay)
    {
        GuiHelper.render(icon, ax, ay, 16, 16);
    }

    @Override
    public void postRender(int ax, int ay)
    {
    }

    @Override
    public boolean isVisible()
    {
        return true;
    }
}