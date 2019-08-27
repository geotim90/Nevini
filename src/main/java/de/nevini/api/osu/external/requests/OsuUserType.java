package de.nevini.api.osu.external.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum OsuUserType {

    STRING("string"),
    ID("id");

    private final String id;

}
