package com.feed_the_beast.ftbl.api.info;

import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * Created by LatvianModder on 08.08.2016.
 */
public interface IInfoPage extends IJsonSerializable
{
    @Nullable
    ITextComponent getName();

    @Nonnull
    List<IInfoTextLine> getText();

    @Nonnull
    Map<String, ? extends IInfoPage> getPages();
}