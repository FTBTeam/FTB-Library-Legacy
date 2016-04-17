package ftb.lib.api.paint;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface INoPaintBlock
{
	boolean hasPaint(IBlockAccess iba, BlockPos pos, IBlockState state, EnumFacing s);
}