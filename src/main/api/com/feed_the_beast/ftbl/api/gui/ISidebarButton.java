package com.feed_the_beast.ftbl.api.gui;

import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.latmod.lib.EnumEnabled;
import com.latmod.lib.TextureCoords;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 16.08.2016.
 */
public interface ISidebarButton
{
    int getPriority();

    TextureCoords getIcon();

    EnumEnabled getConfigDefault();

    @SideOnly(Side.CLIENT)
    void onClicked(IMouseButton button);

    @Nullable
    ITextComponent getDisplayNameOverride();

    @SideOnly(Side.CLIENT)
    boolean isVisibleFor(IForgePlayer player);

    @SideOnly(Side.CLIENT)
    void render(Minecraft mc, int ax, int ay);

    @SideOnly(Side.CLIENT)
    void postRender(Minecraft mc, int ax, int ay);

    boolean isVisible();
}