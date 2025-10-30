# Script para probar la API del e-commerce
Write-Host "=== PROBANDO API E-COMMERCE ===" -ForegroundColor Green

# Probar productos
Write-Host "`n1. Probando GET /api/productos..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/productos" -Method Get -ContentType "application/json"
    Write-Host "✅ Productos encontrados: $($response.Count)" -ForegroundColor Green
    
    if ($response.Count -gt 0) {
        Write-Host "`nPrimer producto:" -ForegroundColor Cyan
        $response[0] | ConvertTo-Json -Depth 2
        
        $productId = $response[0].id
        Write-Host "`n2. Probando GET producto específico (ID: $productId)..." -ForegroundColor Yellow
        $producto = Invoke-RestMethod -Uri "http://localhost:8080/api/productos/$productId" -Method Get -ContentType "application/json"
        Write-Host "✅ Producto obtenido: $($producto.nombreObra)" -ForegroundColor Green
        Write-Host "   Stock actual: $($producto.stock)" -ForegroundColor Cyan
    }
} catch {
    Write-Host "❌ Error al obtener productos: $($_.Exception.Message)" -ForegroundColor Red
}

# Probar categorías
Write-Host "`n3. Probando GET /api/categorias..." -ForegroundColor Yellow
try {
    $categorias = Invoke-RestMethod -Uri "http://localhost:8080/api/categorias" -Method Get -ContentType "application/json"
    Write-Host "✅ Categorías encontradas: $($categorias.Count)" -ForegroundColor Green
} catch {
    Write-Host "❌ Error al obtener categorías: $($_.Exception.Message)" -ForegroundColor Red
}

# Probar artistas
Write-Host "`n4. Probando GET /api/artistas..." -ForegroundColor Yellow
try {
    $artistas = Invoke-RestMethod -Uri "http://localhost:8080/api/artistas" -Method Get -ContentType "application/json"
    Write-Host "✅ Artistas encontrados: $($artistas.Count)" -ForegroundColor Green
} catch {
    Write-Host "❌ Error al obtener artistas: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n=== FIN DE PRUEBAS ===" -ForegroundColor Green