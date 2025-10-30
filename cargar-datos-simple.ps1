# Script simple para cargar datos de arte
$baseUrl = "http://localhost:8080"

Write-Host "🎨 CARGANDO DATOS DE ARTE - VERSION SIMPLE" -ForegroundColor Cyan
Write-Host "===============================================" -ForegroundColor Gray

# Función para hacer peticiones API
function Invoke-ApiRequest {
    param(
        [string]$Method,
        [string]$Url,
        [hashtable]$Body
    )
    
    try {
        $headers = @{
            "Content-Type" = "application/json"
        }
        
        $jsonBody = $Body | ConvertTo-Json -Depth 10
        
        $response = Invoke-RestMethod -Uri $Url -Method $Method -Body $jsonBody -Headers $headers -ErrorAction Stop
        return $response
    }
    catch {
        Write-Host "❌ Error en $Url`: $($_.Exception.Message)" -ForegroundColor Red
        return $null
    }
}

# Verificar que el servidor esté funcionando
Write-Host "🔍 Verificando servidor..." -ForegroundColor Yellow
try {
    $healthCheck = Invoke-RestMethod -Uri "$baseUrl/health" -Method GET -ErrorAction Stop
    Write-Host "✅ Servidor funcionando correctamente" -ForegroundColor Green
}
catch {
    Write-Host "❌ Error: El servidor no está funcionando en $baseUrl" -ForegroundColor Red
    Write-Host "   Asegúrate de que el servidor esté iniciado con: .\mvnw.cmd spring-boot:run" -ForegroundColor Yellow
    exit 1
}

# 1. Crear Categorías
Write-Host "`n📁 Creando categorías..." -ForegroundColor Yellow
$categorias = @(
    @{ nombre = "Pintura"; descripcion = "Obras pictóricas tradicionales y contemporáneas"; activa = $true; orden = 1 },
    @{ nombre = "Escultura"; descripcion = "Arte tridimensional en diversos materiales"; activa = $true; orden = 2 },
    @{ nombre = "Fotografía"; descripcion = "Arte fotográfico analógico y digital"; activa = $true; orden = 3 },
    @{ nombre = "Arte Digital"; descripcion = "Creaciones artísticas generadas digitalmente"; activa = $true; orden = 4 },
    @{ nombre = "Arte Abstracto"; descripcion = "Expresiones artísticas no figurativas"; activa = $true; orden = 5 },
    @{ nombre = "Realismo"; descripcion = "Representaciones fieles de la realidad"; activa = $true; orden = 6 },
    @{ nombre = "Impresionismo"; descripcion = "Estilo artístico con énfasis en la luz"; activa = $true; orden = 7 },
    @{ nombre = "Arte Contemporáneo"; descripcion = "Expresiones artísticas actuales"; activa = $true; orden = 8 }
)

foreach ($categoria in $categorias) {
    Write-Host "Agregando categoría: $($categoria.nombre)" -ForegroundColor White
    $response = Invoke-ApiRequest -Method "POST" -Url "$baseUrl/categorias" -Body $categoria
    if ($response) {
        Write-Host "✅ Categoría '$($categoria.nombre)' agregada con ID: $($response.id)" -ForegroundColor Green
    }
    Start-Sleep 1
}

