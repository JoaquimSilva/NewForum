package com.silva.joaquim.forum.controller;

import com.silva.joaquim.forum.controller.form.AtualizacaoTopicoForm;
import com.silva.joaquim.forum.controller.form.TopicoForm;
import com.silva.joaquim.forum.modelo.Topico;
import com.silva.joaquim.forum.modelo.dto.DetalhesTopicoDto;
import com.silva.joaquim.forum.modelo.dto.TopicoDto;
import com.silva.joaquim.forum.repository.CursoRepository;
import com.silva.joaquim.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
	@Cacheable(value = "listaDeTopicos")
	public Page<TopicoDto> lista (@RequestParam(required = false) String nomeCurso,
								  @PageableDefault(sort = "id",direction = Sort.Direction.ASC, page = 0, size = 10) Pageable paginacao){

		if (nomeCurso == null) {
			Page<Topico> topicos = topicoRepository.findAll(paginacao);
			return TopicoDto.converter(topicos);
		}
		else {
			Page<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso, paginacao);
			return TopicoDto.converter(topicos);
		}

	}
	@PostMapping
	@Transactional
	@CacheEvict(value = "listaDeTopicos",allEntries = true)
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
	@CacheEvict(value = "listaDeTopicos",allEntries = true)
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
	@CacheEvict(value = "listaDeTopicos",allEntries = true)
	public ResponseEntity<TopicoDto> remover (@PathVariable Long id){
		Optional<Topico> topicoOptional = topicoRepository.findById(id);

		if (topicoOptional.isPresent()){
			topicoRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}

		return ResponseEntity.notFound().build();
	}
}
