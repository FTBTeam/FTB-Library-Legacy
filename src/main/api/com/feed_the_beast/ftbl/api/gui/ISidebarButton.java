package com.feed_the_beast.ftbl.api.gui;

import com.feed_the_beast.ftbl.api.config.IConfigValue;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 16.08.2016.
 */
public interface ISidebarButton
{
    default int getPriority()
    {
        return 0;
    }

    @Nullable
    IImageProvider getIcon();

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

    void setPath(String path);

    String getPath();
}