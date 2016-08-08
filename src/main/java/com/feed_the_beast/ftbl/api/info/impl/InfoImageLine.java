package com.feed_the_beast.ftbl.api.info.impl;

import com.feed_the_beast.ftbl.api.client.gui.widgets.ButtonLM;
import com.feed_the_beast.ftbl.api.info.IGuiInfoPage;
import com.feed_the_beast.ftbl.gui.info.ButtonInfoImage;
import com.feed_the_beast.ftbl.gui.info.GuiInfo;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.latmod.lib.util.LMUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.awt.image.BufferedImage;

/**
 * Created by LatvianModder on 23.03.2016.
 */
public class InfoImageLine extends InfoExtendedTextLine
{
    public String imageURL;
    private InfoImage texture;
    private double displayW, displayH, displayS;

    public InfoImageLine(InfoImage img)
    {
        super(null);
        texture = img;
    }

    @SideOnly(Side.CLIENT)
    public InfoImage getImage(IGuiInfoPage page)
    {
        if(texture == InfoImage.NULL)
        {
            return null;
        }
        else if(texture != null)
        {
            return texture;
        }

        texture = InfoImage.NULL;

        try
        {
            //File file = new File(FTBLib.folderModpack, "images/" + imageURL);
            //if(FTBLib.DEV_ENV) { FTBLib.dev_logger.info("Loading Guide image: " + file.getAbsolutePath()); }
            BufferedImage img = page.getResourceProvider().getConnection(imageURL).connect().asImage();
            ResourceLocation tex = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("ftbu_guide/" + imageURL, new DynamicTexture(img));
            texture = new InfoImage(tex, img.getWidth(), img.getHeight());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return texture;
    }

    public InfoImageLine setImage(String img)
    {
        String imageURL0 = imageURL == null ? null : (imageURL + "");
        imageURL = img;

        if(!LMUtils.areObjectsEqual(imageURL0, imageURL, true))
        {
            texture = null;
        }

        return this;
    }

    @SideOnly(Side.CLIENT)
    public InfoImage getDisplayImage(IGuiInfoPage page)
    {
        InfoImage img = getImage(page);

        if(img == null)
        {
            return null;
        }

        double w = (displayW > 0D) ? displayW : (displayS == 0D ? texture.width : (displayS > 0D ? texture.width * displayS : (texture.width / -displayS)));
        double h = (displayH > 0D) ? displayH : (displayS == 0D ? texture.height : (displayS > 0D ? texture.height * displayS : (texture.height / -displayS)));
        return new InfoImage(texture.texture, (int) w, (int) h);
    }

    public InfoImageLine setDisplaySize(double w, double h)
    {
        displayW = w;
        displayH = h;
        return this;
    }

    @Override
    @Nonnull
    @SideOnly(Side.CLIENT)
    public ButtonLM createWidget(GuiInfo gui, IGuiInfoPage page)
    {
        return new ButtonInfoImage(gui, this, getDisplayImage(page));
    }

    @Override
    public void fromJson(@Nonnull JsonElement e)
    {
        super.fromJson(e);

        displayW = displayH = displayS = 0D;

        JsonObject o = e.getAsJsonObject();

        setImage(o.has("image") ? o.get("image").getAsString() : null);

        if(o.has("scale"))
        {
            displayS = o.get("scale").getAsDouble();
        }
        else
        {
            if(o.has("width"))
            {
                displayW = o.get("width").getAsDouble();
            }

            if(o.has("height"))
            {
                displayH = o.get("height").getAsDouble();
            }
        }
    }

    @Nonnull
    @Override
    public JsonElement getSerializableElement()
    {
        JsonObject o = (JsonObject) super.getSerializableElement();

        if(imageURL != null && !imageURL.isEmpty())
        {
            o.add("image", new JsonPrimitive(imageURL));
        }

        return o;
    }
}