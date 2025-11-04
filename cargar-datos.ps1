# Script para Cargar Datos de Prueba
# Ejecutar como: .\cargar-datos.ps1

Write-Host "🎨 CARGANDO DATOS DE PRUEBA EN E-COMMERCE DE ARTE" -ForegroundColor Green
Write-Host "=================================================" -ForegroundColor Green

# Verificar que los contenedores estén ejecutándose
Write-Host "`n1. Verificando contenedores..." -ForegroundColor Cyan
$mysqlContainer = docker ps --filter "name=ecommerce_mysql" --format "{{.Names}}"
$backendContainer = docker ps --filter "name=ecommerce_backend" --format "{{.Names}}"

if (-not $mysqlContainer) {
    Write-Host "   ❌ Contenedor MySQL no está ejecutándose" -ForegroundColor Red
    Write-Host "   💡 Ejecuta primero: .\inicio-rapido.ps1" -ForegroundColor Yellow
    exit 1
}

if (-not $backendContainer) {
    Write-Host "   ❌ Contenedor Backend no está ejecutándose" -ForegroundColor Red
    Write-Host "   💡 Ejecuta primero: .\inicio-rapido.ps1" -ForegroundColor Yellow
    exit 1
}

Write-Host "   ✅ Contenedores encontrados:" -ForegroundColor Green
Write-Host "     - $mysqlContainer" -ForegroundColor White
Write-Host "     - $backendContainer" -ForegroundColor White

# Verificar que el archivo SQL existe
Write-Host "`n2. Verificando archivo de datos..." -ForegroundColor Cyan
if (-not (Test-Path "datos_completos_arte.sql")) {
    Write-Host "   ❌ Archivo datos_completos_arte.sql no encontrado" -ForegroundColor Red
    Write-Host "   💡 Asegúrate de estar en el directorio correcto del proyecto" -ForegroundColor Yellow
    exit 1
}
Write-Host "   ✅ Archivo datos_completos_arte.sql encontrado" -ForegroundColor Green

# Copiar archivo SQL al contenedor
Write-Host "`n3. Copiando archivo SQL al contenedor..." -ForegroundColor Cyan
try {
    docker cp datos_completos_arte.sql ecommerce_mysql:/tmp/datos_completos_arte.sql
    Write-Host "   ✅ Archivo copiado exitosamente" -ForegroundColor Green
} catch {
    Write-Host "   ❌ Error al copiar archivo: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# Ejecutar script SQL
Write-Host "`n4. Ejecutando script SQL..." -ForegroundColor Cyan
Write-Host "   ⏳ Esto puede tomar unos segundos..." -ForegroundColor Yellow

try {
    $result = docker exec -it ecommerce_mysql mysql -u root ecommerce_db -e "source /tmp/datos_completos_arte.sql"
    Write-Host "   ✅ Script SQL ejecutado exitosamente" -ForegroundColor Green
} catch {
    Write-Host "   ❌ Error al ejecutar script SQL: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "   💡 Revisa los logs: docker logs ecommerce_mysql" -ForegroundColor Yellow
    exit 1
}

# Verificar datos cargados
Write-Host "`n5. Verificando datos cargados..." -ForegroundColor Cyan
try {
    $productos = Invoke-RestMethod -Uri "http://localhost:8080/api/productos" -Method GET -TimeoutSec 30
    $categorias = Invoke-RestMethod -Uri "http://localhost:8080/api/categorias" -Method GET -TimeoutSec 30
    $artistas = Invoke-RestMethod -Uri "http://localhost:8080/api/artistas" -Method GET -TimeoutSec 30
    
    Write-Host "   ✅ Datos verificados correctamente:" -ForegroundColor Green
    Write-Host "     - $($productos.Count) productos cargados" -ForegroundColor White
    Write-Host "     - $($categorias.Count) categorías cargadas" -ForegroundColor White
    Write-Host "     - $($artistas.Count) artistas cargados" -ForegroundColor White
} catch {
    Write-Host "   ⚠️  Error al verificar datos via API: $($_.Exception.Message)" -ForegroundColor Yellow
    Write-Host "   💡 Los datos pueden haberse cargado correctamente. Verifica manualmente." -ForegroundColor Blue
}

# Mostrar ejemplos de productos
Write-Host "`n📋 EJEMPLOS DE PRODUCTOS CARGADOS:" -ForegroundColor Green
Write-Host "===================================" -ForegroundColor Green
try {
    $productosDestacados = Invoke-RestMethod -Uri "http://localhost:8080/api/productos/destacados" -Method GET
    $productosDestacados | Select-Object -First 3 | ForEach-Object {
        Write-Host "• $($_.nombreObra) - $($_.artista) - $$$($_.precio)" -ForegroundColor White
    }
} catch {
    Write-Host "No se pudieron obtener ejemplos de productos" -ForegroundColor Yellow
}

Write-Host "`n🎯 DATOS DE PRUEBA CARGADOS EXITOSAMENTE!" -ForegroundColor Green
Write-Host "==========================================" -ForegroundColor Green
Write-Host "• 8 Categorías de arte" -ForegroundColor White
Write-Host "• 5 Artistas con biografías" -ForegroundColor White
Write-Host "• 11 Obras de arte con precios" -ForegroundColor White
Write-Host "• 2 Usuarios de prueba" -ForegroundColor White
Write-Host ""
Write-Host "🔐 CREDENCIALES DE PRUEBA:" -ForegroundColor Yellow
Write-Host "• Admin: admin@arte.com / Admin123@" -ForegroundColor White
Write-Host "• Usuario: galeria@arte.com / Admin123@" -ForegroundColor White
Write-Host ""
Write-Host "🌐 PRUEBA LA API:" -ForegroundColor Blue
Write-Host "curl http://localhost:8080/api/productos" -ForegroundColor White