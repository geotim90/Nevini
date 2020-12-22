package de.nevini.modules.osu.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum OsuMod implements OsuEnum {

    NO_FAIL(1, "No Fail", "NF"),
    EASY(2, "Easy", "EZ"),
    TOUCH_DEVICE(4, "Touch Device", "TD"),
    HIDDEN(8, "Hidden", "HD"),
    HARD_ROCK(16, "Hard Rock", "HR"),
    SUDDEN_DEATH(32, "Sudden Death", "SD"),
    DOUBLE_TIME(64, "Double Time", "DT"),
    RELAX(128, "Relax", "RL"),
    HALF_TIME(256, "Half Time", "HT"),
    NIGHTCORE(512, "Nightcore", "NC"),
    FLASHLIGHT(1024, "Flashlight", "FL"),
    AUTOPLAY(2048, "Autoplay", "AT"),
    SPUN_OUT(4096, "Spun Out", "SO"),
    AUTOPILOT(8192, "Autopilot", "AP"),
    PERFECT(16384, "Perfect", "PF"),
    KEY_4(32768, "4K", "4K"),
    KEY_5(65536, "5K", "5K"),
    KEY_6(131072, "6K", "6K"),
    KEY_7(262144, "7K", "7K"),
    KEY_8(524288, "8K", "8K"),
    FADE_IN(1048576, "Fade-In", "FI"),
    RANDOM(2097152, "Random", "RD"),
    CINEMA(4194304, "Cinema", "CM"),
    TARGET(8388608, "Target Practice", "TP"),
    KEY_9(16777216, "9K", "9K"),
    KEY_COOP(33554432, "Co-op", "CP"),
    KEY_1(67108864, "1K", "1K"),
    KEY_3(134217728, "3K", "3K"),
    KEY_2(268435456, "2K", "2K"),
    SCORE_V2(536870912, "Score v2", "V2"),
    LAST_MOD(1073741824, "Last Mod", "NV");

    private final int id;
    private final String name;
    private final String code;

    public static int NONE = 0;

    public static int sum(@NonNull OsuMod... mods) {
        int value = 0;
        for (OsuMod mod : mods) {
            value |= mod.getId();
        }
        return value;
    }

}
