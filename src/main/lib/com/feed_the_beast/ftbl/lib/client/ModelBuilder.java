package com.feed_the_beast.ftbl.lib.client;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.common.model.TRSRTransformation;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.util.vector.Vector3f;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author LatvianModder
 */
public class ModelBuilder
{
	private static final FaceBakery BAKERY = new FaceBakery();
	private static final ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> TRANSFORM_MAP;

	private static TRSRTransformation get(float tx, float ty, float tz, float ax, float ay, float az, float s)
	{
		return TRSRTransformation.blockCenterToCorner(new TRSRTransformation(new javax.vecmath.Vector3f(tx / 16F, ty / 16F, tz / 16F), TRSRTransformation.quatFromXYZDegrees(new javax.vecmath.Vector3f(ax, ay, az)), new javax.vecmath.Vector3f(s, s, s), null));
	}

	static
	{
		TRSRTransformation thirdperson = get(0, 2.5F, 0, 75, 45, 0, 0.375F);
		TRSRTransformation flipX = new TRSRTransformation(null, null, new javax.vecmath.Vector3f(-1, 1, 1), null);
		ImmutableMap.Builder<ItemCameraTransforms.TransformType, TRSRTransformation> builder = ImmutableMap.builder();
		builder.put(ItemCameraTransforms.TransformType.GUI, get(0, 0, 0, 30, 225, 0, 0.625F));
		builder.put(ItemCameraTransforms.TransformType.GROUND, get(0, 3, 0, 0, 0, 0, 0.25F));
		builder.put(ItemCameraTransforms.TransformType.FIXED, get(0, 0, 0, 0, 0, 0, 0.5F));
		builder.put(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, thirdperson);
		builder.put(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, TRSRTransformation.blockCenterToCorner(flipX.compose(TRSRTransformation.blockCornerToCenter(thirdperson)).compose(flipX)));
		builder.put(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, get(0, 0, 0, 0, 45, 0, 0.4F));
		builder.put(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, get(0, 0, 0, 0, 225, 0, 0.4F));
		TRANSFORM_MAP = Maps.immutableEnumMap(builder.build());
	}

	public static Pair<? extends IBakedModel, Matrix4f> handlePerspective(IBakedModel model, ItemCameraTransforms.TransformType cameraTransformType)
	{
		return PerspectiveMapWrapper.handlePerspective(model, TRANSFORM_MAP, cameraTransformType);
	}

	public static final Function<ResourceLocation, TextureAtlasSprite> DEFAULT_TEXTURE_GETTER = location -> FTBLibClient.MC.getTextureMapBlocks().registerSprite(location);

	public final VertexFormat format;
	private List<BakedQuad> quads;
	private ModelRotation rotation;
	private boolean uvLocked = true;
	private boolean shade = true;

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
			quads.add(BAKERY.makeBakedQuad(from, to, new BlockPartFace(face, -1, "", new BlockFaceUV(uv, 0)), sprite, face, rotation, null, uvLocked, shade));
		}
	}

	public void addQuad(float fromX, float fromY, float fromZ, float toX, float toY, float toZ, EnumFacing face, @Nullable TextureAtlasSprite sprite)
	{
		addQuad(new Vector3f(fromX, fromY, fromZ), new Vector3f(toX, toY, toZ), face, sprite);
	}
}