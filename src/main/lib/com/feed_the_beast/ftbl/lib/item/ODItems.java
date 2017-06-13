package com.feed_the_beast.ftbl.lib.item;

import com.feed_the_beast.ftbl.lib.util.RecipeUtils;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class ODItems
{
	//@formatter:off
    public static final Ingredient

    WOOD = get("logWood"),
    SAPLING = get("treeSapling"),
    PLANKS = get("plankWood"),
    SLAB = get("slabWood"),
    STICK = get("stickWood"),
    GLASS = get("blockGlassColorless"),
    GLASS_ANY = get("blockGlass"),
    GLASS_PANE = get("paneGlassColorless"),
    GLASS_PANE_ANY = get("paneGlass"),
    STONE = get("stone"),
    COBBLE = get("cobblestone"),
    SAND = get("sand"),
    OBSIDIAN = get("obsidian"),
    CHEST = get("chest"),
    CHEST_WOOD = get("chestWood"),
    CRAFTING_TABLE = get("workbench"),
    QUARTZ_BLOCK = get("blockQuartz"),
	WOOL_WHITE = get(new ItemStack(Blocks.WOOL, 1, 0)),

    SLIMEBALL = get("slimeball"),
    STRING = get("string"),
    LEATHER = get("leather"),
    GUNPOWDER = get("gunpowder"),
    PAPER = get("paper"),
    MEAT_RAW = get("listAllmeatraw"),
    MEAT_COOKED = get("listAllmeatcooked"),
    RUBBER = get("itemRubber"),
    SILICON = get("itemSilicon"),
    IRON_ROD = get("rodIron"),

    REDSTONE = get("dustRedstone"),
    GLOWSTONE = get("dustGlowstone"),
    QUARTZ = get("gemQuartz"),
    LAPIS = get("gemLapis"),
    ENDERPEARL = get("enderpearl"),
    NETHERSTAR = get("netherStar"),

    IRON = get("ingotIron"),
    GOLD = get("ingotGold"),
    DIAMOND = get("gemDiamond"),
    EMERALD = get("gemEmerald"),

    TIN = get("ingotTin"),
    COPPER = get("ingotCopper"),
    LEAD = get("ingotLead"),
    BRONZE = get("ingotBronze"),
    SILVER = get("ingotSilver"),
    NETHER_INGOT = get("ingotBrickNether"),

    RUBY = get("gemRuby"),
    SAPPHIRE = get("gemSapphire"),
    PERIDOT = get("gemPeridot"),

    NUGGET_GOLD = get("nuggetGold"),
    NUGGET_TIN = get("nuggetTin"),
    NUGGET_COPPER = get("nuggetCopper"),
    NUGGET_LEAD = get("nuggetLead"),
    NUGGET_SILVER = get("nuggetSilver");
    //@formatter:on

	private static Ingredient get(Object o)
	{
		return RecipeUtils.getIngredient(o);
	}

	public static void preInit()
	{
		OreDictionary.registerOre("listAllmeatraw", new ItemStack(Items.BEEF));
		OreDictionary.registerOre("listAllmeatraw", new ItemStack(Items.PORKCHOP));
		OreDictionary.registerOre("listAllmeatraw", new ItemStack(Items.CHICKEN));
		OreDictionary.registerOre("listAllmeatraw", new ItemStack(Items.MUTTON));
		OreDictionary.registerOre("listAllmeatraw", new ItemStack(Items.RABBIT));

		OreDictionary.registerOre("listAllmeatcooked", new ItemStack(Items.COOKED_BEEF));
		OreDictionary.registerOre("listAllmeatcooked", new ItemStack(Items.COOKED_PORKCHOP));
		OreDictionary.registerOre("listAllmeatcooked", new ItemStack(Items.COOKED_CHICKEN));
		OreDictionary.registerOre("listAllmeatcooked", new ItemStack(Items.COOKED_MUTTON));
		OreDictionary.registerOre("listAllmeatcooked", new ItemStack(Items.COOKED_RABBIT));
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