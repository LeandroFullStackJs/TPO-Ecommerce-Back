-- Script SQL para cargar datos completos de arte en e-commerce
-- Ejecutar después de que el servidor haya creado las tablas

USE ecommerce_db;

-- Limpiar datos existentes (en orden para evitar conflictos de FK)
SET FOREIGN_KEY_CHECKS = 0;
DELETE FROM productos_categorias;
DELETE FROM pedido_items;
DELETE FROM pedidos;
DELETE FROM productos;
DELETE FROM direcciones;
DELETE FROM artistas;
DELETE FROM categorias;
DELETE FROM usuarios;
SET FOREIGN_KEY_CHECKS = 1;

-- Reiniciar AUTO_INCREMENT
ALTER TABLE categorias AUTO_INCREMENT = 1;
ALTER TABLE artistas AUTO_INCREMENT = 1;
ALTER TABLE productos AUTO_INCREMENT = 1;
ALTER TABLE usuarios AUTO_INCREMENT = 1;
ALTER TABLE direcciones AUTO_INCREMENT = 1;
ALTER TABLE pedidos AUTO_INCREMENT = 1;
ALTER TABLE pedido_items AUTO_INCREMENT = 1;

-- =====================================================
-- 1. INSERTAR CATEGORÍAS
-- =====================================================
INSERT INTO categorias (nombre, descripcion, activa, orden, imagen_icono) VALUES
('Pintura', 'Obras pictóricas tradicionales y contemporáneas', 1, 1, 'https://images.unsplash.com/photo-1578662996442-48f60103fc96?w=64&h=64&fit=crop&crop=center'),
('Escultura', 'Arte tridimensional en diversos materiales', 1, 2, 'https://images.unsplash.com/photo-1594736797933-d0ca9c65b53c?w=64&h=64&fit=crop&crop=center'),
('Fotografía', 'Arte fotográfico analógico y digital', 1, 3, 'https://images.unsplash.com/photo-1606983340126-99ab4feaa64a?w=64&h=64&fit=crop&crop=center'),
('Arte Digital', 'Creaciones artísticas generadas digitalmente', 1, 4, 'https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=64&h=64&fit=crop&crop=center'),
('Arte Abstracto', 'Expresiones artísticas no figurativas', 1, 5, 'https://images.unsplash.com/photo-1541961017774-22349e4a1262?w=64&h=64&fit=crop&crop=center'),
('Realismo', 'Representaciones fieles de la realidad', 1, 6, 'https://images.unsplash.com/photo-1578321272176-b7bbc0679853?w=64&h=64&fit=crop&crop=center'),
('Impresionismo', 'Estilo artístico con énfasis en la luz', 1, 7, 'https://images.unsplash.com/photo-1578662996442-48f60103fc96?w=64&h=64&fit=crop&crop=center'),
('Arte Contemporáneo', 'Expresiones artísticas actuales', 1, 8, 'https://images.unsplash.com/photo-1536924430914-91f9e2041b83?w=64&h=64&fit=crop&crop=center');

-- =====================================================
-- 2. INSERTAR ARTISTAS
-- =====================================================
INSERT INTO artistas (nombre, biografia, email, imagen_perfil, activo, fecha_creacion, fecha_actualizacion) VALUES
('Carlos Mendoza', 
 'Artista contemporáneo especializado en pintura abstracta. Sus obras reflejan la complejidad de las emociones humanas a través de colores vibrantes y formas orgánicas. Con más de 10 años de experiencia, ha expuesto en galerías de Buenos Aires, Madrid y Nueva York.',
 'carlos.mendoza@arte.com',
 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150&h=150&fit=crop&crop=face',
 1, NOW(), NOW()),

('María García', 
 'Pintora expresionista con más de 15 años de experiencia. Su trabajo explora la relación entre música y color, creando sinestesias visuales únicas. Graduada de la Academia de Bellas Artes, es reconocida por su técnica innovadora.',
 'maria.garcia@arte.com',
 'https://images.unsplash.com/photo-1494790108755-2616c6b14068?w=150&h=150&fit=crop&crop=face',
 1, NOW(), NOW()),

('Ana Rodríguez', 
 'Fotógrafa documentalista especializada en fotografía de naturaleza. Sus imágenes capturan la belleza efímera de los momentos naturales. Ha ganado múltiples premios internacionales y sus obras están en colecciones privadas.',
 'ana.rodriguez@arte.com',
 'https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=150&h=150&fit=crop&crop=face',
 1, NOW(), NOW()),

('Diego Silva', 
 'Artista digital pionero en el uso de inteligencia artificial para la creación artística. Combina tecnología y creatividad tradicional para crear obras que desafían los límites entre lo real y lo virtual.',
 'diego.silva@arte.com',
 'https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=150&h=150&fit=crop&crop=face',
 1, NOW(), NOW()),

('Elena Vásquez',
 'Escultora contemporánea especializada en bronce y materiales mixtos. Sus obras exploran temas de identidad y memoria colectiva. Ha participado en bienales internacionales y tiene obras en espacios públicos.',
 'elena.vasquez@arte.com',
 'https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=150&h=150&fit=crop&crop=face',
 1, NOW(), NOW());

