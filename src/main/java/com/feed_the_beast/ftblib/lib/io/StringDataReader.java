package com.feed_the_beast.ftblib.lib.io;

import com.feed_the_beast.ftblib.lib.util.JsonUtils;
import com.feed_the_beast.ftblib.lib.util.StringUtils;
import com.google.gson.JsonElement;

import javax.annotation.Nullable;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class StringDataReader extends DataReader
{
	public final String string;

	StringDataReader(@Nullable String s)
	{
		string = StringUtils.emptyIfNull(s);
	}

	public String toString()
	{
		return string;
	}

	@Override
	public boolean canRead()
	{
		return !string.isEmpty();
	}

	@Override
	public String string()
	{
		return string;
	}

	@Override
	public String string(int bufferSize)
	{
		return string;
	}

	@Override
	public List<String> stringList() throws Exception
	{
		BufferedReader reader = new BufferedReader(new StringReader(string));
		List<String> l = new ArrayList<>();
		String s;
		while ((s = reader.readLine()) != null)
		{
			l.add(s);
		}
		return l;
	}

	@Override
	public JsonElement json() throws Exception
	{
		try (Reader reader = new StringReader(string))
		{
			return JsonUtils.parse(reader);
		}
	}

	@Override
	public BufferedImage image()
	{
		throw new IllegalStateException("Can't parse string as image!");
	}
}