package com.feed_the_beast.ftbl.api;

import net.minecraft.util.text.ITextComponent;

import java.util.UUID;

/**
 * @author LatvianModder
 */
public interface ITeamMessage extends Comparable<ITeamMessage>
{
	UUID getSender();

	long getTime();

	ITextComponent getMessage();
}