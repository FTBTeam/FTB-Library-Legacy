package ftb.lib.api.item;

import latmod.lib.IIDObject;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IItemLM extends IIDObject
{
	Item getItem();
	void onPostLoaded();
	void loadRecipes();
	
	@SideOnly(Side.CLIENT)
	void loadModels();
}