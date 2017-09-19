package com.feed_the_beast.ftbl.lib;

import com.feed_the_beast.ftbl.lib.util.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.IntSupplier;

/**
 * @author LatvianModder
 */
public class ChatHistory implements Iterable<ChatHistory.Message>, IJsonSerializable
{
	public static class Message implements Comparable<Message>
	{
		public final ITextComponent message;
		public final ITextComponent from;
		public final long ticks;

		public Message(ITextComponent msg, ITextComponent p, long t)
		{
			message = msg;
			from = p;
			ticks = t;
		}

		public Message(JsonObject json)
		{
			message = JsonUtils.deserializeTextComponent(json.get("message"));
			from = JsonUtils.deserializeTextComponent(json.get("from"));
			ticks = json.has("time") ? json.get("time").getAsLong() : 0L;
		}

		public boolean isValid()
		{
			return message != null && from != null && ticks > 0;
		}

		public JsonObject toJson()
		{
			JsonObject json = new JsonObject();
			json.add("message", JsonUtils.serializeTextComponent(message));
			json.add("from", JsonUtils.serializeTextComponent(from));
			json.addProperty("time", ticks);
			return json;
		}

		@Override
		public int compareTo(Message o)
		{
			return Long.compare(o.ticks, ticks);
		}
	}

	public final List<Message> list;
	public final IntSupplier limit;

	public ChatHistory(IntSupplier l)
	{
		list = new ArrayList<>(); //TODO: Use LinkedList?
		limit = l;
	}

	public void add(Message message)
	{
		int limitInt = limit.getAsInt();
		while (list.size() >= limitInt)
		{
			list.remove(0);
		}

		list.add(message);
	}

	@Override
	public Iterator<Message> iterator()
	{
		return list.iterator();
	}

	@Override
	public void fromJson(JsonElement json)
	{
		list.clear();

		if (json.isJsonArray())
		{
			for (JsonElement element : json.getAsJsonArray())
			{
				Message message = new Message(element.getAsJsonObject());

				if (message.isValid())
				{
					list.add(message);
				}
			}
		}
	}

	@Override
	public JsonElement getSerializableElement()
	{
		JsonArray json = new JsonArray();

		for (Message message : list)
		{
			if (message.isValid())
			{
				json.add(message.toJson());
			}
		}

		return json;
	}
}