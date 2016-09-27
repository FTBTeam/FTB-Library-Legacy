package com.feed_the_beast.ftbl.lib.math;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by LatvianModder on 29.04.2016.
 */
@SideOnly(Side.CLIENT)
public class BlockStateSerializer extends DefaultStateMapper
{
    public static final BlockStateSerializer INSTANCE = new BlockStateSerializer();

    private BlockStateSerializer()
    {
    }

    public ModelResourceLocation get(IBlockState state)
    {
        ResourceLocation id = Block.REGISTRY.getNameForObject(state.getBlock());
        return new ModelResourceLocation(new ResourceLocation(id.getResourceDomain(), "blocks/" + id.getResourcePath()), getPropertyString(state.getProperties()));
    }
}
