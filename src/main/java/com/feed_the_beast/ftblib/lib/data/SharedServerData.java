package com.feed_the_beast.ftblib.lib.data;

import com.feed_the_beast.ftblib.lib.util.StringUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.IJsonSerializable;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author LatvianModder
 */
public class SharedServerData extends SharedData implements IJsonSerializable
{
	public static final SharedServerData INSTANCE = new SharedServerData();

	private SharedServerData()
	{
	}

	@Override
	public Side getSide()
	{
		return Side.SERVER;
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