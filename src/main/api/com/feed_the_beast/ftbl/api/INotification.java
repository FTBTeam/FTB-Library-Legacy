package com.feed_the_beast.ftbl.api;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

/**
 * Created by LatvianModder on 17.08.2016.
 */
public interface INotification
{
    int getID();

    List<ITextComponent> getText();

    ItemStack getItem();

    boolean isTemp();

    int getTimer();

    int getColor();
}