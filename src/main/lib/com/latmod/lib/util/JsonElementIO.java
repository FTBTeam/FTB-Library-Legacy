package com.latmod.lib.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.latmod.lib.io.ByteIOStream;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

/**
 * Created by LatvianModder on 23.01.2016.
 */
public class JsonElementIO
{
    public static final byte NULL = 0;
    public static final byte ARRAY = 1;
    public static final byte OBJECT = 2;
    public static final byte STRING = 3;
    public static final byte BOOL = 4;
    public static final byte BYTE = 5;
    public static final byte SHORT = 6;
    public static final byte INT = 7;
    public static final byte LONG = 8;
    public static final byte FLOAT = 9;
    public static final byte DOUBLE = 10;

    public static byte getID(@Nullable JsonElement e)
    {
        if(e == null || e.isJsonNull())
        {
            return NULL;
        }
        else if(e.isJsonArray())
        {
            return ARRAY;
        }
        else if(e.isJsonObject())
        {
            return OBJECT;
        }
        else
        {
            JsonPrimitive p = e.getAsJsonPrimitive();

            if(p.isString())
            {
                return STRING;
            }
            else if(p.isBoolean())
            {
                return BOOL;
            }
            else
            {
                Class<? extends Number> n = p.getAsNumber().getClass();

                if(n == Integer.class)
                {
                    return INT;
                }
                else if(n == Byte.class)
                {
                    return BYTE;
                }
                else if(n == Short.class)
                {
                    return SHORT;
                }
                else if(n == Long.class)
                {
                    return LONG;
                }
                else if(n == Float.class)
                {
                    return FLOAT;
                }
                else if(n == Double.class)
                {
                    return DOUBLE;
                }
                else
                {
                    return NULL;
                }
            }
        }
    }

    public static JsonElement read(ByteIOStream io)
    {
        switch(io.readByte())
        {
            case NULL:
                return JsonNull.INSTANCE;
            case ARRAY:
            {
                JsonArray a = new JsonArray();
                int s = io.readInt();

                for(int i = 0; i < s; i++)
                {
                    a.add(read(io));
                }

                return a;
            }
            case OBJECT:
            {
                JsonObject o = new JsonObject();
                int s = io.readInt();

                for(int i = 0; i < s; i++)
                {
                    String key = io.readUTF();

                    if(key != null)
                    {
                        o.add(key, read(io));
                    }
                }

                return o;
            }
            case STRING:
                return new JsonPrimitive(io.readUTF());
            case BOOL:
                return new JsonPrimitive(io.readBoolean());
            case BYTE:
                return new JsonPrimitive(io.readByte());
            case SHORT:
                return new JsonPrimitive(io.readShort());
            case INT:
                return new JsonPrimitive(io.readInt());
            case LONG:
                return new JsonPrimitive(io.readLong());
            case FLOAT:
                return new JsonPrimitive(io.readFloat());
            case DOUBLE:
                return new JsonPrimitive(io.readDouble());
        }

        return JsonNull.INSTANCE;
    }

    public static void write(ByteIOStream io, @Nullable JsonElement e)
    {
        if(e == null || e.isJsonNull())
        {
            io.writeByte(NULL);
        }
        else if(e.isJsonArray())
        {
            io.writeByte(ARRAY);

            JsonArray a = e.getAsJsonArray();
            int s = a.size();
            io.writeInt(s);

            for(int i = 0; i < s; i++)
            {
                write(io, a.get(i));
            }
        }
        else if(e.isJsonObject())
        {
            io.writeByte(OBJECT);

            Set<Map.Entry<String, JsonElement>> set = e.getAsJsonObject().entrySet();
            io.writeInt(set.size());

            for(Map.Entry<String, JsonElement> entry : set)
            {
                io.writeUTF(entry.getKey());
                write(io, entry.getValue());
            }
        }
        else
        {
            JsonPrimitive p = e.getAsJsonPrimitive();

            if(p.isString())
            {
                io.writeByte(STRING);
                io.writeUTF(p.getAsString());
            }
            else if(p.isBoolean())
            {
                io.writeByte(BOOL);
                io.writeBoolean(p.getAsBoolean());
            }
            else
            {
                Class<? extends Number> n = p.getAsNumber().getClass();

                if(n == Integer.class)
                {
                    io.writeByte(INT);
                    io.writeInt(p.getAsInt());
                }
                else if(n == Byte.class)
                {
                    io.writeByte(BYTE);
                    io.writeByte(p.getAsByte());
                }
                else if(n == Short.class)
                {
                    io.writeByte(SHORT);
                    io.writeShort(p.getAsShort());
                }
                else if(n == Long.class)
                {
                    io.writeByte(LONG);
                    io.writeLong(p.getAsLong());
                }
                else if(n == Float.class)
                {
                    io.writeByte(FLOAT);
                    io.writeFloat(p.getAsFloat());
                }
                else if(n == Double.class)
                {
                    io.writeByte(DOUBLE);
                    io.writeDouble(p.getAsDouble());
                }
                else
                {
                    io.writeByte(NULL);
                }
            }
        }
    }
}
