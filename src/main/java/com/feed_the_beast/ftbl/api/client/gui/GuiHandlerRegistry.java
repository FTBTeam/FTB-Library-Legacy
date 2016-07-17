package com.feed_the_beast.ftbl.api.client.gui;

import java.util.HashMap;
import java.util.Map;

public class GuiHandlerRegistry
{
    private static final Map<String, GuiHandler> map = new HashMap<>();

    public static void add(GuiHandler h)
    {
        if(h != null && !map.containsKey(h.ID))
        {
            map.put(h.ID, h);
        }
    }

    public static GuiHandler get(String id)
    {
        return map.get(id);
    }
}