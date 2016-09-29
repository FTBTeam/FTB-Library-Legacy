package com.feed_the_beast.ftbl.api.gui;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 29.09.2016.
 */
public interface IGuiSelectors
{
    interface StringCallback
    {
        void onStringCallback(@Nullable Object id, String value);
    }

    void selectString(@Nullable Object ID, String def, StringCallback callback);

    interface IntCallback
    {
        void onIntCallback(@Nullable Object id, int value);
    }

    void selectInt(@Nullable Object ID, int def, IntCallback callback);

    interface DoubleCallback
    {
        void onDoubleCallback(@Nullable Object id, double value);
    }

    void selectDouble(@Nullable Object ID, double def, DoubleCallback callback);

    interface ColorCallback
    {
        void onColorCallback(@Nullable Object id, byte value);
    }

    void selectColor(@Nullable Object ID, byte def, ColorCallback callback);
}