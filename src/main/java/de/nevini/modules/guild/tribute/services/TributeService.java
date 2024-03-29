package de.nevini.modules.guild.tribute.services;

import de.nevini.modules.guild.tribute.data.*;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;

@Slf4j
@Service
public class TributeService {

    private static final byte CONTRIBUTED = 1;
    private static final byte NOT_CONTRIBUTED = 0;

    private final TributeMemberRepository memberRepository;
    private final TributeRoleRepository roleRepository;
    private final TributeTimeoutRepository timeoutRepository;

    public TributeService(
            @Autowired TributeMemberRepository memberRepository,
            @Autowired TributeRoleRepository roleRepository,
            @Autowired TributeTimeoutRepository timeoutRepository
    ) {
        this.memberRepository = memberRepository;
        this.roleRepository = roleRepository;
        this.timeoutRepository = timeoutRepository;
    }

    public @Nullable
    Long getStart(@NonNull Member member) {
        return memberRepository.findById(new TributeMemberId(member.getGuild().getIdLong(), member.getIdLong()))
                .map(TributeMemberData::getStart).orElse(null);
    }

    @Transactional
    public void setStart(@NonNull Member member, long start) {
        synchronized (memberRepository) {
            TributeMemberData data = memberRepository.findById(
                    new TributeMemberId(member.getGuild().getIdLong(), member.getIdLong()))
                    .orElse(new TributeMemberData(member.getGuild().getIdLong(), member.getIdLong(), start, null,
                            NOT_CONTRIBUTED));
            data.setStart(start);
            log.info("Save data: {}", data);
            memberRepository.save(data);
        }
    }

    @Transactional
    public void setStartIfNull(@NonNull Member member, long start) {
        synchronized (memberRepository) {
            TributeMemberData data = memberRepository.findById(
                    new TributeMemberId(member.getGuild().getIdLong(), member.getIdLong()))
                    .orElse(new TributeMemberData(member.getGuild().getIdLong(), member.getIdLong(), start, null,
                            NOT_CONTRIBUTED));
            // do not overwrite existing "start" values
            if (data.getStart() == null) {
                data.setStart(start);
            }
            log.info("Save data: {}", data);
            memberRepository.save(data);
        }
    }

    @Transactional
    public void unsetStart(@NonNull Member member) {
        synchronized (memberRepository) {
            TributeMemberData data = memberRepository.findById(
                    new TributeMemberId(member.getGuild().getIdLong(), member.getIdLong()))
                    .orElse(new TributeMemberData(member.getGuild().getIdLong(), member.getIdLong(), null, null,
                            NOT_CONTRIBUTED));
            data.setStart(null);
            log.info("Save data: {}", data);
            memberRepository.save(data);
        }
    }

    public boolean getTribute(@NonNull Member member) {
        return memberRepository.findById(new TributeMemberId(member.getGuild().getIdLong(), member.getIdLong()))
                .map(data -> data.getFlag() == CONTRIBUTED).orElse(false);
    }

    @Transactional
    public void setTribute(@NonNull Member member) {
        synchronized (memberRepository) {
            TributeMemberData data = memberRepository.findById(
                    new TributeMemberId(member.getGuild().getIdLong(), member.getIdLong()))
                    .orElse(new TributeMemberData(member.getGuild().getIdLong(), member.getIdLong(), null, null,
                            CONTRIBUTED));
            data.setFlag(CONTRIBUTED);
            log.info("Save data: {}", data);
            memberRepository.save(data);
        }
    }

    @Transactional
    public void unsetTribute(@NonNull Member member) {
        synchronized (memberRepository) {
            TributeMemberData data = memberRepository.findById(
                    new TributeMemberId(member.getGuild().getIdLong(), member.getIdLong()))
                    .orElse(new TributeMemberData(member.getGuild().getIdLong(), member.getIdLong(), null, null,
                            NOT_CONTRIBUTED));
            data.setFlag(NOT_CONTRIBUTED);
            log.info("Save data: {}", data);
            memberRepository.save(data);
        }
    }

    public Role getRole(@NonNull Guild guild) {
        return roleRepository.findById(guild.getIdLong()).map(data -> guild.getRoleById(data.getRole())).orElse(null);
    }

    public void setRole(@NonNull Role role) {
        TributeRoleData data = new TributeRoleData(role.getGuild().getIdLong(), role.getIdLong());
        log.info("Save data: {}", data);
        roleRepository.save(data);
    }

    @Transactional
    public void unsetRole(@NonNull Guild guild) {
        synchronized (roleRepository) {
            if (roleRepository.existsById(guild.getIdLong())) {
                log.info("Delete data: {}", new TributeRoleData(guild.getIdLong(), null));
                roleRepository.deleteById(guild.getIdLong());
            }
        }
    }

    public Long getTimeout(@NonNull Guild guild) {
        return timeoutRepository.findById(guild.getIdLong()).map(TributeTimeoutData::getTimeout).orElse(null);
    }

    public void setTimeout(@NonNull Guild guild, long timeout) {
        TributeTimeoutData data = new TributeTimeoutData(guild.getIdLong(), timeout);
        log.info("Save data: {}", data);
        timeoutRepository.save(data);
    }

    @Transactional
    public void unsetTimeout(@NonNull Guild guild) {
        synchronized (timeoutRepository) {
            if (timeoutRepository.existsById(guild.getIdLong())) {
                log.info("Delete data: {}", new TributeTimeoutData(guild.getIdLong(), null));
                timeoutRepository.deleteById(guild.getIdLong());
            }
        }
    }

    public Long getDelay(@NonNull Member member) {
        return memberRepository.findById(new TributeMemberId(member.getGuild().getIdLong(), member.getIdLong()))
                .map(TributeMemberData::getDelay).orElse(null);
    }

    @Transactional
    public void setDelay(@NonNull Member member, long delay) {
        synchronized (memberRepository) {
            TributeMemberData data = memberRepository.findById(
                    new TributeMemberId(member.getGuild().getIdLong(), member.getIdLong()))
                    .orElse(new TributeMemberData(member.getGuild().getIdLong(), member.getIdLong(), null, delay,
                            NOT_CONTRIBUTED));
            data.setDelay(delay);
            log.info("Save data: {}", data);
            memberRepository.save(data);
        }
    }

    @Transactional
    public void unsetDelay(@NonNull Member member) {
        synchronized (memberRepository) {
            TributeMemberData data = memberRepository.findById(
                    new TributeMemberId(member.getGuild().getIdLong(), member.getIdLong()))
                    .orElse(new TributeMemberData(member.getGuild().getIdLong(), member.getIdLong(), null, null,
                            NOT_CONTRIBUTED));
            data.setDelay(null);
            log.info("Save data: {}", data);
            memberRepository.save(data);
        }
    }

}
