package ftb.lib.api;

public class GuiLang
{
	private static LangKey get(String s)
	{ return new LangKey("gui_lm." + s); }
	
	public static final LangKey button_settings = get("button.settings");
	public static final LangKey button_back = get("button.back");
	public static final LangKey button_up = get("button.up");
	public static final LangKey button_down = get("button.down");
	public static final LangKey button_prev = get("button.prev");
	public static final LangKey button_next = get("button.next");
	public static final LangKey button_cancel = get("button.cancel");
	public static final LangKey button_accept = get("button.accept");
	public static final LangKey button_add = get("button.add");
	public static final LangKey button_remove = get("button.remove");
	public static final LangKey button_close = get("button.close");
	public static final LangKey button_save = get("button.save");
	public static final LangKey button_refresh = get("button.refresh");
	public static final LangKey button_edit = get("button.edit");
	public static final LangKey button_info = get("button.info");
	
	public static final LangKey label_server_forced = get("label.server_forced");
	public static final LangKey label_true = get("label.true");
	public static final LangKey label_false = get("label.false");
	public static final LangKey label_enabled = get("label.enabled");
	public static final LangKey label_disabled = get("label.disabled");
	public static final LangKey label_on = get("label.on");
	public static final LangKey label_off = get("label.off");
	public static final LangKey label_online = get("label.online");
	public static final LangKey label_offline = get("label.offline");
	public static final LangKey label_whitelist = get("label.whitelist");
	public static final LangKey label_blacklist = get("label.blacklist");
	
	
}