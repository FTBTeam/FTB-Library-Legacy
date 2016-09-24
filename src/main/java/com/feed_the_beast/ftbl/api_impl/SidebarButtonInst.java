package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.api.gui.GuiHelper;
import com.feed_the_beast.ftbl.api.gui.ISidebarButton;
import com.latmod.lib.EnumEnabled;
import com.latmod.lib.client.ITextureCoords;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

public abstract class SidebarButtonInst implements ISidebarButton
{
    private final ResourceLocation ID;
    private final int priority;
    private final ITextureCoords icon;
    private final EnumEnabled configDefault;

    public SidebarButtonInst(ResourceLocation id, int p, ITextureCoords c, @Nullable EnumEnabled b)
    {
        ID = id;
        priority = p;
        icon = c;
        configDefault = b;
    }

    @Override
    public ResourceLocation getID()
    {
        return ID;
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