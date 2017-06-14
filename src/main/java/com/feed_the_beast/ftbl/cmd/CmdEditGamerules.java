package com.feed_the_beast.ftbl.cmd;

import com.feed_the_beast.ftbl.api.config.IConfigContainer;
import com.feed_the_beast.ftbl.api.config.IConfigTree;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.lib.cmd.CmdEditConfigBase;
import com.feed_the_beast.ftbl.lib.config.ConfigKey;
import com.feed_the_beast.ftbl.lib.config.ConfigTree;
import com.feed_the_beast.ftbl.lib.config.PropertyBool;
import com.feed_the_beast.ftbl.lib.config.PropertyInt;
import com.feed_the_beast.ftbl.lib.config.PropertyString;
import com.feed_the_beast.ftbl.lib.util.StringUtils;
import com.google.gson.JsonObject;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.GameRules;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class CmdEditGamerules extends CmdEditConfigBase
{
	private static final ITextComponent TITLE = StringUtils.text("Game Rules");
	private GameRules rules, oldRules;
	private IConfigTree tree;

	public IConfigContainer container = new IConfigContainer()
	{
		@Override
		public IConfigTree getConfigTree()
		{
			if (oldRules != rules)
			{
				oldRules = rules;
				tree = new ConfigTree();

				for (String rule : rules.getRules())
				{
					IConfigValue value;

					if (rules.areSameType(rule, GameRules.ValueType.BOOLEAN_VALUE))
					{
						value = new PropertyBool(rules.getBoolean(rule))
						{
							@Override
							public void setBoolean(boolean v)
							{
								rules.setOrCreateGameRule(rule, v ? "true" : "false");
							}

							@Override
							public boolean getBoolean()
							{
								return rules.getBoolean(rule);
							}
						};
					}
					else if (rules.areSameType(rule, GameRules.ValueType.NUMERICAL_VALUE))
					{
						value = new PropertyInt(rules.getInt(rule))
						{
							@Override
							public void setInt(int v)
							{
								rules.setOrCreateGameRule(rule, Integer.toString(v));
							}

							@Override
							public int getInt()
							{
								return rules.getInt(rule);
							}
						};
					}
					else
					{
						value = new PropertyString(rules.getString(rule))
						{
							@Override
							public void setString(String v)
							{
								rules.setOrCreateGameRule(rule, v);
							}

							@Override
							public String getString()
							{
								return rules.getString(rule);
							}
						};
					}

					tree.add(new ConfigKey(rule, value), value);
				}
			}

			return tree;
		}

		@Override
		public ITextComponent getTitle()
		{
			return TITLE;
		}

		@Override
		public void saveConfig(ICommandSender sender, @Nullable NBTTagCompound nbt, JsonObject json)
		{
			getConfigTree().fromJson(json);
		}
	};

	public CmdEditGamerules()
	{
		super("edit_gamerules", Level.OP);
	}

	@Override
	public IConfigContainer getConfigContainer(ICommandSender sender) throws CommandException
	{
		rules = sender.getEntityWorld().getGameRules();
		return container;
	}
}