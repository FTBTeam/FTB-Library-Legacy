package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.lib.LangKey;

public interface GuiLang
{
	LangKey BUTTON_DONE = LangKey.of("gui.done");
	LangKey BUTTON_CANCEL = LangKey.of("gui.cancel");
	LangKey BUTTON_BACK = LangKey.of("gui.back");
	LangKey BUTTON_UP = LangKey.of("gui.up");
	LangKey BUTTON_DOWN = LangKey.of("gui.down");
	LangKey BUTTON_YES = LangKey.of("gui.yes");
	LangKey BUTTON_NO = LangKey.of("gui.no");
	LangKey BUTTON_NONE = LangKey.of("gui.none");
	LangKey BUTTON_ALL = LangKey.of("gui.all");
	LangKey BUTTON_EDIT = LangKey.of("selectServer.edit");
	LangKey BUTTON_OPTIONS = LangKey.of("options.title");
	LangKey BUTTON_PREV_PAGE = LangKey.of("createWorld.customize.custom.prev");
	LangKey BUTTON_NEXT_PAGE = LangKey.of("createWorld.customize.custom.next");
	LangKey BUTTON_DEFAULTS = LangKey.of("createWorld.customize.custom.defaults");
	LangKey BUTTON_DELETE = LangKey.of("selectServer.delete");
	LangKey BUTTON_REFRESH = LangKey.of("selectServer.refresh");

	LangKey LABEL_ENABLED = LangKey.of("addServer.resourcePack.enabled");
	LangKey LABEL_DISABLED = LangKey.of("addServer.resourcePack.disabled");
	LangKey LABEL_ON = LangKey.of("options.on");
	LangKey LABEL_OFF = LangKey.of("options.off");

	LangKey BUTTON_ACCEPT = LangKey.of("gui_lm.button.accept");
	LangKey BUTTON_ADD = LangKey.of("gui_lm.button.add");
	LangKey BUTTON_CLOSE = LangKey.of("gui_lm.button.close");
	LangKey BUTTON_SAVE = LangKey.of("gui_lm.button.save");
	LangKey BUTTON_INFO = LangKey.of("gui_lm.button.info");

	LangKey LABEL_OTHER = LangKey.of("gui_lm.label.other");
	LangKey LABEL_SERVER_FORCED = LangKey.of("gui_lm.label.server_forced");
	LangKey LABEL_ONLINE = LangKey.of("gui_lm.label.online");
	LangKey LABEL_OFFLINE = LangKey.of("gui_lm.label.offline");
	LangKey LABEL_WHITELIST = LangKey.of("gui_lm.label.whitelist");
	LangKey LABEL_BLACKLIST = LangKey.of("gui_lm.label.blacklist");
	LangKey LABEL_COLOR = LangKey.of("gui_lm.label.color");
}