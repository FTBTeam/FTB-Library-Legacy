package com.latmod.lib.io;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LMConnection
{
    public final RequestMethod type;
    public final String url;
    public byte[] data;

    public LMConnection(RequestMethod t, String s)
    {
        type = (t == null) ? RequestMethod.SIMPLE_GET : t;
        url = s;
    }

    public Response connect() throws Exception
    {
        long startTime = System.currentTimeMillis();

        switch(type)
        {
            case SIMPLE_GET:
            {
                URL con = new URL(url);
                InputStream is = con.openStream();
                return new Response(RequestMethod.SIMPLE_GET, System.currentTimeMillis() - startTime, 200, is);
            }
            case FILE:
            {
                FileInputStream is = new FileInputStream(url);
                return new Response(RequestMethod.FILE, System.currentTimeMillis() - startTime, 200, is);
            }
            default:
            {
                HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
                con.setRequestMethod(type.name());
                con.setRequestProperty("User-Agent", "HTTP/1.1");
                con.setDoInput(true);

                if(data != null && data.length > 0)
                {
                    con.setDoOutput(true);
                    OutputStream os = con.getOutputStream();
                    os.write(data, 0, data.length);
                    os.flush();
                    os.close();
                }

                int responseCode = con.getResponseCode();
                return new Response(type, System.currentTimeMillis() - startTime, responseCode, con.getInputStream());
            }
        }
    }
}