package com.feed_the_beast.ftbl.api.info.impl;

import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.client.gui.GuiLM;
import com.feed_the_beast.ftbl.gui.GuiInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 * Created by LatvianModder on 04.03.2016.
 */
@SideOnly(Side.CLIENT)
public class ButtonInfoImage extends ButtonInfoExtendedTextLine
{
    public InfoImage texture;

    public ButtonInfoImage(GuiInfo g, @Nonnull InfoImageLine l, InfoImage img)
    {
        super(g, l);

        if(img != null)
        {
            int w = Math.min(g.panelText.width, img.width);
            double h = img.height * (w / (double) img.width);
            texture = new InfoImage(img.texture, w, (int) h);

            width = texture.width;
            height = texture.height + 1;
        }
        else
        {
            width = 64;
            height = 64;
            texture = new InfoImage(new ResourceLocation("minecraft:missing_texture"), 64, 64);
        }
    }

    @Override
    public void renderWidget(@Nonnull GuiLM gui)
    {
        if(texture != null)
        {
            GlStateManager.color(1F, 1F, 1F, 1F);
            FTBLibClient.setTexture(texture.texture);
            GuiLM.drawTexturedRect(getAX(), getAY(), texture.width, texture.height, 0D, 0D, 1D, 1D);
        }
    }
}
