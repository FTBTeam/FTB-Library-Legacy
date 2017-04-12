package com.feed_the_beast.ftbl.api.gui;

import com.feed_the_beast.ftbl.api.config.IConfigValue;
import net.minecraft.util.IStringSerializable;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Created by LatvianModder on 16.08.2016.
 */
public interface ISidebarButton extends IStringSerializable
{
    @Nullable
    IDrawableObject getIcon();

    @Nullable
    IConfigValue getConfig();

    void onClicked(IMouseButton button);

    default void postRender(int ax, int ay)
    {
    }

    default boolean isVisible()
    {
        return true;
    }

    Map<String, Boolean> getDependencies();
}