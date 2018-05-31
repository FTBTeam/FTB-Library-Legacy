package com.feed_the_beast.ftblib.client;

import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.feed_the_beast.ftblib.lib.client.PixelBuffer;
import com.feed_the_beast.ftblib.lib.gui.misc.ChunkSelectorMap;
import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.math.MathUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
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
	private static final Map<IBlockState, Color4I> COLOR_CACHE = new HashMap<>();
	private static final BlockPos.MutableBlockPos CURRENT_BLOCK_POS = new BlockPos.MutableBlockPos(0, 0, 0);
	private static World world = null;
	private static final Function<IBlockState, Color4I> COLOR_GETTER = state1 -> Color4I.rgb(getBlockColor0(state1));
	private static ThreadReloadChunkSelector instance;
	private static int textureID = -1;
	private static final int[] HEIGHT_MAP = new int[PIXEL_SIZE * PIXEL_SIZE];

	static int getTextureId()
	{
		if (textureID == -1)
		{
			textureID = TextureUtil.glGenTextures();
		}

		return textureID;
	}

	static void updateTexture()
	{
		if (pixelBuffer != null)
		{
			//boolean hasBlur = false;
			//int filter = hasBlur ? GL11.GL_LINEAR : GL11.GL_NEAREST;
			GlStateManager.bindTexture(getTextureId());
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, ChunkSelectorMap.TILES_TEX * 16, ChunkSelectorMap.TILES_TEX * 16, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixelBuffer);
			pixelBuffer = null;
		}
	}

	static void reloadArea(World w, int sx, int sz)
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

	private static int getBlockColor0(IBlockState state)
	{
		Block b = state.getBlock();

		if (b == Blocks.SANDSTONE || b == Blocks.END_STONE)
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
		else if (b == Blocks.GRASS_PATH)
		{
			return 0xB7A14B;
		}
		else if (b == Blocks.TORCH)
		{
			return 0xFFA530;
		}
		else if (b == Blocks.NETHERRACK || b == Blocks.QUARTZ_ORE)
		{
			return 0x7F1321;
		}
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

		return state.getMapColor(world, CURRENT_BLOCK_POS).colorValue;
	}

	private static int getHeight(int x, int z)
	{
		int index = x + z * PIXEL_SIZE;
		return index < 0 || index >= HEIGHT_MAP.length ? -1 : HEIGHT_MAP[index];
	}

	@Override
	public void run()
	{
		Arrays.fill(PIXELS.getPixels(), Color4I.rgb(world.getSkyColor(ClientUtils.MC.player, 0)).rgba());
		Arrays.fill(HEIGHT_MAP, -1);
		pixelBuffer = PIXELS.toByteBuffer(false);

		Chunk chunk;
		int cx, cz, x, z, wi, wx, wz, by, topY;
		Color4I color;
		IBlockState state;

		int startY = ClientUtils.MC.player.getPosition().getY();

		try
		{
			for (cz = 0; cz < ChunkSelectorMap.TILES_GUI; cz++)
			{
				for (cx = 0; cx < ChunkSelectorMap.TILES_GUI; cx++)
				{
					if (world == null)
					{
						continue;
					}

					chunk = world.getChunkProvider().getLoadedChunk(startX + cx, startZ + cz);

					if (chunk != null)
					{
						x = (startX + cx) << 4;
						z = (startZ + cz) << 4;
						topY = (world.provider.getDimension() == -1) ? startY + 5 : Math.max(world.getActualHeight(), chunk.getTopFilledSegment() + 15);

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
					if (world == null)
					{
						continue;
					}

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

							color = COLOR_CACHE.computeIfAbsent(state, COLOR_GETTER).addBrightness(MathUtils.RAND.nextFloat() * 0.04F);

							int bn = getHeight(cx * 16 + wx, cz * 16 + wz - 1);
							int bw = getHeight(cx * 16 + wx - 1, cz * 16 + wz);

							if (by > bn && bn != -1 || by > bw && bw != -1)
							{
								color = color.addBrightness(0.1F);
							}

							if (by < bn && bn != -1 || by < bw && bw != -1)
							{
								color = color.addBrightness(-0.1F);
							}

							PIXELS.setRGB(cx * 16 + wx, cz * 16 + wz, color.rgba());
						}
					}

					pixelBuffer = PIXELS.toByteBuffer(false);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		pixelBuffer = PIXELS.toByteBuffer(false);
		world = null;
		instance = null;
	}
}