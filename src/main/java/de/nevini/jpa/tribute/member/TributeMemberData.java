package de.nevini.jpa.tribute.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(TributeMemberId.class)
@Table(name = "tribute_member")
public class TributeMemberData {

    @Id
    private Long guild;

    @Id
    private Long member;

    private Long start;

    private Byte flag;

}
