package com.feed_the_beast.ftbl.lib.util;

import com.feed_the_beast.ftbl.api.INotification;
import com.feed_the_beast.ftbl.lib.util.text_components.CustomStyle;
import com.feed_the_beast.ftbl.lib.util.text_components.Notification;
import com.feed_the_beast.ftbl.lib.util.text_components.TextComponentCountdown;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import net.minecraft.client.resources.IResource;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
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
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class JsonUtils
{
	public static final JsonParser PARSER = new JsonParser();

	public static boolean isNull(@Nullable JsonElement element)
	{
		return element == null || element == JsonNull.INSTANCE || element.isJsonNull();
	}

	public static void toJson(@Nullable JsonElement element, Writer writer, boolean prettyPrinting)
	{
		if (isNull(element))
		{
			try
			{
				writer.write("null");
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}

			return;
		}

		JsonWriter jsonWriter = new JsonWriter(writer);
		jsonWriter.setLenient(true);
		jsonWriter.setHtmlSafe(false);
		jsonWriter.setSerializeNulls(true);

		if (prettyPrinting)
		{
			jsonWriter.setIndent("\t");
		}

		try
		{
			Streams.write(element, jsonWriter);
		}
		catch (Exception ex)
		{
			throw new JsonIOException(ex);
		}
	}

	public static String toJson(@Nullable JsonElement element, boolean prettyPrinting)
	{
		StringWriter writer = new StringWriter();
		toJson(element, writer, prettyPrinting);
		return writer.toString();
	}

	public static void toJson(@Nullable JsonElement element, File file, boolean prettyPrinting)
	{
		try (OutputStreamWriter output = new OutputStreamWriter(new FileOutputStream(FileUtils.newFile(file)), StandardCharsets.UTF_8);
			 BufferedWriter writer = new BufferedWriter(output))
		{
			toJson(element, writer, prettyPrinting);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public static String toJson(@Nullable JsonElement element)
	{
		return toJson(element, false);
	}

	public static void toJson(@Nullable JsonElement element, File file)
	{
		toJson(element, file, true);
	}

	public static JsonElement fromJson(@Nullable String json)
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

	public static JsonElement fromJson(@Nullable Reader reader)
	{
		if (reader == null)
		{
			return JsonNull.INSTANCE;
		}

		try
		{
			JsonElement element = PARSER.parse(reader);
			reader.close();
			return element;
		}
		catch (IOException e)
		{
			return JsonNull.INSTANCE;
		}
	}

	public static JsonElement fromJson(File file)
	{
		try
		{
			if (!file.exists())
			{
				return JsonNull.INSTANCE;
			}

			FileInputStream fis = new FileInputStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8));
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


	public static JsonArray toArray(JsonElement element)
	{
		if (element.isJsonArray())
		{
			return element.getAsJsonArray();
		}

		JsonArray array = new JsonArray();

		if (!element.isJsonNull())
		{
			array.add(element);
		}

		return array;
	}

	public static JsonElement serializeTextComponent(@Nullable ITextComponent component)
	{
		if (component == null)
		{
			return JsonNull.INSTANCE;
		}
		else if (component.getClass() == TextComponentString.class && component.getSiblings().isEmpty() && component.getStyle().isEmpty())
		{
			return new JsonPrimitive(((TextComponentString) component).getText());
		}

		JsonObject json = new JsonObject();
		Style style = component.getStyle();

		if (!style.isEmpty())
		{
			if (style.bold != null)
			{
				json.addProperty("bold", style.bold);
			}

			if (style.italic != null)
			{
				json.addProperty("italic", style.italic);
			}

			if (style.underlined != null)
			{
				json.addProperty("underlined", style.underlined);
			}

			if (style.strikethrough != null)
			{
				json.addProperty("strikethrough", style.strikethrough);
			}

			if (style.obfuscated != null)
			{
				json.addProperty("obfuscated", style.obfuscated);
			}

			if (style instanceof CustomStyle)
			{
				CustomStyle customStyle = (CustomStyle) style;

				if (customStyle.monospaced != null)
				{
					json.addProperty("monospaced", customStyle.monospaced);
				}

				if (customStyle.background != null)
				{
					json.addProperty("background", customStyle.background.getFriendlyName());
				}
			}

			if (style.color != null)
			{
				json.addProperty("color", style.color.getFriendlyName());
			}

			if (style.insertion != null)
			{
				json.addProperty("insertion", style.insertion);
			}

			if (style.clickEvent != null)
			{
				json.add("clickEvent", serializeClickEvent(style.clickEvent));
			}

			if (style.hoverEvent != null)
			{
				json.add("hoverEvent", serializeHoverEvent(style.hoverEvent));
			}
		}

		if (!component.getSiblings().isEmpty())
		{
			JsonArray array = new JsonArray();

			for (ITextComponent itextcomponent : component.getSiblings())
			{
				array.add(serializeTextComponent(itextcomponent));
			}

			json.add("extra", array);
		}

		if (component instanceof TextComponentString)
		{
			json.addProperty("text", ((TextComponentString) component).getText());

			if (component instanceof TextComponentCountdown)
			{
				json.addProperty("countdown", ((TextComponentCountdown) component).countdown);
			}
			else if (component instanceof INotification)
			{
				INotification n = (INotification) component;

				if (!n.getId().equals(INotification.VANILLA_STATUS))
				{
					json.addProperty("notification", n.getId().toString());
				}

				if (n.getTimer() != 60)
				{
					json.addProperty("timer", n.getTimer());
				}

				if (n.isImportant())
				{
					json.addProperty("important", true);
				}
			}
		}
		else if (component instanceof TextComponentTranslation)
		{
			TextComponentTranslation translation = (TextComponentTranslation) component;
			json.addProperty("translate", translation.getKey());

			if (translation.getFormatArgs().length > 0)
			{
				JsonArray array = new JsonArray();

				for (Object object : translation.getFormatArgs())
				{
					if (object instanceof ITextComponent)
					{
						array.add(serializeTextComponent((ITextComponent) object));
					}
					else
					{
						array.add(String.valueOf(object));
					}
				}

				json.add("with", array);
			}
		}
		else if (component instanceof TextComponentScore)
		{
			TextComponentScore score = (TextComponentScore) component;
			JsonObject json1 = new JsonObject();
			json1.addProperty("name", score.getName());
			json1.addProperty("objective", score.getObjective());
			json1.addProperty("value", score.getUnformattedComponentText());
			json.add("score", json1);
		}
		else if (component instanceof TextComponentSelector)
		{
			json.addProperty("selector", ((TextComponentSelector) component).getSelector());
		}
		else if (component instanceof TextComponentKeybind)
		{
			json.addProperty("keybind", ((TextComponentKeybind) component).getKeybind());
		}

		if (json.entrySet().isEmpty())
		{
			throw new IllegalArgumentException("Don't know how to serialize " + component + " as a Component");
		}

		return json;
	}

	@Nullable
	public static ITextComponent deserializeTextComponent(@Nullable JsonElement element)
	{
		if (isNull(element))
		{
			return null;
		}
		else if (element.isJsonPrimitive())
		{
			return new TextComponentString(StringUtils.fixTabs(element.getAsString(), 2));
		}
		else if (!element.isJsonObject())
		{
			if (!element.isJsonArray())
			{
				throw new JsonParseException("Don't know how to turn " + element + " into a Component");
			}

			ITextComponent component = null;

			for (JsonElement element1 : element.getAsJsonArray())
			{
				ITextComponent component1 = deserializeTextComponent(element1);

				if (component1 == null)
				{
					component1 = new TextComponentString("");
				}

				if (component == null)
				{
					component = component1;
				}
				else
				{
					component.appendSibling(component1);
				}
			}

			return component;
		}
		else
		{
			JsonObject json = element.getAsJsonObject();
			ITextComponent component;

			if (json.has("text") || json.has("notification"))
			{
				String s = json.has("text") ? StringUtils.fixTabs(json.get("text").getAsString(), 2) : "";

				if (json.has("notification") || json.has("timer") || json.has("important"))
				{
					Notification n = Notification.of(json.has("notification") ? new ResourceLocation(json.get("notification").getAsString()) : INotification.VANILLA_STATUS, s);
					component = n;

					if (json.has("timer"))
					{
						n.setTimer(net.minecraft.util.JsonUtils.getInt(json, "timer"));
					}

					if (json.has("important"))
					{
						n.setImportant(net.minecraft.util.JsonUtils.getBoolean(json, "important"));
					}
				}
				else if (json.has("countdown"))
				{
					component = new TextComponentCountdown(s, json.get("countdown").getAsLong());
				}
				else
				{
					component = new TextComponentString(s);
				}
			}
			else if (json.has("translate"))
			{
				String s = json.get("translate").getAsString();

				if (json.has("with"))
				{
					JsonArray a = json.getAsJsonArray("with");
					Object[] o1 = new Object[a.size()];

					for (int i = 0; i < o1.length; ++i)
					{
						o1[i] = deserializeTextComponent(a.get(i));

						if (o1[i] != null && o1[i].getClass() == TextComponentString.class)
						{
							TextComponentString t2 = (TextComponentString) o1[i];

							if (t2.getStyle().isEmpty() && t2.getSiblings().isEmpty())
							{
								o1[i] = t2.getText();
							}
						}
					}

					component = new TextComponentTranslation(s, o1);
				}
				else
				{
					component = new TextComponentTranslation(s, CommonUtils.NO_OBJECTS);
				}
			}
			else if (json.has("score"))
			{
				JsonObject o1 = json.getAsJsonObject("score");

				if (!o1.has("name") || !o1.has("objective"))
				{
					throw new JsonParseException("A score component needs a least a name and an objective");
				}

				component = new TextComponentScore(net.minecraft.util.JsonUtils.getString(o1, "name"), net.minecraft.util.JsonUtils.getString(o1, "objective"));

				if (o1.has("value"))
				{
					((TextComponentScore) component).setValue(net.minecraft.util.JsonUtils.getString(o1, "value"));
				}
			}
			else if (json.has("selector"))
			{
				component = new TextComponentSelector(net.minecraft.util.JsonUtils.getString(json, "selector"));
			}
			else if (json.has("keybind"))
			{
				component = new TextComponentKeybind(net.minecraft.util.JsonUtils.getString(json, "keybind"));
			}
			else
			{
				return null;
			}

			if (json.has("extra"))
			{
				JsonArray a = json.getAsJsonArray("extra");

				if (a.size() <= 0)
				{
					throw new JsonParseException("Unexpected empty array of components");
				}

				for (JsonElement element1 : a)
				{
					ITextComponent component1 = deserializeTextComponent(element1);
					component.appendSibling(component1 == null ? new TextComponentString("") : component1);
				}
			}

			Style style = (json.has("monospaced") || json.has("background")) ? new CustomStyle() : new Style();

			if (json.has("bold"))
			{
				style.bold = json.get("bold").getAsBoolean();
			}

			if (json.has("italic"))
			{
				style.italic = json.get("italic").getAsBoolean();
			}

			if (json.has("underlined"))
			{
				style.underlined = json.get("underlined").getAsBoolean();
			}

			if (json.has("strikethrough"))
			{
				style.strikethrough = json.get("strikethrough").getAsBoolean();
			}

			if (json.has("obfuscated"))
			{
				style.obfuscated = json.get("obfuscated").getAsBoolean();
			}

			if (json.has("color"))
			{
				style.color = TextFormatting.getValueByName(json.get("color").getAsString());
			}

			if (style instanceof CustomStyle)
			{
				CustomStyle cs = (CustomStyle) style;

				if (json.has("monospaced"))
				{
					cs.monospaced = json.get("monospaced").getAsBoolean();
				}

				if (json.has("background"))
				{
					cs.background = TextFormatting.getValueByName(json.get("background").getAsString());
				}
			}

			if (json.has("insertion"))
			{
				style.insertion = json.get("insertion").getAsString();
			}

			if (json.has("clickEvent"))
			{
				style.clickEvent = deserializeClickEvent(json.get("clickEvent"));
			}

			if (json.has("hoverEvent"))
			{
				style.hoverEvent = deserializeHoverEvent(json.get("hoverEvent"));
			}

			component.setStyle(style);
			return component;
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
	public static ClickEvent deserializeClickEvent(JsonElement element)
	{
		if (isNull(element))
		{
			return null;
		}

		JsonObject o = element.getAsJsonObject();

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
	public static HoverEvent deserializeHoverEvent(@Nullable JsonElement element)
	{
		if (isNull(element))
		{
			return null;
		}

		JsonObject o = element.getAsJsonObject();

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

	public static JsonObject fromJsonTree(JsonObject o)
	{
		JsonObject map = new JsonObject();
		fromJsonTree0(map, null, o);
		return map;
	}

	private static void fromJsonTree0(JsonObject map, @Nullable String id0, JsonObject o)
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

	public static JsonObject toJsonTree(Collection<Map.Entry<String, JsonElement>> tree)
	{
		JsonObject o1 = new JsonObject();

		for (Map.Entry<String, JsonElement> entry : tree)
		{
			findGroup(o1, entry.getKey()).add(lastKeyPart(entry.getKey()), entry.getValue());
		}

		return o1;
	}

	private static String lastKeyPart(String s)
	{
		int idx = s.lastIndexOf('.');

		if (idx != -1)
		{
			return s.substring(idx + 1);
		}

		return s;
	}

	private static JsonObject findGroup(JsonObject parent, String s)
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


	public static String fixJsonString(String json)
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


	public static JsonElement toJson(@Nullable NBTBase nbt)
	{
		if (nbt == null)
		{
			return JsonNull.INSTANCE;
		}

		switch (nbt.getId())
		{
			case Constants.NBT.TAG_COMPOUND:
			{
				JsonObject json = new JsonObject();

				if (!nbt.hasNoTags())
				{
					NBTTagCompound tagCompound = (NBTTagCompound) nbt;
					for (String s : tagCompound.getKeySet())
					{
						json.add(s, toJson(tagCompound.getTag(s)));
					}
				}

				return json;
			}
			case Constants.NBT.TAG_LIST:
			{
				JsonArray json = new JsonArray();

				if (!nbt.hasNoTags())
				{
					NBTTagList list = (NBTTagList) nbt;
					for (int i = 0; i < list.tagCount(); i++)
					{
						json.add(toJson(list.get(i)));
					}
				}

				return json;
			}
			case Constants.NBT.TAG_STRING:
				return new JsonPrimitive(((NBTTagString) nbt).getString());
			case Constants.NBT.TAG_BYTE:
				return new JsonPrimitive(((NBTPrimitive) nbt).getByte());
			case Constants.NBT.TAG_SHORT:
				return new JsonPrimitive(((NBTPrimitive) nbt).getShort());
			case Constants.NBT.TAG_INT:
				return new JsonPrimitive(((NBTPrimitive) nbt).getInt());
			case Constants.NBT.TAG_LONG:
				return new JsonPrimitive(((NBTPrimitive) nbt).getLong());
			case Constants.NBT.TAG_FLOAT:
				return new JsonPrimitive(((NBTPrimitive) nbt).getFloat());
			case Constants.NBT.TAG_DOUBLE:
				return new JsonPrimitive(((NBTPrimitive) nbt).getDouble());
			case Constants.NBT.TAG_BYTE_ARRAY:
			{
				JsonArray json = new JsonArray();

				if (!nbt.hasNoTags())
				{
					for (byte v : ((NBTTagByteArray) nbt).getByteArray())
					{
						json.add(v);
					}
				}

				return json;
			}
			case Constants.NBT.TAG_INT_ARRAY:
			{
				JsonArray json = new JsonArray();

				if (!nbt.hasNoTags())
				{
					for (int v : ((NBTTagIntArray) nbt).getIntArray())
					{
						json.add(v);
					}
				}

				return json;
			}
			default:
				return JsonNull.INSTANCE;
		}
	}

	@Nullable
	public static NBTBase toNBT(@Nullable JsonElement element)
	{
		if (isNull(element))
		{
			return null;
		}

		try
		{
			return JsonToNBT.getTagFromJson(toJson(element));
		}
		catch (Exception ex)
		{
			return null;
		}
	}
}