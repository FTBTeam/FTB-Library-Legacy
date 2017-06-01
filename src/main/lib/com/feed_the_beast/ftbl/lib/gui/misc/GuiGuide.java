package com.feed_the_beast.ftbl.lib.gui.misc;

import com.feed_the_beast.ftbl.api.gui.IClientActionGui;
import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.api.guide.IGuideTextLine;
import com.feed_the_beast.ftbl.api.guide.SpecialGuideButton;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.client.ColoredObject;
import com.feed_the_beast.ftbl.lib.client.ImageProvider;
import com.feed_the_beast.ftbl.lib.gui.Button;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.GuiLang;
import com.feed_the_beast.ftbl.lib.gui.Panel;
import com.feed_the_beast.ftbl.lib.gui.PanelScrollBar;
import com.feed_the_beast.ftbl.lib.gui.Widget;
import com.feed_the_beast.ftbl.lib.gui.WidgetLayout;
import com.feed_the_beast.ftbl.lib.guide.ButtonGuidePage;
import com.feed_the_beast.ftbl.lib.guide.GuidePage;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import net.minecraft.client.renderer.GlStateManager;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class GuiGuide extends GuiBase implements IClientActionGui
{
    private static final IDrawableObject TEXTURE = ImageProvider.get(FTBLibFinals.MOD_ID + ":textures/gui/info.png");

    private static final IDrawableObject TEX_SLIDER = TEXTURE.withUVfromCoords(0, 30, 12, 18, 64, 64);
    private static final IDrawableObject TEX_BACK = TEXTURE.withUVfromCoords(13, 30, 14, 11, 64, 64);
    private static final IDrawableObject TEX_CLOSE = TEXTURE.withUVfromCoords(13, 41, 14, 11, 64, 64);
    public static final IDrawableObject TEX_BULLET = TEXTURE.withUVfromCoords(0, 49, 6, 6, 64, 64);

    private static final IDrawableObject TEX_BG_MU = TEXTURE.withUVfromCoords(14, 0, 1, 13, 64, 64);
    private static final IDrawableObject TEX_BG_MD = TEXTURE.withUVfromCoords(14, 16, 1, 13, 64, 64);
    private static final IDrawableObject TEX_BG_ML = TEXTURE.withUVfromCoords(0, 14, 13, 1, 64, 64);
    private static final IDrawableObject TEX_BG_MR = TEXTURE.withUVfromCoords(16, 14, 13, 1, 64, 64);

    private static final IDrawableObject TEX_BG_NN = TEXTURE.withUVfromCoords(0, 0, 13, 13, 64, 64);
    private static final IDrawableObject TEX_BG_PN = TEXTURE.withUVfromCoords(16, 0, 13, 13, 64, 64);
    private static final IDrawableObject TEX_BG_NP = TEXTURE.withUVfromCoords(0, 16, 13, 13, 64, 64);
    private static final IDrawableObject TEX_BG_PP = TEXTURE.withUVfromCoords(16, 16, 13, 13, 64, 64);

    public static final IDrawableObject FILLING = (x, y, w, h, col) -> GuiHelper.drawBlankRect(x + 4, y + 4, w - 8, h - 8, col.hasColor() ? col : GuiConfigs.INFO_BACKGROUND.getColor());
    public static final IDrawableObject BORDERS = (x, y, w, h, col) ->
    {
        Color4I c = col.hasColor() ? col : Color4I.WHITE;
        TEX_BG_MU.draw(x + 13, y, w - 24, 13, c);
        TEX_BG_MR.draw(x + w - 13, y + 13, 13, h - 25, c);
        TEX_BG_MD.draw(x + 13, y + h - 13, w - 24, 13, c);
        TEX_BG_ML.draw(x, y + 13, 13, h - 25, c);

        TEX_BG_NN.draw(x, y, 13, 13, c);
        TEX_BG_NP.draw(x, y + h - 13, 13, 13, c);
        TEX_BG_PN.draw(x + w - 13, y, 13, 13, c);
        TEX_BG_PP.draw(x + w - 13, y + h - 13, 13, 13, c);
    };

    private static class ButtonSpecial extends Button
    {
        private final SpecialGuideButton specialInfoButton;

        public ButtonSpecial(SpecialGuideButton b)
        {
            super(0, 0, 16, 16);
            specialInfoButton = b;
            setTitle(specialInfoButton.title.getFormattedText());
        }

        @Override
        public void onClicked(GuiBase gui, IMouseButton button)
        {
            if(GuiHelper.onClickEvent(specialInfoButton.clickEvent))
            {
                GuiHelper.playClickSound();
            }
        }

        @Override
        public void renderWidget(GuiBase gui)
        {
            specialInfoButton.icon.draw(getAX(), getAY(), width, height, Color4I.NONE);
        }
    }

    public final GuidePage pageTree;
    public final Panel panelPages, panelText, panelTitle;
    public final PanelScrollBar sliderPages, sliderText;
    private final Button buttonBack;
    public int panelWidth;
    private final List<ButtonSpecial> specialButtons;

    private GuidePage selectedPage;

    public GuiGuide(GuidePage tree)
    {
        super(0, 0);
        selectedPage = pageTree = tree;

        buttonBack = new Button(12, 12, 14, 11)
        {
            @Override
            public void onClicked(GuiBase gui, IMouseButton button)
            {
                GuiHelper.playClickSound();
                sliderPages.setValue(gui, 0D);
                sliderText.setValue(gui, 0D);
                setSelectedPage(selectedPage.parent);
            }

            @Override
            public String getTitle(GuiBase gui)
            {
                return (selectedPage.parent == null) ? GuiLang.BUTTON_CLOSE.translate() : GuiLang.BUTTON_BACK.translate();
            }
        };

        buttonBack.setIcon(new ColoredObject(TEX_CLOSE, GuiConfigs.INFO_TEXT.getColor()));

        panelPages = new Panel(0, 0, 0, 0)
        {
            @Override
            public void addWidgets()
            {
                for(GuidePage c : selectedPage.childPages.values())
                {
                    add(c.createWidget(GuiGuide.this));
                }

                panelTitle.refreshWidgets();
            }

            @Override
            public void updateWidgetPositions()
            {
                if(!widgets.isEmpty())
                {
                    sliderPages.setElementSize(align(WidgetLayout.VERTICAL));
                }
            }
        };

        panelPages.addFlags(Panel.FLAG_DEFAULTS);

        panelText = new Panel(0, 0, 0, 0)
        {
            private final WidgetLayout LAYOUT = new WidgetLayout.Vertical(2, 0, 4);

            @Override
            public void addWidgets()
            {
                for(Widget w : panelPages.widgets)
                {
                    if(w instanceof ButtonGuidePage)
                    {
                        ((ButtonGuidePage) w).updateTitle(GuiGuide.this);
                    }
                }

                boolean uni = getFont().getUnicodeFlag();
                getFont().setUnicodeFlag(true);

                for(IGuideTextLine line : selectedPage.text)
                {
                    add(line == null ? new Widget(0, 0, panelText.width, getFont().FONT_HEIGHT + 1) : line.createWidget(GuiGuide.this, panelText));
                }

                getFont().setUnicodeFlag(uni);
            }

            @Override
            public void updateWidgetPositions()
            {
                if(!widgets.isEmpty())
                {
                    int s = align(LAYOUT);
                    sliderText.setElementSize(s);
                    sliderText.setSrollStepFromOneElementSize((s - 6) / widgets.size());
                }
            }
        };

        panelText.addFlags(Panel.FLAG_DEFAULTS | Panel.FLAG_UNICODE_FONT);

        panelTitle = new Panel(0, 0, 0, 0)
        {
            @Override
            public void addWidgets()
            {
                add(buttonBack);
                buttonBack.setIcon(new ColoredObject((selectedPage.parent == null) ? TEX_CLOSE : TEX_BACK, getContentColor()));

                specialButtons.clear();

                for(SpecialGuideButton button : selectedPage.specialButtons)
                {
                    specialButtons.add(new ButtonSpecial(button));
                }

                addAll(specialButtons);
            }
        };

        sliderPages = new PanelScrollBar(0, 0, 12, 0, 18, panelPages);
        sliderPages.slider = TEX_SLIDER;
        sliderPages.background = ImageProvider.NULL;

        sliderText = new PanelScrollBar(0, 0, 12, 0, 18, panelText);
        sliderText.slider = TEX_SLIDER;
        sliderText.background = ImageProvider.NULL;

        specialButtons = new ArrayList<>();
    }

    public GuidePage getSelectedPage()
    {
        return selectedPage;
    }

    public void setSelectedPage(@Nullable GuidePage p)
    {
        sliderText.setValue(this, 0D);

        if(selectedPage != p)
        {
            if(p == null)
            {
                mc.player.closeScreen();
                return;
            }
            else
            {
                selectedPage = p;

                if(p.childPages.isEmpty())
                {
                    panelText.refreshWidgets();
                }
                else
                {
                    sliderPages.setValue(this, 0);
                    refreshWidgets();
                }
            }
        }

        panelTitle.refreshWidgets();
    }

    @Override
    public void addWidgets()
    {
        selectedPage.refreshGui(this);

        add(sliderText);
        add(panelPages);
        add(panelText);
        add(panelTitle);
        add(sliderPages);

        panelPages.setWidth(panelWidth - (sliderPages.isEnabled(this) ? 32 : 17));

        for(int i = 0; i < specialButtons.size(); i++)
        {
            ButtonSpecial b = specialButtons.get(i);
            b.posX = panelWidth - 24 - 20 * i;
            b.posY = 10;
        }
    }

    @Override
    public void onInit()
    {
        posX = GuiConfigs.INFO_BORDER_WIDTH.getInt();
        posY = GuiConfigs.INFO_BORDER_HEIGHT.getInt();
        setWidth(getScreen().getScaledWidth() - posX * 2);
        setHeight(getScreen().getScaledHeight() - posY * 2);

        panelWidth = (int) (width * 0.3D);

        panelTitle.width = panelWidth;
        panelTitle.height = 46;

        panelPages.posX = 10;
        panelPages.posY = 43;
        panelPages.setWidth(panelWidth - 17);
        panelPages.setHeight(height - 49);

        panelText.posX = panelWidth + 10;
        panelText.posY = 6;
        panelText.setWidth(width - panelWidth - 23 - sliderText.width);
        panelText.setHeight(height - 12);

        sliderPages.posX = panelWidth - sliderPages.width - 10;
        sliderPages.posY = 46;
        sliderPages.setHeight(height - 56);

        sliderText.posY = 10;
        sliderText.setHeight(height - 20);
        sliderText.posX = width - 10 - sliderText.width;
    }

    @Override
    public void drawBackground()
    {
        TEXTURE.bindTexture();
        FILLING.draw(posX + panelWidth, posY, width - panelWidth, height, Color4I.NONE);
        FILLING.draw(posX, posY + 36, panelWidth, height - 36, Color4I.NONE);
        FILLING.draw(posX, posY, panelWidth, 36, Color4I.NONE);

        GuiHelper.pushScissor(getScreen(), posX, posY, panelWidth, 36);
        drawString(selectedPage.getDisplayName().getFormattedText(), buttonBack.getAX() + buttonBack.width + 5, posY + 14);
        GuiHelper.popScissor();
        GlStateManager.color(1F, 1F, 1F, 1F);
    }

    @Override
    public void drawForeground()
    {
        GlStateManager.color(1F, 1F, 1F, 1F);
        BORDERS.draw(posX + panelWidth, posY, width - panelWidth, height, Color4I.NONE);
        BORDERS.draw(posX, posY + 36, panelWidth, height - 36, Color4I.NONE);
        BORDERS.draw(posX, posY, panelWidth, 36, Color4I.NONE);

        super.drawForeground();
    }

    @Override
    public Color4I getContentColor()
    {
        return GuiConfigs.INFO_TEXT.getColor();
    }

    @Override
    public boolean drawDefaultBackground()
    {
        return false;
    }

    @Override
    public void onClientDataChanged()
    {
        refreshWidgets();
    }

    @Override
    public boolean changePage(String value)
    {
        GuidePage page = pageTree.getSubRaw(value);

        if(page != null)
        {
            if(page.parent != null)
            {
                setSelectedPage(page.parent);
            }

            setSelectedPage(page);
        }

        return false;
    }
}