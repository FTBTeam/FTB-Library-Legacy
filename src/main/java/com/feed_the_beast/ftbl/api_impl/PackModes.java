package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.api.IPackMode;
import com.feed_the_beast.ftbl.api.IPackModes;
import com.feed_the_beast.ftbl.lib.util.LMFileUtils;
import com.feed_the_beast.ftbl.lib.util.LMJsonUtils;
import com.feed_the_beast.ftbl.lib.util.LMUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import javax.annotation.Nullable;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum PackModes implements IPackModes
{
    INSTANCE;

    private Map<String, IPackMode> modes;
    private IPackMode defaultMode;
    private Map<String, JsonElement> customData;

    public void load()
    {
        File file = LMFileUtils.newFile(new File(LMUtils.folderModpack, "packmodes.json"));
        JsonElement el = LMJsonUtils.fromJson(file);

        JsonObject o = isValid(el) ? el.getAsJsonObject() : createDefault();

        Map<String, IPackMode> modes0 = new HashMap<>();

        JsonArray a = o.get("modes").getAsJsonArray();

        for(int i = 0; i < a.size(); i++)
        {
            IPackMode m = new PackMode(a.get(i).getAsString());
            modes0.put(m.getName(), m);
        }

        defaultMode = modes0.get(o.get("default").getAsString());

        if(modes0.containsKey("common"))
        {
            throw new RuntimeException("FTBLib: 'common' can't be one of 'modes'!");
        }

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
        LMJsonUtils.toJson(file, INSTANCE.toJsonObject());
    }

    private boolean isValid(JsonElement e)
    {
        if(!e.isJsonObject())
        {
            return false;
        }

        JsonObject o = e.getAsJsonObject();
        return o.has("modes") && o.has("default");
    }

    private JsonObject createDefault()
    {
        JsonObject o = new JsonObject();
        JsonArray a = new JsonArray();
        a.add(new JsonPrimitive("default"));
        o.add("modes", a);
        o.add("default", new JsonPrimitive("default"));
        return o;
    }

    public JsonObject toJsonObject()
    {
        JsonObject o = new JsonObject();

        JsonArray a = new JsonArray();

        for(IPackMode m : modes.values())
        {
            a.add(new JsonPrimitive(m.getName()));
        }

        o.add("modes", a);

        o.add("default", new JsonPrimitive(defaultMode.getName()));

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

    @Override
    public Collection<IPackMode> getModes()
    {
        return modes.values();
    }

    @Override
    @Nullable
    public IPackMode getRawMode(String id)
    {
        return id.isEmpty() ? null : modes.get(id);
    }

    @Override
    public IPackMode getMode(String id)
    {
        if(id.isEmpty())
        {
            return defaultMode;
        }

        IPackMode m = modes.get(id);
        return (m == null) ? defaultMode : m;
    }

    @Override
    public IPackMode getDefault()
    {
        return defaultMode;
    }

    @Override
    public JsonElement getCustomData(String id)
    {
        JsonElement e = customData.get(id);
        return e == null ? JsonNull.INSTANCE : e;
    }
}