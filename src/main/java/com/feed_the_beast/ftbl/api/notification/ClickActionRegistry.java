package com.feed_the_beast.ftbl.api.notification;

import latmod.lib.util.LMStringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ClickActionRegistry
{
    private static final Map<String, ClickActionType> map = new HashMap<>();

    public static ClickActionType register(ClickActionType a)
    {
        if(a != null && LMStringUtils.isValid(a.getID()) && !map.containsKey(a.getID()))
        {
            map.put(a.getID(), a);
        }

        return a;
    }

    public static Collection<String> getKeys()
    {
        return map.keySet();
    }

    public static ClickActionType get(String s)
    {
        return map.get(s);
    }
}