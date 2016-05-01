package ftb.lib.api.tile;

import ftb.lib.BlockDimPos;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by LatvianModder on 12.03.2016.
 */
public interface ITileEntity
{
	TileEntity getTile();
	BlockDimPos getDimPos();
}
