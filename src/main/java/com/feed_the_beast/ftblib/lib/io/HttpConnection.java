package com.feed_the_beast.ftblib.lib.io;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import org.apache.http.entity.ContentType;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;

public class HttpConnection
{
	public static final String TEXT = ContentType.TEXT_PLAIN.getMimeType();
	public static final String JSON = ContentType.APPLICATION_JSON.getMimeType();
	public static final String HTML = ContentType.TEXT_HTML.getMimeType();
	public static final String XML = ContentType.TEXT_XML.getMimeType();

	public final String url;
	public final String contentType;
	public final RequestMethod requestMethod;
	public byte[] data;

	public static HttpConnection connection(String url, RequestMethod requestMethod, String contentType)
	{
		return new HttpConnection(url, requestMethod, contentType);
	}

	public static JsonElement getJson(String url, Proxy proxy)
	{
		try
		{
			return connection(url, RequestMethod.GET, JSON).connect(proxy).asJson();
		}
		catch (Exception ex)
		{
			return JsonNull.INSTANCE;
		}
	}

	private HttpConnection(String s, RequestMethod t, String c)
	{
		url = s;
		contentType = c;
		requestMethod = t;
		data = null;
	}

	public Response connect(Proxy proxy) throws Exception
	{
		long startTime = System.currentTimeMillis();
		HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection(proxy);
		con.setRequestMethod(requestMethod.name());
		con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-GB; rv:1.9.0.3) Gecko/2008092417 Firefox/3.0.3");

		if (!contentType.isEmpty())
		{
			con.setRequestProperty("Content-Type", contentType);
		}

		con.setDoInput(true);

		if (data != null && data.length > 0)
		{
			con.setDoOutput(true);
			OutputStream os = con.getOutputStream();
			os.write(data, 0, data.length);
			os.flush();
			os.close();
		}

		int responseCode = con.getResponseCode();
		InputStream stream = con.getInputStream();
		return new Response(con, requestMethod, System.currentTimeMillis() - startTime, responseCode, stream);
	}
}