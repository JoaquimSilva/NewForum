package com.silva.joaquim.forum.controller;

import com.silva.joaquim.forum.controller.form.AtualizacaoTopicoForm;
import com.silva.joaquim.forum.controller.form.TopicoForm;
import com.silva.joaquim.forum.modelo.Topico;
import com.silva.joaquim.forum.modelo.dto.DetalhesTopicoDto;
import com.silva.joaquim.forum.modelo.dto.TopicoDto;
import com.silva.joaquim.forum.repository.CursoRepository;
import com.silva.joaquim.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/topicos")
public class TopicosController {

	@Autowired
	private TopicoRepository topicoRepository;
	@Autowired
	private CursoRepository cursoRepository;


	@GetMapping
	public List<TopicoDto> lista (String nomeCurso){
		if (nomeCurso == null) {
			List<Topico> topicos = topicoRepository.findAll();
			return TopicoDto.converter(topicos);
		}
		else {
			List<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso);
			return TopicoDto.converter(topicos);
		}

	}
	@PostMapping
	@Transactional
	public ResponseEntity<TopicoDto> cadastrar (@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder){
		Topico topico = form.convert(cursoRepository);
		topicoRepository.save(topico);

		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		return ResponseEntity.created(uri).body(new TopicoDto(topico));
	}

	@GetMapping("/{id}")
	public ResponseEntity<DetalhesTopicoDto> detalhado (@PathVariable long id){
		Optional<Topico> topicoOptional = topicoRepository.findById(id);
		return topicoOptional.map(topico -> ResponseEntity.ok(
				new DetalhesTopicoDto(topico))).orElseGet(() ->
				ResponseEntity.notFound().build());
	}

	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<TopicoDto> atualiza (@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form) {
		Optional<Topico> topicoOptional = topicoRepository.findById(id);

		if (topicoOptional.isPresent()){
			Topico topico = form.atualizar(id,topicoRepository);
			return ResponseEntity.ok(new TopicoDto(topicoOptional.get()));
		}
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<TopicoDto> remover (@PathVariable Long id){
		Optional<Topico> topicoOptional = topicoRepository.findById(id);

		if (topicoOptional.isPresent()){
			topicoRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}

		return ResponseEntity.notFound().build();
	}
}
