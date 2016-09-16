package com.feed_the_beast.ftbl.api.config;

import com.latmod.lib.annotations.IFlagContainer;
import com.latmod.lib.annotations.IInfoContainer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 11.09.2016.
 */
public interface IConfigKey extends IStringSerializable, IInfoContainer, IFlagContainer
{
    IConfigValue getDefValue();

    @Nullable
    ITextComponent getRawDisplayName();

    default ITextComponent getDisplayName()
    {
        ITextComponent t = getRawDisplayName();
        return t == null ? new TextComponentString(getName()) : t;
    }
}