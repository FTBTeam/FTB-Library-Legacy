package com.feed_the_beast.ftbl.api.gui;

import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.lib.client.ITextureCoords;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 16.08.2016.
 */
public interface ISidebarButton
{
    ResourceLocation getID();

    default int getPriority()
    {
        return 0;
    }

    @Nullable
    ITextureCoords getIcon();

    @Nullable
    IConfigValue getConfig();

    void onClicked(IMouseButton button);

    @Nullable
    default ITextComponent getDisplayNameOverride()
    {
        return null;
    }

    void render(int ax, int ay);

    default void postRender(int ax, int ay)
    {
    }

    default boolean isVisible()
    {
        return true;
    }

    default String getPath()
    {
        return "sidebar_button." + getID().getResourceDomain() + '.' + getID().getResourcePath();
    }
}