package com.feed_the_beast.ftbl.lib.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNullableByDefault;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Type for Lists: new TypeToken<List<E>>() {}.getType()
 */
@ParametersAreNullableByDefault
public class LMJsonUtils
{
    public static final JsonDeserializationContext DESERIALIZATION_CONTEXT;
    public static final JsonSerializationContext SERIALIZATION_CONTEXT, PRETTY_SERIALIZATION_CONTEXT;
    public static final Gson GSON;
    public static final Gson GSON_PRETTY;
    public static final Gson TEXT_COMPONENT_GSON;
    public static final JsonParser PARSER;

    static
    {
        GsonBuilder gb = new GsonBuilder();
        GSON = gb.create();
        gb.setPrettyPrinting();
        GSON_PRETTY = gb.create();

        DESERIALIZATION_CONTEXT = new JsonDeserializationContext()
        {
            @Override
            public <T> T deserialize(@Nonnull JsonElement json, @Nonnull Type typeOfT) throws JsonParseException
            {
                return GSON.fromJson(json, typeOfT);
            }
        };

        SERIALIZATION_CONTEXT = new JsonSerializationContext()
        {
            @Override
            public JsonElement serialize(Object src)
            {
                return GSON.toJsonTree(src);
            }

            @Override
            public JsonElement serialize(@Nonnull Object src, @Nonnull Type typeOfSrc)
            {
                return GSON.toJsonTree(src, typeOfSrc);
            }
        };

        PRETTY_SERIALIZATION_CONTEXT = new JsonSerializationContext()
        {
            @Override
            public JsonElement serialize(@Nonnull Object src)
            {
                return GSON_PRETTY.toJsonTree(src);
            }

            @Override
            public JsonElement serialize(@Nonnull Object src, @Nonnull Type typeOfSrc)
            {
                return GSON_PRETTY.toJsonTree(src, typeOfSrc);
            }
        };

        try
        {
            TEXT_COMPONENT_GSON = (Gson) ReflectionHelper.findField(ITextComponent.Serializer.class, "field_150700_a", "GSON").get(null);
        }
        catch(Exception ex)
        {
            throw new NullPointerException("Failed to read ITextComponent.Serializer.GSON!");
        }

        PARSER = new JsonParser();
    }

    public static String toJson(@Nonnull Gson gson, JsonElement e)
    {
        return gson.toJson(e == null ? JsonNull.INSTANCE : e);
    }

