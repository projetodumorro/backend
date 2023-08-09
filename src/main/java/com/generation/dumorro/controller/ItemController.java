	package com.generation.dumorro.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.dumorro.model.Item;
import com.generation.dumorro.repository.CategoriaRepository;
import com.generation.dumorro.repository.ItemRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/itens")
@CrossOrigin(origins = "*", allowedHeaders = "*")

public class ItemController {

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private CategoriaRepository categoriaRepository;

	@GetMapping
	public ResponseEntity<List<Item>> getAll() {
		return ResponseEntity.ok(itemRepository.findAll());

	}

	@GetMapping("/{id}")
	public ResponseEntity<Item> getById(@PathVariable Long id) {
		return itemRepository.findById(id).map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@GetMapping("/nome/{nome}")
	public ResponseEntity<List<Item>> getByTitle(@PathVariable String nome) {
		return ResponseEntity.ok(itemRepository.findAllByNomeContainingIgnoreCase(nome));
	}

	@GetMapping("/tipo/{tipo}")
	public ResponseEntity<List<Item>> getByTipo(@PathVariable String tipo) {
		return ResponseEntity.ok(itemRepository.findAllByNomeContainingIgnoreCase(tipo));
	}

	@PostMapping
	public ResponseEntity<Item> post(@Valid @RequestBody Item item) {
		if (categoriaRepository.existsById(item.getCategoria().getId()))
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(itemRepository.save(item));

		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria não existe!", null);
	}

	@PutMapping
	public ResponseEntity<Item> put(@Valid @RequestBody Item item) {
		if (itemRepository.existsById(item.getId())) {
			if (categoriaRepository.existsById(item.getCategoria().getId()))
				return ResponseEntity.status(HttpStatus.OK)
						.body(itemRepository.save(item));

			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria não existe!", null);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		Optional<Item> item = itemRepository.findById(id);

		if (item.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		itemRepository.deleteById(id);
	}

}
