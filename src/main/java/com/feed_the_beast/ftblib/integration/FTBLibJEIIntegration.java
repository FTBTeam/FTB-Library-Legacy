package com.feed_the_beast.ftblib.integration;

import com.feed_the_beast.ftblib.events.client.CustomClickEvent;
import com.feed_the_beast.ftblib.lib.gui.GuiContainerWrapper;
import mezz.jei.Internal;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.bookmarks.BookmarkList;
import mezz.jei.config.KeyBindings;
import mezz.jei.input.InputHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author mezz
 * @author LatvianModder
 */
@JEIPlugin
public class FTBLibJEIIntegration implements IModPlugin
{
	public static IJeiRuntime RUNTIME;
	private static Optional<BookmarkList> bookmarkList;

	@Override
	public void onRuntimeAvailable(IJeiRuntime r)
	{
		RUNTIME = r;
	}

	@Override
	public void register(IModRegistry registry)
	{
		try
		{
			registry.addGlobalGuiHandlers(new JEIGlobalGuiHandler());
		}
		catch (RuntimeException | LinkageError ignored)
		{
			// only JEI 4.14.0 or higher supports addGlobalGuiHandlers
		}

		try
		{
			registry.addGhostIngredientHandler(GuiContainerWrapper.class, new JEIGhostItemHandler());
		}
		catch (RuntimeException | LinkageError ignored)
		{
		}

		MinecraftForge.EVENT_BUS.register(FTBLibJEIIntegration.class);
	}

	/**
	 * FIXME: Remove hacks when JEI API supports ingredients in non-container GuiScreens
	 */
	public static void handleIngredientKey(int key, Object object)
	{
	try{
		if (RUNTIME != null)
		{
			if (KeyBindings.showRecipe.isActiveAndMatches(key))
			{
				showRecipe(object);
			}
			else if (KeyBindings.showUses.isActiveAndMatches(key))
			{
				showUses(object);
			}
			else if (KeyBindings.bookmark.isActiveAndMatches(key)) //NoSuchFieldError https://paste.dimdev.org/qosinakulu.http
			{
				addBookmark(object);
			}
		}
		}catch(Exception e){e.printStackTrace();}
	}

	public static void showRecipe(Object object)
	{
		RUNTIME.getRecipesGui().show(RUNTIME.getRecipeRegistry().createFocus(IFocus.Mode.OUTPUT, object));
	}

	public static void showUses(Object object)
	{
		RUNTIME.getRecipesGui().show(RUNTIME.getRecipeRegistry().createFocus(IFocus.Mode.INPUT, object));
	}

	@SuppressWarnings({"deprecation", "OptionalAssignedToNull", "OptionalIsPresent"})
	public static void addBookmark(Object object)
	{
		if (bookmarkList == null)
		{
			try
			{
				bookmarkList = Optional.of((BookmarkList) ReflectionHelper.findField(InputHandler.class, "bookmarkList").get(ReflectionHelper.findField(Internal.class, "inputHandler").get(null)));
			}
			catch (Exception ex)
			{
				bookmarkList = Optional.empty();
			}
		}

		if (bookmarkList.isPresent())
		{
			bookmarkList.get().add(object);
		}
	}

	@SubscribeEvent
	public static void onCustomClick(CustomClickEvent event)
	{
		if (event.getID().getNamespace().equals("jeicategory"))
		{
			if (RUNTIME != null)
			{
				RUNTIME.getRecipesGui().showCategories(Arrays.asList(event.getID().getPath().split(";")));
			}
		}
	}
}
