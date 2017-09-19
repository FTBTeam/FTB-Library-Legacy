package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.api.ISharedServerData;
import com.feed_the_beast.ftbl.lib.util.StringUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.IJsonSerializable;

/**
 * @author LatvianModder
 */
public class SharedServerData extends SharedData implements ISharedServerData, IJsonSerializable
{
	public static final SharedServerData INSTANCE = new SharedServerData();

	private SharedServerData()
	{
	}

	@Override
	public void fromJson(JsonElement json)
	{
		JsonObject group = json.getAsJsonObject();
		universeId = group.has("world_id") ? StringUtils.fromString(group.get("world_id").getAsString()) : null;
	}

	@Override
	public JsonElement getSerializableElement()
	{
		JsonObject o = new JsonObject();
		o.addProperty("world_id", StringUtils.fromUUID(getUniverseId()));
		return o;
	}
}