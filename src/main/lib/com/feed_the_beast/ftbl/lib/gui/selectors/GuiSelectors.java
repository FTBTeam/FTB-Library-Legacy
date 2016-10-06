package com.feed_the_beast.ftbl.lib.gui.selectors;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 29.09.2016.
 */
public class GuiSelectors
{
    public interface StringCallback
    {
        void onStringCallback(@Nullable Object id, String value);
    }

    public static void selectString(@Nullable Object ID, String def, StringCallback callback)
    {
        new GuiStringField(ID, def, callback).openGui();
    }

    public interface IntCallback
    {
        void onIntCallback(@Nullable Object id, int value);
    }

    public static void selectInt(@Nullable Object ID, int def, IntCallback callback)
    {
        new GuiIntField(ID, def, callback).openGui();
    }

    public interface DoubleCallback
    {
        void onDoubleCallback(@Nullable Object id, double value);
    }

    public static void selectDouble(@Nullable Object ID, double def, DoubleCallback callback)
    {
        new GuiDoubleField(ID, def, callback).openGui();
    }

    public interface ColorCallback
    {
        void onColorCallback(@Nullable Object id, byte value);
    }

    public static void selectColor(@Nullable Object ID, byte def, ColorCallback callback)
    {
        new GuiColorField(ID, def, callback).openGui();
    }
}
