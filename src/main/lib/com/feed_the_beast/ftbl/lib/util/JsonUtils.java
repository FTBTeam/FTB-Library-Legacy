package com.feed_the_beast.ftbl.lib.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNullableByDefault;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

/**
 * Type for Lists: new TypeToken<List<E>>() {}.getType()
 */
@ParametersAreNullableByDefault
public class JsonUtils
{
	public static final JsonDeserializationContext DESERIALIZATION_CONTEXT;
	public static final JsonSerializationContext SERIALIZATION_CONTEXT, PRETTY_SERIALIZATION_CONTEXT;
	public static final Gson GSON;
	public static final Gson GSON_PRETTY;
	public static final JsonParser PARSER;

	static
	{
		GsonBuilder gb = new GsonBuilder();
		GSON = gb.create();
		gb.setPrettyPrinting();
		gb.disableHtmlEscaping();
		GSON_PRETTY = gb.create();

		DESERIALIZATION_CONTEXT = new JsonDeserializationContext()
		{
			@Override
			public <T> T deserialize(@Nonnull JsonElement json, @Nonnull Type typeOfT) throws JsonParseException
			{
				return GSON.fromJson(json, typeOfT);
			}
		};

		SERIALIZATION_CONTEXT = new JsonSerializationContext()
		{
			@Override
			public JsonElement serialize(Object src)
			{
				return GSON.toJsonTree(src);
			}

			@Override
			public JsonElement serialize(@Nonnull Object src, @Nonnull Type typeOfSrc)
			{
				return GSON.toJsonTree(src, typeOfSrc);
			}
		};

		PRETTY_SERIALIZATION_CONTEXT = new JsonSerializationContext()
		{
			@Override
			public JsonElement serialize(@Nonnull Object src)
			{
				return GSON_PRETTY.toJsonTree(src);
			}

			@Override
			public JsonElement serialize(@Nonnull Object src, @Nonnull Type typeOfSrc)
			{
				return GSON_PRETTY.toJsonTree(src, typeOfSrc);
			}
		};

		PARSER = new JsonParser();
	}

	public static String toJson(@Nonnull Gson gson, JsonElement e)
	{
		return gson.toJson(e == null ? JsonNull.INSTANCE : e);
	}

