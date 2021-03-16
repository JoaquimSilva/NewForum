package com.silva.joaquim.forum.modelo.dto;

import com.silva.joaquim.forum.modelo.Resposta;
import com.silva.joaquim.forum.modelo.Usuario;

import java.time.LocalDateTime;

public class ResportaDto {

    private Long id;
    private String mensagem;
    private String nomeAutor;
    private LocalDateTime dataCriacao;


    public ResportaDto (Resposta resposta) {
        this.id = resposta.getId();
        this.mensagem = resposta.getMensagem();
        this.nomeAutor = resposta.getAutor().getNome();
        this.dataCriacao = resposta.getDataCriacao();
    }

    public Long getId() {
        return id;
    }

    public String getMensagem() {
        return mensagem;
    }

    public String getNomeAutor() {
        return nomeAutor;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
}
