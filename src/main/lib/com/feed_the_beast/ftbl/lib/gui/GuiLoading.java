package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

/**
 * Created by LatvianModder on 27.06.2016.
 */
public class GuiLoading extends GuiLM
{
    public static final ResourceLocation TEXTURE = FTBLibFinals.get("textures/gui/loading.png");
    private boolean startedLoading = false;
    private boolean isLoading = true;

    public GuiLoading()
    {
        super(128, 128);
    }

    public static void renderLoading(int x, int y, int w, int h)
    {
        FTBLibClient.setTexture(TEXTURE);
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + w / 2D, y + h / 2D, 0D);
        GlStateManager.pushMatrix();
        GlStateManager.rotate(((System.currentTimeMillis() % 7200L) * 0.3F) % 360F, 0F, 0F, 1F);
        GuiHelper.drawTexturedRect(-w / 2, -h / 2, w, h, 0D, 0D, 1D, 1D);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.rotate(-((System.currentTimeMillis() % 7200L) * 0.13F) % 360F, 0F, 0F, 1F);
        GlStateManager.scale(0.5D, 0.5D, 1D);
        GuiHelper.drawTexturedRect(-w / 2, -h / 2, w, h, 0D, 0D, 1D, 1D);
        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
    }

    @Override
    public void addWidgets()
    {
    }

    @Override
    public void drawBackground()
    {
        if(!startedLoading)
        {
            startLoading();
            startedLoading = true;
        }

        if(isLoading())
        {
            renderLoading(getAX(), getAY(), getWidth(), getHeight());
        }
        else
        {
            closeGui();
            finishLoading();
        }
    }

    public void setFinished()
    {
        isLoading = false;
    }

    public void startLoading()
    {
    }

    public boolean isLoading()
    {
        return isLoading;
    }

    public void finishLoading()
    {
    }
}