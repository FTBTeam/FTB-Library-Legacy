package com.feed_the_beast.ftbl.lib.io;

import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.NameMap;
import com.feed_the_beast.ftbl.lib.math.BlockDimPos;
import com.feed_the_beast.ftbl.lib.util.JsonElementIO;
import com.feed_the_beast.ftbl.lib.util.JsonUtils;
import com.google.gson.JsonElement;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author LatvianModder
 */
public class DataOut
{
	public interface Serializer<T>
	{
		void write(DataOut data, T object);
	}

	public static Serializer<String> STRING = DataOut::writeString;
	public static Serializer<Integer> INT = DataOut::writeInt;
	public static Serializer<UUID> UUID = DataOut::writeUUID;
	public static Serializer<BlockPos> BLOCK_POS = DataOut::writePos;
	public static Serializer<BlockDimPos> BLOCK_DIM_POS = DataOut::writeDimPos;

	public static final DataOut.Serializer<ChunkPos> CHUNK_POS = (data, pos) ->
	{
		data.writeInt(pos.x);
		data.writeInt(pos.z);
	};

	private final ByteBuf byteBuf;

	public DataOut(ByteBuf io)
	{
		byteBuf = io;
	}

	public void writeBoolean(boolean value)
	{
		byteBuf.writeBoolean(value);
	}

	public void writeByte(int value)
	{
		byteBuf.writeByte(value);
	}

	public void writeBytes(byte[] bytes, int off, int len)
	{
		byteBuf.writeBytes(bytes, off, len);
	}

	public void writeBytes(byte[] bytes)
	{
		writeBytes(bytes, 0, bytes.length);
	}

	public void writeShort(int value)
	{
		byteBuf.writeShort(value);
	}

	public void writeInt(int value)
	{
		byteBuf.writeInt(value);
	}

	public void writeLong(long value)
	{
		byteBuf.writeLong(value);
	}

	public void writeFloat(float value)
	{
		byteBuf.writeFloat(value);
	}

	public void writeDouble(double value)
	{
		byteBuf.writeDouble(value);
	}

	public void writePos(Vec3i pos)
	{
		writeInt(pos.getX());
		writeInt(pos.getY());
		writeInt(pos.getZ());
	}

	public void writeDimPos(BlockDimPos pos)
	{
		writeInt(pos.dim);
		writeInt(pos.posX);
		writeInt(pos.posY);
		writeInt(pos.posZ);
	}

	public void writeUUID(UUID id)
	{
		writeLong(id.getMostSignificantBits());
		writeLong(id.getLeastSignificantBits());
	}

	public void writeString(String string)
	{
		ByteBufUtils.writeUTF8String(byteBuf, string);
	}

	public <T> void writeCollection(Collection<T> collection, Serializer<T> serializer)
	{
		int size = collection.size();

		if (size == 0)
		{
			writeByte(6);
			return;
		}

		boolean set = collection instanceof Set;

		if (size >= 65536)
		{
			writeByte(set ? 5 : 2);
			writeInt(size);
		}
		else if (size >= 256)
		{
			writeByte(set ? 4 : 1);
			writeShort(size);
		}
		else
		{
			writeByte(set ? 3 : 0);
			writeByte(size);
		}

		for (T object : collection)
		{
			serializer.write(this, object);
		}
	}

	public <K, V> void writeMap(Map<K, V> map, Serializer<K> keySerializer, Serializer<V> valueSerializer)
	{
		int size = map.size();

		if (size == 0)
		{
			writeByte(3);
			return;
		}
		else if (size >= 65536)
		{
			writeByte(2);
			writeInt(size);
		}
		else if (size >= 256)
		{
			writeByte(1);
			writeShort(size);
		}
		else
		{
			writeByte(0);
			writeByte(size);
		}

		for (Map.Entry<K, V> entry : map.entrySet())
		{
			keySerializer.write(this, entry.getKey());
			valueSerializer.write(this, entry.getValue());
		}
	}

	public void writeItemStack(ItemStack stack)
	{
		ByteBufUtils.writeItemStack(byteBuf, stack);
	}

	public void writeNBT(@Nullable NBTTagCompound nbt)
	{
		ByteBufUtils.writeTag(byteBuf, nbt);
	}

	public void writeResourceLocation(ResourceLocation r)
	{
		writeString(r.toString());
	}

	public void writeJson(JsonElement element)
	{
		JsonElementIO.write(byteBuf, element);
	}

	public void writeTextComponent(@Nullable ITextComponent component)
	{
		writeJson(JsonUtils.serializeTextComponent(component));
	}

	public void writeProfile(GameProfile profile)
	{
		writeUUID(profile.getId());
		writeString(profile.getName());
	}

	public void writeBlockState(IBlockState state)
	{
		writeInt(state == Blocks.AIR.getDefaultState() ? 0 : Block.getStateId(state));
	}

	public void writeColor(@Nullable Color4I color)
	{
		writeJson((color == null ? Color4I.NONE : color).toJson());
	}

	public <E> void write(NameMap<E> map, E value)
	{
		int index = map.getIndex(value);

		if (map.values.size() >= 256)
		{
			writeShort(index);
		}
		else
		{
			writeByte(index);
		}
	}
}