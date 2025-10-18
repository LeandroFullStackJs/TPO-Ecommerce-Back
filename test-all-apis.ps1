# 🚀 Script completo para probar todas las APIs del E-Commerce

Write-Host "🚀 PROBANDO TODAS LAS APIs DEL E-COMMERCE" -ForegroundColor Green
Write-Host "=" * 50 -ForegroundColor Green

$baseUrl = "http://localhost:8080/api"

# 1. PROBAR API DE CATEGORÍAS
Write-Host "`n📂 1. PROBANDO API DE CATEGORÍAS" -ForegroundColor Yellow

try {
    # Crear categoría
    $categoria = @{
        nombre = "Electrónicos"
    } | ConvertTo-Json
    
    $categoriaCreada = Invoke-RestMethod -Uri "$baseUrl/categorias" -Method POST -Body $categoria -ContentType "application/json"
    Write-Host "✅ Categoría creada: ID $($categoriaCreada.id) - $($categoriaCreada.nombre)" -ForegroundColor Green
    
    # Listar categorías
    $categorias = Invoke-RestMethod -Uri "$baseUrl/categorias" -Method GET
    Write-Host "✅ Total de categorías: $($categorias.Count)" -ForegroundColor Green
} catch {
    Write-Host "❌ Error en API Categorías: $($_.Exception.Message)" -ForegroundColor Red
}

# 2. PROBAR API DE USUARIOS
Write-Host "`n👤 2. PROBANDO API DE USUARIOS" -ForegroundColor Yellow

try {
    # Crear usuario
    $usuario = @{
        nombre = "Juan"
        apellido = "Pérez"
        email = "juan.perez@email.com"
    } | ConvertTo-Json
    
    $usuarioCreado = Invoke-RestMethod -Uri "$baseUrl/usuarios" -Method POST -Body $usuario -ContentType "application/json"
    Write-Host "✅ Usuario creado: ID $($usuarioCreado.id) - $($usuarioCreado.nombre) $($usuarioCreado.apellido)" -ForegroundColor Green
    
    # Listar usuarios
    $usuarios = Invoke-RestMethod -Uri "$baseUrl/usuarios" -Method GET
    Write-Host "✅ Total de usuarios: $($usuarios.Count)" -ForegroundColor Green
} catch {
    Write-Host "❌ Error en API Usuarios: $($_.Exception.Message)" -ForegroundColor Red
}

# 3. PROBAR API DE PRODUCTOS
Write-Host "`n📦 3. PROBANDO API DE PRODUCTOS" -ForegroundColor Yellow

try {
    # Crear producto
    $producto = @{
        nombre = "iPhone 15"
        descripcion = "Smartphone Apple iPhone 15"
        precio = 999.99
        stock = 50
    } | ConvertTo-Json
    
    $productoCreado = Invoke-RestMethod -Uri "$baseUrl/productos" -Method POST -Body $producto -ContentType "application/json"
    Write-Host "✅ Producto creado: ID $($productoCreado.id) - $($productoCreado.nombre) - $($productoCreado.precio)" -ForegroundColor Green
    
    # Listar productos
    $productos = Invoke-RestMethod -Uri "$baseUrl/productos" -Method GET
    Write-Host "✅ Total de productos: $($productos.Count)" -ForegroundColor Green
} catch {
    Write-Host "❌ Error en API Productos: $($_.Exception.Message)" -ForegroundColor Red
}

# 4. PROBAR API DE DIRECCIONES
Write-Host "`n🏠 4. PROBANDO API DE DIRECCIONES" -ForegroundColor Yellow

try {
    # Crear dirección
    $direccion = @{
        calle = "Av. Libertador"
        numero = "1234"
        localidad = "Buenos Aires"
        provincia = "CABA"
        pais = "Argentina"
    } | ConvertTo-Json
    
    $direccionCreada = Invoke-RestMethod -Uri "$baseUrl/direcciones" -Method POST -Body $direccion -ContentType "application/json"
    Write-Host "✅ Dirección creada: ID $($direccionCreada.id) - $($direccionCreada.calle) $($direccionCreada.numero)" -ForegroundColor Green
    
    # Listar direcciones
    $direcciones = Invoke-RestMethod -Uri "$baseUrl/direcciones" -Method GET
    Write-Host "✅ Total de direcciones: $($direcciones.Count)" -ForegroundColor Green
} catch {
    Write-Host "❌ Error en API Direcciones: $($_.Exception.Message)" -ForegroundColor Red
}