-- =====================================================
-- 3. INSERTAR USUARIOS (para que los productos tengan un creador)
-- =====================================================
-- CONTRASEÑAS SIMPLES (BCrypt de "password"):
-- admin@arte.com: password = "Admin123@"
-- galeria@arte.com: password = "Admin123@"
-- Hash BCrypt de "Admin123@": $2a$10$tRk/HcIcyd15alHbuTvFcexfweu1v9I5q6.Le4XsqSzmhbZaK/cj.
INSERT INTO usuarios (nombre, apellido, email, password, role) VALUES
('Admin', 'Sistema', 'admin@arte.com', '$2a$10$tRk/HcIcyd15alHbuTvFcexfweu1v9I5q6.Le4XsqSzmhbZaK/cj.', 'ADMIN'),
('Galería', 'Arte Moderno', 'galeria@arte.com', '$2a$10$tRk/HcIcyd15alHbuTvFcexfweu1v9I5q6.Le4XsqSzmhbZaK/cj.', 'USER');

-- =====================================================
-- 4. INSERTAR PRODUCTOS (OBRAS DE ARTE)
-- =====================================================
INSERT INTO productos (
    nombre_obra, descripcion, precio, stock, imagen, artista, tecnica, 
    dimensiones, anio, estilo, destacado, activo, artista_id, usuario_id,
    fecha_creacion, fecha_actualizacion
) VALUES

-- Obras de Carlos Mendoza
('Sinfonía de Colores',
 'Una explosión de colores que representa la complejidad de las emociones humanas. Cada pincelada cuenta una historia diferente, creando un diálogo visual entre la alegría y la melancolía.',
 1500.00, 1,
 'https://images.unsplash.com/photo-1541961017774-22349e4a1262?w=400&h=500&fit=crop',
 'Carlos Mendoza', 'Óleo sobre lienzo',
 '80cm x 100cm', 2024, 'Abstracto',
 1, 1, 1, 1, NOW(), NOW()),

('Formas en Movimiento',
 'Escultura dinámica que parece cambiar según el ángulo de observación, representando la fluidez del tiempo y la percepción. Una pieza que invita a la contemplación activa.',
 3500.00, 1,
 'https://images.unsplash.com/photo-1594736797933-d0ca9c65b53c?w=400&h=500&fit=crop',
 'Carlos Mendoza', 'Bronce patinado',
 '150cm x 80cm x 60cm', 2024, 'Contemporáneo',
 1, 1, 1, 1, NOW(), NOW()),

-- Obras de María García
('Melodía Visual',
 'Pintura que traduce la música clásica en colores y formas. Cada pincelada representa una nota musical, creando una sinfonía visual que puede escucharse con los ojos.',
 2200.00, 1,
 'https://images.unsplash.com/photo-1578662996442-48f60103fc96?w=400&h=500&fit=crop',
 'María García', 'Acrílico sobre lienzo',
 '100cm x 120cm', 2023, 'Expresionista',
 0, 1, 2, 1, NOW(), NOW()),

('Danza de Luces',
 'Una obra que captura el movimiento y la energía de la danza a través de trazos luminosos y colores vibrantes. La técnica mixta permite jugar con texturas y profundidad.',
 1800.00, 2,
 'https://images.unsplash.com/photo-1536924430914-91f9e2041b83?w=400&h=500&fit=crop',
 'María García', 'Técnica mixta sobre lienzo',
 '90cm x 110cm', 2023, 'Expresionista',
 1, 1, 2, 1, NOW(), NOW()),

-- Obras de Ana Rodríguez
('Bosque Susurrante',
 'Fotografía capturada en el momento perfecto donde la luz se filtra entre los árboles creando un ambiente mágico. La naturaleza revela sus secretos más íntimos.',
 800.00, 3,
 'https://images.unsplash.com/photo-1441974231531-c6227db76b6e?w=400&h=500&fit=crop',
 'Ana Rodríguez', 'Fotografía digital',
 '60cm x 90cm', 2023, 'Naturalismo',
 0, 1, 3, 1, NOW(), NOW()),

('Reflejos del Alma',
 'Fotografía en blanco y negro que captura la esencia humana a través de juegos de luz y sombra. Una exploración profunda de la condición humana.',
 950.00, 4,
 'https://images.unsplash.com/photo-1568605117036-5fe5e7bab0b7?w=400&h=500&fit=crop',
 'Ana Rodríguez', 'Fotografía analógica',
 '70cm x 100cm', 2023, 'Documental',
 0, 1, 3, 1, NOW(), NOW()),

('Horizontes Infinitos',
 'Paisaje que captura la inmensidad del horizonte marino al amanecer. La técnica de larga exposición crea un efecto etéreo y contemplativo.',
 1100.00, 2,
 'https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=400&h=500&fit=crop',
 'Ana Rodríguez', 'Fotografía de larga exposición',
 '80cm x 120cm', 2024, 'Paisajismo',
 1, 1, 3, 1, NOW(), NOW()),

