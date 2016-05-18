package com.feed_the_beast.ftbl.gui;

import com.feed_the_beast.ftbl.api.MouseButton;
import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.gui.GuiLM;
import com.feed_the_beast.ftbl.api.gui.widgets.ButtonLM;
import com.feed_the_beast.ftbl.util.TextureCoords;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiViewImage extends GuiLM
{
    public final GuiScreen parent;
    public final TextureCoords texCoords;
    public final ButtonLM buttonClose;
    
    public GuiViewImage(GuiScreen p, TextureCoords t)
    {
        super(null, null);
        parent = p;
        texCoords = t;
        
        buttonClose = new ButtonLM(this, 0, 0, 0, 0)
        {
            @Override
            public void onClicked(MouseButton button)
            { close(parent); }
        };
    }
    
    @Override
    public void initLMGui()
    {
        mainPanel.width = buttonClose.width = width;
        mainPanel.height = buttonClose.height = height;
    }
    
    @Override
    public void addWidgets()
    {
        mainPanel.add(buttonClose);
    }
    
    @Override
    public void drawBackground()
    {
        super.drawBackground();
        
        if(texCoords != null && texCoords.isValid())
        {
            FTBLibClient.setTexture(texCoords.texture);
            
            double w = texCoords.width;
            double h = texCoords.height;
            
            if(w > width)
            {
                w = width;
                h = texCoords.getHeight(w);
            }
            
            if(h > height)
            {
                h = height;
                w = texCoords.getWidth(h);
            }
            
            GuiLM.render(texCoords, (width - w) / 2, (height - h) / 2, zLevel, w, h);
        }
    }
}