package com.generation.farmacia.service;

import java.nio.charset.Charset;
import org.apache.commons.codec.binary.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import com.generation.farmacia.model.UserLogin;
import com.generation.farmacia.model.Usuario;
import com.generation.farmacia.repository.UsuarioRepository;

@Service
public class UsuarioService {



	@Autowired
	private UsuarioRepository repository;

	
	public Optional<Usuario>cadastraUsuario(Usuario usuario){
		
		if(repository.findByUsuario(usuario.getUsuario()).isPresent())
			return Optional.empty();
	
			
			usuario.setSenha(criptografarSenha(usuario.getSenha()));
			
			
			return Optional.of(repository.save(usuario));
	};
	

	public String criptografarSenha(String senha) {

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		return encoder.encode(senha);
	};


	public Optional<UserLogin> autenticaUsuario(Optional<UserLogin> usuarioLogin){
		Optional<Usuario> usuario = repository.findByUsuario(usuarioLogin.get().getUsuario());
		
		if(usuario.isPresent()) {
			if(compararSenhas(usuarioLogin.get().getSenha(), usuario.get().getSenha())) {
				usuarioLogin.get().setId(usuario.get().getId());
				usuarioLogin.get().setNome(usuario.get().getNome());
				usuarioLogin.get().setToken(gerarBasicToken(usuarioLogin.get().getUsuario(), usuarioLogin.get().getSenha()));
				usuarioLogin.get().setSenha(usuario.get().getSenha());	
				
				return usuarioLogin;
			}					
		}		
		return Optional.empty();
	}
	
	private boolean compararSenhas(String senhaDigitada, String senhaBanco) {
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		return encoder.matches(senhaDigitada, senhaBanco);
	}
	
	private String gerarBasicToken(String usuario, String senha) {

		String token = usuario + ":" + senha;
		byte[] tokenBase64 = Base64.encodeBase64(token.getBytes(Charset.forName("US-ASCII")));
		return "Basic " + new String(tokenBase64);
	}
}