package com.feed_the_beast.ftbl.api.tile;

public enum InvMode
{
	ENABLED("enabled"),
	ONLY_IN("onlyin"),
	ONLY_OUT("onlyout"),
	DISABLED("disabled");
	
	public static final InvMode[] VALUES = values();
	public static final String enumLangKey = "ftbl.invmode";
	
	public final String langKey;
	
	InvMode(String s)
	{
		langKey = enumLangKey + '.' + s;
	}
	
	public InvMode next()
	{ return VALUES[(ordinal() + 1) % VALUES.length]; }
	
	public InvMode prev()
	{
		int id = ordinal() - 1;
		if(id < 0) { id = VALUES.length - 1; }
		return VALUES[id];
	}
	
	public boolean canInsertItem()
	{ return this == ENABLED || this == ONLY_IN; }
	
	public boolean canExtractItem()
	{ return this == ENABLED || this == ONLY_OUT; }
}