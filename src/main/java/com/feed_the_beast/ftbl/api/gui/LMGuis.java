package com.feed_the_beast.ftbl.api.gui;

import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.gui.GuiSelectColor;
import com.feed_the_beast.ftbl.gui.GuiSelectField;
import latmod.lib.Converter;
import latmod.lib.LMColor;
import latmod.lib.ObjectCallback;

public class LMGuis
{
    public enum FieldType
    {
        STRING,
        INTEGER,
        DOUBLE;

        public boolean isValid(String s)
        {
            switch(this)
            {
                case INTEGER:
                {
                    return Converter.canParseInt(s);
                }
                case DOUBLE:
                {
                    return Converter.canParseDouble(s);
                }
                default:
                {
                    return true;
                }
            }
        }
    }

    public static void displayColorSelector(Object id, boolean instant, LMColor col, ObjectCallback.Handler cb)
    {
        FTBLibClient.mc().displayGuiScreen(new GuiSelectColor(cb, col, id, instant));
    }

    public static void displayFieldSelector(Object id, FieldType typ, Object d, ObjectCallback.Handler c)
    {
        FTBLibClient.mc().displayGuiScreen(new GuiSelectField(id, typ, String.valueOf(d), c));
    }
}