package com.feed_the_beast.ftbl.lib.guide;

import com.feed_the_beast.ftbl.api.guide.IGuideTextLine;
import com.feed_the_beast.ftbl.api.guide.IGuideTextLineProvider;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LatvianModder on 08.08.2016.
 */
public class GuidePageHelper
{
    public static final Map<String, IGuideTextLineProvider> INFO_TEXT_LINE_PROVIDERS = new HashMap<>();

    @Nullable
    public static IGuideTextLine createLine(GuidePage page, @Nullable JsonElement json)
    {
        if(json == null || json.isJsonNull())
        {
            return null;
        }
        else if(json.isJsonPrimitive())
        {
            String s = json.getAsString();
            return s.trim().isEmpty() ? null : new GuideTextLineString(s);
        }
        else if(json.isJsonArray())
        {
            return new GuideExtendedTextLine(json);
        }
        else
        {
            JsonObject o = json.getAsJsonObject();
            IGuideTextLineProvider provider = null;

            if(o.has("id"))
            {
                String id = o.get("id").getAsString();
                provider = INFO_TEXT_LINE_PROVIDERS.get(id);

                if(provider == null)
                {
                    ITextComponent component = new TextComponentString("Unknown ID: " + id);
                    component.getStyle().setColor(TextFormatting.DARK_RED);
                    component.getStyle().setBold(true);
                    return new GuideExtendedTextLine(component);
                }
            }
            /*
            else
            {
                provider = null;

                for(Map.Entry<String, JsonElement> entry : o.entrySet())
                {
                    provider = INFO_TEXT_LINE_PROVIDERS.get(entry.getKey());

                    if(provider != null)
                    {
                        break;
                    }
                }
            }*/

            IGuideTextLine line;

            if(provider != null)
            {
                line = provider.create(page, json);
            }
            else
            {
                line = new GuideExtendedTextLine(json);
            }

            return line;
        }
    }
}