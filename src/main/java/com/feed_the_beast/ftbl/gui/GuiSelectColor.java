package com.feed_the_beast.ftbl.gui;

import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.lib.gui.ButtonLM;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.GuiLM;
import com.feed_the_beast.ftbl.lib.util.LMColorUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class GuiSelectColor extends GuiLM
{
    public interface Callback
    {
        void onCallback(@Nullable Object id, byte value);
    }

    private class ButtonColor extends ButtonLM
    {
        private final byte colID;
        private final int col;

        private ButtonColor(int x, int y, byte cid)
        {
            super(x, y, 16, 16);
            colID = cid;
            col = LMColorUtils.getColorFromID(colID);
            setTitle((colID & 0xFF) + ": " + (colID == 0 ? "-" : LMColorUtils.getHex(col)));
        }

        @Override
        public void onClicked(IGui gui, IMouseButton button)
        {
            GuiHelper.playClickSound();
            callback.onCallback(ID, colID);
        }
    }

    private final Object ID;
    private final byte initCol;
    private final Callback callback;
    private final List<ButtonColor> colorButtons;
    private ButtonColor noColorButton;

    public static void display(@Nullable Object id, byte col, Callback c)
    {
        new GuiSelectColor(id, col, c).openGui();
    }

    private GuiSelectColor(@Nullable Object id, byte col, Callback c)
    {
        super(256, 256);
        ID = id;
        initCol = col;
        callback = c;

        colorButtons = new ArrayList<>();

        for(int y = 0; y < 16; y++)
        {
            for(int x = 0; x < 16; x++)
            {
                ButtonColor b = new ButtonColor(x * 16, y * 16, (byte) (x + (16 - y - 1) * 16));
                colorButtons.add(b);

                if(b.colID == 0)
                {
                    noColorButton = b;
                }
            }
        }
    }

    @Override
    public void addWidgets()
    {
        addAll(colorButtons);
    }

    @Override
    public void renderWidgets()
    {
        GlStateManager.disableTexture2D();
        GlStateManager.color(1F, 1F, 1F, 1F);

        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

        int ax = getAX();
        int ay = getAY();
        int w = getWidth();
        int h = getHeight();

        int r = 26;
        int g = 26;
        int b = 26;
        int a = 90;

        buffer.pos(ax, ay + h, 0D).color(r, g, b, a).endVertex();
        buffer.pos(ax + w, ay + h, 0D).color(r, g, b, a).endVertex();
        buffer.pos(ax + w, ay, 0D).color(r, g, b, a).endVertex();
        buffer.pos(ax, ay, 0D).color(r, g, b, a).endVertex();

        a = 255;

        for(ButtonColor button : colorButtons)
        {
            ax = button.getAX() - 1;
            ay = button.getAY() - 1;
            w = button.getWidth() + 2;
            h = button.getHeight() + 2;

            if(initCol == button.colID)
            {
                r = g = b = 180 + (int) (Math.sin(System.currentTimeMillis() * 0.003D) * 60D);

                buffer.pos(ax, ay + h, 0D).color(r, g, b, a).endVertex();
                buffer.pos(ax + w, ay + h, 0D).color(r, g, b, a).endVertex();
                buffer.pos(ax + w, ay, 0D).color(r, g, b, a).endVertex();
                buffer.pos(ax, ay, 0D).color(r, g, b, a).endVertex();
            }

            ax += 2;
            ay += 2;
            w -= 4;
            h -= 4;

            r = LMColorUtils.getRed(button.col);
            g = LMColorUtils.getGreen(button.col);
            b = LMColorUtils.getBlue(button.col);

            buffer.pos(ax, ay + h, 0D).color(r, g, b, a).endVertex();
            buffer.pos(ax + w, ay + h, 0D).color(r, g, b, a).endVertex();
            buffer.pos(ax + w, ay, 0D).color(r, g, b, a).endVertex();
            buffer.pos(ax, ay, 0D).color(r, g, b, a).endVertex();
        }

        tessellator.draw();

        ax = noColorButton.getAX() + 1;
        ay = noColorButton.getAY() + 1;
        w = noColorButton.getWidth() - 2;
        h = noColorButton.getHeight() - 2;

        r = 255;
        g = 30;
        b = 30;
        a = 240;

        buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(ax, ay, 0D).color(r, g, b, a).endVertex();
        buffer.pos(ax + w, ay + h, 0D).color(r, g, b, a).endVertex();
        buffer.pos(ax + w, ay, 0D).color(r, g, b, a).endVertex();
        buffer.pos(ax, ay + h, 0D).color(r, g, b, a).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture2D();
    }

    @Override
    public void drawBackground()
    {
        GlStateManager.color(0.5F, 0.5F, 0.5F, 0.3F);
        GuiHelper.drawBlankRect(getAX(), getAY(), getWidth(), getHeight());
        GlStateManager.color(1F, 1F, 1F, 1F);
    }
}