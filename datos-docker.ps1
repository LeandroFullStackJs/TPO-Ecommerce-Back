# Script para agregar datos básicos al e-commerce via API
Write-Host "🚀 AGREGANDO DATOS BÁSICOS AL E-COMMERCE CON DOCKER" -ForegroundColor Green

$baseUrl = "http://localhost:8080/api"

# Función para hacer requests
function Invoke-ApiRequest {
    param($Method, $Uri, $Body = $null)
    try {
        if ($Body) {
            $response = Invoke-RestMethod -Uri $Uri -Method $Method -Body ($Body | ConvertTo-Json) -ContentType "application/json"
        } else {
            $response = Invoke-RestMethod -Uri $Uri -Method $Method
        }
        return $response
    } catch {
        Write-Host "Error en $Uri : $($_.Exception.Message)" -ForegroundColor Red
        return $null
    }
}

# 1. Agregar usuarios
Write-Host "`n👤 1. AGREGANDO USUARIOS" -ForegroundColor Cyan

$usuarios = @(
    @{ 
        nombre = "Admin"
        apellido = "Sistema"
        email = "admin@artgallery.com"
        password = "admin123"
        role = "ADMIN"
    },
    @{ 
        nombre = "Demo"
        apellido = "User"
        email = "demo@artgallery.com"
        password = "demo123"
        role = "USER"
    }
)

foreach ($usuario in $usuarios) {
    Write-Host "  Agregando usuario: $($usuario.email)"
    $response = Invoke-ApiRequest -Method "POST" -Uri "$baseUrl/auth/register" -Body $usuario
    if ($response) {
        Write-Host "    ✅ Usuario creado exitosamente" -ForegroundColor Green
    }
}

# 2. Agregar categorías
Write-Host "`n📂 2. AGREGANDO CATEGORÍAS" -ForegroundColor Cyan

$categorias = @(
    @{ nombre = "Pintura" },
    @{ nombre = "Escultura" },
    @{ nombre = "Arte Digital" },
    @{ nombre = "Fotografía" },
    @{ nombre = "Arte Abstracto" }
)

foreach ($categoria in $categorias) {
    Write-Host "  Agregando categoría: $($categoria.nombre)"
    $response = Invoke-ApiRequest -Method "POST" -Uri "$baseUrl/categorias" -Body $categoria
    if ($response) {
        Write-Host "    ✅ Categoría creada exitosamente" -ForegroundColor Green
    }
}

# 3. Agregar artistas
Write-Host "`n🎨 3. AGREGANDO ARTISTAS" -ForegroundColor Cyan

$artistas = @(
    @{ 
        nombre = "María González"
        biografia = "Artista contemporánea especializada en arte abstracto"
        email = "maria@artgallery.com"
        activo = $true
    },
    @{ 
        nombre = "Carlos Mendoza"
        biografia = "Pintor paisajista reconocido internacionalmente"
        email = "carlos@artgallery.com"
        activo = $true
    },
    @{ 
        nombre = "Ana Silvestre"
        biografia = "Especialista en retratos y arte figurativo"
        email = "ana@artgallery.com"
        activo = $true
    }
)

foreach ($artista in $artistas) {
    Write-Host "  Agregando artista: $($artista.nombre)"
    $response = Invoke-ApiRequest -Method "POST" -Uri "$baseUrl/artistas" -Body $artista
    if ($response) {
        Write-Host "    ✅ Artista creado exitosamente" -ForegroundColor Green
    }
}

# 4. Listar datos para verificar
Write-Host "`n📋 4. VERIFICANDO DATOS CREADOS" -ForegroundColor Cyan

Write-Host "  Usuarios:" -ForegroundColor Yellow
$usuarios = Invoke-ApiRequest -Method "GET" -Uri "$baseUrl/usuarios"
if ($usuarios) {
    $usuarios | ForEach-Object { Write-Host "    - $($_.email)" }
}

Write-Host "  Categorías:" -ForegroundColor Yellow
$categorias = Invoke-ApiRequest -Method "GET" -Uri "$baseUrl/categorias"
if ($categorias) {
    $categorias | ForEach-Object { Write-Host "    - $($_.nombre)" }
}

Write-Host "  Artistas:" -ForegroundColor Yellow
$artistas = Invoke-ApiRequest -Method "GET" -Uri "$baseUrl/artistas"
if ($artistas) {
    $artistas | ForEach-Object { Write-Host "    - $($_.nombre)" }
}

Write-Host "`n🎉 DATOS BÁSICOS AGREGADOS EXITOSAMENTE" -ForegroundColor Green
Write-Host "Tu API está funcionando en: http://localhost:8080" -ForegroundColor Cyan