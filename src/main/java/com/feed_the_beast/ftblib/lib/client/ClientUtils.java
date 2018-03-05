package com.feed_the_beast.ftblib.lib.client;

import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.icon.PlayerHeadIcon;
import com.feed_the_beast.ftblib.lib.util.misc.NameMap;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.model.ModelLoader;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ClientUtils
{
	public static final Minecraft MC = Minecraft.getMinecraft();
	private static final Frustum FRUSTUM = new Frustum();
	public static final Function<ResourceLocation, TextureAtlasSprite> DEFAULT_TEXTURE_GETTER = location -> MC.getTextureMapBlocks().registerSprite(location);
	public static final NameMap<EnumBlockRenderType> BLOCK_RENDER_TYPE_NAME_MAP = NameMap.create(EnumBlockRenderType.MODEL, EnumBlockRenderType.values());
	public static final NameMap<BlockRenderLayer> BLOCK_RENDER_LAYER_NAME_MAP = NameMap.create(BlockRenderLayer.SOLID, BlockRenderLayer.values());
	public static final Map<ResourceLocation, TextureAtlasSprite> SPRITE_MAP = new HashMap<>();

	private static float lastBrightnessX, lastBrightnessY;
	private static EntityItem entityItem;

	public static PlayerHeadIcon localPlayerHead;

	public static void registerModel(Object _item, int meta, String variant)
	{
		Item item = _item instanceof Item ? (Item) _item : Item.getItemFromBlock((Block) _item);
		ModelLoader.setCustomModelResourceLocation(item, meta, variant.indexOf('#') != -1 ? new ModelResourceLocation(variant) : new ModelResourceLocation(item.getRegistryName(), variant));
	}

	public static void registerModel(Object _item)
	{
		registerModel(_item, 0, "inventory");
	}

	public static int getDim()
	{
		return MC.world != null ? MC.world.provider.getDimension() : 0;
	}

	public static void spawnParticle(Particle e)
	{
		MC.effectRenderer.addEffect(e);
	}

	public static void pushBrightness(int u, int t)
	{
		lastBrightnessX = OpenGlHelper.lastBrightnessX;
		lastBrightnessY = OpenGlHelper.lastBrightnessY;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, u, t);
	}

	public static void pushMaxBrightness()
	{
		pushBrightness(240, 240);
	}

	public static void popBrightness()
	{
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastBrightnessX, lastBrightnessY);
	}

	public static void execClientCommand(String s, boolean printChat)
	{
		Minecraft mc = MC;

		if (printChat)
		{
			mc.ingameGUI.getChatGUI().addToSentMessages(s);
		}

		if (ClientCommandHandler.instance.executeCommand(mc.player, s) == 0)
		{
			mc.player.sendChatMessage(s);
		}
	}

	public static void execClientCommand(String s)
	{
		execClientCommand(s, false);
	}

	public static void renderItem(World w, ItemStack is)
	{
		if (entityItem == null)
		{
			entityItem = new EntityItem(w);
		}

		entityItem.setWorld(w);
		entityItem.hoverStart = 0F;
		entityItem.setItem(is);
		MC.getRenderManager().renderEntity(entityItem, 0D, 0D, 0D, 0F, 0F, true);
	}

	public static void drawOutlinedBoundingBox(AxisAlignedBB bb, Color4I color)
	{
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();

		buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
		buffer.pos(bb.minX, bb.minY, bb.minZ).endVertex();
		buffer.pos(bb.maxX, bb.minY, bb.minZ).endVertex();
		buffer.pos(bb.maxX, bb.minY, bb.maxZ).endVertex();
		buffer.pos(bb.minX, bb.minY, bb.maxZ).endVertex();
		buffer.pos(bb.minX, bb.minY, bb.minZ).endVertex();
		tessellator.draw();

		buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
		buffer.pos(bb.minX, bb.maxY, bb.minZ).endVertex();
		buffer.pos(bb.maxX, bb.maxY, bb.minZ).endVertex();
		buffer.pos(bb.maxX, bb.maxY, bb.maxZ).endVertex();
		buffer.pos(bb.minX, bb.maxY, bb.maxZ).endVertex();
		buffer.pos(bb.minX, bb.maxY, bb.minZ).endVertex();
		tessellator.draw();

		buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION);
		buffer.pos(bb.minX, bb.minY, bb.minZ).endVertex();
		buffer.pos(bb.minX, bb.maxY, bb.minZ).endVertex();
		buffer.pos(bb.maxX, bb.minY, bb.minZ).endVertex();
		buffer.pos(bb.maxX, bb.maxY, bb.minZ).endVertex();
		buffer.pos(bb.maxX, bb.minY, bb.maxZ).endVertex();
		buffer.pos(bb.maxX, bb.maxY, bb.maxZ).endVertex();
		buffer.pos(bb.minX, bb.minY, bb.maxZ).endVertex();
		buffer.pos(bb.minX, bb.maxY, bb.maxZ).endVertex();
		tessellator.draw();
	}

	public static BlockRenderLayer getStrongest(BlockRenderLayer layer1, BlockRenderLayer layer2)
	{
		return BLOCK_RENDER_LAYER_NAME_MAP.get(Math.max(layer1.ordinal(), layer2.ordinal()));
	}

	public static void runLater(final Runnable runnable)
	{
		new Thread(() -> MC.addScheduledTask(runnable)).start();
	}

	public static TextureAtlasSprite getAtlasSprite(ResourceLocation name)
	{
		return SPRITE_MAP.computeIfAbsent(name, DEFAULT_TEXTURE_GETTER);
	}

	public static boolean isClientOP()
	{
		return MC.player != null && MC.player.getPermissionLevel() > 0;
	}

	public static boolean isFirstPersonView()
	{
		return MC.gameSettings.thirdPersonView == 0;
	}

	public static double getPlayerX()
	{
		return MC.getRenderManager().viewerPosX;
	}

	public static double getPlayerY()
	{
		return MC.getRenderManager().viewerPosY;
	}

	public static double getPlayerZ()
	{
		return MC.getRenderManager().viewerPosZ;
	}

	public static double getRenderX()
	{
		return TileEntityRendererDispatcher.staticPlayerX;
	}

	public static double getRenderY()
	{
		return TileEntityRendererDispatcher.staticPlayerY;
	}

	public static double getRenderZ()
	{
		return TileEntityRendererDispatcher.staticPlayerZ;
	}

	public static Frustum getFrustum(double x, double y, double z)
	{
		FRUSTUM.setPosition(x, y, z);
		return FRUSTUM;
	}

	public static Frustum getFrustum()
	{
		return getFrustum(getPlayerX(), getPlayerY(), getPlayerZ());
	}
}