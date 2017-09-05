package com.feed_the_beast.ftbl.lib.util;

import com.feed_the_beast.ftbl.api.INotification;
import com.feed_the_beast.ftbl.lib.CustomStyle;
import com.feed_the_beast.ftbl.lib.Notification;
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
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentKeybind;
import net.minecraft.util.text.TextComponentScore;
import net.minecraft.util.text.TextComponentSelector;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
		gb.setLenient();
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
		if (json == null || json.isEmpty())
		{
			return JsonNull.INSTANCE;
		}

		try
		{
			return PARSER.parse(json);
		}
		catch (Exception e)
		{
			return JsonNull.INSTANCE;
		}
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

	@SideOnly(Side.CLIENT)
	public static JsonElement fromJson(IResource resource)
	{
		try
		{
			return fromJson(new InputStreamReader(resource.getInputStream()));
		}
		catch (Exception ex)
		{
			return JsonNull.INSTANCE;
		}
	}

	@Nonnull
	public static JsonArray toArray(@Nonnull JsonElement element)
	{
		if (element.isJsonArray())
		{
			return element.getAsJsonArray();
		}

		JsonArray a = new JsonArray();
		a.add(element);
		return a;
	}

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
			a.add(anAi);
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
		if (c == null)
		{
			return JsonNull.INSTANCE;
		}

		if (c instanceof TextComponentString && c.getStyle().isEmpty() && c.getSiblings().isEmpty())
		{
			return new JsonPrimitive(((TextComponentString) c).getText());
		}

		JsonObject o = new JsonObject();
		Style s = c.getStyle();

		if (!s.isEmpty())
		{
			if (s.bold != null)
			{
				o.addProperty("bold", s.bold);
			}

			if (s.italic != null)
			{
				o.addProperty("italic", s.italic);
			}

			if (s.underlined != null)
			{
				o.addProperty("underlined", s.underlined);
			}

			if (s.strikethrough != null)
			{
				o.addProperty("strikethrough", s.strikethrough);
			}

			if (s.obfuscated != null)
			{
				o.addProperty("obfuscated", s.obfuscated);
			}

			if (s instanceof CustomStyle)
			{
				CustomStyle cs = (CustomStyle) s;

				if (cs.monospaced != null)
				{
					o.addProperty("monospaced", cs.monospaced);
				}
			}

			if (s.color != null)
			{
				o.addProperty("color", s.color.getFriendlyName());
			}

			if (s.insertion != null)
			{
				o.addProperty("insertion", s.insertion);
			}

			if (s.clickEvent != null)
			{
				o.add("clickEvent", serializeClickEvent(s.clickEvent));
			}

			if (s.hoverEvent != null)
			{
				o.add("hoverEvent", serializeHoverEvent(s.hoverEvent));
			}
		}

		if (!c.getSiblings().isEmpty())
		{
			JsonArray a = new JsonArray();

			for (ITextComponent itextcomponent : c.getSiblings())
			{
				a.add(serializeTextComponent(itextcomponent));
			}

			o.add("extra", a);
		}

		if (c instanceof TextComponentString)
		{
			o.addProperty("text", ((TextComponentString) c).getText());

			if (c instanceof INotification)
			{
				INotification n = (INotification) c;

				if (!n.getId().equals(INotification.VANILLA_STATUS))
				{
					o.addProperty("nid", n.getId().toString());
				}

				if (n.getTimer() != 60)
				{
					o.addProperty("timer", n.getTimer());
				}
			}
		}
		else if (c instanceof TextComponentTranslation)
		{
			TextComponentTranslation t = (TextComponentTranslation) c;
			o.addProperty("translate", t.getKey());

			if (t.getFormatArgs().length > 0)
			{
				JsonArray a = new JsonArray();

				for (Object object : t.getFormatArgs())
				{
					if (object instanceof ITextComponent)
					{
						a.add(serializeTextComponent((ITextComponent) object));
					}
					else
					{
						a.add(String.valueOf(object));
					}
				}

				o.add("with", a);
			}
		}
		else if (c instanceof TextComponentScore)
		{
			TextComponentScore t = (TextComponentScore) c;
			JsonObject o1 = new JsonObject();
			o1.addProperty("name", t.getName());
			o1.addProperty("objective", t.getObjective());
			o1.addProperty("value", t.getUnformattedComponentText());
			o.add("score", o1);
		}
		else if (c instanceof TextComponentSelector)
		{
			o.addProperty("selector", ((TextComponentSelector) c).getSelector());
		}
		else
		{
			if (!(c instanceof TextComponentKeybind))
			{
				throw new IllegalArgumentException("Don't know how to serialize " + c + " as a Component");
			}

			o.addProperty("keybind", ((TextComponentKeybind) c).getKeybind());
		}

		return o;
	}

	@Nullable
	public static ITextComponent deserializeTextComponent(JsonElement e)
	{
		if (e == null || e.isJsonNull())
		{
			return null;
		}
		else if (e.isJsonPrimitive())
		{
			return new TextComponentString(e.getAsString());
		}
		else if (!e.isJsonObject())
		{
			if (!e.isJsonArray())
			{
				throw new JsonParseException("Don't know how to turn " + e + " into a Component");
			}

			ITextComponent t = null;

			for (JsonElement jsonelement : e.getAsJsonArray())
			{
				ITextComponent t2 = deserializeTextComponent(jsonelement);

				if (t2 == null)
				{
					t2 = new TextComponentString("");
				}

				if (t == null)
				{
					t = t2;
				}
				else
				{
					t.appendSibling(t2);
				}
			}

			return t;
		}
		else
		{
			JsonObject o = e.getAsJsonObject();
			ITextComponent t;

			if (o.has("text"))
			{
				String s = o.get("text").getAsString();

				if (o.has("nid") || o.has("timer"))
				{
					t = Notification.of(new ResourceLocation(o.has("nid") ? o.get("nid").getAsString() : ""), s);

					if (o.has("timer"))
					{
						((Notification) t).setTimer(net.minecraft.util.JsonUtils.getInt(o, "timer"));
					}
				}
				else
				{
					t = new TextComponentString(s);
				}
			}
			else if (o.has("translate"))
			{
				String s = o.get("translate").getAsString();

				if (o.has("with"))
				{
					JsonArray a = o.getAsJsonArray("with");
					Object[] o1 = new Object[a.size()];

					for (int i = 0; i < o1.length; ++i)
					{
						o1[i] = deserializeTextComponent(a.get(i));

						if (o1[i] instanceof TextComponentString)
						{
							TextComponentString t2 = (TextComponentString) o1[i];

							if (t2.getStyle().isEmpty() && t2.getSiblings().isEmpty())
							{
								o1[i] = t2.getText();
							}
						}
					}

					t = new TextComponentTranslation(s, o1);
				}
				else
				{
					t = new TextComponentTranslation(s, CommonUtils.NO_OBJECTS);
				}
			}
			else if (o.has("score"))
			{
				JsonObject o1 = o.getAsJsonObject("score");

				if (!o1.has("name") || !o1.has("objective"))
				{
					throw new JsonParseException("A score component needs a least a name and an objective");
				}

				t = new TextComponentScore(net.minecraft.util.JsonUtils.getString(o1, "name"), net.minecraft.util.JsonUtils.getString(o1, "objective"));

				if (o1.has("value"))
				{
					((TextComponentScore) t).setValue(net.minecraft.util.JsonUtils.getString(o1, "value"));
				}
			}
			else if (o.has("selector"))
			{
				t = new TextComponentSelector(net.minecraft.util.JsonUtils.getString(o, "selector"));
			}
			else
			{
				if (!o.has("keybind"))
				{
					throw new JsonParseException("Don't know how to turn " + e + " into a Component");
				}

				t = new TextComponentKeybind(net.minecraft.util.JsonUtils.getString(o, "keybind"));
			}

			if (o.has("extra"))
			{
				JsonArray a = o.getAsJsonArray("extra");

				if (a.size() <= 0)
				{
					throw new JsonParseException("Unexpected empty array of components");
				}

				for (int j = 0; j < a.size(); ++j)
				{
					t.appendSibling(deserializeTextComponent(a.get(j)));
				}
			}

			CustomStyle style = new CustomStyle();

			if (o.has("bold"))
			{
				style.bold = o.get("bold").getAsBoolean();
			}

			if (o.has("italic"))
			{
				style.italic = o.get("italic").getAsBoolean();
			}

			if (o.has("underlined"))
			{
				style.underlined = o.get("underlined").getAsBoolean();
			}

			if (o.has("strikethrough"))
			{
				style.strikethrough = o.get("strikethrough").getAsBoolean();
			}

			if (o.has("obfuscated"))
			{
				style.obfuscated = o.get("obfuscated").getAsBoolean();
			}

			if (o.has("monospaced"))
			{
				style.monospaced = o.get("monospaced").getAsBoolean();
			}

			if (o.has("color"))
			{
				style.color = TextFormatting.getValueByName(o.get("color").getAsString());
			}

			if (o.has("background"))
			{
				style.background = TextFormatting.getValueByName(o.get("background").getAsString());
			}

			if (o.has("insertion"))
			{
				style.insertion = o.get("insertion").getAsString();
			}

			if (o.has("clickEvent"))
			{
				style.clickEvent = deserializeClickEvent(o.get("clickEvent"));
			}

			if (o.has("hoverEvent"))
			{
				style.hoverEvent = deserializeHoverEvent(o.get("hoverEvent"));
			}

			t.setStyle(style);
			return t;
		}
	}

	public static JsonElement serializeClickEvent(@Nullable ClickEvent event)
	{
		if (event == null)
		{
			return JsonNull.INSTANCE;
		}

		JsonObject o = new JsonObject();
		o.addProperty("action", event.getAction().getCanonicalName());
		o.addProperty("value", event.getValue());
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
		o.addProperty("action", event.getAction().getCanonicalName());
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

	private static void fromJsonTree0(@Nonnull JsonObject map, @Nullable String id0, @Nonnull JsonObject o)
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