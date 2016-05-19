package com.feed_the_beast.ftbl.gui.info;

import com.feed_the_beast.ftbl.api.MouseButton;
import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.gui.GuiLM;
import com.feed_the_beast.ftbl.api.gui.widgets.ButtonLM;
import com.feed_the_beast.ftbl.api.info.InfoPage;
import com.feed_the_beast.ftbl.util.TextureCoords;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * Created by LatvianModder on 04.03.2016.
 */
public class ButtonInfoPage extends ButtonLM
{
    public final GuiInfo guiInfo;
    public final InfoPage page;
    public String hover;
    public TextureCoords icon;
    public boolean iconBlur = false;
    private boolean prevMouseOver = false;

    public ButtonInfoPage(GuiInfo g, InfoPage p, TextureCoords t)
    {
        super(g, 0, g.panelPages.height, g.panelWidth - 36, t == null ? 13 : 18);
        guiInfo = g;
        page = p;
        icon = t;
        updateTitle();
    }

    public ButtonInfoPage setIconBlur()
    {
        iconBlur = true;
        return this;
    }

    @Override
    public void onClicked(MouseButton button)
    {
        FTBLibClient.playClickSound();

        page.refreshGui(guiInfo);

        if(page.childPages.isEmpty())
        {
            guiInfo.selectedPage = page;
            guiInfo.sliderText.value = 0F;
            guiInfo.panelText.posY = 10;
            guiInfo.panelText.refreshWidgets();
        }
        else
        {
            FTBLibClient.openGui(new GuiInfo(guiInfo, page));
        }
    }

    public void updateTitle()
    {
        ITextComponent titleC = page.getTitleComponent().createCopy();
        if(guiInfo.selectedPage == page)
        {
            titleC.getStyle().setBold(true);
        }

        if(mouseOver())
        {
            titleC.getStyle().setUnderlined(true);
        }

        title = titleC.getFormattedText();
        hover = null;

        if(gui.getFontRenderer().getStringWidth(title) > width)
        {
            hover = page.getTitleComponent().getFormattedText();
        }
    }

    @Override
    public void addMouseOverText(List<String> l)
    {
        if(hover != null)
        {
            l.add(hover);
        }
    }

    @Override
    public void renderWidget()
    {
        int ay = getAY();
        int ax = getAX();

        boolean mouseOver = mouseOver();

        if(prevMouseOver != mouseOver)
        {
            updateTitle();
            prevMouseOver = mouseOver;
        }

        if(icon != null)
        {
            GlStateManager.color(1F, 1F, 1F, 1F);
            FTBLibClient.setTexture(icon.texture);

            if(iconBlur)
            {
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            }

            GuiLM.render(icon, ax + 1, ay + 1, gui.getZLevel(), 16, 16);

            if(iconBlur)
            {
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            }

            guiInfo.getFontRenderer().drawString(title, ax + 19, ay + 6, guiInfo.colorText);
        }
        else
        {
            GlStateManager.color(1F, 1F, 1F, 1F);
            guiInfo.getFontRenderer().drawString(title, ax + 1, ay + 1, guiInfo.colorText);
        }

        GlStateManager.color(1F, 1F, 1F, 1F);
    }
}