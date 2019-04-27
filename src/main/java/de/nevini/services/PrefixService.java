package de.nevini.services;

import de.nevini.db.prefix.PrefixData;
import de.nevini.db.prefix.PrefixRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.core.entities.Guild;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class PrefixService {

    private final PrefixRepository prefixRepository;

    @Getter
    private final String defaultPrefix;

    public PrefixService(
            @Autowired PrefixRepository prefixRepository,
            @Value("${bot.prefix.default:NVN>}") String defaultPrefix
    ) {
        this.prefixRepository = prefixRepository;
        this.defaultPrefix = defaultPrefix;
    }

    public String getGuildPrefix(Guild guild) {
        Optional<PrefixData> data = guild != null ? prefixRepository.findById(guild.getIdLong()) : Optional.empty();
        return data.isPresent() ? data.get().getPrefix() : getDefaultPrefix();
    }

    public synchronized void setGuildPrefix(Guild guild, String prefix) {
        PrefixData data = new PrefixData(guild.getIdLong(), prefix);
        log.info("Save data: {}", data);
        prefixRepository.save(data);
    }

}
