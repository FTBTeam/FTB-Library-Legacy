package ftb.lib.api.block;

import net.minecraft.block.Block;
import net.minecraft.item.*;

public class ItemBlockLM extends ItemBlock
{
	public IBlockLM blockLM;
	
	public ItemBlockLM(Block b)
	{
		super(b);
		setHasSubtypes(true);
		setMaxDamage(0);
		
		blockLM = (IBlockLM) b;
	}
	
	public int getMetadata(int m)
	{ return m; }
	
	public String getUnlocalizedName(ItemStack stack)
	{
		if(blockLM instanceof BlockLM) return ((BlockLM) blockLM).getUnlocalizedName(stack.getItemDamage());
		return getUnlocalizedName();
	}
}