package ftb.lib;

import com.google.gson.*;
import net.minecraft.util.EnumTypeAdapterFactory;
import net.minecraft.util.text.*;

public class JsonHelper
{
	public static Gson chatComponentGson;
	
	public static void init()
	{
		GsonBuilder gb = new GsonBuilder();
		gb.registerTypeHierarchyAdapter(ITextComponent.class, new ITextComponent.Serializer());
		gb.registerTypeHierarchyAdapter(Style.class, new Style.Serializer());
		gb.registerTypeAdapterFactory(new EnumTypeAdapterFactory());
		chatComponentGson = gb.create();
	}
	
	public static JsonElement serializeICC(ITextComponent c)
	{
		if(c == null) return null;
		return chatComponentGson.toJsonTree(c, ITextComponent.class);
	}
	
	public static ITextComponent deserializeICC(JsonElement e)
	{
		if(e == null || e.isJsonNull()) return null;
		return chatComponentGson.fromJson(e, ITextComponent.class);
	}
}