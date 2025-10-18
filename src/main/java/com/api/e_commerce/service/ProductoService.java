package com.api.e_commerce.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.e_commerce.model.Producto;
import com.api.e_commerce.model.Categoria;
import com.api.e_commerce.repository.ProductoRepository;
import com.api.e_commerce.repository.CategoriaRepository;
import com.api.e_commerce.dto.ProductoDTO;
import com.api.e_commerce.dto.ProductoCreateDTO;
import com.api.e_commerce.dto.ProductoUpdateDTO;

@Service
@Transactional
public class ProductoService {
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<ProductoDTO> getAllProductos() {
        return productoRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    public List<ProductoDTO> getProductosActivos() {
        return productoRepository.findByActivoTrue().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    public List<ProductoDTO> getProductosDestacados() {
        return productoRepository.findByDestacadoTrue().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    public List<ProductoDTO> getProductosPorCategoria(Long categoriaId) {
        return productoRepository.findByCategorias_Id(categoriaId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    public List<ProductoDTO> buscarProductos(String termino) {
        return productoRepository.findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCase(termino, termino)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public Optional<ProductoDTO> getProductoById(Long id) {
        return productoRepository.findById(id)
                .map(this::convertirADTO);
    }

    public ProductoDTO crearProducto(ProductoCreateDTO productoCreateDTO) {
        Producto producto = convertirAEntidad(productoCreateDTO);
        producto.setFechaCreacion(LocalDateTime.now());
        producto.setFechaActualizacion(LocalDateTime.now());
        
        // Asociar categorías si se proporcionaron
        if (productoCreateDTO.getCategoriaIds() != null && !productoCreateDTO.getCategoriaIds().isEmpty()) {
            List<Categoria> categorias = categoriaRepository.findAllById(productoCreateDTO.getCategoriaIds());
            producto.setCategorias(categorias);
        }
        
        Producto productoGuardado = productoRepository.save(producto);
        return convertirADTO(productoGuardado);
    }

    public void deleteProducto(Long id) {
        productoRepository.deleteById(id);
    }    

    public ProductoDTO updateProducto(Long id, ProductoUpdateDTO productoDTO) {
        return productoRepository.findById(id)
            .map(producto -> {
                actualizarCamposProducto(producto, productoDTO);
                producto.setFechaActualizacion(LocalDateTime.now());
                
                // Actualizar categorías si se proporcionaron
                if (productoDTO.getCategoriaIds() != null) {
                    List<Categoria> categorias = categoriaRepository.findAllById(productoDTO.getCategoriaIds());
                    producto.setCategorias(categorias);
                }
                
                Producto productoActualizado = productoRepository.save(producto);
                return convertirADTO(productoActualizado);
            })
            .orElse(null);
    }
    
    private void actualizarCamposProducto(Producto producto, ProductoUpdateDTO dto) {
        if (dto.getNombre() != null) producto.setNombre(dto.getNombre());
        if (dto.getDescripcion() != null) producto.setDescripcion(dto.getDescripcion());
        if (dto.getPrecio() != null) producto.setPrecio(dto.getPrecio());
        if (dto.getStock() != null) producto.setStock(dto.getStock());
        if (dto.getImagen() != null) producto.setImagen(dto.getImagen());
        if (dto.getImagenAdicional1() != null) producto.setImagenAdicional1(dto.getImagenAdicional1());
        if (dto.getImagenAdicional2() != null) producto.setImagenAdicional2(dto.getImagenAdicional2());
        if (dto.getImagenAdicional3() != null) producto.setImagenAdicional3(dto.getImagenAdicional3());
        if (dto.getActivo() != null) producto.setActivo(dto.getActivo());
        if (dto.getDestacado() != null) producto.setDestacado(dto.getDestacado());
        // Campos específicos para obras de arte (exactos del frontend)
        if (dto.getArtista() != null) producto.setArtista(dto.getArtista());
        if (dto.getArtistaId() != null) producto.setArtistaId(dto.getArtistaId());
        if (dto.getUsuarioId() != null) producto.setUsuarioId(dto.getUsuarioId());
        if (dto.getTecnica() != null) producto.setTecnica(dto.getTecnica());
        if (dto.getDimensiones() != null) producto.setDimensiones(dto.getDimensiones());
        if (dto.getAnio() != null) producto.setAnio(dto.getAnio());
        if (dto.getEstilo() != null) producto.setEstilo(dto.getEstilo());
    }
    
    private ProductoDTO convertirADTO(Producto producto) {
        ProductoDTO dto = new ProductoDTO();
        dto.setId(producto.getId());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setPrecio(producto.getPrecio());
        dto.setStock(producto.getStock());
        dto.setImagen(producto.getImagen());
        dto.setImagenAdicional1(producto.getImagenAdicional1());
        dto.setImagenAdicional2(producto.getImagenAdicional2());
        dto.setImagenAdicional3(producto.getImagenAdicional3());
        dto.setActivo(producto.getActivo());
        dto.setDestacado(producto.getDestacado());
        dto.setFechaCreacion(producto.getFechaCreacion());
        dto.setFechaActualizacion(producto.getFechaActualizacion());
        // Campos específicos para obras de arte (exactos del frontend)
        dto.setArtista(producto.getArtista());
        dto.setArtistaId(producto.getArtistaId());
        dto.setUsuarioId(producto.getUsuarioId());
        dto.setTecnica(producto.getTecnica());
        dto.setDimensiones(producto.getDimensiones());
        dto.setAnio(producto.getAnio());
        dto.setEstilo(producto.getEstilo());
        
        // Convertir categorías a lista de nombres
        if (producto.getCategorias() != null) {
            dto.setCategorias(producto.getCategorias().stream()
                    .map(Categoria::getNombre)
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }
    
    private Producto convertirAEntidad(ProductoCreateDTO dto) {
        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        producto.setImagen(dto.getImagen());
        producto.setImagenAdicional1(dto.getImagenAdicional1());
        producto.setImagenAdicional2(dto.getImagenAdicional2());
        producto.setImagenAdicional3(dto.getImagenAdicional3());
        producto.setActivo(dto.getActivo() != null ? dto.getActivo() : true);
        producto.setDestacado(dto.getDestacado() != null ? dto.getDestacado() : false);
        // Campos específicos para obras de arte (exactos del frontend)
        producto.setArtista(dto.getArtista());
        producto.setArtistaId(dto.getArtistaId());
        producto.setUsuarioId(dto.getUsuarioId());
        producto.setTecnica(dto.getTecnica());
        producto.setDimensiones(dto.getDimensiones());
        producto.setAnio(dto.getAnio());
        producto.setEstilo(dto.getEstilo());
        return producto;
    }
}
