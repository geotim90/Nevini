package de.nevini.api.osu.jpa.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OsuUserId implements Serializable {

    private Integer userId;

    private Integer mode;

}
