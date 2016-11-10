package com.feed_the_beast.ftbl.lib.info;

import com.feed_the_beast.ftbl.api.info.IGuiInfoPage;
import com.feed_the_beast.ftbl.api.info.IInfoPage;
import com.feed_the_beast.ftbl.api.info.IInfoTextLine;
import com.feed_the_beast.ftbl.api.info.IInfoTextLineProvider;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LatvianModder on 08.08.2016.
 */
public class InfoPageHelper
{
    public static final Map<String, IInfoTextLineProvider> INFO_TEXT_LINE_PROVIDERS = new HashMap<>();

    public static String getUnformattedText(IInfoPage page)
    {
        List<IInfoTextLine> text = page.getText();

        if(text.isEmpty())
        {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        int s = text.size();
        for(int i = 0; i < s; i++)
        {
            IInfoTextLine c = text.get(i);

            if(c == null)
            {
                sb.append('\n');
            }
            else
            {
                String s1 = c.getUnformattedText();
                sb.append((s1 == null || s1.isEmpty()) ? "\n" : s1);
            }

            if(i != s - 1)
            {
                sb.append('\n');
            }
        }
        return sb.toString();
    }

    @Nullable
    public static IInfoTextLine createLine(IGuiInfoPage page, @Nullable JsonElement e)
    {
        if(e == null || e.isJsonNull())
        {
            return null;
        }
        else if(e.isJsonPrimitive())
        {
            String s = e.getAsString();
            return s.trim().isEmpty() ? null : new InfoTextLineString(s);
        }
        else
        {
            IInfoTextLine line = null;

            if(e.isJsonObject())
            {
                JsonObject o = e.getAsJsonObject();

                if(o.has("id"))
                {
                    IInfoTextLineProvider provider = INFO_TEXT_LINE_PROVIDERS.get(o.get("id").getAsString());

                    if(provider != null)
                    {
                        line = provider.create(page, o);
                    }
                }
            }

            if(line == null)
            {
                line = new InfoExtendedTextLine(null);
            }

            line.fromJson(e);
            return line;
        }
    }
}