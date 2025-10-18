# 🎯 SCRIPT PARA AGREGAR DATOS DE EJEMPLO AL E-COMMERCE

Write-Host "🚀 AGREGANDO DATOS DE EJEMPLO AL E-COMMERCE" -ForegroundColor Green
Write-Host "=" * 60 -ForegroundColor Green

Write-Host "🔍 COMANDOS PARA VERIFICAR LOS DATOS:" -ForegroundColor Cyan
Write-Host "Invoke-RestMethod -Uri http://localhost:8080/api/categorias -Method GET" -ForegroundColor White
Write-Host "Invoke-RestMethod -Uri http://localhost:8080/api/usuarios -Method GET" -ForegroundColor White
Write-Host "Invoke-RestMethod -Uri http://localhost:8080/api/productos -Method GET" -ForegroundColor White
Write-Host "Invoke-RestMethod -Uri http://localhost:8080/api/direcciones -Method GET" -ForegroundColor White
Write-Host "Invoke-RestMethod -Uri http://localhost:8080/api/pedidos -Method GET" -ForegroundColor Whiterl = "http://localhost:8080/api"

# Esperar a que la aplicación esté lista
Write-Host "⏳ Esperando que la aplicación esté lista..." -ForegroundColor Yellow
Start-Sleep 10

# 1. AGREGAR CATEGORÍAS
Write-Host "`n📂 1. AGREGANDO CATEGORÍAS" -ForegroundColor Cyan

$categorias = @(
    @{ nombre = "Electrónicos" },
    @{ nombre = "Ropa" },
    @{ nombre = "Hogar y Jardín" },
    @{ nombre = "Deportes" },
    @{ nombre = "Libros" },
    @{ nombre = "Juguetes" },
    @{ nombre = "Salud y Belleza" },
    @{ nombre = "Automóviles" }
)

$categoriasCreadas = @()
foreach ($categoria in $categorias) {
    try {
        $json = $categoria | ConvertTo-Json
        $resultado = Invoke-RestMethod -Uri "$baseUrl/categorias" -Method POST -Body $json -ContentType "application/json"
        $categoriasCreadas += $resultado
        Write-Host "✅ Categoría creada: $($resultado.nombre) (ID: $($resultado.id))" -ForegroundColor Green
    } catch {
        Write-Host "❌ Error creando categoría $($categoria.nombre): $($_.Exception.Message)" -ForegroundColor Red
    }
}

# 2. AGREGAR USUARIOS
Write-Host "`n👤 2. AGREGANDO USUARIOS" -ForegroundColor Cyan

$usuarios = @(
    @{ nombre = "Juan"; apellido = "Pérez"; email = "juan.perez@email.com" },
    @{ nombre = "María"; apellido = "González"; email = "maria.gonzalez@email.com" },
    @{ nombre = "Carlos"; apellido = "Rodríguez"; email = "carlos.rodriguez@email.com" },
    @{ nombre = "Ana"; apellido = "Martínez"; email = "ana.martinez@email.com" },
    @{ nombre = "Luis"; apellido = "López"; email = "luis.lopez@email.com" },
    @{ nombre = "Carmen"; apellido = "Sánchez"; email = "carmen.sanchez@email.com" },
    @{ nombre = "Roberto"; apellido = "Fernández"; email = "roberto.fernandez@email.com" },
    @{ nombre = "Isabel"; apellido = "García"; email = "isabel.garcia@email.com" }
)

$usuariosCreados = @()
foreach ($usuario in $usuarios) {
    try {
        $json = $usuario | ConvertTo-Json
        $resultado = Invoke-RestMethod -Uri "$baseUrl/usuarios" -Method POST -Body $json -ContentType "application/json"
        $usuariosCreados += $resultado
        Write-Host "✅ Usuario creado: $($resultado.nombre) $($resultado.apellido) (ID: $($resultado.id))" -ForegroundColor Green
    } catch {
        Write-Host "❌ Error creando usuario $($usuario.nombre): $($_.Exception.Message)" -ForegroundColor Red
    }
}

# 3. AGREGAR PRODUCTOS
Write-Host "`n📦 3. AGREGANDO PRODUCTOS" -ForegroundColor Cyan

