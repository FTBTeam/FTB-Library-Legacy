package com.feed_the_beast.ftblib.lib.gui;

import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.feed_the_beast.ftblib.lib.gui.misc.GuiLoading;
import com.feed_the_beast.ftblib.lib.gui.misc.ThemeVanilla;
import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.io.Bits;
import com.feed_the_beast.ftblib.lib.util.NetUtils;
import it.unimi.dsi.fastutil.booleans.BooleanArrayList;
import it.unimi.dsi.fastutil.booleans.BooleanStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.fml.client.config.GuiUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import javax.annotation.Nullable;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author LatvianModder
 */
public abstract class GuiBase extends Panel
{
	public static class PositionedTextData
	{
		public final int posX, posY;
		public final int width, height;
		public final ClickEvent clickEvent;
		public final HoverEvent hoverEvent;
		public final String insertion;

		public PositionedTextData(int x, int y, int w, int h, Style s)
		{
			posX = x;
			posY = y;
			width = w;
			height = h;
			clickEvent = s.getClickEvent();
			hoverEvent = s.getHoverEvent();
			insertion = s.getInsertion();
		}
	}

	private final FontRenderer font;
	private int mouseX, mouseY, mouseWheel;
	private float partialTicks;
	private boolean refreshWidgets;
	private ScaledResolution screen;
	public boolean fixUnicode;
	private GuiScreen prevScreen;
	private final BooleanStack fontUnicode;
	private Theme theme;

	public GuiBase(int w, int h)
	{
		super(null, 0, 0, w, h);
		gui = this;
		font = createFont();
		prevScreen = ClientUtils.MC.currentScreen;
		fontUnicode = new BooleanArrayList();
	}

	public static void setupDrawing()
	{
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
	}

	public final void initGui()
	{
		screen = new ScaledResolution(ClientUtils.MC);
		if (onInit())
		{
			refreshWidgets();
			fixUnicode = screen.getScaleFactor() % 2 == 1;
			onPostInit();
		}
	}

	public Theme getTheme()
	{
		if (theme == null)
		{
			theme = createTheme();
		}

		return theme;
	}

	protected Theme createTheme()
	{
		return ThemeVanilla.INSTANCE;
	}

	@Override
	public int getAX()
	{
		return (screen.getScaledWidth() - width) / 2;
	}

	@Override
	public int getAY()
	{
		return (screen.getScaledHeight() - height) / 2;
	}

	@Override
	public void setScrollX(int scroll)
	{
	}

	@Override
	public void setScrollY(int scroll)
	{
	}

	@Override
	public boolean hasFlag(int flag)
	{
		return false;
	}

	public boolean onInit()
	{
		return true;
	}

	protected boolean setFullscreen()
	{
		setWidth(screen.getScaledWidth());
		setHeight(screen.getScaledHeight());
		return true;
	}

	public void onPostInit()
	{
	}

	@Nullable
	public GuiScreen getPrevScreen()
	{
		if (prevScreen instanceof GuiWrapper && ((GuiWrapper) prevScreen).getWrappedGui() instanceof GuiLoading)
		{
			return ((GuiWrapper) prevScreen).getWrappedGui().getPrevScreen();
		}
		else if (prevScreen instanceof GuiChat)
		{
			return null;
		}

		return prevScreen;
	}

	public final void closeGui()
	{
		closeGui(true);
	}

	public final void closeGui(boolean openPrevScreen)
	{
		int mx = Mouse.getX();
		int my = Mouse.getY();

		if (ClientUtils.MC.player != null)
		{
			ClientUtils.MC.player.closeScreen();
		}

		if (openPrevScreen)
		{
			ClientUtils.MC.displayGuiScreen(getPrevScreen());
			Mouse.setCursorPosition(mx, my);
		}

		onClosed();
	}

	public boolean onClosedByKey()
	{
		return true;
	}

	public void onClosed()
	{
	}