# 2. Crear Artistas
Write-Host "`n👨‍🎨 Creando artistas..." -ForegroundColor Yellow
$artistas = @(
    @{
        nombre = "Carlos Mendoza"
        biografia = "Artista contemporáneo especializado en pintura abstracta. Sus obras reflejan la complejidad de las emociones humanas a través de colores vibrantes y formas orgánicas."
        email = "carlos.mendoza@arte.com"
        imagenPerfil = "https://via.placeholder.com/150/FF6B6B/FFFFFF?text=CM"
        activo = $true
    },
    @{
        nombre = "María García"
        biografia = "Pintora expresionista con más de 15 años de experiencia. Su trabajo explora la relación entre música y color, creando sinestesias visuales únicas."
        email = "maria.garcia@arte.com"
        imagenPerfil = "https://via.placeholder.com/150/4ECDC4/FFFFFF?text=MG"
        activo = $true
    },
    @{
        nombre = "Ana Rodríguez"
        biografia = "Fotógrafa documentalista especializada en fotografía de naturaleza. Sus imágenes capturan la belleza efímera de los momentos naturales."
        email = "ana.rodriguez@arte.com"
        imagenPerfil = "https://via.placeholder.com/150/95E1D3/FFFFFF?text=AR"
        activo = $true
    },
    @{
        nombre = "Diego Silva"
        biografia = "Artista digital pionero en el uso de inteligencia artificial para la creación artística. Combina tecnología y creatividad tradicional."
        email = "diego.silva@arte.com"
        imagenPerfil = "https://via.placeholder.com/150/F38BA8/FFFFFF?text=DS"
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

# 3. Crear Productos (Obras de Arte)
Write-Host "`n🖼️ Creando obras de arte..." -ForegroundColor Yellow
$productos = @(
    @{
        nombre = "Sinfonía de Colores"
        descripcion = "Una explosión de colores que representa la complejidad de las emociones humanas. Cada pincelada cuenta una historia diferente."
        precio = 1500.00
        stock = 1
        imagen = "https://via.placeholder.com/400x500/FF6B6B/FFFFFF?text=Sinfonía+de+Colores"
        artista = "Carlos Mendoza"
        tecnica = "Oleo sobre lienzo"
        dimensiones = "80cm x 100cm"
        anio = 2024
        estilo = "Abstracto"
        destacado = $true
        activo = $true
        categoriaIds = @(1, 5, 8) # Pintura, Arte Abstracto, Arte Contemporáneo
    },
    @{
        nombre = "Bosque Susurrante"
        descripcion = "Fotografía capturada en el momento perfecto donde la luz se filtra entre los árboles creando un ambiente mágico."
        precio = 800.00
        stock = 3
        imagen = "https://via.placeholder.com/400x500/4ECDC4/FFFFFF?text=Bosque+Susurrante"
        artista = "Ana Rodríguez"
        tecnica = "Fotografía digital"
        dimensiones = "60cm x 90cm"
        anio = 2023
        estilo = "Naturalismo"
        destacado = $false
        activo = $true
        categoriaIds = @(3, 8) # Fotografía, Arte Contemporáneo
    },
    @{
        nombre = "Visiones Digitales"
        descripcion = "Obra digital que explora las posibilidades infinitas del arte generado por computadora, fusionando realidad y fantasía."
        precio = 1200.00
        stock = 2
        imagen = "https://via.placeholder.com/400x500/95E1D3/FFFFFF?text=Visiones+Digitales"
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
        imagen = "https://via.placeholder.com/400x500/F38BA8/FFFFFF?text=Melodía+Visual"
        artista = "María García"
        tecnica = "Acrílico sobre lienzo"
        dimensiones = "100cm x 120cm"
        anio = 2023
        estilo = "Expresionista"
        destacado = $false
        activo = $true
        categoriaIds = @(1, 8) # Pintura, Arte Contemporáneo
    },
    @{
        nombre = "Reflejos del Alma"
        descripcion = "Fotografía en blanco y negro que captura la esencia humana a través de juegos de luz y sombra."
        precio = 950.00
        stock = 4
        imagen = "https://via.placeholder.com/400x500/A8DADC/FFFFFF?text=Reflejos+del+Alma"
        artista = "Ana Rodríguez"
        tecnica = "Fotografía analógica"
        dimensiones = "70cm x 100cm"
        anio = 2023
        estilo = "Documental"
        destacado = $false
        activo = $true
        categoriaIds = @(3, 8) # Fotografía, Arte Contemporáneo
    },
    @{
        nombre = "Formas en Movimiento"
        descripcion = "Escultura dinámica que parece cambiar según el ángulo de observación, representando la fluidez del tiempo."
        precio = 3500.00
        stock = 1
        imagen = "https://via.placeholder.com/400x500/457B9D/FFFFFF?text=Formas+en+Movimiento"
        artista = "Carlos Mendoza"
        tecnica = "Bronce patinado"
        dimensiones = "150cm x 80cm x 60cm"
        anio = 2024
        estilo = "Contemporáneo"
        destacado = $true
        activo = $true
        categoriaIds = @(2, 8) # Escultura, Arte Contemporáneo
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

Write-Host "`n🔗 Prueba rápida:" -ForegroundColor Cyan
Write-Host "curl $baseUrl/productos" -ForegroundColor Gray