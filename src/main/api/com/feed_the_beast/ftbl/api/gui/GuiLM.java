package com.feed_the_beast.ftbl.api.gui;

import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.gui.widgets.PanelLM;
import com.feed_the_beast.ftbl.api.gui.widgets.WidgetLM;
import com.latmod.lib.ITextureCoords;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LatvianModder on 09.06.2016.
 */
@SideOnly(Side.CLIENT)
public abstract class GuiLM extends PanelLM implements IClientActionGui
{
    private static final List<String> TEMP_TEXT_LIST = new ArrayList<>();

    public final Minecraft mc;
    public final FontRenderer font;
    public ScaledResolution screen;
    public float partialTicks;
    public int mouseX, mouseY, pmouseX, pmouseY, dmouseX, dmouseY;
    public int mouseWheel, dmouseWheel;
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

    public static void drawTexturedRect(int x, int y, int w, int h, double u0, double v0, double u1, double v1)
    {
        if(u0 == 0D && v0 == 0D && u1 == 0D && v1 == 0D)
        {
            Tessellator tessellator = Tessellator.getInstance();
            VertexBuffer buffer = tessellator.getBuffer();
            buffer.begin(7, DefaultVertexFormats.POSITION);
            buffer.pos(x, y + h, 0D).endVertex();
            buffer.pos(x + w, y + h, 0D).endVertex();
            buffer.pos(x + w, y, 0D).endVertex();
            buffer.pos(x, y, 0D).endVertex();
            tessellator.draw();
        }
        else
        {
            Tessellator tessellator = Tessellator.getInstance();
            VertexBuffer buffer = tessellator.getBuffer();
            buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
            buffer.pos(x, y + h, 0D).tex(u0, v1).endVertex();
            buffer.pos(x + w, y + h, 0D).tex(u1, v1).endVertex();
            buffer.pos(x + w, y, 0D).tex(u1, v0).endVertex();
            buffer.pos(x, y, 0D).tex(u0, v0).endVertex();
            tessellator.draw();
        }
    }

    public static void drawPlayerHead(String username, int x, int y, int w, int h)
    {
        FTBLibClient.setTexture(FTBLibClient.getSkinTexture(username));
        drawTexturedRect(x, y, w, h, 0.125D, 0.125D, 0.25D, 0.25D);
        drawTexturedRect(x, y, w, h, 0.625D, 0.125D, 0.75D, 0.25D);
    }

    public static void drawBlankRect(int x, int y, int w, int h)
    {
        GlStateManager.disableTexture2D();
        drawTexturedRect(x, y, w, h, 0D, 0D, 0D, 0D);
        GlStateManager.enableTexture2D();
    }

    public static void render(ITextureCoords tc, int x, int y, int w, int h)
    {
        if(tc.isValid())
        {
            FTBLibClient.setTexture(tc.getTexture());
            drawTexturedRect(x, y, w, h, tc.getMinU(), tc.getMinV(), tc.getMaxU(), tc.getMaxV());
        }
    }

    public static void drawCenteredString(FontRenderer font, String txt, double x, double y, int color)
    {
        font.drawString(txt, (int) (x - font.getStringWidth(txt) / 2D), (int) (y - font.FONT_HEIGHT / 2D), color);
    }

    public static void playClickSound()
    {
        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1F));
    }

    public static void renderGuiItem(RenderItem itemRender, ItemStack stack, double x, double y)
    {
        itemRender.zLevel = 200F;
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 32F);
        FontRenderer font = stack.getItem().getFontRenderer(stack);

        if(font == null)
        {
            font = Minecraft.getMinecraft().fontRendererObj;
        }

        GlStateManager.enableLighting();
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableRescaleNormal();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
        GlStateManager.color(1F, 1F, 1F, 1F);
        itemRender.renderItemAndEffectIntoGUI(stack, 0, 0);
        itemRender.renderItemOverlayIntoGUI(font, stack, 0, 0, null);
        GlStateManager.popMatrix();
        itemRender.zLevel = 0F;
    }

    public final void initGui()
    {
        screen = new ScaledResolution(mc);
        onInit();
        posX = (screen.getScaledWidth() - width) / 2;
        posY = (screen.getScaledHeight() - height) / 2;
        refreshWidgets();
    }

    public void onInit()
    {
    }

    public final void closeGui()
    {
        onClosed();
        mc.thePlayer.closeScreen();
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

    public void setFullscreen()
    {
        width = screen.getScaledWidth();
        height = screen.getScaledHeight();
        posX = posY = 0;
    }

    public final void updateGui(int mx, int my, float pt)
    {
        partialTicks = pt;

        pmouseX = mouseX;
        pmouseY = mouseY;

        mouseX = mx;
        mouseY = my;

        dmouseX = mouseX - pmouseX;
        dmouseY = mouseY - pmouseY;

        dmouseWheel = Mouse.getDWheel();
        mouseWheel += dmouseWheel;

        if(refreshWidgets)
        {
            super.refreshWidgets();
            refreshWidgets = false;
        }
    }

    @Override
    public final void renderWidget(GuiLM gui)
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

    @Override
    public boolean isInside(WidgetLM w)
    {
        int a = w.getAY();

        if(a < -w.height || a > screen.getScaledHeight_double())
        {
            return false;
        }

        a = w.getAX();

        return a >= -w.width && a <= screen.getScaledWidth_double();
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
        GuiUtils.drawHoveringText(TEMP_TEXT_LIST, mouseX, Math.max(mouseY, 18), screen.getScaledWidth(), screen.getScaledHeight(), 0, font);
        TEMP_TEXT_LIST.clear();
    }

    public final boolean isMouseOver(int x, int y, int w, int h)
    {
        return mouseX >= x && mouseY >= y && mouseX < x + w && mouseY < y + h;
    }

    public final boolean isMouseOver(WidgetLM w)
    {
        return isMouseOver(w.getAX(), w.getAY(), w.width, w.height);
    }

    public GuiScreen getWrapper()
    {
        return new GuiWrapper(this);
    }

    public final void openGui()
    {
        mc.displayGuiScreen(getWrapper());
    }

    public final void playSoundFX(SoundEvent e, float pitch)
    {
        mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(e, pitch));
    }

    public final void scissor(int x, int y, int w, int h)
    {
        int scale = screen.getScaleFactor();
        int h1 = screen.getScaledHeight() * scale;
        GL11.glScissor(x * scale, h1 - (y * scale + h * scale), w * scale, h * scale);
    }

    @Override
    public void onClientDataChanged()
    {
    }
}