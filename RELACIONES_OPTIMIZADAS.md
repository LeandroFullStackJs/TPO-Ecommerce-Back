# 🎨 **RELACIONES OPTIMIZADAS - E-COMMERCE DE ARTE**

## 📋 **Resumen de Optimizaciones Implementadas**

### 🔗 **Relaciones Implementadas**

#### 1. **Artista ↔ Producto** (One-to-Many)
```java
// En Artista.java
@OneToMany(mappedBy = "artistaEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
private List<Producto> productos = new ArrayList<>();

// En Producto.java  
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "artista_id", foreignKey = @ForeignKey(name = "FK_producto_artista"))
private Artista artistaEntity;
```

#### 2. **Usuario ↔ Producto** (One-to-Many) - Productos creados
```java
// En Usuario.java
@OneToMany(mappedBy = "usuarioCreador", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
private List<Producto> productosCreados = new ArrayList<>();

// En Producto.java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "usuario_id", foreignKey = @ForeignKey(name = "FK_producto_usuario"))
private Usuario usuarioCreador;
```

#### 3. **Usuario ↔ Pedido** (One-to-Many)
```java
// En Usuario.java
@OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
private List<Pedido> pedidos = new ArrayList<>();

// En Pedido.java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "usuario_id", nullable = false, foreignKey = @ForeignKey(name = "FK_pedido_usuario"))
private Usuario usuario;
```

#### 4. **Usuario ↔ Direccion** (One-to-Many)
```java
// En Usuario.java
@OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
private List<Direccion> direcciones = new ArrayList<>();

// En Direccion.java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "usuario_id", nullable = false, foreignKey = @ForeignKey(name = "FK_direccion_usuario"))
private Usuario usuario;
```

#### 5. **Pedido ↔ PedidoItem** (One-to-Many) - **NUEVA ENTIDAD**
```java
// En Pedido.java
@OneToMany(mappedBy = "pedido", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
private List<PedidoItem> items = new ArrayList<>();

// En PedidoItem.java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "pedido_id", nullable = false, foreignKey = @ForeignKey(name = "FK_pedido_item_pedido"))
private Pedido pedido;
```

#### 6. **Producto ↔ PedidoItem** (One-to-Many)
```java
// En Producto.java
@OneToMany(mappedBy = "producto", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
private List<PedidoItem> pedidoItems = new ArrayList<>();

// En PedidoItem.java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "producto_id", nullable = false, foreignKey = @ForeignKey(name = "FK_pedido_item_producto"))
private Producto producto;
```

#### 7. **Producto ↔ Categoria** (Many-to-Many)
```java
// En Producto.java
@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
@JoinTable(
    name = "productos_categorias",
    joinColumns = @JoinColumn(name = "producto_id", foreignKey = @ForeignKey(name = "FK_productos_categorias_producto")),
    inverseJoinColumns = @JoinColumn(name = "categoria_id", foreignKey = @ForeignKey(name = "FK_productos_categorias_categoria"))
)
private List<Categoria> categorias = new ArrayList<>();

// En Categoria.java
@ManyToMany(mappedBy = "categorias", fetch = FetchType.LAZY)
private List<Producto> productos = new ArrayList<>();
```

#### 8. **Pedido ↔ Direccion** (Many-to-One) - Dirección de envío
```java
// En Pedido.java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "direccion_envio_id", foreignKey = @ForeignKey(name = "FK_pedido_direccion"))
private Direccion direccionEnvio;

// En Direccion.java
@OneToMany(mappedBy = "direccionEnvio", fetch = FetchType.LAZY)
private List<Pedido> pedidosEnvio = new ArrayList<>();
```

---

## 🆕 **Nueva Entidad: PedidoItem**

### **Propósito:**
- Representa los elementos individuales dentro de un pedido
- Almacena cantidad, precio unitario y subtotal
- Permite historial de precios (precio al momento de la compra)

### **Campos:**
- `id` - ID único
- `cantidad` - Cantidad del producto
- `precioUnitario` - Precio al momento de la compra
- `subtotal` - Cantidad × Precio unitario (calculado automáticamente)
- `pedido` - Relación al pedido padre
- `producto` - Relación al producto comprado

### **Métodos útiles:**
- `calcularSubtotal()` - Calcula automáticamente el subtotal
- `@PrePersist` y `@PreUpdate` - Ejecuta cálculo antes de guardar

---

## ✨ **Mejoras Implementadas**

### **1. Optimización de Performance:**
- ✅ `FetchType.LAZY` en todas las relaciones para evitar N+1 queries
- ✅ `@JsonIgnore` en relaciones inversas para evitar recursión infinita
- ✅ Cascade configurado apropiadamente según necesidades

### **2. Integridad Referencial:**
- ✅ Foreign Keys nombradas para mejor debugging
- ✅ Validaciones en campos críticos
- ✅ `orphanRemoval = true` donde corresponde

### **3. Funcionalidades de Negocio:**
- ✅ Cálculo automático de totales en pedidos
- ✅ Métodos helper para agregar/remover items
- ✅ Enum para estados de pedido
- ✅ Contador de productos por categoría

### **4. Campos Adicionales Útiles:**
- ✅ Timestamps de creación y actualización
- ✅ Campos de estado (activo, destacado, principal)
- ✅ Observaciones y notas
- ✅ Código postal en direcciones

---

## 🗄️ **Estructura de Base de Datos Resultante**

```
usuarios (id, nombre, apellido, email, password, role)
├── pedidos (id, fecha, estado, total, usuario_id, direccion_envio_id)
│   └── pedido_items (id, cantidad, precio_unitario, subtotal, pedido_id, producto_id)
├── direcciones (id, calle, numero, localidad, provincia, pais, codigo_postal, usuario_id)
└── productos (id, nombre_obra, descripcion, precio, stock, artista_id, usuario_id)

artistas (id, nombre, biografia, email, imagen_perfil, activo)
└── productos (artista_id FK)

categorias (id, nombre, descripcion, activa, orden)
└── productos_categorias (producto_id, categoria_id) -- Tabla intermedia

direcciones
└── pedidos (direccion_envio_id FK) -- Para envíos
```

---

## 🚀 **Próximos Pasos Recomendados**

1. **Actualizar DTOs** para incluir las nuevas relaciones
2. **Modificar servicios** para aprovechar las relaciones optimizadas
3. **Crear endpoints** para gestionar PedidoItems
4. **Implementar consultas optimizadas** usando JOIN FETCH cuando sea necesario
5. **Agregar índices** en campos frecuentemente consultados

---

## 📊 **Consultas Útiles Disponibles**

### **En PedidoItemRepository:**
- `contarVentasProducto(Long productoId)` - Total vendido de un producto
- `findProductosMasVendidos()` - Ranking de productos
- `calcularIngresosPorProducto(Long productoId)` - Ingresos por producto

### **En Categoria:**
- `contarProductosActivos()` - Productos activos en la categoría

### **En Pedido:**
- `calcularTotal()` - Calcula total basado en items
- `agregarItem(PedidoItem item)` - Agrega item y recalcula
- `removerItem(PedidoItem item)` - Remueve item y recalcula

---

## ⚡ **Beneficios de esta Arquitectura**

✅ **Escalabilidad:** Relaciones optimizadas para crecimiento  
✅ **Performance:** Lazy loading evita consultas innecesarias  
✅ **Mantenibilidad:** Código limpio y bien estructurado  
✅ **Integridad:** Foreign keys y validaciones garantizan consistencia  
✅ **Flexibilidad:** Fácil extensión para nuevas funcionalidades  
✅ **Frontend-Ready:** Estructuras preparadas para integración con React