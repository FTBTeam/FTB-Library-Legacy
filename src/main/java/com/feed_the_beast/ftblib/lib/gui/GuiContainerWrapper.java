package com.feed_the_beast.ftblib.lib.gui;

import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
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
	protected void drawGuiContainerBackgroundLayer(float f, int mx, int my)
	{
		if (wrappedGui.fixUnicode)
		{
			GuiHelper.setFixUnicode(true);
		}

		GuiHelper.setupDrawing();
		drawDefaultBackground();
		wrappedGui.getIcon().draw(guiLeft, guiTop, xSize, ySize);
		wrappedGui.drawBackground();

		Icon icon = wrappedGui.getTheme().getContainerSlot();

		for (Slot slot : inventorySlots.inventorySlots)
		{
			icon.draw(guiLeft + slot.xPos, guiTop + slot.yPos, 16, 16);
		}

		wrappedGui.draw();

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
		GuiHelper.setupDrawing();
		wrappedGui.drawForeground();
		renderHoveredToolTip(mx, my);
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