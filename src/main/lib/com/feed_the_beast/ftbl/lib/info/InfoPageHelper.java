package com.feed_the_beast.ftbl.lib.info;

import com.feed_the_beast.ftbl.api.info.IInfoTextLine;
import com.feed_the_beast.ftbl.api.info.IInfoTextLineProvider;
import com.feed_the_beast.ftbl.lib.client.FTBLibClient;
import com.feed_the_beast.ftbl.lib.util.LMNetUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LatvianModder on 08.08.2016.
 */
public class InfoPageHelper
{
    public static final Map<String, IInfoTextLineProvider> INFO_TEXT_LINE_PROVIDERS = new HashMap<>();

    public static String getUnformattedText(InfoPage page)
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
    public static IInfoTextLine createLine(InfoPage page, @Nullable JsonElement json)
    {
        if(json == null || json.isJsonNull())
        {
            return null;
        }
        else if(json.isJsonPrimitive())
        {
            String s = json.getAsString();
            return s.trim().isEmpty() ? null : new InfoTextLineString(s);
        }
        else if(json.isJsonArray())
        {
            return new InfoListLine(page, json);
        }
        else
        {
            JsonObject o = json.getAsJsonObject();
            IInfoTextLineProvider provider = null;

            if(o.has("id"))
            {
                provider = INFO_TEXT_LINE_PROVIDERS.get(o.get("id").getAsString());
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

            IInfoTextLine line;

            if(provider != null)
            {
                line = provider.create(page, json);
            }
            else
            {
                line = new InfoExtendedTextLine(json);
            }

            return line;
        }
    }

    @SideOnly(Side.CLIENT)
    public static void onClickEvent(ClickEvent clickEvent)
    {
        switch(clickEvent.getAction())
        {
            case OPEN_URL:
            {
                try
                {
                    final URI uri = new URI(clickEvent.getValue());
                    String s = uri.getScheme();

                    if(s == null)
                    {
                        throw new URISyntaxException(clickEvent.getValue(), "Missing protocol");
                    }
                    if(!s.toLowerCase().contains("http") && !s.toLowerCase().contains("https"))
                    {
                        throw new URISyntaxException(clickEvent.getValue(), "Unsupported protocol: " + s.toLowerCase());
                    }

                    Minecraft mc = Minecraft.getMinecraft();

                    if(mc.gameSettings.chatLinksPrompt)
                    {
                        final GuiScreen currentScreen = mc.currentScreen;

                        mc.displayGuiScreen(new GuiConfirmOpenLink((result, id) ->
                        {
                            if(result)
                            {
                                try
                                {
                                    LMNetUtils.openURI(uri);
                                }
                                catch(Exception ex)
                                {
                                    ex.printStackTrace();
                                }
                            }
                            mc.displayGuiScreen(currentScreen);
                        }, clickEvent.getValue(), 0, false));
                    }
                    else
                    {
                        LMNetUtils.openURI(uri);
                    }
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
            }
            case OPEN_FILE:
            {
                try
                {
                    LMNetUtils.openURI((new File(clickEvent.getValue())).toURI());
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
            }
            case SUGGEST_COMMAND:
            {
                //FIXME
            }
            case RUN_COMMAND:
            {
                FTBLibClient.execClientCommand(clickEvent.getValue(), true);
            }
        }
    }
}