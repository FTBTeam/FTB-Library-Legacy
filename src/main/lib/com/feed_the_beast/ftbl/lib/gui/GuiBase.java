package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.lib.client.ClientUtils;
import com.feed_the_beast.ftbl.lib.gui.misc.GuiLoading;
import com.feed_the_beast.ftbl.lib.gui.misc.ThemeVanilla;
import com.feed_the_beast.ftbl.lib.icon.Color4I;
import com.feed_the_beast.ftbl.lib.icon.Icon;
import com.feed_the_beast.ftbl.lib.io.Bits;
import it.unimi.dsi.fastutil.booleans.BooleanArrayList;
import it.unimi.dsi.fastutil.booleans.BooleanStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.client.config.GuiUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author LatvianModder
 */
public abstract class GuiBase extends Panel
{
	public static final List<String> TEMP_TEXT_LIST = new ArrayList<>();

	private final FontRenderer font;
	private int mouseX, mouseY, mouseWheel;
	private float partialTicks;
	private boolean refreshWidgets;
	private ScaledResolution screen;
	public boolean fixUnicode;
	private GuiScreen prevScreen;
	private final BooleanStack fontUnicode;

	public GuiBase(int w, int h)
	{
		super(null, 0, 0, w, h);
		gui = this;
		setParentPanel(this);
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
		onInit();
		refreshWidgets();
		updateWidgetPositions();
		fixUnicode = screen.getScaleFactor() % 2 == 1;
	}

	public Theme getTheme()
	{
		return ThemeVanilla.INSTANCE;
	}

	@Override
	public Panel getParentPanel()
	{
		return this;
	}

	@Override
	public void setParentPanel(Panel p)
	{
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

	public void onInit()
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
	public final void renderWidget()
	{
		setupDrawing();
		drawBackground();
		super.renderWidget();
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
		addMouseOverText(TEMP_TEXT_LIST);
		GuiUtils.drawHoveringText(TEMP_TEXT_LIST, mouseX, Math.max(mouseY, 18), screen.getScaledWidth(), screen.getScaledHeight(), 0, font);
		TEMP_TEXT_LIST.clear();
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

	public boolean isMouseOver(Widget w)
	{
		if (w == this)
		{
			return true;
		}
		else if (isMouseOver(w.getAX(), w.getAY(), w.width, w.height))
		{
			Panel p = w.getParentPanel();
			boolean offset = p.isOffset();
			p.setOffset(false);
			boolean b = isMouseOver(p);
			p.setOffset(offset);
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
		return drawString(text, x, y, getTheme().getContentColor(), flags);
	}

	public final int drawString(String text, int x, int y)
	{
		return drawString(text, x, y, getTheme().getContentColor(), false, false);
	}

	public boolean isShiftDown()
	{
		return GuiScreen.isShiftKeyDown();
	}

	public boolean isCtrlDown()
	{
		return GuiScreen.isCtrlKeyDown();
	}

	public boolean changePage(String value)
	{
		return false;
	}

	@Override
	public Icon getIcon()
	{
		return getTheme().getGui(false);
	}
}