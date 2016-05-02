package ftb.lib.api.tile;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public enum RedstoneMode
{
	DISABLED("disabled"),
	ACTIVE_HIGH("high"),
	ACTIVE_LOW("low");
	
	public static final RedstoneMode[] VALUES = values();
	
	public final int ID;
	public final String uname;
	
	RedstoneMode(String s)
	{
		ID = ordinal();
		uname = s;
	}
	
	public boolean cancel(boolean b)
	{
		if(this == DISABLED) { return false; }
		if(this == ACTIVE_HIGH && !b) { return true; }
		return this == ACTIVE_LOW && b;
	}
	
	public RedstoneMode next()
	{ return VALUES[(ID + 1) % VALUES.length]; }
	
	public RedstoneMode prev()
	{
		int id = ID - 1;
		if(id < 0) { id = VALUES.length - 1; }
		return VALUES[id];
	}
	
	@SideOnly(Side.CLIENT)
	public String getText()
	{ return I18n.format("ftbl.redstonemode." + uname); }
	
	@SideOnly(Side.CLIENT)
	public String getTitle()
	{ return I18n.format("ftbl.redstonemode"); }
}