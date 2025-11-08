package com.api.e_commerce.service;

import com.api.e_commerce.dto.CategoriaDTO;
import com.api.e_commerce.model.Categoria;
import com.api.e_commerce.repository.CategoriaRepository;
import com.api.e_commerce.exception.CategoriaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoriaService {
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    public List<CategoriaDTO> obtenerTodasLasCategorias() {
        return categoriaRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    public CategoriaDTO obtenerCategoriaPorId(Long id) {
        return categoriaRepository.findById(id)
                .map(this::convertirADTO)
                .orElseThrow(() -> new CategoriaNotFoundException("No se encontró la categoría con id: " + id));
    }
    
    public Optional<CategoriaDTO> obtenerCategoriaPorNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre)
                .map(this::convertirADTO);
    }
    
    public CategoriaDTO crearCategoria(CategoriaDTO categoriaDTO) {
        Categoria categoria = convertirAEntidad(categoriaDTO);
        Categoria categoriaGuardada = categoriaRepository.save(categoria);
        return convertirADTO(categoriaGuardada);
    }
    
    public CategoriaDTO actualizarCategoria(Long id, CategoriaDTO categoriaDTO) {
        return categoriaRepository.findById(id)
                .map(categoria -> {
                    categoria.setNombre(categoriaDTO.getNombre());
                    return convertirADTO(categoriaRepository.save(categoria));
                })
                .orElseThrow(() -> new CategoriaNotFoundException("No se encontró la categoría con id: " + id));
    }
    
    public void eliminarCategoria(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new CategoriaNotFoundException("No se encontró la categoría con id: " + id);
        }
        categoriaRepository.deleteById(id);
    }
    
    private CategoriaDTO convertirADTO(Categoria categoria) {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setId(categoria.getId());
        dto.setNombre(categoria.getNombre());
        return dto;
    }
    
    private Categoria convertirAEntidad(CategoriaDTO dto) {
        Categoria categoria = new Categoria();
        categoria.setNombre(dto.getNombre());
        return categoria;
    }
}