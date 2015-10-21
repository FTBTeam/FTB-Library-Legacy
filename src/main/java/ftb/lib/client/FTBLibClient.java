package ftb.lib.client;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.UUID;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.relauncher.*;
import latmod.lib.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.*;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.client.ClientCommandHandler;

@SideOnly(Side.CLIENT)
public class FTBLibClient // LatCoreMCClient
{
	public static final Minecraft mc = FMLClientHandler.instance().getClient();
	private static final FastMap<ResourceLocation, Integer> textureMap = new FastMap<ResourceLocation, Integer>();
	private static final FastMap<String, ResourceLocation> cachedSkins = new FastMap<String, ResourceLocation>();
	public static IIcon blockNullIcon, unknownItemIcon;
	private static final ResourceLocation clickSound = new ResourceLocation("gui.button.press");
	private static float lastBrightnessX, lastBrightnessY;
	
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
		
		World w = mc.theWorld;
		if(w != null)
		{
			EntityPlayer ep = w.func_152378_a(uuid);
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
			img = new ThreadDownloadImageData((File)null, url, def, buffer);
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
		GL11.glColor4f(r / 255F, g / 255F, b / 255F, a / 255F);
	}
	
	public static void setGLColor(int c)
	{ setGLColor(c, LMColorUtils.getAlpha(c)); }
	
	public static ByteBuffer toByteBuffer(int pixels[], boolean alpha)
	{
		if(pixels == null) return null;
		ByteBuffer bb = BufferUtils.createByteBuffer(pixels.length * 4);
		byte alpha255 = (byte)255;
		
		for(int i = 0; i < pixels.length; i++)
		{
			bb.put((byte)LMColorUtils.getRed(pixels[i]));
			bb.put((byte)LMColorUtils.getGreen(pixels[i]));
			bb.put((byte)LMColorUtils.getBlue(pixels[i]));
			bb.put(alpha ? (byte)LMColorUtils.getAlpha(pixels[i]) : alpha255);
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
	
	public static void setTexture(ResourceLocation tex)
	{
		if(mc.currentScreen instanceof ITextureGui)
		{
			((ITextureGui)mc.currentScreen).setTexture(tex);
			return;
		}
		
		Integer i = textureMap.get(tex);
		
		if(i == null)
		{
			FTBLibClient.mc.getTextureManager().bindTexture(tex);
			textureMap.put(tex, i = Integer.valueOf(FTBLibClient.mc.getTextureManager().getTexture(tex).getGlTextureId()));
		}
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, i.intValue());
	}
	
	public static void clearCachedData()
	{
		textureMap.clear();
		cachedSkins.clear();
	}
}