package com.feed_the_beast.ftbl.api.info.impl;

import com.feed_the_beast.ftbl.api.gui.GuiLM;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.api.gui.widgets.ButtonLM;
import com.feed_the_beast.ftbl.gui.GuiInfo;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Created by LatvianModder on 04.03.2016.
 */
@SideOnly(Side.CLIENT)
public class ButtonInfoTextLine extends ButtonLM
{
    public List<String> text;

    public ButtonInfoTextLine(GuiInfo g, String txt)
    {
        super(0, g.panelText.height, 0, 0);

        if(txt != null && !txt.isEmpty())
        {
            text = g.font.listFormattedStringToWidth(txt, g.panelText.width);
        }

        if(text != null)
        {
            if(text.size() > 1)
            {
                width = g.panelText.width;
            }
            else
            {
                width = g.font.getStringWidth(text.get(0));
            }

            height = 10 * text.size();
        }
        else
        {
            width = 0;
            height = 11;
        }
    }

    @Override
    public void addMouseOverText(@Nonnull GuiLM gui, @Nonnull List<String> l)
    {
    }

    @Override
    public void onClicked(@Nonnull GuiLM gui, @Nonnull IMouseButton button)
    {
    }

    @Override
    public void renderWidget(@Nonnull GuiLM gui)
    {
        int ay = getAY();
        int ax = getAX();

        if(text != null)
        {
            for(int i = 0; i < text.size(); i++)
            {
                gui.font.drawString(text.get(i), ax, ay + i * 10 + 1, ((GuiInfo) gui).colorText);
            }
        }
    }
}
