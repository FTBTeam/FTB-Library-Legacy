package ftb.lib.api.item;

import latmod.lib.IIDObject;
import net.minecraft.item.Item;

public interface IItemLM extends IIDObject
{
	void onPostLoaded();
	void loadRecipes();
	Item getItem();
}