	public static boolean toJson(@Nonnull Gson gson, @Nonnull File f, JsonElement o)
	{
		try
		{
			FileUtils.save(f, toJson(gson, o));
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return false;
	}

	public static String toJson(JsonElement o)
	{
		return toJson(GSON, o);
	}

	public static boolean toJson(@Nonnull File f, JsonElement o)
	{
		return toJson(GSON_PRETTY, f, o);
	}

	public static JsonElement fromJson(String json)
	{
		return (json == null || json.isEmpty()) ? JsonNull.INSTANCE : PARSER.parse(json);
	}

	public static JsonElement fromJson(Reader json)
	{
		if (json == null)
		{
			return JsonNull.INSTANCE;
		}

		try
		{
			JsonElement element = PARSER.parse(json);
			json.close();
			return element;
		}
		catch (IOException e)
		{
			return JsonNull.INSTANCE;
		}
	}

	public static JsonElement fromJson(File json)
	{
		try
		{
			if (json == null || !json.exists())
			{
				return JsonNull.INSTANCE;
			}

			FileInputStream fis = new FileInputStream(json);
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis, StringUtils.UTF_8));
			JsonElement e = fromJson(reader);
			reader.close();
			fis.close();
			return e;
		}
		catch (Exception ex)
		{
			return JsonNull.INSTANCE;
		}
	}

	// -- //

	public static JsonElement toIntArray(int... ai)
	{
		if (ai == null)
		{
			return JsonNull.INSTANCE;
		}

		JsonArray a = new JsonArray();
		if (ai.length == 0)
		{
			return a;
		}

		for (int anAi : ai)
		{
			a.add(new JsonPrimitive(anAi));
		}

		return a;
	}

	@Nullable
	public static int[] fromIntArray(JsonElement e)
	{
		if (e == null || e.isJsonNull())
		{
			return null;
		}

		if (e.isJsonArray())
		{
			JsonArray a = e.getAsJsonArray();
			int[] ai = new int[a.size()];
			if (ai.length == 0)
			{
				return ai;
			}
			for (int i = 0; i < ai.length; i++)
			{
				ai[i] = a.get(i).getAsInt();
			}
			return ai;
		}

		return new int[] {e.getAsInt()};
	}

	public static JsonElement serializeTextComponent(@Nullable ITextComponent c)
	{
		if (c instanceof TextComponentString && c.getStyle().isEmpty() && c.getSiblings().isEmpty())
		{
			return new JsonPrimitive(((TextComponentString) c).getText());
		}

		return (c == null) ? JsonNull.INSTANCE : ITextComponent.Serializer.GSON.toJsonTree(c, ITextComponent.class);
	}

	@Nullable
	public static ITextComponent deserializeTextComponent(JsonElement e)
	{
		return (e == null || e.isJsonNull()) ? null : (e.isJsonPrimitive() ? new TextComponentString(e.getAsString()) : ITextComponent.Serializer.GSON.fromJson(e, ITextComponent.class));
	}

	public static JsonElement serializeClickEvent(@Nullable ClickEvent event)
	{
		if (event == null)
		{
			return JsonNull.INSTANCE;
		}

		JsonObject o = new JsonObject();
		o.add("action", new JsonPrimitive(event.getAction().getCanonicalName()));
		o.add("value", new JsonPrimitive(event.getValue()));
		return o;
	}

	@Nullable
	public static ClickEvent deserializeClickEvent(JsonElement e)
	{
		if (e == null || !e.isJsonObject())
		{
			return null;
		}

		JsonObject o = e.getAsJsonObject();

		if (o != null)
		{
			JsonPrimitive a = o.getAsJsonPrimitive("action");
			ClickEvent.Action action = a == null ? null : ClickEvent.Action.getValueByCanonicalName(a.getAsString());
			JsonPrimitive v = o.getAsJsonPrimitive("value");
			String s = v == null ? null : v.getAsString();

			if (action != null && s != null && action.shouldAllowInChat())
			{
				return new ClickEvent(action, s);
			}
		}

		return null;
	}

	public static JsonElement serializeHoverEvent(@Nullable HoverEvent event)
	{
		if (event == null)
		{
			return JsonNull.INSTANCE;
		}

		JsonObject o = new JsonObject();
		o.add("action", new JsonPrimitive(event.getAction().getCanonicalName()));
		o.add("value", serializeTextComponent(event.getValue()));
		return o;
	}

	@Nullable
	public static HoverEvent deserializeHoverEvent(JsonElement e)
	{
		if (e == null || !e.isJsonObject())
		{
			return null;
		}

		JsonObject o = e.getAsJsonObject();

		if (o != null)
		{
			JsonPrimitive a = o.getAsJsonPrimitive("action");
			HoverEvent.Action action = a == null ? null : HoverEvent.Action.getValueByCanonicalName(a.getAsString());
			JsonPrimitive v = o.getAsJsonPrimitive("value");
			ITextComponent t = v == null ? null : deserializeTextComponent(v);

			if (action != null && t != null && action.shouldAllowInChat())
			{
				return new HoverEvent(action, t);
			}
		}

		return null;
	}

	public static JsonObject fromJsonTree(@Nonnull JsonObject o)
	{
		JsonObject map = new JsonObject();
		fromJsonTree0(map, null, o);
		return map;
	}

	private static void fromJsonTree0(@Nonnull JsonObject map, String id0, @Nonnull JsonObject o)
	{
		for (Map.Entry<String, JsonElement> entry : o.entrySet())
		{
			if (entry.getValue() instanceof JsonObject)
			{
				fromJsonTree0(map, (id0 == null) ? entry.getKey() : (id0 + '.' + entry.getKey()), entry.getValue().getAsJsonObject());
			}
			else
			{
				map.add((id0 == null) ? entry.getKey() : (id0 + '.' + entry.getKey()), entry.getValue());
			}
		}
	}

	public static JsonObject toJsonTree(@Nonnull Collection<Map.Entry<String, JsonElement>> tree)
	{
		JsonObject o1 = new JsonObject();
		tree.forEach(entry -> findGroup(o1, entry.getKey()).add(lastKeyPart(entry.getKey()), entry.getValue()));
		return o1;
	}

	private static String lastKeyPart(@Nonnull String s)
	{
		int idx = s.lastIndexOf('.');

		if (idx != -1)
		{
			return s.substring(idx + 1);
		}

		return s;
	}

	private static JsonObject findGroup(@Nonnull JsonObject parent, @Nonnull String s)
	{
		int idx = s.indexOf('.');

		if (idx != -1)
		{
			String s0 = s.substring(0, idx);

			JsonElement o = parent.get(s0);

			if (o == null)
			{
				o = new JsonObject();
				parent.add(s0, o);
			}

			return findGroup(o.getAsJsonObject(), s.substring(idx + 1, s.length() - 1));
		}

		return parent;
	}

	@Nonnull
	public static String fixJsonString(@Nonnull String json)
	{
		if (json.isEmpty())
		{
			return "\"\"";
		}

		if (json.indexOf(' ') != -1 && !((json.startsWith("\"") && json.endsWith("\"")) || (json.startsWith("{") && json.endsWith("}")) || (json.startsWith("[") && json.endsWith("]"))))
		{
			json = "\"" + json + "\"";
		}

		return json;
	}
}