package com.feed_the_beast.ftbl.lib.gui.misc;

import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.client.CachedVertexData;
import com.feed_the_beast.ftbl.lib.client.FTBLibClient;
import com.feed_the_beast.ftbl.lib.gui.Button;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.Panel;
import com.feed_the_beast.ftbl.lib.math.MathUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.ChunkPos;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author LatvianModder
 */
public class GuiChunkSelectorBase extends GuiBase
{
    private static final CachedVertexData GRID = new CachedVertexData(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);

    static
    {
        GRID.color.set(128, 128, 128, 50);

        for(int x = 0; x <= GuiConfigs.CHUNK_SELECTOR_TILES_GUI; x++)
        {
            GRID.pos(x * 16, 0D);
            GRID.pos(x * 16, GuiConfigs.CHUNK_SELECTOR_TILES_GUI * 16, 0D);
        }

        for(int y = 0; y <= GuiConfigs.CHUNK_SELECTOR_TILES_GUI; y++)
        {
            GRID.pos(0D, y * 16, 0D);
            GRID.pos(GuiConfigs.CHUNK_SELECTOR_TILES_GUI * 16, y * 16, 0D);
        }
    }

    public class MapButton extends Button
    {
        public final ChunkPos chunkPos;
        public final int index;
        private boolean isSelected = false;

        private MapButton(int x, int y, int i)
        {
            super(x, y, 16, 16);
            posX += (i % GuiConfigs.CHUNK_SELECTOR_TILES_GUI) * width;
            posY += (i / GuiConfigs.CHUNK_SELECTOR_TILES_GUI) * height;
            chunkPos = new ChunkPos(startX + (i % GuiConfigs.CHUNK_SELECTOR_TILES_GUI), startZ + (i / GuiConfigs.CHUNK_SELECTOR_TILES_GUI));
            index = i;
        }

        @Override
        public void onClicked(GuiBase gui, IMouseButton button)
        {
            GuiHelper.playClickSound();
            currentSelectionMode = getSelectionMode(button);

            if(currentSelectionMode == -1)
            {
                onChunksSelected(Collections.singleton(chunkPos));
            }
        }

        @Override
        public void addMouseOverText(GuiBase gui, List<String> list)
        {
            addButtonText(this, list);
        }

        @Override
        public void renderWidget(GuiBase gui)
        {
            int ax = getAX();
            int ay = getAY();

            if(!isSelected && currentSelectionMode != -1 && isMouseOver(this))
            {
                isSelected = true;
            }

            if(isSelected || gui.isMouseOver(this))
            {
                GuiHelper.drawBlankRect(ax, ay, 16, 16, Color4I.WHITE_A33);
            }
        }
    }

    public final int startX, startZ;
    private final MapButton mapButtons[];
    private final Panel panelButtons;
    public int currentSelectionMode = -1;

    public GuiChunkSelectorBase()
    {
        super(GuiConfigs.CHUNK_SELECTOR_TILES_GUI * 16, GuiConfigs.CHUNK_SELECTOR_TILES_GUI * 16);

        startX = MathUtils.chunk(mc.player.posX) - GuiConfigs.CHUNK_SELECTOR_TILES_GUI2;
        startZ = MathUtils.chunk(mc.player.posZ) - GuiConfigs.CHUNK_SELECTOR_TILES_GUI2;

        panelButtons = new Panel(0, 0, 16, 0)
        {
            @Override
            public void addWidgets()
            {
                addCornerButtons(panelButtons);
            }
        };

        mapButtons = new MapButton[GuiConfigs.CHUNK_SELECTOR_TILES_GUI * GuiConfigs.CHUNK_SELECTOR_TILES_GUI];

        for(int i = 0; i < mapButtons.length; i++)
        {
            mapButtons[i] = new MapButton(0, 0, i);
        }
    }

    @Override
    public void addWidgets()
    {
        for(MapButton b : mapButtons)
        {
            add(b);
        }

        add(panelButtons);
        panelButtons.posX = getScreen().getScaledWidth() - 16 - posX;
        panelButtons.posY = -posY;
        panelButtons.setHeight(panelButtons.widgets.size() * 16);
    }

