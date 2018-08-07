package com.feed_the_beast.ftblib.lib.gui;

import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

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
	protected final void mouseClicked(int mouseX, int mouseY, int button) throws IOException
	{
		wrappedGui.updateMouseOver(mouseX, mouseY);

		if (!wrappedGui.mousePressed(MouseButton.get(button)))
		{
			super.mouseClicked(mouseX, mouseY, button);
		}
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int button)
	{
		wrappedGui.updateMouseOver(mouseX, mouseY);
		wrappedGui.mouseReleased(MouseButton.get(button));
		super.mouseReleased(mouseX, mouseY, button);
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
			wrappedGui.closeGui(!isShiftKeyDown());
			return;
		}

		super.keyTyped(keyChar, key);
	}

	@Override
	public void handleMouseInput() throws IOException
	{
		super.handleMouseInput();

		int scroll = Mouse.getEventDWheel();
		if (scroll != 0)
		{
			wrappedGui.mouseScrolled(scroll);
		}
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

		if (wrappedGui.contextMenu == null)
		{
			renderHoveredToolTip(mx, my);
		}

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
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		wrappedGui.updateGui(mouseX, mouseY);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public GuiBase getGui()
	{
		return wrappedGui;
	}
}