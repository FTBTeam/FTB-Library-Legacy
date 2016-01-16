package ftb.lib.api.tile;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.*;

public enum InvMode
{
	ENABLED("enabled"),
	ONLY_IN("onlyin"),
	ONLY_OUT("onlyout"),
	DISABLED("disabled");
	
	public static final InvMode[] VALUES = values();
	
	public final int ID;
	public final String uname;
	
	InvMode(String s)
	{
		ID = ordinal();
		uname = s;
	}
	
	public InvMode next()
	{ return VALUES[(ID + 1) % VALUES.length]; }
	
	public InvMode prev()
	{
		int id = ID - 1;
		if(id < 0) id = VALUES.length - 1;
		return VALUES[id];
	}
	
	public boolean canInsertItem()
	{ return this == ENABLED || this == ONLY_IN; }
	
	public boolean canExtractItem()
	{ return this == ENABLED || this == ONLY_OUT; }
	
	@SideOnly(Side.CLIENT)
	public String getText()
	{ return I18n.format("ftbl.invmode." + uname); }
	
	@SideOnly(Side.CLIENT)
	public String getTitle()
	{ return I18n.format("ftbl.invmode"); }
}