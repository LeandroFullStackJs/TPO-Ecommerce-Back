# Script para agregar datos de arte de ejemplo al e-commerce
Write-Host "🎨 AGREGANDO DATOS DE ARTE DE EJEMPLO AL E-COMMERCE" -ForegroundColor Green

$baseUrl = "http://localhost:8080/api"

# Esperar a que la aplicación esté lista
Write-Host "⏳ Esperando que la aplicación esté lista..." -ForegroundColor Yellow
Start-Sleep 5

# Función para hacer peticiones HTTP
function Invoke-ApiRequest {
    param(
        [string]$Method,
        [string]$Url,
        [object]$Body
    )
    
    try {
        $headers = @{
            "Content-Type" = "application/json"
        }
        
        if ($Body) {
            $jsonBody = $Body | ConvertTo-Json -Depth 3
            Write-Host "Enviando: $jsonBody" -ForegroundColor Gray
            $response = Invoke-RestMethod -Uri $Url -Method $Method -Headers $headers -Body $jsonBody
        } else {
            $response = Invoke-RestMethod -Uri $Url -Method $Method -Headers $headers
        }
        
        return $response
    }
    catch {
        Write-Host "Error en petición: $($_.Exception.Message)" -ForegroundColor Red
        return $null
    }
}

# 1. AGREGAR CATEGORÍAS DE ARTE
Write-Host "`n🎯 1. AGREGANDO CATEGORÍAS DE ARTE" -ForegroundColor Cyan

$categorias = @(
    @{ nombre = "Pintura" },
    @{ nombre = "Escultura" },
    @{ nombre = "Fotografía" },
    @{ nombre = "Arte Digital" },
    @{ nombre = "Arte Abstracto" },
    @{ nombre = "Realismo" },
    @{ nombre = "Impresionismo" },
    @{ nombre = "Arte Contemporáneo" }
)

foreach ($categoria in $categorias) {
    Write-Host "Agregando categoría: $($categoria.nombre)" -ForegroundColor White
    $response = Invoke-ApiRequest -Method "POST" -Url "$baseUrl/categorias" -Body $categoria
    if ($response) {
        Write-Host "✅ Categoría '$($categoria.nombre)' agregada con ID: $($response.id)" -ForegroundColor Green
    }
    Start-Sleep 1
}

# 2. AGREGAR ARTISTAS
Write-Host "`n👨‍🎨 2. AGREGANDO ARTISTAS" -ForegroundColor Cyan

$artistas = @(
    @{ 
        nombre = "María García"
        email = "maria.garcia@email.com"
        biografia = "Artista especializada en pintura abstracta contemporánea. Sus obras reflejan emociones a través de colores vibrantes y formas dinámicas."
        imagenPerfil = "/api/proxy/image?url=https://images.unsplash.com/photo-1494790108755-2616b612b789?w=300&h=300&fit=crop&crop=face"
        activo = $true
    },
    @{ 
        nombre = "Carlos Mendoza"
        email = "carlos.mendoza@email.com"
        biografia = "Escultor con más de 15 años de experiencia. Trabaja principalmente con bronce y mármol, creando piezas que exploran la forma humana."
        imagenPerfil = "/api/proxy/image?url=https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=300&h=300&fit=crop&crop=face"
        activo = $true
    },
    @{ 
        nombre = "Ana Rodríguez"
        email = "ana.rodriguez@email.com"
        biografia = "Fotógrafa profesional especializada en paisajes y naturaleza. Sus obras capturan la belleza natural con una perspectiva única."
        imagenPerfil = "/api/proxy/image?url=https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=300&h=300&fit=crop&crop=face"
        activo = $true
    },
    @{ 
        nombre = "Diego Silva"
        email = "diego.silva@email.com"
        biografia = "Artista digital emergente que combina técnicas tradicionales con tecnología moderna para crear obras innovadoras."
        imagenPerfil = "/api/proxy/image?url=https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=300&h=300&fit=crop&crop=face"
        activo = $true
    }
)

foreach ($artista in $artistas) {
    Write-Host "Agregando artista: $($artista.nombre)" -ForegroundColor White
    $response = Invoke-ApiRequest -Method "POST" -Url "$baseUrl/artistas" -Body $artista
    if ($response) {
        Write-Host "✅ Artista '$($artista.nombre)' agregado con ID: $($response.id)" -ForegroundColor Green
    }
    Start-Sleep 1
}

# 3. AGREGAR PRODUCTOS (OBRAS DE ARTE)
Write-Host "`n🖼️ 3. AGREGANDO OBRAS DE ARTE" -ForegroundColor Cyan

