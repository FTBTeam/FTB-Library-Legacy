package com.latmod.lib.io;

import io.netty.buffer.ByteBuf;

/**
 * Created by LatvianModder on 12.09.2016.
 */
public interface IExtendedIOObject
{
    void writeData(ByteBuf data, boolean extended);

    void readData(ByteBuf data, boolean extended);
}