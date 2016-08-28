package com.feed_the_beast.ftbl.gui;

import com.feed_the_beast.ftbl.FTBLibFinals;
import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.gui.GuiLM;
import com.feed_the_beast.ftbl.api.gui.GuiLang;
import com.feed_the_beast.ftbl.api.gui.IClientActionGui;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.api.gui.widgets.ButtonLM;
import com.feed_the_beast.ftbl.api.gui.widgets.EnumDirection;
import com.feed_the_beast.ftbl.api.gui.widgets.PanelLM;
import com.feed_the_beast.ftbl.api.gui.widgets.SliderLM;
import com.feed_the_beast.ftbl.api.gui.widgets.WidgetLM;
import com.feed_the_beast.ftbl.api.info.IGuiInfoPage;
import com.feed_the_beast.ftbl.api.info.IInfoPageTheme;
import com.feed_the_beast.ftbl.api.info.IInfoTextLine;
import com.feed_the_beast.ftbl.api.info.impl.ButtonInfoPage;
import com.feed_the_beast.ftbl.api.info.impl.ButtonInfoTextLine;
import com.latmod.lib.TextureCoords;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

@SideOnly(Side.CLIENT)
public class GuiInfo extends GuiLM implements IClientActionGui
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(FTBLibFinals.MOD_ID, "textures/gui/info.png");

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

    public final IGuiInfoPage pageTree;
    public final SliderLM sliderPages, sliderText;
    public final PanelLM panelPages, panelText;
    private final ButtonLM buttonBack, buttonSpecial;
    public int panelWidth;
    public int colorText, colorBackground;
    public boolean useUnicodeFont;
    private IGuiInfoPage selectedPage;

    public GuiInfo(IGuiInfoPage tree)
    {
        super(0, 0);
        selectedPage = pageTree = tree;

        sliderPages = new SliderLM(0, 0, 12, 0, 18)
        {
            @Override
            public boolean canMouseScroll(GuiLM gui)
            {
                return mouseX < panelWidth;
            }

            @Override
            public EnumDirection getDirection()
            {
                return EnumDirection.VERTICAL;
            }

            @Override
            public double getScrollStep()
            {
                return 20D / (double) panelPages.height;
            }
        };

        sliderText = new SliderLM(0, 0, 12, 0, 18)
        {
            @Override
            public boolean canMouseScroll(GuiLM gui)
            {
                return mouseX > panelWidth;
            }

            @Override
            public EnumDirection getDirection()
            {
                return EnumDirection.VERTICAL;
            }

            @Override
            public double getScrollStep()
            {
                return 30D / (double) panelText.height;
            }
        };

        buttonBack = new ButtonLM(0, 0, 14, 11)
        {
            @Override
            public void onClicked(GuiLM gui, IMouseButton button)
            {
                GuiLM.playClickSound();
                setSelectedPage(selectedPage.getParent());
            }

            @Override
            public String getTitle(GuiLM gui)
            {
                return (selectedPage.getParent() == null) ? GuiLang.BUTTON_CLOSE.translate() : GuiLang.BUTTON_BACK.translate();
            }
        };

        panelPages = new PanelLM(0, 0, 0, 0)
        {
            @Override
            public void addWidgets()
            {
                height = 0;

                for(IGuiInfoPage c : selectedPage.getPages())
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

        panelText = new PanelLM(0, 0, 0, 0)
        {
            @Override
            public void addWidgets()
            {
                for(WidgetLM w : panelPages.widgets)
                {
                    ((ButtonInfoPage) w).updateTitle(GuiInfo.this);
                }

                height = 0;

                boolean uni = font.getUnicodeFlag();
                font.setUnicodeFlag(useUnicodeFont);

                for(IInfoTextLine line : selectedPage.getText())
                {
                    WidgetLM w = line == null ? new ButtonInfoTextLine(GuiInfo.this, null) : line.createWidget(GuiInfo.this, selectedPage);
                    add(w);
                    height += w.height;
                }

                font.setUnicodeFlag(uni);
            }
        };

        buttonSpecial = selectedPage.createSpecialButton(this);
    }

    public IGuiInfoPage getSelectedPage()
    {
        return selectedPage;
    }

    public void setSelectedPage(@Nullable IGuiInfoPage p)
    {
        sliderText.setValue(this, 0D);
        panelText.posY = 10;

        if(selectedPage != p)
        {
            if(p == null)
            {
                mc.thePlayer.closeScreen();
            }
            else
            {
                selectedPage = p;
                refreshWidgets();
            }
        }
    }

    @Override
    public void addWidgets()
    {
        selectedPage.refreshGui(this);

        add(sliderPages);
        add(sliderText);
        add(buttonBack);
        add(panelPages);
        add(panelText);
        add(buttonSpecial);
    }

    @Override
    public void onInit()
    {
        posX = InfoClientSettings.border_width.getAsInt();
        posY = InfoClientSettings.border_height.getAsInt();
        width = screen.getScaledWidth() - InfoClientSettings.border_width.getAsInt() * 2;
        height = screen.getScaledHeight() - InfoClientSettings.border_height.getAsInt() * 2;
        panelWidth = (int) (width * 2D / 7D);

        panelPages.posX = 10;
        panelPages.posY = 46;
        panelPages.width = panelWidth - 20;
        panelPages.height = height - 56;

        panelText.posX = panelWidth + 10;
        panelText.posY = 10;
        panelText.width = width - panelWidth - 20 - sliderText.width;
        panelText.height = height - 20;

        sliderPages.posX = panelWidth - sliderPages.width - 10;
        sliderPages.posY = 46;
        sliderPages.height = height - 56;

        sliderText.posY = 10;
        sliderText.height = height - 20;
        sliderText.posX = width - 10 - sliderText.width;

        buttonBack.posX = 12;
        buttonBack.posY = 12;

        IInfoPageTheme theme = selectedPage.getTheme();

        colorText = 0xFF000000 | theme.getTextColor();
        colorBackground = 0xFF000000 | theme.getBackgroundColor();
        useUnicodeFont = theme.getUseUnicodeFont();

        if(buttonSpecial != null)
        {
            buttonSpecial.posX = panelWidth - 24;
            buttonSpecial.posY = 10;
        }
    }

    @Override
    public void renderWidgets()
    {
    }

    @Override
    public void drawBackground()
    {
        sliderPages.updateSlider(this);

        if(sliderPages.getValue(this) == 0D || panelPages.height - (height - 56F) <= 0F)
        {
            panelPages.posY = 46;
            sliderPages.setValue(this, 0D);
        }
        else
        {
            panelPages.posY = (int) (46F - (sliderPages.getValue(this) * (panelPages.height - (height - 56F))));
        }

        sliderText.updateSlider(this);

        if(sliderText.getValue(this) == 0D || panelText.height - (height - 20F) <= 0F)
        {
            setSelectedPage(selectedPage);
        }
        else
        {
            panelText.posY = (int) (10F - (sliderText.getValue(this) * (panelText.height - (height - 20F))));
        }

        super.drawBackground();

        FTBLibClient.setTexture(TEXTURE);

        GlStateManager.color(1F, 1F, 1F, 1F);

        renderFilling(panelWidth, 0, width - panelWidth, height);
        renderFilling(0, 36, panelWidth, height - 36);

        boolean uni = font.getUnicodeFlag();
        font.setUnicodeFlag(useUnicodeFont);

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        scissor(panelText.getAX(), posY + 4, panelText.width, height - 8);
        panelText.renderWidget(this);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        font.setUnicodeFlag(uni);

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        scissor(panelPages.getAX(), posY + 40, panelPages.width, height - 44);
        panelPages.renderWidget(this);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        GlStateManager.color(1F, 1F, 1F, 1F);

        renderBorders(panelWidth, 0, width - panelWidth, height);
        renderBorders(0, 36, panelWidth, height - 36);
        renderFilling(0, 0, panelWidth, 36);
        renderBorders(0, 0, panelWidth, 36);

        sliderPages.renderSlider(TEX_SLIDER);
        sliderText.renderSlider(TEX_SLIDER);
        FTBLibClient.setGLColor(colorText, 255);
        buttonBack.render((selectedPage.getParent() == null) ? TEX_CLOSE : TEX_BACK);

        GlStateManager.color(1F, 1F, 1F, 1F);
        if(buttonSpecial != null)
        {
            buttonSpecial.renderWidget(this);
        }

        int buttonBackAX = buttonBack.getAX();
        String txt = selectedPage.getDisplayName().getFormattedText();
        int txtsize = font.getStringWidth(txt);
        int maxtxtsize = panelWidth - (buttonBackAX + buttonBack.width) + 4;

        if(txtsize > maxtxtsize)
        {
            boolean mouseOver = isMouseOver(buttonBackAX + buttonBack.width + 5, posY + 13, maxtxtsize, 12);

            if(mouseOver)
            {
                FTBLibClient.setGLColor(colorBackground, 255);
                drawBlankRect(buttonBackAX + buttonBack.width + 2, posY + 12, txtsize + 6, 13);
                GlStateManager.color(1F, 1F, 1F, 1F);
                font.drawString(txt, buttonBackAX + buttonBack.width + 5, posY + 14, colorText);
            }
            else
            {
                GL11.glEnable(GL11.GL_SCISSOR_TEST);
                scissor(buttonBackAX + buttonBack.width + 5, posY + 13, maxtxtsize, 12);
                font.drawString(txt, buttonBackAX + buttonBack.width + 5, posY + 14, colorText);
                GL11.glDisable(GL11.GL_SCISSOR_TEST);
            }
        }
        else
        {
            font.drawString(txt, buttonBackAX + buttonBack.width + 5, posY + 14, colorText);
        }
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

        render(TEX_BG_NN, px, py, 13, 13);
        render(TEX_BG_NP, px, py + h - 13, 13, 13);
        render(TEX_BG_PN, px + w - 13, py, 13, 13);
        render(TEX_BG_PP, px + w - 13, py + h - 13, 13, 13);

        render(TEX_BG_MU, px + 13, py, w - 24, 13);
        render(TEX_BG_MR, px + w - 13, py + 13, 13, h - 25);
        render(TEX_BG_MD, px + 13, py + h - 13, w - 24, 13);
        render(TEX_BG_ML, px, py + 13, 13, h - 25);
    }

    private void renderFilling(int px, int py, int w, int h)
    {
        FTBLibClient.setGLColor(colorBackground, 255);
        drawBlankRect(posX + px + 4, posY + py + 4, w - 8, h - 8);
    }

    @Override
    public void onClientDataChanged()
    {
        refreshWidgets();
    }
}