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
import com.api.e_commerce.service.ProductoService;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")

public class ProductoController {
    
    @Autowired
    private ProductoService productoService;

    // Obtener todos los productos
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
    
    // Obtener productos por categoría
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<ProductoDTO>> getProductosPorCategoria(@PathVariable Long categoriaId) {
        List<ProductoDTO> productos = productoService.getProductosPorCategoria(categoriaId);
        return ResponseEntity.ok(productos);
    }
    
    // Buscar productos
    @GetMapping("/buscar")
    public ResponseEntity<List<ProductoDTO>> buscarProductos(@RequestParam String q) {
        List<ProductoDTO> productos = productoService.buscarProductos(q);
        return ResponseEntity.ok(productos);
    }
    
    // Obtener productos destacados
    @GetMapping("/destacados")
    public ResponseEntity<List<ProductoDTO>> getProductosDestacados() {
        List<ProductoDTO> productos = productoService.getProductosDestacados();
        return ResponseEntity.ok(productos);
    }

    // Obtener producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> getProductoById(@PathVariable Long id) {
        Optional<ProductoDTO> producto = productoService.getProductoById(id);
        return producto.map(ResponseEntity::ok)
                      .orElseThrow(() -> new com.api.e_commerce.exception.ProductoNotFoundException(id));
    }    // Crear nuevo producto (requiere autenticación)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ProductoDTO> crearProducto(@Valid @RequestBody ProductoCreateDTO productoCreateDTO) {
        try {
            ProductoDTO producto = productoService.crearProducto(productoCreateDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(producto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    // Actualizar producto (requiere autenticación)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<ProductoDTO> updateProducto(@PathVariable Long id, 
                                                    @Valid @RequestBody ProductoUpdateDTO productoDTO) {
        ProductoDTO productoActualizado = productoService.updateProducto(id, productoDTO);
        return productoActualizado != null ? 
                ResponseEntity.ok(productoActualizado) : 
                ResponseEntity.notFound().build();
    }

    // Eliminar producto (requiere autenticación de admin)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        try {
            productoService.deleteProducto(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