$productos = @(
    # Electrónicos
    @{ nombre = "iPhone 15 Pro"; descripcion = "Smartphone Apple iPhone 15 Pro 128GB"; precio = 1299.99; stock = 25 },
    @{ nombre = "Samsung Galaxy S24"; descripcion = "Smartphone Samsung Galaxy S24 256GB"; precio = 999.99; stock = 30 },
    @{ nombre = "MacBook Air M2"; descripcion = "Laptop Apple MacBook Air 13 pulgadas M2"; precio = 1499.99; stock = 15 },
    @{ nombre = "Dell XPS 13"; descripcion = "Laptop Dell XPS 13 Intel Core i7"; precio = 1299.99; stock = 20 },
    @{ nombre = "iPad Pro"; descripcion = "Tablet Apple iPad Pro 12.9 pulgadas"; precio = 1099.99; stock = 18 },
    @{ nombre = "AirPods Pro"; descripcion = "Audífonos inalámbricos Apple AirPods Pro"; precio = 249.99; stock = 50 },
    @{ nombre = "Sony WH-1000XM5"; descripcion = "Audífonos Sony con cancelación de ruido"; precio = 399.99; stock = 35 },
    @{ nombre = "Nintendo Switch"; descripcion = "Consola de videojuegos Nintendo Switch"; precio = 299.99; stock = 40 },
    
    # Ropa
    @{ nombre = "Camiseta Nike"; descripcion = "Camiseta deportiva Nike Dri-FIT"; precio = 29.99; stock = 100 },
    @{ nombre = "Jeans Levi's 501"; descripcion = "Jeans clásicos Levi's 501 Original"; precio = 89.99; stock = 75 },
    @{ nombre = "Sudadera Adidas"; descripcion = "Sudadera con capucha Adidas Originals"; precio = 69.99; stock = 60 },
    @{ nombre = "Zapatillas Air Max"; descripcion = "Zapatillas Nike Air Max 90"; precio = 119.99; stock = 45 },
    @{ nombre = "Chaqueta North Face"; descripcion = "Chaqueta impermeable The North Face"; precio = 199.99; stock = 25 },
    
    # Hogar y Jardín
    @{ nombre = "Aspiradora Dyson"; descripcion = "Aspiradora sin cable Dyson V15"; precio = 599.99; stock = 20 },
    @{ nombre = "Cafetera Nespresso"; descripcion = "Cafetera Nespresso Vertuo Next"; precio = 199.99; stock = 30 },
    @{ nombre = "Robot Aspirador"; descripcion = "Robot aspirador Roomba i7+"; precio = 799.99; stock = 15 },
    @{ nombre = "Set de Cuchillos"; descripcion = "Set de cuchillos profesionales de cocina"; precio = 149.99; stock = 40 },
    
    # Deportes
    @{ nombre = "Bicicleta Mountain"; descripcion = "Bicicleta de montaña Trek X-Caliber"; precio = 899.99; stock = 12 },
    @{ nombre = "Pelota de Fútbol"; descripcion = "Pelota de fútbol oficial FIFA"; precio = 39.99; stock = 80 },
    @{ nombre = "Raqueta de Tenis"; descripcion = "Raqueta de tenis Wilson Pro Staff"; precio = 249.99; stock = 25 },
    @{ nombre = "Mancuernas"; descripcion = "Set de mancuernas ajustables 20kg"; precio = 199.99; stock = 30 }
)

$productosCreados = @()
foreach ($producto in $productos) {
    try {
        $json = $producto | ConvertTo-Json
        $resultado = Invoke-RestMethod -Uri "$baseUrl/productos" -Method POST -Body $json -ContentType "application/json"
        $productosCreados += $resultado
        Write-Host "✅ Producto creado: $($resultado.nombre) - $($resultado.precio) (ID: $($resultado.id))" -ForegroundColor Green
    } catch {
        Write-Host "❌ Error creando producto $($producto.nombre): $($_.Exception.Message)" -ForegroundColor Red
    }
}

# 4. AGREGAR DIRECCIONES
Write-Host "`n🏠 4. AGREGANDO DIRECCIONES" -ForegroundColor Cyan

$direcciones = @(
    @{ calle = "Av. Corrientes"; numero = "1234"; localidad = "Buenos Aires"; provincia = "CABA"; pais = "Argentina" },
    @{ calle = "Calle Florida"; numero = "567"; localidad = "Buenos Aires"; provincia = "CABA"; pais = "Argentina" },
    @{ calle = "Av. Santa Fe"; numero = "2890"; localidad = "Buenos Aires"; provincia = "CABA"; pais = "Argentina" },
    @{ calle = "Calle Defensa"; numero = "445"; localidad = "San Telmo"; provincia = "CABA"; pais = "Argentina" },
    @{ calle = "Av. Cabildo"; numero = "3456"; localidad = "Belgrano"; provincia = "CABA"; pais = "Argentina" },
    @{ calle = "Calle Lavalle"; numero = "789"; localidad = "Microcentro"; provincia = "CABA"; pais = "Argentina" },
    @{ calle = "Av. Rivadavia"; numero = "5678"; localidad = "Caballito"; provincia = "CABA"; pais = "Argentina" },
    @{ calle = "Calle Maipú"; numero = "321"; localidad = "Vicente López"; provincia = "Buenos Aires"; pais = "Argentina" }
)

