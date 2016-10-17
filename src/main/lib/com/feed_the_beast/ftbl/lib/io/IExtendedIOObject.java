package com.feed_the_beast.ftbl.lib.io;

import io.netty.buffer.ByteBuf;

/**
 * Created by LatvianModder on 12.09.2016.
 */
public interface IExtendedIOObject
{
    void writeToServer(ByteBuf data);

    void readFromServer(ByteBuf data);
}