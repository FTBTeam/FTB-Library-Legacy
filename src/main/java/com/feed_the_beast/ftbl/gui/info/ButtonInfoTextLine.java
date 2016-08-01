package com.feed_the_beast.ftbl.gui.info;

import com.feed_the_beast.ftbl.api.MouseButton;
import com.feed_the_beast.ftbl.api.client.gui.GuiLM;
import com.feed_the_beast.ftbl.api.client.gui.widgets.ButtonLM;
import com.feed_the_beast.ftbl.api.info.InfoTextLine;
import net.minecraft.util.text.ITextComponent;
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
    public final GuiInfo guiInfo;
    public List<String> text;

    public ButtonInfoTextLine(GuiInfo g, InfoTextLine l)
    {
        super(0, g.panelText.height, 0, 0);
        guiInfo = g;

        if(l != null)
        {
            ITextComponent c = l.getText();

            if(c != null)
            {
                text = guiInfo.font.listFormattedStringToWidth(c.getFormattedText(), g.panelText.width);
                if(text.isEmpty())
                {
                    text = null;
                }
            }
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
    public void addMouseOverText(GuiLM gui, List<String> l)
    {
    }

    @Override
    public void onClicked(@Nonnull GuiLM gui, @Nonnull MouseButton button)
    {
    }

    @Override
    public void renderWidget(GuiLM gui)
    {
        int ay = getAY();
        int ax = getAX();

        if(text != null)
        {
            for(int i = 0; i < text.size(); i++)
            {
                guiInfo.font.drawString(text.get(i), ax, ay + i * 10 + 1, guiInfo.colorText);
            }
        }
    }
}
