-- ==========================================================
-- DATOS DE PRUEBA - ADAPTADOS AL NUEVO MODELO DE ENTIDADES
-- ==========================================================

-- 🔹 Limpieza de datos (para entorno de desarrollo)
DELETE FROM productos_categorias;
DELETE FROM productos;
DELETE FROM categorias;
DELETE FROM artistas;
DELETE FROM usuarios;

-- ==========================================================
-- USUARIOS
-- ==========================================================
INSERT INTO usuarios (nombre, apellido, email, password, role) VALUES
('Admin', 'ArtGallery', 'admin@artGallery.com', '$2a$10$vQE.xKhHgcnQGu3zOPTxbO/C.IhQKb.1VLtRjW8gv5HqxV.zOy8FO', 'ADMIN'),
('Demo', 'User', 'demo@artGallery.com', '$2a$10$vQE.xKhHgcnQGu3zOPTxbO/C.IhQKb.1VLtRjW8gv5HqxV.zOy8FO', 'USER'),
('Delfina', 'ArtGallery', 'delfi@artGallery.com', '$2a$10$vQE.xKhHgcnQGu3zOPTxbO/C.IhQKb.1VLtRjW8gv5HqxV.zOy8FO', 'USER');

-- ==========================================================
-- ARTISTAS
-- ==========================================================
INSERT INTO artistas (nombre, biografia, imagen_perfil, email, activo, fecha_creacion) VALUES
('María González', 'Artista contemporánea destacada en arte abstracto.', 
 'https://plus.unsplash.com/premium_photo-1682965455094-a913339699bd?q=80&w=687&auto=format&fit=crop&ixlib=rb-4.1.0', 
 'maria.gonzalez@artista.com', true, NOW()),
('Carlos Mendoza', 'Pintor paisajista conocido por sus representaciones oníricas.', 
 'https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=160&h=160&fit=crop&crop=faces', 
 'carlos.mendoza@artista.com', true, NOW()),
('Frida Kahlo', 'Artista mexicana ícono del surrealismo y el autorretrato.', 
 'https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=160&h=160&fit=crop&crop=faces', 
 'frida.kahlo@artista.com', true, NOW());

-- ==========================================================
-- CATEGORÍAS
-- ==========================================================
INSERT INTO categorias (nombre) VALUES
('Arte Abstracto'),
('Paisajes'),
('Retratos'),
('Geométrico'),
('Naturaleza'),
('Minimalista'),
('Contemporáneo'),
('Texturizado');

-- ==========================================================
-- PRODUCTOS (OBRAS)
-- ==========================================================
INSERT INTO productos (
    nombre_obra, descripcion, precio, stock, imagen,
    activo, destacado, fecha_creacion, fecha_actualizacion,
    artista, artista_id, usuario_id, tecnica, dimensiones, anio, estilo
) VALUES
('Sueño de Paisaje', 'Paisaje onírico que captura la esencia de la naturaleza.', 
 40000.00, 1, 'https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=800&h=600&fit=crop',
 true, true, NOW(), NOW(), 'Carlos Mendoza', 2, 1, 'Óleo', '70x50 cm', 2023, null),

('Sinfonía Geométrica', 'Composición geométrica que explora la armonía entre líneas y ángulos.', 
 41000.00, 2, 'https://images.unsplash.com/photo-1557804506-669a67965ba0?w=800&h=600&fit=crop',
 true, false, NOW(), NOW(), 'Frida Kahlo', 3, 1, 'Acrílico', '80x80 cm', 2024, 'Moderno'),

('Arte Abstracto Azul', 'Obra abstracta con predominio de tonos azules.', 
 50000.00, 4, 'https://images.unsplash.com/photo-1539900492308-6bcbb7b54a19?q=80&w=1142&auto=format&fit=crop',
 true, true, NOW(), NOW(), 'María González', 1, 1, 'Óleo', '60x80 cm', 2018, 'Abstracto');

-- ==========================================================
-- RELACIONES PRODUCTO - CATEGORÍA
-- ==========================================================
INSERT INTO productos_categorias (producto_id, categoria_id)
SELECT p.id, c.id FROM productos p, categorias c
WHERE p.nombre_obra = 'Sueño de Paisaje' AND c.nombre = 'Paisajes';

INSERT INTO productos_categorias (producto_id, categoria_id)
SELECT p.id, c.id FROM productos p, categorias c
WHERE p.nombre_obra = 'Sinfonía Geométrica' AND c.nombre = 'Geométrico';

INSERT INTO productos_categorias (producto_id, categoria_id)
SELECT p.id, c.id FROM productos p, categorias c
WHERE p.nombre_obra = 'Arte Abstracto Azul' AND c.nombre = 'Arte Abstracto';
