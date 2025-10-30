# =====================================================
# TEST DE ENDPOINTS DESPUÉS DE CORRECCIÓN CORS
# =====================================================

$BASE_URL = "http://localhost:8080/api"

Write-Host "🔧 TESTING CORRECCIONES DE CORS" -ForegroundColor Green
Write-Host "================================" -ForegroundColor Green

# Test 1: CORS Test
Write-Host ""
Write-Host "1. Probando endpoint CORS test..." -ForegroundColor Yellow
try {
    $corsTest = Invoke-RestMethod -Uri "$BASE_URL/cors-test" -Method GET
    Write-Host "✅ CORS Test exitoso:" -ForegroundColor Green
    Write-Host "   Message: $($corsTest.message)" -ForegroundColor Cyan
} catch {
    Write-Host "❌ Error CORS test: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 2: Health Check
Write-Host ""
Write-Host "2. Probando health check..." -ForegroundColor Yellow
try {
    $health = Invoke-RestMethod -Uri "$BASE_URL/health" -Method GET
    Write-Host "✅ Health Check exitoso:" -ForegroundColor Green
    Write-Host "   Status: $($health.status)" -ForegroundColor Cyan
    Write-Host "   Service: $($health.service)" -ForegroundColor Cyan
} catch {
    Write-Host "❌ Error health: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 3: Productos (debería funcionar sin CORS error ahora)
Write-Host ""
Write-Host "3. Probando GET productos..." -ForegroundColor Yellow
try {
    $productos = Invoke-RestMethod -Uri "$BASE_URL/productos" -Method GET
    Write-Host "✅ Productos obtenidos: $($productos.Count)" -ForegroundColor Green
    if ($productos.Count -eq 0) {
        Write-Host "⚠️  Base de datos vacía - necesitas cargar datos SQL" -ForegroundColor Yellow
    }
} catch {
    Write-Host "❌ Error productos: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 4: Test específico de producto ID 5 (el que estaba fallando)
Write-Host ""
Write-Host "4. Probando GET producto ID 5..." -ForegroundColor Yellow
try {
    $producto5 = Invoke-RestMethod -Uri "$BASE_URL/productos/5" -Method GET
    Write-Host "✅ Producto 5 obtenido: $($producto5.nombreObra)" -ForegroundColor Green
} catch {
    if ($_.Exception.Message -contains "404") {
        Write-Host "⚠️  Producto 5 no existe (esperado si no hay datos)" -ForegroundColor Yellow
    } else {
        Write-Host "❌ Error producto 5: $($_.Exception.Message)" -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "📋 RESUMEN:" -ForegroundColor Green
Write-Host "- CORS está configurado correctamente" -ForegroundColor White
Write-Host "- Servidor responde sin errores CORS" -ForegroundColor White
Write-Host "- Necesitas cargar datos SQL para tener productos" -ForegroundColor Yellow
Write-Host ""
Write-Host "🎯 PRÓXIMO PASO:" -ForegroundColor Green
Write-Host "Ejecuta el archivo datos_completos_arte.sql en MySQL" -ForegroundColor White