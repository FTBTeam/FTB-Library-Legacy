package com.feed_the_beast.ftbl.api;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by LatvianModder on 17.08.2016.
 */
public interface INotification
{
    NotificationId getId();

    List<ITextComponent> getText();

    @Nullable
    ItemStack getItem();

    int getTimer();

    int getColor();
}