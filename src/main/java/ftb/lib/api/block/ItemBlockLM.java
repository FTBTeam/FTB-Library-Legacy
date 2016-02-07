package ftb.lib.api.block;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraftforge.fml.relauncher.*;

import java.util.List;

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
	
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack is, EntityPlayer ep, List<String> l, boolean adv)
	{ blockLM.addInformation(is, ep, l, adv); }
}