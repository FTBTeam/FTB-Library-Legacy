package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.api.ClientATHelper;
import com.feed_the_beast.ftbl.lib.client.ClientUtils;
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
public class ClientATHelperImpl extends ClientATHelper
{
	@Nullable
	@Override
	public ResourceLocation getFontUnicodePage(int page)
	{
		return FontRenderer.UNICODE_PAGE_LOCATIONS[page];
	}

	@Override
	public int getGuiX(GuiContainer gui)
	{
		return gui.guiLeft;
	}

	@Override
	public int getGuiY(GuiContainer gui)
	{
		return gui.guiTop;
	}

	@Override
	public int getGuiWidth(GuiContainer gui)
	{
		return gui.xSize;
	}

	@Override
	public int getGuiHeight(GuiContainer gui)
	{
		return gui.ySize;
	}

	@Override
	public GuiRecipeBook getRecipeBook(GuiInventory gui)
	{
		return gui.recipeBookGui;
	}

	@Override
	public Map<ChatType, List<IChatListener>> getChatListeners()
	{
		return ClientUtils.MC.ingameGUI.chatListeners;
	}

	@Override
	public Map<String, TextureAtlasSprite> getRegisteredSpritesMap()
	{
		return ClientUtils.MC.getTextureMapBlocks().mapRegisteredSprites;
	}
}