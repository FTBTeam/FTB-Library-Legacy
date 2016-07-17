package com.feed_the_beast.ftbl.api.item;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.FMLLog;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Made by LatvianModder
 */
public class LMInvUtils
{
    public static final IInventory EMPTY_INVENTORY = new InventoryBasic("[Null]", true, 0);
    private static Method baublesMethod = null;

    public static ItemStack singleCopy(ItemStack is)
    {
        if(is == null || is.stackSize <= 0)
        {
            return null;
        }
        ItemStack is1 = is.copy();
        is1.stackSize = 1;
        return is1;
    }

    public static boolean itemsEquals(ItemStack is1, ItemStack is2, boolean size, boolean nbt)
    {
        return is1 == null && is2 == null || !(is1 == null || is2 == null) && is1.getItem() == is2.getItem() && is1.getItemDamage() == is2.getItemDamage() && (!nbt || ItemStack.areItemStackTagsEqual(is1, is2)) && (!size || (is1.stackSize == is2.stackSize));
    }

    public static int[] getAllSlots(IInventory inv, EnumFacing side)
    {
        if(side != null && inv instanceof ISidedInventory)
        {
            return ((ISidedInventory) inv).getSlotsForFace(side);
        }

        int[] ai = new int[inv.getSizeInventory()];
        for(int i = 0; i < ai.length; i++)
        {
            ai[i] = i;
        }
        return ai;
    }

    public static int getFirstIndexWhereFits(IInventory inv, ItemStack filter, EnumFacing side)
    {
        if(inv == null)
        {
            return -1;
        }

        int slots[] = getAllSlots(inv, side);
        for(int i = 0; i < slots.length; i++)
        {
            ItemStack is1 = inv.getStackInSlot(slots[i]);

            if(is1 != null && is1.stackSize < is1.getMaxStackSize())
            {
                if(filter == null)
                {
                    return i;
                }
                else if(itemsEquals(filter, is1, false, true) && is1.stackSize < is1.getMaxStackSize())
                {
                    return i;
                }
            }
        }

        return getFirstEmptyIndex(inv, side);
    }

    public static int getFirstIndexWithItem(IInventory inv, ItemStack filter, EnumFacing side, boolean size, boolean nbt)
    {
        if(inv == null || filter == null)
        {
            return -1;
        }
        int slots[] = getAllSlots(inv, side);
        for(int i = 0; i < slots.length; i++)
        {
            ItemStack is1 = inv.getStackInSlot(slots[i]);
            if(is1 != null && itemsEquals(filter, is1, size, nbt))
            {
                return i;
            }
        }

        return -1;
    }

    public static int getFirstFilledIndex(IInventory inv, ItemStack filter, EnumFacing side)
    {
        if(inv == null)
        {
            return -1;
        }
        int slots[] = getAllSlots(inv, side);
        for(int i = 0; i < slots.length; i++)
        {
            ItemStack is1 = inv.getStackInSlot(slots[i]);

            if(is1 != null)
            {
                if(filter == null)
                {
                    return i;
                }
                else if(itemsEquals(filter, is1, false, true))
                {
                    return i;
                }
            }
        }

        return -1;
    }

    public static int getFirstEmptyIndex(IInventory inv, EnumFacing side)
    {
        if(inv == null)
        {
            return -1;
        }

        int slots[] = getAllSlots(inv, side);
        for(int i = 0; i < slots.length; i++)
        {
            ItemStack is1 = inv.getStackInSlot(slots[i]);
            if(is1 == null || is1.stackSize <= 0)
            {
                return i;
            }
        }

        return -1;
    }

    public static boolean reduceItemInInv(IInventory inv, int i)
    {
        if(inv == null || i == -1 || i >= inv.getSizeInventory())
        {
            return false;
        }
        ItemStack is = inv.getStackInSlot(i);

        if(is != null)
        {
            is.stackSize--;
            if(is.stackSize <= 0)
            {
                is = null;
            }
            inv.setInventorySlotContents(i, is);
            inv.markDirty();
        }

        return false;
    }

    public static void reduceItemInInv(IInventory inv, int[] slots, ItemStack is, int size)
    {
        for(int i = 0; i < slots.length; i++)
        {
            ItemStack is1 = inv.getStackInSlot(slots[i]);

            if(is1 != null && itemsEquals(is1, is, false, true))
            {
                int s = Math.min(is1.stackSize, size);

                size -= s;
                is1.stackSize -= s;
                if(is1.stackSize <= 0)
                {
                    inv.setInventorySlotContents(i, null);
                }
                if(size <= 0)
                {
                    return;
                }
            }
        }

        inv.markDirty();
    }

