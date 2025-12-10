package org.eloyprado.prograavanzada.service;

import org.eloyprado.prograavanzada.Repository.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import usuario.Usuario;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UsuarioRepository usuarioRepository;

    public CustomOAuth2UserService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String username = (String) attributes.get("login");

        Usuario usuario = usuarioRepository.findByUsername(username).orElse(null);

        if (usuario == null) {
            usuario = new Usuario();
            usuario.setUsername(username);
            usuario.setPassword(""); // Sin pasaporte
            usuario.setRole("ROLE_USER");
            usuario.setPrestigio(800);
            usuarioRepository.save(usuario);
        }

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(usuario.getRole()));

        return new DefaultOAuth2User(authorities, attributes, "login");
    }
}
