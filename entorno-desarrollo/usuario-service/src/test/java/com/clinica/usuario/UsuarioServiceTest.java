package com.clinica.usuario.service;

import com.clinica.usuario.dto.UsuarioCreateDTO;
import com.clinica.usuario.dto.UsuarioDTO;
import com.clinica.usuario.model.Usuario;
import com.clinica.usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {
    
    @Mock
    private UsuarioRepository usuarioRepository;
    
    @InjectMocks
    private UsuarioService usuarioService;
    
    private Usuario usuario;
    private UsuarioCreateDTO usuarioCreateDTO;
    
    @BeforeEach
    public void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Admin");
        usuario.setEmail("admin@example.com");
        usuario.setUsername("admin");
        usuario.setRol("ADMIN");
        usuario.setActivo(true);
        
        usuarioCreateDTO = new UsuarioCreateDTO();
        usuarioCreateDTO.setNombre("Admin");
        usuarioCreateDTO.setEmail("admin@example.com");
        usuarioCreateDTO.setUsername("admin");
        usuarioCreateDTO.setPassword("password123");
        usuarioCreateDTO.setRol("ADMIN");
    }
    
    @Test
    public void testCrearUsuario() {
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        
        UsuarioDTO resultado = usuarioService.crearUsuario(usuarioCreateDTO);
        
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("admin", resultado.getUsername());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }
    
    @Test
    public void testObtenerUsuarioPorId() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        
        UsuarioDTO resultado = usuarioService.obtenerPorId(1L);
        
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(usuarioRepository, times(1)).findById(1L);
    }
    
    @Test
    public void testListarTodos() {
        List<Usuario> usuarios = Arrays.asList(usuario);
        when(usuarioRepository.findAll()).thenReturn(usuarios);
        
        List<UsuarioDTO> resultado = usuarioService.listarTodos();
        
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(usuarioRepository, times(1)).findAll();
    }
    
    @Test
    public void testObtenerPorUsername() {
        when(usuarioRepository.findByUsername("admin")).thenReturn(Optional.of(usuario));
        
        UsuarioDTO resultado = usuarioService.obtenerPorUsername("admin");
        
        assertNotNull(resultado);
        assertEquals("admin", resultado.getUsername());
        verify(usuarioRepository, times(1)).findByUsername("admin");
    }
    
    @Test
    public void testActualizarUsuario() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        
        UsuarioDTO resultado = usuarioService.actualizar(1L, usuarioCreateDTO);
        
        assertNotNull(resultado);
        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }
    
    @Test
    public void testEliminarUsuario() {
        doNothing().when(usuarioRepository).deleteById(1L);
        
        usuarioService.eliminar(1L);
        
        verify(usuarioRepository, times(1)).deleteById(1L);
    }
}
