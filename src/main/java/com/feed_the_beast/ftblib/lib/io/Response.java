package com.feed_the_beast.ftblib.lib.io;

import com.feed_the_beast.ftblib.lib.util.JsonUtils;
import com.feed_the_beast.ftblib.lib.util.StringUtils;
import com.google.gson.JsonElement;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;

public final class Response
{
	public final HttpURLConnection connection;
	public final RequestMethod method;
	public final long millis;
	public final int responseCode;
	public final InputStream stream;

	public Response(HttpURLConnection c, RequestMethod m, long ms, int r, InputStream is)
	{
		connection = c;
		method = m;
		millis = ms;
		responseCode = r;
		stream = is;
	}

	public String asString() throws Exception
	{
		return StringUtils.readString(stream);
	}

	public String asString(int bufferSize) throws Exception
	{
		return StringUtils.readString(stream, bufferSize);
	}

	public List<String> asStringList() throws Exception
	{
		return StringUtils.readStringList(stream);
	}

	public JsonElement asJson()
	{
		return JsonUtils.fromJson(stream);
	}

	public BufferedImage asImage() throws Exception
	{
		return ImageIO.read(stream);
	}

	public String toString()
	{
		return method + "-" + responseCode;
	}
}