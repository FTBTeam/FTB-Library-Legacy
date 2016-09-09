package com.feed_the_beast.ftbl.api.info.impl;

import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.api.gui.widgets.ButtonLM;
import com.feed_the_beast.ftbl.gui.GuiInfo;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by LatvianModder on 04.03.2016.
 */
public class ButtonInfoTextLine extends ButtonLM
{
    public List<String> text;

    public ButtonInfoTextLine(GuiInfo g, @Nullable String txt)
    {
        super(0, g.panelText.getHeight(), 0, 0);

        if(txt != null && !txt.isEmpty())
        {
            text = g.getFont().listFormattedStringToWidth(txt, g.panelText.getWidth());
        }

        if(text != null)
        {
            if(text.size() > 1)
            {
                setWidth(g.panelText.getWidth());
            }
            else
            {
                setWidth(g.getFont().getStringWidth(text.get(0)));
            }

            setHeight(10 * text.size());
        }
        else
        {
            setWidth(0);
            setHeight(11);
        }
    }

    @Override
    public void addMouseOverText(IGui gui, List<String> l)
    {
    }

    @Override
    public void onClicked(IGui gui, IMouseButton button)
    {
    }

    @Override
    public void renderWidget(IGui gui)
    {
        int ay = getAY();
        int ax = getAX();

        if(text != null)
        {
            for(int i = 0; i < text.size(); i++)
            {
                gui.getFont().drawString(text.get(i), ax, ay + i * 10 + 1, ((GuiInfo) gui).colorText);
            }
        }
    }
}
