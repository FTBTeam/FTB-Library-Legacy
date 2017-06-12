package com.feed_the_beast.ftbl.lib.reg;

/**
 * @author LatvianModder
 */
public class StringIDRegistry extends IDRegistry<String>
{
	@Override
	public String createStringFromKey(String s)
	{
		return s;
	}

	@Override
	public String createKeyFromString(String s)
	{
		return s;
	}
}
