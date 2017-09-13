package com.feed_the_beast.ftbl.lib;

import com.feed_the_beast.ftbl.lib.client.SpriteSet;
import com.feed_the_beast.ftbl.lib.util.StringUtils;
import com.google.gson.JsonElement;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author LatvianModder
 */
public class TextureSet
{
	public static final TextureSet DEFAULT = TextureSet.of("all=blocks/planks_oak");

	public static TextureSet of(String v)
	{
		TextureSet set = new TextureSet();
		Map<String, String> map = StringUtils.parse(StringUtils.TEMP_MAP, v);

		String s = map.get("all");

		if (s != null)
		{
			ResourceLocation tex = new ResourceLocation(s);

			for (int i = 0; i < 6; i++)
			{
				set.textures[i] = tex;
			}
		}

		for (EnumFacing facing : EnumFacing.VALUES)
		{
			s = map.get(facing.getName());

			if (s != null)
			{
				set.textures[facing.ordinal()] = new ResourceLocation(s);
			}
		}

		return set;
	}

	public static TextureSet of(JsonElement json)
	{
		return of(json.getAsString());
	}

	public static TextureSet of(IBlockState state)
	{
		return DEFAULT; //FIXME
	}

	public final ResourceLocation[] textures;

	private TextureSet()
	{
		textures = new ResourceLocation[6];
	}

	@Nullable
	public ResourceLocation getTexture(EnumFacing face)
	{
		return textures[face.ordinal()];
	}

	public Collection<ResourceLocation> getTextures()
	{
		List<ResourceLocation> list = new ArrayList<>();

		for (int i = 0; i < 6; i++)
		{
			if (textures[i] != null)
			{
				list.add(textures[i]);
			}
		}

		return list;
	}

	@SideOnly(Side.CLIENT)
	public SpriteSet getSpriteSet(Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
	{
		for (ResourceLocation id : textures)
		{
			if (id != null)
			{
				return new SpriteSet(this, bakedTextureGetter);
			}
		}

		return SpriteSet.EMPTY;
	}
}