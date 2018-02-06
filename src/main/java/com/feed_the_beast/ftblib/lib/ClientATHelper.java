package com.feed_the_beast.ftblib.lib;

import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.chat.IChatListener;
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

	public static Map<ChatType, List<IChatListener>> getChatListeners()
	{
		return ClientUtils.MC.ingameGUI.chatListeners;
	}

	public static Map<String, TextureAtlasSprite> getRegisteredSpritesMap()
	{
		return ClientUtils.MC.getTextureMapBlocks().mapRegisteredSprites;
	}
}