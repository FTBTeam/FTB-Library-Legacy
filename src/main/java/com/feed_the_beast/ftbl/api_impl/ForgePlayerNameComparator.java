package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.api.IForgePlayer;

import java.util.Comparator;

public enum ForgePlayerNameComparator implements Comparator<IForgePlayer>
{
	INSTANCE;

	@Override
	public int compare(IForgePlayer o1, IForgePlayer o2)
	{
		return o1.getName().compareToIgnoreCase(o2.getName());
	}
}