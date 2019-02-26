package com.feed_the_beast.ftblib.lib.icon;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.FTBLibConfig;
import com.feed_the_beast.ftblib.lib.gui.GuiHelper;
import com.feed_the_beast.ftblib.lib.item.ItemStackSerializer;
import com.feed_the_beast.ftblib.lib.util.InvUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author LatvianModder
 */
public class ItemIcon extends Icon
{
	private static class LazyItemIcon extends ItemIcon
	{
		private String lazyStackString;
		private boolean createdStack;

		private LazyItemIcon(String s)
		{
			super(ItemStack.EMPTY);
			lazyStackString = s;
		}

		@Override
		public ItemStack getStack()
		{
			if (!createdStack)
			{
				stack = ItemStackSerializer.parseItem(lazyStackString);
				createdStack = true;

				if (FTBLibConfig.debugging.print_more_errors && stack.isEmpty())
				{
					FTBLib.LOGGER.warn("Couldn't parse item '" + lazyStackString + "'!");
				}
			}

			return stack;
		}

		public String toString()
		{
			return "item:" + lazyStackString;
		}
	}

	ItemStack stack;

	public static Icon getItemIcon(ItemStack stack)
	{
		return stack.isEmpty() ? EMPTY : new ItemIcon(stack);
	}

	public static Icon getItemIcon(Item item)
	{
		return getItemIcon(new ItemStack(item));
	}

	public static Icon getItemIcon(Block block)
	{
		return getItemIcon(new ItemStack(block));
	}

	public static Icon getItemIcon(String lazyStackString)
	{
		return lazyStackString.isEmpty() ? EMPTY : new LazyItemIcon(lazyStackString);
	}

	private ItemIcon(ItemStack is)
	{
		stack = is;
	}

	public ItemStack getStack()
	{
		return stack;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void draw(int x, int y, int w, int h, Color4I col)
	{
		if (!GuiHelper.drawItem(getStack(), x, y, w / 16D, h / 16D, true))
		{
			stack = InvUtils.ERROR_ITEM;
		}
		else
		{
			GuiHelper.setupDrawing();
		}
	}

	@SideOnly(Side.CLIENT)
	public static void drawItem3D(ItemStack stack)
	{
		Minecraft mc = Minecraft.getMinecraft();
		mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
		GlStateManager.pushMatrix();
		GlStateManager.scale(1F, -1F, -0.02F);
		IBakedModel bakedmodel = mc.getRenderItem().getItemModelWithOverrides(stack, mc.world, mc.player);
		bakedmodel = ForgeHooksClient.handleCameraTransforms(bakedmodel, ItemCameraTransforms.TransformType.GUI, false);
		mc.getRenderItem().renderItem(stack, bakedmodel);
		GlStateManager.popMatrix();
		mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void draw3D(Color4I col)
	{
		drawItem3D(getStack());
	}

	public String toString()
	{
		return "item:" + ItemStackSerializer.toString(getStack());
	}

	public boolean equals(Object o)
	{
		return o == this || o instanceof ItemIcon && ItemStack.areItemStacksEqualUsingNBTShareTag(getStack(), ((ItemIcon) o).getStack());
	}
}