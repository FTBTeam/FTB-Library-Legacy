package com.feed_the_beast.ftblib.lib.util;

import com.feed_the_beast.ftblib.lib.ATHelper;
import com.feed_the_beast.ftblib.lib.item.ToolLevel;
import com.feed_the_beast.ftblib.lib.item.ToolType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Made by LatvianModder
 */
public class InvUtils
{
	public static final ItemStack ERROR_ITEM = new ItemStack(Blocks.BARRIER);

	static
	{
		ERROR_ITEM.setStackDisplayName("Broken Item!");
	}

	@Nullable
	public static NBTTagCompound nullIfEmpty(@Nullable NBTTagCompound nbt)
	{
		return nbt == null || nbt.hasNoTags() ? null : nbt;
	}

	public static void addPlayerSlots(Container container, EntityPlayer player, int posX, int posY, boolean ignoreCurrent)
	{
		if (player.inventory == null)
		{
			return;
		}

		for (int y = 0; y < 3; y++)
		{
			for (int x = 0; x < 9; x++)
			{
				ATHelper.addSlot(container, new Slot(player.inventory, x + y * 9 + 9, posX + x * 18, posY + y * 18));
			}
		}

		int i = ignoreCurrent ? player.inventory.currentItem : -1;

		for (int x = 0; x < 9; x++)
		{
			if (x != i)
			{
				ATHelper.addSlot(container, new Slot(player.inventory, x, posX + x * 18, posY + 58));
			}
			else
			{
				ATHelper.addSlot(container, new Slot(player.inventory, x, posX + x * 18, posY + 58)
				{
					@Override
					public boolean canTakeStack(EntityPlayer ep)
					{
						return false;
					}
				});
			}
		}
	}

	public static ItemStack transferStackInSlot(Container container, int index, int nonPlayerSlots)
	{
		if (nonPlayerSlots <= 0)
		{
			return ItemStack.EMPTY;
		}

		ItemStack is = ItemStack.EMPTY;
		Slot slot = container.inventorySlots.get(index);

		if (slot != null && slot.getHasStack())
		{
			ItemStack is1 = slot.getStack();
			is = is1.copy();

			if (index < nonPlayerSlots)
			{
				if (!ATHelper.mergeItemStack(container, is1, nonPlayerSlots, container.inventorySlots.size(), true))
				{
					return ItemStack.EMPTY;
				}
			}
			else if (!ATHelper.mergeItemStack(container, is1, 0, nonPlayerSlots, false))
			{
				return ItemStack.EMPTY;
			}

			if (is1.isEmpty())
			{
				slot.putStack(ItemStack.EMPTY);
			}
			else
			{
				slot.onSlotChanged();
			}
		}

		return is;
	}

	public static ItemStack getAndSplit(IItemHandlerModifiable itemHandler, int index, int amount)
	{
		if (index >= 0 && index < itemHandler.getSlots() && !itemHandler.getStackInSlot(index).isEmpty() && amount > 0)
		{
			ItemStack itemstack = itemHandler.getStackInSlot(index).splitStack(amount);

			if (itemHandler.getStackInSlot(index).isEmpty())
			{
				itemHandler.setStackInSlot(index, ItemStack.EMPTY);
			}

			return itemstack;
		}

		return ItemStack.EMPTY;
	}

	public static ItemStack getAndRemove(IItemHandlerModifiable itemHandler, int index)
	{
		ItemStack itemStack = itemHandler.getStackInSlot(index);
		itemHandler.setStackInSlot(index, ItemStack.EMPTY);
		return itemStack;
	}

	public static void clear(IItemHandlerModifiable itemHandler)
	{
		for (int i = 0; i < itemHandler.getSlots(); i++)
		{
			itemHandler.setStackInSlot(i, ItemStack.EMPTY);
		}
	}

	public static void dropItem(World w, double x, double y, double z, double mx, double my, double mz, ItemStack item, int delay)
	{
		if (!item.isEmpty())
		{
			EntityItem ei = new EntityItem(w, x, y, z, item.copy());
			ei.motionX = mx;
			ei.motionY = my;
			ei.motionZ = mz;
			ei.setPickupDelay(delay);
			w.spawnEntity(ei);
		}
	}

	public static void dropItem(World w, double x, double y, double z, ItemStack item, int delay)
	{
		dropItem(w, x, y, z, w.rand.nextGaussian() * 0.07F, w.rand.nextFloat() * 0.05F, w.rand.nextGaussian() * 0.07F, item, delay);
	}

	public static void dropItem(World w, BlockPos pos, ItemStack item, int delay)
	{
		dropItem(w, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, item, delay);
	}

	public static void dropItem(Entity e, ItemStack item)
	{
		dropItem(e.world, e.posX, e.posY, e.posZ, item, 0);
	}

	public static void giveItem(EntityPlayer ep, ItemStack item)
	{
		if (!item.isEmpty())
		{
			if (ep.inventory.addItemStackToInventory(item))
			{
				ep.inventory.markDirty();

				if (ep.openContainer != null)
				{
					ep.openContainer.detectAndSendChanges();
				}
			}
			else
			{
				dropItem(ep, item);
			}
		}
	}

