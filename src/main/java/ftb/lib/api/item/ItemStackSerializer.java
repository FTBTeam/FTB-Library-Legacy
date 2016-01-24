package ftb.lib.api.item;

import com.google.gson.*;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;

public class ItemStackSerializer
{
	private static String getParseRegex(String s)
	{
		if(s.indexOf(' ') != -1) return " ";
		else if(s.indexOf(';') != -1) return ";";
		else if(s.indexOf('@') != -1) return "@";
		else return " x ";
	}
	
	public static ItemStack parseItem(String s)
	{
		if(s == null) return null;
		s = s.trim();
		if(s.isEmpty()) return null;
		
		try
		{
			String[] s1 = s.split(getParseRegex(s));
			if(s1.length <= 0) return null;
			
			Item item = LMInvUtils.getItemFromRegName(new ResourceLocation(s1[0]));
			if(item == null) return null;
			int dmg = 0;
			int size = 1;
			
			if(s1.length == 2) dmg = (s1[1].charAt(0) == '*') ? ODItems.ANY : Integer.parseInt(s1[1]);
			else if(s1.length >= 3)
			{
				size = Integer.parseInt(s1[1]);
				dmg = (s1[2].charAt(0) == '*') ? ODItems.ANY : Integer.parseInt(s1[2]);
			}
			/*else if(s1.length >= 4)
			{
				String tagS = LMStringUtils.unsplitSpaceUntilEnd(3, s1);
				NBTTagCompound tag = (NBTTagCompound)JsonToNBT.func_150315_a(tagS);
				
				if(tag != null)
				{
					ItemStack is = new ItemStack(item, size, dmg);
					is.setTagCompound(tag);
					return is;
				}
			}*/
			
			return new ItemStack(item, size, dmg);
		}
		catch(Exception e) { }
		return null;
	}
	
	public static String toString(ItemStack is)
	{
		if(is == null) return null;
		StringBuilder sb = new StringBuilder();
		sb.append(LMInvUtils.getRegName(is));
		sb.append(' ');
		sb.append(is.stackSize);
		sb.append(' ');
		sb.append(is.getItemDamage());
		return sb.toString();
	}
	
	public static JsonElement serialize(ItemStack is)
	{
		if(is == null || is.getItem() == null) return null;
		return new JsonPrimitive(toString(is));
	}
	
	public static ItemStack deserialize(JsonElement e)
	{
		if(e == null) return null;
		else if(e.isJsonPrimitive()) return parseItem(e.getAsString());
		return null;
	}
}