package com.feed_the_beast.ftblib.lib.gui;

import com.feed_the_beast.ftblib.FTBLibConfig;
import com.feed_the_beast.ftblib.events.client.CustomClickEvent;
import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.feed_the_beast.ftblib.lib.gui.misc.GuiLoading;
import com.feed_the_beast.ftblib.lib.gui.misc.YesNoCallback;
import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.util.NetUtils;
import it.unimi.dsi.fastutil.booleans.BooleanArrayList;
import it.unimi.dsi.fastutil.booleans.BooleanStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.fml.client.config.GuiUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import javax.annotation.Nullable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public abstract class GuiBase extends Panel implements IOpenableGui
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
	private int mouseX, mouseY;
	private boolean refreshWidgets;
	private ScaledResolution screen;
	public boolean fixUnicode;
	private GuiScreen prevScreen;
	private final BooleanStack fontUnicode;
	private Theme theme;
	public static boolean renderDebugBoxes = false;

	public GuiBase()
	{
		//noinspection ConstantConditions
		super(null);
		setSize(176, 166);
		setOnlyRenderWidgetsInside(false);
		setOnlyInteractWithWidgetsInside(false);
		font = createFont();
		prevScreen = ClientUtils.MC.currentScreen;
		fontUnicode = new BooleanArrayList();
	}

	@Override
	public final GuiBase getGui()
	{
		return this;
	}

	@Override
	public void alignWidgets()
	{
	}

	public final void initGui()
	{
		screen = new ScaledResolution(ClientUtils.MC);

		if (onInit())
		{
			super.refreshWidgets();
			fixUnicode = screen.getScaleFactor() % 2 == 1;
			alignWidgets();
			onPostInit();
		}
	}

	@Override
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
		return Theme.DEFAULT;
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
	public int getScrollX()
	{
		return 0;
	}

	@Override
	public int getScrollY()
	{
		return 0;
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
		if (prevScreen instanceof GuiWrapper && ((GuiWrapper) prevScreen).getGui() instanceof GuiLoading)
		{
			return ((GuiWrapper) prevScreen).getGui().getPrevScreen();
		}
		else if (prevScreen instanceof GuiChat)
		{
			return null;
		}

		return prevScreen;
	}

	@Override
	public final void closeGui(boolean openPrevScreen)
	{
		int mx = Mouse.getX();
		int my = Mouse.getY();

		if (ClientUtils.MC.player != null)
		{
			ClientUtils.MC.player.closeScreen();

			if (ClientUtils.MC.currentScreen == null)
			{
				ClientUtils.MC.setIngameFocus();
			}
		}

		if (openPrevScreen)
		{
			ClientUtils.MC.displayGuiScreen(getPrevScreen());
			Mouse.setCursorPosition(mx, my);
		}

		onClosed();
	}

	public boolean onClosedByKey(int key)
	{
		return key == Keyboard.KEY_ESCAPE || ClientUtils.MC.gameSettings.keyBindInventory.isActiveAndMatches(key);
	}

	public boolean doesGuiPauseGame()
	{
		return false;
	}

	@Override
	public FontRenderer getFont()
	{
		return font;
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

	public final void updateGui(int mx, int my)
	{
		mouseX = mx;
		mouseY = my;

		if (refreshWidgets)
		{
			super.refreshWidgets();
			refreshWidgets = false;
		}

		posX = getAX();
		posY = getAY();

		updateMouseOver(mouseX, mouseY);
	}

	@Override
	public final void draw()
	{
		GuiHelper.setupDrawing();
		super.draw();
	}

	@Override
	protected final void drawPanelBackground(int ax, int ay)
	{
	}

	public boolean drawDefaultBackground()
	{
		return true;
	}

	public void drawBackground()
	{
		getTheme().getGui(WidgetType.NORMAL).draw(getAX(), getAY(), width, height);
	}

	public void drawForeground()
	{
		List<String> tempTextList = new ArrayList<>(0);
		addMouseOverText(tempTextList);
		GuiUtils.drawHoveringText(tempTextList, mouseX, Math.max(mouseY, 18), screen.getScaledWidth(), screen.getScaledHeight(), 0, getFont());
	}

	@Override
	public boolean keyPressed(int key, char keyChar)
	{
		boolean b = super.keyPressed(key, keyChar);

		if (!b && FTBLibConfig.debugging.gui_widget_bounds && key == Keyboard.KEY_B)
		{
			renderDebugBoxes = !renderDebugBoxes;
			b = true;
		}

		return b;
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

	@Override
	public final void openGui()
	{
		ClientUtils.MC.displayGuiScreen(getWrapper());
	}

	@Override
	public final ScaledResolution getScreen()
	{
		return screen;
	}

	@Override
	public final int getMouseX()
	{
		return mouseX;
	}

	@Override
	public final int getMouseY()
	{
		return mouseY;
	}

	public boolean isMouseOver(int x, int y, int w, int h)
	{
		return getMouseX() >= x && getMouseY() >= y && getMouseX() < x + w && getMouseY() < y + h;
	}

	public boolean isMouseOver(Widget widget)
	{
		if (widget == this)
		{
			return isMouseOver(getAX(), getAY(), width, height);
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

	@Override
	public void pushFontUnicode(boolean flag)
	{
		fontUnicode.push(getFont().getUnicodeFlag());
		getFont().setUnicodeFlag(flag);
	}

	@Override
	public void popFontUnicode()
	{
		getFont().setUnicodeFlag(fontUnicode.pop());
	}

	@Override
	public Icon getIcon()
	{
		return Icon.EMPTY;
	}

	@Override
	public boolean handleClick(String scheme, String path)
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
			case "custom":
				return new CustomClickEvent(new ResourceLocation(path)).post();
			default:
				return false;
		}
	}

	public void openYesNoFull(String title, String desc, YesNoCallback callback)
	{
		ClientUtils.MC.displayGuiScreen(new GuiYesNo((result, id) ->
		{
			openGui();
			callback.onButtonClicked(result);
			refreshWidgets();
		}, title, desc, 0));
	}

	public final void openYesNo(String title, String desc, Runnable callback)
	{
		openYesNoFull(title, desc, result -> {
			if (result)
			{
				callback.run();
			}
		});
	}
}