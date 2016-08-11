package com.latmod.lib.io;

import com.google.gson.JsonElement;
import com.latmod.lib.json.LMJsonUtils;
import com.latmod.lib.util.LMStringUtils;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public final class Response
{
    public final RequestMethod method;
    public final long millis;
    public final int code;
    public final InputStream stream;

    public Response(@Nonnull RequestMethod m, long ms, int c, @Nonnull InputStream is)
    {
        method = m;
        millis = ms;
        code = c;
        stream = is;
    }

    public Response(@Nonnull InputStream is)
    {
        this(RequestMethod.SIMPLE_GET, 0L, 200, is);
    }

    @Override
    public String toString()
    {
        return method + "-" + Integer.toString(code);
    }

    @Nonnull
    public String asString() throws Exception
    {
        return LMStringUtils.readString(stream);
    }

    @Nonnull
    public List<String> asStringList() throws Exception
    {
        return LMStringUtils.readStringList(stream);
    }

    @Nonnull
    public JsonElement asJson() throws Exception
    {
        return LMJsonUtils.fromJson(new BufferedReader(new InputStreamReader(stream)));
    }

    @Nonnull
    public BufferedImage asImage() throws Exception
    {
        return ImageIO.read(stream);
    }
}