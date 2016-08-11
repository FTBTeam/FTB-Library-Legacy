package com.feed_the_beast.ftbl.api.info.impl;

import com.feed_the_beast.ftbl.api.gui.GuiLM;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.api.notification.ClickAction;
import com.feed_the_beast.ftbl.gui.GuiInfo;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LatvianModder on 04.03.2016.
 */
@SideOnly(Side.CLIENT)
public class ButtonInfoExtendedTextLine extends ButtonInfoTextLine
{
    public ClickAction clickAction;
    public List<String> hover;

    public ButtonInfoExtendedTextLine(GuiInfo g, @Nonnull InfoExtendedTextLine l)
    {
        super(g, l.getText() == null ? null : l.getText().getFormattedText());

        clickAction = l.getClickAction();

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
    public void addMouseOverText(@Nonnull GuiLM gui, @Nonnull List<String> l)
    {
        if(hover != null)
        {
            l.addAll(hover);
        }
    }

    @Override
    public void onClicked(@Nonnull GuiLM gui, @Nonnull IMouseButton button)
    {
        if(clickAction != null)
        {
            GuiLM.playClickSound();
            clickAction.onClicked(button);
        }
    }

    @Override
    public void renderWidget(@Nonnull GuiLM gui)
    {
        int ay = getAY();
        int ax = getAX();

        if(text != null && !text.isEmpty())
        {
            for(int i = 0; i < text.size(); i++)
            {
                gui.font.drawString(text.get(i), ax, ay + i * 10 + 1, ((GuiInfo) gui).colorText);
            }
        }
    }
}
