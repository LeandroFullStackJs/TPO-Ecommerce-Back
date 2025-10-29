-- Script de prueba simplificado para insertar datos paso a paso
-- Ejecutar cada sección por separado para identificar problemas

-- Paso 1: Limpiar datos existentes
DELETE FROM productos_categorias;
DELETE FROM productos;
DELETE FROM categorias;
DELETE FROM artistas;
DELETE FROM usuarios;

SELECT '=== PASO 1: Datos limpiados ===' as status;

-- Paso 2: Insertar usuarios
INSERT INTO usuarios (nombre, apellido, email, password, role) VALUES
('Admin', 'ArtGallery', 'admin@artgallery.com', '$2a$10$vQE.xKhHgcnQGu3zOPTxbO/C.IhQKb.1VLtRjW8gv5HqxV.zOy8FO', 'ADMIN'),
('Demo', 'User', 'demo@artgallery.com', '$2a$10$vQE.xKhHgcnQGu3zOPTxbO/C.IhQKb.1VLtRjW8gv5HqxV.zOy8FO', 'USER');

SELECT '=== PASO 2: Usuarios insertados ===' as status;
SELECT COUNT(*) as usuarios_count FROM usuarios;

-- Paso 3: Insertar artistas
INSERT INTO artistas (nombre, biografia, email, activo, fecha_creacion) VALUES
('María González', 'Artista contemporánea especializada en arte abstracto.', 'maria.gonzalez@artista.com', true, NOW()),
('Carlos Mendoza', 'Pintor paisajista conocido por sus representaciones oníricas.', 'carlos.mendoza@artista.com', true, NOW());

SELECT '=== PASO 3: Artistas insertados ===' as status;
SELECT COUNT(*) as artistas_count FROM artistas;

-- Paso 4: Insertar categorías
INSERT INTO categorias (nombre) VALUES
('Arte Abstracto'),
('Paisajes'),
('Retratos');

SELECT '=== PASO 4: Categorías insertadas ===' as status;
SELECT COUNT(*) as categorias_count FROM categorias;

-- Paso 5: Insertar productos
INSERT INTO productos (nombre_obra, descripcion, precio, stock, imagen, activo, destacado, artista, artista_id, usuario_id, tecnica, dimensiones, anio, fecha_creacion, fecha_actualizacion) VALUES
('Arte Abstracto Azul', 'Una obra abstracta con tonos azules dominantes.', 50000.00, 4, 'https://example.com/image1.jpg', true, true, 'María González', 1, 1, 'Óleo', '60x80 cm', 2018, NOW(), NOW()),
('Paisaje Sereno', 'Un paisaje que transmite serenidad y paz interior.', 35000.00, 3, 'https://example.com/image2.jpg', true, false, 'Carlos Mendoza', 2, 1, 'Óleo', '70x50 cm', 2019, NOW(), NOW());

SELECT '=== PASO 5: Productos insertados ===' as status;
SELECT COUNT(*) as productos_count FROM productos;
SELECT id, nombre_obra, artista FROM productos;

-- Paso 6: Insertar asociaciones productos-categorías usando subqueries
INSERT INTO productos_categorias (producto_id, categoria_id) 
SELECT p.id, c.id 
FROM productos p, categorias c 
WHERE p.nombre_obra = 'Arte Abstracto Azul' AND c.nombre = 'Arte Abstracto';

INSERT INTO productos_categorias (producto_id, categoria_id) 
SELECT p.id, c.id 
FROM productos p, categorias c 
WHERE p.nombre_obra = 'Paisaje Sereno' AND c.nombre = 'Paisajes';

SELECT '=== PASO 6: Asociaciones insertadas ===' as status;
SELECT COUNT(*) as asociaciones_count FROM productos_categorias;

-- Verificación final
SELECT '=== VERIFICACIÓN FINAL ===' as status;
SELECT 
    p.id, 
    p.nombre_obra, 
    p.artista, 
    p.precio,
    GROUP_CONCAT(c.nombre SEPARATOR ', ') as categorias
FROM productos p
LEFT JOIN productos_categorias pc ON p.id = pc.producto_id
LEFT JOIN categorias c ON pc.categoria_id = c.id
GROUP BY p.id
ORDER BY p.id;