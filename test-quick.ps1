# Test rápido de funcionalidad básica
$BASE_URL = "http://localhost:8080/api"

Write-Host "=== TEST RÁPIDO TPO E-COMMERCE ===" -ForegroundColor Green

# Test 1: Verificar productos
Write-Host "1. Probando GET productos..." -ForegroundColor Yellow
try {
    $productos = Invoke-RestMethod -Uri "$BASE_URL/productos" -Method GET
    Write-Host "✅ Productos: $($productos.Count) encontrados" -ForegroundColor Green
    if ($productos.Count -gt 0) {
        $primer = $productos[0]
        Write-Host "   - $($primer.nombreObra) - $($primer.precio)" -ForegroundColor Cyan
    }
} catch {
    Write-Host "❌ Error productos: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 2: Verificar categorías
Write-Host ""
Write-Host "2. Probando GET categorías..." -ForegroundColor Yellow
try {
    $categorias = Invoke-RestMethod -Uri "$BASE_URL/categorias" -Method GET
    Write-Host "✅ Categorías: $($categorias.Count) encontradas" -ForegroundColor Green
    if ($categorias.Count -gt 0) {
        Write-Host "   - $($categorias[0].nombre)" -ForegroundColor Cyan
    }
} catch {
    Write-Host "❌ Error categorías: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 3: Verificar artistas
Write-Host ""
Write-Host "3. Probando GET artistas..." -ForegroundColor Yellow
try {
    $artistas = Invoke-RestMethod -Uri "$BASE_URL/artistas" -Method GET
    Write-Host "✅ Artistas: $($artistas.Count) encontrados" -ForegroundColor Green
    if ($artistas.Count -gt 0) {
        Write-Host "   - $($artistas[0].nombre)" -ForegroundColor Cyan
    }
} catch {
    Write-Host "❌ Error artistas: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 4: Debug usuarios
Write-Host ""
Write-Host "4. Verificando usuarios..." -ForegroundColor Yellow
try {
    $emails = Invoke-RestMethod -Uri "$BASE_URL/usuarios/debug/emails" -Method GET
    Write-Host "✅ Emails en BD:" -ForegroundColor Green
    $emails | ForEach-Object { Write-Host "   - $_" -ForegroundColor Cyan }
} catch {
    Write-Host "❌ Error emails: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 5: Probar login directo
Write-Host ""
Write-Host "5. Probando login..." -ForegroundColor Yellow
$loginData = '{"email":"admin@arte.com","password":"password"}'
try {
    $result = Invoke-RestMethod -Uri "$BASE_URL/auth/login" -Method POST -Body $loginData -ContentType "application/json"
    Write-Host "✅ LOGIN EXITOSO!" -ForegroundColor Green
    Write-Host "   Token: $($result.token.Substring(0,20))..." -ForegroundColor Cyan
} catch {
    Write-Host "❌ Login falló: $($_.Exception.Message)" -ForegroundColor Red
    if ($_.Exception.Response) {
        $errorStream = $_.Exception.Response.GetResponseStream()
        $reader = [System.IO.StreamReader]::new($errorStream)
        $errorBody = $reader.ReadToEnd()
        Write-Host "   Detalles: $errorBody" -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "=== FIN TEST RÁPIDO ===" -ForegroundColor Green