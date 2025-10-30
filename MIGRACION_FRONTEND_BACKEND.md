# 🔧 MIGRACIÓN BACKEND - COMPLETAR COMPATIBILIDAD CON FRONTEND

## ⚠️ CAMBIOS PENDIENTES EN ProductoService.java

### 1. Reemplazar todas las referencias a `convertirADTO` por `productoMapper.toDTO`

```java
// ANTES:
.map(this::convertirADTO)

// DESPUÉS:
.map(productoMapper::toDTO)
```

### 2. Actualizar método deleteProducto - Línea ~140

```java
// ANTES:
if (!usuarioActual.getRole().equals("ADMIN") && !producto.getUsuarioId().equals(usuarioActual.getId())) {

// DESPUÉS:
if (!usuarioActual.getRole().equals("ADMIN") && 
    (producto.getUsuarioCreador() == null || !producto.getUsuarioCreador().getId().equals(usuarioActual.getId()))) {
```

### 3. Actualizar método updateProducto - Línea ~177

```java
// ANTES:
if (!usuarioActual.getRole().equals("ADMIN") && !producto.getUsuarioId().equals(usuarioActual.getId())) {

// DESPUÉS:
if (!usuarioActual.getRole().equals("ADMIN") && 
    (producto.getUsuarioCreador() == null || !producto.getUsuarioCreador().getId().equals(usuarioActual.getId()))) {
```

### 4. Eliminar métodos obsoletos al final del archivo:

- `convertirADTO(Producto producto)` - Línea ~218
- `convertirAEntidad(ProductoCreateDTO dto)` - Línea ~254
- `actualizarProducto(Producto producto, ProductoUpdateDTO dto)` - Línea ~261

### 5. Actualizar imports:

```java
// Añadir:
import com.api.e_commerce.repository.UsuarioRepository;
import com.api.e_commerce.repository.ArtistaRepository;
```

### 6. Inyectar repositories necesarios:

```java
@Autowired
private UsuarioRepository usuarioRepository;

@Autowired 
private ArtistaRepository artistaRepository;
```

## 🔄 TESTING DESPUÉS DE MIGRACIÓN

1. **Compilar proyecto:**
   ```bash
   ./mvnw.cmd clean compile
   ```

2. **Probar endpoints principales:**
   ```
   GET /api/productos
   GET /api/categorias
   GET /api/artistas
   GET /api/health
   ```

3. **Verificar relaciones:**
   - Productos con artistas
   - Productos con categorías
   - Usuarios con productos creados

## 📋 ENDPOINTS NUEVOS DISPONIBLES PARA FRONTEND

### PedidoItem Management:
- `GET /api/pedido-items/pedido/{pedidoId}` - Items de un pedido
- `POST /api/pedido-items` - Crear item
- `PUT /api/pedido-items/{id}` - Actualizar item
- `DELETE /api/pedido-items/{id}` - Eliminar item

### Utilidades:
- `GET /api/health` - Estado del servidor
- `GET /api/info` - Información de la API
- `GET /api/proxy/image?url={external_url}` - Proxy de imágenes
- `GET /api/config/frontend` - Configuración para frontend

### Estadísticas (Admin):
- `GET /api/pedido-items/estadisticas/producto/{id}` - Stats por producto
- `GET /api/pedido-items/estadisticas/mas-vendidos` - Productos populares

## 🎨 COMPATIBILIDAD CON FRONTEND ACTUAL

El `ProductoDTO` incluye aliases para mantener compatibilidad:

```javascript
// Frontend puede usar tanto:
product.nombreObra  // Campo backend
product.name        // Alias frontend

product.artista     // Campo backend  
product.artist      // Alias frontend

product.tecnica     // Campo backend
product.technique   // Alias frontend
```

## 🚀 PASOS FINALES

1. Completar migración de ProductoService
2. Ejecutar script de datos: `.\cargar-datos-arte.ps1`
3. Probar integración con frontend
4. Verificar que imágenes se cargan correctamente vía proxy

## ⚡ BENEFICIOS OBTENIDOS

✅ Relaciones optimizadas con JPA  
✅ DTOs compatibles con frontend existente  
✅ Nuevas funcionalidades (PedidoItem, estadísticas)  
✅ Proxy de imágenes para CORB  
✅ Endpoints de utilidad para debugging  
✅ Estructura escalable para futuras features  