	public boolean doesGuiPauseGame()
	{
		return false;
	}

	protected FontRenderer createFont()
	{
		return ClientUtils.MC.fontRenderer;
	}

	@Override
	public final void refreshWidgets()
	{
		refreshWidgets = true;
	}

	public final void updateGui(int mx, int my, float pt)
	{
		partialTicks = pt;
		mouseX = mx;
		mouseY = my;
		mouseWheel = Mouse.getDWheel();

		if (refreshWidgets)
		{
			super.refreshWidgets();
			refreshWidgets = false;
		}

		posX = getAX();
		posY = getAY();
	}

	@Override
	public final void draw()
	{
		setupDrawing();
		drawBackground();
		super.draw();
	}

	@Override
	protected final void renderPanelBackground(int ax, int ay)
	{
	}

	public boolean drawDefaultBackground()
	{
		return true;
	}

	public void drawBackground()
	{
	}

	public void drawForeground()
	{
		List<String> tempTextList = new ArrayList<>(0);
		addMouseOverText(tempTextList);
		GuiUtils.drawHoveringText(tempTextList, mouseX, Math.max(mouseY, 18), screen.getScaledWidth(), screen.getScaledHeight(), 0, font);
	}

	@Override
	public boolean shouldAddMouseOverText()
	{
		return true;
	}

	public GuiScreen getWrapper()
	{
		return new GuiWrapper(this);
	}

	public final void openGui()
	{
		ClientUtils.MC.displayGuiScreen(getWrapper());
	}

	public final void openGuiLater()
	{
		ClientUtils.runLater(this::openGui);
	}

	public final ScaledResolution getScreen()
	{
		return screen;
	}

	public final int getMouseX()
	{
		return mouseX;
	}

	public final int getMouseY()
	{
		return mouseY;
	}

	public final int getMouseWheel()
	{
		return mouseWheel;
	}

	public final float getPartialTicks()
	{
		return partialTicks;
	}

	public final boolean isMouseButtonDown(int button)
	{
		return Mouse.isButtonDown(button);
	}

	public final boolean isKeyDown(int key)
	{
		return Keyboard.isKeyDown(key);
	}

	public boolean isMouseOver(int x, int y, int w, int h)
	{
		return getMouseX() >= x && getMouseY() >= y && getMouseX() < x + w && getMouseY() < y + h;
	}

	public boolean isMouseOver(Widget widget)
	{
		if (widget == this)
		{
			return true;
		}
		else if (isMouseOver(widget.getAX(), widget.getAY(), widget.width, widget.height))
		{
			boolean offset = widget.parent.isOffset();
			widget.parent.setOffset(false);
			boolean b = isMouseOver(widget.parent);
			widget.parent.setOffset(offset);
			return b;
		}

		return false;
	}

	public void pushFontUnicode(boolean flag)
	{
		fontUnicode.push(font.getUnicodeFlag());
		font.setUnicodeFlag(flag);
	}

	public void popFontUnicode()
	{
		font.setUnicodeFlag(fontUnicode.pop());
	}

	public int getStringWidth(String text)
	{
		return font.getStringWidth(text);
	}

	public int getFontHeight()
	{
		return font.FONT_HEIGHT;
	}

	public String trimStringToWidth(String text, int width, boolean reverse)
	{
		return font.trimStringToWidth(text, width, reverse);
	}

	public List<String> listFormattedStringToWidth(String text, int width)
	{
		if (width <= 0)
		{
			return Collections.emptyList();
		}

		return font.listFormattedStringToWidth(text, width);
	}

	public int drawString(String text, int x, int y, Color4I color, boolean shadow, boolean centered)
	{
		if (text.isEmpty() || color.isEmpty())
		{
			return 0;
		}

		if (centered)
		{
			x -= font.getStringWidth(text) / 2;
			y -= font.FONT_HEIGHT / 2;
		}

		int i = font.drawString(text, x, y, color.rgba(), shadow);
		GlStateManager.color(1F, 1F, 1F, 1F);
		return i;
	}

