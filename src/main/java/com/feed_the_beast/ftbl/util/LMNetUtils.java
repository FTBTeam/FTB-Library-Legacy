package com.feed_the_beast.ftbl.util;

import com.google.gson.JsonElement;
import com.latmod.lib.io.ByteIOStream;
import com.latmod.lib.json.JsonElementIO;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.UUID;

/**
 * Created by LatvianModder on 31.07.2016.
 */
public class LMNetUtils
{
    public static void writePos(ByteBuf io, Vec3i pos)
    {
        io.writeInt(pos.getX());
        io.writeInt(pos.getY());
        io.writeInt(pos.getZ());
    }

    public static BlockPos readPos(ByteBuf io)
    {
        int x = io.readInt();
        int y = io.readInt();
        int z = io.readInt();
        return new BlockPos(x, y, z);
    }

    public static void writeUUID(ByteBuf io, UUID id)
    {
        io.writeLong(id.getMostSignificantBits());
        io.writeLong(id.getLeastSignificantBits());
    }

    public static UUID readUUID(ByteBuf io)
    {
        long msb = io.readLong();
        long lsb = io.readLong();
        return new UUID(msb, lsb);
    }

    public static void writeString(ByteBuf io, String s)
    {
        ByteBufUtils.writeUTF8String(io, s);
    }

    public static String readString(ByteBuf io)
    {
        return ByteBufUtils.readUTF8String(io);
    }

    public static void writeResourceLocation(ByteBuf io, ResourceLocation r)
    {
        writeString(io, r.getResourceDomain());
        writeString(io, r.getResourcePath());
    }

    public static ResourceLocation readResourceLocation(ByteBuf io)
    {
        String d = readString(io);
        String p = readString(io);
        return new ResourceLocation(d, p);
    }

    public static void writeTag(ByteBuf io, NBTTagCompound tag)
    {
        ByteBufUtils.writeTag(io, tag);
    }

    public static NBTTagCompound readTag(ByteBuf io)
    {
        return ByteBufUtils.readTag(io);
    }

    //TODO: Improve me
    public static void writeJsonElement(ByteBuf io, JsonElement e)
    {
        ByteIOStream stream = new ByteIOStream();
        JsonElementIO.write(stream, e);
        writeCompressedByteIOStream(io, stream);
    }

    //TODO: Improve me
    public static JsonElement readJsonElement(ByteBuf io)
    {
        return JsonElementIO.read(readCompressedByteIOStream(io));
    }

    public static void writeCompressedByteIOStream(ByteBuf io, ByteIOStream stream)
    {
        byte[] b = stream.toCompressedByteArray();
        io.writeInt(b.length);
        io.writeBytes(b, 0, b.length);
    }

    public static ByteIOStream readCompressedByteIOStream(ByteBuf io)
    {
        byte[] b = new byte[io.readInt()];
        io.readBytes(b, 0, b.length);
        ByteIOStream stream = new ByteIOStream();
        stream.setCompressedData(b);
        return stream;
    }
}
