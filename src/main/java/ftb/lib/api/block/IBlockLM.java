package ftb.lib.api.block;

import ftb.lib.api.item.IItemLM;
import net.minecraft.item.ItemBlock;

public interface IBlockLM extends IItemLM
{
	ItemBlock createItemBlock();
	void loadTiles();
}