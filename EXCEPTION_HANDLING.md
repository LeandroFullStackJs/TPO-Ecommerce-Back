# Sistema de Manejo de Excepciones - E-Commerce

## Visión General

Este documento describe el sistema completo de manejo de excepciones implementado en el e-commerce. El sistema proporciona respuestas de error consistentes, logging apropiado y validaciones robustas.

## Arquitectura del Sistema

### 1. Excepciones Personalizadas

#### Excepciones de Entidades No Encontradas
- `ProductoNotFoundException`: Cuando un producto no existe
- `UsuarioNotFoundException`: Cuando un usuario no existe
- `ArtistaNotFoundException`: Cuando un artista no existe
- `CategoriaNotFoundException`: Cuando una categoría no existe
- `PedidoNotFoundException`: Cuando un pedido no existe

#### Excepciones de Validación
- `InvalidDataException`: Para datos de entrada inválidos
- `PrecioNegativoException`: Para precios negativos
- `InsufficientStockException`: Para problemas de stock

#### Excepciones de Negocio
- `DuplicateDataException`: Para datos duplicados (ej: email ya existe)
- `AuthenticationException`: Para problemas de autenticación

### 2. DTO de Respuesta Estandarizada

```json
{
  "timestamp": "2025-10-21T14:30:00",
  "status": 404,
  "error": "Producto No Encontrado",
  "message": "No se encontró el producto con id: 123",
  "path": "/api/productos/123",
  "details": ["Información adicional cuando aplique"]
}
```

### 3. GlobalExceptionHandler

El `GlobalExceptionHandler` maneja todas las excepciones de manera centralizada:

- ✅ Logging apropiado para cada tipo de error
- ✅ Respuestas HTTP consistentes
- ✅ Información de contexto (timestamp, path, etc.)
- ✅ Detalles adicionales para errores de validación

## Códigos de Estado HTTP

| Excepción | Código HTTP | Descripción |
|-----------|-------------|-------------|
| `*NotFoundException` | 404 | Recurso no encontrado |
| `DuplicateDataException` | 409 | Conflicto de datos |
| `InvalidDataException` | 400 | Datos inválidos |
| `PrecioNegativoException` | 400 | Precio negativo |
| `InsufficientStockException` | 400 | Stock insuficiente |
| `AuthenticationException` | 401 | Error de autenticación |
| `BadCredentialsException` | 401 | Credenciales inválidas |
| `AccessDeniedException` | 403 | Acceso denegado |
| `MethodArgumentNotValidException` | 400 | Error de validación |
| `DataIntegrityViolationException` | 409 | Error de integridad BD |
| `Exception` (genérica) | 500 | Error interno del servidor |

## Validaciones Implementadas

### ValidationService

Proporciona validaciones centralizadas para:

- **Email**: Formato válido
- **Precio**: No nulo, no negativo
- **Stock**: No nulo, no negativo
- **Texto**: No vacío, longitud mínima/máxima
- **Teléfono**: Formato válido
- **ID**: Positivo, no nulo
- **Password**: Longitud mínima
- **Año**: Rango válido (1000 - año actual)

## Uso en los Servicios

### Ejemplo en ProductoService

```java
@Service
public class ProductoService {
    
    @Autowired
    private ValidationService validationService;
    
    public ProductoDTO crearProducto(ProductoCreateDTO dto) {
        // Validaciones
        validationService.validarTextoNoVacio(dto.getNombre(), "nombre");
        validationService.validarPrecio(dto.getPrecio());
        validationService.validarStock(dto.getStock());
        
        // Lógica de negocio...
    }
    
    public ProductoDTO getProductoById(Long id) {
        validationService.validarId(id, "producto");
        
        return productoRepository.findById(id)
            .map(this::convertirADTO)
            .orElseThrow(() -> new ProductoNotFoundException(id));
    }
}
```

### Ejemplo en AuthService

```java
public AuthResponse register(RegisterRequest request) {
    // Validaciones
    validationService.validarEmail(request.getEmail());
    validationService.validarTextoNoVacio(request.getNombre(), "nombre");
    validationService.validarPassword(request.getPassword());
    
    if (usuarioRepository.existsByEmail(request.getEmail())) {
        throw new DuplicateDataException("usuario", "email", request.getEmail());
    }
    
    // Lógica de registro...
}
```

## Uso en los Controladores

Los controladores deben lanzar las excepciones apropiadas:

```java
@GetMapping("/{id}")
public ResponseEntity<ProductoDTO> getProductoById(@PathVariable Long id) {
    Optional<ProductoDTO> producto = productoService.getProductoById(id);
    return producto.map(ResponseEntity::ok)
                  .orElseThrow(() -> new ProductoNotFoundException(id));
}
```

## Logging

### Configuración

El archivo `application-logging.properties` contiene:

- Niveles de log por paquete
- Formato de logging
- Configuración de archivos de log
- Rotación de logs

### Niveles de Log por Tipo de Error

- `WARN`: Errores de negocio (entidades no encontradas, validaciones)
- `ERROR`: Errores técnicos (base de datos, sistema)
- `INFO`: Operaciones normales
- `DEBUG`: Información detallada para desarrollo

## Mejores Prácticas

### 1. En los Servicios
- ✅ Usar `ValidationService` para validaciones comunes
- ✅ Lanzar excepciones específicas, no genéricas
- ✅ Validar IDs antes de usarlos
- ✅ Verificar existencia antes de operaciones críticas

### 2. En los Controladores
- ✅ Dejar que el `GlobalExceptionHandler` maneje las excepciones
- ✅ No usar `try-catch` innecesario
- ✅ Usar `@Valid` para validaciones de entrada

### 3. Para Nuevas Excepciones
- ✅ Heredar de `RuntimeException`
- ✅ Proporcionar constructores útiles
- ✅ Mensajes descriptivos en español
- ✅ Agregar manejo en `GlobalExceptionHandler`

## Ejemplos de Respuestas

### Producto No Encontrado
```json
{
  "timestamp": "2025-10-21T14:30:00",
  "status": 404,
  "error": "Producto No Encontrado",
  "message": "No se encontró el producto con id: 123",
  "path": "/api/productos/123"
}
```

### Error de Validación
```json
{
  "timestamp": "2025-10-21T14:30:00",
  "status": 400,
  "error": "Error de Validación",
  "message": "Los datos proporcionados no son válidos",
  "path": "/api/productos",
  "details": [
    "nombre: El nombre es obligatorio",
    "precio: El precio debe ser mayor a 0"
  ]
}
```

### Datos Duplicados
```json
{
  "timestamp": "2025-10-21T14:30:00",
  "status": 409,
  "error": "Datos Duplicados",
  "message": "Ya existe un usuario con email: test@example.com",
  "path": "/api/auth/register"
}
```

## Mantenimiento

### Agregar Nueva Excepción

1. Crear la clase de excepción en `com.api.e_commerce.exception`
2. Agregar el manejo en `GlobalExceptionHandler`
3. Usar en los servicios correspondientes
4. Actualizar este README

### Agregar Nueva Validación

1. Agregar método en `ValidationService`
2. Usar en los servicios que lo necesiten
3. Documentar el comportamiento

## Testing

Para probar el sistema de excepciones:

1. **Entidades No Encontradas**: Usar IDs que no existen
2. **Validaciones**: Enviar datos inválidos
3. **Duplicados**: Intentar crear recursos ya existentes
4. **Autenticación**: Usar credenciales incorrectas

Todas las respuestas deben seguir el formato estándar de `ErrorResponse`.