package com.feed_the_beast.ftblib.lib.item;

/**
 * @author LatvianModder
 */
public class SetStackInSlotException extends RuntimeException
{
	public SetStackInSlotException(String mod)
	{
		super("Do not use setStackInSlot method! This is not " + mod + " fault.");
	}
}