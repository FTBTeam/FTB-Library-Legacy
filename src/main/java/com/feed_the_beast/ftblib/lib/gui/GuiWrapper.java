package com.feed_the_beast.ftblib.lib.gui;

import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

/**
 * @author LatvianModder
 */
public class GuiWrapper extends GuiScreen implements IGuiWrapper
{
	private GuiBase wrappedGui;

	public GuiWrapper(GuiBase g)
	{
		wrappedGui = g;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		wrappedGui.initGui();
	}

	@Override
	public boolean doesGuiPauseGame()
	{
		return wrappedGui.doesGuiPauseGame();
	}

	@Override
	protected final void mouseClicked(int mx, int my, int b) throws IOException
	{
		wrappedGui.mousePressed(MouseButton.get(b));
		super.mouseClicked(mx, my, b);
	}

	@Override
	protected void mouseReleased(int mx, int my, int b)
	{
		wrappedGui.mouseReleased(MouseButton.get(b));
		super.mouseReleased(mx, my, b);
	}

	@Override
	protected void keyTyped(char keyChar, int key) throws IOException
	{
		if (wrappedGui.keyPressed(key, keyChar))
		{
			return;
		}
		else if (wrappedGui.onClosedByKey(key))
		{
			wrappedGui.closeGui();
			return;
		}

		super.keyTyped(keyChar, key);
	}

	@Override
	public void handleKeyboardInput() throws IOException
	{
		if (!Keyboard.getEventKeyState())
		{
			wrappedGui.keyReleased(Keyboard.getEventKey());
		}
		else
		{
			super.handleKeyboardInput();
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		if (wrappedGui.fixUnicode)
		{
			GuiHelper.setFixUnicode(true);
		}

		wrappedGui.updateGui(mouseX, mouseY, partialTicks);
		drawDefaultBackground();
		GuiHelper.setupDrawing();
		wrappedGui.getIcon().draw(wrappedGui.getAX(), wrappedGui.getAY(), wrappedGui.width, wrappedGui.height);
		wrappedGui.drawBackground();
		wrappedGui.draw();
		wrappedGui.drawForeground();

		if (wrappedGui.fixUnicode)
		{
			GuiHelper.setFixUnicode(false);
		}
	}

	@Override
	public void drawDefaultBackground()
	{
		if (wrappedGui.drawDefaultBackground())
		{
			super.drawDefaultBackground();
		}
	}

	@Override
	public GuiBase getGui()
	{
		return wrappedGui;
	}
}