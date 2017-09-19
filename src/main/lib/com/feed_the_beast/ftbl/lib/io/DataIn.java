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
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author LatvianModder
 */
public class DataIn
{
	public interface Deserializer<T>
	{
		T read(DataIn data);
	}

	public static final Deserializer<String> STRING = DataIn::readString;
	public static final Deserializer<Integer> INT = DataIn::readInt;
	public static final Deserializer<UUID> UUID = DataIn::readUUID;
	public static final Deserializer<BlockPos> BLOCK_POS = DataIn::readPos;
	public static final Deserializer<BlockDimPos> BLOCK_DIM_POS = DataIn::readDimPos;

	public static final DataIn.Deserializer<ChunkPos> CHUNK_POS = data ->
	{
		int x = data.readInt();
		int z = data.readInt();
		return new ChunkPos(x, z);
	};

	private final ByteBuf byteBuf;

	public DataIn(ByteBuf io)
	{
		byteBuf = io;
	}

	public boolean readBoolean()
	{
		return byteBuf.readBoolean();
	}

	public byte readByte()
	{
		return byteBuf.readByte();
	}

	public void readBytes(byte[] bytes, int off, int len)
	{
		byteBuf.readBytes(bytes, off, len);
	}

	public void readBytes(byte[] bytes)
	{
		readBytes(bytes, 0, bytes.length);
	}

	public short readUnsignedByte()
	{
		return byteBuf.readUnsignedByte();
	}

	public short readShort()
	{
		return byteBuf.readShort();
	}

	public int readUnsignedShort()
	{
		return byteBuf.readUnsignedShort();
	}

	public int readInt()
	{
		return byteBuf.readInt();
	}

	public long readLong()
	{
		return byteBuf.readLong();
	}

	public float readFloat()
	{
		return byteBuf.readFloat();
	}

	public double readDouble()
	{
		return byteBuf.readDouble();
	}

	public String readString()
	{
		return ByteBufUtils.readUTF8String(byteBuf);
	}

	public <T> Collection<T> readCollection(@Nullable Collection<T> collection, Deserializer<T> deserializer)
	{
		if (collection != null)
		{
			collection.clear();
		}

		int id = readUnsignedByte();

		if (id == 6)
		{
			return collection == null ? Collections.emptyList() : collection;
		}

		int size;

		switch (id % 3)
		{
			case 0:
				size = readUnsignedByte();
				break;
			case 1:
				size = readUnsignedShort();
				break;
			default:
				size = readInt();
		}

		if (collection == null)
		{
			collection = (id / 3 == 0) ? new ArrayList<>(size) : new HashSet<>(size);
		}

		while (--size >= 0)
		{
			collection.add(deserializer.read(this));
		}

		return collection;
	}

	public <T> Collection<T> readCollection(Deserializer<T> deserializer)
	{
		return readCollection(null, deserializer);
	}

	public <K, V> Map<K, V> readMap(@Nullable Map<K, V> map, Deserializer<K> keyDeserializer, Deserializer<V> valueDeserializer)
	{
		int id = readUnsignedByte();

		if (id == 3)
		{
			return Collections.emptyMap();
		}

		int size;

		switch (id % 3)
		{
			case 0:
				size = readUnsignedByte();
				break;
			case 1:
				size = readUnsignedShort();
				break;
			default:
				size = readInt();
		}

		if (map == null)
		{
			map = new HashMap<>(size);
		}

		while (--size >= 0)
		{
			K key = keyDeserializer.read(this);
			V value = valueDeserializer.read(this);
			map.put(key, value);
		}

		return map;
	}

	public ItemStack readItemStack()
	{
		return ByteBufUtils.readItemStack(byteBuf);
	}

	@Nullable
	public NBTTagCompound readNBT()
	{
		return ByteBufUtils.readTag(byteBuf);
	}

	public <T extends IForgeRegistryEntry<T>> T readRegistryEntry(IForgeRegistry<T> registry)
	{
		return ByteBufUtils.readRegistryEntry(byteBuf, registry);
	}

	public <T extends IForgeRegistryEntry<T>> List<T> readRegistryEntries(IForgeRegistry<T> registry)
	{
		return ByteBufUtils.readRegistryEntries(byteBuf, registry);
	}

	public BlockPos readPos()
	{
		int x = readInt();
		int y = readInt();
		int z = readInt();
		return new BlockPos(x, y, z);
	}

	public BlockPos.MutableBlockPos readMutablePos()
	{
		int x = readInt();
		int y = readInt();
		int z = readInt();
		return new BlockPos.MutableBlockPos(x, y, z);
	}

	public BlockDimPos readDimPos()
	{
		int d = readInt();
		int x = readInt();
		int y = readInt();
		int z = readInt();
		return new BlockDimPos(x, y, z, d);
	}

	public UUID readUUID()
	{
		long msb = readLong();
		long lsb = readLong();
		return new UUID(msb, lsb);
	}

	public ResourceLocation readResourceLocation()
	{
		return new ResourceLocation(readString());
	}

	public JsonElement readJson()
	{
		return JsonElementIO.read(byteBuf);
	}

	@Nullable
	public ITextComponent readTextComponent()
	{
		return JsonUtils.deserializeTextComponent(readJson());
	}

	public GameProfile readProfile()
	{
		UUID id = readUUID();
		return new GameProfile(id, readString());
	}

	public IBlockState readBlockState()
	{
		int id = readInt();
		return id == 0 ? Blocks.AIR.getDefaultState() : Block.getStateById(id);
	}

	public Color4I readColor()
	{
		return Color4I.fromJson(readJson());
	}

	public <E> E read(NameMap<E> map)
	{
		return map.get(map.values.size() >= 256 ? readUnsignedShort() : readUnsignedByte());
	}
}