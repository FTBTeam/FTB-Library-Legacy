package com.feed_the_beast.ftbl.lib.internal;

import com.feed_the_beast.ftbl.lib.LangKey;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

/**
 * @author LatvianModder
 */
public interface FTBLibLang
{
	LangKey WIP = LangKey.of("wip");
	LangKey DEPRECATED = LangKey.of("deprecated", String.class);
	LangKey EXAMPLE = LangKey.of("example");
	LangKey CLICK_HERE = LangKey.of("click_here").setDefaultStyle(new Style().setColor(TextFormatting.GOLD));
	LangKey FEATURE_DISABLED = LangKey.of("feature_disabled");
	LangKey MISSING_ARGS = LangKey.of("missing_args", String.class);
	LangKey MISSING_ARGS_NUM = LangKey.of("missing_args_num", String.class);
	LangKey ERROR = LangKey.of("error");
	LangKey OWNER = LangKey.of("owner");
	LangKey DELETE_ITEM = LangKey.of("delete_item");
	LangKey SERVER_FORCED = LangKey.of("server_forced");
	LangKey COMMANDS = LangKey.of("commands");
	LangKey MY_PERMISSIONS = LangKey.of("my_permissions");
	LangKey ALL_PERMISSIONS = LangKey.of("all_permissions");
	LangKey DIFFICULTY = LangKey.of("difficulty");
	LangKey MY_TEAM = LangKey.of("sidebar_button.ftbl.my_team");

	LangKey RELOAD_SERVER = LangKey.of("ftbl.lang.reload_server", String.class);
	LangKey RELOAD_CLIENT = LangKey.of("ftbl.lang.reload_client", String.class);
	LangKey RELOAD_CLIENT_CONFIG = LangKey.of("ftbl.lang.reload_client_config");
	LangKey RELOAD_FAILED = LangKey.of("ftbl.lang.reload_failed");

	LangKey MY_SERVER_SETTINGS = LangKey.of("player_config");
	LangKey MY_TEAM_SETTINGS = LangKey.of("team_config");
	LangKey COMMAND_PERMISSION = LangKey.of("commands.generic.permission");
	LangKey COMMAND_USAGE = LangKey.of("commands.generic.usage");

	LangKey CONFIG_COMMAND_INVALID_KEY = LangKey.of("ftbl.lang.config_command.invalid_key", String.class);
	LangKey CONFIG_COMMAND_SETTING = LangKey.of("ftbl.lang.config_command.setting", String.class, String.class);
	LangKey CONFIG_COMMAND_SET = LangKey.of("ftbl.lang.config_command.set", String.class, String.class);

	LangKey CONFIG_ADD_FAKE_PLAYER_INVALID_UUID = LangKey.of("ftbl.lang.add_fake_player.invalid_uuid");
	LangKey CONFIG_ADD_FAKE_PLAYER_PLAYER_EXISTS = LangKey.of("ftbl.lang.add_fake_player.player_exists");
	LangKey CONFIG_ADD_FAKE_PLAYER_ADDED = LangKey.of("ftbl.lang.add_fake_player.added", String.class);

	LangKey TEAM_CREATED = LangKey.of("ftbl.lang.team.created", String.class);
	LangKey TEAM_ID_ALREADY_EXISTS = LangKey.of("ftbl.lang.team.id_already_exists");
	LangKey TEAM_ID_INVALID = LangKey.of("ftbl.lang.team.id_invalid");
	LangKey TEAM_DELETED = LangKey.of("ftbl.lang.team.deleted", String.class);
	LangKey TEAM_MEMBER_LEFT = LangKey.of("ftbl.lang.team.member_left", String.class);
	LangKey TEAM_MEMBER_JOINED = LangKey.of("ftbl.lang.team.member_joined", String.class);
	LangKey TEAM_TRANSFERRED_OWNERSHIP = LangKey.of("ftbl.lang.team.transferred_ownership", String.class);
	LangKey TEAM_INVITED = LangKey.of("ftbl.lang.team.invited", String.class, String.class);
	LangKey TEAM_INVITED_YOU = LangKey.of("ftbl.lang.team.invited_you", String.class, String.class);
	LangKey TEAM_NOT_FOUND = LangKey.of("ftbl.lang.team.error.not_found");
	LangKey TEAM_NO_TEAM = LangKey.of("ftbl.lang.team.error.no_team");
	LangKey TEAM_MUST_LEAVE = LangKey.of("ftbl.lang.team.error.must_leave");
	LangKey TEAM_NOT_OWNER = LangKey.of("ftbl.lang.team.error.not_owner");
	LangKey TEAM_NOT_MEMBER = LangKey.of("ftbl.lang.team.error.not_member", String.class);
	LangKey TEAM_MUST_TRANSFER_OWNERSHIP = LangKey.of("ftbl.lang.team.error.must_transfer_ownership");
	LangKey TEAM_ALREADY_INVITED = LangKey.of("ftbl.lang.team.error.already_invited", String.class);
	LangKey TEAM_FAILED_TO_JOIN = LangKey.of("ftbl.lang.team.error.failed_to_join");
	LangKey TEAM_PERMISSION_SET = LangKey.of("ftbl.lang.team.permission.set", String.class, String.class, String.class);
	LangKey TEAM_PERMISSION_OWNER = LangKey.of("ftbl.lang.team.permission.owner");
	LangKey TEAM_NOTIFICATION = LangKey.of("ftbl.lang.team.notification", String.class, String.class);
	LangKey TEAM_NOTIFICATION_HIDE = LangKey.of("ftbl.lang.team.notification.hide");
	LangKey TEAM_CHAT_MESSAGE = LangKey.of("ftbl.lang.team.chat_message", String.class, String.class);
}