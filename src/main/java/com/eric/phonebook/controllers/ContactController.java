package com.eric.phonebook.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eric.phonebook.entities.Contact;
import com.eric.phonebook.services.ContactService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Contacts", description = "Endpoints para gerenciamento de contatos")
@RestController
@RequestMapping("/contacts")
public class ContactController {

	private final ContactService service;

	public ContactController(ContactService service) {
		this.service = service;
	}

	@Operation(summary = "Lista todos os contatos", description = "Retorna todos os contatos cadastrados")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Lista de contatos retornada com sucesso"),
			@ApiResponse(responseCode = "401", description = "Usuário não autenticado") })
	@GetMapping
	public ResponseEntity<List<Contact>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}

	@Operation(summary = "Busca contato por ID", description = "Retorna um contato específico pelo seu identificador")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Contato encontrado"),
			@ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
			@ApiResponse(responseCode = "404", description = "Contato não encontrado") })
	@GetMapping("/{id}")
	public ResponseEntity<Contact> findById(
			@Parameter(description = "ID do contato", example = "1") @PathVariable Long id) {

		return ResponseEntity.ok(service.findById(id));
	}

	@Operation(summary = "Cria um novo contato", description = "Cadastra um novo contato no sistema")
	@ApiResponses({ @ApiResponse(responseCode = "201", description = "Contato criado com sucesso"),
			@ApiResponse(responseCode = "400", description = "Dados inválidos"),
			@ApiResponse(responseCode = "401", description = "Usuário não autenticado") })
	@PostMapping
	public ResponseEntity<Contact> insert(@Valid @RequestBody Contact contact) {

		Contact saved = service.insert(contact);

		URI uri = URI.create("/contacts/" + saved.getId());

		return ResponseEntity.created(uri).body(saved);
	}

	@Operation(summary = "Atualiza um contato", description = "Atualiza os dados de um contato existente")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Contato atualizado com sucesso"),
			@ApiResponse(responseCode = "400", description = "Dados inválidos"),
			@ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
			@ApiResponse(responseCode = "404", description = "Contato não encontrado") })
	@PutMapping("/{id}")
	public ResponseEntity<Contact> update(
			@Parameter(description = "ID do contato", example = "1") @PathVariable Long id,
			@Valid @RequestBody Contact contact) {

		return ResponseEntity.ok(service.update(id, contact));
	}

	@Operation(summary = "Exclui um contato", description = "Remove um contato. Apenas usuários ADMIN podem executar esta operação.")
	@ApiResponses({ @ApiResponse(responseCode = "204", description = "Contato excluído com sucesso"),
			@ApiResponse(responseCode = "401", description = "Usuário não autenticado"),
			@ApiResponse(responseCode = "403", description = "Usuário sem permissão de administrador"),
			@ApiResponse(responseCode = "404", description = "Contato não encontrado") })
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@Parameter(description = "ID do contato", example = "1") @PathVariable Long id) {

		service.delete(id);

		return ResponseEntity.noContent().build();
	}
}