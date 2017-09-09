package com.feed_the_beast.ftbl.lib.icon;

import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.client.ClientUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class AtlasSpriteProvider extends Icon
{
	@SideOnly(Side.CLIENT)
	public static final Map<ResourceLocation, TextureAtlasSprite> SPRITE_MAP = new HashMap<>();

	public final ResourceLocation name;

	AtlasSpriteProvider(ResourceLocation n)
	{
		name = n;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void draw(int x, int y, int w, int h, Color4I col)
	{
		ClientUtils.MC.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
		TextureAtlasSprite sprite = SPRITE_MAP.computeIfAbsent(name, ClientUtils.DEFAULT_TEXTURE_GETTER);
		col = col.hasColor() ? col : Color4I.WHITE;
		buffer.pos(x, y + h, 0D).tex(sprite.getMinU(), sprite.getMaxV()).color(col.redi(), col.greeni(), col.bluei(), col.alphai()).endVertex();
		buffer.pos(x + w, y + h, 0D).tex(sprite.getMaxU(), sprite.getMaxV()).color(col.redi(), col.greeni(), col.bluei(), col.alphai()).endVertex();
		buffer.pos(x + w, y, 0D).tex(sprite.getMaxU(), sprite.getMinV()).color(col.redi(), col.greeni(), col.bluei(), col.alphai()).endVertex();
		buffer.pos(x, y, 0D).tex(sprite.getMinU(), sprite.getMinV()).color(col.redi(), col.greeni(), col.bluei(), col.alphai()).endVertex();
		tessellator.draw();
	}

	@Override
	public JsonElement getJson()
	{
		return new JsonPrimitive(name.toString());
	}
}