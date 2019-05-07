package de.nevini.services;

import de.nevini.db.permission.PermissionData;
import de.nevini.db.permission.PermissionRepository;
import lombok.NonNull;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PermissionService {

    private final static byte TYPE_GUILD = 1;
    private final static byte TYPE_PERMISSION = 2;
    private final static byte TYPE_ROLE = 3;
    private final static byte TYPE_USER = 4;
    private final static byte TYPE_CHANNEL = 5;
    private final static byte TYPE_CHANNEL_PERMISSION = 6;
    private final static byte TYPE_CHANNEL_ROLE = 7;
    private final static byte TYPE_CHANNEL_USER = 8;

    private final PermissionRepository permissionRepository;

    public PermissionService(@Autowired PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public Optional<Boolean> hasPermission(@NonNull TextChannel channel, @NonNull User user, @NonNull String node) {
        Member member = channel.getGuild().getMember(user);
        if (member == null) {
            return Optional.of(false); // not a member of the guild
        } else if (member.isOwner() || member.hasPermission(Permission.ADMINISTRATOR)) {
            return Optional.of(true); // no restrictions for guild owners or server administrators
        }

        final Optional<Boolean> channelUserPermission = getChannelUserPermission(channel, member, node);
        if (channelUserPermission.isPresent()) {
            return channelUserPermission;
        }

        final Optional<Boolean> channelRolePermission = getChannelRolePermission(channel, member, node);
        if (channelRolePermission.isPresent()) {
            return channelRolePermission;
        }

        final Optional<Boolean> channelPermissionPermission = getChannelPermissionPermission(channel, member, node);
        if (channelPermissionPermission.isPresent()) {
            return channelPermissionPermission;
        }

        final Optional<Boolean> channelPermission = getChannelPermission(channel, node);
        if (channelPermission.isPresent()) {
            return channelPermission;
        }

        final Optional<Boolean> userPermission = getUserPermission(member, node);
        if (userPermission.isPresent()) {
            return userPermission;
        }

        final Optional<Boolean> rolePermission = getRolePermission(member, node);
        if (rolePermission.isPresent()) {
            return rolePermission;
        }

        final Optional<Boolean> permissionPermission = getPermissionPermission(member, node);
        if (permissionPermission.isPresent()) {
            return permissionPermission;
        }

        return getGuildPermission(channel.getGuild(), node);
    }

    private Optional<Boolean> getChannelUserPermission(Channel channel, Member user, String node) {
        return resolvePermissions(permissionRepository.findAllByGuildAndChannelAndTypeAndIdAndNodeStartingWith(
                channel.getGuild().getIdLong(),
                channel.getIdLong(),
                TYPE_CHANNEL_USER,
                user.getUser().getIdLong(),
                node
        ));
    }

    private Optional<Boolean> getChannelRolePermission(Channel channel, Member user, String node) {
        return resolvePermissions(permissionRepository.findAllByGuildAndChannelAndTypeAndIdInAndNodeStartingWith(
                channel.getGuild().getIdLong(),
                channel.getIdLong(),
                TYPE_CHANNEL_ROLE,
                user.getRoles().stream().map(ISnowflake::getIdLong).collect(Collectors.toList()),
                node
        ));
    }

    private Optional<Boolean> getChannelPermissionPermission(Channel channel, Member user, String node) {
        return resolvePermissions(permissionRepository.findAllByGuildAndChannelAndTypeAndIdInAndNodeStartingWith(
                channel.getGuild().getIdLong(),
                channel.getIdLong(),
                TYPE_CHANNEL_PERMISSION,
                user.getPermissions(channel).stream().map(Permission::getRawValue).collect(Collectors.toList()),
                node
        ));
    }

    private Optional<Boolean> getChannelPermission(Channel channel, String node) {
        return resolvePermissions(permissionRepository.findAllByGuildAndChannelAndTypeAndIdAndNodeStartingWith(
                channel.getGuild().getIdLong(),
                channel.getIdLong(),
                TYPE_CHANNEL,
                channel.getGuild().getIdLong(),
                node
        ));
    }

    private Optional<Boolean> getUserPermission(Member user, String node) {
        return resolvePermissions(permissionRepository.findAllByGuildAndChannelAndTypeAndIdAndNodeStartingWith(
                user.getGuild().getIdLong(),
                user.getGuild().getIdLong(),
                TYPE_USER,
                user.getUser().getIdLong(),
                node
        ));
    }

    private Optional<Boolean> getRolePermission(Member user, String node) {
        return resolvePermissions(permissionRepository.findAllByGuildAndChannelAndTypeAndIdInAndNodeStartingWith(
                user.getGuild().getIdLong(),
                user.getGuild().getIdLong(),
                TYPE_ROLE,
                user.getRoles().stream().map(ISnowflake::getIdLong).collect(Collectors.toList()),
                node
        ));
    }

    private Optional<Boolean> getPermissionPermission(Member user, String node) {
        return resolvePermissions(permissionRepository.findAllByGuildAndChannelAndTypeAndIdInAndNodeStartingWith(
                user.getGuild().getIdLong(),
                user.getGuild().getIdLong(),
                TYPE_PERMISSION,
                user.getPermissions().stream().map(Permission::getRawValue).collect(Collectors.toList()),
                node
        ));
    }

    private Optional<Boolean> getGuildPermission(Guild server, String node) {
        return resolvePermissions(permissionRepository.findAllByGuildAndChannelAndTypeAndIdAndNodeStartingWith(
                server.getIdLong(),
                server.getIdLong(),
                TYPE_GUILD,
                server.getIdLong(),
                node
        ));
    }

    private Optional<Boolean> resolvePermissions(Collection<PermissionData> data) {
        boolean anyAllow = false;
        boolean anyDeny = false;
        for (PermissionData p : data) {
            if (p.getFlag() != 0) {
                if (p.getFlag() > 0) {
                    anyAllow = true;
                } else {
                    anyDeny = true;
                }
            }
        }
        if (anyAllow) {
            return Optional.of(true);
        } else if (anyDeny) {
            return Optional.of(false);
        } else {
            return Optional.empty();
        }
    }

}
