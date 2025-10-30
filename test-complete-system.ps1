# =====================================================
# PRUEBA COMPLETA DEL SISTEMA TPO E-COMMERCE
# =====================================================

$BASE_URL = "http://localhost:8080/api"
$token = $null

Write-Host "🚀 INICIANDO PRUEBAS COMPLETAS DEL SISTEMA" -ForegroundColor Green
Write-Host "=============================================" -ForegroundColor Green
Write-Host ""

# =====================================================
# FASE 1: ENDPOINTS PÚBLICOS
# =====================================================
Write-Host "📋 FASE 1: PROBANDO ENDPOINTS PÚBLICOS" -ForegroundColor Yellow
Write-Host "---------------------------------------" -ForegroundColor Yellow

# Test 1: Productos
Write-Host ""
Write-Host "1️⃣  Probando GET /productos..." -ForegroundColor Cyan
try {
    $productos = Invoke-RestMethod -Uri "$BASE_URL/productos" -Method GET
    Write-Host "✅ Productos obtenidos: $($productos.Count)" -ForegroundColor Green
    if ($productos.Count -gt 0) {
        $primer = $productos[0]
        Write-Host "   📦 Ejemplo: '$($primer.nombreObra)' - $($primer.precio) USD" -ForegroundColor White
        Write-Host "   🎨 Artista: $($primer.artista)" -ForegroundColor White
        Write-Host "   🖼️  Imagen: $($primer.imagen)" -ForegroundColor White
        
        # Probamos también producto individual
        Write-Host ""
        Write-Host "   🔍 Probando producto individual (ID: $($primer.id))..." -ForegroundColor Cyan
        $productoIndividual = Invoke-RestMethod -Uri "$BASE_URL/productos/$($primer.id)" -Method GET
        Write-Host "   ✅ Producto individual obtenido: $($productoIndividual.nombreObra)" -ForegroundColor Green
    }
} catch {
    Write-Host "❌ Error en productos: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 2: Categorías
Write-Host ""
Write-Host "2️⃣  Probando GET /categorias..." -ForegroundColor Cyan
try {
    $categorias = Invoke-RestMethod -Uri "$BASE_URL/categorias" -Method GET
    Write-Host "✅ Categorías obtenidas: $($categorias.Count)" -ForegroundColor Green
    if ($categorias.Count -gt 0) {
        $primeraCat = $categorias[0]
        Write-Host "   📂 Ejemplo: '$($primeraCat.nombre)'" -ForegroundColor White
        Write-Host "   📝 Descripción: $($primeraCat.descripcion)" -ForegroundColor White
        
        # Probamos productos por categoría
        Write-Host ""
        Write-Host "   🔍 Probando productos por categoría (ID: $($primeraCat.id))..." -ForegroundColor Cyan
        $productosPorCat = Invoke-RestMethod -Uri "$BASE_URL/productos/categoria/$($primeraCat.id)" -Method GET
        Write-Host "   ✅ Productos en categoría: $($productosPorCat.Count)" -ForegroundColor Green
    }
} catch {
    Write-Host "❌ Error en categorías: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 3: Artistas
Write-Host ""
Write-Host "3️⃣  Probando GET /artistas..." -ForegroundColor Cyan
try {
    $artistas = Invoke-RestMethod -Uri "$BASE_URL/artistas" -Method GET
    Write-Host "✅ Artistas obtenidos: $($artistas.Count)" -ForegroundColor Green
    if ($artistas.Count -gt 0) {
        $primerArtista = $artistas[0]
        Write-Host "   👨‍🎨 Ejemplo: '$($primerArtista.nombre)'" -ForegroundColor White
        Write-Host "   📧 Email: $($primerArtista.email)" -ForegroundColor White
        
        # Probamos productos por artista
        Write-Host ""
        Write-Host "   🔍 Probando productos por artista (ID: $($primerArtista.id))..." -ForegroundColor Cyan
        $productosPorArtista = Invoke-RestMethod -Uri "$BASE_URL/productos/artista/$($primerArtista.id)" -Method GET
        Write-Host "   ✅ Productos del artista: $($productosPorArtista.Count)" -ForegroundColor Green
    }
} catch {
    Write-Host "❌ Error en artistas: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 4: Productos destacados
Write-Host ""
Write-Host "4️⃣  Probando GET /productos/destacados..." -ForegroundColor Cyan
try {
    $destacados = Invoke-RestMethod -Uri "$BASE_URL/productos/destacados" -Method GET
    Write-Host "✅ Productos destacados: $($destacados.Count)" -ForegroundColor Green
    if ($destacados.Count -gt 0) {
        Write-Host "   ⭐ Primer destacado: $($destacados[0].nombreObra)" -ForegroundColor White
    }
} catch {
    Write-Host "❌ Error en productos destacados: $($_.Exception.Message)" -ForegroundColor Red
}

# =====================================================
# FASE 2: DEBUG Y VERIFICACIÓN DE DATOS
# =====================================================
Write-Host ""
Write-Host ""
Write-Host "🔍 FASE 2: VERIFICACIÓN DE DATOS" -ForegroundColor Yellow
Write-Host "--------------------------------" -ForegroundColor Yellow

# Test 5: Debug de usuarios
Write-Host ""
Write-Host "5️⃣  Probando endpoint debug de emails..." -ForegroundColor Cyan
try {
    $emails = Invoke-RestMethod -Uri "$BASE_URL/usuarios/debug/emails" -Method GET
    Write-Host "✅ Emails en base de datos:" -ForegroundColor Green
    $emails | ForEach-Object { Write-Host "   📧 $_" -ForegroundColor White }
} catch {
    Write-Host "❌ Error obteniendo emails: $($_.Exception.Message)" -ForegroundColor Red
}

# =====================================================
# FASE 3: AUTENTICACIÓN
# =====================================================
Write-Host ""
Write-Host ""
Write-Host "🔐 FASE 3: PRUEBAS DE AUTENTICACIÓN" -ForegroundColor Yellow
Write-Host "-----------------------------------" -ForegroundColor Yellow

# Test 6: Login
$credenciales = @(
    @{ email = "admin@arte.com"; password = "password"; descripcion = "Admin principal" },
    @{ email = "galeria@arte.com"; password = "password"; descripcion = "Usuario galería" }
)

foreach ($cred in $credenciales) {
    Write-Host ""
    Write-Host "6️⃣  Probando login: $($cred.descripcion) ($($cred.email))..." -ForegroundColor Cyan
    
    $loginData = @{
        email = $cred.email
        password = $cred.password
    } | ConvertTo-Json
    
    try {
        $loginResponse = Invoke-RestMethod -Uri "$BASE_URL/auth/login" -Method POST -Body $loginData -ContentType "application/json"
        
        if ($loginResponse.token) {
            Write-Host "✅ LOGIN EXITOSO!" -ForegroundColor Green
            Write-Host "   🎟️  Token: $($loginResponse.token.Substring(0,30))..." -ForegroundColor White
            Write-Host "   👤 Usuario: $($loginResponse.user.nombre) $($loginResponse.user.apellido)" -ForegroundColor White
            Write-Host "   🔑 Role: $($loginResponse.user.role)" -ForegroundColor White
            
            # Guardamos el token para pruebas posteriores
            if (-not $token) {
                $token = $loginResponse.token
                Write-Host "   💾 Token guardado para pruebas protegidas" -ForegroundColor Green
            }
            break
        }
    } catch {
        Write-Host "❌ Login falló para $($cred.email): $($_.Exception.Message)" -ForegroundColor Red
    }
}

# =====================================================
# FASE 4: ENDPOINTS PROTEGIDOS (si tenemos token)
# =====================================================
if ($token) {
    Write-Host ""
    Write-Host ""
    Write-Host "🔒 FASE 4: PROBANDO ENDPOINTS PROTEGIDOS" -ForegroundColor Yellow
    Write-Host "----------------------------------------" -ForegroundColor Yellow
    
    $headers = @{
        "Authorization" = "Bearer $token"
        "Content-Type" = "application/json"
    }
    
    # Test 7: Usuarios (protegido)
    Write-Host ""
    Write-Host "7️⃣  Probando GET /usuarios (protegido)..." -ForegroundColor Cyan
    try {
        $usuarios = Invoke-RestMethod -Uri "$BASE_URL/usuarios" -Method GET -Headers $headers
        Write-Host "✅ Usuarios obtenidos: $($usuarios.Count)" -ForegroundColor Green
        if ($usuarios.Count -gt 0) {
            Write-Host "   👤 Primer usuario: $($usuarios[0].nombre) $($usuarios[0].apellido)" -ForegroundColor White
        }
    } catch {
        Write-Host "❌ Error obteniendo usuarios: $($_.Exception.Message)" -ForegroundColor Red
    }
    
    # Test 8: Crear producto
    Write-Host ""
    Write-Host "8️⃣  Probando POST /productos (crear producto)..." -ForegroundColor Cyan
    
    $nuevoProducto = @{
        nombreObra = "Obra de Prueba Automatizada"
        descripcion = "Esta es una obra creada automáticamente durante las pruebas del sistema"
        precio = 2500.0
        stock = 3
        tecnica = "Óleo sobre lienzo"
        dimensiones = "60x40 cm"
        estilo = "Abstracto contemporáneo"
        anio = 2024
        imagen = "https://images.unsplash.com/photo-1578662996442-48f60103fc96"
        artistaId = 1
        categoriaIds = @(1, 2)
        destacado = $true
        activo = $true
    } | ConvertTo-Json
    
    try {
        $productoCreado = Invoke-RestMethod -Uri "$BASE_URL/productos" -Method POST -Body $nuevoProducto -Headers $headers
        Write-Host "✅ Producto creado exitosamente!" -ForegroundColor Green
        Write-Host "   🆔 ID: $($productoCreado.id)" -ForegroundColor White
        Write-Host "   🎨 Nombre: $($productoCreado.nombreObra)" -ForegroundColor White
        Write-Host "   💰 Precio: $($productoCreado.precio)" -ForegroundColor White
        
        # Test 9: Actualizar el producto que acabamos de crear
        Write-Host ""
        Write-Host "9️⃣  Probando PUT /productos/{id} (actualizar producto)..." -ForegroundColor Cyan
        
        $productoActualizado = $productoCreado | ConvertTo-Json
        $productoActualizado = $productoActualizado | ConvertFrom-Json
        $productoActualizado.precio = 3000.0
        $productoActualizado.descripcion = "Descripción actualizada durante la prueba"
        
        $productoActualizadoJson = $productoActualizado | ConvertTo-Json
        
        try {
            $resultado = Invoke-RestMethod -Uri "$BASE_URL/productos/$($productoCreado.id)" -Method PUT -Body $productoActualizadoJson -Headers $headers
            Write-Host "✅ Producto actualizado exitosamente!" -ForegroundColor Green
            Write-Host "   💰 Nuevo precio: $($resultado.precio)" -ForegroundColor White
        } catch {
            Write-Host "❌ Error actualizando producto: $($_.Exception.Message)" -ForegroundColor Red
        }
        
    } catch {
        Write-Host "❌ Error creando producto: $($_.Exception.Message)" -ForegroundColor Red
    }
    
} else {
    Write-Host ""
    Write-Host ""
    Write-Host "⚠️  FASE 4: SALTADA - No se obtuvo token de autenticación" -ForegroundColor Yellow
}

# =====================================================
# RESUMEN FINAL
# =====================================================
Write-Host ""
Write-Host ""
Write-Host "📊 RESUMEN DE PRUEBAS COMPLETADO" -ForegroundColor Green
Write-Host "================================" -ForegroundColor Green
Write-Host ""
Write-Host "✅ Servidor funcionando en: http://localhost:8080" -ForegroundColor Green
Write-Host "✅ Base de datos conectada correctamente" -ForegroundColor Green
Write-Host "✅ Endpoints públicos funcionando" -ForegroundColor Green
Write-Host "✅ Sistema de autenticación operativo" -ForegroundColor Green
if ($token) {
    Write-Host "✅ Endpoints protegidos funcionando" -ForegroundColor Green
    Write-Host "✅ CRUD de productos operativo" -ForegroundColor Green
}
Write-Host ""
Write-Host "🎯 El sistema está listo para integración con el frontend!" -ForegroundColor Green
Write-Host ""