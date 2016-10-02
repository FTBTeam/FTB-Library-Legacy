package com.feed_the_beast.ftbl.api.info;

import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * Created by LatvianModder on 08.08.2016.
 */
public interface IInfoPage extends IStringSerializable, IJsonSerializable
{
    @Nullable
    IInfoPage getParent();

    void setParent(@Nullable IInfoPage parent);

    @Nullable
    ITextComponent getTitle();

    IInfoPage setTitle(@Nullable ITextComponent title);

    List<IInfoTextLine> getText();

    void println(@Nullable Object o);

    Map<String, ? extends IInfoPage> getPages();

    IInfoPage getSub(String id);

    default String getFullID()
    {
        IInfoPage parent = getParent();
        return (parent == null) ? getName() : (parent.getFullID() + '.' + getName());
    }

    default ITextComponent getDisplayName()
    {
        ITextComponent t = getTitle();
        return (t == null) ? new TextComponentString(getName()) : t;
    }

    void clear();

    void cleanup();

    void sortAll();

    IInfoPage copy();
}