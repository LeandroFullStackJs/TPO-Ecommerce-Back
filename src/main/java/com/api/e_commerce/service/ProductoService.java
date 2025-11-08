package com.api.e_commerce.service;

import java.util.ArrayList;
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
import com.api.e_commerce.dto.ProductoMapper;

@Service
@Transactional
public class ProductoService {
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ValidationService validationService;

    @Autowired 
    private ProductoMapper productoMapper;

    // --- MÉTODOS GET (Sin cambios en lógica, ya son públicos) ---
    public List<ProductoDTO> getAllProductos() {
        return productoRepository.findAll().stream()
                .map(productoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<ProductoDTO> getProductosActivos() {
        return productoRepository.findByActivoTrue().stream()
                .map(productoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<ProductoDTO> getProductosDestacados() {
        return productoRepository.findByDestacadoTrue().stream()
                .map(productoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<ProductoDTO> getProductosPorCategoria(Long categoriaId) {
        return productoRepository.findByCategorias_Id(categoriaId).stream()
                .map(productoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<ProductoDTO> buscarProductos(String termino) {
        return productoRepository.findByNombreObraContainingIgnoreCaseOrDescripcionContainingIgnoreCase(termino, termino)
                .stream()
                .map(productoMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<ProductoDTO> getProductoById(Long id) {
        validationService.validarId(id, "producto");
        return productoRepository.findById(id)
                .map(productoMapper::toDTO);
    }

// --- MÉTODO PARA CREAR PRODUCTO (CON VALIDACIÓN DETALLADA) ---
public ProductoDTO crearProducto(ProductoCreateDTO productoCreateDTO) {
    List<String> errores = new ArrayList<>();

    // Validaciones de negocio — recolectamos los errores en lugar de lanzar uno por uno
    try {
        validationService.validarTextoNoVacio(productoCreateDTO.getNombre(), "nombre");
    } catch (IllegalArgumentException e) {
        errores.add("nombre: " + e.getMessage());
    }

    try {
        validationService.validarPrecio(productoCreateDTO.getPrecio());
    } catch (IllegalArgumentException e) {
        errores.add("precio: " + e.getMessage());
    }

    try {
        validationService.validarStock(productoCreateDTO.getStock());
    } catch (IllegalArgumentException e) {
        errores.add("stock: " + e.getMessage());
    }

    if (productoCreateDTO.getAnio() != null) {
        try {
            validationService.validarAnio(productoCreateDTO.getAnio());
        } catch (IllegalArgumentException e) {
            errores.add("anio: " + e.getMessage());
        }
    }

    // Si hubo errores, lanzamos una excepción con todos los detalles
    if (!errores.isEmpty()) {
        throw new ProductoNotFoundException(
            "Faltan campos obligatorios o algunos valores son inválidos.",
            errores
        );
    }

    // Mapeo del DTO a entidad
    Producto producto = productoMapper.fromCreateDTO(productoCreateDTO);
    producto.setFechaCreacion(LocalDateTime.now());
    producto.setFechaActualizacion(LocalDateTime.now());

    // --- FORZAR PROPIEDAD: el creador es el usuario autenticado ---
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Usuario usuarioActual = (Usuario) authentication.getPrincipal();
    producto.setUsuarioCreador(usuarioActual);
    // --- FIN FORZAR PROPIEDAD ---

    // --- Asociar categorías ---
    if (productoCreateDTO.getCategoriaIds() != null && !productoCreateDTO.getCategoriaIds().isEmpty()) {
        List<Categoria> categorias = categoriaRepository.findAllById(productoCreateDTO.getCategoriaIds());

        if (categorias.size() != productoCreateDTO.getCategoriaIds().size()) {
            throw new CategoriaNotFoundException("Una o más categorías no fueron encontradas");
        }

        producto.setCategorias(categorias);
    }

    Producto productoGuardado = productoRepository.save(producto);
    return productoMapper.toDTO(productoGuardado);
}


    // --- MÉTODO PARA ELIMINAR PRODUCTO (VERIFICA PROPIEDAD) ---
    public void deleteProducto(Long id) {
        validationService.validarId(id, "producto");
        
        Producto producto = productoRepository.findById(id)
            .orElseThrow(() -> new ProductoNotFoundException(id));

        // --- ¡VERIFICAR PROPIEDAD! Solo el dueño o Admin puede borrar ---
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuarioActual = (Usuario) authentication.getPrincipal();
        
        if (!usuarioActual.getRole().equals("ADMIN") && 
            (producto.getUsuarioCreador() == null || !producto.getUsuarioCreador().getId().equals(usuarioActual.getId()))) {
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

        return productoRepository.findById(id)
            .map(producto -> {
                // --- ¡VERIFICAR PROPIEDAD! Solo el dueño o Admin puede actualizar ---
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                Usuario usuarioActual = (Usuario) authentication.getPrincipal();

                if (!usuarioActual.getRole().equals("ADMIN") && 
                    (producto.getUsuarioCreador() == null || !producto.getUsuarioCreador().getId().equals(usuarioActual.getId()))) {
                    throw new AccessDeniedException("No tienes permiso para modificar este producto.");
                }
                // --- FIN VERIFICACIÓN DE PROPIEDAD ---

                productoMapper.updateFromDTO(producto, productoDTO);
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
                return productoMapper.toDTO(productoActualizado);
            })
            .orElseThrow(() -> new ProductoNotFoundException(id));
    }

    // --- MÉTODOS DE GESTIÓN DE STOCK ---
    
    public ProductoDTO decrementarStock(Long id, Integer cantidad) {
        return productoRepository.findById(id)
            .map(producto -> {
                if (producto.getStock() < cantidad) {
                    // Usamos el constructor enriquecido para incluir todos los detalles
                    throw new com.api.e_commerce.exception.InsufficientStockException(
                        producto.getId(),
                        producto.getNombreObra(),
                        producto.getStock(),
                        cantidad);
                }
                
                producto.setStock(producto.getStock() - cantidad);
                producto.setFechaActualizacion(LocalDateTime.now());
                
                Producto productoActualizado = productoRepository.save(producto);
                return productoMapper.toDTO(productoActualizado);
            })
            .orElseThrow(() -> new ProductoNotFoundException(id));
    }

    public ProductoDTO incrementarStock(Long id, Integer cantidad) {
        return productoRepository.findById(id)
            .map(producto -> {
                producto.setStock(producto.getStock() + cantidad);
                producto.setFechaActualizacion(LocalDateTime.now());
                
                Producto productoActualizado = productoRepository.save(producto);
                return productoMapper.toDTO(productoActualizado);
            })
            .orElseThrow(() -> new ProductoNotFoundException(id));
    }
}
