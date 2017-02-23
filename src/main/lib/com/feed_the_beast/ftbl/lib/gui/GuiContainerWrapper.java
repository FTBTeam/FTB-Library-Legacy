package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.api.gui.IClientActionGui;
import com.feed_the_beast.ftbl.api.gui.IGuiWrapper;
import com.feed_the_beast.ftbl.lib.MouseButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

/**
 * Created by LatvianModder on 09.06.2016.
 */
public class GuiContainerWrapper extends GuiContainer implements IGuiWrapper, IClientActionGui
{
    private GuiLM wrappedGui;

    public GuiContainerWrapper(GuiLM g, Container c)
    {
        super(c);
        wrappedGui = g;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        wrappedGui.initGui();
        guiLeft = wrappedGui.getAX();
        guiTop = wrappedGui.getAY();
        xSize = wrappedGui.getWidth();
        ySize = wrappedGui.getHeight();
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return wrappedGui.doesGuiPauseGame();
    }

    @Override
    protected final void mouseClicked(int mx, int my, int b) throws IOException
    {
        wrappedGui.mousePressed(wrappedGui, MouseButton.get(b));
        super.mouseClicked(mx, my, b);
    }

    @Override
    protected void mouseReleased(int mx, int my, int state)
    {
        wrappedGui.mouseReleased(wrappedGui);
        super.mouseReleased(mx, my, state);
    }

    @Override
    protected void keyTyped(char keyChar, int key) throws IOException
    {
        if(wrappedGui.keyPressed(wrappedGui, key, keyChar))
        {
            return;
        }

        if(key == Keyboard.KEY_ESCAPE || mc.gameSettings.keyBindInventory.isActiveAndMatches(key))
        {
            if(wrappedGui.onClosedByKey())
            {
                wrappedGui.closeGui();
            }

            return;
        }

        super.keyTyped(keyChar, key);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mx, int my)
    {
        if(wrappedGui.fixUnicode)
        {
            GuiHelper.setFixUnicode(mc, true);
        }

        GuiLM.setupDrawing();
        wrappedGui.drawBackground();
        GuiLM.setupDrawing();
        wrappedGui.renderWidgets();

        if(wrappedGui.fixUnicode)
        {
            GuiHelper.setFixUnicode(mc, false);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mx, int my)
    {
        if(wrappedGui.fixUnicode)
        {
            GuiHelper.setFixUnicode(mc, true);
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(-guiLeft, -guiTop, 0D);
        GuiLM.setupDrawing();
        wrappedGui.drawForeground();
        GlStateManager.popMatrix();

        if(wrappedGui.fixUnicode)
        {
            GuiHelper.setFixUnicode(mc, false);
        }
    }

    @Override
    public void drawDefaultBackground()
    {
        if(wrappedGui.drawDefaultBackground())
        {
            super.drawDefaultBackground();
        }
    }

    @Override
    public void drawScreen(int mx, int my, float f)
    {
        wrappedGui.updateGui(mx, my, f);
        super.drawScreen(mx, my, f);
    }

    @Override
    public GuiLM getWrappedGui()
    {
        return wrappedGui;
    }

    @Override
    public void onClientDataChanged()
    {
        wrappedGui.onClientDataChanged();
    }
}
