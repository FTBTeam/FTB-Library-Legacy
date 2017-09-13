package com.feed_the_beast.ftbl.lib.gui.misc;

import com.feed_the_beast.ftbl.api.guide.IGuideTextLine;
import com.feed_the_beast.ftbl.api.guide.SpecialGuideButton;
import com.feed_the_beast.ftbl.client.FTBLibClientConfig;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.MouseButton;
import com.feed_the_beast.ftbl.lib.client.ClientUtils;
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
import com.feed_the_beast.ftbl.lib.icon.ColoredIcon;
import com.feed_the_beast.ftbl.lib.icon.Icon;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import net.minecraft.client.renderer.GlStateManager;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class GuiGuide extends GuiBase
{
	private static final Icon TEXTURE = Icon.getIcon(FTBLibFinals.MOD_ID + ":textures/gui/guide.png");

	private static final Icon TEX_SLIDER_V = TEXTURE.withUVfromCoords(0, 30, 12, 18, 64, 64);
	private static final Icon TEX_SLIDER_H = TEXTURE.withUVfromCoords(30, 0, 18, 12, 64, 64);
	private static final Icon TEX_BACK = TEXTURE.withUVfromCoords(13, 30, 14, 11, 64, 64);
	private static final Icon TEX_CLOSE = TEXTURE.withUVfromCoords(13, 41, 14, 11, 64, 64);
	public static final Icon TEX_BULLET = TEXTURE.withUVfromCoords(0, 49, 6, 6, 64, 64);

	private static final Icon TEX_BG_MU = TEXTURE.withUVfromCoords(14, 0, 1, 13, 64, 64);
	private static final Icon TEX_BG_MD = TEXTURE.withUVfromCoords(14, 16, 1, 13, 64, 64);
	private static final Icon TEX_BG_ML = TEXTURE.withUVfromCoords(0, 14, 13, 1, 64, 64);
	private static final Icon TEX_BG_MR = TEXTURE.withUVfromCoords(16, 14, 13, 1, 64, 64);

	private static final Icon TEX_BG_NN = TEXTURE.withUVfromCoords(0, 0, 13, 13, 64, 64);
	private static final Icon TEX_BG_PN = TEXTURE.withUVfromCoords(16, 0, 13, 13, 64, 64);
	private static final Icon TEX_BG_NP = TEXTURE.withUVfromCoords(0, 16, 13, 13, 64, 64);
	private static final Icon TEX_BG_PP = TEXTURE.withUVfromCoords(16, 16, 13, 13, 64, 64);

	public static final Icon FILLING = new Icon()
	{
		@Override
		public void draw(int x, int y, int w, int h, Color4I col)
		{
			GuiHelper.drawBlankRect(x + 4, y + 4, w - 8, h - 8, col.hasColor() ? col : FTBLibClientConfig.guide.background.getColor());
		}
	};

	public static final Icon BORDERS = new Icon()
	{
		@Override
		public void draw(int x, int y, int w, int h, Color4I col)
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
		}
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
		public void onClicked(GuiBase gui, MouseButton button)
		{
			if (GuiHelper.onClickEvent(specialInfoButton.clickEvent))
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
	public final PanelScrollBar sliderPages, sliderTextV;
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
			public void onClicked(GuiBase gui, MouseButton button)
			{
				GuiHelper.playClickSound();
				sliderPages.setValue(gui, 0D);
				sliderTextV.setValue(gui, 0D);
				setSelectedPage(selectedPage.parent);
			}

			@Override
			public String getTitle(GuiBase gui)
			{
				return (selectedPage.parent == null) ? GuiLang.CLOSE.translate() : GuiLang.BACK.translate();
			}
		};

		buttonBack.setIcon(new ColoredIcon(TEX_CLOSE, FTBLibClientConfig.guide.text.getColor()));

		panelPages = new Panel(0, 0, 0, 0)
		{
			@Override
			public void addWidgets()
			{
				for (GuidePage c : selectedPage.childPages.values())
				{
					add(c.createWidget(GuiGuide.this));
				}

				panelTitle.refreshWidgets();
			}

			@Override
			public void updateWidgetPositions()
			{
				if (!widgets.isEmpty())
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
				for (Widget w : panelPages.widgets)
				{
					if (w instanceof ButtonGuidePage)
					{
						((ButtonGuidePage) w).updateTitle(GuiGuide.this);
					}
				}

				boolean uni = getFont().getUnicodeFlag();
				getFont().setUnicodeFlag(true);

				for (IGuideTextLine line : selectedPage.text)
				{
					add(line == null ? new Widget(0, 0, panelText.width, getFont().FONT_HEIGHT + 1) : line.createWidget(GuiGuide.this, panelText));
				}

				getFont().setUnicodeFlag(uni);
			}

			@Override
			public void updateWidgetPositions()
			{
				if (!widgets.isEmpty())
				{
					int s = align(LAYOUT);
					sliderTextV.setElementSize(s);
					sliderTextV.setSrollStepFromOneElementSize((s - 6) / widgets.size());
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
				buttonBack.setIcon(new ColoredIcon((selectedPage.parent == null) ? TEX_CLOSE : TEX_BACK, getContentColor()));

				specialButtons.clear();

				for (SpecialGuideButton button : selectedPage.specialButtons)
				{
					specialButtons.add(new ButtonSpecial(button));
				}

				addAll(specialButtons);
			}
		};

		sliderPages = new PanelScrollBar(0, 0, 12, 0, 18, panelPages);
		sliderPages.slider = TEX_SLIDER_V;
		sliderPages.background = Icon.EMPTY;

		sliderTextV = new PanelScrollBar(0, 0, 12, 0, 18, panelText);
		sliderTextV.slider = TEX_SLIDER_V;
		sliderTextV.background = Icon.EMPTY;

		specialButtons = new ArrayList<>();
	}

	public GuidePage getSelectedPage()
	{
		return selectedPage;
	}

	public void setSelectedPage(@Nullable GuidePage p)
	{
		sliderTextV.setValue(this, 0D);

		if (selectedPage != p)
		{
			if (p == null)
			{
				ClientUtils.MC.player.closeScreen();
				return;
			}
			else
			{
				selectedPage = p;

				if (p.childPages.isEmpty())
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

		add(sliderTextV);
		add(panelPages);
		add(panelText);
		add(panelTitle);
		add(sliderPages);

		panelPages.setWidth(panelWidth - (sliderPages.isEnabled(this) ? 32 : 17));

		for (int i = 0; i < specialButtons.size(); i++)
		{
			ButtonSpecial b = specialButtons.get(i);
			b.posX = panelWidth - 24 - 20 * i;
			b.posY = 10;
		}
	}

	@Override
	public void onInit()
	{
		posX = FTBLibClientConfig.guide.border_width;
		posY = FTBLibClientConfig.guide.border_height;
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
		panelText.setWidth(width - panelWidth - 23 - sliderTextV.width);
		panelText.setHeight(height - 12);

		sliderPages.posX = panelWidth - sliderPages.width - 10;
		sliderPages.posY = 46;
		sliderPages.setHeight(height - 56);

		sliderTextV.posY = 10;
		sliderTextV.setHeight(height - 20);
		sliderTextV.posX = width - 10 - sliderTextV.width;
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
		return FTBLibClientConfig.guide.text.getColor();
	}

	@Override
	public boolean drawDefaultBackground()
	{
		return false;
	}

	@Override
	public boolean changePage(String value)
	{
		GuidePage page = pageTree.getSubRaw(value);

		if (page != null)
		{
			if (page.parent != null)
			{
				setSelectedPage(page.parent);
			}

			setSelectedPage(page);
		}

		return false;
	}
}