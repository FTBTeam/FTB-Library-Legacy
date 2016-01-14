package ftb.lib.api;

import com.google.gson.*;
import ftb.lib.FTBLib;
import latmod.lib.*;

import java.io.File;
import java.lang.reflect.Type;
import java.util.*;

public class GameModes
{
	public final Map<String, GameMode> modes;
	public final GameMode defaultMode;
	public final GameMode commonMode;
	public final Map<String, String> customData;
	
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
	
	private static boolean isValid(JsonObject o)
	{
		if(o == null || o.entrySet().isEmpty()) return false;
		return o.has("modes") && o.has("default") && o.has("common");
	}
	
	public GameModes(JsonObject o)
	{
		if(!isValid(o)) o = createDefault();
		
		HashMap<String, GameMode> modes0 = new HashMap<>();
		
		JsonArray a = o.get("modes").getAsJsonArray();
		
		for(int i = 0; i < a.size(); i++)
		{
			GameMode m = new GameMode(a.get(i).getAsString());
			modes0.put(m.ID, m);
		}
		
		defaultMode = modes0.get(o.get("default").getAsString());
		
		String common = o.get("common").getAsString();
		if(modes0.containsKey(common)) throw new RuntimeException("FTBLib: common mode name can't be one of 'modes'!");
		commonMode = new GameMode(common);
		
		modes = Collections.unmodifiableMap(modes0);
		
		HashMap<String, String> customData0 = new HashMap<>();
		
		if(o.has("custom"))
		{
			JsonObject o1 = o.get("custom").getAsJsonObject();
			for(Map.Entry<String, JsonElement> e : o1.entrySet())
				customData0.put(e.getKey(), e.getValue().getAsString());
		}
		
		customData = Collections.unmodifiableMap(customData0);
	}
	
	// Static //
	
	private static File gamemodesJsonFile = null;
	private static GameModes gameModes = null;
	
	public static void reload()
	{
		if(gamemodesJsonFile == null)
			gamemodesJsonFile = LMFileUtils.newFile(new File(FTBLib.folderModpack, "gamemodes.json"));
		gameModes = (GameModes) LMJsonUtils.fromJsonFile(Serializer.getGson(), gamemodesJsonFile, GameModes.class);
		if(gameModes == null)
		{
			gameModes = new GameModes(createDefault());
			LMJsonUtils.toJsonFile(Serializer.getGson(), gamemodesJsonFile, gameModes);
		}
	}
	
	public static GameModes getGameModes()
	{ return gameModes; }
	
	public GameMode get(String s)
	{
		if(s == null || s.isEmpty()) return defaultMode;
		GameMode m = modes.get(s);
		return (m == null) ? defaultMode : m;
	}
	
	// Serializer //
	
	private static class Serializer implements JsonSerializer<GameModes>, JsonDeserializer<GameModes>
	{
		private static Gson gson = null;
		
		public JsonElement serialize(GameModes src, Type typeOfSrc, JsonSerializationContext context)
		{
			if(src == null) return null;
			
			JsonObject o = new JsonObject();
			
			o.add("default", new JsonPrimitive(src.defaultMode.ID));
			o.add("common", new JsonPrimitive(src.commonMode.ID));
			
			JsonArray a = new JsonArray();
			for(int i = 0; i < src.modes.size(); i++)
				a.add(new JsonPrimitive(src.modes.get(i).ID));
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
			if(!isValid(o)) return null;
			return new GameModes(o);
		}
		
		private static Gson getGson()
		{
			if(gson == null)
			{
				GsonBuilder gb = new GsonBuilder();
				gb.setPrettyPrinting();
				gb.registerTypeAdapter(GameModes.class, new Serializer());
				gson = gb.create();
			}
			
			return gson;
		}
	}
}