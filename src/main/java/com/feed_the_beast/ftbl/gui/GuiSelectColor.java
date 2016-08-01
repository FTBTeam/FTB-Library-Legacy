package com.feed_the_beast.ftbl.gui;

import com.feed_the_beast.ftbl.FTBLibFinals;
import com.feed_the_beast.ftbl.api.MouseButton;
import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.client.gui.GuiLM;
import com.feed_the_beast.ftbl.api.client.gui.GuiLang;
import com.feed_the_beast.ftbl.api.client.gui.widgets.ButtonLM;
import com.feed_the_beast.ftbl.api.client.gui.widgets.SliderLM;
import com.feed_the_beast.ftbl.api.client.gui.widgets.WidgetLM;
import com.feed_the_beast.ftbl.util.EnumDyeColorHelper;
import com.feed_the_beast.ftbl.util.TextureCoords;
import com.latmod.lib.LMColor;
import com.latmod.lib.ObjectCallbackHandler;
import com.latmod.lib.math.MathHelperLM;
import com.latmod.lib.util.LMColorUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@SideOnly(Side.CLIENT)
@ParametersAreNonnullByDefault
public class GuiSelectColor extends GuiLM
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(FTBLibFinals.MOD_ID, "textures/gui/colselector.png");
    private static final ResourceLocation TEXTURE_WHEEL = new ResourceLocation(FTBLibFinals.MOD_ID, "textures/gui/colselector_wheel.png");

    private static final int COL_TEX_W = 29;
    private static final int COL_TEX_H = 16;

    private static final TextureCoords col_tex = new TextureCoords(TEXTURE, 145, 10, COL_TEX_W, COL_TEX_H, 256, 256);
    private static final TextureCoords cursor_tex = new TextureCoords(TEXTURE, 145, 36, 8, 8, 256, 256);

    private static final int SLIDER_W = 6, SLIDER_H = 10, SLIDER_BAR_W = 64;
    private static final TextureCoords slider_tex = new TextureCoords(TEXTURE, 145, 26, SLIDER_W, SLIDER_H, 256, 256);
    private static final TextureCoords slider_col_tex = new TextureCoords(TEXTURE, 145, 0, SLIDER_BAR_W, SLIDER_H, 256, 256);

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
            double ax = getAX();
            double ay = getAY();

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

                cursorPosX = MathHelperLM.clamp(cursorPosX, 0D, 1D);
                cursorPosY = MathHelperLM.clamp(cursorPosY, 0D, 1D);

                double h = Math.atan2(cursorPosY - 0.5D, cursorPosX - 0.5D) / MathHelperLM.TWO_PI;

                setColor(new LMColor.HSB((float) h, (float) s, (float) sliderBrightness.value));
            }

            GlStateManager.enableBlend();
            GlStateManager.color(1F, 1F, 1F, 1F);
            FTBLibClient.setTexture(TEXTURE_WHEEL);
            drawTexturedRect(ax, ay, width, height, 0D, 0D, 1D, 1D);

            if(cursorPosX >= 0D && cursorPosY >= 0D)
            {
                GlStateManager.color((float) (1F - sliderRed.value), (float) (1F - sliderGreen.value), (float) (1F - sliderBlue.value), 1F);
                GuiLM.render(cursor_tex, ax + cursorPosX * width - 2, ay + cursorPosY * height - 2, 4, 4);
                GlStateManager.color(1F, 1F, 1F, 1F);
            }
        }

        @Override
        public void mousePressed(GuiLM gui, MouseButton b)
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
    private final SliderLM sliderRed, sliderGreen, sliderBlue;
    private final SliderLM sliderHue, sliderSaturation, sliderBrightness;
    private final ColorSelector colorSelector;

    private GuiSelectColor(ObjectCallbackHandler cb, LMColor col, Object id)
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
            public void onClicked(@Nonnull GuiLM gui, @Nonnull MouseButton button)
            {
                closeGui(false);
            }

            @Override
            public void addMouseOverText(GuiLM gui, List<String> s)
            {
                s.add(GuiLang.button_cancel.translate());
                s.add(initCol.toString());
            }
        };

        colorCurrent = new ButtonLM(109, 71, COL_TEX_W, COL_TEX_H)
        {
            @Override
            public void onClicked(@Nonnull GuiLM gui, @Nonnull MouseButton button)
            {
                closeGui(true);
            }

            @Override
            public void addMouseOverText(GuiLM gui, List<String> s)
            {
                s.add(GuiLang.button_accept.translate());
                s.add(currentColor.toString());
            }
        };

        sliderRed = new SliderLM(6, 6, SLIDER_BAR_W, SLIDER_H, SLIDER_W)
        {
            @Override
            public void onMoved(GuiLM gui)
            {
                setColor(new LMColor.RGB((int) (value * 255F), currentColor.green(), currentColor.blue()));
            }
        };
        sliderRed.displayMax = 255;
        sliderRed.title = EnumDyeColorHelper.get(EnumDyeColor.RED).toString();
        sliderRed.scrollStep = 1F / 255F;

        sliderGreen = new SliderLM(6, 19, SLIDER_BAR_W, SLIDER_H, SLIDER_W)
        {
            @Override
            public void onMoved(GuiLM gui)
            {
                setColor(new LMColor.RGB(currentColor.red(), (int) (value * 255F), currentColor.blue()));
            }
        };

        sliderGreen.displayMax = 255;
        sliderGreen.title = EnumDyeColorHelper.get(EnumDyeColor.GREEN).toString();
        sliderGreen.scrollStep = 1F / 255F;

        sliderBlue = new SliderLM(6, 32, SLIDER_BAR_W, SLIDER_H, SLIDER_W)
        {
            @Override
            public void onMoved(GuiLM gui)
            {
                setColor(new LMColor.RGB(currentColor.red(), currentColor.green(), (int) (value * 255F)));
            }
        };

        sliderBlue.displayMax = 255;
        sliderBlue.title = EnumDyeColorHelper.get(EnumDyeColor.BLUE).toString();
        sliderBlue.scrollStep = 1F / 255F;

        sliderHue = new SliderLM(6, 51, SLIDER_BAR_W, SLIDER_H, SLIDER_W)
        {
            @Override
            public void onMoved(GuiLM gui)
            {
                setColor(new LMColor.HSB((float) value, currentColor.saturation(), currentColor.brightness()));
            }
        };

        sliderHue.displayMax = 255;
        sliderHue.title = "Hue";
        sliderHue.scrollStep = 1F / 255F;

        sliderSaturation = new SliderLM(6, 64, SLIDER_BAR_W, SLIDER_H, SLIDER_W)
        {
            @Override
            public void onMoved(GuiLM gui)
            {
                setColor(new LMColor.HSB(currentColor.hue(), (float) value, currentColor.brightness()));
            }
        };
        sliderSaturation.displayMax = 255;
        sliderSaturation.title = "Saturation";
        sliderSaturation.scrollStep = 1F / 255F;

        sliderBrightness = new SliderLM(6, 77, SLIDER_BAR_W, SLIDER_H, SLIDER_W)
        {
            @Override
            public void onMoved(GuiLM gui)
            {
                setColor(new LMColor.HSB(currentColor.hue(), currentColor.saturation(), (float) value));
            }
        };
        sliderBrightness.displayMax = 255;
        sliderBrightness.title = "Brightness";
        sliderBrightness.scrollStep = 1F / 255F;

        colorSelector = new ColorSelector(75, 5, 64, 64);

        setColor(initCol);
    }

    public static void display(Object id, LMColor col, ObjectCallbackHandler cb)
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

        sliderRed.value = currentColor.red() / 255F;
        sliderGreen.value = currentColor.green() / 255F;
        sliderBlue.value = currentColor.blue() / 255F;

        sliderHue.value = currentColor.hue();
        sliderSaturation.value = currentColor.saturation();
        sliderBrightness.value = currentColor.brightness();

        colorSelector.cursorPosX = (Math.cos(sliderHue.value * MathHelperLM.TWO_PI) * 0.5D) * sliderSaturation.value + 0.5D;
        colorSelector.cursorPosY = (Math.sin(sliderHue.value * MathHelperLM.TWO_PI) * 0.5D) * sliderSaturation.value + 0.5D;
    }

    @Override
    public void renderWidgets()
    {
    }

    @Override
    public void drawBackground()
    {
        sliderRed.update(this);
        sliderGreen.update(this);
        sliderBlue.update(this);

        sliderHue.update(this);
        sliderSaturation.update(this);
        sliderBrightness.update(this);

        FTBLibClient.setTexture(TEXTURE);
        GuiScreen.drawModalRectWithCustomSizedTexture(getAX(), getAY(), 0F, 0F, width, height, 256F, 256F);

        FTBLibClient.setGLColor(initCol.color(), 255);
        colorInit.render(col_tex);
        FTBLibClient.setGLColor(currentColor.color(), 255);
        colorCurrent.render(col_tex);

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

        sliderRed.renderSlider(slider_tex);
        sliderGreen.renderSlider(slider_tex);
        sliderBlue.renderSlider(slider_tex);

        sliderHue.renderSlider(slider_tex);
        sliderSaturation.renderSlider(slider_tex);
        sliderBrightness.renderSlider(slider_tex);
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
        buffer.pos(x, y, 0D).tex(slider_col_tex.minU, slider_col_tex.minV).color(red, green, blue, 255).endVertex();
        buffer.pos(x, y + h, 0D).tex(slider_col_tex.minU, slider_col_tex.maxV).color(red, green, blue, 255).endVertex();
        red = LMColorUtils.getRed(colRight);
        green = LMColorUtils.getGreen(colRight);
        blue = LMColorUtils.getBlue(colRight);
        buffer.pos(x + w, y + h, 0D).tex(slider_col_tex.maxU, slider_col_tex.maxV).color(red, green, blue, 255).endVertex();
        buffer.pos(x + w, y, 0D).tex(slider_col_tex.maxU, slider_col_tex.minV).color(red, green, blue, 255).endVertex();
        tessellator.draw();
    }

    private void closeGui(boolean set)
    {
        GuiLM.playClickSound();
        callback.onCallback(colorID, set ? currentColor : initCol);
    }
}