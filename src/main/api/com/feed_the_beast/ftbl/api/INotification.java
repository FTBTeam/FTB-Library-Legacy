package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.lib.Color4I;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

/**
 * @author LatvianModder
 */
public interface INotification
{
    NotificationId getId();

    List<ITextComponent> getText();

    IDrawableObject getIcon();

    int getTimer();

    Color4I getColor();
}