package com.feed_the_beast.ftbl.api.info;

import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by LatvianModder on 08.08.2016.
 */
public interface IInfoPage extends IStringSerializable, IJsonSerializable
{
    @Nullable
    IInfoPage getParent();

    @Nullable
    ITextComponent getTitle();

    @Nonnull
    List<IInfoTextLine> getText();

    @Nonnull
    List<? extends IInfoPage> getPages();

    @Nonnull
    default String getFullID()
    {
        IInfoPage parent = getParent();
        return (parent == null) ? getName() : (parent.getFullID() + '.' + getName());
    }

    @Nonnull
    default ITextComponent getDisplayName()
    {
        ITextComponent t = getTitle();
        return (t == null) ? new TextComponentString(getName()) : t;
    }
}