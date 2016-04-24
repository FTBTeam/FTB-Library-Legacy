package ftb.lib.api.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemBlockLM extends ItemBlock
{
	public ItemBlockLM(Block b)
	{
		super(b);
		setHasSubtypes(true);
		setMaxDamage(0);
	}
	
	@Override
	public int getMetadata(int m)
	{ return m; }
}