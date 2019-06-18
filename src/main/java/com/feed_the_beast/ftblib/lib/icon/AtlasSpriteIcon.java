package com.feed_the_beast.ftblib.lib.icon;

import javafx.scene.image.Image;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.Optional;

/**
 * @author LatvianModder
 */
public class AtlasSpriteIcon extends Icon
{
	public final String name;

	AtlasSpriteIcon(String n)
	{
		name = n;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void draw(int x, int y, int w, int h, Color4I col)
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
		TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(name);
		col = col.whiteIfEmpty();
		buffer.pos(x, y + h, 0D).tex(sprite.getMinU(), sprite.getMaxV()).color(col.redi(), col.greeni(), col.bluei(), col.alphai()).endVertex();
		buffer.pos(x + w, y + h, 0D).tex(sprite.getMaxU(), sprite.getMaxV()).color(col.redi(), col.greeni(), col.bluei(), col.alphai()).endVertex();
		buffer.pos(x + w, y, 0D).tex(sprite.getMaxU(), sprite.getMinV()).color(col.redi(), col.greeni(), col.bluei(), col.alphai()).endVertex();
		buffer.pos(x, y, 0D).tex(sprite.getMinU(), sprite.getMinV()).color(col.redi(), col.greeni(), col.bluei(), col.alphai()).endVertex();
		tessellator.draw();
		Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
	}

	@Override
	public String toString()
	{
		return name;
	}

	@Override
	public boolean isLoadedJFXImageInstant()
	{
		return true;
	}

	@Override
	public Optional<Image> loadInstantJFXImage()
	{
		try
		{
			ResourceLocation rl = new ResourceLocation(name);
			return Optional.of(new Image(Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(rl.getNamespace(), "textures/" + rl.getPath() + ".png")).getInputStream()));
		}
		catch (Exception ex)
		{
			return Optional.empty();
		}
	}
}