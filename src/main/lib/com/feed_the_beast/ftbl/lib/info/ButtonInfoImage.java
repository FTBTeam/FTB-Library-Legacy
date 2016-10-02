package com.feed_the_beast.ftbl.lib.info;

import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.gui.GuiInfo;
import com.feed_the_beast.ftbl.lib.gui.ButtonLM;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import net.minecraft.client.renderer.GlStateManager;

import java.util.List;

/**
 * Created by LatvianModder on 04.03.2016.
 */
//FIXME: Image sizes
class ButtonInfoImage extends ButtonLM
{
    private final InfoImageLine line;

    ButtonInfoImage(GuiInfo g, InfoImageLine l)
    {
        super(0, 0, 0, 0);
        line = l;

        if(l.imageProvider == null)
        {
            setWidth(64);
            setHeight(64);
        }
        else
        {
            //double w = (displayW > 0D) ? displayW : (displayS == 0D ? imageProvider.getWidth() : (displayS > 0D ? imageProvider.getWidth() * displayS : (imageProvider.getWidth() / -displayS)));
            //double h = (displayH > 0D) ? displayH : (displayS == 0D ? imageProvider.getHeight() : (displayS > 0D ? imageProvider.getHeight() * displayS : (imageProvider.getHeight() / -displayS)));

            double w = Math.min(g.panelText.getWidth(), l.imageWidth * l.imageScale);
            double h = l.imageHeight * (w / (l.imageWidth * l.imageScale));

            //imageProvider = new ScaledImageProvider(img, w, (int) h);

            setWidth((int) w);
            setHeight((int) h + 1);
        }
    }

    @Override
    public void renderWidget(IGui gui)
    {
        if(line.imageProvider != EmptyImageProvider.INSTANCE)
        {
            GlStateManager.color(1F, 1F, 1F, 1F);
            FTBLibClient.setTexture(line.imageProvider.getImage());
            GuiHelper.drawTexturedRect(getAX(), getAY(), getWidth(), getHeight(), 0D, 0D, 1D, 1D);
        }
    }

    @Override
    public void addMouseOverText(IGui gui, List<String> l)
    {
        if(line.hover != null)
        {
            l.addAll(line.hover);
        }
    }

    @Override
    public void onClicked(IGui gui, IMouseButton button)
    {
        if(line.clickEvent != null)
        {
            GuiHelper.playClickSound();
            ButtonInfoExtendedTextLine.onClickEvent(line.clickEvent);
        }
    }
}
