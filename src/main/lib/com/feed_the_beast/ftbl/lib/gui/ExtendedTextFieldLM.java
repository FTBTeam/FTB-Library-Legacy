package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by LatvianModder on 23.02.2017.
 */
public class ExtendedTextFieldLM extends TextFieldLM
{
    private static class CachedTextData extends WidgetLM
    {
        private final ClickEvent clickEvent;
        private final HoverEvent hoverEvent;
        private final String insertion;

        private CachedTextData(int x, int y, int w, int h, Style s)
        {
            super(x, y, w, h);
            clickEvent = s.getClickEvent();
            hoverEvent = s.getHoverEvent();
            insertion = s.getInsertion();
        }
    }

    public final ITextComponent textComponent;
    private List<CachedTextData> textData;

    public ExtendedTextFieldLM(int x, int y, int width, int height, FontRenderer font, ITextComponent t)
    {
        super(x, y, width, height, font, t.getFormattedText());
        textComponent = t;
    }

    @Nullable
    private CachedTextData getDataAtMouse(IGui gui)
    {
        int ax = getAX();
        int ay = getAY();

        for(CachedTextData data : textData)
        {
            if(gui.isMouseOver(data.posX + ax, data.posY + ay, 0, 0))
            {
                return data;
            }
        }

        return null;
    }

    @Override
    public void addMouseOverText(IGui gui, List<String> list)
    {
        CachedTextData data = getDataAtMouse(gui);

        if(data != null && data.hoverEvent != null)
        {
        }
    }

    @Override
    public void onClicked(IGui gui, IMouseButton button)
    {
        CachedTextData data = getDataAtMouse(gui);

        if(data != null && data.clickEvent != null && GuiHelper.onClickEvent(data.clickEvent))
        {
            GuiHelper.playClickSound();
        }
    }
}