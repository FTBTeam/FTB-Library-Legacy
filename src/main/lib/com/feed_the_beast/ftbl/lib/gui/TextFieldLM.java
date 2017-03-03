package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;

import java.util.Collections;
import java.util.List;

/**
 * Created by LatvianModder on 04.03.2016.
 */
public class TextFieldLM extends ButtonLM
{
    public List<String> text = Collections.emptyList();

    public TextFieldLM(int x, int y, int width, int height, FontRenderer font, String txt)
    {
        super(x, y, width, height);

        if(!txt.isEmpty())
        {
            if(width >= 10)
            {
                text = font.listFormattedStringToWidth(txt, width);
            }
            else
            {
                text = Collections.singletonList(txt);
            }
        }

        if(height <= 0)
        {
            int h = font.FONT_HEIGHT + 1;
            setHeight(text.isEmpty() ? h : h * text.size());
        }
    }

    @Override
    public void addMouseOverText(IGui gui, List<String> list)
    {
    }

    @Override
    public void onClicked(IGui gui, IMouseButton button)
    {
    }

    @Override
    public void renderWidget(IGui gui)
    {
        if(text.isEmpty())
        {
            return;
        }

        int ay = getAY();
        int ax = getAX();

        for(int i = 0; i < text.size(); i++)
        {
            gui.getFont().drawString(text.get(i), ax, ay + i * 10 + 1, gui.getTextColor());
        }

        GlStateManager.color(1F, 1F, 1F, 1F);
    }
}