$productos = @(
    @{
        nombre = "Amanecer Abstracto"
        descripcion = "Una interpretación moderna del amanecer a través de formas geométricas y colores cálidos. Esta obra captura la energía del nuevo día."
        precio = 1500.00
        stock = 1
        imagen = "/api/proxy/image?url=https://images.unsplash.com/photo-1541961017774-22349e4a1262?w=500&h=600&fit=crop"
        artista = "María García"
        tecnica = "Oleo sobre lienzo"
        dimensiones = "80cm x 100cm"
        anio = 2023
        estilo = "Abstracto"
        destacado = $true
        activo = $true
        categoriaIds = @(1, 5) # Pintura, Arte Abstracto
    },
    @{
        nombre = "Serenidad en Bronce"
        descripcion = "Escultura que representa la tranquilidad del espíritu humano. Cada curva ha sido cuidadosamente trabajada para transmitir paz."
        precio = 3500.00
        stock = 1
        imagen = "https://images.unsplash.com/photo-1578662996442-48f60103fc96?w=500`&h=600`&fit=crop"
        artista = "Carlos Mendoza"
        tecnica = "Bronce fundido"
        dimensiones = "45cm x 30cm x 25cm"
        anio = 2023
        estilo = "Contemporáneo"
        destacado = $true
        activo = $true
        categoriaIds = @(2, 8) # Escultura, Arte Contemporáneo
    },
    @{
        nombre = "Bosque Susurrante"
        descripcion = "Fotografía capturada en el momento perfecto donde la luz se filtra entre los árboles creando un ambiente mágico."
        precio = 800.00
        stock = 3
        imagen = "https://images.unsplash.com/photo-1441974231531-c6227db76b6e?w=500&h=600&fit=crop"
        artista = "Ana Rodríguez"
        tecnica = "Fotografía digital"
        dimensiones = "60cm x 90cm"
        anio = 2023
        estilo = "Naturalismo"
        destacado = $false
        activo = $true
        categoriaIds = @(3) # Fotografía
    },
    @{
        nombre = "Visiones Digitales"
        descripcion = "Obra digital que explora las posibilidades infinitas del arte generado por computadora, fusionando realidad y fantasía."
        precio = 1200.00
        stock = 2
        imagen = "/api/proxy/image?url=https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=500&h=600&fit=crop"
        artista = "Diego Silva"
        tecnica = "Arte digital"
        dimensiones = "50cm x 70cm (impresión)"
        anio = 2024
        estilo = "Digital"
        destacado = $true
        activo = $true
        categoriaIds = @(4, 8) # Arte Digital, Arte Contemporáneo
    },
    @{
        nombre = "Melodía Visual"
        descripcion = "Pintura que traduce la música clásica en colores y formas. Cada pincelada representa una nota musical."
        precio = 2200.00
        stock = 1
        imagen = "/api/proxy/image?url=https://images.unsplash.com/photo-1536924430914-91f9e2041b83?w=500&h=600&fit=crop"
        artista = "María García"
        tecnica = "Acrílico sobre lienzo"
        dimensiones = "100cm x 120cm"
        anio = 2023
        estilo = "Expresionista"
        destacado = $false
        activo = $true
        categoriaIds = @(1, 5) # Pintura, Arte Abstracto
    },
    @{
        nombre = "Reflejo Urbano"
        descripcion = "Fotografía urbana que captura el contraste entre la arquitectura moderna y la vida cotidiana en la ciudad."
        precio = 950.00
        stock = 2
        imagen = "/api/proxy/image?url=https://images.unsplash.com/photo-1449824913935-59a10b8d2000?w=500&h=600&fit=crop"
        artista = "Ana Rodríguez"
        tecnica = "Fotografía analógica"
        dimensiones = "70cm x 100cm"
        anio = 2023
        estilo = "Documental"
        destacado = $false
        activo = $true
        categoriaIds = @(3, 8) # Fotografía, Arte Contemporáneo
    }
)

foreach ($producto in $productos) {
    Write-Host "Agregando obra: $($producto.nombre)" -ForegroundColor White
    $response = Invoke-ApiRequest -Method "POST" -Url "$baseUrl/productos" -Body $producto
    if ($response) {
        Write-Host "✅ Obra '$($producto.nombre)' agregada con ID: $($response.id)" -ForegroundColor Green
    }
    Start-Sleep 2
}

Write-Host "`n🎉 ¡DATOS DE ARTE AGREGADOS EXITOSAMENTE!" -ForegroundColor Green
Write-Host "📊 Resumen:" -ForegroundColor Yellow
Write-Host "   • 8 categorías de arte" -ForegroundColor White
Write-Host "   • 4 artistas" -ForegroundColor White
Write-Host "   • 6 obras de arte" -ForegroundColor White
Write-Host "`n🌐 Endpoints disponibles:" -ForegroundColor Yellow
Write-Host "   • GET $baseUrl/productos - Ver todas las obras" -ForegroundColor White
Write-Host "   • GET $baseUrl/categorias - Ver categorías" -ForegroundColor White
Write-Host "   • GET $baseUrl/artistas - Ver artistas" -ForegroundColor White
Write-Host "   • GET $baseUrl/health - Estado del servidor" -ForegroundColor White