package ftb.lib.api.info;

import com.google.gson.*;
import ftb.lib.*;
import ftb.lib.api.client.FTBLibClient;
import ftb.lib.mod.client.gui.info.*;
import latmod.lib.LMUtils;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by LatvianModder on 23.03.2016.
 */
public class InfoImageLine extends InfoExtendedTextLine
{
	private String imageURL;
	private TextureCoords texture;
	private double displayW, displayH, displayS;
	
	public InfoImageLine(InfoPage c)
	{
		super(c, null);
	}
	
	@SideOnly(Side.CLIENT)
	public TextureCoords getImage()
	{
		if(texture == TextureCoords.nullTexture) return null;
		else if(texture != null) return texture;
		else if(imageURL == null) return null;
		
		texture = TextureCoords.nullTexture;
		
		try
		{
			File file = new File(FTBLib.folderModpack, "images/" + imageURL);
			if(FTBLib.DEV_ENV) FTBLib.dev_logger.info("Loading Guide image: " + file.getAbsolutePath());
			BufferedImage img = ImageIO.read(file);
			ResourceLocation tex = FTBLibClient.mc.getTextureManager().getDynamicTextureLocation("ftbu_guide/" + imageURL, new DynamicTexture(img));
			texture = new TextureCoords(tex, 0D, 0D, img.getWidth(), img.getHeight(), img.getWidth(), img.getHeight());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return texture;
	}
	
	@SideOnly(Side.CLIENT)
	public TextureCoords getDisplayImage()
	{
		TextureCoords img = getImage();
		if(img == null) return null;
		double w = (displayW > 0D) ? displayW : (displayS == 0D ? texture.width : (displayS > 0D ? texture.width * displayS : ((double) texture.width / -displayS)));
		double h = (displayH > 0D) ? displayH : (displayS == 0D ? texture.height : (displayS > 0D ? texture.height * displayS : ((double) texture.height / -displayS)));
		return new TextureCoords(texture.texture, 0D, 0D, w, h, w, h);
	}
	
	public void setImage(String img)
	{
		String imageURL0 = imageURL == null ? null : (imageURL + "");
		imageURL = img;
		if(!LMUtils.areObjectsEqual(imageURL0, imageURL, true)) texture = null;
		if(imageURL != null) text = null;
	}
	
	public void setImage(TextureCoords t)
	{
		texture = t;
		imageURL = null;
		text = null;
	}
	
	@SideOnly(Side.CLIENT)
	public ButtonInfoTextLine createWidget(GuiInfo gui)
	{
		if(getImage() == null) return null;
		return new ButtonInfoImage(gui, this);
	}
	
	public void fromJson(JsonElement e)
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
	
	public JsonElement getSerializableElement()
	{
		JsonObject o = (JsonObject) super.getSerializableElement();
		
		if(imageURL != null && !imageURL.isEmpty()) o.add("image", new JsonPrimitive(imageURL));
		
		return o;
	}
}