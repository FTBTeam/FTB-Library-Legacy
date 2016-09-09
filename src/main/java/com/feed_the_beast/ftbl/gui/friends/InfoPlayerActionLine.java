package com.feed_the_beast.ftbl.gui.friends;

import com.feed_the_beast.ftbl.api.gui.GuiHelper;
import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.api.gui.ISidebarButton;
import com.feed_the_beast.ftbl.api.gui.widgets.ButtonLM;
import com.feed_the_beast.ftbl.api.info.IGuiInfoPage;
import com.feed_the_beast.ftbl.api.info.impl.ButtonInfoTextLine;
import com.feed_the_beast.ftbl.api.info.impl.EmptyInfoPageLine;
import com.feed_the_beast.ftbl.gui.GuiInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Created by LatvianModder on 23.03.2016.
 */
public class InfoPlayerActionLine extends EmptyInfoPageLine
{
    public class ButtonInfoPlayerAction extends ButtonInfoTextLine
    {
        public ButtonInfoPlayerAction(GuiInfo g)
        {
            super(g, null);
            setHeight(18);
            ITextComponent c = action.getDisplayNameOverride();
            setTitle(((c == null) ? new TextComponentTranslation("sidebar_button." + actionID) : c).getFormattedText());
            setWidth((action.getIcon() == null ? 8 : 24) + g.getFont().getStringWidth(getTitle(g)));
        }

        @Override
        public void addMouseOverText(IGui gui, List<String> l)
        {
        }

        @Override
        public void onClicked(IGui gui, IMouseButton button)
        {
            GuiHelper.playClickSound();
            action.onClicked(button);
        }

        @Override
        public void renderWidget(IGui gui)
        {
            int ay = getAY();
            int ax = getAX();

            GlStateManager.enableBlend();

            if(gui.isMouseOver(this))
            {
                GlStateManager.color(1F, 1F, 1F, 0.2F);
                GuiHelper.drawBlankRect(ax, ay, getWidth(), getHeight());
            }

            GlStateManager.color(1F, 1F, 1F, 1F);

            action.render(ax + 1, ay + 1);
            action.postRender(ax + 1, ay + 1);

            gui.getFont().drawString(getTitle(gui), ax + (action.getIcon() == null ? 4 : 20), ay + 5, ((GuiInfo) gui).colorText);
        }
    }

    public final ResourceLocation actionID;
    public final ISidebarButton action;

    public InfoPlayerActionLine(ResourceLocation id, ISidebarButton a)
    {
        actionID = id;
        action = a;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ButtonLM createWidget(GuiInfo gui, IGuiInfoPage page)
    {
        return new ButtonInfoPlayerAction(gui);
    }
}
