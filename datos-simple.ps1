# Script para agregar datos de ejemplo al e-commerce
Write-Host "🚀 AGREGANDO DATOS DE EJEMPLO AL E-COMMERCE" -ForegroundColor Green

$baseUrl = "http://localhost:8080/api"

# Esperar a que la aplicación esté lista
Write-Host "⏳ Esperando que la aplicación esté lista..." -ForegroundColor Yellow
Start-Sleep 5

# 1. AGREGAR CATEGORÍAS
Write-Host "`n📂 1. AGREGANDO CATEGORÍAS" -ForegroundColor Cyan

$categorias = @(
    @{ nombre = "Electrónicos" },
    @{ nombre = "Ropa" },
    @{ nombre = "Hogar y Jardín" },
    @{ nombre = "Deportes" },
    @{ nombre = "Libros" }
)

foreach ($categoria in $categorias) {
    try {
        $json = $categoria | ConvertTo-Json
        $resultado = Invoke-RestMethod -Uri "$baseUrl/categorias" -Method POST -Body $json -ContentType "application/json"
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
    @{ nombre = "Ana"; apellido = "Martínez"; email = "ana.martinez@email.com" }
)

foreach ($usuario in $usuarios) {
    try {
        $json = $usuario | ConvertTo-Json
        $resultado = Invoke-RestMethod -Uri "$baseUrl/usuarios" -Method POST -Body $json -ContentType "application/json"
        Write-Host "✅ Usuario creado: $($resultado.nombre) $($resultado.apellido) (ID: $($resultado.id))" -ForegroundColor Green
    } catch {
        Write-Host "❌ Error creando usuario $($usuario.nombre): $($_.Exception.Message)" -ForegroundColor Red
    }
}

# 3. AGREGAR PRODUCTOS
Write-Host "`n📦 3. AGREGANDO PRODUCTOS" -ForegroundColor Cyan

$productos = @(
    @{ nombre = "iPhone 15 Pro"; descripcion = "Smartphone Apple iPhone 15 Pro 128GB"; precio = 1299.99; stock = 25 },
    @{ nombre = "Samsung Galaxy S24"; descripcion = "Smartphone Samsung Galaxy S24 256GB"; precio = 999.99; stock = 30 },
    @{ nombre = "MacBook Air M2"; descripcion = "Laptop Apple MacBook Air 13 pulgadas M2"; precio = 1499.99; stock = 15 },
    @{ nombre = "Camiseta Nike"; descripcion = "Camiseta deportiva Nike Dri-FIT"; precio = 29.99; stock = 100 },
    @{ nombre = "Jeans Levi's 501"; descripcion = "Jeans clásicos Levi's 501 Original"; precio = 89.99; stock = 75 }
)

foreach ($producto in $productos) {
    try {
        $json = $producto | ConvertTo-Json
        $resultado = Invoke-RestMethod -Uri "$baseUrl/productos" -Method POST -Body $json -ContentType "application/json"
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
    @{ calle = "Av. Santa Fe"; numero = "2890"; localidad = "Buenos Aires"; provincia = "CABA"; pais = "Argentina" }
)

foreach ($direccion in $direcciones) {
    try {
        $json = $direccion | ConvertTo-Json
        $resultado = Invoke-RestMethod -Uri "$baseUrl/direcciones" -Method POST -Body $json -ContentType "application/json"
        Write-Host "✅ Dirección creada: $($resultado.calle) $($resultado.numero), $($resultado.localidad) (ID: $($resultado.id))" -ForegroundColor Green
    } catch {
        Write-Host "❌ Error creando dirección: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# 5. AGREGAR PEDIDOS
Write-Host "`n🛒 5. AGREGANDO PEDIDOS" -ForegroundColor Cyan

try {
    $usuariosObtenidos = Invoke-RestMethod -Uri "$baseUrl/usuarios" -Method GET
    
    if ($usuariosObtenidos.Count -gt 0) {
        for ($i = 1; $i -le 5; $i++) {
            $usuarioAleatorio = $usuariosObtenidos | Get-Random
            $estadoAleatorio = @("PENDIENTE", "PROCESANDO", "ENVIADO") | Get-Random
            
            $pedido = @{
                estado = $estadoAleatorio
                usuarioId = $usuarioAleatorio.id
            }
            
            $json = $pedido | ConvertTo-Json
            $resultado = Invoke-RestMethod -Uri "$baseUrl/pedidos" -Method POST -Body $json -ContentType "application/json"
            Write-Host "✅ Pedido creado: ID $($resultado.id) - Estado: $($resultado.estado)" -ForegroundColor Green
        }
    }
} catch {
    Write-Host "❌ Error creando pedidos: $($_.Exception.Message)" -ForegroundColor Red
}

# 6. MOSTRAR RESUMEN FINAL
Write-Host "`n📊 RESUMEN DE DATOS AGREGADOS:" -ForegroundColor Green

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
Write-Host "🌐 Puedes ver los datos en: http://localhost:8080/api/entidad" -ForegroundColor Yellow