package de.nevini.bot.db.activity;

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
@IdClass(ActivityId.class)
@Table(name = "activity")
public class ActivityData {

    @Id
    private Long user;

    @Id
    private Byte type;

    @Id
    private Long id;

    @Id
    private Long source;

    private Long uts;

}
