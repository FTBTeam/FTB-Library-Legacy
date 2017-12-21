package com.feed_the_beast.ftblib.lib;

import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.chat.IChatListener;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.gui.recipebook.GuiRecipeBook;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ChatType;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * @author LatvianModder
 */
public final class ClientATHelper
{
	@Nullable
	public static ResourceLocation getFontUnicodePage(int page)
	{
		return FontRenderer.UNICODE_PAGE_LOCATIONS[page];
	}

	public static int getGuiX(GuiContainer gui)
	{
		return gui.guiLeft;
	}

	public static int getGuiY(GuiContainer gui)
	{
		return gui.guiTop;
	}

	public static int getGuiWidth(GuiContainer gui)
	{
		return gui.xSize;
	}

	public static int getGuiHeight(GuiContainer gui)
	{
		return gui.ySize;
	}

	public static GuiRecipeBook getRecipeBook(GuiInventory gui)
	{
		return gui.recipeBookGui;
	}

	public static Map<ChatType, List<IChatListener>> getChatListeners()
	{
		return ClientUtils.MC.ingameGUI.chatListeners;
	}

	public static Map<String, TextureAtlasSprite> getRegisteredSpritesMap()
	{
		return ClientUtils.MC.getTextureMapBlocks().mapRegisteredSprites;
	}
}