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

    private String foto1;
    private String foto2;
    private String nome;
    private Double preco;
    private String textoDescritivo;
    private Boolean ativo;

    public Destino(DestinosDTO destinoDTO) {
        this.id = destinoDTO.id();
        this.foto1 = destinoDTO.foto1();
        this.foto2 = destinoDTO.foto2();
        this.nome = destinoDTO.nome();
        this.preco = destinoDTO.preco();
        this.textoDescritivo = destinoDTO.textoDescritivo();
        this.ativo = true;
    }

    public void updateDestino(DestinosDTO destino) {

        if(destino.foto1() != null) {
            this.foto1 = destino.foto1();
        }
        if(destino.foto2() != null) {
            this.foto2 = destino.foto2();
        }

        if(destino.preco() != null) {
            this.preco = destino.preco();
        }

        if(destino.nome() != null) {
            this.nome = destino.nome();
        }

        if(destino.textoDescritivo() != null) {
            this.textoDescritivo = destino.textoDescritivo();
        }

    }

    public void deleteDestino() {
        this.ativo = false;

    }
}
