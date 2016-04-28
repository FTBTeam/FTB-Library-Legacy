package ftb.lib.api.block;

import ftb.lib.api.item.IItemLM;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public interface IBlockLM extends IItemLM
{
	ItemBlock createItemBlock();
	String getUnlocalizedName(ItemStack stack);
	void loadTiles();
}