package com.feed_the_beast.ftbl.gui.friends;

import com.feed_the_beast.ftbl.api.client.gui.GuiIcons;
import com.feed_the_beast.ftbl.api.info.IGuiInfoPageTree;
import com.feed_the_beast.ftbl.api.info.impl.ButtonInfoPage;
import com.feed_the_beast.ftbl.api.info.impl.InfoPage;
import com.feed_the_beast.ftbl.api.notification.ClientNotifications;
import com.feed_the_beast.ftbl.gui.GuiInfo;
import com.latmod.lib.util.LMMapUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 * Created by LatvianModder on 24.03.2016.
 */
@SideOnly(Side.CLIENT)
public class InfoNotificationsPage extends InfoPage
{
    public InfoNotificationsPage()
    {
        setTitle(new TextComponentTranslation("client_config.ftbl.notifications"));
    }

    @Override
    public void refreshGui(@Nonnull GuiInfo gui)
    {
        ITextComponent title = new TextComponentTranslation("client_config.ftbl.notifications");

        if(!ClientNotifications.Perm.map.isEmpty())
        {
            title.appendText(" [");
            ITextComponent num = new TextComponentString(Integer.toString(ClientNotifications.Perm.map.size()));
            num.getStyle().setColor(TextFormatting.RED);
            title.appendSibling(num);
            title.appendText("]");
        }

        setTitle(title);

        clear();

        if(!ClientNotifications.Perm.map.isEmpty())
        {
            LMMapUtils.sortMap(ClientNotifications.Perm.map, (o1, o2) -> Long.compare(o2.getValue().timeAdded, o1.getValue().timeAdded));

            for(ClientNotifications.Perm p : ClientNotifications.Perm.map.values())
            {
                println(new InfoNotificationLine(p));
            }
        }
    }

    @Override
    public ButtonInfoPage createButton(GuiInfo gui, IGuiInfoPageTree p)
    {
        return new ButtonInfoPage(gui, p, GuiIcons.chat);
    }
}
