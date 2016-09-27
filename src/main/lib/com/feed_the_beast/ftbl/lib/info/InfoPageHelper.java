package com.feed_the_beast.ftbl.lib.info;

import com.feed_the_beast.ftbl.api.events.InfoGuiLineEvent;
import com.feed_the_beast.ftbl.api.info.IGuiInfoPage;
import com.feed_the_beast.ftbl.api.info.IInfoPage;
import com.feed_the_beast.ftbl.api.info.IInfoTextLine;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by LatvianModder on 08.08.2016.
 */
public class InfoPageHelper
{
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
            JsonObject o = e.getAsJsonObject();

            IInfoTextLine l;

            if(o.has("image"))
            {
                l = new InfoImageLine();
            }
            else
            {
                InfoGuiLineEvent event = new InfoGuiLineEvent(page, o);
                MinecraftForge.EVENT_BUS.post(event);
                l = (event.getLine() == null) ? new InfoExtendedTextLine(null) : event.getLine();
            }

            l.fromJson(o);
            return l;
        }
    }
}