$direccionesCreadas = @()
foreach ($direccion in $direcciones) {
    try {
        $json = $direccion | ConvertTo-Json
        $resultado = Invoke-RestMethod -Uri "$baseUrl/direcciones" -Method POST -Body $json -ContentType "application/json"
        $direccionesCreadas += $resultado
        Write-Host "✅ Dirección creada: $($resultado.calle) $($resultado.numero), $($resultado.localidad) (ID: $($resultado.id))" -ForegroundColor Green
    } catch {
        Write-Host "❌ Error creando dirección: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# 5. AGREGAR PEDIDOS
Write-Host "`n🛒 5. AGREGANDO PEDIDOS" -ForegroundColor Cyan

$estados = @("PENDIENTE", "PROCESANDO", "ENVIADO", "ENTREGADO", "CANCELADO")

if ($usuariosCreados.Count -gt 0) {
    for ($i = 1; $i -le 15; $i++) {
        try {
            $usuarioAleatorio = $usuariosCreados | Get-Random
            $estadoAleatorio = $estados | Get-Random
            
            $pedido = @{
                estado = $estadoAleatorio
                usuarioId = $usuarioAleatorio.id
            }
            
            $json = $pedido | ConvertTo-Json
            $resultado = Invoke-RestMethod -Uri "$baseUrl/pedidos" -Method POST -Body $json -ContentType "application/json"
            Write-Host "✅ Pedido creado: ID $($resultado.id) - Estado: $($resultado.estado) - Usuario: $($resultado.usuarioNombre)" -ForegroundColor Green
        } catch {
            Write-Host "❌ Error creando pedido $i : $($_.Exception.Message)" -ForegroundColor Red
        }
    }
} else {
    Write-Host "⚠️ No se pueden crear pedidos sin usuarios" -ForegroundColor Orange
}

# 6. MOSTRAR RESUMEN FINAL
Write-Host "`n📊 RESUMEN DE DATOS AGREGADOS:" -ForegroundColor Green
Write-Host "=" * 40 -ForegroundColor Green

try {
    $totalCategorias = (Invoke-RestMethod -Uri "$baseUrl/categorias" -Method GET).Count
    $totalUsuarios = (Invoke-RestMethod -Uri "$baseUrl/usuarios" -Method GET).Count
    $totalProductos = (Invoke-RestMethod -Uri "$baseUrl/productos" -Method GET).Count
    $totalDirecciones = (Invoke-RestMethod -Uri "$baseUrl/direcciones" -Method GET).Count
    $totalPedidos = (Invoke-RestMethod -Uri "$baseUrl/pedidos" -Method GET).Count
    
    Write-Host "📂 Categorías: $totalCategorias" -ForegroundColor Cyan
    Write-Host "👤 Usuarios: $totalUsuarios" -ForegroundColor Cyan
    Write-Host "📦 Productos: $totalProductos" -ForegroundColor Cyan
    Write-Host "🏠 Direcciones: $totalDirecciones" -ForegroundColor Cyan
    Write-Host "🛒 Pedidos: $totalPedidos" -ForegroundColor Cyan
} catch {
    Write-Host "❌ Error obteniendo resumen: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n🎉 ¡DATOS DE EJEMPLO AGREGADOS EXITOSAMENTE!" -ForegroundColor Green
Write-Host "🌐 Puedes ver los datos en: http://localhost:8080/api/{entidad}" -ForegroundColor Yellow
Write-Host "📋 Ejemplo: http://localhost:8080/api/productos" -ForegroundColor Yellow

Write-Host "`n🔍 COMANDOS PARA VERIFICAR LOS DATOS:" -ForegroundColor Cyan
Write-Host "Invoke-RestMethod -Uri 'http://localhost:8080/api/categorias' -Method GET" -ForegroundColor White
Write-Host "Invoke-RestMethod -Uri 'http://localhost:8080/api/usuarios' -Method GET" -ForegroundColor White
Write-Host "Invoke-RestMethod -Uri 'http://localhost:8080/api/productos' -Method GET" -ForegroundColor White
Write-Host "Invoke-RestMethod -Uri 'http://localhost:8080/api/direcciones' -Method GET" -ForegroundColor White
Write-Host "Invoke-RestMethod -Uri 'http://localhost:8080/api/pedidos' -Method GET" -ForegroundColor White