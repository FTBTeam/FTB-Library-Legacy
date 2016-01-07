package ftb.lib.client;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.relauncher.*;
import ftb.lib.api.gui.callback.ClientTickCallback;
import ftb.lib.gui.GuiLM;
import ftb.lib.mod.client.FTBLibRenderHandler;
import latmod.lib.LMColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.client.ClientCommandHandler;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;
import java.util.*;

@SideOnly(Side.CLIENT)
public class FTBLibClient // LatCoreMCClient
{
	public static final Minecraft mc = FMLClientHandler.instance().getClient();
	private static final HashMap<String, ResourceLocation> cachedSkins = new HashMap<>();
	public static IIcon blockNullIcon, unknownItemIcon;
	private static final ResourceLocation clickSound = new ResourceLocation("gui.button.press");
	private static float lastBrightnessX, lastBrightnessY;
	private static EntityItem entityItem;
	public static int displayW, displayH;
	
	public static boolean isPlaying()
	{ return mc.theWorld != null && mc.thePlayer != null && mc.thePlayer.worldObj != null; }
	
	public static UUID getUUID()
	{ return mc.getSession().func_148256_e().getId(); }
	
	public static void spawnPart(EntityFX e)
	{ mc.effectRenderer.addEffect(e); }
	
	public static KeyBinding addKeyBinding(String name, int key, String cat)
	{ KeyBinding k = new KeyBinding(name, key, cat); ClientRegistry.registerKeyBinding(k); return k; }
	
	public static void pushMaxBrightness()
	{
		lastBrightnessX = OpenGlHelper.lastBrightnessX;
		lastBrightnessY = OpenGlHelper.lastBrightnessY;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
	}
	
	public static void popMaxBrightness()
	{ OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastBrightnessX, lastBrightnessY); }

	public static EntityPlayerSP getPlayerSP(UUID uuid)
	{
		//getMinecraft().getIntegratedServer().getConfigurationManager().playerEntityList

		if(uuid == null) return mc.thePlayer;

		if(mc.theWorld != null)
		{
			EntityPlayer ep = mc.theWorld.func_152378_a(uuid);
			if(ep != null && ep instanceof EntityPlayerSP)
				return (EntityPlayerSP)ep;
		}
		
		return null;
	}
	
	public static ThreadDownloadImageData getDownloadImage(ResourceLocation out, String url, ResourceLocation def, IImageBuffer buffer)
	{
		TextureManager t = mc.getTextureManager();
		ThreadDownloadImageData img = (ThreadDownloadImageData)t.getTexture(out);
		
		if (img == null)
		{
			img = new ThreadDownloadImageData(null, url, def, buffer);
			t.loadTexture(out, img);
		}
		
		return img;
	}
	
	public static void playClickSound()
	{ mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(clickSound, 1F)); }
	
	public static void setGLColor(int c, int a)
	{
		int r = LMColorUtils.getRed(c);
		int g = LMColorUtils.getGreen(c);
		int b = LMColorUtils.getBlue(c);
		GlStateManager.color(r / 255F, g / 255F, b / 255F, a / 255F);
	}
	
	public static void setGLColor(int c)
	{ setGLColor(c, LMColorUtils.getAlpha(c)); }
	
	public static ByteBuffer toByteBuffer(int pixels[], boolean alpha)
	{
		if(pixels == null) return null;
		ByteBuffer bb = BufferUtils.createByteBuffer(pixels.length * 4);
		byte alpha255 = (byte)255;
		
		for(int p : pixels)
		{
			bb.put((byte)LMColorUtils.getRed(p));
			bb.put((byte)LMColorUtils.getGreen(p));
			bb.put((byte)LMColorUtils.getBlue(p));
			bb.put(alpha ? (byte)LMColorUtils.getAlpha(p) : alpha255);
		}
		
		bb.flip();
		return bb;
	}
	
	public static void execClientCommand(String s)
	{
		mc.ingameGUI.getChatGUI().addToSentMessages(s);
		if(ClientCommandHandler.instance.executeCommand(mc.thePlayer, s) == 0)
			mc.thePlayer.sendChatMessage(s);
	}
	
	public static ResourceLocation getSkinTexture(String username)
	{
		ResourceLocation r = cachedSkins.get(username);
		
		if(r == null)
		{
			r = AbstractClientPlayer.getLocationSkin(username);
			
			try
			{
				AbstractClientPlayer.getDownloadImageSkin(r, username);
				cachedSkins.put(username, r);
			}
			catch(Exception e)
			{ e.printStackTrace(); }
		}
		
		return r;
	}
	
	public static void setTexture(TextureCoords tex)
	{ if(tex != null) setTexture(tex.texture); }
	
	public static void setTexture(ResourceLocation tex)
	{
		if(mc.currentScreen instanceof GuiLM)
			((GuiLM)mc.currentScreen).setTexture(tex);
		else mc.getTextureManager().bindTexture(tex);
	}
	
	public static void clearCachedData()
	{ cachedSkins.clear(); }

	public static boolean canRenderGui()
	{ return mc.currentScreen == null || mc.currentScreen instanceof GuiChat; }
	
	public static void addClientTickCallback(ClientTickCallback e)
	{ FTBLibRenderHandler.callbacks.add(e); }

	public static void renderItem(World w, ItemStack is, boolean fancy, boolean frame)
	{
		if(entityItem == null) entityItem = new EntityItem(w);

		entityItem.worldObj = w;
		entityItem.hoverStart = 0F;
		entityItem.setEntityItemStack(is);
		
		boolean isFancy = RenderManager.instance.options.fancyGraphics;
		RenderManager.instance.options.fancyGraphics = true;
		RenderItem.renderInFrame = frame;
		
		RenderManager.instance.renderEntityWithPosYaw(entityItem, 0D, 0D, 0D, 0F, 0F);
		
		RenderManager.instance.options.fancyGraphics = isFancy;
		RenderItem.renderInFrame = false;
	}
	
	public static void drawOutlinedBoundingBoxGL(AxisAlignedBB bb)
	{
		GL11.glBegin(GL11.GL_LINE_STRIP);
		GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
		GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
		GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
		GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
		GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
		GL11.glEnd();
		
		GL11.glBegin(GL11.GL_LINE_STRIP);
		GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
		GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
		GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
		GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
		GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
		GL11.glEnd();
		
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
		GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
		GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
		GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
		GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
		GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
		GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
		GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
		GL11.glEnd();
	}
	
	public static void renderGuiItem(ItemStack is, RenderItem itemRender, FontRenderer font, int x, int y)
	{
		if(is == null || is.getItem() == null) return;
		GlStateManager.pushAttrib();
		GlStateManager.pushMatrix();
		//GL11.glTranslatef(0F, 0F, 32F);
		GlStateManager.enableLighting();
		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.enableRescaleNormal();
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
		GlStateManager.color(1F, 1F, 1F, 1F);
		FontRenderer f = is.getItem().getFontRenderer(is);
		if (f == null) f = font;
		itemRender.renderItemAndEffectIntoGUI(f, FTBLibClient.mc.getTextureManager(), is, x, y);
		itemRender.renderItemOverlayIntoGUI(f, FTBLibClient.mc.getTextureManager(), is, x, y, null);
		GlStateManager.popMatrix();
		GlStateManager.popAttrib();
	}
}