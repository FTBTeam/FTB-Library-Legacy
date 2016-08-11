package com.feed_the_beast.ftbl.api.gui;

import javax.annotation.Nonnull;

public interface IClickable
{
    void onClicked(@Nonnull IMouseButton button);
}