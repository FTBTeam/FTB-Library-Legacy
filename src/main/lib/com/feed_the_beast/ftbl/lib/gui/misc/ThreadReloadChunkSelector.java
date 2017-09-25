package com.feed_the_beast.ftbl.lib.gui.misc;

import com.feed_the_beast.ftbl.lib.client.ClientUtils;
import com.feed_the_beast.ftbl.lib.client.PixelBuffer;
import com.feed_the_beast.ftbl.lib.math.MathUtils;
import com.feed_the_beast.ftbl.lib.util.ColorUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ThreadReloadChunkSelector extends Thread
{
	private static ByteBuffer pixelBuffer = null;
	private static final int PIXEL_SIZE = ChunkSelectorMap.TILES_TEX * 16;
	private static final PixelBuffer PIXELS = new PixelBuffer(PIXEL_SIZE, PIXEL_SIZE);
	private static final Map<IBlockState, Integer> COLOR_CACHE = new HashMap<>();
	private static final BlockPos.MutableBlockPos CURRENT_BLOCK_POS = new BlockPos.MutableBlockPos(0, 0, 0);
	private static World world = null;
	private static final Function<IBlockState, Integer> COLOR_GETTER = state1 -> 0xFF000000 | getBlockColor0(state1, world, CURRENT_BLOCK_POS);
	private static ThreadReloadChunkSelector instance;
	private static int textureID = -1;
	private static final int[] HEIGHT_MAP = new int[PIXEL_SIZE * PIXEL_SIZE];

	public static int getTextureID()
	{
		if (textureID == -1)
		{
			textureID = TextureUtil.glGenTextures();
		}

		return textureID;
	}

	public static void updateTexture()
	{
		if (pixelBuffer != null)
		{
			//boolean hasBlur = false;
			//int filter = hasBlur ? GL11.GL_LINEAR : GL11.GL_NEAREST;
			GlStateManager.bindTexture(getTextureID());
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, ChunkSelectorMap.TILES_TEX * 16, ChunkSelectorMap.TILES_TEX * 16, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixelBuffer);
			pixelBuffer = null;
		}
	}

	public static void reloadArea(World w, int sx, int sz)
	{
		if (instance != null)
		{
			instance.cancelled = true;
			instance = null;
		}

		instance = new ThreadReloadChunkSelector(w, sx, sz);
		instance.cancelled = false;
		instance.start();
		COLOR_CACHE.clear();
	}

	public static boolean isReloading()
	{
		return instance != null && !instance.cancelled;
	}

	private final int startX, startZ;
	private boolean cancelled = false;

	private ThreadReloadChunkSelector(World w, int sx, int sz)
	{
		super("ChunkSelectorAreaReloader");
		setDaemon(true);
		world = w;
		startX = sx;
		startZ = sz;
	}

	private static int getBlockColor0(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		Block b = state.getBlock();

		if (b == Blocks.SANDSTONE)
		{
			return MapColor.SAND.colorValue;
		}
		else if (b == Blocks.FIRE)
		{
			return MapColor.RED.colorValue;
		}
		else if (b == Blocks.YELLOW_FLOWER)
		{
			return MapColor.YELLOW.colorValue;
		}
		else if (b == Blocks.LAVA)
		{
			return MapColor.ADOBE.colorValue;
		}
		else if (b == Blocks.END_STONE)
		{
			return MapColor.SAND.colorValue;
		}
		else if (b == Blocks.OBSIDIAN)
		{
			return 0x150047;
		}
		else if (b == Blocks.GRAVEL)
		{
			return 0x8D979B;
		}
		else if (b == Blocks.GRASS)
		{
			return 0x549954;
		}
		else if (b == Blocks.TORCH)
		{
			return 0xFFA530;
		}
		//else if(b.getMaterial(state) == Material.WATER)
		//	return ColorUtils.multiply(MapColor.waterColor.colorValue, b.colorMultiplier(world, pos), 200);
		else if (b == Blocks.RED_FLOWER)
		{
			switch (state.getValue(Blocks.RED_FLOWER.getTypeProperty()))
			{
				case DANDELION:
					return MapColor.YELLOW.colorValue;
				case POPPY:
					return MapColor.RED.colorValue;
				case BLUE_ORCHID:
					return MapColor.LIGHT_BLUE.colorValue;
				case ALLIUM:
					return MapColor.MAGENTA.colorValue;
				case HOUSTONIA:
					return MapColor.SILVER.colorValue;
				case RED_TULIP:
					return MapColor.RED.colorValue;
				case ORANGE_TULIP:
					return MapColor.ADOBE.colorValue;
				case WHITE_TULIP:
					return MapColor.SNOW.colorValue;
				case PINK_TULIP:
					return MapColor.PINK.colorValue;
				case OXEYE_DAISY:
					return MapColor.SILVER.colorValue;
			}
		}
		else if (b == Blocks.PLANKS)
		{
			switch (state.getValue(BlockPlanks.VARIANT))
			{
				case OAK:
					return 0xC69849;
				case SPRUCE:
					return 0x7C5E2E;
				case BIRCH:
					return 0xF2E093;
				case JUNGLE:
					return 0xC67653;
				case ACACIA:
					return 0xE07F3E;
				case DARK_OAK:
					return 0x512D14;
			}
		}

		//if(b == Blocks.leaves || b == Blocks.vine || b == Blocks.waterlily)
		//	return ColorUtils.addBrightness(b.colorMultiplier(world, pos), -40);
		//else if(b == Blocks.grass && state.getValue(BlockGrass.SNOWY))
		//	return ColorUtils.addBrightness(b.colorMultiplier(world, pos), -15);

		return state.getMapColor(world, pos).colorValue;
	}

	private static int getHeight(int x, int z)
	{
		int index = x + z * PIXEL_SIZE;
		return index < 0 || index >= HEIGHT_MAP.length ? -1 : HEIGHT_MAP[index];
	}

	@Override
	public void run()
	{
		Arrays.fill(PIXELS.getPixels(), 0);
		Arrays.fill(HEIGHT_MAP, -1);
		pixelBuffer = ColorUtils.toByteBuffer(PIXELS.getPixels(), false);

		Chunk chunk;
		int cx, cz, x, z, wi, wx, wz, by, color, topY;
		IBlockState state;

		int startY = ClientUtils.MC.player.getPosition().getY();

		try
		{
			for (cz = 0; cz < ChunkSelectorMap.TILES_GUI; cz++)
			{
				for (cx = 0; cx < ChunkSelectorMap.TILES_GUI; cx++)
				{
					chunk = world.getChunkProvider().getLoadedChunk(startX + cx, startZ + cz);

					if (chunk != null)
					{
						x = (startX + cx) << 4;
						z = (startZ + cz) << 4;
						topY = (world.provider.getDimension() == -1) ? startY + 30 : Math.max(255, chunk.getTopFilledSegment() + 15);

						for (wi = 0; wi < 256; wi++)
						{
							wx = wi % 16;
							wz = wi / 16;

							for (by = topY; by > 0; --by)
							{
								if (cancelled)
								{
									return;
								}

								CURRENT_BLOCK_POS.setPos(x + wx, by, z + wz);
								state = chunk.getBlockState(wx, by, wz);

								if (state.getBlock() != Blocks.TALLGRASS && !state.getBlock().isAir(state, world, CURRENT_BLOCK_POS))
								{
									HEIGHT_MAP[(cx * 16 + wx) + (cz * 16 + wz) * PIXEL_SIZE] = by;
									break;
								}
							}
						}
					}
				}
			}

			for (cz = 0; cz < ChunkSelectorMap.TILES_GUI; cz++)
			{
				for (cx = 0; cx < ChunkSelectorMap.TILES_GUI; cx++)
				{
					chunk = world.getChunkProvider().getLoadedChunk(startX + cx, startZ + cz);

					if (chunk != null)
					{
						x = (startX + cx) << 4;
						z = (startZ + cz) << 4;

						for (wi = 0; wi < 256; wi++)
						{
							wx = wi % 16;
							wz = wi / 16;
							by = getHeight(cx * 16 + wx, cz * 16 + wz);

							if (by < 0)
							{
								continue;
							}

							CURRENT_BLOCK_POS.setPos(x + wx, by, z + wz);
							state = chunk.getBlockState(wx, by, wz);

							color = ColorUtils.addBrightness(COLOR_CACHE.computeIfAbsent(state, COLOR_GETTER), MathUtils.RAND.nextFloat() * 0.04F);

							int bn = getHeight(cx * 16 + wx, cz * 16 + wz - 1);
							int bw = getHeight(cx * 16 + wx - 1, cz * 16 + wz);

							if (by > bn && bn != -1 || by > bw && bw != -1)
							{
								color = ColorUtils.addBrightness(color, 0.1F);
							}

							if (by < bn && bn != -1 || by < bw && bw != -1)
							{
								color = ColorUtils.addBrightness(color, -0.1F);
							}

							PIXELS.setRGB(cx * 16 + wx, cz * 16 + wz, color);
						}
					}

					pixelBuffer = ColorUtils.toByteBuffer(PIXELS.getPixels(), false);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		pixelBuffer = ColorUtils.toByteBuffer(PIXELS.getPixels(), false);
		world = null;
		instance = null;
	}
}