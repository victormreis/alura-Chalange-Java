package com.alura.challange.model;

import com.alura.challange.records.DestinosDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "destinos")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Destino {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String foto;
    private String nome;
    private Double preco;
    private Boolean ativo;

    public Destino(DestinosDTO destinoDTO) {
        this.foto = destinoDTO.foto();
        this.nome = destinoDTO.nome();
        this.preco = destinoDTO.preco();
        this.ativo = true;
    }

    public void updateDestino(DestinosDTO destino) {

        if(destino.foto() != null) {
            this.foto = destino.foto();
        }

        if(destino.preco() != null) {
            this.preco = destino.preco();
        }

        if(destino.nome() != null) {
            this.nome = destino.nome();
        }

    }

    public void deleteDestino() {
        this.ativo = false;

    }
}
