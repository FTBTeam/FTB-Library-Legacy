package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.gui.GuiLM;
import com.feed_the_beast.ftbl.api.gui.ISidebarButton;
import com.latmod.lib.EnumEnabled;
import com.latmod.lib.TextureCoords;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public abstract class SidebarButton implements ISidebarButton
{
    private final int priority;
    private final TextureCoords icon;
    private final EnumEnabled configDefault;

    public SidebarButton(int p, TextureCoords c, EnumEnabled b)
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
    public TextureCoords getIcon()
    {
        return icon;
    }

    @Override
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
    @SideOnly(Side.CLIENT)
    public boolean isVisibleFor(IForgePlayer player)
    {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void render(Minecraft mc, int ax, int ay)
    {
        GuiLM.render(icon, ax, ay, 16, 16);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void postRender(Minecraft mc, int ax, int ay)
    {
    }

    @Override
    public boolean isVisible()
    {
        return true;
    }
}