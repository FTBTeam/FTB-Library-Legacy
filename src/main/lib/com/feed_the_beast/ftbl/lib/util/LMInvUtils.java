package com.feed_the_beast.ftbl.lib.util;

import com.feed_the_beast.ftbl.lib.item.ToolLevel;
import com.feed_the_beast.ftbl.lib.item.ToolType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.lang.reflect.Method;

/**
 * Made by LatvianModder
 */
public class LMInvUtils
{
    public static final IInventory EMPTY_INVENTORY = new InventoryBasic("[Null]", true, 0);
    private static Method baublesMethod = null;

    public static final ItemStack ERROR_ITEM = new ItemStack(Blocks.BARRIER);

    static
    {
        ERROR_ITEM.setStackDisplayName("Error!");
    }

    public static void addPlayerSlots(Container container, EntityPlayer player, int posX, int posY, boolean ignoreCurrent)
    {
        if(player.inventory == null)
        {
            return;
        }

        for(int y = 0; y < 3; y++)
        {
            for(int x = 0; x < 9; x++)
            {
                container.addSlotToContainer(new Slot(player.inventory, x + y * 9 + 9, posX + x * 18, posY + y * 18));
            }
        }

        int i = ignoreCurrent ? player.inventory.currentItem : -1;

        for(int x = 0; x < 9; x++)
        {
            if(x != i)
            {
                container.addSlotToContainer(new Slot(player.inventory, x, posX + x * 18, posY + 58));
            }
            else
            {
                container.addSlotToContainer(new Slot(player.inventory, x, posX + x * 18, posY + 58)
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

    @Nullable
    public static ItemStack transferStackInSlot(Container container, int index, int nonPlayerSlots)
    {
        if(nonPlayerSlots <= 0)
        {
            return null;
        }

        ItemStack is = null;
        Slot slot = container.inventorySlots.get(index);

        if(slot != null && slot.getHasStack())
        {
            ItemStack is1 = slot.getStack();
            is = is1.copy();

            if(index < nonPlayerSlots)
            {
                if(!container.mergeItemStack(is1, nonPlayerSlots, container.inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if(!container.mergeItemStack(is1, 0, nonPlayerSlots, false))
            {
                return null;
            }

            if(is1.stackSize == 0)
            {
                slot.putStack(null);
            }
            else
            {
                slot.onSlotChanged();
            }
        }

        return is;
    }

    public static ItemStack singleCopy(@Nullable ItemStack is)
    {
        if(is != null && is.stackSize > 0)
        {
            ItemStack is1 = is.copy();
            is1.stackSize = 1;
            return is1;
        }

        return null;
    }

    public static boolean itemsEquals(@Nullable ItemStack is1, @Nullable ItemStack is2, boolean size, boolean nbt)
    {
        return is1 == null && is2 == null || !(is1 == null || is2 == null) && is1.getItem() == is2.getItem() && is1.getItemDamage() == is2.getItemDamage() && (!nbt || ItemStack.areItemStackTagsEqual(is1, is2)) && (!size || (is1.stackSize == is2.stackSize));
    }

    @Nullable
    public static ItemStack getAndSplit(IItemHandlerModifiable itemHandler, int index, int amount)
    {
        if(index >= 0 && index < itemHandler.getSlots() && itemHandler.getStackInSlot(index) != null && amount > 0)
        {
            ItemStack itemstack = itemHandler.getStackInSlot(index).splitStack(amount);

            if(itemHandler.getStackInSlot(index).stackSize == 0)
            {
                itemHandler.setStackInSlot(index, null);
            }

            return itemstack;
        }

        return null;
    }

    @Nullable
    public static ItemStack getAndRemove(IItemHandlerModifiable itemHandler, int index)
    {
        ItemStack itemStack = itemHandler.getStackInSlot(index);
        itemHandler.setStackInSlot(index, null);
        return itemStack;
    }

    public static void clear(IItemHandlerModifiable itemHandler)
    {
        for(int i = 0; i < itemHandler.getSlots(); i++)
        {
            itemHandler.setStackInSlot(i, null);
        }
    }

    public static void dropItem(World w, double x, double y, double z, double mx, double my, double mz, ItemStack item, int delay)
    {
        if(item.stackSize > 0)
        {
            EntityItem ei = new EntityItem(w, x, y, z, item.copy());
            ei.motionX = mx;
            ei.motionY = my;
            ei.motionZ = mz;
            ei.setPickupDelay(delay);
            w.spawnEntityInWorld(ei);
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
        dropItem(e.worldObj, e.posX, e.posY, e.posZ, item, 0);
    }

    public static void giveItem(EntityPlayer ep, @Nullable ItemStack item)
    {
        if(item != null && item.stackSize > 0)
        {
            if(ep.inventory.addItemStackToInventory(item))
            {
                ep.inventory.markDirty();

                if(ep.openContainer != null)
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

    public static void dropAllItems(World w, double x, double y, double z, Iterable<ItemStack> items)
    {
        if(!w.isRemote)
        {
            for(ItemStack item : items)
            {
                if(item != null && item.stackSize > 0)
                {
                    dropItem(w, x, y, z, item, 10);
                }
            }
        }
    }

    public static boolean canStack(@Nullable ItemStack is1, @Nullable ItemStack is2)
    {
        return !(is1 == null || is2 == null) && (is1.stackSize + is2.stackSize <= is1.getMaxStackSize() && is1.stackSize + is2.stackSize <= is2.getMaxStackSize());
    }

    @Nullable
    public static ItemStack reduceItem(@Nullable ItemStack is)
    {
        if(is == null || is.stackSize <= 0)
        {
            return null;
        }

        if(is.stackSize == 1)
        {
            if(is.getItem().hasContainerItem(is))
            {
                return is.getItem().getContainerItem(is);
            }
            return null;
        }

        is.splitStack(1);
        return is;
    }

    public static boolean isWrench(@Nullable ItemStack is)
    {
        return is != null && is.getItem().getHarvestLevel(is, ToolType.WRENCH.getName(), null, null) >= ToolLevel.BASIC.ordinal();
    }

    public static void removeDisplayName(ItemStack is)
    {
        if(is.hasTagCompound())
        {
            if(is.getTagCompound().hasKey("display"))
            {
                NBTTagCompound tag1 = is.getTagCompound().getCompoundTag("display");

                if(tag1.hasKey("Name"))
                {
                    tag1.removeTag("Name");

                    if(tag1.hasNoTags())
                    {
                        is.getTagCompound().removeTag("display");
                    }
                }

                if(is.getTagCompound().hasNoTags())
                {
                    is.setTagCompound(null);
                }
            }
        }
    }

    public static void transferItems(IItemHandler from, IItemHandler to, int transfer)
    {
        if(transfer <= 0)
        {
            return;
        }

        for(int i = 0; i < from.getSlots(); i++)
        {
            ItemStack extracted = from.extractItem(i, transfer, true);

            if(extracted != null && extracted.stackSize > 0)
            {
                ItemStack inserted = ItemHandlerHelper.insertItem(to, extracted, false);
                int s = inserted == null ? extracted.stackSize : (extracted.stackSize - inserted.stackSize);

                if(s > 0)
                {
                    from.extractItem(i, s, false);
                    transfer -= s;

                    if(transfer <= 0)
                    {
                        return;
                    }
                }
            }
        }
    }

    /**
     * Retrieves the baubles inventory for the supplied player
     *
     * @author Azanor
     */
    public static IInventory getBaubles(EntityPlayer player)
    {
        IInventory ot = null;

        try
        {
            if(baublesMethod == null)
            {
                Class<?> fake = Class.forName("baubles.common.lib.PlayerHandler");
                baublesMethod = fake.getMethod("getPlayerBaubles", EntityPlayer.class);
            }

            ot = (IInventory) baublesMethod.invoke(null, player);
        }
        catch(Exception ex)
        {
            FMLLog.warning("[Baubles API] Could not invoke baubles.common.lib.PlayerHandler method getPlayerBaubles");
        }

        return ot;
    }
}