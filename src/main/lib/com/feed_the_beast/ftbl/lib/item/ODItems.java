package com.feed_the_beast.ftbl.lib.item;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class ODItems
{
	public static final ItemStack WOOL_WHITE = new ItemStack(Blocks.WOOL, 1, 0);

	//@formatter:off
    public static final String

    WOOD = "logWood",
    SAPLING = "treeSapling",
    PLANKS = "plankWood",
    SLAB = "slabWood",
    STICK = "stickWood",
    GLASS = "blockGlassColorless",
    GLASS_ANY = "blockGlass",
    GLASS_PANE = "paneGlassColorless",
    GLASS_PANE_ANY = "paneGlass",
    STONE = "stone",
    COBBLE = "cobblestone",
    SAND = "sand",
    OBSIDIAN = "obsidian",
    CHEST = "chest",
    CHEST_WOOD = "chestWood",
    CRAFTING_TABLE = "workbench",
    QUARTZ_BLOCK = "blockQuartz",

    SLIMEBALL = "slimeball",
    STRING = "string",
    LEATHER = "leather",
    GUNPOWDER = "gunpowder",
    PAPER = "paper",
    MEAT_RAW = "listAllmeatraw",
    MEAT_COOKED = "listAllmeatcooked",
    RUBBER = "itemRubber",
    SILICON = "itemSilicon",
    IRON_ROD = "rodIron",

    REDSTONE = "dustRedstone",
    GLOWSTONE = "dustGlowstone",
    QUARTZ = "gemQuartz",
    LAPIS = "gemLapis",
    ENDERPEARL = "enderpearl",
    NETHERSTAR = "netherStar",

    IRON = "ingotIron",
    GOLD = "ingotGold",
    DIAMOND = "gemDiamond",
    EMERALD = "gemEmerald",

    TIN = "ingotTin",
    COPPER = "ingotCopper",
    LEAD = "ingotLead",
    BRONZE = "ingotBronze",
    SILVER = "ingotSilver",
    NETHER_INGOT = "ingotBrickNether",

    RUBY = "gemRuby",
    SAPPHIRE = "gemSapphire",
    PERIDOT = "gemPeridot",

    NUGGET_GOLD = "nuggetGold",
    NUGGET_TIN = "nuggetTin",
    NUGGET_COPPER = "nuggetCopper",
    NUGGET_LEAD = "nuggetLead",
    NUGGET_SILVER = "nuggetSilver";
    //@formatter:on

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

	public static Collection<String> getOreNames(ItemStack is)
	{
		int[] ai = OreDictionary.getOreIDs(is);

		if (ai.length > 0)
		{
			Collection<String> l = new HashSet<>(ai.length);

			for (int i : ai)
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
		if (!l.isEmpty())
		{
			return l.get(0);
		}
		return ItemStack.EMPTY;
	}

	public static boolean hasOre(String s)
	{
		return !getOres(s).isEmpty();
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