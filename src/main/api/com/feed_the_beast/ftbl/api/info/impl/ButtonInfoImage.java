package com.feed_the_beast.ftbl.api.info.impl;

import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.gui.GuiLM;
import com.feed_the_beast.ftbl.api.info.IImageProvider;
import com.feed_the_beast.ftbl.gui.GuiInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 04.03.2016.
 */
@SideOnly(Side.CLIENT)
public class ButtonInfoImage extends ButtonInfoExtendedTextLine
{
    public IImageProvider imageProvider;

    public ButtonInfoImage(GuiInfo g, InfoImageLine l, @Nullable IImageProvider img, int imgW, int imgH, double imgS)
    {
        super(g, l);

        if(img == null)
        {
            width = 64;
            height = 64;
            imageProvider = EmptyImageProvider.INSTANCE;
        }
        else
        {
            imageProvider = img;

            //double w = (displayW > 0D) ? displayW : (displayS == 0D ? imageProvider.getWidth() : (displayS > 0D ? imageProvider.getWidth() * displayS : (imageProvider.getWidth() / -displayS)));
            //double h = (displayH > 0D) ? displayH : (displayS == 0D ? imageProvider.getHeight() : (displayS > 0D ? imageProvider.getHeight() * displayS : (imageProvider.getHeight() / -displayS)));

            int w = Math.min(g.panelText.width, imgW);
            double h = imgH * (w / (double) imgW);

            //imageProvider = new ScaledImageProvider(img, w, (int) h);

            width = imgW;
            height = imgH + 1;
        }
    }

    @Override
    public void renderWidget(GuiLM gui)
    {
        if(imageProvider != null)
        {
            GlStateManager.color(1F, 1F, 1F, 1F);
            FTBLibClient.setTexture(imageProvider.getImage());
            GuiLM.drawTexturedRect(getAX(), getAY(), width, height, 0D, 0D, 1D, 1D);
        }
    }
}
