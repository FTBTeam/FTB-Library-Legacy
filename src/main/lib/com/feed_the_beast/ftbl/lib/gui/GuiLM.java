package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.api.gui.IClientActionGui;
import com.feed_the_beast.ftbl.api.gui.IGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.client.config.GuiUtils;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LatvianModder on 09.06.2016.
 */
public abstract class GuiLM extends PanelLM implements IGui, IClientActionGui
{
    private static final List<String> TEMP_TEXT_LIST = new ArrayList<>();

    public final Minecraft mc;
    private final FontRenderer font;
    private int screenW, screenH, screenScaleFactor, mouseX, mouseY, mouseWheel;
    private float partialTicks;
    private boolean refreshWidgets;

    public GuiLM(int w, int h)
    {
        super(0, 0, w, h);
        mc = Minecraft.getMinecraft();
        font = createFont(mc);
    }

    public static void setupDrawing()
    {
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.enableTexture2D();
    }

    public final void initGui()
    {
        ScaledResolution screen = new ScaledResolution(mc);
        screenW = screen.getScaledWidth();
        screenH = screen.getScaledHeight();
        screenScaleFactor = screen.getScaleFactor();

        if(isFullscreen())
        {
            posX = 0;
            posY = 0;
            setWidth(screenW);
            setHeight(screenH);
        }

        onInit();

        if(!isFullscreen())
        {
            posX = (screenW - getWidth()) / 2;
            posY = (screenH - getHeight()) / 2;
        }

        refreshWidgets();
    }

    public void onInit()
    {
    }

    public final void closeGui()
    {
        onClosed();

        if(mc.thePlayer == null)
        {
            mc.displayGuiScreen(null);
        }
        else
        {
            mc.thePlayer.closeScreen();
        }
    }

    public boolean onClosedByKey()
    {
        return true;
    }

    public void onClosed()
    {
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }

    protected FontRenderer createFont(Minecraft mc)
    {
        return mc.fontRendererObj;
    }

    @Override
    public final void refreshWidgets()
    {
        refreshWidgets = true;
    }

    public boolean isFullscreen()
    {
        return false;
    }

    @Override
    public int getWidth()
    {
        return isFullscreen() ? screenW : super.getWidth();
    }

    @Override
    public int getHeight()
    {
        return isFullscreen() ? screenH : super.getHeight();
    }

    public final void updateGui(int mx, int my, float pt)
    {
        partialTicks = pt;
        mouseX = mx;
        mouseY = my;
        mouseWheel = Mouse.getDWheel();

        if(refreshWidgets)
        {
            super.refreshWidgets();
            refreshWidgets = false;
        }
    }

    @Override
    public final void renderWidget(IGui gui)
    {
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        drawBackground();
        renderWidgets();
    }

    public void renderWidgets()
    {
        super.renderWidget(this);
    }

    public boolean drawDefaultBackground()
    {
        return true;
    }

    public void drawBackground()
    {
    }

    public void drawForeground()
    {
        addMouseOverText(this, TEMP_TEXT_LIST);
        GuiUtils.drawHoveringText(TEMP_TEXT_LIST, mouseX, Math.max(mouseY, 18), screenW, screenH, 0, font);
        TEMP_TEXT_LIST.clear();
    }

    @Override
    public GuiScreen getWrapper()
    {
        return new GuiWrapper(this);
    }

    @Override
    public final void openGui()
    {
        mc.displayGuiScreen(getWrapper());
    }

    @Override
    public final FontRenderer getFont()
    {
        return font;
    }

    @Override
    public final int getScreenWidth()
    {
        return screenW;
    }

    @Override
    public final int getScreenHeight()
    {
        return screenH;
    }

    @Override
    public final int getScreenScaleFactor()
    {
        return screenScaleFactor;
    }

    @Override
    public final int getMouseX()
    {
        return mouseX;
    }

    @Override
    public final int getMouseY()
    {
        return mouseY;
    }

    @Override
    public final int getMouseWheel()
    {
        return mouseWheel;
    }

    @Override
    public final float getPartialTicks()
    {
        return partialTicks;
    }

    @Override
    public void onClientDataChanged()
    {
    }
}