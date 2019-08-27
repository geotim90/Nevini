package de.nevini.api.osu.mappers;

import de.nevini.api.osu.model.OsuMod;
import de.nevini.api.osu.model.OsuMode;
import de.nevini.api.osu.model.OsuRank;

import java.util.Arrays;
import java.util.Date;

public class OsuMapperUtils {

    public static Date convertDate(Long value) {
        return value == null ? null : new Date(value);
    }

    public static Long convertDate(Date value) {
        return value == null ? null : value.getTime();
    }

    static OsuMode convertMode(Integer value) {
        if (value != null) {
            for (OsuMode mode : OsuMode.values()) {
                if (mode.getId() == value) {
                    return mode;
                }
            }
        }
        return null;
    }

    static OsuMod[] convertMods(Integer value) {
        if (value == null) {
            return null;
        } else {
            return Arrays.stream(OsuMod.values()).filter(mod -> (value & mod.getId()) != 0).toArray(OsuMod[]::new);
        }
    }

    static OsuRank convertRank(String value) {
        if (value != null) {
            for (OsuRank rank : OsuRank.values()) {
                if (rank.getId().equals(value)) {
                    return rank;
                }
            }
        }
        return null;
    }
}