    public static boolean toJson(@Nonnull Gson gson, @Nonnull File f, JsonElement o)
    {
        try
        {
            LMFileUtils.save(f, toJson(gson, o));
            return true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }

    public static String toJson(JsonElement o)
    {
        return toJson(GSON, o);
    }

    public static boolean toJson(@Nonnull File f, JsonElement o)
    {
        return toJson(GSON_PRETTY, f, o);
    }

    public static JsonElement fromJson(String json)
    {
        return (json == null || json.isEmpty()) ? JsonNull.INSTANCE : PARSER.parse(json);
    }

    public static JsonElement fromJson(Reader json)
    {
        return (json == null) ? JsonNull.INSTANCE : PARSER.parse(json);
    }

    public static JsonElement fromJson(File json)
    {
        try
        {
            if(json == null || !json.exists())
            {
                return JsonNull.INSTANCE;
            }

            FileInputStream fis = new FileInputStream(json);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis, LMStringUtils.UTF_8));
            JsonElement e = fromJson(reader);
            reader.close();
            fis.close();
            return e;
        }
        catch(Exception ex)
        {
            return JsonNull.INSTANCE;
        }
    }

    // -- //

    public static JsonElement toIntArray(int... ai)
    {
        if(ai == null)
        {
            return JsonNull.INSTANCE;
        }

        JsonArray a = new JsonArray();
        if(ai.length == 0)
        {
            return a;
        }

        for(int anAi : ai)
        {
            a.add(new JsonPrimitive(anAi));
        }

        return a;
    }

    @Nullable
    public static int[] fromIntArray(JsonElement e)
    {
        if(e == null || e.isJsonNull())
        {
            return null;
        }

        if(e.isJsonArray())
        {
            JsonArray a = e.getAsJsonArray();
            int[] ai = new int[a.size()];
            if(ai.length == 0)
            {
                return ai;
            }
            for(int i = 0; i < ai.length; i++)
            {
                ai[i] = a.get(i).getAsInt();
            }
            return ai;
        }

        return new int[] {e.getAsInt()};
    }

    public static JsonElement toNumberArray(Number[] ai)
    {
        if(ai == null)
        {
            return JsonNull.INSTANCE;
        }

        JsonArray a = new JsonArray();
        if(ai.length == 0)
        {
            return a;
        }

        for(Number anAi : ai)
        {
            a.add(new JsonPrimitive(anAi));
        }

        return a;
    }

    @Nullable
    public static Number[] fromNumberArray(JsonElement e)
    {
        if(e == null || e.isJsonNull())
        {
            return null;
        }

        if(e.isJsonArray())
        {
            JsonArray a = e.getAsJsonArray();
            Number[] ai = new Number[a.size()];
            if(ai.length == 0)
            {
                return ai;
            }
            for(int i = 0; i < ai.length; i++)
            {
                ai[i] = a.get(i).getAsNumber();
            }
            return ai;
        }

        return new Number[] {e.getAsNumber()};
    }

    public static JsonElement toStringArray(String... ai)
    {
        if(ai == null)
        {
            return JsonNull.INSTANCE;
        }

        JsonArray a = new JsonArray();
        if(ai.length == 0)
        {
            return a;
        }

        for(String anAi : ai)
        {
            a.add(new JsonPrimitive(anAi));
        }

        return a;
    }

    @Nullable
    public static String[] fromStringArray(JsonElement e)
    {
        if(e == null || e.isJsonNull() || !e.isJsonArray())
        {
            return null;
        }
        JsonArray a = e.getAsJsonArray();
        String[] ai = new String[a.size()];
        if(ai.length == 0)
        {
            return ai;
        }
        for(int i = 0; i < ai.length; i++)
        {
            ai[i] = a.get(i).getAsString();
        }
        return ai;
    }

    public static List<JsonElement> deserializeText(List<String> text)
    {
        List<JsonElement> elements = new ArrayList<>();

        if(text == null || text.isEmpty())
        {
            return elements;
        }

        StringBuilder sb = new StringBuilder();
        int inc = 0;

        for(String s : text)
        {
            s = LMStringUtils.trimAllWhitespace(s);

            System.out.println(s);

            if(s.isEmpty())
            {
                elements.add(JsonNull.INSTANCE);
            }
            else
            {
                if(inc > 0 || s.startsWith("{"))
                {
                    for(int i = 0; i < s.length(); i++)
                    {
                        char c = s.charAt(i);
                        if(c == '{')
                        {
                            inc++;
                        }
                        else if(c == '}')
                        {
                            inc--;
                        }
                        sb.append(c);

                        if(inc == 0)
                        {
                            System.out.println(":: " + sb);
                            elements.add(fromJson(sb.toString()));
                            sb.setLength(0);
                        }
                    }
                }
                else
                {
                    elements.add(new JsonPrimitive(s));
                }
            }
        }

        return elements;
    }

    public static JsonElement serializeTextComponent(ITextComponent c)
    {
        return (c == null) ? JsonNull.INSTANCE : TEXT_COMPONENT_GSON.toJsonTree(c, ITextComponent.class);
    }

    @Nullable
    public static ITextComponent deserializeTextComponent(JsonElement e)
    {
        return (e == null || e.isJsonNull()) ? null : TEXT_COMPONENT_GSON.fromJson(e, ITextComponent.class);
    }

    @Nonnull
    public static JsonObject fromJsonTree(@Nonnull JsonObject o)
    {
        JsonObject map = new JsonObject();
        fromJsonTree0(map, null, o);
        return map;
    }

    private static void fromJsonTree0(@Nonnull JsonObject map, String id0, @Nonnull JsonObject o)
    {
        for(Map.Entry<String, JsonElement> entry : o.entrySet())
        {
            if(entry.getValue() instanceof JsonObject)
            {
                fromJsonTree0(map, (id0 == null) ? entry.getKey() : (id0 + '.' + entry.getKey()), entry.getValue().getAsJsonObject());
            }
            else
            {
                map.add((id0 == null) ? entry.getKey() : (id0 + '.' + entry.getKey()), entry.getValue());
            }
        }
    }

    @Nonnull
    public static JsonObject toJsonTree(@Nonnull Collection<Map.Entry<String, JsonElement>> tree)
    {
        JsonObject o1 = new JsonObject();
        tree.forEach(entry -> findGroup(o1, entry.getKey()).add(lastKeyPart(entry.getKey()), entry.getValue()));
        return o1;
    }

    @Nonnull
    private static String lastKeyPart(String s)
    {
        int idx = s.lastIndexOf('.');

        if(idx != -1)
        {
            return s.substring(idx + 1);
        }

        return s;
    }

    @Nonnull
    private static JsonObject findGroup(@Nonnull JsonObject parent, @Nonnull String s)
    {
        int idx = s.indexOf('.');

        if(idx != -1)
        {
            String s0 = s.substring(0, idx);

            JsonElement o = parent.get(s0);

            if(o == null)
            {
                o = new JsonObject();
                parent.add(s0, o);
            }

            return findGroup(o.getAsJsonObject(), s.substring(idx + 1, s.length() - 1));
        }

        return parent;
    }
}