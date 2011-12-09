package de.flower.rmt.model;

import de.flower.common.model.AbstractBaseEntity;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Size;

/**
 * Opponents for matches.
 * // TODO (flowerrrr - 23.09.11) need to clarify if entities can be
 * stored together with Team-instances in same table.
 *
 * @author flowerrrr
 */
@Entity
public class Opponent extends AbstractBaseEntity {

    @NotBlank
    @Size(max = 50)
    @Column
    private String name;

    @URL
    @Size(max = 255)
    @Column
    private String url;

    private Opponent() {
    }
}
