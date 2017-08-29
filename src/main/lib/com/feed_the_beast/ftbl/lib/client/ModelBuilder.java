package com.feed_the_beast.ftbl.lib.client;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import org.lwjgl.util.vector.Vector3f;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class ModelBuilder
{
	private static final FaceBakery BAKERY = new FaceBakery();
	//SimpleBakedModel.Builder // List<BlockPart> parts

	public final VertexFormat format;
	private List<BakedQuad> quads;
	private ModelRotation rotation;
	private boolean uvLocked = true;
	private boolean shade = true;
	private int tintIndex = -1;

	public ModelBuilder(VertexFormat f, ModelRotation r)
	{
		format = f;
		quads = new ArrayList<>();
		rotation = r;
	}

	public void setRotation(ModelRotation r)
	{
		rotation = r;
	}

	public void setUVLocked(boolean b)
	{
		uvLocked = b;
	}

	public void setShade(boolean b)
	{
		shade = b;
	}

	public void setTintIndex(int i)
	{
		tintIndex = -i;
	}

	public List<BakedQuad> getQuads()
	{
		return quads;
	}

	public void addCube(Vector3f from, Vector3f to, SpriteSet sprites)
	{
		for (EnumFacing facing : EnumFacing.VALUES)
		{
			addQuad(from, to, facing, sprites.get(facing));
		}
	}

	public void addCube(float fromX, float fromY, float fromZ, float toX, float toY, float toZ, SpriteSet sprites)
	{
		addCube(new Vector3f(fromX, fromY, fromZ), new Vector3f(toX, toY, toZ), sprites);
	}

	public void addInvertedCube(Vector3f from, Vector3f to, SpriteSet sprites)
	{
		for (EnumFacing facing : EnumFacing.VALUES)
		{
			addQuad(to, from, facing, sprites.get(facing));
		}
	}

	public void addInvertedCube(float fromX, float fromY, float fromZ, float toX, float toY, float toZ, SpriteSet sprites)
	{
		addInvertedCube(new Vector3f(fromX, fromY, fromZ), new Vector3f(toX, toY, toZ), sprites);
	}

	private static float[] getUV(Vector3f from, Vector3f to, EnumFacing face)
	{
		switch (face)
		{
			case DOWN:
				return new float[] {from.x, 16F - to.z, to.x, 16F - from.z};
			case UP:
				return new float[] {from.x, from.z, to.x, to.z};
			case NORTH:
			default:
				return new float[] {16F - to.x, 16F - to.y, 16F - from.x, 16F - from.y};
			case SOUTH:
				return new float[] {from.x, 16F - to.y, to.x, 16F - from.y};
			case WEST:
				return new float[] {from.z, 16F - to.y, to.z, 16F - from.y};
			case EAST:
				return new float[] {16F - to.z, 16F - to.y, 16F - from.z, 16F - from.y};
		}
	}

	public void addQuad(Vector3f from, Vector3f to, EnumFacing face, @Nullable TextureAtlasSprite sprite)
	{
		if (sprite != null)
		{
			float[] uv = getUV(from, to, face);
			quads.add(BAKERY.makeBakedQuad(from, to, new BlockPartFace(face, tintIndex, "", new BlockFaceUV(uv, 0)), sprite, face, rotation, null, uvLocked, shade));
		}
	}

	public void addQuad(float fromX, float fromY, float fromZ, float toX, float toY, float toZ, EnumFacing face, @Nullable TextureAtlasSprite sprite)
	{
		addQuad(new Vector3f(fromX, fromY, fromZ), new Vector3f(toX, toY, toZ), face, sprite);
	}
}