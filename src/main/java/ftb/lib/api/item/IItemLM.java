package ftb.lib.api.item;

public interface IItemLM
{
	String getItemID();
	void onPostLoaded();
	void loadRecipes();
}