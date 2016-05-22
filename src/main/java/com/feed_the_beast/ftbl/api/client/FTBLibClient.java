package com.feed_the_beast.ftbl.api.client;

import com.feed_the_beast.ftbl.api.gui.IClientActionGui;
import com.feed_the_beast.ftbl.util.TextureCoords;
import latmod.lib.LMColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SideOnly(Side.CLIENT)
public class FTBLibClient
{
    public static final Minecraft mc = FMLClientHandler.instance().getClient();
    private static final Map<String, ResourceLocation> cachedSkins = new HashMap<>();
    public static int displayW, displayH;
    private static float lastBrightnessX, lastBrightnessY;
    private static EntityItem entityItem;

    // - Registry - //

    public static <T extends Entity> void addEntityRenderer(Class<T> c, IRenderFactory<? super T> r)
    {
        RenderingRegistry.registerEntityRenderingHandler(c, r);
    }

    public static <T extends TileEntity> void addTileRenderer(Class<T> c, TileEntitySpecialRenderer<? super T> r)
    {
        ClientRegistry.bindTileEntitySpecialRenderer(c, r);
    }

    public static KeyBinding addKeyBinding(String name, int key, String cat)
    {
        KeyBinding k = new KeyBinding(name, key, cat);
        ClientRegistry.registerKeyBinding(k);
        return k;
    }

    // -- //

    public static boolean isIngame()
    {
        return mc.theWorld != null && mc.thePlayer != null && mc.thePlayer.worldObj != null;
    }

    public static int getDim()
    {
        return isIngame() ? mc.theWorld.provider.getDimension() : 0;
    }

    public static UUID getUUID()
    {
        return mc.getSession().getProfile().getId();
    }

    public static void spawnPart(Particle e)
    {
        mc.effectRenderer.addEffect(e);
    }

    public static void onGuiClientAction()
    {
        if(mc.currentScreen instanceof IClientActionGui)
        {
            ((IClientActionGui) mc.currentScreen).onClientDataChanged();
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

    public static EntityPlayerSP getPlayerSP(UUID uuid)
    {
        //getMinecraft().getIntegratedServer().getConfigurationManager().playerEntityList

        if(uuid == null)
        {
            return mc.thePlayer;
        }

        if(mc.theWorld != null)
        {
            EntityPlayer ep = mc.theWorld.getPlayerEntityByUUID(uuid);
            if(ep != null && ep instanceof EntityPlayerSP)
            {
                return (EntityPlayerSP) ep;
            }
        }

        return null;
    }

    public static ThreadDownloadImageData getDownloadImage(ResourceLocation out, String url, ResourceLocation def, IImageBuffer buffer)
    {
        TextureManager t = mc.getTextureManager();
        ThreadDownloadImageData img = (ThreadDownloadImageData) t.getTexture(out);

        if(img == null)
        {
            img = new ThreadDownloadImageData(null, url, def, buffer);
            t.loadTexture(out, img);
        }

        return img;
    }

    public static void playClickSound()
    {
        mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1F));
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

    public static ByteBuffer toByteBuffer(int pixels[], boolean alpha)
    {
        if(pixels == null)
        {
            return null;
        }
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

    public static void execClientCommand(String s)
    {
        mc.ingameGUI.getChatGUI().addToSentMessages(s);
        if(ClientCommandHandler.instance.executeCommand(mc.thePlayer, s) == 0)
        {
            mc.thePlayer.sendChatMessage(s);
        }
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
            {
                e.printStackTrace();
            }
        }

        return r;
    }

    public static void setTexture(TextureCoords tex)
    {
        if(tex != null)
        {
            setTexture(tex.texture);
        }
    }

    public static void setTexture(ResourceLocation tex)
    {
        if(tex != null)
        {
            mc.getTextureManager().bindTexture(tex);
        }
    }

    public static void clearCachedData()
    {
        cachedSkins.clear();
    }

    public static boolean canRenderGui()
    {
        return mc.currentScreen == null || mc.currentScreen instanceof GuiChat;
    }

    public static void renderItem(World w, ItemStack is)
    {
        if(w == null || is == null)
        {
            return;
        }
        if(entityItem == null)
        {
            entityItem = new EntityItem(w);
        }
        entityItem.worldObj = w;
        entityItem.hoverStart = 0F;
        entityItem.setEntityItemStack(is);
        mc.getRenderManager().doRenderEntity(entityItem, 0D, 0D, 0D, 0F, 0F, true);
    }

    public static void drawOutlinedBoundingBoxGL(AxisAlignedBB bb)
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

    public static void renderGuiItem(ItemStack is, RenderItem itemRender, FontRenderer font, int x, int y)
    {
        if(is == null || is.getItem() == null)
        {
            return;
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0F);
        GlStateManager.enableLighting();
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableRescaleNormal();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
        GlStateManager.color(1F, 1F, 1F, 1F);
        itemRender.renderItem(is, itemRender.getItemModelMesher().getItemModel(is));
        GlStateManager.popMatrix();
    }

    public static void openGui(GuiScreen gui)
    {
        if(gui == null && mc.thePlayer != null)
        {
            mc.thePlayer.closeScreen();
        }
        else
        {
            mc.displayGuiScreen(gui);
        }
    }

    public static int computeGuiScale()
    {
        Minecraft mc = Minecraft.getMinecraft();
        int scaleFactor = 1;

        int k = mc.gameSettings.guiScale;

        if(k == 0)
        {
            k = 1000;
        }

        while(scaleFactor < k && mc.displayWidth / (scaleFactor + 1) >= 320 && mc.displayHeight / (scaleFactor + 1) >= 240)
        {
            ++scaleFactor;
        }
        return scaleFactor;
    }
}