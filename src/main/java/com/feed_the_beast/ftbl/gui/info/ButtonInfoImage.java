package com.feed_the_beast.ftbl.gui.info;

import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.client.gui.GuiLM;
import com.feed_the_beast.ftbl.api.info.InfoImage;
import com.feed_the_beast.ftbl.api.info.InfoImageLine;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by LatvianModder on 04.03.2016.
 */
@SideOnly(Side.CLIENT)
public class ButtonInfoImage extends ButtonInfoExtendedTextLine
{
    public InfoImage texture;

    public ButtonInfoImage(GuiInfo g, InfoImageLine l)
    {
        super(g, l);

        InfoImage img = l.getDisplayImage();

        double w = Math.min(guiInfo.panelText.width, img.width);
        double h = img.height * (w / img.width);
        img = new InfoImage(texture.texture, w, h);

        width = img.width;
        height = img.height + 1;
    }

    @Override
    public void renderWidget(GuiLM gui)
    {
        if(texture != null)
        {
            GlStateManager.color(1F, 1F, 1F, 1F);
            FTBLibClient.setTexture(texture.texture);
            GuiLM.drawTexturedRect(getAX(), getAY(), texture.width, texture.height, 0D, 0D, 1D, 1D);
        }
    }
}
