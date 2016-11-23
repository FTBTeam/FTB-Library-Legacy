package com.feed_the_beast.ftbl.lib.info;

import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IWidget;
import com.feed_the_beast.ftbl.api.info.IPageIconRenderer;
import com.feed_the_beast.ftbl.lib.client.ITextureCoordsProvider;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

/**
 * Created by LatvianModder on 06.10.2016.
 */
public class TexturePageIconRenderer implements IPageIconRenderer
{
    private final ITextureCoordsProvider icon;

    public TexturePageIconRenderer(ITextureCoordsProvider i)
    {
        icon = i;
    }

    public boolean isIconBlurry(IGui gui, IWidget widget)
    {
        return false;
    }

    @Override
    public void renderIcon(IGui gui, IWidget widget, int x, int y)
    {
        Minecraft.getMinecraft().getTextureManager().bindTexture(icon.getTextureCoords().getTexture());

        boolean iconBlur = isIconBlurry(gui, widget);

        if(iconBlur)
        {
            GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        }

        GuiHelper.render(icon.getTextureCoords(), x, y, 16, 16);

        if(iconBlur)
        {
            GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        }
    }
}
