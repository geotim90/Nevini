package de.nevini.jpa.tribute.member;

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
