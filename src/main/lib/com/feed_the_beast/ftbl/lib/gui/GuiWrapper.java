package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.api.gui.IGuiWrapper;
import com.feed_the_beast.ftbl.lib.MouseButton;
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
		wrappedGui.mousePressed(wrappedGui, MouseButton.get(b));
		super.mouseClicked(mx, my, b);
	}

	@Override
	protected void mouseReleased(int mx, int my, int state)
	{
		wrappedGui.mouseReleased(wrappedGui);
		super.mouseReleased(mx, my, state);
	}

	@Override
	protected void keyTyped(char keyChar, int key) throws IOException
	{
		if (wrappedGui.keyPressed(wrappedGui, key, keyChar))
		{
			return;
		}

		if (key == Keyboard.KEY_ESCAPE || (mc.world != null && mc.gameSettings.keyBindInventory.isActiveAndMatches(key)))
		{
			if (wrappedGui.onClosedByKey())
			{
				wrappedGui.closeGui();
			}

			return;
		}

		super.keyTyped(keyChar, key);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		if (wrappedGui.fixUnicode)
		{
			GuiHelper.setFixUnicode(true);
		}

		wrappedGui.updateGui(mouseX, mouseY, partialTicks);

		if (wrappedGui.drawDefaultBackground())
		{
			drawDefaultBackground();
		}

		GuiBase.setupDrawing();
		wrappedGui.drawBackground();
		GuiBase.setupDrawing();
		wrappedGui.renderWidgets();
		GuiBase.setupDrawing();
		wrappedGui.drawForeground();

		if (wrappedGui.fixUnicode)
		{
			GuiHelper.setFixUnicode(false);
		}
	}

	@Override
	public GuiBase getWrappedGui()
	{
		return wrappedGui;
	}
}