package com.feed_the_beast.ftbl.gui.info;

import com.feed_the_beast.ftbl.FTBLibFinals;
import com.feed_the_beast.ftbl.api.MouseButton;
import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.client.gui.GuiLM;
import com.feed_the_beast.ftbl.api.client.gui.GuiLang;
import com.feed_the_beast.ftbl.api.client.gui.IClientActionGui;
import com.feed_the_beast.ftbl.api.client.gui.widgets.ButtonLM;
import com.feed_the_beast.ftbl.api.client.gui.widgets.PanelLM;
import com.feed_the_beast.ftbl.api.client.gui.widgets.SliderLM;
import com.feed_the_beast.ftbl.api.client.gui.widgets.WidgetLM;
import com.feed_the_beast.ftbl.api.info.InfoPage;
import com.feed_the_beast.ftbl.api.info.InfoPageTheme;
import com.feed_the_beast.ftbl.api.info.InfoTextLine;
import com.feed_the_beast.ftbl.util.TextureCoords;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiInfo extends GuiLM implements IClientActionGui
{
    public static final ResourceLocation tex = new ResourceLocation(FTBLibFinals.MOD_ID, "textures/gui/info.png");
    public static final TextureCoords tex_slider = new TextureCoords(tex, 0, 30, 12, 18, 64, 64);
    public static final TextureCoords tex_back = new TextureCoords(tex, 13, 30, 14, 11, 64, 64);
    public static final TextureCoords tex_close = new TextureCoords(tex, 13, 41, 14, 11, 64, 64);
    public static final TextureCoords tex_bullet = new TextureCoords(tex, 0, 49, 6, 6, 64, 64);

    public static final TextureCoords tex_bg_MU = new TextureCoords(tex, 14, 0, 1, 13, 64, 64);
    public static final TextureCoords tex_bg_MD = new TextureCoords(tex, 14, 16, 1, 13, 64, 64);
    public static final TextureCoords tex_bg_ML = new TextureCoords(tex, 0, 14, 13, 1, 64, 64);
    public static final TextureCoords tex_bg_MR = new TextureCoords(tex, 16, 14, 13, 1, 64, 64);

    public static final TextureCoords tex_bg_NN = new TextureCoords(tex, 0, 0, 13, 13, 64, 64);
    public static final TextureCoords tex_bg_PN = new TextureCoords(tex, 16, 0, 13, 13, 64, 64);
    public static final TextureCoords tex_bg_NP = new TextureCoords(tex, 0, 16, 13, 13, 64, 64);
    public static final TextureCoords tex_bg_PP = new TextureCoords(tex, 16, 16, 13, 13, 64, 64);

    public final GuiInfo parentGui;
    public final InfoPage page;
    public final String pageTitle;
    public final SliderLM sliderPages, sliderText;
    public final ButtonLM buttonBack, buttonSpecial;
    public final PanelLM panelPages, panelText;
    public InfoPage selectedPage;
    public int panelWidth;
    public int colorText, colorBackground;
    public boolean useUnicodeFont;

    public GuiInfo(GuiInfo g, InfoPage c)
    {
        parentGui = g;
        page = c;
        pageTitle = page.getTitleComponent().getFormattedText();
        selectedPage = page;

        sliderPages = new SliderLM(0, 0, tex_slider.widthI(), 0, tex_slider.heightI())
        {
            @Override
            public boolean canMouseScroll(GuiLM gui)
            {
                return mouseX < panelWidth;
            }
        };

        sliderPages.isVertical = true;

        sliderText = new SliderLM(0, 0, tex_slider.widthI(), 0, tex_slider.heightI())
        {
            @Override
            public boolean canMouseScroll(GuiLM gui)
            {
                return mouseX > panelWidth;
            }
        };

        sliderText.isVertical = true;

        buttonBack = new ButtonLM(0, 0, tex_back.widthI(), tex_back.heightI())
        {
            @Override
            public void onClicked(GuiLM gui, MouseButton button)
            {
                FTBLibClient.playClickSound();

                if(selectedPage == page || page.getUnformattedText().isEmpty())
                {
                    if(parentGui == null)
                    {
                        mc.thePlayer.closeScreen();
                    }
                    else
                    {
                        parentGui.selectedPage = parentGui.page;
                        parentGui.sliderText.value = 0F;
                        parentGui.panelText.posY = 10;
                        parentGui.openGui();
                    }
                }
                else
                {
                    selectedPage = page;
                    sliderText.value = 0F;
                    panelText.posY = 10;
                    onInit();
                    refreshWidgets();
                }
            }
        };

        panelPages = new PanelLM(0, 0, 0, 0)
        {
            @Override
            public void addWidgets()
            {
                heightW = 0;

                for(InfoPage c : page.childPages.values())
                {
                    ButtonInfoPage b = c.createButton(GuiInfo.this);

                    if(b != null && b.heightW > 0)
                    {
                        add(b);
                        heightW += b.heightW;
                    }
                }
            }
        };

        panelText = new PanelLM(0, 0, 0, 0)
        {
            @Override
            public void addWidgets()
            {
                for(WidgetLM w : panelPages.widgets)
                {
                    ((ButtonInfoPage) w).updateTitle(GuiInfo.this);
                }

                heightW = 0;

                boolean uni = font.getUnicodeFlag();
                font.setUnicodeFlag(useUnicodeFont);

                for(InfoTextLine line : selectedPage.text)
                {
                    ButtonInfoTextLine l = line == null ? new ButtonInfoTextLine(GuiInfo.this, null) : line.createWidget(GuiInfo.this);

                    if(l != null && l.heightW > 0)
                    {
                        heightW += l.heightW;
                        add(l);
                    }
                }

                font.setUnicodeFlag(uni);
                sliderText.scrollStep = 30F / (float) heightW;
            }
        };

        buttonSpecial = page.createSpecialButton(this);
    }

    @Override
    public void addWidgets()
    {
        page.refreshGuiTree(GuiInfo.this);

        add(sliderPages);
        add(sliderText);
        add(buttonBack);
        add(panelPages);
        add(panelText);
        add(buttonSpecial);

        buttonBack.title = (parentGui == null) ? GuiLang.button_close.translate() : GuiLang.button_back.translate();
    }

    @Override
    public void onInit()
    {
        posX = InfoClientSettings.border_width.getAsInt();
        posY = InfoClientSettings.border_height.getAsInt();
        widthW = screen.getScaledWidth_double() - InfoClientSettings.border_width.getAsInt() * 2;
        heightW = screen.getScaledHeight_double() - InfoClientSettings.border_height.getAsInt() * 2;
        panelWidth = (int) (widthW * 2D / 7D);

        panelPages.posX = 10;
        panelPages.posY = 46;
        panelPages.widthW = panelWidth - 20;
        panelPages.heightW = heightW - 56;

        panelText.posX = panelWidth + 10;
        panelText.posY = 10;
        panelText.widthW = widthW - panelWidth - 20 - sliderText.widthW;
        panelText.heightW = heightW - 20;

        sliderPages.posX = panelWidth - sliderPages.widthW - 10;
        sliderPages.posY = 46;
        sliderPages.heightW = heightW - 56;

        sliderText.posY = 10;
        sliderText.heightW = heightW - 20;
        sliderText.posX = widthW - 10 - sliderText.widthW;

        buttonBack.posX = 12;
        buttonBack.posY = 12;

        InfoPageTheme theme = page.getTheme();

        colorText = 0xFF000000 | theme.textColor.color();
        colorBackground = 0xFF000000 | theme.backgroundColor.color();

        if(theme.useUnicodeFont == null)
        {
            useUnicodeFont = InfoClientSettings.unicode.getAsBoolean();
        }
        else
        {
            useUnicodeFont = theme.useUnicodeFont;
        }

        if(buttonSpecial != null)
        {
            buttonSpecial.posX = panelWidth - 24;
            buttonSpecial.posY = 10;
        }
    }

    @Override
    public void drawBackground()
    {
        sliderPages.update(this);

        if(sliderPages.value == 0F || panelPages.heightW - (heightW - 56F) <= 0F)
        {
            panelPages.posY = 46;
            sliderPages.value = 0F;
        }
        else
        {
            panelPages.posY = (int) (46F - (sliderPages.value * (panelPages.heightW - (heightW - 56F))));
        }

        sliderText.update(this);

        if(sliderText.value == 0F || panelText.heightW - (heightW - 20F) <= 0F)
        {
            panelText.posY = 10;
            sliderText.value = 0F;
        }
        else
        {
            panelText.posY = (int) (10F - (sliderText.value * (panelText.heightW - (heightW - 20F))));
        }

        super.drawBackground();

        FTBLibClient.setTexture(tex);

        GlStateManager.color(1F, 1F, 1F, 1F);

        renderFilling(panelWidth, 0, widthW - panelWidth, heightW, InfoClientSettings.transparency.getAsInt());
        renderFilling(0, 36, panelWidth, heightW - 36, 255);

        boolean uni = font.getUnicodeFlag();
        font.setUnicodeFlag(useUnicodeFont);

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        scissor(panelText.getAX(), posY + 4, panelText.widthW, heightW - 8);
        panelText.renderWidget(this);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        font.setUnicodeFlag(uni);

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        scissor(panelPages.getAX(), posY + 40, panelPages.widthW, heightW - 44);
        panelPages.renderWidget(this);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        GlStateManager.color(1F, 1F, 1F, 1F);

        renderBorders(panelWidth, 0, widthW - panelWidth, heightW);
        renderBorders(0, 36, panelWidth, heightW - 36);
        renderFilling(0, 0, panelWidth, 36, 255);
        renderBorders(0, 0, panelWidth, 36);

        sliderPages.renderSlider(tex_slider);
        sliderText.renderSlider(tex_slider);
        FTBLibClient.setGLColor(colorText, 255);
        buttonBack.render((parentGui == null) ? tex_close : tex_back);

        GlStateManager.color(1F, 1F, 1F, 1F);
        if(buttonSpecial != null)
        {
            buttonSpecial.renderWidget(this);
        }

        font.drawString(pageTitle, (int) (buttonBack.getAX() + buttonBack.widthW + 5), (int) (posY + 14), colorText);
    }

    @Override
    public boolean drawDefaultBackground()
    {
        return false;
    }

    private void renderBorders(double px, double py, double w, double h)
    {
        GlStateManager.color(1F, 1F, 1F, 1F);
        px += posX;
        py += posY;

        render(tex_bg_NN, px, py, 13, 13);
        render(tex_bg_NP, px, py + h - 13, 13, 13);
        render(tex_bg_PN, px + w - 13, py, 13, 13);
        render(tex_bg_PP, px + w - 13, py + h - 13, 13, 13);

        render(tex_bg_MU, px + 13, py, w - 24, 13);
        render(tex_bg_MR, px + w - 13, py + 13, 13, h - 25);
        render(tex_bg_MD, px + 13, py + h - 13, w - 24, 13);
        render(tex_bg_ML, px, py + 13, 13, h - 25);
    }

    private void renderFilling(double px, double py, double w, double h, int a)
    {
        FTBLibClient.setGLColor(colorBackground, a);
        drawBlankRect(posX + px + 4, posY + py + 4, w - 8, h - 8);
    }

    @Override
    public void onClientDataChanged()
    {
        refreshWidgets();
    }
}