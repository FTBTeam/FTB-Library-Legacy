package com.feed_the_beast.ftbl.api.info;

import com.latmod.lib.IIDObject;
import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by LatvianModder on 08.08.2016.
 */
public interface IInfoPage extends IIDObject, IJsonSerializable
{
    @Nullable
    IInfoPage getParent();

    @Nullable
    ITextComponent getName();

    @Nonnull
    List<IInfoTextLine> getText();

    @Nonnull
    List<? extends IInfoPage> getPages();

    @Nonnull
    default String getFullID()
    {
        IInfoPage parent = getParent();
        return (parent == null) ? getID() : (parent.getFullID() + '.' + getID());
    }

    @Nonnull
    default ITextComponent getDisplayName()
    {
        ITextComponent t = getName();
        return (t == null) ? new TextComponentString(getID()) : t;
    }
}