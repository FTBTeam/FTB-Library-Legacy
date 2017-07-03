package com.feed_the_beast.ftbl.lib.client;

import com.feed_the_beast.ftbl.lib.IconSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.function.Function;

/**
 * @author LatvianModder
 */
public class SpriteSet
{
	private final TextureAtlasSprite sprites[];

	private SpriteSet()
	{
		sprites = new TextureAtlasSprite[6];
	}

	public SpriteSet(IconSet set, Function<ResourceLocation, TextureAtlasSprite> function)
	{
		this();

		for (int i = 0; i < 6; i++)
		{
			ResourceLocation tex = set.getTexture(EnumFacing.VALUES[i]);
			sprites[i] = tex == null ? null : function.apply(tex);
		}
	}

	@Nullable
	public TextureAtlasSprite get(EnumFacing f)
	{
		return sprites[f.ordinal()];
	}

	public SpriteSet exclude(EnumFacing... f)
	{
		SpriteSet set = new SpriteSet();
		System.arraycopy(sprites, 0, set.sprites, 0, 6);

		for (EnumFacing facing : f)
		{
			set.sprites[facing.ordinal()] = null;
		}

		return set;
	}
}