package com.feed_the_beast.ftbl.lib.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.model.TRSRTransformation;
import org.lwjgl.util.vector.Vector3f;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LatvianModder on 03.01.2017.
 */
public class ModelBuilder
{
    private static final FaceBakery BAKERY = new FaceBakery();
    public static final ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> TRANSFORM_MAP;

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

    public static ModelRotation getRotation(EnumFacing facing)
    {
        switch(facing)
        {
            case SOUTH:
                return ModelRotation.X0_Y180;
            case EAST:
                return ModelRotation.X0_Y90;
            case WEST:
                return ModelRotation.X0_Y270;
            default:
                return ModelRotation.X0_Y0;
        }
    }

    private List<BakedQuad> quads;
    private ModelRotation rotation;

    public ModelBuilder(ModelRotation r)
    {
        quads = new ArrayList<>();
        rotation = r;
    }

    public void setRotation(ModelRotation r)
    {
        rotation = r;
    }

    public List<BakedQuad> getQuads()
    {
        return new ArrayList<>(quads);
    }

    public void addCube(float fromX, float fromY, float fromZ, float toX, float toY, float toZ, SpriteSet sprites)
    {
        for(EnumFacing facing : EnumFacing.VALUES)
        {
            addQuad(fromX, fromY, fromZ, toX, toY, toZ, facing, sprites.get(facing));
        }
    }

    public void addInvertedCube(float fromX, float fromY, float fromZ, float toX, float toY, float toZ, SpriteSet sprites)
    {
        for(EnumFacing facing : EnumFacing.VALUES)
        {
            addQuad(toX, toY, toZ, fromX, fromY, fromZ, facing, sprites.get(facing));
        }
    }

    public float[] getUV(float fromX, float fromY, float fromZ, float toX, float toY, float toZ, EnumFacing face)
    {
        switch(face)
        {
            case DOWN:
            case UP:
                return new float[] {fromX, fromZ, toX, toZ};
            case NORTH:
            case SOUTH:
                return new float[] {fromX, fromY, toX, toY};
            case EAST:
            case WEST:
                return new float[] {fromZ, fromY, toZ, toY};
        }

        return new float[] {0F, 0F, 1F, 1F};
    }

    public void addQuad(float fromX, float fromY, float fromZ, float toX, float toY, float toZ, EnumFacing face, @Nullable TextureAtlasSprite sprite)
    {
        if(sprite != null)
        {
            float[] uv = getUV(fromX, fromY, fromZ, toX, toY, toZ, face);
            quads.add(BAKERY.makeBakedQuad(new Vector3f(fromX, fromY, fromZ), new Vector3f(toX, toY, toZ), new BlockPartFace(face, -1, "", new BlockFaceUV(uv, 0)), sprite, face, rotation, null, true, true));
        }
    }
}