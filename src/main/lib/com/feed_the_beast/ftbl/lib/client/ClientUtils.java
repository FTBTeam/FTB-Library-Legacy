package com.feed_the_beast.ftbl.lib.client;

import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.NameMap;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.model.ModelLoader;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class ClientUtils
{
	public static final Minecraft MC = Minecraft.getMinecraft();
	public static final Frustum FRUSTUM = new Frustum();
	public static final Map<String, ResourceLocation> CACHED_SKINS = new HashMap<>();
	public static final Function<ResourceLocation, TextureAtlasSprite> DEFAULT_TEXTURE_GETTER = location -> MC.getTextureMapBlocks().registerSprite(location);
	public static final NameMap<EnumBlockRenderType> BLOCK_RENDER_TYPE_NAME_MAP = NameMap.create(EnumBlockRenderType.MODEL, EnumBlockRenderType.values());

	public static boolean isFirstPerson;
	public static int currentDim, playerPosHash;
	public static double playerX, playerY, playerZ;
	public static double renderX, renderY, renderZ;
	private static float lastBrightnessX, lastBrightnessY;
	private static EntityItem entityItem;

	public static PlayerHeadImage localPlayerHead;

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

	public static ITextureObject bindTexture(ResourceLocation texture)
	{
		TextureManager t = MC.getTextureManager();

		ITextureObject textureObject = t.getTexture(texture);
		if (textureObject == null)
		{
			textureObject = new SimpleTexture(texture);
			t.loadTexture(texture, textureObject);
		}

		GlStateManager.bindTexture(textureObject.getGlTextureId());
		return textureObject;
	}

	public static ITextureObject getDownloadImage(ResourceLocation out, String url, ResourceLocation def, @Nullable IImageBuffer buffer)
	{
		TextureManager t = MC.getTextureManager();
		ITextureObject img = t.getTexture(out);

		if (img == null)
		{
			img = new ThreadDownloadImageData(null, url, def, buffer);
			t.loadTexture(out, img);
		}

		return img;
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

	public static ResourceLocation getSkinTexture(String username)
	{
		ResourceLocation r = CACHED_SKINS.get(username);

		if (r == null)
		{
			r = AbstractClientPlayer.getLocationSkin(username);

			try
			{
				AbstractClientPlayer.getDownloadImageSkin(r, username);
				CACHED_SKINS.put(username, r);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return r;
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
		MC.getRenderManager().doRenderEntity(entityItem, 0D, 0D, 0D, 0F, 0F, true);
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

	public static void updateRenderInfo()
	{
		Minecraft mc = MC;
		isFirstPerson = mc.gameSettings.thirdPersonView == 0;
		currentDim = ClientUtils.getDim();
		//mc.thePlayer.posX

		playerX = mc.getRenderManager().viewerPosX;
		playerY = mc.getRenderManager().viewerPosY;
		playerZ = mc.getRenderManager().viewerPosZ;
		renderX = TileEntityRendererDispatcher.staticPlayerX;
		renderY = TileEntityRendererDispatcher.staticPlayerY;
		renderZ = TileEntityRendererDispatcher.staticPlayerZ;
		playerPosHash = Objects.hash(currentDim, playerX, playerY, playerZ);
		FRUSTUM.setPosition(playerX, playerY, playerZ);
	}

	public static Map<String, TextureAtlasSprite> getRegisteredSpritesMap()
	{
		return MC.getTextureMapBlocks().mapRegisteredSprites;
	}
}