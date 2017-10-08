package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.lib.util.misc.MouseButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

/**
 * @author LatvianModder
 */
public class GuiContainerWrapper extends GuiContainer implements IGuiWrapper
{
	private GuiBase wrappedGui;

	public GuiContainerWrapper(GuiBase g, Container c)
	{
		super(c);
		wrappedGui = g;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		wrappedGui.initGui();
		guiLeft = wrappedGui.getAX();
		guiTop = wrappedGui.getAY();
		xSize = wrappedGui.width;
		ySize = wrappedGui.height;
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
	protected void mouseReleased(int mx, int my, int state)
	{
		wrappedGui.mouseReleased();
		super.mouseReleased(mx, my, state);
	}

	@Override
	protected void keyTyped(char keyChar, int key) throws IOException
	{
		if (wrappedGui.keyPressed(key, keyChar))
		{
			return;
		}

		if (key == Keyboard.KEY_ESCAPE || mc.gameSettings.keyBindInventory.isActiveAndMatches(key))
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
	protected void drawGuiContainerBackgroundLayer(float f, int mx, int my)
	{
		if (wrappedGui.fixUnicode)
		{
			GuiHelper.setFixUnicode(true);
		}

		GuiBase.setupDrawing();
		wrappedGui.getFontUnicode().reset();
		wrappedGui.getIcon().draw(wrappedGui);
		wrappedGui.drawBackground();
		GuiBase.setupDrawing();
		wrappedGui.renderWidgets();

		if (wrappedGui.fixUnicode)
		{
			GuiHelper.setFixUnicode(false);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mx, int my)
	{
		if (wrappedGui.fixUnicode)
		{
			GuiHelper.setFixUnicode(true);
		}

		GlStateManager.pushMatrix();
		GlStateManager.translate(-guiLeft, -guiTop, 0D);
		GuiBase.setupDrawing();
		wrappedGui.drawForeground();
		GlStateManager.popMatrix();

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
	public void drawScreen(int mx, int my, float f)
	{
		wrappedGui.updateGui(mx, my, f);
		super.drawScreen(mx, my, f);
	}

	@Override
	public GuiBase getWrappedGui()
	{
		return wrappedGui;
	}
}