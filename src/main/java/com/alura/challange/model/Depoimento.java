package com.alura.challange.model;

import com.alura.challange.records.DepoimentosDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "depoimentos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Depoimento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String depoimento;
    private String autor;
    private String foto;

    @ManyToOne
    @JoinColumn(name = "destino_id", nullable = false)
    private Destino destino;

    public Depoimento(DepoimentosDTO depoimento) {
        this.depoimento = depoimento.depoimento();
        this.foto = depoimento.foto();
        this.autor = depoimento.autor();
    }

    public void updateDepoimento(DepoimentosDTO depoimentoUpdated) {
        if(depoimentoUpdated.autor() != null) {
            this.autor = depoimentoUpdated.autor();
        }

        if(depoimentoUpdated.depoimento() != null) {
            this.depoimento = depoimentoUpdated.depoimento();
        }

        if(depoimentoUpdated.foto() != null) {
            this.foto = depoimentoUpdated.foto();
        }

    }
}
