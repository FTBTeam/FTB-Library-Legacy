package com.feed_the_beast.ftblib.integration;

import com.feed_the_beast.ftblib.client.FTBLibClientEventHandler;
import com.feed_the_beast.ftblib.lib.gui.IGuiWrapper;
import mezz.jei.api.gui.IGlobalGuiHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Collection;
import java.util.Collections;

/**
 * @author LatvianModder
 */
public class JEIGlobalGuiHandler implements IGlobalGuiHandler
{
	@Override
	public Collection<Rectangle> getGuiExtraAreas()
	{
		GuiScreen currentScreen = Minecraft.getMinecraft().currentScreen;

		if (FTBLibClientEventHandler.areButtonsVisible(currentScreen))
		{
			return Collections.singleton(FTBLibClientEventHandler.lastDrawnArea);
		}

		return Collections.emptySet();
	}

	@Override
	@Nullable
	public Object getIngredientUnderMouse(int mouseX, int mouseY)
	{
		GuiScreen currentScreen = Minecraft.getMinecraft().currentScreen;

		if (currentScreen instanceof IGuiWrapper)
		{
			return ((IGuiWrapper) currentScreen).getGui().getIngredientUnderMouse();
		}

		return null;
	}
}