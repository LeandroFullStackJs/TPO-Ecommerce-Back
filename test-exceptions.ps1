# Script para probar el sistema de manejo de excepciones
# Ejecutar este script después de iniciar la aplicación Spring Boot

Write-Host "=== PRUEBAS DEL SISTEMA DE MANEJO DE EXCEPCIONES ===" -ForegroundColor Cyan
Write-Host ""

function Test-Endpoint {
    param(
        [string]$Name,
        [string]$Url,
        [string]$Method = "GET",
        [int]$ExpectedStatus = 200
    )
    
    Write-Host "🧪 Probando: $Name" -ForegroundColor Yellow
    Write-Host "   URL: $Url" -ForegroundColor Gray
    Write-Host "   Método: $Method" -ForegroundColor Gray
    
    try {
        if ($Method -eq "GET") {
            $response = Invoke-WebRequest -Uri $Url -Method GET -ErrorAction Stop
        } else {
            $response = Invoke-WebRequest -Uri $Url -Method POST -ErrorAction Stop
        }
        
        Write-Host "   ✅ Status: $($response.StatusCode)" -ForegroundColor Green
        if ($response.StatusCode -eq 200) {
            Write-Host "   📄 Response: $($response.Content)" -ForegroundColor White
        }
    }
    catch {
        $statusCode = $_.Exception.Response.StatusCode.value__
        $errorBody = ""
        
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
        
        Write-Host "   ❌ Status: $statusCode" -ForegroundColor Red
        Write-Host "   📄 Error Response:" -ForegroundColor Red
        
        try {
            $jsonResponse = $errorBody | ConvertFrom-Json
            Write-Host "   {" -ForegroundColor Magenta
            Write-Host "     timestamp: $($jsonResponse.timestamp)" -ForegroundColor Magenta
            Write-Host "     status: $($jsonResponse.status)" -ForegroundColor Magenta
            Write-Host "     error: $($jsonResponse.error)" -ForegroundColor Magenta
            Write-Host "     message: $($jsonResponse.message)" -ForegroundColor Magenta
            Write-Host "     path: $($jsonResponse.path)" -ForegroundColor Magenta
            Write-Host "   }" -ForegroundColor Magenta
        }
        catch {
            Write-Host "   $errorBody" -ForegroundColor Red
        }
    }
    
    Write-Host ""
}

# Verificar que la aplicación esté corriendo
Write-Host "🔍 Verificando que la aplicación esté corriendo..." -ForegroundColor Blue
try {
    $healthCheck = Invoke-WebRequest -Uri "http://localhost:8080/actuator/health" -Method GET -ErrorAction SilentlyContinue
    if ($healthCheck.StatusCode -eq 200) {
        Write-Host "✅ Aplicación corriendo correctamente" -ForegroundColor Green
    }
}
catch {
    Write-Host "⚠️  No se pudo verificar el estado de la aplicación con /actuator/health" -ForegroundColor Yellow
    Write-Host "   Continuando con las pruebas..." -ForegroundColor Yellow
}
Write-Host ""

# 1. Información del sistema
Test-Endpoint "Información del sistema" "http://localhost:8080/api/demo/info"

# 2. Producto no encontrado (404)
Test-Endpoint "Producto no encontrado" "http://localhost:8080/api/demo/producto-no-encontrado/999"

# 3. ID inválido (400) 
Test-Endpoint "ID inválido" "http://localhost:8080/api/demo/validacion-id/-1"

# 4. ID válido (200)
Test-Endpoint "ID válido" "http://localhost:8080/api/demo/validacion-id/5"

# 5. Precio negativo (400)
Test-Endpoint "Precio negativo" "http://localhost:8080/api/demo/validar-precio?precio=-10" "POST"

# 6. Precio válido (200)
Test-Endpoint "Precio válido" "http://localhost:8080/api/demo/validar-precio?precio=25.50" "POST"

# 7. Datos duplicados (409)
Test-Endpoint "Datos duplicados" "http://localhost:8080/api/demo/datos-duplicados?email=test@test.com" "POST"

# 8. Email disponible (200)
Test-Endpoint "Email disponible" "http://localhost:8080/api/demo/datos-duplicados?email=nuevo@test.com" "POST"

# 9. Argumento inválido (400)
Test-Endpoint "Argumento inválido" "http://localhost:8080/api/demo/argumento-invalido?texto="

# 10. Argumento válido (200)
Test-Endpoint "Argumento válido" "http://localhost:8080/api/demo/argumento-invalido?texto=HolaMundo"

# 11. Error interno (500)
Test-Endpoint "Error interno" "http://localhost:8080/api/demo/error-interno"

Write-Host "=== PRUEBAS COMPLETADAS ===" -ForegroundColor Cyan
Write-Host ""
Write-Host "📋 Resumen de códigos de estado esperados:" -ForegroundColor Blue
Write-Host "   200 - OK (operaciones exitosas)" -ForegroundColor Green
Write-Host "   400 - Bad Request (datos inválidos, argumentos incorrectos)" -ForegroundColor Yellow
Write-Host "   404 - Not Found (producto no encontrado)" -ForegroundColor Yellow
Write-Host "   409 - Conflict (datos duplicados)" -ForegroundColor Yellow
Write-Host "   500 - Internal Server Error (error interno)" -ForegroundColor Red
Write-Host ""
Write-Host "🎉 Si ves respuestas JSON estructuradas con timestamp, status, error y message," -ForegroundColor Green
Write-Host "   el sistema de manejo de excepciones está funcionando correctamente!" -ForegroundColor Green