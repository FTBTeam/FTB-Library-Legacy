package com.feed_the_beast.ftbl.api.config;

import com.latmod.lib.annotations.IFlagContainer;
import com.latmod.lib.annotations.IInfoContainer;
import com.latmod.lib.io.Bits;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 11.09.2016.
 */
public interface IConfigKey extends IStringSerializable, IInfoContainer, IFlagContainer
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

    /**
     * Use display name as I18n key
     */
    int TRANSLATE_DISPLAY_NAME = 16;

    IConfigValue getDefValue();

    @Nullable
    String getRawDisplayName();

    default ITextComponent getDisplayName()
    {
        String s = getRawDisplayName();

        if(s != null && !s.isEmpty())
        {
            if(Bits.getFlag(getFlags(), TRANSLATE_DISPLAY_NAME))
            {
                //TODO: Replace with client side I18n
                return new TextComponentTranslation(s);
            }

            return new TextComponentString(s);
        }

        return new TextComponentString(getName());
    }
}