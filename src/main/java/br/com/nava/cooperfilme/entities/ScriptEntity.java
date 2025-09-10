package br.com.nava.cooperfilme.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.nava.cooperfilme.dtos.ScriptStatus;
import br.com.nava.cooperfilme.entities.converters.ScriptStatusConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Builder
@Table(name = "script")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class ScriptEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Setter
    @Column(name = "id_user_owner")
    private UUID idUserOwner;

    @Column(name = "id_user_creator")
    private UUID idUserCreator;

    @Column(name = "name")
    private String name;

    @Column(name = "content")
    private String content;

    @Setter
    @Convert(converter = ScriptStatusConverter.class)
    @Column(name = "status", nullable = false)
    private ScriptStatus status;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

}
