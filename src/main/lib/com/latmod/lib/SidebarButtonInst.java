package com.latmod.lib;

import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.gui.GuiHelper;
import com.feed_the_beast.ftbl.api.gui.ISidebarButton;
import com.latmod.lib.client.ITextureCoords;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public abstract class SidebarButtonInst implements ISidebarButton
{
    private final ResourceLocation ID;
    private final int priority;
    private final ITextureCoords icon;
    private final IConfigValue config;
    private final String path;

    public SidebarButtonInst(ResourceLocation id, int p, ITextureCoords c, @Nullable IConfigValue b)
    {
        ID = id;
        priority = p;
        icon = c;
        config = b;
        path = "sidebar_button." + getID().getResourceDomain() + '.' + getID().getResourcePath();
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
    public IConfigValue getConfig()
    {
        return config;
    }

    @Override
    public void render(int ax, int ay)
    {
        GuiHelper.render(icon, ax, ay, 16, 16);
    }

    @Override
    public String getPath()
    {
        return path;
    }
}