package com.feed_the_beast.ftblib.lib.gui;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IFocus;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
@JEIPlugin
public class FTBLibJEIIntegration implements IModPlugin
{
	public static IJeiRuntime RUNTIME;

	@Override
	public void onRuntimeAvailable(IJeiRuntime r)
	{
		RUNTIME = r;
	}

	public static boolean openFocus(int key, @Nullable Object object)
	{
		if (object != null && RUNTIME != null)
		{
			if (key == Keyboard.KEY_R) // Replace with JEI Key
			{
				RUNTIME.getRecipesGui().show(RUNTIME.getRecipeRegistry().createFocus(IFocus.Mode.OUTPUT, object));
				return true;
			}
			else if (key == Keyboard.KEY_U) // Replace with JEI Key
			{
				RUNTIME.getRecipesGui().show(RUNTIME.getRecipeRegistry().createFocus(IFocus.Mode.INPUT, object));
				return true;
			}
		}

		return false;
	}
}