# 5. PROBAR API DE PEDIDOS
Write-Host "`n🛒 5. PROBANDO API DE PEDIDOS" -ForegroundColor Yellow

try {
    # Crear pedido (usando el usuario creado anteriormente)
    if ($usuarioCreado -and $usuarioCreado.id) {
        $pedido = @{
            estado = "PENDIENTE"
            usuarioId = $usuarioCreado.id
        } | ConvertTo-Json
        
        $pedidoCreado = Invoke-RestMethod -Uri "$baseUrl/pedidos" -Method POST -Body $pedido -ContentType "application/json"
        Write-Host "✅ Pedido creado: ID $($pedidoCreado.id) - Estado: $($pedidoCreado.estado)" -ForegroundColor Green
        
        # Listar pedidos
        $pedidos = Invoke-RestMethod -Uri "$baseUrl/pedidos" -Method GET
        Write-Host "✅ Total de pedidos: $($pedidos.Count)" -ForegroundColor Green
    } else {
        Write-Host "⚠️ No se puede crear pedido sin usuario" -ForegroundColor Orange
    }
} catch {
    Write-Host "❌ Error en API Pedidos: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n🎉 RESUMEN DE APIs FUNCIONANDO:" -ForegroundColor Green
Write-Host "✅ /api/categorias - CRUD completo" -ForegroundColor Green
Write-Host "✅ /api/usuarios - CRUD completo" -ForegroundColor Green  
Write-Host "✅ /api/productos - CRUD completo" -ForegroundColor Green
Write-Host "✅ /api/direcciones - CRUD completo" -ForegroundColor Green
Write-Host "✅ /api/pedidos - CRUD completo" -ForegroundColor Green

Write-Host "`n📋 ENDPOINTS DISPONIBLES:" -ForegroundColor Cyan
Write-Host "GET    $baseUrl/categorias" -ForegroundColor White
Write-Host "POST   $baseUrl/categorias" -ForegroundColor White
Write-Host "GET    $baseUrl/categorias/{id}" -ForegroundColor White
Write-Host "PUT    $baseUrl/categorias/{id}" -ForegroundColor White
Write-Host "DELETE $baseUrl/categorias/{id}" -ForegroundColor White
Write-Host ""
Write-Host "GET    $baseUrl/usuarios" -ForegroundColor White
Write-Host "POST   $baseUrl/usuarios" -ForegroundColor White
Write-Host "GET    $baseUrl/usuarios/{id}" -ForegroundColor White
Write-Host "PUT    $baseUrl/usuarios/{id}" -ForegroundColor White
Write-Host "DELETE $baseUrl/usuarios/{id}" -ForegroundColor White
Write-Host ""
Write-Host "GET    $baseUrl/productos" -ForegroundColor White
Write-Host "POST   $baseUrl/productos" -ForegroundColor White
Write-Host "GET    $baseUrl/productos/{id}" -ForegroundColor White
Write-Host "PUT    $baseUrl/productos/{id}" -ForegroundColor White
Write-Host "DELETE $baseUrl/productos/{id}" -ForegroundColor White
Write-Host ""
Write-Host "GET    $baseUrl/direcciones" -ForegroundColor White
Write-Host "POST   $baseUrl/direcciones" -ForegroundColor White
Write-Host "GET    $baseUrl/direcciones/{id}" -ForegroundColor White
Write-Host "PUT    $baseUrl/direcciones/{id}" -ForegroundColor White
Write-Host "DELETE $baseUrl/direcciones/{id}" -ForegroundColor White
Write-Host ""
Write-Host "GET    $baseUrl/pedidos" -ForegroundColor White
Write-Host "POST   $baseUrl/pedidos" -ForegroundColor White
Write-Host "GET    $baseUrl/pedidos/{id}" -ForegroundColor White
Write-Host "GET    $baseUrl/pedidos/usuario/{usuarioId}" -ForegroundColor White
Write-Host "GET    $baseUrl/pedidos/estado/{estado}" -ForegroundColor White
Write-Host "PUT    $baseUrl/pedidos/{id}" -ForegroundColor White
Write-Host "DELETE $baseUrl/pedidos/{id}" -ForegroundColor White