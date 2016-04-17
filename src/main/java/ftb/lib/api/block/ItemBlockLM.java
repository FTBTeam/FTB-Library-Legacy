package ftb.lib.api.block;

import net.minecraft.block.Block;
import net.minecraft.item.*;

public class ItemBlockLM extends ItemBlock
{
	public final IBlockLM blockLM;
	
	public ItemBlockLM(IBlockLM b)
	{
		super((Block) b);
		blockLM = b;
		setHasSubtypes(true);
		setMaxDamage(0);
	}
	
	public int getMetadata(int m)
	{ return m; }
	
	public String getUnlocalizedName(ItemStack stack)
	{ return blockLM.getUnlocalizedName(stack); }
}