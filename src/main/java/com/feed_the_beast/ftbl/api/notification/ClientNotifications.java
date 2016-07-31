package com.feed_the_beast.ftbl.api.notification;

import com.feed_the_beast.ftbl.api.MouseButton;
import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.client.gui.GuiLM;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@SideOnly(Side.CLIENT)
public class ClientNotifications
{
    private static Temp current = null;

    public static class NotificationWidget
    {
        public final Notification notification;
        public final List<String> text;
        public final double height;
        public double width;

        public NotificationWidget(Notification n)
        {
            notification = n;
            text = new ArrayList<>();
            width = 0;

            for(ITextComponent t : notification.text)
            {
                String s = t.getFormattedText();
                text.add(s);
            }

            if(notification.text.size() > 2)
            {
                height = 4 + notification.text.size() * 12;
            }
            else
            {
                height = 32;
            }
        }

        public void render(Minecraft mc, double ax, double ay)
        {
            GlStateManager.enableBlend();

            FTBLibClient.setGLColor(notification.getColor(), 255);
            GuiLM.drawBlankRect(ax, ay, width, height);

            GlStateManager.color(1F, 1F, 1F, 1F);

            if(notification.hasItem())
            {
                GuiLM.renderGuiItem(mc.getRenderItem(), notification.getItem(), ax + 8, ay + (height - 16D) / 2D);
            }

            for(int i = 0; i < text.size(); i++)
            {
                mc.fontRendererObj.drawString(text.get(i), (int) ax + (notification.hasItem() ? 30 : 10), (int) (ay + i * 11D + (height - text.size() * 10D) / 2D), 0xFFFFFFFF);
            }
        }
    }

    public static class Temp
    {
        public static final LinkedHashMap<Integer, Temp> map = new LinkedHashMap<>();

        private long time;
        private NotificationWidget widget;

        private Temp(Notification n)
        {
            widget = new NotificationWidget(n);
            widget.width = 0;
            time = -1L;
        }

        public boolean render(ScaledResolution screen)
        {
            if(time == -1L)
            {
                time = System.currentTimeMillis();
            }

            Minecraft mc = Minecraft.getMinecraft();

            if(widget.width == 0)
            {
                for(String s : widget.text)
                {
                    widget.width = Math.max(widget.width, mc.fontRendererObj.getStringWidth(s));
                }

                widget.width += 20;

                if(widget.notification.hasItem())
                {
                    widget.width += 20;
                }
            }

            if(time > 0L)
            {
                int timeExisted = (int) (System.currentTimeMillis() - time);
                int timer = widget.notification.getTimer();

                if(timeExisted > timer)
                {
                    time = 0L;
                    return true;
                }

                double d1 = 1D;

                if(timer - timeExisted < 300)
                {
                    d1 = (timer - timeExisted) / 300D;
                }

                if(timeExisted < 300)
                {
                    d1 = timeExisted / 300D;
                }

                GlStateManager.disableDepth();
                GlStateManager.depthMask(false);
                GlStateManager.disableLighting();
                widget.render(mc, screen.getScaledWidth() - widget.width - 4, d1 * widget.height - widget.height);
                GlStateManager.depthMask(true);
                GlStateManager.color(1F, 1F, 1F, 1F);
                GlStateManager.enableLighting();
            }

            return time == 0L;
        }
    }

    public static class Perm implements Comparable<Perm>
    {
        public static final LinkedHashMap<Integer, Perm> map = new LinkedHashMap<>();

        public final Notification notification;
        public final long timeAdded;

        private Perm(Notification n)
        {
            notification = n;
            timeAdded = System.currentTimeMillis();
        }

        @Override
        public int compareTo(@Nonnull Perm o)
        {
            return Long.compare(o.timeAdded, timeAdded);
        }

        public void onClicked(MouseButton button)
        {
            if(notification.getClickAction() != null)
            {
                notification.getClickAction().onClicked(button);
            }
        }
    }

    public static void renderTemp(ScaledResolution screen)
    {
        if(current != null)
        {
            if(current.render(screen))
            {
                current = null;
            }
        }
        else if(!Temp.map.isEmpty())
        {
            current = Temp.map.values().iterator().next();
            Temp.map.remove(current.widget.notification.ID);
        }
    }

    public static void add(Notification n)
    {
        if(n != null)
        {
            Temp.map.remove(n.ID);
            Perm.map.remove(n.ID);

            if(current != null && current.widget.notification.ID == n.ID)
            {
                current = null;
            }

            Temp.map.put(n.ID, new Temp(n));

            if(!n.isTemp())
            {
                Perm.map.put(n.ID, new Perm(n));
            }
        }
    }

    public static void init()
    {
        current = null;
        Perm.map.clear();
        Temp.map.clear();
    }
}