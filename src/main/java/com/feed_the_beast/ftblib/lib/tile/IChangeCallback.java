package com.feed_the_beast.ftblib.lib.tile;

/**
 * @author LatvianModder
 */
@FunctionalInterface
public interface IChangeCallback
{
	void onContentsChanged(boolean majorChange);
}