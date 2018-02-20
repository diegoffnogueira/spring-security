package com.diego.api.security.services;

import com.diego.api.security.entity.Usuario;

import java.util.Optional;

public interface UsuarioService {

	/**
	 * Busca e retorna um usuário dado um email.
	 * 
	 * @param email
	 * @return Optional<Usuario>
	 */
	Optional<Usuario> buscarPorEmail(String email);

}
