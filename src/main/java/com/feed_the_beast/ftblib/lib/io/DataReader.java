package com.feed_the_beast.ftblib.lib.io;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import net.minecraft.client.resources.IResource;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.Proxy;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class DataReader
{
	public static final String TEXT = "text/plain; charset=utf-8";
	public static final String JSON = "application/json; charset=utf-8";
	public static final String HTML = "text/html; charset=utf-8";
	public static final String XML = "text/xml; charset=utf-8";
	public static final String PNG = "image/png";

	public static DataReader get(URL url, RequestMethod requestMethod, String contentType, @Nullable HttpDataReader.HttpDataOutput data, Proxy proxy)
	{
		return new HttpDataReader(url, requestMethod, contentType, data, proxy);
	}

	public static DataReader get(URL url, String contentType, Proxy proxy)
	{
		return get(url, RequestMethod.GET, contentType, null, proxy);
	}

	public static DataReader get(File file)
	{
		return new FileDataReader(file);
	}

	public static DataReader get(@Nullable String string)
	{
		return new StringDataReader(string);
	}

	@SideOnly(Side.CLIENT)
	public static DataReader get(IResource resource)
	{
		return new ResourceDataReader(resource);
	}

	public static DataReader get(InputStream stream)
	{
		return new StreamDataReader(stream);
	}

	public static DataReader get(URI uri, Proxy proxy)
	{
		switch (uri.getScheme())
		{
			case "http":
			case "https":
				try
				{
					return get(uri.toURL(), "", proxy);
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			case "file":
				return get(new File(uri.getPath()));
			case "string":
				return get(uri.getPath());
		}

		throw new IllegalArgumentException("Unknown URI scheme: " + uri.getScheme() + "!");
	}

	public boolean canRead()
	{
		return true;
	}

	public String string() throws Exception
	{
		return string(64);
	}

	public String safeString()
	{
		if (!canRead())
		{
			return "";
		}

		try
		{
			return string();
		}
		catch (Exception ex)
		{
			return "";
		}
	}

	protected static String readStringFromStream(InputStream stream, int bufferSize) throws Exception
	{
		try (Reader in = new InputStreamReader(stream, StandardCharsets.UTF_8))
		{
			char[] buffer = new char[bufferSize];
			StringBuilder out = new StringBuilder();
			int read;
			do
			{
				read = in.read(buffer, 0, bufferSize);
				if (read > 0)
				{
					out.append(buffer, 0, read);
				}
			}
			while (read >= 0);
			return out.toString();
		}
	}

	public abstract String string(int bufferSize) throws Exception;

	public abstract List<String> stringList() throws Exception;

	public List<String> safeStringList()
	{
		if (!canRead())
		{
			return Collections.emptyList();
		}

		try
		{
			return stringList();
		}
		catch (Exception ex)
		{
			return Collections.emptyList();
		}
	}

	protected static List<String> readStringListFromStream(InputStream stream) throws Exception
	{
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)))
		{
			List<String> list = new ArrayList<>();
			String line;
			while ((line = reader.readLine()) != null)
			{
				list.add(line);
			}
			return list;
		}
	}

	public abstract JsonElement json() throws Exception;

	public JsonElement safeJson()
	{
		if (!canRead())
		{
			return JsonNull.INSTANCE;
		}

		try
		{
			return json();
		}
		catch (Exception ex)
		{
			return JsonNull.INSTANCE;
		}
	}

	public abstract BufferedImage image() throws Exception;
}