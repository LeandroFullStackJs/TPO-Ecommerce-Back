# Script para probar endpoints de la API
Write-Host "🚀 Probando API E-Commerce con MySQL..." -ForegroundColor Green

# Esperar a que la aplicación esté lista
Start-Sleep 5

try {
    Write-Host "📋 1. Probando GET /api/productos..." -ForegroundColor Yellow
    $productos = Invoke-RestMethod -Uri "http://localhost:8080/api/productos" -Method GET
    Write-Host "✅ Productos encontrados: $($productos.Count)" -ForegroundColor Green
    
    Write-Host "📋 2. Probando GET /api/usuarios..." -ForegroundColor Yellow
    $usuarios = Invoke-RestMethod -Uri "http://localhost:8080/api/usuarios" -Method GET
    Write-Host "✅ Usuarios encontrados: $($usuarios.Count)" -ForegroundColor Green
    
    # Crear un producto de ejemplo
    Write-Host "📝 3. Creando producto de ejemplo..." -ForegroundColor Yellow
    $nuevoProducto = @{
        nombre = "Laptop Gaming"
        descripcion = "Laptop para gaming de alta gama"
        precio = 1200.50
        stock = 10
    } | ConvertTo-Json
    
    $resultado = Invoke-RestMethod -Uri "http://localhost:8080/api/productos" -Method POST -Body $nuevoProducto -ContentType "application/json"
    Write-Host "✅ Producto creado con ID: $($resultado.id)" -ForegroundColor Green
    
    # Verificar que se creó
    Write-Host "📋 4. Verificando productos después de crear..." -ForegroundColor Yellow
    $productosNuevos = Invoke-RestMethod -Uri "http://localhost:8080/api/productos" -Method GET
    Write-Host "✅ Total de productos ahora: $($productosNuevos.Count)" -ForegroundColor Green
    
    Write-Host "🎉 ¡Todas las pruebas exitosas! La API funciona correctamente con MySQL." -ForegroundColor Green
    
} catch {
    Write-Host "❌ Error al probar los endpoints: $($_.Exception.Message)" -ForegroundColor Red
}