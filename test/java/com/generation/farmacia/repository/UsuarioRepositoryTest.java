package com.generation.farmacia.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.generation.farmacia.model.Usuario;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioRepositoryTest {
	@Autowired 
	private UsuarioRepository repository;
	
	@BeforeAll
	void start() {
		
		repository.save(new Usuario(0L, "Maiar silva", "isadora@gmail.com", "51 e pinga"));
		
		repository.save(new Usuario(0L, "Michael silva", "michaeltrimundial@gmail.com","12345"));
		
		repository.save(new Usuario(0L, "Brocco silva", "broco@gmail.com", "brocolis"));
		
		repository.save(new Usuario(0L, "Mayara anjos", "will31smith@gmail.com", "cenoura"));
	}
	
	@Test
	@DisplayName("Teste que retorna 1 usuario")
	public void retornaUmUsuario() {
		
		Optional<Usuario> usuario = repository.findByUsuario("isadora@gmail.com");
		assertTrue(usuario.get().getUsuario().equals("isadora@gmail.com"));
	}
	@Test
	@DisplayName("Teste que retorna 3 usuarios")
	public void deveRetornarTresUsuarios() {
		
		List<Usuario> listaDeUsuarios = repository.findAllByNomeContainingIgnoreCase("silva");
		assertEquals(3, listaDeUsuarios.size());
		assertTrue(listaDeUsuarios.get(0).getNome().equals("Maiar silva"));
		assertTrue(listaDeUsuarios.get(1).getNome().equals("Michael silva"));
		assertTrue(listaDeUsuarios.get(2).getNome().equals("Brocco silva"));
		
	}
	
	@AfterAll
	public void end() {
		repository.deleteAll();
	}
}
