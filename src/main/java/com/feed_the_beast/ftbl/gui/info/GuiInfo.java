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

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

@SideOnly(Side.CLIENT)
@ParametersAreNonnullByDefault
public class GuiInfo extends GuiLM implements IClientActionGui
{
    private static final ResourceLocation tex = new ResourceLocation(FTBLibFinals.MOD_ID, "textures/gui/info.png");

    private static final TextureCoords tex_slider = new TextureCoords(tex, 0, 30, 12, 18, 64, 64);
    private static final TextureCoords tex_back = new TextureCoords(tex, 13, 30, 14, 11, 64, 64);
    private static final TextureCoords tex_close = new TextureCoords(tex, 13, 41, 14, 11, 64, 64);
    private static final TextureCoords tex_bullet = new TextureCoords(tex, 0, 49, 6, 6, 64, 64);

    private static final TextureCoords tex_bg_MU = new TextureCoords(tex, 14, 0, 1, 13, 64, 64);
    private static final TextureCoords tex_bg_MD = new TextureCoords(tex, 14, 16, 1, 13, 64, 64);
    private static final TextureCoords tex_bg_ML = new TextureCoords(tex, 0, 14, 13, 1, 64, 64);
    private static final TextureCoords tex_bg_MR = new TextureCoords(tex, 16, 14, 13, 1, 64, 64);

    private static final TextureCoords tex_bg_NN = new TextureCoords(tex, 0, 0, 13, 13, 64, 64);
    private static final TextureCoords tex_bg_PN = new TextureCoords(tex, 16, 0, 13, 13, 64, 64);
    private static final TextureCoords tex_bg_NP = new TextureCoords(tex, 0, 16, 13, 13, 64, 64);
    private static final TextureCoords tex_bg_PP = new TextureCoords(tex, 16, 16, 13, 13, 64, 64);
    public final Map.Entry<String, InfoPage> page;
    public final SliderLM sliderPages, sliderText;
    public final PanelLM panelPages, panelText;
    private final GuiInfo parentGui;
    private final String pageTitle;
    private final ButtonLM buttonBack, buttonSpecial;
    public Map.Entry<String, InfoPage> selectedPage;
    public int panelWidth;
    public int colorText, colorBackground;
    public boolean useUnicodeFont;

    public GuiInfo(GuiInfo g, Map.Entry<String, InfoPage> c)
    {
        super(0, 0);
        parentGui = g;
        page = c;
        pageTitle = page.getValue().getTitleComponent(page.getKey()).getFormattedText();
        selectedPage = page;

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

        buttonBack = new ButtonLM(0, 0, 14, 11, parentGui == null ? GuiLang.button_close.translate() : GuiLang.button_back.translate())
        {
            @Override
            public void onClicked(@Nonnull GuiLM gui, @Nonnull MouseButton button)
            {
                GuiLM.playClickSound();

                if(selectedPage == page || page.getValue().getUnformattedText().isEmpty())
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
                height = 0;

                for(InfoPage c : page.getValue().childPages.values())
                {
                    ButtonInfoPage b = c.createButton(GuiInfo.this, GuiInfo.this.page.getKey());

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

                for(InfoTextLine line : selectedPage.getValue().text)
                {
                    ButtonInfoTextLine l = line == null ? new ButtonInfoTextLine(GuiInfo.this, null) : line.createWidget(GuiInfo.this);

                    if(l != null && l.height > 0)
                    {
                        add(l);
                        height += l.height + 1;
                    }
                }

                font.setUnicodeFlag(uni);
                sliderText.scrollStep = 30F / (float) height;
            }
        };

        buttonSpecial = page.getValue().createSpecialButton(this);
    }

    @Override
    public void addWidgets()
    {
        page.getValue().refreshGuiTree(GuiInfo.this);

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

        InfoPageTheme theme = page.getValue().getTheme();

        colorText = 0xFF000000 | theme.textColor;
        colorBackground = 0xFF000000 | theme.backgroundColor;

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

    public GuiInfo getParentGui()
    {
        return parentGui;
    }

    public String getPageTitle()
    {
        return pageTitle;
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
            panelText.posY = 10;
            sliderText.value = 0F;
        }
        else
        {
            panelText.posY = (int) (10F - (sliderText.value * (panelText.height - (height - 20F))));
        }

        super.drawBackground();

        FTBLibClient.setTexture(tex);

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

        sliderPages.renderSlider(tex_slider);
        sliderText.renderSlider(tex_slider);
        FTBLibClient.setGLColor(colorText, 255);
        buttonBack.render((parentGui == null) ? tex_close : tex_back);

        GlStateManager.color(1F, 1F, 1F, 1F);
        if(buttonSpecial != null)
        {
            buttonSpecial.renderWidget(this);
        }

        font.drawString(pageTitle, buttonBack.getAX() + buttonBack.width + 5, posY + 14, colorText);
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