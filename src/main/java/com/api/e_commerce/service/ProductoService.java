package com.api.e_commerce.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException; // IMPORTANTE: Para errores de propiedad
import org.springframework.security.core.Authentication; // IMPORTANTE
import org.springframework.security.core.context.SecurityContextHolder; // IMPORTANTE
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.api.e_commerce.exception.ProductoNotFoundException;
import com.api.e_commerce.exception.CategoriaNotFoundException;
import com.api.e_commerce.model.Producto;
import com.api.e_commerce.model.Categoria;
import com.api.e_commerce.model.Usuario; // IMPORTANTE
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

    @Autowired
    private ValidationService validationService;

    // --- MÉTODOS GET (Sin cambios en lógica, ya son públicos) ---
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
        validationService.validarId(id, "producto");
        return productoRepository.findById(id)
                .map(this::convertirADTO);
    }

    // --- MÉTODO PARA CREAR PRODUCTO (FUERZA PROPIEDAD) ---
    public ProductoDTO crearProducto(ProductoCreateDTO productoCreateDTO) {
        // Validaciones de negocio
        validationService.validarTextoNoVacio(productoCreateDTO.getNombre(), "nombre");
        validationService.validarPrecio(productoCreateDTO.getPrecio());
        validationService.validarStock(productoCreateDTO.getStock());
        
        if (productoCreateDTO.getAnio() != null) {
            validationService.validarAnio(productoCreateDTO.getAnio());
        }

        // Validación URL (para los 4 campos de imagen)
        validationService.validarUrl(productoCreateDTO.getImagen(), "imagen");
        validationService.validarUrl(productoCreateDTO.getImagenAdicional1(), "imagenAdicional1");
        validationService.validarUrl(productoCreateDTO.getImagenAdicional2(), "imagenAdicional2");
        validationService.validarUrl(productoCreateDTO.getImagenAdicional3(), "imagenAdicional3");
        
        
        Producto producto = convertirAEntidad(productoCreateDTO);
        producto.setFechaCreacion(LocalDateTime.now());
        producto.setFechaActualizacion(LocalDateTime.now());

        // --- ¡FORZAR PROPIEDAD! El creador es el usuario autenticado ---
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioActual = (Usuario) authentication.getPrincipal();
        producto.setUsuarioId(usuarioActual.getId());
        // --- FIN FORZAR PROPIEDAD ---
        
        // Asociar categorías
        if (productoCreateDTO.getCategoriaIds() != null && !productoCreateDTO.getCategoriaIds().isEmpty()) {
            List<Categoria> categorias = categoriaRepository.findAllById(productoCreateDTO.getCategoriaIds());
            
            // Verificar que todas las categorías existen
            if (categorias.size() != productoCreateDTO.getCategoriaIds().size()) {
                throw new CategoriaNotFoundException("Una o más categorías no fueron encontradas");
            }
            
            producto.setCategorias(categorias);
        }
        
        Producto productoGuardado = productoRepository.save(producto);
        return convertirADTO(productoGuardado);
    }

    // --- MÉTODO PARA ELIMINAR PRODUCTO (VERIFICA PROPIEDAD) ---
    public void deleteProducto(Long id) {
        validationService.validarId(id, "producto");
        
        Producto producto = productoRepository.findById(id)
            .orElseThrow(() -> new ProductoNotFoundException(id));

        // --- ¡VERIFICAR PROPIEDAD! Solo el dueño o Admin puede borrar ---
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioActual = (Usuario) authentication.getPrincipal();
        
        if (!usuarioActual.getRole().equals("ADMIN") && !producto.getUsuarioId().equals(usuarioActual.getId())) {
            throw new AccessDeniedException("No tienes permiso para eliminar este producto.");
        }
        // --- FIN VERIFICACIÓN DE PROPIEDAD ---
        
        productoRepository.deleteById(id);
    }    


    // --- MÉTODO PARA ACTUALIZAR PRODUCTO (VERIFICA PROPIEDAD) ---
    public ProductoDTO updateProducto(Long id, ProductoUpdateDTO productoDTO) {
        validationService.validarId(id, "producto");
        
        // Validaciones de los datos a actualizar
        if (productoDTO.getPrecio() != null) {
            validationService.validarPrecio(productoDTO.getPrecio());
        }
        if (productoDTO.getStock() != null) {
            validationService.validarStock(productoDTO.getStock());
        }
        if (productoDTO.getAnio() != null) {
            validationService.validarAnio(productoDTO.getAnio());
        }
        
        // Validación URL (para los 4 campos de imagen)
        if (productoDTO.getImagen() != null) { validationService.validarUrl(productoDTO.getImagen(), "imagen"); }
        if (productoDTO.getImagenAdicional1() != null) { validationService.validarUrl(productoDTO.getImagenAdicional1(), "imagenAdicional1"); }
        if (productoDTO.getImagenAdicional2() != null) { validationService.validarUrl(productoDTO.getImagenAdicional2(), "imagenAdicional2"); }
        if (productoDTO.getImagenAdicional3() != null) { validationService.validarUrl(productoDTO.getImagenAdicional3(), "imagenAdicional3"); }


        return productoRepository.findById(id)
            .map(producto -> {
                // --- ¡VERIFICAR PROPIEDAD! Solo el dueño o Admin puede actualizar ---
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                Usuario usuarioActual = (Usuario) authentication.getPrincipal();

                if (!usuarioActual.getRole().equals("ADMIN") && !producto.getUsuarioId().equals(usuarioActual.getId())) {
                    throw new AccessDeniedException("No tienes permiso para modificar este producto.");
                }
                // --- FIN VERIFICACIÓN DE PROPIEDAD ---

                actualizarCamposProducto(producto, productoDTO);
                producto.setFechaActualizacion(LocalDateTime.now());
                
                // Actualizar categorías
                if (productoDTO.getCategoriaIds() != null) {
                    List<Categoria> categorias = categoriaRepository.findAllById(productoDTO.getCategoriaIds());
                    
                    // Verificar que todas las categorías existen
                    if (categorias.size() != productoDTO.getCategoriaIds().size()) {
                        throw new CategoriaNotFoundException("Una o más categorías no fueron encontradas");
                    }
                    
                    producto.setCategorias(categorias);
                }
                
                Producto productoActualizado = productoRepository.save(producto);
                return convertirADTO(productoActualizado);
            })
            .orElseThrow(() -> new ProductoNotFoundException(id));
    }
    
    // --- MÉTODOS AUXILIARES (DEJADOS COMO ESTÁN EN TUS ARCHIVOS) ---
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
    
    public ProductoDTO convertirADTO(Producto producto) {
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
