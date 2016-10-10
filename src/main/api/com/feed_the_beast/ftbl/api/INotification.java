package com.feed_the_beast.ftbl.api;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by LatvianModder on 17.08.2016.
 */
public interface INotification extends IRegistryObject
{
    ResourceLocation getID();

    byte getVariant();

    List<ITextComponent> getText();

    @Nullable
    ItemStack getItem();

    boolean isPermanent();

    short getTimer();

    byte getColorID();
}