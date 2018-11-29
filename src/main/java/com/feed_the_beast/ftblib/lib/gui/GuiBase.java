package com.feed_the_beast.ftblib.lib.gui;

import com.feed_the_beast.ftblib.FTBLibConfig;
import com.feed_the_beast.ftblib.events.client.CustomClickEvent;
import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.feed_the_beast.ftblib.lib.gui.misc.GuiLoading;
import com.feed_the_beast.ftblib.lib.gui.misc.YesNoCallback;
import com.feed_the_beast.ftblib.lib.util.NetUtils;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import javax.annotation.Nullable;
import java.net.URI;
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

	private int mouseX, mouseY;
	private boolean refreshWidgets;
	private ScaledResolution screen;
	public boolean fixUnicode;
	private GuiScreen prevScreen;
	public Panel contextMenu = null;

	public GuiBase()
	{
		//noinspection ConstantConditions
		super(null);
		setSize(176, 166);
		setOnlyRenderWidgetsInside(false);
		setOnlyInteractWithWidgetsInside(false);
		prevScreen = ClientUtils.MC.currentScreen;
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
		if (parent instanceof GuiBase)
		{
			screen = parent.getScreen();
		}
		else
		{
			screen = new ScaledResolution(ClientUtils.MC);
		}

		if (onInit())
		{
			super.refreshWidgets();
			fixUnicode = getScreen().getScaleFactor() % 2 == 1;
			alignWidgets();
			onPostInit();
		}
	}

	public Theme getTheme()
	{
		return Theme.DEFAULT;
	}

	@Override
	public int getX()
	{
		return (getScreen().getScaledWidth() - width) / 2;
	}

	@Override
	public int getY()
	{
		return (getScreen().getScaledHeight() - height) / 2;
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

	public void onBack()
	{
		closeGui(true);
	}

	public boolean doesGuiPauseGame()
	{
		return false;
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

		posX = getX();
		posY = getY();

		updateMouseOver(mouseX, mouseY);
	}

	@Override
	public void updateMouseOver(int mouseX, int mouseY)
	{
		isMouseOver = checkMouseOver(mouseX, mouseY);
		setOffset(true);

		if (contextMenu != null)
		{
			contextMenu.updateMouseOver(mouseX, mouseY);
		}
		else
		{
			for (Widget widget : widgets)
			{
				widget.updateMouseOver(mouseX, mouseY);
			}
		}

		setOffset(false);
	}

	@Override
	public final void draw(Theme theme, int x, int y, int w, int h)
	{
		super.draw(theme, x, y, w, h);
	}

	@Override
	public void openContextMenu(@Nullable Panel panel)
	{
		int px = 0, py = 0;

		if (contextMenu != null)
		{
			px = contextMenu.posX;
			py = contextMenu.posY;
			contextMenu.onClosed();
			widgets.remove(contextMenu);
		}

		if (panel == null)
		{
			contextMenu = null;
			return;
		}

		int x = getX();
		int y = getY();

		if (contextMenu == null)
		{
			px = getMouseX() - x;
			py = getMouseY() - y;
		}

		contextMenu = panel;
		contextMenu.parent = this;
		widgets.add(contextMenu);
		contextMenu.refreshWidgets();
		px = Math.min(px, screen.getScaledWidth() - contextMenu.width - x) - 3;
		py = Math.min(py, screen.getScaledHeight() - contextMenu.height - y) - 3;
		contextMenu.setPos(px, py);

		if (contextMenu instanceof GuiBase)
		{
			((GuiBase) contextMenu).initGui();
		}
	}

	public ContextMenu openContextMenu(List<ContextMenuItem> menu)
	{
		ContextMenu contextMenu = new ContextMenu(this, menu);
		openContextMenu(contextMenu);
		return contextMenu;
	}

	@Override
	public void closeContextMenu()
	{
		openContextMenu((Panel) null);
		onInit();
	}

	@Override
	public void onClosed()
	{
		super.onClosed();
		closeContextMenu();
	}

	@Override
	public void drawBackground(Theme theme, int x, int y, int w, int h)
	{
		theme.drawGui(x, y, w, h, WidgetType.NORMAL);
	}

	public boolean drawDefaultBackground()
	{
		return true;
	}

	public void drawForeground(Theme theme, int x, int y, int w, int h)
	{
	}

	@Override
	public boolean mousePressed(MouseButton button)
	{
		if (button == MouseButton.BACK)
		{
			closeGui(true);
		}

		return super.mousePressed(button);
	}

	@Override
	public boolean keyPressed(int key, char keyChar)
	{
		if (super.keyPressed(key, keyChar))
		{
			return true;
		}
		else if (FTBLibConfig.debugging.gui_widget_bounds && key == Keyboard.KEY_B)
		{
			Theme.renderDebugBoxes = !Theme.renderDebugBoxes;
			return true;
		}

		return false;
	}

	@Override
	public boolean shouldAddMouseOverText()
	{
		return contextMenu == null;
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
		if (screen == null)
		{
			return parent.getScreen();
		}

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
			return isMouseOver(getX(), getY(), width, height);
		}
		else if (isMouseOver(widget.getX(), widget.getY(), widget.width, widget.height))
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
			case "curseforgepages":
			{
				String[] s = path.split(":", 2);

				if (s.length == 2)
				{
					return handleClick("https://minecraft.curseforge.com/projects/" + s[0] + "/pages/" + s[1]);
				}

				return false;
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