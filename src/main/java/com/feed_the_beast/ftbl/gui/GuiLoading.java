package com.feed_the_beast.ftbl.gui;

import com.feed_the_beast.ftbl.FTBLibFinals;
import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.client.gui.GuiLM;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by LatvianModder on 27.06.2016.
 */
@SideOnly(Side.CLIENT)
public class GuiLoading extends GuiLM
{
    public static final ResourceLocation TEXTURE = new ResourceLocation(FTBLibFinals.MOD_ID, "textures/gui/loading.png");

    @Override
    public void onInit()
    {
        width = 128;
        height = 128;
    }

    @Override
    public void addWidgets()
    {
    }

    @Override
    public void drawBackground()
    {
        FTBLibClient.setTexture(TEXTURE);
        GlStateManager.pushMatrix();
        GlStateManager.translate(screen.getScaledWidth_double() / 2D, screen.getScaledHeight_double() / 2D, 0D);
        GlStateManager.pushMatrix();
        GlStateManager.rotate(((System.currentTimeMillis() % 7200L) * 0.3F) % 360F, 0F, 0F, 1F);
        drawTexturedRect(-width / 2D, -height / 2D, width, height, 0D, 0D, 1D, 1D);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.rotate(-((System.currentTimeMillis() % 7200L) * 0.13F) % 360F, 0F, 0F, 1F);
        GlStateManager.scale(0.5D, 0.5D, 1D);
        drawTexturedRect(-width / 2D, -height / 2D, width, height, 0D, 0D, 1D, 1D);
        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
    }
}