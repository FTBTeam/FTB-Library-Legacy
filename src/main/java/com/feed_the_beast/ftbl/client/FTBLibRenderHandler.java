package com.feed_the_beast.ftbl.client;

import com.feed_the_beast.ftbl.api.client.CubeRenderer;
import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.client.LMFrustrumUtils;
import com.feed_the_beast.ftbl.api.notification.ClientNotifications;
import com.feed_the_beast.ftbl.util.FTBLib;
import latmod.lib.MathHelperLM;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class FTBLibRenderHandler
{
    public static final FTBLibRenderHandler instance = new FTBLibRenderHandler();
    public static final ResourceLocation chunkBorderTexture = new ResourceLocation("ftbl", "textures/world/chunk_border.png");
    private static final CubeRenderer chunkBorderRenderer = new CubeRenderer().setHasTexture().setHasNormals();
    private static final List<MobSpawnPos> lightList = new ArrayList<>();
    public static boolean renderChunkBounds = false;

    private static boolean renderLightValues = false;
    private static boolean needsLightUpdate = true;
    private static int lastX, lastY = -1, lastZ;
    public enum LightValueTexture
    {
        O,
        X;

        public final ResourceLocation texture;

        LightValueTexture()
        {
            texture = new ResourceLocation("ftbl", "textures/world/light_value_" + name().toLowerCase() + ".png");
        }
    }

    private static class MobSpawnPos
    {
        public final BlockPos pos;
        public final boolean alwaysSpawns;
        public final int lightValue;

        public MobSpawnPos(BlockPos p, boolean b, int lv)
        {
            pos = p;
            alwaysSpawns = b;
            lightValue = lv;
        }
    }

    public static void toggleLightLevel()
    {
        renderLightValues = !renderLightValues;
        needsLightUpdate = renderLightValues;

        if(!renderLightValues)
        {
            needsLightUpdate = false;
            lightList.clear();
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

        if(e.phase == TickEvent.Phase.END && FTBLibClient.isIngame())
        {
            ClientNotifications.renderTemp();
        }

        GlStateManager.popMatrix();
    }

    @SubscribeEvent
    public void blockChanged(BlockEvent e)
    {
        if(MathHelperLM.distSq(e.getPos().getX() + 0.5D, e.getPos().getY() + 0.5D, e.getPos().getZ() + 0.5D, lastX + 0.5D, lastY + 0.5D, lastZ + 0.5D) <= 4096D)
        {
            needsLightUpdate = true;
        }
    }

    @SubscribeEvent
    public void renderWorld(RenderWorldLastEvent e)
    {
        LMFrustrumUtils.update();

        if(renderChunkBounds || renderLightValues)
        {
            int x = MathHelperLM.unchunk(MathHelperLM.chunk(LMFrustrumUtils.playerX));
            int z = MathHelperLM.unchunk(MathHelperLM.chunk(LMFrustrumUtils.playerZ));

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

            if(renderChunkBounds)
            {
                double d = 0.02D;
                FTBLibClient.setTexture(chunkBorderTexture);
                chunkBorderRenderer.setTessellator(Tessellator.getInstance());
                chunkBorderRenderer.setSize(x + d, 0D, z + d, x + 16D - d, 256D, z + 16D - d);
                chunkBorderRenderer.setUV(0D, 0D, 16D, 256D);
                chunkBorderRenderer.renderSides();
            }

            if(renderLightValues && LMFrustrumUtils.playerY >= 0D)
            {
                if(lastY == -1D || MathHelperLM.distSq(LMFrustrumUtils.playerX, LMFrustrumUtils.playerY, LMFrustrumUtils.playerZ, lastX + 0.5D, lastY + 0.5D, lastZ + 0.5D) >= MathHelperLM.SQRT_2 * 2D)
                {
                    needsLightUpdate = true;
                }

                if(needsLightUpdate)
                {
                    needsLightUpdate = false;
                    lightList.clear();
                    World w = FTBLibClient.mc.theWorld;

                    lastX = MathHelperLM.floor(LMFrustrumUtils.playerX);
                    lastY = MathHelperLM.floor(LMFrustrumUtils.playerY);
                    lastZ = MathHelperLM.floor(LMFrustrumUtils.playerZ);

                    for(int by = lastY - 16; by <= lastY + 16; by++)
                    {
                        for(int bx = lastX - 16; bx <= lastX + 16; bx++)
                        {
                            for(int bz = lastZ - 16; bz <= lastZ + 16; bz++)
                            {
                                BlockPos pos = new BlockPos(bx, by, bz);
                                Boolean b = FTBLib.canMobSpawn(w, pos);
                                if(b != null)
                                {
                                    int lv = 0;
                                    if(FTBLibClient.mc.gameSettings.showDebugInfo)
                                    {
                                        lv = w.getLight(pos, true);
                                    }
                                    lightList.add(new MobSpawnPos(pos, b == Boolean.TRUE, lv));
                                }
                            }
                        }
                    }
                }

                if(!lightList.isEmpty())
                {
                    GlStateManager.enableCull();
                    GlStateManager.color(1F, 1F, 1F, 1F);
                    FTBLibClient.setTexture(FTBLibModClient.light_value_texture.get().texture);

                    Tessellator tessellator = Tessellator.getInstance();
                    VertexBuffer buffer = tessellator.getBuffer();

                    buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);

                    for(MobSpawnPos pos : lightList)
                    {
                        double bx = pos.pos.getX();
                        double by = pos.pos.getY() + 0.03D;
                        double bz = pos.pos.getZ();

                        float green = pos.alwaysSpawns ? 0.2F : 1F;
                        buffer.pos(bx, by, bz).tex(0D, 0D).color(1F, green, 0.2F, 1F).endVertex();
                        buffer.pos(bx, by, bz + 1D).tex(0D, 1D).color(1F, green, 0.2F, 1F).endVertex();
                        buffer.pos(bx + 1D, by, bz + 1D).tex(1D, 1D).color(1F, green, 0.2F, 1F).endVertex();
                        buffer.pos(bx + 1D, by, bz).tex(1D, 0D).color(1F, green, 0.2F, 1F).endVertex();
                    }

                    tessellator.draw();

                    GlStateManager.color(1F, 1F, 1F, 1F);

                    if(FTBLibClient.mc.gameSettings.showDebugInfo)
                    {
                        for(MobSpawnPos pos : lightList)
                        {
                            double bx = pos.pos.getX() + 0.5D;
                            double by = pos.pos.getY() + 0.14D;
                            double bz = pos.pos.getZ() + 0.5D;

                            if(MathHelperLM.distSq(LMFrustrumUtils.playerX, LMFrustrumUtils.playerY, LMFrustrumUtils.playerZ, bx, by, bz) <= 144D)
                            {
                                GlStateManager.pushMatrix();
                                GlStateManager.translate(bx, by, bz);
                                GlStateManager.rotate((float) (-Math.atan2(bz - LMFrustrumUtils.playerZ, bx - LMFrustrumUtils.playerX) * 180D / Math.PI) + 90F, 0F, 1F, 0F);
                                GlStateManager.rotate(40F, 1F, 0F, 0F);

                                float scale = 1F / 32F;
                                GlStateManager.scale(-scale, -scale, 1F);

                                String s = Integer.toString(pos.lightValue);
                                FTBLibClient.mc.fontRendererObj.drawString(s, -FTBLibClient.mc.fontRendererObj.getStringWidth(s) / 2, -5, 0xFFFFFFFF);
                                GlStateManager.popMatrix();
                            }
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

        if(FTBLib.ftbu != null)
        {
            FTBLib.ftbu.renderWorld(e.getPartialTicks());
        }
    }
}