    public static boolean addSingleItemToInv(ItemStack is, IInventory inv, int[] slots, EnumFacing side, boolean doAdd)
    {
        if(is == null)
        {
            return false;
        }
        ItemStack single = singleCopy(is);

        for(int i = 0; i < slots.length; i++)
        {
            ItemStack is1 = inv.getStackInSlot(slots[i]);
            if(is1 != null && is1.stackSize > 0 && LMInvUtils.itemsEquals(is, is1, false, true))
            {
                if(is1.stackSize + 1 <= is1.getMaxStackSize())
                {
                    if(side == null || canInsert(inv, single, i, side))
                    {
                        if(doAdd)
                        {
                            is1.stackSize++;
                            inv.setInventorySlotContents(slots[i], is1);
                            inv.markDirty();
                        }

                        return true;
                    }
                }
            }
        }

        for(int i = 0; i < slots.length; i++)
        {
            ItemStack is1 = inv.getStackInSlot(slots[i]);
            if(is1 == null || is1.stackSize == 0)
            {
                if(side == null || canInsert(inv, single, i, side))
                {
                    if(doAdd)
                    {
                        inv.setInventorySlotContents(slots[i], single);
                        inv.markDirty();
                    }

                    return true;
                }
            }
        }

        return false;
    }

    public static boolean addSingleItemToInv(ItemStack is, IInventory inv, EnumFacing side, boolean doAdd)
    {
        return addSingleItemToInv(is, inv, getAllSlots(inv, side), side, doAdd);
    }

    public static NBTTagCompound removeTags(NBTTagCompound tag, String... tags)
    {
        if(tag == null || tag.hasNoTags())
        {
            return null;
        }
        for(String tag1 : tags)
        {
            tag.removeTag(tag1);
        }
        if(tag.hasNoTags())
        {
            tag = null;
        }
        return tag;
    }

    public static ItemStack removeTags(ItemStack is, String... tags)
    {
        if(is == null)
        {
            return null;
        }
        is.setTagCompound(removeTags(is.getTagCompound(), tags));
        return is;
    }

    public static void writeItemsToNBT(ItemStack[] stacks, NBTTagCompound tag, String s)
    {
        NBTTagList list = new NBTTagList();

        for(int i = 0; i < stacks.length; i++)
        {
            if(stacks[i] != null)
            {
                NBTTagCompound tag1 = new NBTTagCompound();
                tag1.setShort("Slot", (short) i);
                stacks[i].writeToNBT(tag1);
                list.appendTag(tag1);
            }
        }

        if(list.tagCount() > 0)
        {
            tag.setTag(s, list);
        }
    }

    public static void writeItemsToNBT(IInventory inventory, NBTTagCompound tag, String s)
    {
        ItemStack[] stacks = new ItemStack[inventory.getSizeInventory()];
        for(int i = 0; i < stacks.length; i++)
        {
            stacks[i] = inventory.getStackInSlot(i);
        }
        writeItemsToNBT(stacks, tag, s);
    }

    public static void readItemsFromNBT(ItemStack[] stacks, NBTTagCompound tag, String s)
    {
        Arrays.fill(stacks, null);

        if(tag.hasKey(s))
        {
            NBTTagList list = tag.getTagList(s, Constants.NBT.TAG_COMPOUND);

            for(int i = 0; i < list.tagCount(); i++)
            {
                NBTTagCompound tag1 = list.getCompoundTagAt(i);
                int slot = tag1.getShort("Slot");
                stacks[slot] = ItemStack.loadItemStackFromNBT(tag1);

                if(i >= stacks.length)
                {
                    break;
                }
            }
        }
    }

    public static void readItemsFromNBT(IInventory inventory, NBTTagCompound tag, String s)
    {
        ItemStack[] stacks = new ItemStack[inventory.getSizeInventory()];
        readItemsFromNBT(stacks, tag, s);
        for(int i = 0; i < stacks.length; i++)
        {
            inventory.setInventorySlotContents(i, stacks[i]);
        }
    }

    public static ItemStack decrStackSize(IInventory inv, int slot, int amt)
    {
        if(inv == null)
        {
            return null;
        }
        ItemStack stack = inv.getStackInSlot(slot);
        if(stack != null)
        {
            if(stack.stackSize <= amt)
            {
                inv.setInventorySlotContents(slot, null);
            }
            else
            {
                stack = stack.splitStack(amt);
                if(stack.stackSize == 0)
                {
                    inv.setInventorySlotContents(slot, null);
                }
            }
        }

        return stack;
    }

    public static ItemStack removeStackFromSlot(IInventory inv, int i)
    {
        if(inv == null)
        {
            return null;
        }
        ItemStack is = inv.getStackInSlot(i);

        if(is != null)
        {
            inv.setInventorySlotContents(i, null);
            return (is.stackSize > 0) ? is : null;
        }

        return null;
    }

    public static boolean clear(IInventory inv)
    {
        if(inv == null)
        {
            return false;
        }
        boolean hadItems = false;

        for(int i = 0; i < inv.getSizeInventory(); i++)
        {
            ItemStack is = removeStackFromSlot(inv, i);
            if(!hadItems && is != null && is.stackSize > 0)
            {
                hadItems = true;
            }
        }

        if(hadItems)
        {
            inv.markDirty();
        }
        return hadItems;
    }

    public static void dropItem(World w, double x, double y, double z, double mx, double my, double mz, ItemStack is, int delay)
    {
        if(w == null || is == null || is.stackSize == 0)
        {
            return;
        }
        EntityItem ei = new EntityItem(w, x, y, z, is.copy());
        ei.motionX = mx;
        ei.motionY = my;
        ei.motionZ = mz;
        ei.setPickupDelay(delay);
        w.spawnEntityInWorld(ei);
    }

