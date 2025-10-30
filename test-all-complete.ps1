# Script completo de pruebas para TPO E-commerce Back
# =====================================================

$BASE_URL = "http://localhost:8080/api"

Write-Host "=== PRUEBAS COMPLETAS TPO E-COMMERCE BACKEND ===" -ForegroundColor Green
Write-Host ""

# Test 1: Verificar que el servidor responde
Write-Host "1. VERIFICANDO SERVIDOR..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$BASE_URL/productos" -Method GET -TimeoutSec 5
    Write-Host "✅ Servidor respondiendo correctamente" -ForegroundColor Green
} catch {
    Write-Host "❌ Error: Servidor no responde - $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# Test 2: Debug - Verificar usuarios en base de datos
Write-Host ""
Write-Host "2. VERIFICANDO USUARIOS EN BD..." -ForegroundColor Yellow
try {
    $emails = Invoke-RestMethod -Uri "$BASE_URL/usuarios/debug/emails" -Method GET
    Write-Host "✅ Usuarios encontrados:" -ForegroundColor Green
    $emails | ForEach-Object { Write-Host "   - $_" -ForegroundColor Cyan }
} catch {
    Write-Host "❌ Error obteniendo emails: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 3: Probar login con diferentes credenciales
Write-Host ""
Write-Host "3. PROBANDO AUTENTICACIÓN..." -ForegroundColor Yellow

$credenciales = @(
    @{ email = "admin@arte.com"; password = "password" },
    @{ email = "galeria@arte.com"; password = "password" },
    @{ email = "admin@arte.com"; password = "123456" },
    @{ email = "galeria@arte.com"; password = "123456" }
)

$token = $null
foreach ($cred in $credenciales) {
    try {
        $loginData = @{
            email = $cred.email
            password = $cred.password
        } | ConvertTo-Json
        
        Write-Host "Probando: $($cred.email) / $($cred.password)" -ForegroundColor White
        
        $loginResponse = Invoke-RestMethod -Uri "$BASE_URL/auth/login" -Method POST -Body $loginData -ContentType "application/json"
        
        if ($loginResponse.token) {
            Write-Host "✅ LOGIN EXITOSO con $($cred.email)" -ForegroundColor Green
            Write-Host "   Token: $($loginResponse.token.Substring(0,20))..." -ForegroundColor Cyan
            $token = $loginResponse.token
            break
        }
    } catch {
        Write-Host "❌ Falló $($cred.email): $($_.Exception.Message)" -ForegroundColor Red
    }
}

# Test 4: Probar endpoints públicos
Write-Host ""
Write-Host "4. PROBANDO ENDPOINTS PÚBLICOS..." -ForegroundColor Yellow

# Productos
try {
    $productos = Invoke-RestMethod -Uri "$BASE_URL/productos" -Method GET
    Write-Host "✅ Productos obtenidos: $($productos.Count) items" -ForegroundColor Green
    if ($productos.Count -gt 0) {
        Write-Host "   Primer producto: $($productos[0].nombreObra)" -ForegroundColor Cyan
    }
} catch {
    Write-Host "❌ Error obteniendo productos: $($_.Exception.Message)" -ForegroundColor Red
}

# Categorías
try {
    $categorias = Invoke-RestMethod -Uri "$BASE_URL/categorias" -Method GET
    Write-Host "✅ Categorías obtenidas: $($categorias.Count) items" -ForegroundColor Green
    if ($categorias.Count -gt 0) {
        Write-Host "   Primera categoría: $($categorias[0].nombre)" -ForegroundColor Cyan
    }
} catch {
    Write-Host "❌ Error obteniendo categorías: $($_.Exception.Message)" -ForegroundColor Red
}

# Artistas
try {
    $artistas = Invoke-RestMethod -Uri "$BASE_URL/artistas" -Method GET
    Write-Host "✅ Artistas obtenidos: $($artistas.Count) items" -ForegroundColor Green
    if ($artistas.Count -gt 0) {
        Write-Host "   Primer artista: $($artistas[0].nombre)" -ForegroundColor Cyan
    }
} catch {
    Write-Host "❌ Error obteniendo artistas: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 5: Si tenemos token, probar endpoints protegidos
if ($token) {
    Write-Host ""
    Write-Host "5. PROBANDO ENDPOINTS PROTEGIDOS..." -ForegroundColor Yellow
    
    $headers = @{
        "Authorization" = "Bearer $token"
        "Content-Type" = "application/json"
    }
    
    try {
        $usuarios = Invoke-RestMethod -Uri "$BASE_URL/usuarios" -Method GET -Headers $headers
        Write-Host "✅ Usuarios obtenidos (protegido): $($usuarios.Count) items" -ForegroundColor Green
    } catch {
        Write-Host "❌ Error obteniendo usuarios protegidos: $($_.Exception.Message)" -ForegroundColor Red
    }
} else {
    Write-Host ""
    Write-Host "5. SALTANDO ENDPOINTS PROTEGIDOS (sin token)" -ForegroundColor Yellow
}

# Test 6: Probar creación de producto (si tenemos token)
if ($token) {
    Write-Host ""
    Write-Host "6. PROBANDO CREACIÓN DE PRODUCTO..." -ForegroundColor Yellow
    
    $nuevoProducto = @{
        nombreObra = "Obra de Prueba"
        descripcion = "Descripción de prueba"
        precio = 1500.0
        stock = 5
        tecnica = "Óleo sobre lienzo"
        dimensiones = "40x30 cm"
        estilo = "Contemporáneo"
        anio = 2024
        imagen = "https://images.unsplash.com/photo-1578321272176-b7bbc0679853"
        artistaId = 1
        categoriaIds = @(1, 2)
        destacado = $true
        activo = $true
    } | ConvertTo-Json
    
    $headers = @{
        "Authorization" = "Bearer $token"
        "Content-Type" = "application/json"
    }
    
    try {
        $productoCreado = Invoke-RestMethod -Uri "$BASE_URL/productos" -Method POST -Body $nuevoProducto -Headers $headers
        Write-Host "✅ Producto creado exitosamente: ID $($productoCreado.id)" -ForegroundColor Green
    } catch {
        Write-Host "❌ Error creando producto: $($_.Exception.Message)" -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "=== RESUMEN DE PRUEBAS COMPLETADO ===" -ForegroundColor Green
Write-Host "Revisa los resultados arriba para ver qué funciona y qué necesita ajustes." -ForegroundColor White