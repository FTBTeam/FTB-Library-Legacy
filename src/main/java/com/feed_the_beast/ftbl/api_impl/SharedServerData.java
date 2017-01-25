package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.api.IPackMode;
import com.feed_the_beast.ftbl.api.ISharedServerData;
import com.feed_the_beast.ftbl.lib.internal.FTBLibIntegrationInternal;
import com.feed_the_beast.ftbl.lib.util.LMStringUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.util.IJsonSerializable;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 12.11.2016.
 */
public class SharedServerData extends SharedData implements ISharedServerData, IJsonSerializable
{
    public static final SharedServerData INSTANCE = new SharedServerData();

    private SharedServerData()
    {
    }

    public int setMode(String mode)
    {
        IPackMode m = FTBLibIntegrationInternal.API.getPackModes().getRawMode(mode);

        if(m == null)
        {
            return 1;
        }
        if(m.equals(getPackMode()))
        {
            return 2;
        }

        currentMode = m;
        return 0;
    }

    @Override
    public boolean hasOptionalServerMod(@Nullable String id)
    {
        return id == null || optionalServerMods.contains(id);
    }

    @Override
    public void fromJson(JsonElement json)
    {
        JsonObject group = json.getAsJsonObject();
        universeID = group.has("world_id") ? LMStringUtils.fromString(group.get("world_id").getAsString()) : null;
        currentMode = group.has("mode") ? FTBLibIntegrationInternal.API.getPackModes().getMode(group.get("mode").getAsString()) : FTBLibIntegrationInternal.API.getPackModes().getDefault();
    }

    @Override
    public JsonElement getSerializableElement()
    {
        JsonObject o = new JsonObject();
        o.add("world_id", new JsonPrimitive(LMStringUtils.fromUUID(getUniverseID())));
        o.add("mode", new JsonPrimitive(getPackMode().getName()));
        return o;
    }
}