    public static void dropItem(World w, double x, double y, double z, ItemStack is, int delay)
    {
        dropItem(w, x, y, z, w.rand.nextGaussian() * 0.07F, w.rand.nextFloat() * 0.05F, w.rand.nextGaussian() * 0.07F, is, delay);
    }

    public static void dropItem(Entity e, ItemStack item)
    {
        if(e == null || item == null || item.stackSize <= 0)
        {
            return;
        }
        dropItem(e.worldObj, e.posX, e.posY, e.posZ, item, 0);
    }

    public static void giveItem(EntityPlayer ep, ItemStack item)
    {
        giveItem(ep, item, -1);
    }

    public static void giveItem(EntityPlayer ep, ItemStack item, int optionalSlot)
    {
        if(ep == null || ep.inventory == null || item == null || item.stackSize <= 0)
        {
            return;
        }
        ItemStack is = item.copy();
        boolean changed = false;

        int size = is.stackSize;
        for(int i = 0; i < size; i++)
        {
            if(LMInvUtils.addSingleItemToInv(is, ep.inventory, LMInvUtils.getPlayerSlots(ep), null, true))
            {
                is.stackSize--;
                changed = true;
            }
        }

        if(changed)
        {
            ep.inventory.markDirty();

            if(ep.openContainer != null)
            {
                ep.openContainer.detectAndSendChanges();
            }
        }

        if(is.stackSize > 0)
        {
            dropItem(ep, is);
        }
    }

    public static void dropAllItems(World w, double x, double y, double z, ItemStack[] items)
    {
        if(w.isRemote || items == null || items.length == 0)
        {
            return;
        }

        for(ItemStack item : items)
        {
            if(item != null && item.stackSize > 0)
            {
                dropItem(w, x, y, z, item, 10);
            }
        }
    }

    public static boolean canStack(ItemStack is1, ItemStack is2)
    {
        return !(is1 == null || is2 == null) && (is1.stackSize + is2.stackSize <= is1.getMaxStackSize() && is1.stackSize + is2.stackSize <= is2.getMaxStackSize());
    }

    public static ItemStack[] getAllItems(IInventory inv, EnumFacing side)
    {
        if(inv == null)
        {
            return null;
        }
        int[] slots = LMInvUtils.getAllSlots(inv, side);
        ItemStack[] ai = new ItemStack[slots.length];
        if(ai.length == 0)
        {
            return ai;
        }
        for(int i = 0; i < ai.length; i++)
        {
            ai[i] = inv.getStackInSlot(slots[i]);
        }
        return ai;
    }

    public static boolean canExtract(IInventory inv, ItemStack is, int slot, EnumFacing side)
    {
        return !(inv instanceof ISidedInventory) || ((ISidedInventory) inv).canExtractItem(slot, is, side);
    }

    public static boolean canInsert(IInventory inv, ItemStack is, int slot, EnumFacing side)
    {
        return !(inv instanceof ISidedInventory) || ((ISidedInventory) inv).canInsertItem(slot, is, side);
    }

    public static Map<Integer, ItemStack> getAllItemsMap(IInventory inv, EnumFacing side)
    {
        ItemStack[] is = getAllItems(inv, side);
        if(is == null)
        {
            return null;
        }
        HashMap<Integer, ItemStack> map = new HashMap<>();
        for(int i = 0; i < is.length; i++)
        {
            if(is[i] != null)
            {
                map.put(i, is[i]);
            }
        }
        return map;
    }

    public static int[] getPlayerSlots(EntityPlayer ep)
    {
        int[] ai = new int[ep.inventory.mainInventory.length];
        for(int i = 0; i < ai.length; i++)
        {
            ai[i] = i;
        }
        return ai;
    }

    public static ItemStack reduceItem(ItemStack is)
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

    public static ItemStack loadStack(NBTTagCompound tag, String s)
    {
        if(tag.hasKey(s))
        {
            return ItemStack.loadItemStackFromNBT(tag.getCompoundTag(s));
        }
        return null;
    }

    public static void saveStack(NBTTagCompound tag, String s, ItemStack is)
    {
        if(is != null)
        {
            NBTTagCompound tag1 = new NBTTagCompound();
            is.writeToNBT(tag1);
            tag.setTag(s, tag1);
        }
    }

    public static Item getItemFromRegName(ResourceLocation s)
    {
        return Item.REGISTRY.getObject(s);
    }

    public static ResourceLocation getRegName(Item item)
    {
        return Item.REGISTRY.getNameForObject(item);
    }

    public static ResourceLocation getRegName(Block block)
    {
        return Block.REGISTRY.getNameForObject(block);
    }

    public static ResourceLocation getRegName(ItemStack is)
    {
        return (is != null && is.getItem() != null) ? getRegName(is.getItem()) : null;
    }

    public static boolean isWrench(ItemStack is)
    {
        return is != null && is.getItem() != null && is.getItem().getHarvestLevel(is, Tool.Type.WRENCH) >= Tool.Level.BASIC;
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