package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by LatvianModder on 23.02.2017.
 */
public class ExtendedTextFieldLM extends TextFieldLM
{
    public final ITextComponent textComponent;
    private List<GuiHelper.PositionedTextData> textData;

    public ExtendedTextFieldLM(int x, int y, int width, int height, FontRenderer font, ITextComponent t)
    {
        super(x, y, width, height, font, t.getFormattedText());
        textComponent = t;
        textData = GuiHelper.createDataFrom(t, font, width);
    }

    @Nullable
    private GuiHelper.PositionedTextData getDataAtMouse(IGui gui)
    {
        int ax = getAX();
        int ay = getAY();

        for(GuiHelper.PositionedTextData data : textData)
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
        GuiHelper.PositionedTextData data = getDataAtMouse(gui);

        if(data != null && data.hoverEvent != null) //TODO: Special handling for each data.hoverEvent.getAction()
        {
            for(String s : data.hoverEvent.getValue().getFormattedText().split("\n"))
            {
                list.add(s);
            }
        }
    }

    @Override
    public void onClicked(IGui gui, IMouseButton button)
    {
        GuiHelper.PositionedTextData data = getDataAtMouse(gui);

        if(data != null && data.clickEvent != null && GuiHelper.onClickEvent(data.clickEvent))
        {
            GuiHelper.playClickSound();
        }
    }
}