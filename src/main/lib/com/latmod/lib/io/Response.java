package com.latmod.lib.io;

import com.google.gson.JsonElement;
import com.latmod.lib.util.LMJsonUtils;
import com.latmod.lib.util.LMStringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public final class Response
{
    private RequestMethod method;
    private long millis;
    private int code;
    private InputStream stream;

    public Response(RequestMethod m, long ms, int c, InputStream is)
    {
        method = m;
        millis = ms;
        code = c;
        stream = is;
    }

    public Response(InputStream is)
    {
        this(RequestMethod.GET, 0L, 200, is);
    }

    public RequestMethod getMethod()
    {
        return method;
    }

    public long getMillis()
    {
        return millis;
    }

    public int getResponseCode()
    {
        return code;
    }

    public InputStream getStream()
    {
        return stream;
    }

    public String asString() throws Exception
    {
        return LMStringUtils.readString(getStream());
    }

    public List<String> asStringList() throws Exception
    {
        return LMStringUtils.readStringList(getStream());
    }

    public JsonElement asJson() throws Exception
    {
        return LMJsonUtils.fromJson(new BufferedReader(new InputStreamReader(getStream())));
    }

    public BufferedImage asImage() throws Exception
    {
        return ImageIO.read(getStream());
    }

    public String toString()
    {
        return getMethod() + "-" + Integer.toString(getResponseCode());
    }
}