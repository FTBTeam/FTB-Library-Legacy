package com.feed_the_beast.ftbl.lib.info;

import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.api.gui.IPanel;
import com.feed_the_beast.ftbl.api.gui.IWidget;
import com.feed_the_beast.ftbl.api.info.IInfoTextLine;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.misc.GuiInfo;
import com.feed_the_beast.ftbl.lib.util.LMJsonUtils;
import com.google.gson.JsonElement;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by LatvianModder on 20.03.2016.
 */
public class InfoExtendedTextLine extends EmptyInfoPageLine
{
    public final ITextComponent textComponent;

    public InfoExtendedTextLine(@Nullable ITextComponent cc)
    {
        textComponent = cc;
    }

    public InfoExtendedTextLine(JsonElement json)
    {
        textComponent = LMJsonUtils.deserializeTextComponent(json);
    }

    @Override
    @Nullable
    public String getUnformattedText()
    {
        return textComponent == null ? null : textComponent.getUnformattedText();
    }

    @Override
    public IWidget createWidget(IGui gui, IPanel parent)
    {
        return new ButtonInfoExtendedTextLine(gui, parent);
    }

    @Override
    public IInfoTextLine copy(InfoPage page)
    {
        return new InfoExtendedTextLine(textComponent.createCopy());
    }

    @Override
    public JsonElement getJson()
    {
        return LMJsonUtils.serializeTextComponent(textComponent);
    }

    private class ButtonInfoExtendedTextLine extends ButtonInfoTextLine
    {
        private ClickEvent clickEvent;
        private String hover;

        private ButtonInfoExtendedTextLine(IGui gui, IPanel parent)
        {
            super(gui, parent, textComponent == null ? null : textComponent.getFormattedText());

            if(textComponent != null)
            {
                clickEvent = textComponent.getStyle().getClickEvent();

                HoverEvent hoverEvent = textComponent.getStyle().getHoverEvent();
                if(hoverEvent != null && hoverEvent.getAction() == HoverEvent.Action.SHOW_TEXT)
                {
                    hover = hoverEvent.getValue().getFormattedText();
                }
            }
        }

        @Override
        public void addMouseOverText(IGui gui, List<String> l)
        {
            if(hover != null)
            {
                l.add(hover);
            }
        }

        @Override
        public void onClicked(IGui gui, IMouseButton button)
        {
            if(clickEvent != null)
            {
                GuiHelper.playClickSound();
                InfoPageHelper.onClickEvent(clickEvent);
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
}