package com.feed_the_beast.ftbl.gui.info;

import com.feed_the_beast.ftbl.api.GuiLang;
import com.feed_the_beast.ftbl.api.MouseButton;
import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.gui.GuiLM;
import com.feed_the_beast.ftbl.api.gui.IClientActionGui;
import com.feed_the_beast.ftbl.api.gui.widgets.ButtonLM;
import com.feed_the_beast.ftbl.api.gui.widgets.PanelLM;
import com.feed_the_beast.ftbl.api.gui.widgets.SliderLM;
import com.feed_the_beast.ftbl.api.gui.widgets.WidgetLM;
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
    public static final ResourceLocation tex = new ResourceLocation("ftbl", "textures/gui/info.png");
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
        super(null, null);
        parentGui = g;
        page = c;
        pageTitle = page.getTitleComponent().getFormattedText();
        selectedPage = page;

        sliderPages = new SliderLM(this, 0, 0, tex_slider.widthI(), 0, tex_slider.heightI())
        {
            @Override
            public boolean canMouseScroll()
            {
                return gui.mouse().x < panelWidth;
            }
        };

        sliderPages.isVertical = true;

        sliderText = new SliderLM(this, 0, 0, tex_slider.widthI(), 0, tex_slider.heightI())
        {
            @Override
            public boolean canMouseScroll()
            {
                return gui.mouse().x > panelWidth;
            }
        };

        sliderText.isVertical = true;

        buttonBack = new ButtonLM(this, 0, 0, tex_back.widthI(), tex_back.heightI())
        {
            @Override
            public void onClicked(MouseButton button)
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
                        FTBLibClient.openGui(parentGui);
                    }
                }
                else
                {
                    selectedPage = page;
                    sliderText.value = 0F;
                    panelText.posY = 10;
                    initLMGui();
                    refreshWidgets();
                }
            }
        };

        panelPages = new PanelLM(this, 0, 0, 0, 0)
        {
            @Override
            public void addWidgets()
            {
                height = 0;

                for(InfoPage c : page.childPages.values())
                {
                    ButtonInfoPage b = c.createButton(GuiInfo.this);

                    if(b != null && b.height > 0)
                    {
                        add(b);
                        height += b.height;
                    }
                }
            }
        };

        panelText = new PanelLM(this, 0, 0, 0, 0)
        {
            @Override
            public void addWidgets()
            {
                for(WidgetLM w : panelPages.widgets)
                {
                    ((ButtonInfoPage) w).updateTitle();
                }

                height = 0;

                boolean uni = fontRendererObj.getUnicodeFlag();
                fontRendererObj.setUnicodeFlag(useUnicodeFont);

                for(InfoTextLine line : selectedPage.text)
                {
                    ButtonInfoTextLine l = line == null ? new ButtonInfoTextLine(GuiInfo.this, null) : line.createWidget(GuiInfo.this);

                    if(l != null && l.height > 0)
                    {
                        height += l.height;
                        add(l);
                    }
                }

                fontRendererObj.setUnicodeFlag(uni);
                sliderText.scrollStep = 30F / (float) height;
            }
        };

        buttonSpecial = page.createSpecialButton(this);
    }

    @Override
    public void addWidgets()
    {
        page.refreshGuiTree(GuiInfo.this);

        mainPanel.add(sliderPages);
        mainPanel.add(sliderText);
        mainPanel.add(buttonBack);
        mainPanel.add(panelPages);
        mainPanel.add(panelText);
        mainPanel.add(buttonSpecial);

        buttonBack.title = (parentGui == null) ? GuiLang.button_close.translate() : GuiLang.button_back.translate();
    }

    @Override
    public void initLMGui()
    {
        mainPanel.posX = InfoClientSettings.border_width.getAsInt();
        mainPanel.posY = InfoClientSettings.border_height.getAsInt();
        mainPanel.width = width - InfoClientSettings.border_width.getAsInt() * 2;
        mainPanel.height = height - InfoClientSettings.border_height.getAsInt() * 2;
        panelWidth = (int) (mainPanel.width * 2D / 7D);

        panelPages.posX = 10;
        panelPages.posY = 46;
        panelPages.width = panelWidth - 20;
        panelPages.height = mainPanel.height - 56;

        panelText.posX = panelWidth + 10;
        panelText.posY = 10;
        panelText.width = mainPanel.width - panelWidth - 20 - sliderText.width;
        panelText.height = mainPanel.height - 20;

        sliderPages.posX = panelWidth - sliderPages.width - 10;
        sliderPages.posY = 46;
        sliderPages.height = mainPanel.height - 56;

        sliderText.posY = 10;
        sliderText.height = mainPanel.height - 20;
        sliderText.posX = mainPanel.width - 10 - sliderText.width;

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
    public void drawTexturedModalRectD(double x, double y, double u, double v, double w, double h)
    {
        drawTexturedModalRectD(x, y, zLevel, u, v, w, h, 64, 64);
    }

    @Override
    public void drawBackground()
    {
        sliderPages.update();

        if(sliderPages.value == 0F || panelPages.height - (mainPanel.height - 56F) <= 0F)
        {
            panelPages.posY = 46;
            sliderPages.value = 0F;
        }
        else
        {
            panelPages.posY = (int) (46F - (sliderPages.value * (panelPages.height - (mainPanel.height - 56F))));
        }

        sliderText.update();

        if(sliderText.value == 0F || panelText.height - (mainPanel.height - 20F) <= 0F)
        {
            panelText.posY = 10;
            sliderText.value = 0F;
        }
        else
        {
            panelText.posY = (int) (10F - (sliderText.value * (panelText.height - (mainPanel.height - 20F))));
        }

        //GL11.glEnable(GL11.GL_SCISSOR_TEST);
        //scissor(20, 20, mainPanel.width - 40, mainPanel.height - 40);

        super.drawBackground();

        FTBLibClient.setTexture(texture);

        GlStateManager.color(1F, 1F, 1F, 1F);

        renderFilling(panelWidth, 0, mainPanel.width - panelWidth, mainPanel.height, InfoClientSettings.transparency.getAsInt());
        renderFilling(0, 36, panelWidth, mainPanel.height - 36, 255);

        boolean uni = fontRendererObj.getUnicodeFlag();
        fontRendererObj.setUnicodeFlag(useUnicodeFont);

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        scissor(panelText.getAX(), mainPanel.posY + 4, panelText.width, mainPanel.height - 8);
        panelText.renderWidget();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        fontRendererObj.setUnicodeFlag(uni);

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        scissor(panelPages.getAX(), mainPanel.posY + 40, panelPages.width, mainPanel.height - 44);
        panelPages.renderWidget();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        GlStateManager.color(1F, 1F, 1F, 1F);

        renderBorders(panelWidth, 0, mainPanel.width - panelWidth, mainPanel.height);
        renderBorders(0, 36, panelWidth, mainPanel.height - 36);
        renderFilling(0, 0, panelWidth, 36, 255);
        renderBorders(0, 0, panelWidth, 36);

        sliderPages.renderSlider(tex_slider);
        sliderText.renderSlider(tex_slider);
        FTBLibClient.setGLColor(colorText, 255);
        buttonBack.render((parentGui == null) ? tex_close : tex_back);

        GlStateManager.color(1F, 1F, 1F, 1F);
        if(buttonSpecial != null)
        {
            buttonSpecial.renderWidget();
        }

        fontRendererObj.drawString(pageTitle, buttonBack.getAX() + buttonBack.width + 5, mainPanel.posY + 14, colorText);
    }

    @Override
    public void drawDefaultBackground()
    {
    }

    private void renderBorders(int px, int py, int w, int h)
    {
        GlStateManager.color(1F, 1F, 1F, 1F);
        px += mainPanel.posX;
        py += mainPanel.posY;

        render(tex_bg_NN, px, py, zLevel, 13, 13);
        render(tex_bg_NP, px, py + h - 13, zLevel, 13, 13);
        render(tex_bg_PN, px + w - 13, py, zLevel, 13, 13);
        render(tex_bg_PP, px + w - 13, py + h - 13, zLevel, 13, 13);

        render(tex_bg_MU, px + 13, py, zLevel, w - 24, 13);
        render(tex_bg_MR, px + w - 13, py + 13, zLevel, 13, h - 25);
        render(tex_bg_MD, px + 13, py + h - 13, zLevel, w - 24, 13);
        render(tex_bg_ML, px, py + 13, zLevel, 13, h - 25);
    }

    private void renderFilling(int px, int py, int w, int h, int a)
    {
        FTBLibClient.setGLColor(colorBackground, a);
        drawBlankRect(mainPanel.posX + px + 4, mainPanel.posY + py + 4, zLevel, w - 8, h - 8);
    }

    @Override
    public void onClientDataChanged()
    {
        refreshWidgets();
    }
}