package com.feed_the_beast.ftbl.api.client;

import com.latmod.lib.util.LMUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LMFrustumUtils
{
    public static final Frustum frustum = new Frustum();
    /*
    private static final Vector4f OBJECTCOORDS = new Vector4f();
    private static final Vector4f TEMP_POINT = new Vector4f();
    private static final Matrix4f MATRIX_MVM = new Matrix4f();
    private static final Matrix4f MATRIX_PJM = new Matrix4f();
    private static final Matrix4f MATRIX_OUT = new Matrix4f();
    */
    public static boolean isFirstPerson;
    public static int currentDim;
    public static double playerX, playerY, playerZ;
    public static double renderX, renderY, renderZ;
    public static long playerPosHash;
    //private static IntBuffer VIEWPORT;
    //private static FloatBuffer MODELVIEW, PROJECTION;

    public static void update()
    {
        Minecraft mc = FTBLibClient.mc();
        isFirstPerson = mc.gameSettings.thirdPersonView == 0;
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

        /*
        VIEWPORT = null;

        if(VIEWPORT == null)
        {
            try
            {
                Field f = ReflectionHelper.findField(ActiveRenderInfo.class, "field_178814_a", "VIEWPORT");
                f.setAccessible(true);
                VIEWPORT = (IntBuffer) f.get(null);

                ClippingHelperImpl inst = (ClippingHelperImpl) ClippingHelperImpl.getInstance();

                f = ReflectionHelper.findField(ClippingHelperImpl.class, "field_78562_g", "modelviewMatrixBuffer");
                f.setAccessible(true);
                MODELVIEW = (FloatBuffer) f.get(inst);

                f = ReflectionHelper.findField(ClippingHelperImpl.class, "field_78561_f", "projectionMatrixBuffer");
                f.setAccessible(true);
                PROJECTION = (FloatBuffer) f.get(inst);
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }

        MODELVIEW.flip().limit(16);
        MATRIX_MVM.load(MODELVIEW);

        PROJECTION.flip().limit(16);
        MATRIX_PJM.load(PROJECTION);

        Matrix4f.mul(MATRIX_MVM, MATRIX_PJM, MATRIX_OUT);

        // System.out.println(VIEWPORT);
        // System.out.println(MODELVIEW);
        // System.out.println(PROJECTION);
        
        */
    }
    
    /*

    public static Vector4f worldToViewport(float x, float y, float z)
    {
        TEMP_POINT.x = x;
        TEMP_POINT.y = y;
        TEMP_POINT.z = z;
        TEMP_POINT.w = 1F;

        Matrix4f.transform(MATRIX_OUT, TEMP_POINT, OBJECTCOORDS);

        if(Math.abs(OBJECTCOORDS.w) > 0.0000001F)
        {
            OBJECTCOORDS.scale(1F / OBJECTCOORDS.w);
        }

        return OBJECTCOORDS;
    }

    */
}