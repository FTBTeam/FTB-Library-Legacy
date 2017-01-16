package com.feed_the_beast.ftbl.lib;

import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.gui.IImageProvider;
import com.feed_the_beast.ftbl.api.gui.ISidebarButton;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;

import javax.annotation.Nullable;

public abstract class SidebarButton implements ISidebarButton
{
    private final int priority;
    private final IImageProvider icon;
    private final IConfigValue config;
    private String path = "";

    public SidebarButton(int p, IImageProvider c, @Nullable IConfigValue b)
    {
        priority = p;
        icon = c;
        config = b;
    }

    @Override
    public int getPriority()
    {
        return priority;
    }

    @Override
    public IImageProvider getIcon()
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
    public void setPath(String p)
    {
        path = p;
    }

    @Override
    public String getPath()
    {
        return path;
    }
}