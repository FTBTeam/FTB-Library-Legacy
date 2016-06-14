package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.util.FTBLib;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import latmod.lib.json.LMJsonUtils;
import latmod.lib.util.LMFileUtils;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class PackModes
{
    private static PackModes inst = null;
    private final Map<String, PackMode> modes;
    private final PackMode defaultMode;
    private final PackMode commonMode;
    private final Map<String, JsonElement> customData;

    public PackModes(JsonElement el)
    {
        JsonObject o = isValid(el) ? el.getAsJsonObject() : createDefault();

        Map<String, PackMode> modes0 = new HashMap<>();

        JsonArray a = o.get("modes").getAsJsonArray();

        for(int i = 0; i < a.size(); i++)
        {
            PackMode m = new PackMode(a.get(i).getAsString());
            modes0.put(m.getID(), m);
        }

        defaultMode = modes0.get(o.get("default").getAsString());

        if(modes0.containsKey("common"))
        {
            throw new RuntimeException("FTBLib: 'common' can't be one of 'modes'!");
        }

        commonMode = new PackMode("common");

        modes = Collections.unmodifiableMap(modes0);

        Map<String, JsonElement> customData0 = new HashMap<>();

        if(o.has("custom"))
        {
            JsonObject o1 = o.get("custom").getAsJsonObject();

            for(Map.Entry<String, JsonElement> e : o1.entrySet())
            {
                customData0.put(e.getKey(), e.getValue());
            }
        }

        customData = Collections.unmodifiableMap(customData0);
    }

    private static JsonObject createDefault()
    {
        JsonObject o = new JsonObject();
        JsonArray a = new JsonArray();
        a.add(new JsonPrimitive("default"));
        o.add("modes", a);
        o.add("default", new JsonPrimitive("default"));
        o.add("common", new JsonPrimitive("common"));
        return o;
    }

    private static boolean isValid(JsonElement e)
    {
        if(e == null || !e.isJsonObject())
        {
            return false;
        }
        JsonObject o = e.getAsJsonObject();
        return !(o == null || o.entrySet().isEmpty()) && o.has("modes") && o.has("default") && o.has("common");
    }

    // Static //

    public static void reload()
    {
        File file = LMFileUtils.newFile(new File(FTBLib.folderModpack, "packmodes.json"));
        inst = new PackModes(LMJsonUtils.fromJson(file));
        LMJsonUtils.toJson(file, inst.toJsonObject());
    }

    public static PackModes instance()
    {
        if(inst == null)
        {
            reload();
        }
        return inst;
    }

    public JsonObject toJsonObject()
    {
        JsonObject o = new JsonObject();

        JsonArray a = new JsonArray();

        for(PackMode m : modes.values())
        {
            a.add(new JsonPrimitive(m.getID()));
        }

        o.add("modes", a);

        o.add("default", new JsonPrimitive(defaultMode.getID()));
        o.add("common", new JsonPrimitive(commonMode.getID()));

        if(!customData.isEmpty())
        {
            JsonObject o1 = new JsonObject();

            for(Map.Entry<String, JsonElement> e : customData.entrySet())
            {
                o1.add(e.getKey(), e.getValue());
            }

            o.add("custom", o1);
        }

        return o;
    }

    public Collection<String> getModes()
    {
        return modes.keySet();
    }

    public PackMode getRawMode(String s)
    {
        return (s == null || s.isEmpty()) ? null : modes.get(s);
    }

    public PackMode getMode(String s)
    {
        if(s == null || s.isEmpty())
        {
            return defaultMode;
        }

        PackMode m = modes.get(s);
        return (m == null) ? defaultMode : m;
    }

    public PackMode getDefault()
    {
        return defaultMode;
    }

    public PackMode getCommon()
    {
        return commonMode;
    }

    public JsonElement getCustomData(String s)
    {
        return (s != null && !s.isEmpty() && customData.containsKey(s)) ? customData.get(s) : JsonNull.INSTANCE;
    }
}