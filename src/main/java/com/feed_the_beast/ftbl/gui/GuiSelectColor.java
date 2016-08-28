package com.feed_the_beast.ftbl.gui;

import com.feed_the_beast.ftbl.FTBLibFinals;
import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.gui.GuiLM;
import com.feed_the_beast.ftbl.api.gui.GuiLang;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.api.gui.widgets.ButtonLM;
import com.feed_the_beast.ftbl.api.gui.widgets.EnumDirection;
import com.feed_the_beast.ftbl.api.gui.widgets.SliderLM;
import com.feed_the_beast.ftbl.api.gui.widgets.WidgetLM;
import com.feed_the_beast.ftbl.util.EnumDyeColorHelper;
import com.latmod.lib.LMColor;
import com.latmod.lib.ObjectCallbackHandler;
import com.latmod.lib.TextureCoords;
import com.latmod.lib.math.MathHelperLM;
import com.latmod.lib.util.LMColorUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiSelectColor extends GuiLM
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(FTBLibFinals.MOD_ID, "textures/gui/colselector.png");
    private static final ResourceLocation TEXTURE_WHEEL = new ResourceLocation(FTBLibFinals.MOD_ID, "textures/gui/colselector_wheel.png");

    private static final int COL_TEX_W = 29;
    private static final int COL_TEX_H = 16;

    private static final TextureCoords COL_TEX = TextureCoords.fromCoords(TEXTURE, 145, 10, COL_TEX_W, COL_TEX_H, 256, 256);
    private static final TextureCoords CURSOR_TEX = TextureCoords.fromCoords(TEXTURE, 145, 36, 8, 8, 256, 256);

    private static final int SLIDER_W = 6, SLIDER_H = 10, SLIDER_BAR_W = 64;
    private static final TextureCoords SLIDER_TEX = TextureCoords.fromCoords(TEXTURE, 145, 26, SLIDER_W, SLIDER_H, 256, 256);
    private static final TextureCoords SLIDER_COL_TEX = TextureCoords.fromCoords(TEXTURE, 145, 0, SLIDER_BAR_W, SLIDER_H, 256, 256);

    private static class ColorSlider extends SliderLM
    {
        private ColorSlider(int x, int y)
        {
            super(x, y, SLIDER_BAR_W, SLIDER_H, SLIDER_W);
        }

        @Override
        public double getDisplayMin()
        {
            return 0D;
        }

        @Override
        public double getDisplayMax()
        {
            return 255D;
        }

        @Override
        public double getScrollStep()
        {
            return 1D / 255D;
        }

        @Override
        public EnumDirection getDirection()
        {
            return EnumDirection.HORIZONTAL;
        }
    }

    private class ColorSelector extends WidgetLM
    {
        private boolean grabbed = false;
        private double cursorPosX = 0D;
        private double cursorPosY = 0D;

        private ColorSelector(int x, int y, int w, int h)
        {
            super(x, y, w, h);
            cursorPosX = cursorPosY = -1D;
        }

        @Override
        public void renderWidget(GuiLM gui)
        {
            int ax = getAX();
            int ay = getAY();

            if(grabbed && !Mouse.isButtonDown(0))
            {
                grabbed = false;
            }

            if(grabbed)
            {
                cursorPosX = (mouseX - ax) / width;
                cursorPosY = (mouseY - ay) / height;

                double s = MathHelperLM.dist(cursorPosX, cursorPosY, 0D, 0.5D, 0.5D, 0D) * 2D;

                if(s > 1D)
                {
                    cursorPosX = (cursorPosX - 0.5D) / s + 0.5D;
                    cursorPosY = (cursorPosY - 0.5D) / s + 0.5D;
                    s = 1D;
                }

                cursorPosX = MathHelper.clamp_double(cursorPosX, 0D, 1D);
                cursorPosY = MathHelper.clamp_double(cursorPosY, 0D, 1D);

                double h = Math.atan2(cursorPosY - 0.5D, cursorPosX - 0.5D) / MathHelperLM.TWO_PI;

                setColor(new LMColor.HSB((float) h, (float) s, (float) sliderBrightness.getValue(gui)));
            }

            GlStateManager.enableBlend();
            GlStateManager.color(1F, 1F, 1F, 1F);
            FTBLibClient.setTexture(TEXTURE_WHEEL);
            drawTexturedRect(ax, ay, width, height, 0D, 0D, 1D, 1D);

            if(cursorPosX >= 0D && cursorPosY >= 0D)
            {
                GlStateManager.color((float) (1F - sliderRed.getValue(gui)), (float) (1F - sliderGreen.getValue(gui)), (float) (1F - sliderBlue.getValue(gui)), 1F);
                GuiLM.render(CURSOR_TEX, ax + (int) (cursorPosX * width) - 2, ay + (int) (cursorPosY * height) - 2, 4, 4);
                GlStateManager.color(1F, 1F, 1F, 1F);
            }
        }

        @Override
        public void mousePressed(GuiLM gui, IMouseButton b)
        {
            if(b.isLeft() && gui.isMouseOver(this))
            {
                grabbed = true;
            }
        }
    }

    private final ObjectCallbackHandler callback;
    private final LMColor.HSB initCol;
    private final Object colorID;
    private final LMColor currentColor;
    private final ButtonLM colorInit, colorCurrent;
    private final ColorSlider sliderRed, sliderGreen, sliderBlue;
    private final ColorSlider sliderHue, sliderSaturation, sliderBrightness;
    private final ColorSelector colorSelector;

    private GuiSelectColor(ObjectCallbackHandler cb, LMColor col, @Nullable Object id)
    {
        super(143, 93);
        callback = cb;
        initCol = new LMColor.HSB();
        initCol.set(col);
        currentColor = new LMColor.RGB();
        colorID = id;

        colorInit = new ButtonLM(76, 71, COL_TEX_W, COL_TEX_H)
        {
            @Override
            public void onClicked(GuiLM gui, IMouseButton button)
            {
                closeGui(false);
            }

            @Override
            public void addMouseOverText(GuiLM gui, List<String> s)
            {
                s.add(GuiLang.BUTTON_CANCEL.translate());
                s.add(initCol.toString());
            }
        };

        colorCurrent = new ButtonLM(109, 71, COL_TEX_W, COL_TEX_H)
        {
            @Override
            public void onClicked(GuiLM gui, IMouseButton button)
            {
                closeGui(true);
            }

            @Override
            public void addMouseOverText(GuiLM gui, List<String> s)
            {
                s.add(GuiLang.BUTTON_ACCEPT.translate());
                s.add(currentColor.toString());
            }
        };

        sliderRed = new ColorSlider(6, 6)
        {
            @Override
            public void onMoved(GuiLM gui)
            {
                setColor(new LMColor.RGB((int) (getValue(gui) * 255D), currentColor.green(), currentColor.blue()));
            }

            @Override
            public String getTitle(GuiLM gui)
            {
                return EnumDyeColorHelper.get(EnumDyeColor.RED).getLangKey().translate();
            }
        };

        sliderGreen = new ColorSlider(6, 19)
        {
            @Override
            public void onMoved(GuiLM gui)
            {
                setColor(new LMColor.RGB(currentColor.red(), (int) (getValue(gui) * 255D), currentColor.blue()));
            }

            @Override
            public String getTitle(GuiLM gui)
            {
                return EnumDyeColorHelper.get(EnumDyeColor.GREEN).getLangKey().translate();
            }
        };

        sliderBlue = new ColorSlider(6, 32)
        {
            @Override
            public void onMoved(GuiLM gui)
            {
                setColor(new LMColor.RGB(currentColor.red(), currentColor.green(), (int) (getValue(gui) * 255D)));
            }

            @Override
            public String getTitle(GuiLM gui)
            {
                return EnumDyeColorHelper.get(EnumDyeColor.BLUE).getLangKey().translate();
            }
        };

        sliderHue = new ColorSlider(6, 51)
        {
            @Override
            public void onMoved(GuiLM gui)
            {
                setColor(new LMColor.HSB((float) getValue(gui), currentColor.saturation(), currentColor.brightness()));
            }

            @Override
            public String getTitle(GuiLM gui)
            {
                return "Hue"; //TODO: Lang
            }
        };

        sliderSaturation = new ColorSlider(6, 64)
        {
            @Override
            public void onMoved(GuiLM gui)
            {
                setColor(new LMColor.HSB(currentColor.hue(), (float) getValue(gui), currentColor.brightness()));
            }

            @Override
            public String getTitle(GuiLM gui)
            {
                return "Saturation"; //TODO: Lang
            }
        };

        sliderBrightness = new ColorSlider(6, 77)
        {
            @Override
            public void onMoved(GuiLM gui)
            {
                setColor(new LMColor.HSB(currentColor.hue(), currentColor.saturation(), (float) getValue(gui)));
            }

            @Override
            public String getTitle(GuiLM gui)
            {
                return "Brightness"; //TODO: Lang
            }
        };

        colorSelector = new ColorSelector(75, 5, 64, 64);

        setColor(initCol);
    }

    public static void display(@Nullable Object id, LMColor col, ObjectCallbackHandler cb)
    {
        new GuiSelectColor(cb, col, id).openGui();
    }

    @Override
    public void addWidgets()
    {
        add(colorInit);
        add(colorCurrent);

        add(sliderRed);
        add(sliderGreen);
        add(sliderBlue);

        add(sliderHue);
        add(sliderSaturation);
        add(sliderBrightness);

        add(colorSelector);
    }

    public void setColor(LMColor col)
    {
        if((0xFF000000 | currentColor.color()) == (0xFF000000 | col.color()))
        {
            return;
        }
        currentColor.set(col);

        sliderRed.setValue(this, currentColor.red() / 255D);
        sliderGreen.setValue(this, currentColor.green() / 255D);
        sliderBlue.setValue(this, currentColor.blue() / 255D);

        sliderHue.setValue(this, currentColor.hue());
        sliderSaturation.setValue(this, currentColor.saturation());
        sliderBrightness.setValue(this, currentColor.brightness());

        colorSelector.cursorPosX = (Math.cos(sliderHue.getValue(this) * MathHelperLM.TWO_PI) * 0.5D) * sliderSaturation.getValue(this) + 0.5D;
        colorSelector.cursorPosY = (Math.sin(sliderHue.getValue(this) * MathHelperLM.TWO_PI) * 0.5D) * sliderSaturation.getValue(this) + 0.5D;
    }

    @Override
    public void renderWidgets()
    {
    }

    @Override
    public void drawBackground()
    {
        sliderRed.updateSlider(this);
        sliderGreen.updateSlider(this);
        sliderBlue.updateSlider(this);

        sliderHue.updateSlider(this);
        sliderSaturation.updateSlider(this);
        sliderBrightness.updateSlider(this);

        FTBLibClient.setTexture(TEXTURE);
        GuiScreen.drawModalRectWithCustomSizedTexture(getAX(), getAY(), 0F, 0F, width, height, 256F, 256F);

        FTBLibClient.setGLColor(initCol.color(), 255);
        colorInit.render(COL_TEX);
        FTBLibClient.setGLColor(currentColor.color(), 255);
        colorCurrent.render(COL_TEX);

        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);

        LMColor col1 = new LMColor.RGB();
        LMColor col2 = new LMColor.RGB();

        col1.setRGBA(0, currentColor.green(), currentColor.blue(), 255);
        col2.setRGBA(255, currentColor.green(), currentColor.blue(), 255);
        renderSlider(sliderRed, col1.color(), col2.color());

        col1.setRGBA(currentColor.red(), 0, currentColor.blue(), 255);
        col2.setRGBA(currentColor.red(), 255, currentColor.blue(), 255);
        renderSlider(sliderGreen, col1.color(), col2.color());

        col1.setRGBA(currentColor.red(), currentColor.green(), 0, 255);
        col2.setRGBA(currentColor.red(), currentColor.green(), 255, 255);
        renderSlider(sliderBlue, col1.color(), col2.color());

        col1 = new LMColor.HSB();
        col2 = new LMColor.HSB();

        col1.setHSB(currentColor.hue(), currentColor.saturation(), currentColor.brightness());
        col2.setHSB(currentColor.hue(), currentColor.saturation(), currentColor.brightness());
        renderSlider(sliderHue, col1.color(), col2.color());

        col1.setHSB(currentColor.hue(), 0F, currentColor.brightness());
        col2.setHSB(currentColor.hue(), 1F, currentColor.brightness());
        renderSlider(sliderSaturation, col1.color(), col2.color());

        col1.setHSB(currentColor.hue(), currentColor.saturation(), 0F);
        col2.setHSB(currentColor.hue(), currentColor.saturation(), 1F);
        renderSlider(sliderBrightness, col1.color(), col2.color());

        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.shadeModel(GL11.GL_FLAT);

        colorSelector.renderWidget(this);

        sliderRed.renderSlider(SLIDER_TEX);
        sliderGreen.renderSlider(SLIDER_TEX);
        sliderBlue.renderSlider(SLIDER_TEX);

        sliderHue.renderSlider(SLIDER_TEX);
        sliderSaturation.renderSlider(SLIDER_TEX);
        sliderBrightness.renderSlider(SLIDER_TEX);
    }

    private void renderSlider(WidgetLM widget, int colLeft, int colRight)
    {
        double x = widget.getAX();
        double y = widget.getAY();
        double w = widget.width;
        double h = widget.height;
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer buffer = tessellator.getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        int red = LMColorUtils.getRed(colLeft);
        int green = LMColorUtils.getGreen(colLeft);
        int blue = LMColorUtils.getBlue(colLeft);
        buffer.pos(x, y, 0D).tex(SLIDER_COL_TEX.getMinU(), SLIDER_COL_TEX.getMinV()).color(red, green, blue, 255).endVertex();
        buffer.pos(x, y + h, 0D).tex(SLIDER_COL_TEX.getMinU(), SLIDER_COL_TEX.getMaxV()).color(red, green, blue, 255).endVertex();
        red = LMColorUtils.getRed(colRight);
        green = LMColorUtils.getGreen(colRight);
        blue = LMColorUtils.getBlue(colRight);
        buffer.pos(x + w, y + h, 0D).tex(SLIDER_COL_TEX.getMaxU(), SLIDER_COL_TEX.getMaxV()).color(red, green, blue, 255).endVertex();
        buffer.pos(x + w, y, 0D).tex(SLIDER_COL_TEX.getMaxU(), SLIDER_COL_TEX.getMinV()).color(red, green, blue, 255).endVertex();
        tessellator.draw();
    }

    private void closeGui(boolean set)
    {
        GuiLM.playClickSound();
        callback.onCallback(colorID, set ? currentColor : initCol);
    }
}