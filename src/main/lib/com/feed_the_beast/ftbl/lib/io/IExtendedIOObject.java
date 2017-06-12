package com.feed_the_beast.ftbl.lib.io;

import io.netty.buffer.ByteBuf;

/**
 * @author LatvianModder
 */
public interface IExtendedIOObject
{
	void writeData(ByteBuf data);

	void readData(ByteBuf data);
}