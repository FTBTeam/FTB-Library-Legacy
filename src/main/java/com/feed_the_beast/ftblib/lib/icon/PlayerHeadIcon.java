package com.feed_the_beast.ftblib.lib.icon;

import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.feed_the_beast.ftblib.lib.gui.GuiHelper;
import com.feed_the_beast.ftblib.lib.util.StringUtils;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;

/**
 * @author LatvianModder
 */
public class PlayerHeadIcon extends ImageIcon
{
	public final GameProfile profile;

	public PlayerHeadIcon(GameProfile p)
	{
		super(new ResourceLocation("player", StringUtils.fromUUID(p.getId())), 0.125D, 0.125D, 0.25D, 0.25D);
		profile = p;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void bindTexture()
	{
		ResourceLocation tex;
		Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = ClientUtils.MC.getSkinManager().loadSkinFromCache(profile);

		if (map.containsKey(MinecraftProfileTexture.Type.SKIN))
		{
			tex = ClientUtils.MC.getSkinManager().loadSkin(map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
		}
		else
		{
			tex = DefaultPlayerSkin.getDefaultSkin(EntityPlayer.getUUID(profile));
		}

		ClientUtils.MC.getTextureManager().bindTexture(tex);
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
		return "player:" + StringUtils.fromUUID(profile.getId()) + ":" + profile.getName();
	}
}