package ftb.lib.api.paint;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface ICustomPaintBlock
{
	Paint getCustomPaint(IBlockAccess w, BlockPos pos, IBlockState state);
}