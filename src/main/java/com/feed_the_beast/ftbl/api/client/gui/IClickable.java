package com.feed_the_beast.ftbl.api.client.gui;

import com.feed_the_beast.ftbl.api.MouseButton;

import javax.annotation.Nonnull;

public interface IClickable
{
    void onClicked(@Nonnull MouseButton button);
}