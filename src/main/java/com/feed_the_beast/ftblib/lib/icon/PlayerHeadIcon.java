package com.feed_the_beast.ftblib.lib.icon;

import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.feed_the_beast.ftblib.lib.gui.GuiHelper;
import com.feed_the_beast.ftblib.lib.io.HttpConnection;
import com.feed_the_beast.ftblib.lib.io.RequestMethod;
import com.feed_the_beast.ftblib.lib.io.Response;
import com.feed_the_beast.ftblib.lib.util.JsonUtils;
import com.feed_the_beast.ftblib.lib.util.StringUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

/**
 * @author LatvianModder
 */
public class PlayerHeadIcon extends ImageIcon
{
	@SideOnly(Side.CLIENT)
	private static class ThreadLoadSkin extends SimpleTexture
	{
		private final PlayerHeadIcon icon;
		private final IImageBuffer imageBuffer;
		private BufferedImage bufferedImage;
		private Thread imageThread;
		private boolean textureUploaded;

		public ThreadLoadSkin(PlayerHeadIcon i)
		{
			super(DefaultPlayerSkin.getDefaultSkin(i.uuid));
			icon = i;
			imageBuffer = new ImageBufferDownload();
		}

		@Override
		public int getGlTextureId()
		{
			if (!textureUploaded)
			{
				if (bufferedImage != null)
				{
					deleteGlTexture();
					TextureUtil.uploadTextureImage(super.getGlTextureId(), bufferedImage);
					textureUploaded = true;
				}
			}

			return super.getGlTextureId();
		}

		@Override
		public void loadTexture(IResourceManager resourceManager) throws IOException
		{
			if (bufferedImage == null)
			{
				super.loadTexture(resourceManager);
			}

			if (imageThread == null)
			{
				imageThread = new Thread("Skin Downloader for " + icon.uuid)
				{
					@Override
					public void run()
					{
						String imageUrl = "";

						try (Response response = HttpConnection.connection("https://sessionserver.mojang.com/session/minecraft/profile/" + StringUtils.fromUUID(icon.uuid), RequestMethod.GET, HttpConnection.JSON).connect(ClientUtils.MC.getProxy()))
						{
							if (response.isOK())
							{
								JsonObject json = response.asJson().getAsJsonObject();

								for (JsonElement element : json.get("properties").getAsJsonArray())
								{
									if (element.isJsonObject() && element.getAsJsonObject().get("name").getAsString().equals("textures"))
									{
										String base64 = new String(Base64.getDecoder().decode(element.getAsJsonObject().get("value").getAsString()), StandardCharsets.UTF_8);
										JsonObject json1 = JsonUtils.fromJson(base64).getAsJsonObject().get("textures").getAsJsonObject();

										if (json1.has("SKIN"))
										{
											imageUrl = json1.get("SKIN").getAsJsonObject().get("url").getAsString();
										}
									}
								}
							}
						}
						catch (Exception ex)
						{
						}

						if (imageUrl.isEmpty())
						{
							return;
						}

						try (Response response = HttpConnection.connection(imageUrl, RequestMethod.GET, HttpConnection.PNG).connect(ClientUtils.MC.getProxy()))
						{
							if (response.isOK())
							{
								bufferedImage = imageBuffer.parseUserSkin(response.asImage());
							}
						}
						catch (Exception ex)
						{
						}
					}
				};

				imageThread.setDaemon(true);
				imageThread.start();
			}
		}
	}

	public final UUID uuid;

	public PlayerHeadIcon(UUID id)
	{
		super(new ResourceLocation("skins/" + StringUtils.fromUUID(id)));
		uuid = id;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void bindTexture()
	{
		TextureManager manager = ClientUtils.MC.getTextureManager();
		ITextureObject img = manager.getTexture(texture);

		if (img == null)
		{
			img = new ThreadLoadSkin(this);
			manager.loadTexture(texture, img);
		}

		GlStateManager.bindTexture(img.getGlTextureId());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void draw(int x, int y, int w, int h, Color4I col)
	{
		bindTexture();
		col = col.whiteIfEmpty();
		GuiHelper.drawTexturedRect(x, y, w, h, col, 0.125D, 0.125D, 0.25D, 0.25D);
		GuiHelper.drawTexturedRect(x, y, w, h, col, 0.625D, 0.125D, 0.75D, 0.25D);
	}

	@Override
	public String toString()
	{
		return "player:" + uuid;
	}
}