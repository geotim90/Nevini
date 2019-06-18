package de.nevini.bot.services.common;

import de.nevini.bot.db.permission.PermissionData;
import de.nevini.bot.db.permission.PermissionId;
import de.nevini.bot.db.permission.PermissionRepository;
import de.nevini.bot.scope.Node;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.utils.PermissionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PermissionService {

    private final static byte SERVER = 1;
    private final static byte PERMISSION = 2;
    private final static byte ROLE = 3;
    private final static byte USER = 4;
    private final static byte CHANNEL = 5;
    private final static byte CHANNEL_PERMISSION = 6;
    private final static byte CHANNEL_ROLE = 7;
    private final static byte CHANNEL_USER = 8;

    private final PermissionRepository permissionRepository;

    public PermissionService(@Autowired PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public boolean hasChannelUserPermission(@NonNull TextChannel channel, @NonNull Member user, @NonNull Node node) {
        return hasPermission(channel.getGuild(), null, null, user, channel, node, CHANNEL_USER);
    }

    public boolean hasChannelRolePermission(@NonNull TextChannel channel, @NonNull Role role, @NonNull Node node) {
        return hasPermission(channel.getGuild(), null, role, null, channel, node, CHANNEL_ROLE);
    }

    public boolean hasChannelRolePermission(@NonNull TextChannel channel, @NonNull Member user, @NonNull Node node) {
        return hasPermission(channel.getGuild(), null, null, user, channel, node, CHANNEL_ROLE);
    }

    public boolean hasChannelPermissionPermission(
            @NonNull TextChannel channel, @NonNull Permission permission, @NonNull Node node
    ) {
        return hasPermission(channel.getGuild(), permission, null, null, channel, node, CHANNEL_PERMISSION);
    }

    public boolean hasChannelPermissionPermission(
            @NonNull TextChannel channel, @NonNull Role role, @NonNull Node node
    ) {
        return hasPermission(channel.getGuild(), null, role, null, channel, node, CHANNEL_PERMISSION);
    }

    public boolean hasChannelPermissionPermission(
            @NonNull TextChannel channel, @NonNull Member user, @NonNull Node node
    ) {
        return hasPermission(channel.getGuild(), null, null, user, channel, node, CHANNEL_PERMISSION);
    }

    public boolean hasChannelPermission(@NonNull TextChannel channel, @NonNull Node node) {
        return hasPermission(channel.getGuild(), null, null, null, channel, node, CHANNEL);
    }

    public boolean hasUserPermission(@NonNull Member user, @NonNull Node node) {
        return hasPermission(user.getGuild(), null, null, user, null, node, USER);
    }

    public boolean hasRolePermission(@NonNull Role role, @NonNull Node node) {
        return hasPermission(role.getGuild(), null, role, null, null, node, ROLE);
    }

    public boolean hasRolePermission(@NonNull Member user, @NonNull Node node) {
        return hasPermission(user.getGuild(), null, null, user, null, node, ROLE);
    }

    public boolean hasPermissionPermission(@NonNull Guild server, @NonNull Permission permission, @NonNull Node node) {
        return hasPermission(server, permission, null, null, null, node, PERMISSION);
    }

    public boolean hasPermissionPermission(@NonNull Role role, @NonNull Node node) {
        return hasPermission(role.getGuild(), null, role, null, null, node, PERMISSION);
    }

    public boolean hasPermissionPermission(@NonNull Member user, @NonNull Node node) {
        return hasPermission(user.getGuild(), null, null, user, null, node, PERMISSION);
    }

    public boolean hasServerPermission(@NonNull Guild server, @NonNull Node node) {
        return hasPermission(server, null, null, null, null, node, SERVER);
    }

    private boolean hasPermission(
            @NonNull Guild server,
            Permission permission,
            Role role,
            Member user,
            TextChannel channel,
            @NonNull Node node,
            byte scope
    ) {
        if (user != null && (user.isOwner() || user.hasPermission(Permission.ADMINISTRATOR))) {
            return true;
        } else if (role != null && role.hasPermission(Permission.ADMINISTRATOR)) {
            return true;
        } else if (permission != null && permission.equals(Permission.ADMINISTRATOR)) {
            return true;
        }
        if (scope >= CHANNEL_USER && channel != null && user != null) {
            Optional<Boolean> channelUserPermission = getChannelUserPermission(channel, user, node);
            if (channelUserPermission.isPresent()) {
                return channelUserPermission.get();
            }
        }
        if (scope >= CHANNEL_ROLE) {
            Optional<Boolean> channelRolePermission;
            if (channel != null && user != null) {
                channelRolePermission = getChannelRolePermission(channel, user, node);
            } else if (channel != null && role != null) {
                channelRolePermission = getChannelRolePermission(channel, role, node);
            } else {
                channelRolePermission = Optional.empty();
            }
            if (channelRolePermission.isPresent()) {
                return channelRolePermission.get();
            }
        }
        if (scope >= CHANNEL_PERMISSION) {
            Optional<Boolean> channelPermissionPermission;
            if (channel != null && user != null) {
                channelPermissionPermission = getChannelPermissionPermission(channel, user, node);
            } else if (channel != null && role != null) {
                channelPermissionPermission = getChannelPermissionPermission(channel, role, node);
            } else if (channel != null && permission != null) {
                channelPermissionPermission = getChannelPermissionPermission(channel, permission, node);
            } else {
                channelPermissionPermission = Optional.empty();
            }
            if (channelPermissionPermission.isPresent()) {
                return channelPermissionPermission.get();
            }
        }
        if (scope >= CHANNEL && channel != null) {
            Optional<Boolean> channelPermission;
            channelPermission = getChannelPermission(channel, node);
            if (channelPermission.isPresent()) {
                return channelPermission.get();
            }
        }
        if (scope >= USER && user != null) {
            Optional<Boolean> userPermission = getUserPermission(user, node);
            if (userPermission.isPresent()) {
                return userPermission.get();
            }
        }
        if (scope >= ROLE) {
            Optional<Boolean> rolePermission;
            if (user != null) {
                rolePermission = getRolePermission(user, node);
            } else if (role != null) {
                rolePermission = getRolePermission(role, node);
            } else {
                rolePermission = Optional.empty();
            }
            if (rolePermission.isPresent()) {
                return rolePermission.get();
            }
        }
        if (scope >= PERMISSION) {
            Optional<Boolean> permissionPermission;
            if (user != null) {
                permissionPermission = getPermissionPermission(user, node);
            } else if (role != null) {
                permissionPermission = getPermissionPermission(role, node);
            } else if (permission != null) {
                permissionPermission = getPermissionPermission(server, permission, node);
            } else {
                permissionPermission = Optional.empty();
            }
            if (permissionPermission.isPresent()) {
                return permissionPermission.get();
            }
        }
        if (scope >= SERVER) {
            Optional<Boolean> serverPermission = getServerPermission(server, node);
            if (serverPermission.isPresent()) {
                return serverPermission.get();
            }
        }
        // DEFAULT
        if (channel != null && user != null) {
            return user.hasPermission(channel, node.getDefaultPermissions());
        } else if (channel != null && role != null) {
            return role.hasPermission(channel, node.getDefaultPermissions());
        } else if (channel != null && permission != null) {
            return Arrays.stream(node.getDefaultPermissions()).anyMatch(required -> !required.equals(permission));
        } else if (channel != null) {
            return server.getPublicRole().hasPermission(channel, node.getDefaultPermissions());
        } else if (user != null) {
            return user.hasPermission(node.getDefaultPermissions());
        } else if (role != null) {
            return role.hasPermission(node.getDefaultPermissions());
        } else if (permission != null) {
            return Arrays.stream(node.getDefaultPermissions()).anyMatch(required -> !required.equals(permission));
        } else {
            return server.getPublicRole().hasPermission(node.getDefaultPermissions());
        }
    }

    private Optional<Boolean> getChannelUserPermission(Channel channel, Member user, Node node) {
        return resolvePermission(permissionRepository.findById(new PermissionId(
                channel.getGuild().getIdLong(),
                channel.getIdLong(),
                CHANNEL_USER,
                user.getUser().getIdLong(),
                node.getNode()
        )).orElse(null));
    }

    private Optional<Boolean> getChannelRolePermission(Channel channel, Role role, Node node) {
        return resolvePermission(permissionRepository.findById(new PermissionId(
                channel.getGuild().getIdLong(),
                channel.getIdLong(),
                CHANNEL_ROLE,
                role.getIdLong(),
                node.getNode()
        )).orElse(null));
    }

    private Optional<Boolean> getChannelRolePermission(Channel channel, Member user, Node node) {
        return resolvePermissions(permissionRepository.findAllByGuildAndChannelAndTypeAndIdInAndNode(
                channel.getGuild().getIdLong(),
                channel.getIdLong(),
                CHANNEL_ROLE,
                user.getRoles().stream().map(ISnowflake::getIdLong).collect(Collectors.toList()),
                node.getNode()
        ));
    }

    private Optional<Boolean> getChannelPermissionPermission(Channel channel, Permission permission, Node node) {
        return resolvePermission(permissionRepository.findById(new PermissionId(
                channel.getGuild().getIdLong(),
                channel.getIdLong(),
                CHANNEL_PERMISSION,
                permission.getRawValue(),
                node.getNode()
        )).orElse(null));
    }

    private Optional<Boolean> getChannelPermissionPermission(Channel channel, Role role, Node node) {
        return resolvePermissions(permissionRepository.findAllByGuildAndChannelAndTypeAndIdInAndNode(
                channel.getGuild().getIdLong(),
                channel.getIdLong(),
                CHANNEL_PERMISSION,
                Permission.getPermissions(PermissionUtil.getEffectivePermission(channel, role)).stream()
                        .map(Permission::getRawValue).collect(Collectors.toList()),
                node.getNode()
        ));
    }

    private Optional<Boolean> getChannelPermissionPermission(Channel channel, Member user, Node node) {
        return resolvePermissions(permissionRepository.findAllByGuildAndChannelAndTypeAndIdInAndNode(
                channel.getGuild().getIdLong(),
                channel.getIdLong(),
                CHANNEL_PERMISSION,
                user.getPermissions(channel).stream().map(Permission::getRawValue).collect(Collectors.toList()),
                node.getNode()
        ));
    }

    private Optional<Boolean> getChannelPermission(Channel channel, Node node) {
        return resolvePermission(permissionRepository.findById(new PermissionId(
                channel.getGuild().getIdLong(),
                channel.getIdLong(),
                CHANNEL,
                -1L,
                node.getNode()
        )).orElse(null));
    }

    private Optional<Boolean> getUserPermission(Member user, Node node) {
        return resolvePermission(permissionRepository.findById(new PermissionId(
                user.getGuild().getIdLong(),
                -1L,
                USER,
                user.getUser().getIdLong(),
                node.getNode()
        )).orElse(null));
    }

    private Optional<Boolean> getRolePermission(Role role, Node node) {
        return resolvePermission(permissionRepository.findById(new PermissionId(
                role.getGuild().getIdLong(),
                -1L,
                ROLE,
                role.getIdLong(),
                node.getNode()
        )).orElse(null));
    }

    private Optional<Boolean> getRolePermission(Member user, Node node) {
        return resolvePermissions(permissionRepository.findAllByGuildAndChannelAndTypeAndIdInAndNode(
                user.getGuild().getIdLong(),
                -1L,
                ROLE,
                user.getRoles().stream().map(ISnowflake::getIdLong).collect(Collectors.toList()),
                node.getNode()
        ));
    }

    private Optional<Boolean> getPermissionPermission(Guild guild, Permission permission, Node node) {
        return resolvePermission(permissionRepository.findById(new PermissionId(
                guild.getIdLong(),
                -1L,
                PERMISSION,
                permission.getRawValue(),
                node.getNode()
        )).orElse(null));
    }

    private Optional<Boolean> getPermissionPermission(Role role, Node node) {
        return resolvePermissions(permissionRepository.findAllByGuildAndChannelAndTypeAndIdInAndNode(
                role.getGuild().getIdLong(),
                -1L,
                PERMISSION,
                role.getPermissions().stream().map(Permission::getRawValue).collect(Collectors.toList()),
                node.getNode()
        ));
    }

    private Optional<Boolean> getPermissionPermission(Member user, Node node) {
        return resolvePermissions(permissionRepository.findAllByGuildAndChannelAndTypeAndIdInAndNode(
                user.getGuild().getIdLong(),
                -1L,
                PERMISSION,
                user.getPermissions().stream().map(Permission::getRawValue).collect(Collectors.toList()),
                node.getNode()
        ));
    }

    private Optional<Boolean> getServerPermission(Guild server, Node node) {
        return resolvePermission(permissionRepository.findById(new PermissionId(
                server.getIdLong(),
                -1L,
                SERVER,
                -1L,
                node.getNode()
        )).orElse(null));
    }

    private Optional<Boolean> resolvePermission(PermissionData data) {
        if (data != null && data.getFlag() != 0) {
            if (data.getFlag() > 0) {
                return Optional.of(true);
            } else {
                return Optional.of(false);
            }
        } else {
            return Optional.empty();
        }
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

    public void setChannelUserPermission(
            @NonNull TextChannel channel, @NonNull Member user, @NonNull Node node, Boolean override
    ) {
        setPermission(new PermissionData(
                channel.getGuild().getIdLong(),
                channel.getIdLong(),
                CHANNEL_USER,
                user.getUser().getIdLong(),
                node.getNode(),
                null), override);
    }

    public void setChannelRolePermission(
            @NonNull TextChannel channel, @NonNull Role role, @NonNull Node node, Boolean override
    ) {
        setPermission(new PermissionData(
                channel.getGuild().getIdLong(),
                channel.getIdLong(),
                CHANNEL_ROLE,
                role.getIdLong(),
                node.getNode(),
                null), override);
    }

    public void setChannelPermissionPermission(
            @NonNull TextChannel channel, @NonNull Permission permission, @NonNull Node node, Boolean override
    ) {
        setPermission(new PermissionData(
                channel.getGuild().getIdLong(),
                channel.getIdLong(),
                CHANNEL_PERMISSION,
                permission.getRawValue(),
                node.getNode(),
                null), override);
    }

    public void setChannelPermission(@NonNull TextChannel channel, @NonNull Node node, Boolean override) {
        setPermission(new PermissionData(
                channel.getGuild().getIdLong(),
                channel.getIdLong(),
                CHANNEL,
                -1L,
                node.getNode(),
                null), override);
    }

    public void setUserPermission(@NonNull Member user, @NonNull Node node, Boolean override) {
        setPermission(new PermissionData(
                user.getGuild().getIdLong(),
                -1L,
                USER,
                user.getUser().getIdLong(),
                node.getNode(),
                null), override);
    }

    public void setRolePermission(@NonNull Role role, @NonNull Node node, Boolean override) {
        setPermission(new PermissionData(
                role.getGuild().getIdLong(),
                -1L,
                ROLE,
                role.getIdLong(),
                node.getNode(),
                null), override);
    }

    public void setPermissionPermission(
            @NonNull Guild server, @NonNull Permission permission, @NonNull Node node, Boolean override
    ) {
        setPermission(new PermissionData(
                server.getIdLong(),
                -1L,
                PERMISSION,
                permission.getRawValue(),
                node.getNode(),
                null), override);
    }

    public void setServerPermission(@NonNull Guild server, @NonNull Node node, Boolean override) {
        setPermission(new PermissionData(
                server.getIdLong(),
                -1L,
                SERVER,
                -1L,
                node.getNode(),
                null), override);
    }

    private void setPermission(PermissionData permission, Boolean override) {
        permission.setFlag(override == null ? (byte) 0 : (override ? (byte) 1 : (byte) -1));
        log.info("Save data: {}", permission);
        permissionRepository.save(permission);
    }

}
