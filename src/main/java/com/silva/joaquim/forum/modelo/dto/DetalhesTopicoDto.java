package com.silva.joaquim.forum.modelo.dto;

import com.silva.joaquim.forum.modelo.Resposta;
import com.silva.joaquim.forum.modelo.StatusTopico;
import com.silva.joaquim.forum.modelo.Topico;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DetalhesTopicoDto {

    private Long id;
    private String titulo;
    private String mensagem;
    private String nomeAutor;
    private StatusTopico status;
    private LocalDateTime dataCriacao;
    private List<ResportaDto> resportas;

    public DetalhesTopicoDto (Topico topico) {
        this.id = topico.getId();
        this.titulo = topico.getTitulo();
        this.status = topico.getStatus();
        this.mensagem = topico.getMensagem();
        this.nomeAutor = topico.getAutor().getNome();
        this.dataCriacao = topico.getDataCriacao();
        this.resportas = new ArrayList<>();
        this.resportas.addAll(topico.getRespostas().stream().
                map(ResportaDto::new).
                collect(Collectors.toList()));
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public String getNomeAutor() {
        return nomeAutor;
    }

    public StatusTopico getStatus() {
        return status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public List<ResportaDto> getResportas() {
        return resportas;
    }
}
