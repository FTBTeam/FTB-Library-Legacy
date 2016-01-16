package ftb.lib.api.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.*;

import java.util.List;

public interface IBlockLM
{
	Class<? extends ItemBlockLM> getItemBlock();
	String getItemID();
	void onPostLoaded();
	void loadRecipes();
	
	@SideOnly(Side.CLIENT)
	void addInformation(ItemStack is, EntityPlayer ep, List<String> l, boolean adv);
}