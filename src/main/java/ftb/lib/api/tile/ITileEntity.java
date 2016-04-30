package ftb.lib.api.tile;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.DimensionType;

/**
 * Created by LatvianModder on 12.03.2016.
 */
public interface ITileEntity
{
	TileEntity getTile();
	DimensionType getDimension();
}
