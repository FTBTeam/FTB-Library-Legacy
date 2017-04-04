package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.api.gui.IClientActionGui;
import com.feed_the_beast.ftbl.api.gui.IGuiWrapper;
import com.feed_the_beast.ftbl.lib.client.TexturelessRectangle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LatvianModder on 09.06.2016.
 */
public abstract class GuiBase extends Panel implements IClientActionGui
{
    private static final List<String> TEMP_TEXT_LIST = new ArrayList<>();
    public static final TexturelessRectangle DEFAULT_BACKGROUND = new TexturelessRectangle(0xC8333333).setLineColor(0xFFC0C0C0).setRoundEdges(true);

    public final Minecraft mc;
    private final FontRenderer font;
    private int mouseX, mouseY, mouseWheel;
    private float partialTicks;
    private boolean refreshWidgets;
    private ScaledResolution screen;
    public boolean fixUnicode;

    public GuiBase(int w, int h)
    {
        super(0, 0, w, h);
        mc = Minecraft.getMinecraft();
        font = createFont();
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
        screen = new ScaledResolution(mc);

        if(isFullscreen())
        {
            posX = 0;
            posY = 0;
            setWidth(screen.getScaledWidth());
            setHeight(screen.getScaledHeight());
        }

        onInit();

        if(!isFullscreen())
        {
            posX = (screen.getScaledWidth() - getWidth()) / 2;
            posY = (screen.getScaledHeight() - getHeight()) / 2;
        }

        refreshWidgets();
        updateWidgetPositions();
        fixUnicode = screen.getScaleFactor() % 2 == 1;
    }

    public void onInit()
    {
    }

    public final void closeGui()
    {
        onClosed();

        if(mc.player == null)
        {
            mc.displayGuiScreen(null);
        }
        else
        {
            mc.player.closeScreen();
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

    protected FontRenderer createFont()
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
        return isFullscreen() ? screen.getScaledWidth() : super.getWidth();
    }

    @Override
    public int getHeight()
    {
        return isFullscreen() ? screen.getScaledHeight() : super.getHeight();
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
    public final void renderWidget(GuiBase gui)
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
        getIcon(this).draw(this);
    }

    public void drawForeground()
    {
        addMouseOverText(this, TEMP_TEXT_LIST);
        GuiUtils.drawHoveringText(TEMP_TEXT_LIST, mouseX, Math.max(mouseY, 18), screen.getScaledWidth(), screen.getScaledHeight(), 0, font);
        TEMP_TEXT_LIST.clear();
    }

    public GuiScreen getWrapper()
    {
        return new GuiWrapper(this);
    }

    public final void openGui()
    {
        mc.displayGuiScreen(getWrapper());
    }

    public final FontRenderer getFont()
    {
        return font;
    }

    public final ScaledResolution getScreen()
    {
        return screen;
    }

    public final int getMouseX()
    {
        return mouseX;
    }

    public final int getMouseY()
    {
        return mouseY;
    }

    public final int getMouseWheel()
    {
        return mouseWheel;
    }

    public final float getPartialTicks()
    {
        return partialTicks;
    }

    public final boolean isMouseButtonDown(int button)
    {
        return Mouse.isButtonDown(button);
    }

    public final boolean isKeyDown(int key)
    {
        return Keyboard.isKeyDown(key);
    }

    public void playSoundFX(SoundEvent e, float pitch)
    {
        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(e, pitch));
    }

    public boolean isMouseOver(int x, int y, int w, int h)
    {
        return getMouseX() >= x && getMouseY() >= y && getMouseX() < x + w && getMouseY() < y + h;
    }

    public boolean isMouseOver(Widget w)
    {
        return isMouseOver(w.getAX(), w.getAY(), w.getWidth(), w.getHeight());
    }

    public int getTextColor()
    {
        return 0xFFC0C0C0;
    }

    @SideOnly(Side.CLIENT)
    public void drawString(String text, float x, float y)
    {
        getFont().drawString(text, x, y, getTextColor(), false);
    }

    @Override
    public void onClientDataChanged()
    {
    }

    public boolean isOpen()
    {
        return mc.currentScreen instanceof IGuiWrapper && ((IGuiWrapper) mc.currentScreen).getWrappedGui() == this;
    }
}