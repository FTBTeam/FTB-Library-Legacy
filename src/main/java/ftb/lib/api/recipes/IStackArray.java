package ftb.lib.api.recipes;

import net.minecraft.item.ItemStack;

public interface IStackArray
{
	boolean matches(ItemStack[] ai);
	IStackArray[] getItems();
}