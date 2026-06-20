package com.serfagab.service;

import org.springframework.stereotype.Service;

import com.serfagab.dto.AutenticacionFilter;
import com.serfagab.model.Usuario;
import com.serfagab.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AutenticacionService {

    private final UsuarioRepository usuarioRepository;

    public Usuario autenticar(AutenticacionFilter filter) {
        return usuarioRepository.findByLoginAndClave(filter.getLogin(), filter.getClave());
    }
}
