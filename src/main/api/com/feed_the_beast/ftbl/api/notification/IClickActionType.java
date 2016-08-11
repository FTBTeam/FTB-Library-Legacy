package com.feed_the_beast.ftbl.api.notification;

import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.google.gson.JsonElement;

import javax.annotation.Nonnull;

public interface IClickActionType
{
    void onClicked(@Nonnull JsonElement data, @Nonnull IMouseButton button);
}