package com.feed_the_beast.ftbl.gui;

import com.feed_the_beast.ftbl.FTBLibFinals;
import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.gui.GuiLM;
import com.feed_the_beast.ftbl.api.gui.GuiLang;
import com.feed_the_beast.ftbl.api.gui.IClientActionGui;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.api.gui.widgets.ButtonLM;
import com.feed_the_beast.ftbl.api.gui.widgets.PanelLM;
import com.feed_the_beast.ftbl.api.gui.widgets.SliderLM;
import com.feed_the_beast.ftbl.api.gui.widgets.WidgetLM;
import com.feed_the_beast.ftbl.api.info.IGuiInfoPage;
import com.feed_the_beast.ftbl.api.info.IGuiInfoPageTree;
import com.feed_the_beast.ftbl.api.info.IInfoPageTheme;
import com.feed_the_beast.ftbl.api.info.IInfoTextLine;
import com.feed_the_beast.ftbl.api.info.impl.ButtonInfoPage;
import com.feed_the_beast.ftbl.api.info.impl.ButtonInfoTextLine;
import com.feed_the_beast.ftbl.api.info.impl.GuiInfoPageTree;
import com.latmod.lib.TextureCoords;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@SideOnly(Side.CLIENT)
@ParametersAreNonnullByDefault
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

    public final IGuiInfoPageTree pageTree;
    public final SliderLM sliderPages, sliderText;
    public final PanelLM panelPages, panelText;
    private final ButtonLM buttonBack, buttonSpecial;
    public int panelWidth;
    public int colorText, colorBackground;
    public boolean useUnicodeFont;
    private IGuiInfoPageTree selectedPage;

    public GuiInfo(String id, IGuiInfoPage c)
    {
        super(0, 0);
        pageTree = new GuiInfoPageTree(id, null, c);
        selectedPage = pageTree;

        sliderPages = new SliderLM(0, 0, 12, 0, 18)
        {
            @Override
            public boolean canMouseScroll(GuiLM gui)
            {
                return mouseX < panelWidth;
            }
        };

        sliderPages.isVertical = true;

        sliderText = new SliderLM(0, 0, 12, 0, 18)
        {
            @Override
            public boolean canMouseScroll(GuiLM gui)
            {
                return mouseX > panelWidth;
            }
        };

        sliderText.isVertical = true;

        buttonBack = new ButtonLM(0, 0, 14, 11, selectedPage.getParent() == null ? GuiLang.button_close.translate() : GuiLang.button_back.translate())
        {
            @Override
            public void onClicked(@Nonnull GuiLM gui, @Nonnull IMouseButton button)
            {
                GuiLM.playClickSound();
                setSelectedPage(selectedPage.getParent());
            }
        };

        panelPages = new PanelLM(0, 0, 0, 0)
        {
            @Override
            public void addWidgets()
            {
                height = 0;

                for(IGuiInfoPageTree c : selectedPage.getPages())
                {
                    ButtonInfoPage b = c.getPage().createButton(GuiInfo.this, c);

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

                for(IInfoTextLine line : selectedPage.getPage().getText())
                {
                    WidgetLM w = line == null ? new ButtonInfoTextLine(GuiInfo.this, null) : line.createWidget(GuiInfo.this, selectedPage);
                    add(w);
                    height += w.height;
                }

                font.setUnicodeFlag(uni);
                sliderText.scrollStep = 30F / (float) height;
            }
        };

        buttonSpecial = selectedPage.getPage().createSpecialButton(this, selectedPage);
    }

    public IGuiInfoPageTree getSelectedPage()
    {
        return selectedPage;
    }

    public void setSelectedPage(@Nullable IGuiInfoPageTree p)
    {
        sliderText.value = 0F;
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
        selectedPage.getPage().refreshGui(this);

        add(sliderPages);
        add(sliderText);
        add(buttonBack);
        add(panelPages);
        add(panelText);
        add(buttonSpecial);

        buttonBack.title = (selectedPage.getParent() == null) ? GuiLang.button_close.translate() : GuiLang.button_back.translate();
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

        IInfoPageTheme theme = selectedPage.getPage().getTheme();

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
        sliderPages.update(this);

        if(sliderPages.value == 0F || panelPages.height - (height - 56F) <= 0F)
        {
            panelPages.posY = 46;
            sliderPages.value = 0F;
        }
        else
        {
            panelPages.posY = (int) (46F - (sliderPages.value * (panelPages.height - (height - 56F))));
        }

        sliderText.update(this);

        if(sliderText.value == 0F || panelText.height - (height - 20F) <= 0F)
        {
            setSelectedPage(selectedPage);
        }
        else
        {
            panelText.posY = (int) (10F - (sliderText.value * (panelText.height - (height - 20F))));
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

        font.drawString(selectedPage.getFormattedTitle(), buttonBack.getAX() + buttonBack.width + 5, posY + 14, colorText);
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

        render(TEX_BG_NN, px, py, 13, 13);
        render(TEX_BG_NP, px, py + h - 13, 13, 13);
        render(TEX_BG_PN, px + w - 13, py, 13, 13);
        render(TEX_BG_PP, px + w - 13, py + h - 13, 13, 13);

        render(TEX_BG_MU, px + 13, py, w - 24, 13);
        render(TEX_BG_MR, px + w - 13, py + 13, 13, h - 25);
        render(TEX_BG_MD, px + 13, py + h - 13, w - 24, 13);
        render(TEX_BG_ML, px, py + 13, 13, h - 25);
    }

    private void renderFilling(double px, double py, double w, double h)
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