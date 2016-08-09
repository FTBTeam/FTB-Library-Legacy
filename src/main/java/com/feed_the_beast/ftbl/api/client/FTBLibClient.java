package com.feed_the_beast.ftbl.api.client;

import com.feed_the_beast.ftbl.api.client.gui.IClientActionGui;
import com.latmod.lib.util.LMColorUtils;
import com.latmod.lib.util.LMUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class FTBLibClient
{
    public static final Frustum frustum = new Frustum();
    private static final Map<String, ResourceLocation> cachedSkins = new HashMap<>();
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
    private static float lastBrightnessX, lastBrightnessY;
    private static EntityItem entityItem;
    //private static IntBuffer VIEWPORT;
    //private static FloatBuffer MODELVIEW, PROJECTION;

    // - Registry - //

    public static <T extends Entity> void addEntityRenderer(@Nonnull Class<T> c, @Nonnull IRenderFactory<? super T> r)
    {
        RenderingRegistry.registerEntityRenderingHandler(c, r);
    }

    @Nonnull
    public static KeyBinding addKeyBinding(@Nonnull KeyBinding k)
    {
        ClientRegistry.registerKeyBinding(k);
        return k;
    }

    // -- //

    public static int getDim()
    {
        Minecraft mc = Minecraft.getMinecraft();
        return mc.thePlayer != null ? mc.theWorld.provider.getDimension() : 0;
    }

    public static void spawnPart(@Nonnull Particle e)
    {
        Minecraft.getMinecraft().effectRenderer.addEffect(e);
    }

    public static void onGuiClientAction()
    {
        GuiScreen screen = Minecraft.getMinecraft().currentScreen;

        if(screen instanceof IClientActionGui)
        {
            ((IClientActionGui) screen).onClientDataChanged();
        }
    }

    public static void pushMaxBrightness()
    {
        lastBrightnessX = OpenGlHelper.lastBrightnessX;
        lastBrightnessY = OpenGlHelper.lastBrightnessY;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
    }

    public static void popMaxBrightness()
    {
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastBrightnessX, lastBrightnessY);
    }

    public static ThreadDownloadImageData getDownloadImage(@Nonnull ResourceLocation out, @Nonnull String url, @Nonnull ResourceLocation def, @Nullable IImageBuffer buffer)
    {
        TextureManager t = Minecraft.getMinecraft().getTextureManager();
        ThreadDownloadImageData img = (ThreadDownloadImageData) t.getTexture(out);

        if(img == null)
        {
            img = new ThreadDownloadImageData(null, url, def, buffer);
            t.loadTexture(out, img);
        }

        return img;
    }

    public static void setGLColor(int c, int a)
    {
        int r = LMColorUtils.getRed(c);
        int g = LMColorUtils.getGreen(c);
        int b = LMColorUtils.getBlue(c);
        GlStateManager.color(r / 255F, g / 255F, b / 255F, a / 255F);
    }

    public static void setGLColor(int c)
    {
        setGLColor(c, LMColorUtils.getAlpha(c));
    }

    @Nonnull
    public static ByteBuffer toByteBuffer(@Nonnull int pixels[], boolean alpha)
    {
        ByteBuffer bb = BufferUtils.createByteBuffer(pixels.length * 4);
        byte alpha255 = (byte) 255;

        for(int p : pixels)
        {
            bb.put((byte) LMColorUtils.getRed(p));
            bb.put((byte) LMColorUtils.getGreen(p));
            bb.put((byte) LMColorUtils.getBlue(p));
            bb.put(alpha ? (byte) LMColorUtils.getAlpha(p) : alpha255);
        }

        bb.flip();
        return bb;
    }

    public static void execClientCommand(@Nonnull String s, boolean printChat)
    {
        Minecraft mc = Minecraft.getMinecraft();

        if(printChat)
        {
            mc.ingameGUI.getChatGUI().addToSentMessages(s);
        }

        if(ClientCommandHandler.instance.executeCommand(mc.thePlayer, s) == 0)
        {
            mc.thePlayer.sendChatMessage(s);
        }
    }

    @Nonnull
    public static ResourceLocation getSkinTexture(@Nonnull String username)
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
            {
                e.printStackTrace();
            }
        }

        return r;
    }

    public static void setTexture(@Nonnull ResourceLocation tex)
    {
        Minecraft.getMinecraft().getTextureManager().bindTexture(tex);
    }

    public static void clearCachedData()
    {
        cachedSkins.clear();
    }

    public static void renderItem(@Nonnull World w, @Nonnull ItemStack is)
    {
        if(entityItem == null)
        {
            entityItem = new EntityItem(w);
        }

        entityItem.worldObj = w;
        entityItem.hoverStart = 0F;
        entityItem.setEntityItemStack(is);
        Minecraft.getMinecraft().getRenderManager().doRenderEntity(entityItem, 0D, 0D, 0D, 0F, 0F, true);
    }

    public static void drawOutlinedBoundingBox(@Nonnull AxisAlignedBB bb)
    {
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer buffer = tessellator.getBuffer();

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
        Minecraft mc = Minecraft.getMinecraft();
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