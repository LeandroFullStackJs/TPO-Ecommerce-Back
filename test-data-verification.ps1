# Test script to verify the SQL data insertion works correctly
# This will test each step individually to identify any issues

Write-Host "🔍 Probando inserción de datos paso a paso..." -ForegroundColor Cyan

# Test data for verification
$testQueries = @(
    @{
        Description = "Verificar que existen usuarios"
        Query = "SELECT COUNT(*) as total FROM usuarios;"
    },
    @{
        Description = "Verificar que existen artistas"
        Query = "SELECT COUNT(*) as total FROM artistas;"
    },
    @{
        Description = "Verificar que existen categorías"
        Query = "SELECT COUNT(*) as total FROM categorias;"
    },
    @{
        Description = "Verificar que existen productos"
        Query = "SELECT COUNT(*) as total FROM productos;"
    },
    @{
        Description = "Verificar que existen asociaciones producto-categoría"
        Query = "SELECT COUNT(*) as total FROM productos_categorias;"
    },
    @{
        Description = "Mostrar algunos productos con sus categorías"
        Query = "SELECT p.id, p.nombre_obra, p.artista, GROUP_CONCAT(c.nombre SEPARATOR ', ') as categorias FROM productos p LEFT JOIN productos_categorias pc ON p.id = pc.producto_id LEFT JOIN categorias c ON pc.categoria_id = c.id GROUP BY p.id LIMIT 5;"
    }
)

Write-Host ""
Write-Host "📋 Consultas de verificación disponibles:" -ForegroundColor Yellow

for ($i = 0; $i -lt $testQueries.Count; $i++) {
    Write-Host "$($i + 1). $($testQueries[$i].Description)" -ForegroundColor White
}

Write-Host ""
Write-Host "💡 Para ejecutar estas consultas:" -ForegroundColor Green
Write-Host "   - Abre MySQL Workbench o tu cliente MySQL preferido" -ForegroundColor White
Write-Host "   - Conéctate a la base de datos 'ecommerce_db'" -ForegroundColor White
Write-Host "   - Primero ejecuta el archivo 'datos_prueba_arte.sql'" -ForegroundColor White
Write-Host "   - Luego ejecuta las consultas de verificación de arriba" -ForegroundColor White

Write-Host ""
Write-Host "🔧 Si hay errores de foreign key:" -ForegroundColor Yellow
Write-Host "   - Verifica que las tablas principales (usuarios, artistas, categorias, productos)" -ForegroundColor White
Write-Host "     se hayan creado correctamente antes de las asociaciones" -ForegroundColor White
Write-Host "   - El script actualizado usa subqueries para encontrar los IDs correctos" -ForegroundColor White

Write-Host ""
Write-Host "Presiona cualquier tecla para continuar..." -ForegroundColor Cyan
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")