	public int drawString(String text, int x, int y, Color4I color, int flags)
	{
		return drawString(text, x, y, color, Bits.getFlag(flags, SHADOW), Bits.getFlag(flags, CENTERED));
	}

	public final int drawString(String text, int x, int y, int flags)
	{
		return drawString(text, x, y, getTheme().getContentColor(Bits.getFlag(flags, MOUSE_OVER)), flags);
	}

	public final int drawString(String text, int x, int y)
	{
		return drawString(text, x, y, getTheme().getContentColor(false), false, false);
	}

	public boolean isShiftDown()
	{
		return GuiScreen.isShiftKeyDown();
	}

	public boolean isCtrlDown()
	{
		return GuiScreen.isCtrlKeyDown();
	}

	@Override
	public Icon getIcon()
	{
		return getTheme().getGui(false);
	}

	public final boolean onClickEvent(@Nullable ClickEvent clickEvent)
	{
		if (clickEvent == null)
		{
			return false;
		}

		switch (clickEvent.getAction())
		{
			case OPEN_URL:
				return onClickEvent(clickEvent.getValue());
			case OPEN_FILE:
				return onClickEvent("file", clickEvent.getValue());
			case RUN_COMMAND:
				return onClickEvent("command", clickEvent.getValue());
			case SUGGEST_COMMAND:
				return onClickEvent("suggest_command", clickEvent.getValue());
			case CHANGE_PAGE:
				return onClickEvent("", clickEvent.getValue());
			default:
				return false;
		}
	}

	public final boolean onClickEvent(String click)
	{
		int i = click.indexOf(':');

		if (i != -1)
		{
			return onClickEvent(click.substring(0, i), click.substring(i + 1, click.length()));
		}
		else
		{
			return onClickEvent("", click);
		}
	}

	public boolean onClickEvent(String scheme, String path)
	{
		switch (scheme)
		{
			case "http":
			case "https":
			{
				try
				{
					final URI uri = new URI(scheme + ':' + path);
					if (ClientUtils.MC.gameSettings.chatLinksPrompt)
					{
						final GuiScreen currentScreen = ClientUtils.MC.currentScreen;

						ClientUtils.MC.displayGuiScreen(new GuiConfirmOpenLink((result, id) ->
						{
							if (result)
							{
								try
								{
									NetUtils.openURI(uri);
								}
								catch (Exception ex)
								{
									ex.printStackTrace();
								}
							}
							ClientUtils.MC.displayGuiScreen(currentScreen);
						}, scheme + ':' + path, 0, false));
					}
					else
					{
						NetUtils.openURI(uri);
					}

					return true;
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}

				return false;
			}
			case "file":
			{
				try
				{
					NetUtils.openURI(new URI("file:" + path));
					return true;
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}

				return false;
			}
			case "command":
			{
				ClientUtils.execClientCommand(path, false);
				return true;
			}
			default:
				return false;
		}
	}

	//TODO: Improve me to fix occasional offset
	public List<PositionedTextData> createDataFrom(ITextComponent component, FontRenderer font, int width)
	{
		if (width <= 0 || component.getUnformattedText().isEmpty())
		{
			return Collections.emptyList();
		}

		List<PositionedTextData> list = new ArrayList<>();

		int line = 0;
		int currentWidth = 0;

		for (ITextComponent t : component.createCopy())
		{
			String text = t.getUnformattedComponentText();
			int textWidth = font.getStringWidth(text);

			while (textWidth > 0)
			{
				int w = textWidth;
				if (w > width - currentWidth)
				{
					w = width - currentWidth;
				}

				list.add(new PositionedTextData(currentWidth, line * 10, w, 10, t.getStyle()));

				currentWidth += w;
				textWidth -= w;

				if (currentWidth >= width)
				{
					currentWidth = 0;
					line++;
				}
			}
		}

		return list;
	}
}