package com.feed_the_beast.ftbl.api;

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
public abstract class ClientATHelper
{
	public static ClientATHelper INSTANCE;

	@Nullable
	public abstract ResourceLocation getFontUnicodePage(int page);

	public abstract int getGuiX(GuiContainer gui);

	public abstract int getGuiY(GuiContainer gui);

	public abstract int getGuiWidth(GuiContainer gui);

	public abstract int getGuiHeight(GuiContainer gui);

	public abstract GuiRecipeBook getRecipeBook(GuiInventory gui);

	public abstract Map<ChatType, List<IChatListener>> getChatListeners();

	public abstract Map<String, TextureAtlasSprite> getRegisteredSpritesMap();
}