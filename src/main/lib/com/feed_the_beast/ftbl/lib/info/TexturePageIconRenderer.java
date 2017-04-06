package com.feed_the_beast.ftbl.lib.info;

import com.feed_the_beast.ftbl.api.gui.IImageProvider;
import com.feed_the_beast.ftbl.api.info.IPageIconRenderer;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import com.feed_the_beast.ftbl.lib.gui.Widget;

/**
 * Created by LatvianModder on 06.10.2016.
 */
public class TexturePageIconRenderer implements IPageIconRenderer
{
    private final IImageProvider icon;

    public TexturePageIconRenderer(IImageProvider i)
    {
        icon = i;
    }

    /*
    public boolean isIconBlurry(IGui gui, IWidget widget)
    {
        return false;
    }
    */

    @Override
    public void renderIcon(GuiBase gui, Widget widget, int x, int y)
    {
        /*
        icon.bindTexture();

        boolean iconBlur = isIconBlurry(gui, widget);

        if(iconBlur)
        {
            GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        }
        */
        icon.draw(x, y, 16, 16, Color4I.NONE);
        /*
        if(iconBlur)
        {
            GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        }
        */
    }
}
