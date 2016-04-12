package ftb.lib.api.info.lines.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IChatComponent;

import java.util.List;

/**
 * Created by LatvianModder on 11.04.2016.
 */
public class InfoRecipe
{
	public static class InfoRecipeItem
	{
		public final boolean input;
		public int slot;
		public ItemStack item;
		public List<IChatComponent> info;
		public float chance;
		
		public InfoRecipeItem(boolean in)
		{
			input = in;
		}
	}
}