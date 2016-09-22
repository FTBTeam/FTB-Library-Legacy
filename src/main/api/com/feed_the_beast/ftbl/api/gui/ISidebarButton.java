package com.feed_the_beast.ftbl.api.gui;

import com.latmod.lib.EnumEnabled;
import com.latmod.lib.client.ITextureCoords;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 16.08.2016.
 */
public interface ISidebarButton
{
    int getPriority();

    @Nullable
    ITextureCoords getIcon();

    @Nullable
    EnumEnabled getConfigDefault();

    void onClicked(IMouseButton button);

    @Nullable
    ITextComponent getDisplayNameOverride();

    void render(int ax, int ay);

    void postRender(int ax, int ay);

    boolean isVisible();
}