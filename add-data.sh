#!/bin/bash
# Script para agregar datos usando curl

echo "🚀 AGREGANDO DATOS DE EJEMPLO AL E-COMMERCE"

# 1. Agregar Categorías
echo "📂 Agregando categorías..."
curl -X POST http://localhost:8080/api/categorias -H "Content-Type: application/json" -d '{"nombre":"Electrónicos"}'
curl -X POST http://localhost:8080/api/categorias -H "Content-Type: application/json" -d '{"nombre":"Ropa"}'
curl -X POST http://localhost:8080/api/categorias -H "Content-Type: application/json" -d '{"nombre":"Hogar"}'

# 2. Agregar Usuarios
echo "👤 Agregando usuarios..."
curl -X POST http://localhost:8080/api/usuarios -H "Content-Type: application/json" -d '{"nombre":"Juan","apellido":"Pérez","email":"juan@email.com"}'
curl -X POST http://localhost:8080/api/usuarios -H "Content-Type: application/json" -d '{"nombre":"María","apellido":"González","email":"maria@email.com"}'

# 3. Agregar Productos
echo "📦 Agregando productos..."
curl -X POST http://localhost:8080/api/productos -H "Content-Type: application/json" -d '{"nombre":"iPhone 15","descripcion":"Smartphone Apple","precio":1299.99,"stock":25}'
curl -X POST http://localhost:8080/api/productos -H "Content-Type: application/json" -d '{"nombre":"Camiseta Nike","descripcion":"Camiseta deportiva","precio":29.99,"stock":100}'

# 4. Agregar Direcciones
echo "🏠 Agregando direcciones..."
curl -X POST http://localhost:8080/api/direcciones -H "Content-Type: application/json" -d '{"calle":"Av. Corrientes","numero":"1234","localidad":"Buenos Aires","provincia":"CABA","pais":"Argentina"}'

echo "✅ Datos agregados exitosamente!"