-- Obras de Diego Silva
('Visiones Digitales',
 'Obra digital que explora las posibilidades infinitas del arte generado por computadora, fusionando realidad y fantasía en un universo paralelo de colores y formas.',
 1200.00, 2,
 'https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=400&h=500&fit=crop',
 'Diego Silva', 'Arte digital',
 '50cm x 70cm (impresión)', 2024, 'Digital',
 1, 1, 4, 1, NOW(), NOW()),

('Código Poético',
 'Una exploración de la belleza encontrada en algoritmos y código. Esta pieza digital convierte líneas de programación en una experiencia visual poética.',
 900.00, 5,
 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=400&h=500&fit=crop',
 'Diego Silva', 'Arte generativo',
 '40cm x 60cm (impresión)', 2024, 'Digital',
 0, 1, 4, 1, NOW(), NOW()),

-- Obras de Elena Vásquez
('Memoria Fragmentada',
 'Escultura que explora los recuerdos fragmentados de la infancia. Cada pieza de bronce representa un momento perdido en el tiempo.',
 2800.00, 1,
 'https://images.unsplash.com/photo-1578321272176-b7bbc0679853?w=400&h=500&fit=crop',
 'Elena Vásquez', 'Bronce y acero',
 '120cm x 70cm x 50cm', 2023, 'Contemporáneo',
 1, 1, 5, 1, NOW(), NOW()),

('Identidad Fluida',
 'Una obra que cuestiona las nociones fijas de identidad en la era moderna. Materiales mixtos que se transforman según la perspectiva del observador.',
 2400.00, 1,
 'https://images.unsplash.com/photo-1551698618-1dfe5d97d256?w=400&h=500&fit=crop',
 'Elena Vásquez', 'Materiales mixtos',
 '100cm x 90cm x 40cm', 2024, 'Conceptual',
 0, 1, 5, 1, NOW(), NOW());

-- =====================================================
-- 5. INSERTAR RELACIONES PRODUCTOS-CATEGORÍAS
-- =====================================================
INSERT INTO productos_categorias (producto_id, categoria_id) VALUES
-- Sinfonía de Colores: Pintura, Arte Abstracto, Arte Contemporáneo
(1, 1), (1, 5), (1, 8),
-- Formas en Movimiento: Escultura, Arte Contemporáneo
(2, 2), (2, 8),
-- Melodía Visual: Pintura, Arte Contemporáneo
(3, 1), (3, 8),
-- Danza de Luces: Pintura, Arte Contemporáneo
(4, 1), (4, 8),
-- Bosque Susurrante: Fotografía, Arte Contemporáneo
(5, 3), (5, 8),
-- Reflejos del Alma: Fotografía, Arte Contemporáneo
(6, 3), (6, 8),
-- Horizontes Infinitos: Fotografía, Arte Contemporáneo
(7, 3), (7, 8),
-- Visiones Digitales: Arte Digital, Arte Contemporáneo
(8, 4), (8, 8),
-- Código Poético: Arte Digital, Arte Contemporáneo
(9, 4), (9, 8),
-- Memoria Fragmentada: Escultura, Arte Contemporáneo
(10, 2), (10, 8),
-- Identidad Fluida: Escultura, Arte Contemporáneo
(11, 2), (11, 8);

-- =====================================================
-- 6. VERIFICACIÓN DE DATOS
-- =====================================================
-- Mostrar resumen de datos insertados
SELECT 'CATEGORÍAS' as TABLA, COUNT(*) as CANTIDAD FROM categorias
UNION ALL
SELECT 'ARTISTAS' as TABLA, COUNT(*) as CANTIDAD FROM artistas
UNION ALL
SELECT 'USUARIOS' as TABLA, COUNT(*) as CANTIDAD FROM usuarios
UNION ALL
SELECT 'PRODUCTOS' as TABLA, COUNT(*) as CANTIDAD FROM productos
UNION ALL
SELECT 'RELACIONES PRODUCTOS-CATEGORÍAS' as TABLA, COUNT(*) as CANTIDAD FROM productos_categorias;

-- Mostrar productos con sus categorías
SELECT 
    p.id,
    p.nombre_obra,
    p.artista,
    p.precio,
    p.destacado,
    GROUP_CONCAT(c.nombre SEPARATOR ', ') as categorias
FROM productos p
LEFT JOIN productos_categorias pc ON p.id = pc.producto_id
LEFT JOIN categorias c ON pc.categoria_id = c.id
GROUP BY p.id, p.nombre_obra, p.artista, p.precio, p.destacado
ORDER BY p.id;

-- =====================================================
-- SCRIPT COMPLETADO EXITOSAMENTE
-- =====================================================
-- Para usar este script:
-- 1. Asegúrate de que el servidor Spring Boot esté ejecutándose
-- 2. Abre MySQL Workbench o tu cliente MySQL preferido
-- 3. Conecta a la base de datos 'ecommerce_db'
-- 4. Ejecuta este script completo
-- 5. Verifica los datos con: SELECT * FROM productos;
-- 6. Prueba la API: GET http://localhost:8080/productos