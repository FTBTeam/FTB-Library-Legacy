package com.feed_the_beast.ftbl.api.config;

import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by LatvianModder on 11.09.2016.
 */
public interface IConfigValueProvider
{
    ResourceLocation getID();

    IConfigValue createDefault();

    int getColor(IConfigValue value);

    @Nullable
    default String getMinValue(IConfigValue value)
    {
        return null;
    }

    @Nullable
    default String getMaxValue(IConfigValue value)
    {
        return null;
    }

    @Nullable
    default List getVariants()
    {
        return null;
    }

    default boolean onClicked(IConfigValue value, IMouseButton button)
    {
        return false;
    }
}