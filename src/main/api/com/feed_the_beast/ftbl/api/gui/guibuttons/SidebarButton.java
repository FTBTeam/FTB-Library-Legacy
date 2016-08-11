package com.feed_the_beast.ftbl.api.gui.guibuttons;

import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.gui.GuiLM;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.latmod.lib.TextureCoords;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public abstract class SidebarButton
{
    public final int priority;
    public final TextureCoords icon;
    public final Boolean configDefault;

    public SidebarButton(int p, TextureCoords c, Boolean b)
    {
        priority = p;
        icon = c;
        configDefault = b;
    }

    @SideOnly(Side.CLIENT)
    public abstract void onClicked(IMouseButton button);

    @Nullable
    public ITextComponent getDisplayNameOverride()
    {
        return null;
    }

    @SideOnly(Side.CLIENT)
    public boolean isVisibleFor(IForgePlayer player)
    {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public void render(Minecraft mc, double ax, double ay)
    {
        GuiLM.render(icon, ax, ay, 16D, 16D);
    }

    @SideOnly(Side.CLIENT)
    public void postRender(Minecraft mc, double ax, double ay)
    {
    }

    public boolean isVisible()
    {
        return true;
    }
}