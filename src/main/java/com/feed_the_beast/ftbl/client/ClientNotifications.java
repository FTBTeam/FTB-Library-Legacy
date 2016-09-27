package com.feed_the_beast.ftbl.client;

import com.feed_the_beast.ftbl.api.INotification;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.util.LMColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.util.LinkedHashMap;
import java.util.List;

public class ClientNotifications
{
    private static Temp current;

    public static class NotificationWidget
    {
        public final INotification notification;
        public final String[] text;
        public final int height;
        public int width;

        public NotificationWidget(INotification n)
        {
            notification = n;
            width = 0;

            List<ITextComponent> list = notification.getText();
            text = new String[list.size()];

            for(int i = 0; i < text.length; i++)
            {
                text[i] = list.get(i).getFormattedText();
            }

            if(text.length > 2)
            {
                height = 4 + text.length * 12;
            }
            else
            {
                height = 32;
            }
        }

        public void render(Minecraft mc, int ax, int ay)
        {
            GlStateManager.enableBlend();

            LMColorUtils.setGLColor(LMColorUtils.getColorFromID(notification.getColorID()), 255);
            GuiHelper.drawBlankRect(ax, ay, width, height);

            GlStateManager.color(1F, 1F, 1F, 1F);

            if(notification.getItem() != null)
            {
                GuiHelper.renderGuiItem(mc.getRenderItem(), notification.getItem(), ax + 8, ay + (height - 16D) / 2D);
            }

            for(int i = 0; i < text.length; i++)
            {
                mc.fontRendererObj.drawString(text[i], ax + (notification.getItem() != null ? 30 : 10), (int) (ay + i * 11D + (height - text.length * 10D) / 2D), 0xFFFFFFFF);
            }
        }
    }

    private static class Temp
    {
        private static final LinkedHashMap<ResourceLocation, INotification> MAP = new LinkedHashMap<>();

        private long time;
        private NotificationWidget widget;

        private Temp(INotification n)
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

                if(widget.notification.getItem() != null)
                {
                    widget.width += 20;
                }
            }

            if(time > 0L)
            {
                int timeExisted = (int) (System.currentTimeMillis() - time);
                int timer = widget.notification.getTimer() & 0xFFFF;

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

                GlStateManager.pushMatrix();
                GlStateManager.disableDepth();
                GlStateManager.depthMask(false);
                GlStateManager.disableLighting();
                widget.render(mc, screen.getScaledWidth() - widget.width - 4, (int) (d1 * widget.height) - widget.height);
                GlStateManager.depthMask(true);
                GlStateManager.color(1F, 1F, 1F, 1F);
                GlStateManager.enableLighting();
                GlStateManager.popMatrix();
            }

            return time == 0L;
        }
    }

    public static class Perm implements Comparable<Perm>
    {
        public static final LinkedHashMap<ResourceLocation, Perm> MAP = new LinkedHashMap<>();

        public final INotification notification;
        public final long timeAdded;

        private Perm(INotification n)
        {
            notification = n;
            timeAdded = System.currentTimeMillis();
        }

        @Override
        public int compareTo(Perm o)
        {
            return Long.compare(o.timeAdded, timeAdded);
        }
    }

    static boolean shouldRenderTemp()
    {
        return current != null || !Temp.MAP.isEmpty();
    }

    static void renderTemp(ScaledResolution screen)
    {
        if(current != null)
        {
            if(current.render(screen))
            {
                current = null;
            }
        }
        else if(!Temp.MAP.isEmpty())
        {
            current = new Temp(Temp.MAP.values().iterator().next());
            Temp.MAP.remove(current.widget.notification.getID());
        }
    }

    public static void add(INotification n)
    {
        Perm.MAP.remove(n.getID());
        Temp.MAP.remove(n.getID());

        /*
        if(current != null && current.widget.notification.getID().equals(n.getID()))
        {
            current = null;
        }
        */

        Temp.MAP.put(n.getID(), n);

        if(n.isPermanent())
        {
            Perm.MAP.put(n.getID(), new Perm(n));
        }
    }

    public static void init()
    {
        current = null;
        Perm.MAP.clear();
        Temp.MAP.clear();
    }
}