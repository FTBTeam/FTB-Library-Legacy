package com.feed_the_beast.ftbl.lib.info;

import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.info.IImageProvider;
import com.feed_the_beast.ftbl.gui.GuiInfo;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import net.minecraft.client.renderer.GlStateManager;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 04.03.2016.
 */
public class ButtonInfoImage extends ButtonInfoExtendedTextLine
{
    public IImageProvider imageProvider;

    public ButtonInfoImage(GuiInfo g, InfoImageLine l, @Nullable IImageProvider img, int imgW, int imgH, double imgS)
    {
        super(g, l);

        if(img == null)
        {
            setWidth(64);
            setHeight(64);
            imageProvider = EmptyImageProvider.INSTANCE;
        }
        else
        {
            imageProvider = img;

            //double w = (displayW > 0D) ? displayW : (displayS == 0D ? imageProvider.getWidth() : (displayS > 0D ? imageProvider.getWidth() * displayS : (imageProvider.getWidth() / -displayS)));
            //double h = (displayH > 0D) ? displayH : (displayS == 0D ? imageProvider.getHeight() : (displayS > 0D ? imageProvider.getHeight() * displayS : (imageProvider.getHeight() / -displayS)));

            int w = Math.min(g.panelText.getWidth(), imgW);
            double h = imgH * (w / (double) imgW);

            //imageProvider = new ScaledImageProvider(img, w, (int) h);

            setWidth(imgW);
            setHeight(imgH + 1);
        }
    }

    @Override
    public void renderWidget(IGui gui)
    {
        if(imageProvider != null)
        {
            GlStateManager.color(1F, 1F, 1F, 1F);
            FTBLibClient.setTexture(imageProvider.getImage());
            GuiHelper.drawTexturedRect(getAX(), getAY(), getWidth(), getHeight(), 0D, 0D, 1D, 1D);
        }
    }
}
