package com.latmod.lib.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by LatvianModder on 12.09.2016.
 */
public interface IExtendedIOObject
{
    void writeData(DataOutput data, boolean extended) throws IOException;

    void readData(DataInput data, boolean extended) throws IOException;
}