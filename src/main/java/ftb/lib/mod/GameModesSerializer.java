package ftb.lib.mod;

import java.lang.reflect.Type;
import java.util.*;

import com.google.gson.*;

import ftb.lib.api.GameModes;
import latmod.lib.FastList;

public class GameModesSerializer implements JsonSerializer<GameModes>, JsonDeserializer<GameModes>
{
	public JsonElement serialize(GameModes src, Type typeOfSrc, JsonSerializationContext context)
	{
		if(src == null) return null;
		
		JsonObject o = new JsonObject();
		
		o.add("default", new JsonPrimitive(src.defaultMode));
		o.add("common", new JsonPrimitive(src.commonMode));
		
		JsonArray a = new JsonArray();
		for(int i = 0; i < src.allModes.size(); i++)
			a.add(new JsonPrimitive(src.allModes.get(i)));
		o.add("modes", a);
		
		if(!src.customData.isEmpty())
		{
			JsonObject o1 = new JsonObject();
			for(String key : src.customData.keySet())
				o1.add(key, new JsonPrimitive(src.customData.get(key)));
			o.add("custom", o1);
		}
		
		return o;
	}
	
	public GameModes deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	{
		if(json.isJsonNull() || !json.isJsonObject()) return null;
		JsonObject o = json.getAsJsonObject();
		
		if(!o.has("default") || !o.has("common") || !o.has("modes")) return null;
		
		String def = o.get("default").getAsString();
		String common = o.get("common").getAsString();
		
		FastList<String> allModes = new FastList<String>();
		JsonArray a = o.get("modes").getAsJsonArray();
		for(int i = 0; i < a.size(); i++)
			allModes.add(a.get(i).getAsString());
		allModes.removeObj(common);
		
		HashMap<String, String> custom = new HashMap<String, String>();
		
		if(o.has("custom"))
		{
			JsonObject o1 = o.get("custom").getAsJsonObject();
			for(Map.Entry<String, JsonElement> e : o1.entrySet())
				custom.put(e.getKey(), e.getValue().getAsString());
		}
		
		allModes.setLocked();
		return new GameModes(allModes, def, common, custom);
	}
}