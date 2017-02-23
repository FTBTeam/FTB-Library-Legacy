package com.feed_the_beast.ftbl.lib.gui.misc;

import com.feed_the_beast.ftbl.api.gui.IClientActionGui;
import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.api.gui.IWidget;
import com.feed_the_beast.ftbl.api.info.IInfoTextLine;
import com.feed_the_beast.ftbl.api.info.ISpecialInfoButton;
import com.feed_the_beast.ftbl.lib.client.ColoredObject;
import com.feed_the_beast.ftbl.lib.client.TextureCoords;
import com.feed_the_beast.ftbl.lib.gui.ButtonLM;
import com.feed_the_beast.ftbl.lib.gui.EnumDirection;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.GuiLM;
import com.feed_the_beast.ftbl.lib.gui.GuiLang;
import com.feed_the_beast.ftbl.lib.gui.PanelLM;
import com.feed_the_beast.ftbl.lib.gui.SliderLM;
import com.feed_the_beast.ftbl.lib.gui.TextFieldLM;
import com.feed_the_beast.ftbl.lib.info.ButtonInfoPage;
import com.feed_the_beast.ftbl.lib.info.InfoPage;
import com.feed_the_beast.ftbl.lib.info.InfoPageTheme;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import com.feed_the_beast.ftbl.lib.util.LMColorUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class GuiInfo extends GuiLM implements IClientActionGui
{
    private static final ResourceLocation TEXTURE = FTBLibFinals.get("textures/gui/info.png");

    private static final TextureCoords TEX_SLIDER = TextureCoords.fromCoords(TEXTURE, 0, 30, 12, 18, 64, 64);
    private static final TextureCoords TEX_BACK = TextureCoords.fromCoords(TEXTURE, 13, 30, 14, 11, 64, 64);
    private static final TextureCoords TEX_CLOSE = TextureCoords.fromCoords(TEXTURE, 13, 41, 14, 11, 64, 64);
    private static final TextureCoords TEX_BULLET = TextureCoords.fromCoords(TEXTURE, 0, 49, 6, 6, 64, 64);

    private static final TextureCoords TEX_BG_MU = TextureCoords.fromCoords(TEXTURE, 14, 0, 1, 13, 64, 64);
    private static final TextureCoords TEX_BG_MD = TextureCoords.fromCoords(TEXTURE, 14, 16, 1, 13, 64, 64);
    private static final TextureCoords TEX_BG_ML = TextureCoords.fromCoords(TEXTURE, 0, 14, 13, 1, 64, 64);
    private static final TextureCoords TEX_BG_MR = TextureCoords.fromCoords(TEXTURE, 16, 14, 13, 1, 64, 64);

    private static final TextureCoords TEX_BG_NN = TextureCoords.fromCoords(TEXTURE, 0, 0, 13, 13, 64, 64);
    private static final TextureCoords TEX_BG_PN = TextureCoords.fromCoords(TEXTURE, 16, 0, 13, 13, 64, 64);
    private static final TextureCoords TEX_BG_NP = TextureCoords.fromCoords(TEXTURE, 0, 16, 13, 13, 64, 64);
    private static final TextureCoords TEX_BG_PP = TextureCoords.fromCoords(TEXTURE, 16, 16, 13, 13, 64, 64);

    private static class ButtonSpecial extends ButtonLM
    {
        private ISpecialInfoButton specialInfoButton;

        public ButtonSpecial()
        {
            super(0, 0, 16, 16);
        }

        @Override
        public boolean isEnabled(IGui gui)
        {
            return specialInfoButton != null;
        }

        private void updateButton(GuiInfo gui)
        {
            specialInfoButton = gui.selectedPage.createSpecialButton(gui);
            setTitle(isEnabled(gui) ? specialInfoButton.getTitle() : "");
        }

        @Override
        public void onClicked(IGui gui, IMouseButton button)
        {
            if(isEnabled(gui))
            {
                specialInfoButton.onClicked(button);
            }
        }

        @Override
        public void renderWidget(IGui gui)
        {
            if(isEnabled(gui))
            {
                specialInfoButton.render(gui, getAX(), getAY());
            }
        }
    }

    public final InfoPage pageTree;
    public final SliderLM sliderPages, sliderText;
    public final PanelLM panelPages, panelText;
    private final ButtonLM buttonBack;
    private final ButtonSpecial buttonSpecial;
    public int panelWidth;
    private int colorText, colorBackground;
    public boolean useUnicodeFont;
    private InfoPage selectedPage;
    private int pagesHeight, textHeight;

    public GuiInfo(InfoPage tree)
    {
        super(0, 0);
        selectedPage = pageTree = tree;

        sliderPages = new SliderLM(0, 0, 12, 0, 18)
        {
            @Override
            public boolean canMouseScroll(IGui gui)
            {
                return getMouseX() < panelWidth;
            }

            @Override
            public EnumDirection getDirection()
            {
                return EnumDirection.VERTICAL;
            }

            @Override
            public double getScrollStep()
            {
                return 20D / (double) pagesHeight;
            }

            @Override
            public void onMoved(IGui gui)
            {
                panelPages.setScrollY(getValue(gui), pagesHeight);
            }
        };

        sliderPages.slider = TEX_SLIDER;

        sliderText = new SliderLM(0, 0, 12, 0, 18)
        {
            @Override
            public boolean canMouseScroll(IGui gui)
            {
                return getMouseX() > panelWidth;
            }

            @Override
            public EnumDirection getDirection()
            {
                return EnumDirection.VERTICAL;
            }

            @Override
            public double getScrollStep()
            {
                return 30D / (double) textHeight;
            }

            @Override
            public void onMoved(IGui gui)
            {
                panelText.setScrollY(getValue(gui), textHeight);
            }
        };

        sliderText.slider = TEX_SLIDER;

        buttonBack = new ButtonLM(0, 0, 14, 11)
        {
            @Override
            public void onClicked(IGui gui, IMouseButton button)
            {
                GuiHelper.playClickSound();
                setSelectedPage(selectedPage.getParent());
            }

            @Override
            public String getTitle(IGui gui)
            {
                return (selectedPage.getParent() == null) ? GuiLang.BUTTON_CLOSE.translate() : GuiLang.BUTTON_BACK.translate();
            }
        };

        panelPages = new PanelLM(0, 0, 0, 0)
        {
            @Override
            public void addWidgets()
            {
                pagesHeight = 0;

                for(InfoPage c : selectedPage.getPages().values())
                {
                    IWidget b = c.createWidget(GuiInfo.this);

                    if(b.getHeight() > 0)
                    {
                        add(b);
                        b.setY(pagesHeight);
                        pagesHeight += b.getHeight();
                    }
                }

                buttonSpecial.updateButton(GuiInfo.this);
            }
        };

        panelPages.addFlags(PanelLM.FLAG_ONLY_RENDER_WIDGETS_INSIDE | PanelLM.FLAG_ONLY_INTERACT_WITH_WIDGETS_INSIDE);

        panelText = new PanelLM(0, 0, 0, 0)
        {
            @Override
            public void addWidgets()
            {
                for(IWidget w : panelPages.getWidgets())
                {
                    if(w instanceof ButtonInfoPage)
                    {
                        ((ButtonInfoPage) w).updateTitle(GuiInfo.this);
                    }
                }

                boolean uni = getFont().getUnicodeFlag();
                getFont().setUnicodeFlag(useUnicodeFont);

                for(IInfoTextLine line : selectedPage.getText())
                {
                    add(line == null ? new TextFieldLM(0, 0, panelText.getWidth(), -1, getFont(), "") : line.createWidget(GuiInfo.this, panelText));
                }

                updateWidgetPositions();
                getFont().setUnicodeFlag(uni);
            }

            @Override
            public void updateWidgetPositions()
            {
                textHeight = 0;

                for(IWidget w : getWidgets())
                {
                    w.setY(textHeight);
                    textHeight += w.getHeight();
                }
            }
        };

        panelText.addFlags(PanelLM.FLAG_ONLY_RENDER_WIDGETS_INSIDE | PanelLM.FLAG_ONLY_INTERACT_WITH_WIDGETS_INSIDE | PanelLM.FLAG_UNICODE_FONT);

        buttonSpecial = new ButtonSpecial();
    }

    public InfoPage getSelectedPage()
    {
        return selectedPage;
    }

    public void setSelectedPage(@Nullable InfoPage p)
    {
        sliderText.setValue(this, 0D);

        if(selectedPage != p)
        {
            if(p == null)
            {
                mc.thePlayer.closeScreen();
                return;
            }
            else
            {
                selectedPage = p;

                if(p.getPages().isEmpty())
                {
                    panelText.refreshWidgets();
                }
                else
                {
                    refreshWidgets();
                }
            }
        }

        buttonSpecial.updateButton(this);
        buttonBack.setIcon(new ColoredObject((selectedPage.getParent() == null) ? TEX_CLOSE : TEX_BACK, colorText));
    }

    @Override
    public void addWidgets()
    {
        selectedPage.refreshGui(this);

        add(sliderText);
        add(buttonBack);
        add(panelPages);
        add(panelText);
        add(buttonSpecial);
        add(sliderPages);
    }

    @Override
    public void onInit()
    {
        posX = GuiConfigs.BORDER_WIDTH.getInt();
        posY = GuiConfigs.BORDER_HEIGHT.getInt();
        int width = getScreen().getScaledWidth() - posX * 2;
        int height = getScreen().getScaledHeight() - posY * 2;
        setWidth(width);
        setHeight(height);

        panelWidth = (int) (width * 0.3D);

        panelPages.posX = 10;
        panelPages.posY = 46;
        panelPages.setWidth(panelWidth - 20);
        panelPages.setHeight(height - 56);

        panelText.posX = panelWidth + 10;
        panelText.posY = 6;
        panelText.setWidth(width - panelWidth - 23 - sliderText.getWidth());
        panelText.setHeight(height - 12);

        sliderPages.posX = panelWidth - sliderPages.getWidth() - 10;
        sliderPages.posY = 46;
        sliderPages.setHeight(height - 56);

        sliderText.posY = 10;
        sliderText.setHeight(height - 20);
        sliderText.posX = width - 10 - sliderText.getWidth();

        buttonBack.posX = 12;
        buttonBack.posY = 12;

        InfoPageTheme theme = selectedPage.getTheme();

        colorText = 0xFF000000 | theme.getTextColor();
        colorBackground = 0xFF000000 | theme.getBackgroundColor();
        useUnicodeFont = theme.getUseUnicodeFont();

        buttonSpecial.posX = panelWidth - 24;
        buttonSpecial.posY = 10;
    }

    @Override
    public void drawBackground()
    {
        int width = getWidth();
        int height = getHeight();

        mc.getTextureManager().bindTexture(TEXTURE);
        GlStateManager.color(1F, 1F, 1F, 1F);
        renderFilling(panelWidth, 0, width - panelWidth, height);
        renderFilling(0, 36, panelWidth, height - 36);
        renderFilling(0, 0, panelWidth, 36);
        GlStateManager.color(1F, 1F, 1F, 1F);

        int buttonBackAX = buttonBack.getAX();
        String txt = selectedPage.getDisplayName().getFormattedText();
        int txtsize = getFont().getStringWidth(txt);
        int maxtxtsize = panelWidth - (buttonBackAX + buttonBack.getWidth()) + 4;

        if(txtsize > maxtxtsize)
        {
            boolean mouseOver = isMouseOver(buttonBackAX + buttonBack.getWidth() + 5, posY + 13, maxtxtsize, 12);

            if(mouseOver)
            {
                LMColorUtils.GL_COLOR.set(colorBackground, 255);
                GuiHelper.drawBlankRect(buttonBackAX + buttonBack.getWidth() + 2, posY + 12, txtsize + 6, 13);
                GlStateManager.color(1F, 1F, 1F, 1F);
                getFont().drawString(txt, buttonBackAX + buttonBack.getWidth() + 5, posY + 14, colorText);
            }
            else
            {
                GuiHelper.pushScissor(getScreen(), buttonBackAX + buttonBack.getWidth() + 5, posY + 13, maxtxtsize, 12);
                getFont().drawString(txt, buttonBackAX + buttonBack.getWidth() + 5, posY + 14, colorText);
                GuiHelper.popScissor();
            }
        }
        else
        {
            getFont().drawString(txt, buttonBackAX + buttonBack.getWidth() + 5, posY + 14, colorText);
        }
    }

    @Override
    public void drawForeground()
    {
        int width = getWidth();
        int height = getHeight();

        GlStateManager.color(1F, 1F, 1F, 1F);
        renderBorders(panelWidth, 0, width - panelWidth, height);
        renderBorders(0, 36, panelWidth, height - 36);
        renderBorders(0, 0, panelWidth, 36);

        super.drawForeground();
    }

    @Override
    public int getTextColor()
    {
        return colorText;
    }

    @Override
    public boolean drawDefaultBackground()
    {
        return false;
    }

    private void renderBorders(int px, int py, int w, int h)
    {
        GlStateManager.color(1F, 1F, 1F, 1F);
        px += posX;
        py += posY;

        TEX_BG_MU.draw(px + 13, py, w - 24, 13);
        TEX_BG_MR.draw(px + w - 13, py + 13, 13, h - 25);
        TEX_BG_MD.draw(px + 13, py + h - 13, w - 24, 13);
        TEX_BG_ML.draw(px, py + 13, 13, h - 25);

        TEX_BG_NN.draw(px, py, 13, 13);
        TEX_BG_NP.draw(px, py + h - 13, 13, 13);
        TEX_BG_PN.draw(px + w - 13, py, 13, 13);
        TEX_BG_PP.draw(px + w - 13, py + h - 13, 13, 13);
    }

    private void renderFilling(int px, int py, int w, int h)
    {
        LMColorUtils.GL_COLOR.set(colorBackground, 255);
        GuiHelper.drawBlankRect(posX + px + 4, posY + py + 4, w - 8, h - 8);
    }

    @Override
    public void onClientDataChanged()
    {
        refreshWidgets();
    }
}