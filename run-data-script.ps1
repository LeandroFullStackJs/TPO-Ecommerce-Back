# PowerShell script to run the SQL data file
# This script will execute the datos_prueba_arte.sql file against the MySQL database

$sqlFile = "datos_prueba_arte.sql"
$database = "ecommerce_db"
$username = "root"

Write-Host "Ejecutando script de datos de prueba..." -ForegroundColor Green
Write-Host "Archivo: $sqlFile" -ForegroundColor Yellow
Write-Host "Base de datos: $database" -ForegroundColor Yellow

# Check if the SQL file exists
if (-not (Test-Path $sqlFile)) {
    Write-Host "Error: No se encontró el archivo $sqlFile" -ForegroundColor Red
    exit 1
}

# Try to execute the SQL file
try {
    # Using mysql command line tool
    $process = Start-Process -FilePath "mysql" -ArgumentList "-u", $username, "-p", "-D", $database, "-e", "source $sqlFile" -Wait -NoNewWindow -PassThru
    
    if ($process.ExitCode -eq 0) {
        Write-Host "✅ Script ejecutado exitosamente!" -ForegroundColor Green
    } else {
        Write-Host "❌ Error al ejecutar el script. Código de salida: $($process.ExitCode)" -ForegroundColor Red
    }
} catch {
    Write-Host "❌ Error al ejecutar MySQL: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host ""
    Write-Host "Instrucciones alternativas:" -ForegroundColor Yellow
    Write-Host "1. Abrir MySQL Workbench o cualquier cliente MySQL" -ForegroundColor White
    Write-Host "2. Conectarse a la base de datos 'ecommerce_db'" -ForegroundColor White
    Write-Host "3. Ejecutar el contenido del archivo 'datos_prueba_arte.sql'" -ForegroundColor White
}

Write-Host ""
Write-Host "Presiona cualquier tecla para continuar..." -ForegroundColor Cyan
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")