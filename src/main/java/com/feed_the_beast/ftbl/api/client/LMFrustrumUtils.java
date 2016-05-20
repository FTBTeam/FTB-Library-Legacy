package com.feed_the_beast.ftbl.api.client;

import latmod.lib.LMUtils;
import latmod.lib.util.Pos2D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.world.DimensionType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

@SideOnly(Side.CLIENT)
public class LMFrustrumUtils
{
    public static final Frustum frustum = new Frustum();
    public static final IntBuffer VIEWPORT = GLAllocation.createDirectIntBuffer(16);
    public static final FloatBuffer MODELVIEW = GLAllocation.createDirectFloatBuffer(16);
    public static final FloatBuffer PROJECTION = GLAllocation.createDirectFloatBuffer(16);
    public static final FloatBuffer OBJECTCOORDS = GLAllocation.createDirectFloatBuffer(3);
    public static boolean isFirstPerson;
    public static DimensionType currentDim;
    public static double playerX, playerY, playerZ;
    public static double renderX, renderY, renderZ;
    public static long playerPosHash;

    public static void update()
    {
        Minecraft mc = FTBLibClient.mc;
        isFirstPerson = FTBLibClient.mc.gameSettings.thirdPersonView == 0;
        currentDim = FTBLibClient.getDim();
        //mc.thePlayer.posX

        playerX = mc.getRenderManager().viewerPosX;
        playerY = mc.getRenderManager().viewerPosY;
        playerZ = mc.getRenderManager().viewerPosZ;
        renderX = TileEntityRendererDispatcher.staticPlayerX;
        renderY = TileEntityRendererDispatcher.staticPlayerY;
        renderZ = TileEntityRendererDispatcher.staticPlayerZ;
        playerPosHash = Math.abs(LMUtils.longHashCode(currentDim, playerX, playerY, playerZ) + 1);
        frustum.setPosition(playerX, playerY, playerZ);

        updateMatrix();
    }

    public static void updateMatrix()
    {
        GlStateManager.getFloat(GL11.GL_MODELVIEW_MATRIX, MODELVIEW);
        GlStateManager.getFloat(GL11.GL_PROJECTION_MATRIX, PROJECTION);
        GL11.glGetInteger(GL11.GL_VIEWPORT, VIEWPORT);
    }

    public static Pos2D getScreenCoords(float x, float y, float z)
    {
        boolean result = GLU.gluProject(x, y, z, MODELVIEW, PROJECTION, VIEWPORT, OBJECTCOORDS);
        if(result)
        {
            float px = OBJECTCOORDS.get(0);
            float py = OBJECTCOORDS.get(1);
            return new Pos2D(px, py);
            //if(px >= 0 && py >= 0 && px < VIEWPORT.getMode(2) && py < VIEWPORT.getMode(3)) return new Pos2D(px, py);
        }

        return null;
    }
}