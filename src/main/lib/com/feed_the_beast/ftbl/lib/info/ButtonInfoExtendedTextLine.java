package com.feed_the_beast.ftbl.lib.info;

import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.gui.GuiInfo;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.util.LMNetUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.event.ClickEvent;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LatvianModder on 04.03.2016.
 */
public class ButtonInfoExtendedTextLine extends ButtonInfoTextLine
{
    public ClickEvent clickEvent;
    public List<String> hover;

    public ButtonInfoExtendedTextLine(GuiInfo g, InfoExtendedTextLine l)
    {
        super(g, l.getText() == null ? null : l.getText().getFormattedText());

        clickEvent = l.getClickEvent();

        List<ITextComponent> h = l.getHover();

        if(h != null)
        {
            hover = new ArrayList<>();

            for(ITextComponent c1 : h)
            {
                hover.add(c1.getFormattedText());
            }

            if(hover.isEmpty())
            {
                hover = null;
            }
        }
        else
        {
            hover = null;
        }
    }

    @Override
    public void addMouseOverText(IGui gui, List<String> l)
    {
        if(hover != null)
        {
            l.addAll(hover);
        }
    }

    @Override
    public void onClicked(IGui gui, IMouseButton button)
    {
        if(clickEvent != null)
        {
            GuiHelper.playClickSound();

            switch(this.clickEvent.getAction())
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

    @Override
    public void renderWidget(IGui gui)
    {
        int ay = getAY();
        int ax = getAX();

        if(text != null && !text.isEmpty())
        {
            for(int i = 0; i < text.size(); i++)
            {
                gui.getFont().drawString(text.get(i), ax, ay + i * 10 + 1, ((GuiInfo) gui).colorText);
            }
        }
    }
}
