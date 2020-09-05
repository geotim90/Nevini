package de.nevini.modules.guild.tribute.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TributeMemberId implements Serializable {

    private Long guild;

    private Long member;

}