	public static void dropAllItems(World world, double x, double y, double z, Iterable<ItemStack> items)
	{
		if (!world.isRemote)
		{
			for (ItemStack item : items)
			{
				if (!item.isEmpty())
				{
					dropItem(world, x, y, z, item, 10);
				}
			}
		}
	}

	public static void dropAllItems(World world, double x, double y, double z, @Nullable IItemHandler itemHandler)
	{
		if (!world.isRemote && itemHandler != null && itemHandler.getSlots() > 0)
		{
			for (int i = 0; i < itemHandler.getSlots(); i++)
			{
				ItemStack item = itemHandler.getStackInSlot(i);

				if (!item.isEmpty())
				{
					dropItem(world, x, y, z, item, 10);
				}
			}
		}
	}

	public static boolean isWrench(ItemStack is)
	{
		return !is.isEmpty() && is.getItem().getHarvestLevel(is, ToolType.WRENCH.getName(), null, null) >= ToolLevel.BASIC.ordinal();
	}

	public static void transferItems(IItemHandler from, IItemHandler to, int amount, Predicate<ItemStack> filter)
	{
		if (amount <= 0)
		{
			return;
		}

		for (int i = 0; i < from.getSlots(); i++)
		{
			ItemStack extracted = from.extractItem(i, amount, true);

			if (!extracted.isEmpty() && filter.test(extracted))
			{
				ItemStack inserted = ItemHandlerHelper.insertItem(to, extracted, false);
				int s = extracted.getCount() - inserted.getCount();

				if (s > 0)
				{
					from.extractItem(i, s, false);
					amount -= s;

					if (amount <= 0)
					{
						return;
					}
				}
			}
		}
	}

	public static void writeItemHandler(NBTTagCompound nbt, String key, IItemHandlerModifiable itemHandler)
	{
		NBTTagList list = new NBTTagList();

		int slots = itemHandler.getSlots();
		for (int i = 0; i < slots; i++)
		{
			ItemStack stack = itemHandler.getStackInSlot(i);

			if (!stack.isEmpty())
			{
				NBTTagCompound nbt1 = stack.serializeNBT();
				nbt1.setInteger("Slot", i);
				list.appendTag(nbt1);
			}
		}

		if (!list.hasNoTags())
		{
			nbt.setTag(key, list);
		}
	}

	public static void readItemHandler(NBTTagCompound nbt, String key, IItemHandlerModifiable itemHandler)
	{
		NBTTagList list = nbt.getTagList(key, Constants.NBT.TAG_COMPOUND);

		int slots = itemHandler.getSlots();
		for (int i = 0; i < slots; i++)
		{
			itemHandler.setStackInSlot(i, ItemStack.EMPTY);
		}

		for (int i = 0; i < list.tagCount(); i++)
		{
			NBTTagCompound nbt1 = list.getCompoundTagAt(i);
			ItemStack stack = new ItemStack(nbt1);

			if (!stack.isEmpty())
			{
				itemHandler.setStackInSlot(nbt1.getInteger("Slot"), stack);
			}
		}
	}

	public static int getFirstItemIndex(IItemHandler handler, ItemStack filter)
	{
		boolean filterEmpty = filter.isEmpty();
		int slots = handler.getSlots();

		for (int i = 0; i < slots; i++)
		{
			ItemStack stack = handler.getStackInSlot(i);
			boolean stackEmpty = stack.isEmpty();

			if (filterEmpty == stackEmpty)
			{
				if (filterEmpty || filter.getItem() == stack.getItem() && filter.getMetadata() == stack.getMetadata() && ItemStack.areItemStackTagsEqual(filter, stack))
				{
					return i;
				}
			}
		}

		return -1;
	}

	@Nullable
	public static NBTTagCompound getTag(ItemStack stack)
	{
		return !stack.hasTagCompound() || stack.getTagCompound().hasNoTags() ? null : stack.getTagCompound();
	}

	public static boolean stacksAreEqual(ItemStack stackA, ItemStack stackB)
	{
		return stackA.getItem() == stackB.getItem() && stackA.getMetadata() == stackB.getMetadata() && Objects.equals(getTag(stackA), getTag(stackB));
	}

	public static Set<String> getOreNames(@Nullable Set<String> l, ItemStack is)
	{
		if (is.isEmpty())
		{
			return l == null ? Collections.emptySet() : l;
		}

		int[] ai = OreDictionary.getOreIDs(is);

		if (ai.length > 0)
		{
			if (l == null)
			{
				l = new HashSet<>(ai.length);
			}

			for (int i : ai)
			{
				l.add(OreDictionary.getOreName(i));
			}

			return l;
		}

		return Collections.emptySet();
	}

	public static boolean itemHasOre(ItemStack is, String s)
	{
		int[] ai = OreDictionary.getOreIDs(is);

		if (ai.length > 0)
		{
			for (int i : ai)
			{
				if (s.equals(OreDictionary.getOreName(i)))
				{
					return true;
				}
			}
		}

		return false;
	}
}