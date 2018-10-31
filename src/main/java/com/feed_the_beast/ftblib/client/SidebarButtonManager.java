package com.feed_the_beast.ftblib.client;

import com.feed_the_beast.ftblib.FTBLibConfig;
import com.feed_the_beast.ftblib.events.SidebarButtonCreatedEvent;
import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.feed_the_beast.ftblib.lib.io.DataReader;
import com.feed_the_beast.ftblib.lib.util.JsonUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @author LatvianModder
 */
public enum SidebarButtonManager implements ISelectiveResourceReloadListener
{
	INSTANCE;

	public final List<SidebarButtonGroup> groups = new ArrayList<>();

	@Override
	public void onResourceManagerReload(IResourceManager manager, Predicate<IResourceType> resourcePredicate)
	{
		if (!resourcePredicate.test(FTBLibResourceType.FTB_CONFIG))
		{
			return;
		}

		groups.clear();

		JsonElement element = DataReader.get(new File(ClientUtils.MC.gameDir, "local/client/sidebar_buttons.json")).safeJson();
		JsonObject sidebarButtonConfig;

		if (element.isJsonObject())
		{
			sidebarButtonConfig = element.getAsJsonObject();
		}
		else
		{
			sidebarButtonConfig = new JsonObject();
		}

		Map<ResourceLocation, SidebarButtonGroup> groupMap = new HashMap<>();

		for (String domain : manager.getResourceDomains())
		{
			try
			{
				for (IResource resource : manager.getAllResources(new ResourceLocation(domain, "sidebar_button_groups.json")))
				{
					JsonElement json = DataReader.get(resource).json();

					for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject().entrySet())
					{
						if (entry.getValue().isJsonObject())
						{
							JsonObject groupJson = entry.getValue().getAsJsonObject();
							int y = 0;

							if (groupJson.has("y"))
							{
								y = groupJson.get("y").getAsInt();
							}

							SidebarButtonGroup group = new SidebarButtonGroup(new ResourceLocation(domain, entry.getKey()), y);
							groupMap.put(group.getId(), group);
						}
					}
				}
			}
			catch (Exception ex)
			{
				if (!(ex instanceof FileNotFoundException))
				{
					ex.printStackTrace();
				}
			}
		}

		for (String domain : manager.getResourceDomains())
		{
			try
			{
				for (IResource resource : manager.getAllResources(new ResourceLocation(domain, "sidebar_buttons.json")))
				{
					JsonElement json = DataReader.get(resource).json();

					if (json.isJsonObject())
					{
						for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject().entrySet())
						{
							if (entry.getValue().isJsonObject())
							{
								JsonObject buttonJson = entry.getValue().getAsJsonObject();

								if (!buttonJson.has("group"))
								{
									continue;
								}

								if (!FTBLibConfig.debugging.dev_sidebar_buttons && buttonJson.has("dev_only") && buttonJson.get("dev_only").getAsBoolean())
								{
									continue;
								}

								SidebarButtonGroup group = groupMap.get(new ResourceLocation(buttonJson.get("group").getAsString()));

								if (group == null)
								{
									continue;
								}

								SidebarButton button = new SidebarButton(new ResourceLocation(domain, entry.getKey()), group, buttonJson);

								group.getButtons().add(button);

								if (sidebarButtonConfig.has(button.id.getNamespace()))
								{
									JsonElement e = sidebarButtonConfig.get(button.id.getNamespace());

									if (e.isJsonObject() && e.getAsJsonObject().has(button.id.getPath()))
									{
										button.setConfig(e.getAsJsonObject().get(button.id.getPath()).getAsBoolean());
									}
								}
								else if (sidebarButtonConfig.has(button.id.toString()))
								{
									button.setConfig(sidebarButtonConfig.get(button.id.toString()).getAsBoolean());
								}
							}
						}
					}
				}
			}
			catch (Exception ex)
			{
				if (!(ex instanceof FileNotFoundException))
				{
					ex.printStackTrace();
				}
			}
		}

		for (SidebarButtonGroup group : groupMap.values())
		{
			if (!group.getButtons().isEmpty())
			{
				group.getButtons().sort(null);
				groups.add(group);
			}
		}

		groups.sort(null);

		for (SidebarButtonGroup group : groups)
		{
			for (SidebarButton button : group.getButtons())
			{
				new SidebarButtonCreatedEvent(button).post();
			}
		}

		saveConfig();
	}

	public void saveConfig()
	{
		JsonObject o = new JsonObject();

		for (SidebarButtonGroup group : groups)
		{
			for (SidebarButton button : group.getButtons())
			{
				JsonObject o1 = o.getAsJsonObject(button.id.getNamespace());

				if (o1 == null)
				{
					o1 = new JsonObject();
					o.add(button.id.getNamespace(), o1);
				}

				o1.addProperty(button.id.getPath(), button.getConfig());
			}
		}

		JsonUtils.toJsonSafe(new File(ClientUtils.MC.gameDir, "local/client/sidebar_buttons.json"), o);
	}
}