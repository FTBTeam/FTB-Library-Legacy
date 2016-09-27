package com.feed_the_beast.ftbl.lib.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import javax.annotation.Nullable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
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

    public static JsonElement read(DataInput data) throws IOException
    {
        switch(data.readByte())
        {
            case NULL:
                return JsonNull.INSTANCE;
            case ARRAY:
            {
                JsonArray a = new JsonArray();
                int s = data.readInt();

                for(int i = 0; i < s; i++)
                {
                    a.add(read(data));
                }

                return a;
            }
            case OBJECT:
            {
                JsonObject o = new JsonObject();
                int s = data.readInt();

                for(int i = 0; i < s; i++)
                {
                    String key = data.readUTF();
                    o.add(key, read(data));
                }

                return o;
            }
            case STRING:
                return new JsonPrimitive(data.readUTF());
            case BOOL:
                return new JsonPrimitive(data.readBoolean());
            case BYTE:
                return new JsonPrimitive(data.readByte());
            case SHORT:
                return new JsonPrimitive(data.readShort());
            case INT:
                return new JsonPrimitive(data.readInt());
            case LONG:
                return new JsonPrimitive(data.readLong());
            case FLOAT:
                return new JsonPrimitive(data.readFloat());
            case DOUBLE:
                return new JsonPrimitive(data.readDouble());
        }

        return JsonNull.INSTANCE;
    }

    public static void write(DataOutput data, @Nullable JsonElement e) throws IOException
    {
        if(e == null || e.isJsonNull())
        {
            data.writeByte(NULL);
        }
        else if(e.isJsonArray())
        {
            data.writeByte(ARRAY);

            JsonArray a = e.getAsJsonArray();
            int s = a.size();
            data.writeInt(s);

            for(int i = 0; i < s; i++)
            {
                write(data, a.get(i));
            }
        }
        else if(e.isJsonObject())
        {
            data.writeByte(OBJECT);

            Set<Map.Entry<String, JsonElement>> set = e.getAsJsonObject().entrySet();
            data.writeInt(set.size());

            for(Map.Entry<String, JsonElement> entry : set)
            {
                data.writeUTF(entry.getKey());
                write(data, entry.getValue());
            }
        }
        else
        {
            JsonPrimitive p = e.getAsJsonPrimitive();

            if(p.isString())
            {
                data.writeByte(STRING);
                data.writeUTF(p.getAsString());
            }
            else if(p.isBoolean())
            {
                data.writeByte(BOOL);
                data.writeBoolean(p.getAsBoolean());
            }
            else
            {
                Class<? extends Number> n = p.getAsNumber().getClass();

                if(n == Integer.class)
                {
                    data.writeByte(INT);
                    data.writeInt(p.getAsInt());
                }
                else if(n == Byte.class)
                {
                    data.writeByte(BYTE);
                    data.writeByte(p.getAsByte());
                }
                else if(n == Short.class)
                {
                    data.writeByte(SHORT);
                    data.writeShort(p.getAsShort());
                }
                else if(n == Long.class)
                {
                    data.writeByte(LONG);
                    data.writeLong(p.getAsLong());
                }
                else if(n == Float.class)
                {
                    data.writeByte(FLOAT);
                    data.writeFloat(p.getAsFloat());
                }
                else if(n == Double.class)
                {
                    data.writeByte(DOUBLE);
                    data.writeDouble(p.getAsDouble());
                }
                else
                {
                    data.writeByte(NULL);
                }
            }
        }
    }

    public static JsonElement read(ByteBuf data)
    {
        switch(data.readByte())
        {
            case NULL:
                return JsonNull.INSTANCE;
            case ARRAY:
            {
                JsonArray a = new JsonArray();
                int s = data.readInt();

                for(int i = 0; i < s; i++)
                {
                    a.add(read(data));
                }

                return a;
            }
            case OBJECT:
            {
                JsonObject o = new JsonObject();
                int s = data.readInt();

                for(int i = 0; i < s; i++)
                {
                    String key = ByteBufUtils.readUTF8String(data);
                    o.add(key, read(data));
                }

                return o;
            }
            case STRING:
                return new JsonPrimitive(ByteBufUtils.readUTF8String(data));
            case BOOL:
                return new JsonPrimitive(data.readBoolean());
            case BYTE:
                return new JsonPrimitive(data.readByte());
            case SHORT:
                return new JsonPrimitive(data.readShort());
            case INT:
                return new JsonPrimitive(data.readInt());
            case LONG:
                return new JsonPrimitive(data.readLong());
            case FLOAT:
                return new JsonPrimitive(data.readFloat());
            case DOUBLE:
                return new JsonPrimitive(data.readDouble());
        }

        return JsonNull.INSTANCE;
    }

    public static void write(ByteBuf data, @Nullable JsonElement e)
    {
        if(e == null || e.isJsonNull())
        {
            data.writeByte(NULL);
        }
        else if(e.isJsonArray())
        {
            data.writeByte(ARRAY);

            JsonArray a = e.getAsJsonArray();
            int s = a.size();
            data.writeInt(s);

            for(int i = 0; i < s; i++)
            {
                write(data, a.get(i));
            }
        }
        else if(e.isJsonObject())
        {
            data.writeByte(OBJECT);

            Set<Map.Entry<String, JsonElement>> set = e.getAsJsonObject().entrySet();
            data.writeInt(set.size());

            for(Map.Entry<String, JsonElement> entry : set)
            {
                ByteBufUtils.writeUTF8String(data, entry.getKey());
                write(data, entry.getValue());
            }
        }
        else
        {
            JsonPrimitive p = e.getAsJsonPrimitive();

            if(p.isString())
            {
                data.writeByte(STRING);
                ByteBufUtils.writeUTF8String(data, p.getAsString());
            }
            else if(p.isBoolean())
            {
                data.writeByte(BOOL);
                data.writeBoolean(p.getAsBoolean());
            }
            else
            {
                Class<? extends Number> n = p.getAsNumber().getClass();

                if(n == Integer.class)
                {
                    data.writeByte(INT);
                    data.writeInt(p.getAsInt());
                }
                else if(n == Byte.class)
                {
                    data.writeByte(BYTE);
                    data.writeByte(p.getAsByte());
                }
                else if(n == Short.class)
                {
                    data.writeByte(SHORT);
                    data.writeShort(p.getAsShort());
                }
                else if(n == Long.class)
                {
                    data.writeByte(LONG);
                    data.writeLong(p.getAsLong());
                }
                else if(n == Float.class)
                {
                    data.writeByte(FLOAT);
                    data.writeFloat(p.getAsFloat());
                }
                else if(n == Double.class)
                {
                    data.writeByte(DOUBLE);
                    data.writeDouble(p.getAsDouble());
                }
                else
                {
                    data.writeByte(NULL);
                }
            }
        }
    }
}