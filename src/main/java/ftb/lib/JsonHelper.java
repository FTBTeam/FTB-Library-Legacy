package ftb.lib;

import com.google.gson.*;
import net.minecraft.util.*;

public class JsonHelper
{
	public static Gson chatComponentGson;
	
	public static void init()
	{
		GsonBuilder gb = new GsonBuilder();
		gb.registerTypeHierarchyAdapter(IChatComponent.class, new IChatComponent.Serializer());
		gb.registerTypeHierarchyAdapter(ChatStyle.class, new ChatStyle.Serializer());
		gb.registerTypeAdapterFactory(new EnumTypeAdapterFactory());
		chatComponentGson = gb.create();
	}
	
	public static JsonElement serializeICC(IChatComponent c)
	{
		if(c == null) return JsonNull.INSTANCE;
		return chatComponentGson.toJsonTree(c, IChatComponent.class);
	}
	
	public static IChatComponent deserializeICC(JsonElement e)
	{
		if(e == null || e.isJsonNull()) return null;
		return chatComponentGson.fromJson(e, IChatComponent.class);
	}
}