    @Override
    public void drawBackground()
    {
        GlStateManager.color(1F, 1F, 1F, 1F);
        GuiHelper.drawBlankRect(posX - 2, posY - 2, width + 4, height + 4, Color4I.BLACK);
        //drawBlankRect((xSize - 128) / 2, (ySize - 128) / 2, zLevel, 128, 128);

        ThreadReloadChunkSelector.updateTexture();
        GlStateManager.bindTexture(ThreadReloadChunkSelector.getTextureID());
        GuiHelper.drawTexturedRect(posX, posY, GuiConfigs.CHUNK_SELECTOR_TILES_GUI * 16, GuiConfigs.CHUNK_SELECTOR_TILES_GUI * 16, Color4I.WHITE, 0D, 0D, GuiConfigs.CHUNK_SELECTOR_UV, GuiConfigs.CHUNK_SELECTOR_UV);

        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.enableTexture2D();

        for(MapButton mapButton : mapButtons)
        {
            mapButton.renderWidget(this);
        }

        GlStateManager.disableTexture2D();
        GlStateManager.glLineWidth(1F);

        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer buffer = tessellator.getBuffer();
        buffer.setTranslation(mapButtons[0].getAX(), mapButtons[0].getAY(), 0D);
        //GlStateManager.color(1F, 1F, 1F, GuiScreen.isCtrlKeyDown() ? 0.2F : 0.7F);
        GlStateManager.color(1F, 1F, 1F, 1F);
        drawArea(tessellator, buffer);
        GRID.draw(tessellator, buffer);
        buffer.setTranslation(0D, 0D, 0D);
        GlStateManager.enableTexture2D();

        int cx = MathUtils.chunk(mc.player.posX);
        int cy = MathUtils.chunk(mc.player.posZ);

        if(cx >= startX && cy >= startZ && cx < startX + GuiConfigs.CHUNK_SELECTOR_TILES_GUI && cy < startZ + GuiConfigs.CHUNK_SELECTOR_TILES_GUI)
        {
            double x = ((cx - startX) * 16D + MathUtils.wrap(mc.player.posX, 16D));
            double y = ((cy - startZ) * 16D + MathUtils.wrap(mc.player.posZ, 16D));

            GlStateManager.pushMatrix();
            GlStateManager.translate(posX + x, posY + y, 0D);
            GlStateManager.pushMatrix();
            //GlStateManager.rotate((int)((ep.rotationYaw + 180F) / (180F / 8F)) * (180F / 8F), 0F, 0F, 1F);
            GlStateManager.rotate(mc.player.rotationYaw + 180F, 0F, 0F, 1F);
            GuiConfigs.TEX_ENTITY.draw(-8, -8, 16, 16, Color4I.WHITE_A33);
            GlStateManager.popMatrix();
            FTBLibClient.localPlayerHead.draw(-2, -2, 4, 4, Color4I.NONE);
            GlStateManager.popMatrix();
        }

        GlStateManager.color(1F, 1F, 1F, 1F);
    }

    @Override
    public void mouseReleased(GuiBase gui)
    {
        super.mouseReleased(gui);

        if(currentSelectionMode != -1)
        {
            Collection<ChunkPos> c = new ArrayList<>();

            for(MapButton b : mapButtons)
            {
                if(b.isSelected)
                {
                    c.add(b.chunkPos);
                    b.isSelected = false;
                }
            }

            onChunksSelected(c);
            currentSelectionMode = -1;
        }
    }

    @Override
    public void drawForeground()
    {
        addCornerText(TEMP_TEXT_LIST);

        for(int i = 0; i < TEMP_TEXT_LIST.size(); i++)
        {
            String s = TEMP_TEXT_LIST.get((TEMP_TEXT_LIST.size() - 1) - i);
            getFont().drawStringWithShadow(s, getScreen().getScaledWidth() - getFont().getStringWidth(s) - 4, getScreen().getScaledHeight() - 12 - i * 12, 0xFFFFFFFF);
        }

        TEMP_TEXT_LIST.clear();
        super.drawForeground();
    }

    public int getSelectionMode(IMouseButton button)
    {
        return -1;
    }

    public void onChunksSelected(Collection<ChunkPos> chunks)
    {
    }

    public void drawArea(Tessellator tessellator, VertexBuffer buffer)
    {
    }

    public void addCornerButtons(Panel panel)
    {
    }

    public void addCornerText(List<String> list)
    {
    }

    public void addButtonText(MapButton button, List<String> list)
    {
    }
}
