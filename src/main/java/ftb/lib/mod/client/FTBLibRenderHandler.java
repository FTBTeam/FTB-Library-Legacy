package ftb.lib.mod.client;

import ftb.lib.FTBLib;
import ftb.lib.api.client.*;
import ftb.lib.api.client.model.CubeRenderer;
import ftb.lib.api.gui.callback.ClientTickCallback;
import ftb.lib.notification.ClientNotifications;
import latmod.lib.MathHelperLM;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.*;
import org.lwjgl.opengl.GL11;

import java.util.*;

@SideOnly(Side.CLIENT)
public class FTBLibRenderHandler
{
	public static final FTBLibRenderHandler instance = new FTBLibRenderHandler();
	public static final List<ClientTickCallback> callbacks = new ArrayList<>();
	private static final CubeRenderer chunkBorderRenderer = new CubeRenderer().setTessellator(null);
	public static final ResourceLocation chunkBorderTexture = new ResourceLocation("ftbl", "textures/chunk_border.png");
	public static final ResourceLocation lightValueTexture = new ResourceLocation("ftbl", "textures/light_value.png");
	public static boolean renderChunkBounds = false;
	
	private static boolean renderLightValues = false;
	private static boolean needsLightUpdate = true;
	private static final List<BlockPos> lightListR = new ArrayList<>();
	private static final List<BlockPos> lightListY = new ArrayList<>();
	private static int lastX, lastY = -1, lastZ;
	
	public static void toggleLightLevel()
	{
		renderLightValues = !renderLightValues;
		needsLightUpdate = renderLightValues;
		
		if(!renderLightValues)
		{
			needsLightUpdate = false;
			lightListR.clear();
			lightListY.clear();
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOW)
	public void renderTick(TickEvent.RenderTickEvent e)
	{
		GlStateManager.pushMatrix();
		
		if(e.phase == TickEvent.Phase.START)
		{
			ScaledResolution sr = new ScaledResolution(FTBLibClient.mc);
			FTBLibClient.displayW = sr.getScaledWidth();
			FTBLibClient.displayH = sr.getScaledHeight();
		}
		
		if(e.phase == TickEvent.Phase.END && FTBLibClient.isIngame()) ClientNotifications.renderTemp();
		
		GlStateManager.popMatrix();
	}
	
	@SubscribeEvent
	public void clientTick(TickEvent.ClientTickEvent e)
	{
		if(e.phase == TickEvent.Phase.END && !callbacks.isEmpty())
		{
			for(int i = 0; i < callbacks.size(); i++)
				callbacks.get(i).onCallback();
			callbacks.clear();
		}
	}
	
	@SubscribeEvent
	public void blockChanged(BlockEvent e)
	{
		if(MathHelperLM.distSq(e.pos.getX() + 0.5D, e.pos.getY() + 0.5D, e.pos.getZ() + 0.5D, lastX + 0.5D, lastY + 0.5D, lastZ + 0.5D) <= 4096D)
			needsLightUpdate = true;
	}
	
	@SubscribeEvent
	public void renderWorld(RenderWorldLastEvent e)
	{
		LMFrustrumUtils.update();
		
		if(renderChunkBounds || renderLightValues)
		{
			int x = MathHelperLM.unchunk(MathHelperLM.chunk(FTBLibClient.mc.thePlayer.posX));
			int z = MathHelperLM.unchunk(MathHelperLM.chunk(FTBLibClient.mc.thePlayer.posZ));
			
			GlStateManager.pushMatrix();
			GlStateManager.translate(-LMFrustrumUtils.renderX, -LMFrustrumUtils.renderY, -LMFrustrumUtils.renderZ);
			
			FTBLibClient.pushMaxBrightness();
			
			GlStateManager.enableBlend();
			GlStateManager.shadeModel(GL11.GL_SMOOTH);
			GlStateManager.disableCull();
			GlStateManager.depthMask(false);
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.enableTexture2D();
			GlStateManager.color(1F, 1F, 1F, 1F);
			GlStateManager.alphaFunc(GL11.GL_GREATER, 0.01F);
			
			double d = 0.01D;
			
			if(renderChunkBounds)
			{
				FTBLibClient.setTexture(chunkBorderTexture);
				chunkBorderRenderer.setSize(x + d, 0D, z + d, x + 16D - d, 256D, z + 16D - d);
				chunkBorderRenderer.setUV(0D, 0D, 16D, 256D);
				chunkBorderRenderer.renderSides();
			}
			
			if(renderLightValues && FTBLibClient.mc.thePlayer.posY >= 0D)
			{
				if(lastY == -1D || MathHelperLM.distSq(FTBLibClient.mc.thePlayer.posX, FTBLibClient.mc.thePlayer.posY, FTBLibClient.mc.thePlayer.posZ, lastX + 0.5D, lastY + 0.5D, lastZ + 0.5D) >= 32D)
					needsLightUpdate = true;
				
				if(needsLightUpdate)
				{
					needsLightUpdate = false;
					lightListR.clear();
					lightListY.clear();
					World w = FTBLibClient.mc.theWorld;
					
					lastX = MathHelperLM.floor(FTBLibClient.mc.thePlayer.posX);
					lastY = MathHelperLM.floor(FTBLibClient.mc.thePlayer.posY);
					lastZ = MathHelperLM.floor(FTBLibClient.mc.thePlayer.posZ);
					
					for(int by = lastY - 16; by < lastY + 16; by++)
					{
						for(int bx = lastX - 16; bx < lastX + 16; bx++)
						{
							for(int bz = lastZ - 16; bz < lastZ + 16; bz++)
							{
								BlockPos pos = new BlockPos(bx, by, bz);
								Boolean b = FTBLib.canMobSpawn(w, pos);
								
								if(b != null)
								{
									if(b == Boolean.TRUE) lightListR.add(pos);
									else lightListY.add(pos);
								}
							}
						}
					}
				}
				
				if(lightListR.size() > 0 || lightListY.size() > 0)
				{
					FTBLibClient.setTexture(lightValueTexture);
					chunkBorderRenderer.setUV(0D, 0D, 1D, 1D);
					
					if(lightListR.size() > 0)
					{
						GlStateManager.color(1F, 0.2F, 0.2F, 1F);
						
						for(int i = 0; i < lightListR.size(); i++)
						{
							BlockPos pos = lightListR.get(i);
							chunkBorderRenderer.setSizeWHD(pos.getX(), pos.getY() + d, pos.getZ(), 1D, 1D, 1D);
							chunkBorderRenderer.renderDown();
						}
					}
					
					if(lightListY.size() > 0)
					{
						GlStateManager.color(1F, 1F, 0.2F, 1F);
						
						for(int i = 0; i < lightListY.size(); i++)
						{
							BlockPos pos = lightListY.get(i);
							chunkBorderRenderer.setSizeWHD(pos.getX(), pos.getY() + d, pos.getZ(), 1D, 1D, 1D);
							chunkBorderRenderer.renderDown();
						}
					}
				}
			}
			
			GlStateManager.color(1F, 1F, 1F, 1F);
			
			GlStateManager.enableCull();
			GlStateManager.depthMask(true);
			GlStateManager.shadeModel(GL11.GL_FLAT);
			
			FTBLibClient.popMaxBrightness();
			GlStateManager.popMatrix();
			GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
		}
		
		if(FTBLib.ftbu != null) FTBLib.ftbu.renderWorld(e.partialTicks);
	}
}