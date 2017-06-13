package com.feed_the_beast.ftbl.api;

import net.minecraftforge.common.util.INBTSerializable;

/**
 * @author LatvianModder
 */
public interface IDataProvider<T>
{
	INBTSerializable<?> getData(T owner);
}