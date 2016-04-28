package ftb.lib.mod;

import ftb.lib.api.LangKey;

/**
 * Created by LatvianModder on 13.04.2016.
 */
public class FTBLibLang
{
	public static LangKey get(String s)
	{ return new LangKey("ftbl.lang." + s); }
	
	public static final LangKey mode_loaded = get("mode.loaded");
	public static final LangKey mode_not_found = get("mode.not_found");
	public static final LangKey mode_already_set = get("mode.already_set");
	public static final LangKey mode_current = get("mode.current");
	public static final LangKey mode_list = get("mode.list");
	
	public static final LangKey reload_server = get("reload_server");
	public static final LangKey reload_client = get("reload_client");
	public static final LangKey reload_client_config = get("reload_client_config");
	
	//@Deprecated
	public static final LangKey raw = get("raw");
	
	public static final LangKey missing_args = get("missing_args");
	public static final LangKey feature_disabled = get("feature_disabled");
	public static final LangKey invalid_subcmd = get("invalid_subcmd");
	
	public static final LangKey client_config = new LangKey("client_config");
	public static final LangKey owner = get("owner");
	public static final LangKey delete_item = get("delete_item");
	public static final LangKey server_forced = get("server_forced");
	public static final LangKey commands = get("commands");
	public static final LangKey my_permissions = get("my_permissions");
	public static final LangKey difficulty = get("difficulty");
}
