package com.feed_the_beast.ftbl.api.config;

import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 11.09.2016.
 */
public interface IConfigKey extends IStringSerializable
{
    /**
     * Will be excluded from writing / reading from files
     */
    int EXCLUDED = 1;

    /**
     * Will be hidden from config gui
     */
    int HIDDEN = 2;

    /**
     * Will be visible in config gui, but uneditable
     */
    int CANT_EDIT = 4;

    /**
     * Use scroll bar on numbers whenever that is available
     */
    int USE_SCROLL_BAR = 8;

    int getFlags();

    default boolean getFlag(int flag)
    {
        return (getFlags() & flag) != 0;
    }

    IConfigValue getDefValue();

    @Nullable
    ITextComponent getRawDisplayName();

    default ITextComponent getDisplayName()
    {
        ITextComponent t = getRawDisplayName();
        return t == null ? new TextComponentTranslation("config." + getName() + ".name") : t;
    }

    String getInfo();
}