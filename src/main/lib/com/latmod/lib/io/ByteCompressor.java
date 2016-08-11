package com.latmod.lib.io;

import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ByteCompressor
{
    @Nonnull
    public static byte[] compress(@Nonnull byte[] data, int off, int len) throws Exception
    {
        Deflater d = new Deflater();
        d.setInput(data, off, len);
        ByteArrayOutputStream os = new ByteArrayOutputStream(len);
        d.finish();

        byte[] buffer = new byte[1024];
        while(!d.finished())
        {
            int count = d.deflate(buffer);
            os.write(buffer, 0, count);
        }

        os.close();
        byte[] output = os.toByteArray();
        d.end();
        return output;
    }

    @Nonnull
    public static byte[] decompress(@Nonnull byte[] data, int off, int len) throws Exception
    {
        Inflater i = new Inflater();
        i.setInput(data, off, len);

        ByteArrayOutputStream os = new ByteArrayOutputStream(len);
        byte[] buffer = new byte[1024];
        while(!i.finished())
        {
            int count = i.inflate(buffer);
            os.write(buffer, 0, count);
        }

        os.close();
        byte[] output = os.toByteArray();
        i.end();
        return output;
    }
}