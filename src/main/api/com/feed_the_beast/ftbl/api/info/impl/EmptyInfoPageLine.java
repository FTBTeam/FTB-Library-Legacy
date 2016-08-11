package com.feed_the_beast.ftbl.api.info.impl;

import com.feed_the_beast.ftbl.api.info.IInfoTextLine;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 08.08.2016.
 */
public abstract class EmptyInfoPageLine implements IInfoTextLine
{
    @Nullable
    @Override
    public String getUnformattedText()
    {
        return null;
    }

    @Override
    public void fromJson(@Nonnull JsonElement json)
    {
    }

    @Override
    @Nonnull
    public JsonElement getSerializableElement()
    {
        return JsonNull.INSTANCE;
    }
}
