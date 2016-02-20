package ftb.lib;

import ftb.lib.api.gui.GuiIcons;
import net.minecraft.client.resources.I18n;

public enum PrivacyLevel
{
	PUBLIC("public"),
	PRIVATE("private"),
	FRIENDS("friends");
	
	public static final PrivacyLevel[] VALUES_3 = new PrivacyLevel[] {PUBLIC, PRIVATE, FRIENDS};
	public static final PrivacyLevel[] VALUES_2 = new PrivacyLevel[] {PUBLIC, PRIVATE};
	
	public final int ID;
	public final String uname;
	
	PrivacyLevel(String s)
	{
		ID = ordinal();
		uname = s;
	}
	
	public boolean isPublic()
	{ return this == PUBLIC; }
	
	public boolean isRestricted()
	{ return this == FRIENDS; }
	
	public PrivacyLevel next(PrivacyLevel[] l)
	{ return l[(ID + 1) % l.length]; }
	
	public PrivacyLevel prev(PrivacyLevel[] l)
	{
		int id = ID - 1;
		if(id < 0) id = l.length - 1;
		return l[id];
	}
	
	public String getText()
	{ return I18n.format("ftbl.security." + uname); }
	
	public String getTitle()
	{ return I18n.format("ftbl.security"); }
	
	public TextureCoords getIcon()
	{ return GuiIcons.security[ID]; }
	
	public static String[] getNames()
	{
		String[] s = new String[VALUES_3.length];
		for(int i = 0; i < VALUES_3.length; i++)
			s[i] = VALUES_3[i].uname;
		return s;
	}
	
	public static PrivacyLevel get(String s)
	{
		for(PrivacyLevel l : VALUES_3)
		{
			if(l.uname.equalsIgnoreCase(s)) return l;
		}
		return null;
	}
}