package com.feed_the_beast.ftbl.api.item;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class ODItems
{
    public static final String WOOD = "logWood";
    public static final String SAPLING = "treeSapling";
    public static final String PLANKS = "plankWood";
    public static final String STICK = "stickWood";
    public static final String GLASS = "blockGlassColorless";
    public static final String GLASS_ANY = "blockGlass";
    public static final String GLASS_PANE = "paneGlassColorless";
    public static final String GLASS_PANE_ANY = "paneGlass";
    public static final String STONE = "stone";
    public static final String COBBLE = "cobblestone";
    public static final String SAND = "sand";
    public static final String OBSIDIAN = "obsidian";
    public static final ItemStack WOOL = new ItemStack(Blocks.WOOL, 1, OreDictionary.WILDCARD_VALUE);
    public static final ItemStack WOOL_WHITE = new ItemStack(Blocks.WOOL, 1, 0);
    public static final String CHEST = "chest";
    public static final String CHEST_WOOD = "chestWood";
    public static final String QUARTZ_BLOCK = "blockQuartz";

    public static final String SLIMEBALL = "slimeball";
    public static final String STRING = "string";
    public static final String LEATHER = "leather";
    public static final String GUNPOWDER = "gunpowder";
    public static final String PAPER = "paper";
    public static final String MEAT_RAW = "listAllmeatraw";
    public static final String MEAT_COOKED = "listAllmeatcooked";
    public static final String RUBBER = "itemRubber";
    public static final String SILICON = "itemSilicon";
    public static final String IRON_ROD = "rodIron";

    public static final String REDSTONE = "dustRedstone";
    public static final String GLOWSTONE = "dustGlowstone";
    public static final String QUARTZ = "gemQuartz";
    public static final String LAPIS = "gemLapis";
    public static final String ENDERPEARL = "enderpearl";
    public static final String NETHERSTAR = "netherStar";

    public static final String IRON = "ingotIron";
    public static final String GOLD = "ingotGold";
    public static final String DIAMOND = "gemDiamond";
    public static final String EMERALD = "gemEmerald";

    public static final String TIN = "ingotTin";
    public static final String COPPER = "ingotCopper";
    public static final String LEAD = "ingotLead";
    public static final String BRONZE = "ingotBronze";
    public static final String SILVER = "ingotSilver";

    public static final String RUBY = "gemRuby";
    public static final String SAPPHIRE = "gemSapphire";
    public static final String PERIDOT = "gemPeridot";

    public static final String NUGGET_GOLD = "nuggetGold";
    public static final String NUGGET_TIN = "nuggetTin";
    public static final String NUGGET_COPPER = "nuggetCopper";
    public static final String NUGGET_LEAD = "nuggetLead";
    public static final String NUGGET_SILVER = "nuggetSilver";

    public static void preInit()
    {
        OreDictionary.registerOre(MEAT_RAW, new ItemStack(Items.BEEF));
        OreDictionary.registerOre(MEAT_RAW, new ItemStack(Items.PORKCHOP));
        OreDictionary.registerOre(MEAT_RAW, new ItemStack(Items.CHICKEN));
        OreDictionary.registerOre(MEAT_RAW, new ItemStack(Items.MUTTON));
        OreDictionary.registerOre(MEAT_RAW, new ItemStack(Items.RABBIT));

        OreDictionary.registerOre(MEAT_COOKED, new ItemStack(Items.COOKED_BEEF));
        OreDictionary.registerOre(MEAT_COOKED, new ItemStack(Items.COOKED_PORKCHOP));
        OreDictionary.registerOre(MEAT_COOKED, new ItemStack(Items.COOKED_CHICKEN));
        OreDictionary.registerOre(MEAT_COOKED, new ItemStack(Items.COOKED_MUTTON));
        OreDictionary.registerOre(MEAT_COOKED, new ItemStack(Items.COOKED_RABBIT));
    }

    private static boolean addOreName(String item, int damage, String name)
    {
        Item i = LMInvUtils.getItemFromRegName(new ResourceLocation(item));
        if(i != null)
        {
            OreDictionary.registerOre(name, new ItemStack(i, 1, damage));
        }
        return i != null;
    }

    public static Collection<String> getOreNames(ItemStack is)
    {
        int[] ai = OreDictionary.getOreIDs(is);

        if(ai.length > 0)
        {
            Collection<String> l = new HashSet<>(ai.length);

            for(int i : ai)
            {
                l.add(OreDictionary.getOreName(i));
            }

            return l;
        }

        return Collections.emptySet();
    }

    public static List<ItemStack> getOres(String name)
    {
        return OreDictionary.getOres(name);
    }

    public static ItemStack getFirstOre(String name)
    {
        List<ItemStack> l = getOres(name);
        if(!l.isEmpty())
        {
            return l.get(0);
        }
        return null;
    }

    public static boolean hasOre(String s)
    {
        return !getOres(s).isEmpty();
    }

    public static boolean itemHasOre(ItemStack is, String s)
    {
        int[] ai = OreDictionary.getOreIDs(is);

        if(ai.length > 0)
        {
            for(int i : ai)
            {
                if(s.equals(OreDictionary.getOreName(i)))
                {
                    return true;
                }
            }
        }

        return false;
    }
}