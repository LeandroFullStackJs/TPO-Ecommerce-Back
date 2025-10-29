package com.api.e_commerce.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.validation.Valid;

import com.api.e_commerce.dto.ProductoDTO;
import com.api.e_commerce.dto.ProductoCreateDTO;
import com.api.e_commerce.dto.ProductoUpdateDTO;
import com.api.e_commerce.exception.ProductoNotFoundException; // Importar excepción
import com.api.e_commerce.service.ProductoService;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = {"http://localhost:5173"})

public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // Obtener todos los productos (público)
    @GetMapping
    public ResponseEntity<List<ProductoDTO>> getAllProductos(
            @RequestParam(required = false) Boolean activo,
            @RequestParam(required = false) Boolean destacado) {
        List<ProductoDTO> productos;
        
        if (destacado != null && destacado) {
            productos = productoService.getProductosDestacados();
        } else if (activo != null && activo) {
            productos = productoService.getProductosActivos();
        } else {
            productos = productoService.getAllProductos();
        }
        
        return ResponseEntity.ok(productos);
    }

    // Obtener productos por categoría (público)
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<ProductoDTO>> getProductosPorCategoria(@PathVariable Long categoriaId) {
        List<ProductoDTO> productos = productoService.getProductosPorCategoria(categoriaId);
        return ResponseEntity.ok(productos);
    }

    // Buscar productos (público)
    @GetMapping("/buscar")
    public ResponseEntity<List<ProductoDTO>> buscarProductos(@RequestParam String q) {
        List<ProductoDTO> productos = productoService.buscarProductos(q);
        return ResponseEntity.ok(productos);
    }

    // Obtener productos destacados (público)
    @GetMapping("/destacados")
    public ResponseEntity<List<ProductoDTO>> getProductosDestacados() {
        List<ProductoDTO> productos = productoService.getProductosDestacados();
        return ResponseEntity.ok(productos);
    }

    // Obtener producto por ID (público)
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> getProductoById(@PathVariable Long id) {
        // Dejamos que el service lance ProductoNotFoundException si no existe
        Optional<ProductoDTO> producto = productoService.getProductoById(id);
        return producto.map(ResponseEntity::ok)
                      .orElseThrow(() -> new ProductoNotFoundException(id)); // Lanzar excepción para 404
    }

    // Crear nuevo producto (Auth - Servicio fuerza propiedad)
    @PostMapping
    @PreAuthorize("isAuthenticated()") // Cambiado a isAuthenticated() ya que el servicio fuerza el userId
    public ResponseEntity<ProductoDTO> crearProducto(@Valid @RequestBody ProductoCreateDTO productoCreateDTO) {
        // Se quita el try-catch. Dejamos que el Service lance excepciones (ej: CategoriaNotFound)
        ProductoDTO producto = productoService.crearProducto(productoCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(producto);
    }

    // Actualizar producto (Auth - Servicio verifica propiedad)
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()") // Cambiado a isAuthenticated() ya que el servicio verifica propiedad
    public ResponseEntity<ProductoDTO> updateProducto(@PathVariable Long id,
                                                    @Valid @RequestBody ProductoUpdateDTO productoDTO) {
        // Se quita el try-catch. Dejamos que el Service lance excepciones (NotFound, AccessDenied, CategoriaNotFound)
        ProductoDTO productoActualizado = productoService.updateProducto(id, productoDTO);
        return ResponseEntity.ok(productoActualizado);
    }

    // Eliminar producto (Auth - Servicio verifica propiedad)
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()") // Cambiado a isAuthenticated() ya que el servicio verifica propiedad
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        // Se quita el try-catch. Dejamos que el Service lance excepciones (NotFound, AccessDenied)
        productoService.deleteProducto(id);
        return ResponseEntity.noContent().build();
    }
}
