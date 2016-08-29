package com.feed_the_beast.ftbl.api.info.impl;

import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.gui.GuiLM;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.api.gui.widgets.ButtonLM;
import com.feed_the_beast.ftbl.api.info.IGuiInfoPage;
import com.feed_the_beast.ftbl.gui.GuiInfo;
import com.latmod.lib.ITextureCoordsProvider;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by LatvianModder on 04.03.2016.
 */
public class ButtonInfoPage extends ButtonLM
{
    public final GuiInfo guiInfo;
    public final IGuiInfoPage page;
    public String hover;
    public ITextureCoordsProvider icon;
    private boolean prevMouseOver = false;

    public ButtonInfoPage(GuiInfo g, IGuiInfoPage p, @Nullable ITextureCoordsProvider t)
    {
        super(0, g.panelPages.height, g.panelWidth - 36, t == null ? 13 : 18);
        guiInfo = g;
        page = p;
        icon = t;
        updateTitle(g);
    }

    public boolean isIconBlurry(GuiLM gui)
    {
        return false;
    }

    @Override
    public void onClicked(GuiLM gui, IMouseButton button)
    {
        GuiLM.playClickSound();
        guiInfo.setSelectedPage(page);
    }

    public void updateTitle(GuiLM gui)
    {
        ITextComponent titleC = page.getDisplayName().createCopy();

        if(guiInfo.getSelectedPage() == page)
        {
            titleC.getStyle().setBold(true);
        }

        if(gui.isMouseOver(this))
        {
            titleC.getStyle().setUnderlined(true);
        }

        setTitle(titleC.getFormattedText());
        hover = null;

        if(guiInfo.font.getStringWidth(getTitle(gui)) > width)
        {
            hover = page.getDisplayName().getFormattedText();
        }
    }

    @Override
    public void addMouseOverText(GuiLM gui, List<String> l)
    {
        if(hover != null)
        {
            l.add(hover);
        }
    }

    @Override
    public boolean shouldRender(GuiLM gui)
    {
        return getParentWidget().isInside(this);
    }

    @Override
    public void renderWidget(GuiLM gui)
    {
        boolean mouseOver = gui.isMouseOver(this);

        if(prevMouseOver != mouseOver)
        {
            updateTitle(gui);
            prevMouseOver = mouseOver;
        }

        int ay = getAY();
        int ax = getAX();

        if(icon != null)
        {
            GlStateManager.color(1F, 1F, 1F, 1F);
            FTBLibClient.setTexture(icon.getTextureCoords().getTexture());

            boolean iconBlur = isIconBlurry(gui);

            if(iconBlur)
            {
                GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
                GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            }

            GuiLM.render(icon.getTextureCoords(), ax + 1, ay + 1, 16, 16);

            if(iconBlur)
            {
                GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
                GlStateManager.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            }

            guiInfo.font.drawString(getTitle(gui), ax + 19, ay + 6, guiInfo.colorText);
        }
        else
        {
            GlStateManager.color(1F, 1F, 1F, 1F);
            guiInfo.font.drawString(getTitle(gui), ax + 1, ay + 1, guiInfo.colorText);
        }

        GlStateManager.color(1F, 1F, 1F, 1F);
    }
}