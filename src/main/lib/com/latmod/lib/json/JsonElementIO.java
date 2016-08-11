package com.latmod.lib.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.latmod.lib.io.ByteIOStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

/**
 * Created by LatvianModder on 23.01.2016.
 */
public class JsonElementIO
{
    @Nonnull
    public static EnumJsonID getID(@Nullable JsonElement e)
    {
        if(e == null || e.isJsonNull())
        {
            return EnumJsonID.NULL;
        }
        else if(e.isJsonArray())
        {
            return EnumJsonID.ARRAY;
        }
        else if(e.isJsonObject())
        {
            return EnumJsonID.OBJECT;
        }
        else
        {
            JsonPrimitive p = e.getAsJsonPrimitive();

            if(p.isString())
            {
                return EnumJsonID.STRING;
            }
            else if(p.isBoolean())
            {
                return EnumJsonID.BOOL;
            }
            else
            {
                Number n = p.getAsNumber();

                if(n instanceof Integer)
                {
                    return EnumJsonID.INT;
                }
                else if(n instanceof Byte)
                {
                    return EnumJsonID.BYTE;
                }
                else if(n instanceof Short)
                {
                    return EnumJsonID.SHORT;
                }
                else if(n instanceof Long)
                {
                    return EnumJsonID.LONG;
                }
                else if(n instanceof Float)
                {
                    return EnumJsonID.FLOAT;
                }
                else if(n instanceof Double)
                {
                    return EnumJsonID.DOUBLE;
                }
                else
                {
                    return EnumJsonID.NULL;
                }
            }
        }
    }

    @Nonnull
    public static JsonElement read(@Nonnull ByteIOStream io)
    {
        switch(EnumJsonID.values()[io.readByte()])
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
                String s = io.readUTF();
                return s == null ? JsonNull.INSTANCE : new JsonPrimitive(s);
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

    public static void write(@Nonnull ByteIOStream io, @Nullable JsonElement e)
    {
        if(e == null || e.isJsonNull())
        {
            io.writeByte(EnumJsonID.NULL.ID);
        }
        else if(e.isJsonArray())
        {
            io.writeByte(EnumJsonID.ARRAY.ID);

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
            io.writeByte(EnumJsonID.OBJECT.ID);

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
                io.writeByte(EnumJsonID.STRING.ID);
                io.writeUTF(p.getAsString());
            }
            else if(p.isBoolean())
            {
                io.writeByte(EnumJsonID.BOOL.ID);
                io.writeBoolean(p.getAsBoolean());
            }
            else
            {
                Number n = p.getAsNumber();

                if(n instanceof Integer)
                {
                    io.writeByte(EnumJsonID.INT.ID);
                    io.writeInt(n.intValue());
                }
                else if(n instanceof Byte)
                {
                    io.writeByte(EnumJsonID.BYTE.ID);
                    io.writeByte(n.byteValue());
                }
                else if(n instanceof Short)
                {
                    io.writeByte(EnumJsonID.SHORT.ID);
                    io.writeShort(n.shortValue());
                }
                else if(n instanceof Long)
                {
                    io.writeByte(EnumJsonID.LONG.ID);
                    io.writeLong(n.longValue());
                }
                else if(n instanceof Float)
                {
                    io.writeByte(EnumJsonID.FLOAT.ID);
                    io.writeFloat(n.floatValue());
                }
                else if(n instanceof Double)
                {
                    io.writeByte(EnumJsonID.DOUBLE.ID);
                    io.writeDouble(n.doubleValue());
                }
                else
                {
                    io.writeByte(EnumJsonID.NULL.ID);
                }
            }
        }
    }
}
