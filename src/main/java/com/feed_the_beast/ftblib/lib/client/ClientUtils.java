package com.feed_the_beast.ftblib.lib.client;

import com.feed_the_beast.ftblib.lib.gui.GuiBase;
import com.feed_the_beast.ftblib.lib.gui.IGuiWrapper;
import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.icon.PlayerHeadIcon;
import com.feed_the_beast.ftblib.lib.util.misc.NameMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.client.ClientCommandHandler;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ClientUtils
{
	public static final Minecraft MC = Minecraft.getMinecraft();
	public static final Function<ResourceLocation, TextureAtlasSprite> DEFAULT_TEXTURE_GETTER = location -> MC.getTextureMapBlocks().registerSprite(location);
	public static final NameMap<EnumBlockRenderType> BLOCK_RENDER_TYPE_NAME_MAP = NameMap.create(EnumBlockRenderType.MODEL, EnumBlockRenderType.values());
	public static final NameMap<BlockRenderLayer> BLOCK_RENDER_LAYER_NAME_MAP = NameMap.create(BlockRenderLayer.SOLID, BlockRenderLayer.values());
	public static final Map<ResourceLocation, TextureAtlasSprite> SPRITE_MAP = new HashMap<>();

	private static float lastBrightnessX, lastBrightnessY;
	private static EntityItem entityItem;

	public static PlayerHeadIcon localPlayerHead;

	public static int getDim()
	{
		return MC.world != null ? MC.world.provider.getDimension() : 0;
	}

	public static void spawnParticle(Particle particle)
	{
		MC.effectRenderer.addEffect(particle);
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

	public static void execClientCommand(String command, boolean printChat)
	{
		if (printChat)
		{
			MC.ingameGUI.getChatGUI().addToSentMessages(command);
		}

		if (ClientCommandHandler.instance.executeCommand(MC.player, command) == 0)
		{
			MC.player.sendChatMessage(command);
		}
	}

	public static void execClientCommand(String command)
	{
		execClientCommand(command, false);
	}

	public static void renderItem(World world, ItemStack stack)
	{
		if (entityItem == null)
		{
			entityItem = new EntityItem(world);
		}

		entityItem.setWorld(world);
		entityItem.hoverStart = 0F;
		entityItem.setItem(stack);
		MC.getRenderManager().renderEntity(entityItem, 0D, 0D, 0D, 0F, 0F, true);
	}

	public static void drawOutlinedBoundingBox(AxisAlignedBB box, Color4I color)
	{
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		int r = color.redi();
		int g = color.greeni();
		int b = color.bluei();
		int a = color.alphai();

		buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
		buffer.pos(box.minX, box.minY, box.minZ).color(r, g, b, a).endVertex();
		buffer.pos(box.maxX, box.minY, box.minZ).color(r, g, b, a).endVertex();
		buffer.pos(box.maxX, box.minY, box.maxZ).color(r, g, b, a).endVertex();
		buffer.pos(box.minX, box.minY, box.maxZ).color(r, g, b, a).endVertex();
		buffer.pos(box.minX, box.minY, box.minZ).color(r, g, b, a).endVertex();
		tessellator.draw();

		buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
		buffer.pos(box.minX, box.maxY, box.minZ).color(r, g, b, a).endVertex();
		buffer.pos(box.maxX, box.maxY, box.minZ).color(r, g, b, a).endVertex();
		buffer.pos(box.maxX, box.maxY, box.maxZ).color(r, g, b, a).endVertex();
		buffer.pos(box.minX, box.maxY, box.maxZ).color(r, g, b, a).endVertex();
		buffer.pos(box.minX, box.maxY, box.minZ).color(r, g, b, a).endVertex();
		tessellator.draw();

		buffer.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
		buffer.pos(box.minX, box.minY, box.minZ).color(r, g, b, a).endVertex();
		buffer.pos(box.minX, box.maxY, box.minZ).color(r, g, b, a).endVertex();
		buffer.pos(box.maxX, box.minY, box.minZ).color(r, g, b, a).endVertex();
		buffer.pos(box.maxX, box.maxY, box.minZ).color(r, g, b, a).endVertex();
		buffer.pos(box.maxX, box.minY, box.maxZ).color(r, g, b, a).endVertex();
		buffer.pos(box.maxX, box.maxY, box.maxZ).color(r, g, b, a).endVertex();
		buffer.pos(box.minX, box.minY, box.maxZ).color(r, g, b, a).endVertex();
		buffer.pos(box.minX, box.maxY, box.maxZ).color(r, g, b, a).endVertex();
		tessellator.draw();
	}

	public static BlockRenderLayer getStrongest(BlockRenderLayer layer1, BlockRenderLayer layer2)
	{
		return layer1.ordinal() > layer2.ordinal() ? layer1 : layer2;
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

	@Nullable
	public static <T> T getGuiAs(GuiScreen gui, Class<T> clazz)
	{
		if (gui instanceof IGuiWrapper)
		{
			GuiBase guiBase = ((IGuiWrapper) gui).getGui();

			if (clazz.isAssignableFrom(guiBase.getClass()))
			{
				return (T) guiBase;
			}
		}

		return clazz.isAssignableFrom(gui.getClass()) ? (T) MC.currentScreen : null;
	}

	@Nullable
	public static <T> T getCurrentGuiAs(Class<T> clazz)
	{
		return MC.currentScreen == null ? null : getGuiAs(MC.currentScreen, clazz);
	}
}