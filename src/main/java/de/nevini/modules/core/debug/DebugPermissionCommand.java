package de.nevini.modules.core.debug;

import de.nevini.command.Command;
import de.nevini.command.CommandDescriptor;
import de.nevini.command.CommandEvent;
import de.nevini.resolvers.common.Resolvers;
import de.nevini.scope.Node;
import de.nevini.util.command.CommandOptionDescriptor;
import de.nevini.util.command.CommandReaction;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.PermissionOverride;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.utils.PermissionUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Slf4j
class DebugPermissionCommand extends Command {

    private final long[] masks = {
            Permission.ADMINISTRATOR.getRawValue(),
            Permission.VIEW_AUDIT_LOGS.getRawValue(),
            Permission.MANAGE_SERVER.getRawValue(),
            Permission.MANAGE_ROLES.getRawValue(),
            Permission.MANAGE_CHANNEL.getRawValue(),
            Permission.KICK_MEMBERS.getRawValue(),
            Permission.BAN_MEMBERS.getRawValue(),
            Permission.CREATE_INSTANT_INVITE.getRawValue(),
            Permission.MANAGE_CHANNEL.getRawValue(),
            Permission.MANAGE_PERMISSIONS.getRawValue(),
            Permission.NICKNAME_CHANGE.getRawValue(),
            Permission.NICKNAME_MANAGE.getRawValue(),
            Permission.MANAGE_EMOTES.getRawValue(),
            Permission.MANAGE_WEBHOOKS.getRawValue(),
            Permission.MESSAGE_READ.getRawValue(),
            Permission.MESSAGE_WRITE.getRawValue(),
            Permission.MESSAGE_TTS.getRawValue(),
            Permission.MESSAGE_MANAGE.getRawValue(),
            Permission.MESSAGE_EMBED_LINKS.getRawValue(),
            Permission.MESSAGE_ATTACH_FILES.getRawValue(),
            Permission.MESSAGE_HISTORY.getRawValue(),
            Permission.MESSAGE_MENTION_EVERYONE.getRawValue(),
            Permission.MESSAGE_EXT_EMOJI.getRawValue(),
            Permission.MESSAGE_ADD_REACTION.getRawValue(),
            Permission.VOICE_CONNECT.getRawValue(),
            Permission.VOICE_SPEAK.getRawValue(),
            Permission.VOICE_MUTE_OTHERS.getRawValue(),
            Permission.VOICE_DEAF_OTHERS.getRawValue(),
            Permission.VOICE_MOVE_OTHERS.getRawValue(),
            Permission.VOICE_USE_VAD.getRawValue(),
            Permission.PRIORITY_SPEAKER.getRawValue()
    };

    DebugPermissionCommand() {
        super(CommandDescriptor.builder()
                .keyword("permission")
                .aliases(new String[]{"permissions", "perm", "perms"})
                .ownerOnly(true)
                .node(Node.CORE_HELP) // dummy node
                .description("creates a data dump of all permissions and overrides for a user on the server")
                .options(new CommandOptionDescriptor[]{
                        Resolvers.MEMBER_OR_BOT.describe(false, true)
                })
                .build());
    }

    @Override
    protected void execute(CommandEvent event) {
        Resolvers.MEMBER_OR_BOT.resolveArgumentOrOptionOrInput(event, member -> acceptMember(event, member));
    }

    private void acceptMember(CommandEvent event, Member member) {
        try {
            File export = File.createTempFile("export", ".csv");
            try (PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(export), StandardCharsets.UTF_8)
            ))) {
                out.println("Server,User,Channel,Role,Administrator,View Audit Log,Manage Server,Manage Roles,"
                        + "Manage Channels,Kick Members,Ban Members,Create Instant Invite,Manage Channel,"
                        + "Manage Permissions,Change Nickname,Manage Nicknames,Manage Emojis,Manage Webhooks,"
                        + "Read Text Channels & See Voice Channels,Send Messages,Send TTS Messages,Manage Messages,"
                        + "Embed Links,Attach Files,Read Message History,Mention Everyone,Use External Emojis,"
                        + "Add Reactions,Connect,Speak,Mute Members,Deafen Members,Move Members,Use Voice Activity,"
                        + "Priority Speaker");

                final String server = event.getGuild().getName();
                final String user = member.getEffectiveName();

                // 1. server permissions
                println(out, server, user, "(server)", "(everyone)",
                        event.getGuild().getPublicRole().getPermissionsRaw());

                // 2. role server permissions
                for (Role role : member.getRoles()) {
                    println(out, server, user, "(server)", role.getName(),
                            role.getPermissionsRaw());
                }

                // 3. effective server permissions
                println(out, server, user, "(server)", "(effective)",
                        PermissionUtil.getEffectivePermission(member));

                for (Channel channel : event.getGuild().getChannels(true)) {
                    // 4. channel permissions
                    println(out, server, user, channel.getName(), "(everyone)",
                            channel.getPermissionOverride(event.getGuild().getPublicRole()));

                    // 5. role channel permissions
                    for (Role role : member.getRoles()) {
                        println(out, server, user, channel.getName(), role.getName(),
                                channel.getPermissionOverride(role));
                    }

                    // 6. user channel permissions
                    println(out, server, user, channel.getName(), "(user)",
                            channel.getPermissionOverride(member));

                    // 7. effective channel permissions
                    println(out, server, user, channel.getName(), "(effective)",
                            PermissionUtil.getEffectivePermission(channel, member));
                }
            }

            event.getTextChannel().sendFile(export, new MessageBuilder("Here is the data you requested:").build())
                    .queue(event::complete);
        } catch (IOException e) {
            log.error("Something went wrong while exporting data.", e);
            event.reply(CommandReaction.ERROR, event::complete);
        }
    }

    private void println(PrintWriter out, String server, String user, String channel, String role, long permissions) {
        out.append(server).append(',').append(user).append(',').append(channel).append(',').append(role);
        for (long mask : masks) {
            out.append(',').append((permissions & mask) > 0 ? "ON" : "");
        }
        out.println();
    }

    private void println(PrintWriter out, String server, String user, String channel, String role, PermissionOverride override) {
        out.append(server).append(',').append(user).append(',').append(channel).append(',').append(role);
        long allowed = override.getAllowedRaw();
        long denied = override.getDeniedRaw();
        for (long mask : masks) {
            out.append(',').append((allowed & mask) > 0 ? "ON" : (denied & mask) > 0 ? "NO" : "");
        }
        out.println();
    }

}
