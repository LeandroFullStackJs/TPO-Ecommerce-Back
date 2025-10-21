# ============================================================================
# PRUEBAS COMPLETAS DEL SISTEMA DE MANEJO DE EXCEPCIONES
# ============================================================================

Write-Host ""
Write-Host "===============================================================" -ForegroundColor Cyan
Write-Host " 🧪 PRUEBAS DEL SISTEMA DE MANEJO DE EXCEPCIONES - E-COMMERCE" -ForegroundColor Cyan
Write-Host "===============================================================" -ForegroundColor Cyan
Write-Host ""

# Función para probar endpoints
function Test-ExceptionEndpoint {
    param(
        [string]$Name,
        [string]$Url,
        [string]$Method = "GET",
        [int]$ExpectedStatus = 200
    )
    
    Write-Host "🧪 Probando: $Name" -ForegroundColor Yellow
    Write-Host "   📡 URL: $Url" -ForegroundColor Gray
    Write-Host "   🔧 Método: $Method" -ForegroundColor Gray
    
    try {
        if ($Method -eq "GET") {
            $response = Invoke-WebRequest -Uri $Url -Method GET -ErrorAction Stop
        } else {
            $response = Invoke-WebRequest -Uri $Url -Method POST -ErrorAction Stop
        }
        
        Write-Host "   ✅ Status: $($response.StatusCode)" -ForegroundColor Green
        if ($response.StatusCode -eq 200) {
            $content = $response.Content
            if ($content.Length -gt 300) {
                $content = $content.Substring(0, 300) + "..."
            }
            Write-Host "   📄 Response: $content" -ForegroundColor White
        }
    }
    catch {
        $statusCode = "N/A"
        $errorBody = "Error de conexión"
        
        if ($_.Exception.Response) {
            $statusCode = $_.Exception.Response.StatusCode.value__
            
            try {
                $stream = $_.Exception.Response.GetResponseStream()
                $reader = New-Object System.IO.StreamReader($stream)
                $errorBody = $reader.ReadToEnd()
                $reader.Close()
                $stream.Close()
            }
            catch {
                $errorBody = "Error al leer la respuesta"
            }
        }
        
        Write-Host "   ❌ Status: $statusCode" -ForegroundColor Red
        Write-Host "   📄 Error Response:" -ForegroundColor Red
        
        if ($errorBody -ne "Error de conexión" -and $errorBody -ne "Error al leer la respuesta") {
            try {
                $jsonResponse = $errorBody | ConvertFrom-Json
                Write-Host "   📅 Timestamp: $($jsonResponse.timestamp)" -ForegroundColor Magenta
                Write-Host "   🔢 Status: $($jsonResponse.status)" -ForegroundColor Magenta
                Write-Host "   ⚠️  Error: $($jsonResponse.error)" -ForegroundColor Magenta
                Write-Host "   💬 Message: $($jsonResponse.message)" -ForegroundColor Magenta
                Write-Host "   🛤️  Path: $($jsonResponse.path)" -ForegroundColor Magenta
                if ($jsonResponse.details) {
                    Write-Host "   📋 Details: $($jsonResponse.details -join ', ')" -ForegroundColor Magenta
                }
            }
            catch {
                Write-Host "   📄 $errorBody" -ForegroundColor Red
            }
        } else {
            Write-Host "   📄 $errorBody" -ForegroundColor Red
        }
    }
    
    Write-Host ""
}

# Verificar que la aplicación esté corriendo
Write-Host "🔍 Verificando conectividad con la aplicación..." -ForegroundColor Blue
Write-Host ""

# PRUEBA 1: Información del sistema
Test-ExceptionEndpoint "Información del sistema" "http://localhost:8080/api/demo/info"

# PRUEBA 2: Producto no encontrado (404)
Test-ExceptionEndpoint "Producto no encontrado (404)" "http://localhost:8080/api/demo/producto-no-encontrado/999"

# PRUEBA 3: ID inválido (400) 
Test-ExceptionEndpoint "ID inválido (400)" "http://localhost:8080/api/demo/validacion-id/-1"

# PRUEBA 4: ID válido (200)
Test-ExceptionEndpoint "ID válido (200)" "http://localhost:8080/api/demo/validacion-id/5"

# PRUEBA 5: Precio negativo (400)
Test-ExceptionEndpoint "Precio negativo (400)" "http://localhost:8080/api/demo/validar-precio?precio=-10" "POST"

# PRUEBA 6: Precio válido (200)
Test-ExceptionEndpoint "Precio válido (200)" "http://localhost:8080/api/demo/validar-precio?precio=25.50" "POST"

# PRUEBA 7: Datos duplicados (409)
Test-ExceptionEndpoint "Datos duplicados (409)" "http://localhost:8080/api/demo/datos-duplicados?email=test@test.com" "POST"

# PRUEBA 8: Email disponible (200)
Test-ExceptionEndpoint "Email disponible (200)" "http://localhost:8080/api/demo/datos-duplicados?email=nuevo@test.com" "POST"

# PRUEBA 9: Argumento inválido (400)
Test-ExceptionEndpoint "Argumento inválido (400)" "http://localhost:8080/api/demo/argumento-invalido?texto="

# PRUEBA 10: Argumento válido (200)
Test-ExceptionEndpoint "Argumento válido (200)" "http://localhost:8080/api/demo/argumento-invalido?texto=HolaMundo"

# PRUEBA 11: Error interno (500)
Test-ExceptionEndpoint "Error interno (500)" "http://localhost:8080/api/demo/error-interno"

Write-Host "===============================================================" -ForegroundColor Cyan
Write-Host " 📊 RESUMEN DE CÓDIGOS DE ESTADO ESPERADOS" -ForegroundColor Cyan
Write-Host "===============================================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "   🟢 200 - OK (operaciones exitosas)" -ForegroundColor Green
Write-Host "   🟡 400 - Bad Request (datos inválidos, argumentos incorrectos)" -ForegroundColor Yellow
Write-Host "   🟡 404 - Not Found (producto no encontrado)" -ForegroundColor Yellow
Write-Host "   🟡 409 - Conflict (datos duplicados)" -ForegroundColor Yellow
Write-Host "   🔴 500 - Internal Server Error (error interno)" -ForegroundColor Red
Write-Host ""
Write-Host "🎉 Si ves respuestas JSON estructuradas con timestamp, status, error y message," -ForegroundColor Green
Write-Host "   ¡el sistema de manejo de excepciones está funcionando correctamente!" -ForegroundColor Green
Write-Host ""
Write-Host "===============================================================" -ForegroundColor Cyan