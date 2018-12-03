package com.feed_the_beast.ftblib.integration;

import com.feed_the_beast.ftblib.client.FTBLibClientEventHandler;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.gui.IGlobalGuiHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.util.Collection;
import java.util.Collections;

/**
 * @author mezz
 */
@JEIPlugin
public class JeiIntegration implements IModPlugin
{
	@Override
	public void register(IModRegistry registry)
	{
		try
		{
			registry.addGlobalGuiHandlers(new IGlobalGuiHandler()
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
			});
		}
		catch (RuntimeException | LinkageError ignored)
		{
			// only JEI 4.14.0 or higher supports addGlobalGuiHandlers
		}
	}
}
