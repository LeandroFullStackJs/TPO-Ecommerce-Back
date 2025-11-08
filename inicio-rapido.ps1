# Script de Inicio Rapido para E-Commerce de Arte
# Ejecutar como: .\inicio-rapido.ps1

Write-Host "E-COMMERCE DE ARTE - INICIO RAPIDO" -ForegroundColor Green
Write-Host "=====================================" -ForegroundColor Green

# Verificar Docker
Write-Host "`n1. Verificando Docker..." -ForegroundColor Cyan
try {
    $dockerVersion = docker --version
    Write-Host "   [OK] Docker encontrado: $dockerVersion" -ForegroundColor Green
} catch {
    Write-Host "   [ERROR] Docker no encontrado. Instala Docker Desktop primero." -ForegroundColor Red
    exit 1
}

# Verificar Docker Compose
try {
    $composeVersion = docker-compose --version
    Write-Host "   [OK] Docker Compose encontrado: $composeVersion" -ForegroundColor Green
} catch {
    Write-Host "   [ERROR] Docker Compose no encontrado." -ForegroundColor Red
    exit 1
}

# Levantar servicios
Write-Host "`n2. Levantando servicios..." -ForegroundColor Cyan
Write-Host "   [INFO] Iniciando MySQL y Backend..." -ForegroundColor Yellow

try {
    docker-compose up -d
    Write-Host "   [OK] Servicios iniciados correctamente" -ForegroundColor Green
} catch {
    Write-Host "   [ERROR] Error al iniciar servicios" -ForegroundColor Red
    exit 1
}

# Esperar a que los servicios estén listos
Write-Host "`n3. Esperando que los servicios estén listos..." -ForegroundColor Cyan
Start-Sleep 10

# Verificar estado de contenedores
Write-Host "`n4. Verificando estado de contenedores..." -ForegroundColor Cyan
$containers = docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"
Write-Host $containers

# Verificar API
Write-Host "`n5. Verificando API..." -ForegroundColor Cyan
try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/productos" -Method GET -TimeoutSec 30
    $count = $response.Count
    Write-Host "   [OK] API funcionando - $count productos disponibles" -ForegroundColor Green
} catch {
    Write-Host "   [WARNING] API aun no esta lista. Puedes probar en unos segundos." -ForegroundColor Yellow
    Write-Host "   [TIP] Ejecuta: Invoke-RestMethod -Uri 'http://localhost:8080/api/productos'" -ForegroundColor Blue
}

# Mostrar información útil
Write-Host "`n[INFO] INFORMACION UTIL:" -ForegroundColor Green
Write-Host "====================" -ForegroundColor Green
Write-Host "• API Base URL: http://localhost:8080/api" -ForegroundColor White
Write-Host "• Productos: http://localhost:8080/api/productos" -ForegroundColor White
Write-Host "• Categorias: http://localhost:8080/api/categorias" -ForegroundColor White
Write-Host "• Artistas: http://localhost:8080/api/artistas" -ForegroundColor White
Write-Host ""
Write-Host "[USER] USUARIOS DE PRUEBA:" -ForegroundColor Yellow
Write-Host "• Admin: admin@arte.com / Admin123@" -ForegroundColor White
Write-Host "• Usuario: galeria@arte.com / Admin123@" -ForegroundColor White
Write-Host ""
Write-Host "[TOOLS] COMANDOS UTILES:" -ForegroundColor Blue
Write-Host "• Ver logs backend: docker logs ecommerce_backend" -ForegroundColor White
Write-Host "• Ver logs MySQL: docker logs ecommerce_mysql" -ForegroundColor White
Write-Host "• Parar servicios: docker-compose down" -ForegroundColor White
Write-Host "• Reiniciar: docker-compose restart" -ForegroundColor White

Write-Host "`n[SUCCESS] Tu E-Commerce de Arte esta listo!" -ForegroundColor Green
Write-Host "[TIP] Si es la primera vez, ejecuta tambien: .\cargar-datos.ps1" -ForegroundColor Yellow