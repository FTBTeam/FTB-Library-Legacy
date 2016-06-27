package com.feed_the_beast.ftbl.api.client.gui;

import com.feed_the_beast.ftbl.api.MouseButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

/**
 * Created by LatvianModder on 09.06.2016.
 */
@SideOnly(Side.CLIENT)
public class GuiWrapper extends GuiScreen implements IGuiWrapper, IClientActionGui
{
    private GuiLM wrappedGui;

    public GuiWrapper(GuiLM g)
    {
        wrappedGui = g;
    }

    @Override
    public void initGui()
    {
        super.initGui();
        wrappedGui.initGui();
    }

    @Override
    protected final void mouseClicked(int mx, int my, int b) throws IOException
    {
        wrappedGui.mousePressed(wrappedGui, MouseButton.get(b));
        super.mouseClicked(mx, my, b);
    }

    @Override
    protected void keyTyped(char keyChar, int key) throws IOException
    {
        if(wrappedGui.keyPressed(wrappedGui, key, keyChar))
        {
            return;
        }

        if(key == Keyboard.KEY_ESCAPE || (mc.theWorld != null && mc.gameSettings.keyBindInventory.isActiveAndMatches(key)))
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
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        wrappedGui.updateGui(mouseX, mouseY, partialTicks);

        if(wrappedGui.drawDefaultBackground())
        {
            drawDefaultBackground();
        }

        GuiLM.setupDrawing();
        wrappedGui.drawBackground();
        GuiLM.setupDrawing();
        wrappedGui.renderWidgets();
        GuiLM.setupDrawing();
        wrappedGui.drawForeground();
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