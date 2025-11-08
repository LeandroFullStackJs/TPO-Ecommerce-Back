-- Datos de prueba para E-commerce de Arte (basado en db.json del frontend)
-- Limpieza de datos existentes
DELETE FROM productos_categorias;
DELETE FROM productos;
DELETE FROM categorias;
DELETE FROM artistas;
DELETE FROM usuarios;

-- Usuarios de prueba (según db.json)
INSERT INTO usuarios (nombre, apellido, email, password, role) VALUES
('Admin', 'ArtGallery', 'admin@artGallery.com', '$2a$10$vQE.xKhHgcnQGu3zOPTxbO/C.IhQKb.1VLtRjW8gv5HqxV.zOy8FO', 'ADMIN'),
('Demo', 'User', 'demo@artGallery.com', '$2a$10$vQE.xKhHgcnQGu3zOPTxbO/C.IhQKb.1VLtRjW8gv5HqxV.zOy8FO', 'USER'),
('Delfina', 'ArtGallery', 'delfi@artGallery.com', '$2a$10$vQE.xKhHgcnQGu3zOPTxbO/C.IhQKb.1VLtRjW8gv5HqxV.zOy8FO', 'USER'),
('Magali', 'Brandt', 'mbrandt@uade.edu.ar', '$2a$10$vQE.xKhHgcnQGu3zOPTxbO/C.IhQKb.1VLtRjW8gv5HqxV.zOy8FO', 'USER'),
('Delfina Pilar', 'Brunstein', 'del@gmail.com', '$2a$10$vQE.xKhHgcnQGu3zOPTxbO/C.IhQKb.1VLtRjW8gv5HqxV.zOy8FO', 'USER');

-- Artistas (según db.json)
INSERT INTO artistas (nombre, biografia, imagen_perfil, email, activo, fecha_creacion) VALUES
('María González', 'Artista contemporánea con una trayectoria destacada en el mundo del arte abstracto. Su obra explora la interacción del color y la forma.', 
 'https://plus.unsplash.com/premium_photo-1682965455094-a913339699bd?q=80&w=687&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 
 'maria.gonzalez@artista.com', true, NOW()),

('Carlos Mendoza', 'Pintor paisajista conocido por sus representaciones oníricas de la naturaleza. Utiliza principalmente la técnica del óleo sobre lienzo.', 
 'https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=160&h=160&fit=crop&crop=faces', 
 'carlos.mendoza@artista.com', true, NOW()),

('Ana Silvestre', 'Especialista en retratos urbanos, Ana captura la esencia de la vida moderna con una expresividad única a través de la acuarela.', 
 'https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=160&h=160&fit=crop&crop=faces', 
 'ana.silvestre@artista.com', true, NOW()),

('Roberto Klein', 'Su trabajo se centra en la composición geométrica, explorando la armonía entre líneas, ángulos y espacios negativos con acrílico.', 
 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=160&h=160&fit=crop&crop=faces', 
 'roberto.klein@artista.com', true, NOW()),

('Isabella Torres', 'Con una paleta de colores intensos, Isabella ofrece una interpretación moderna de la naturaleza a través de formas orgánicas y fluidas.', 
 'https://images.unsplash.com/photo-1580489944761-15a19d654956?w=160&h=160&fit=crop&crop=faces', 
 'isabella.torres@artista.com', true, NOW()),

('Diego Vargas', 'Artista minimalista que encuentra la belleza en la simplicidad. Su obra se caracteriza por el uso del espacio negativo y la pureza de la forma.', 
 'https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=160&h=160&fit=crop&crop=faces', 
 'diego.vargas@artista.com', true, NOW()),

('Lucia Fernández', 'Lucia expresa el movimiento y la energía contemporánea a través de pinceladas dinámicas y una paleta de colores vibrantes.', 
 'https://images.unsplash.com/photo-1554151228-14d9def656e4?w=160&h=160&fit=crop&crop=faces', 
 'lucia.fernandez@artista.com', true, NOW()),

('Miguel Santos', 'Su obra es una exploración de texturas y relieves, invitando al espectador a una experiencia táctil y visual única.', 
 'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=160&h=160&fit=crop&crop=faces', 
 'miguel.santos@artista.com', true, NOW()),

('Lea', 'Artista emergente con un enfoque en el retrato y el arte abstracto, explorando nuevas formas de expresión.', 
 'https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=160&h=160&fit=crop&crop=faces', 
 'lea@artista.com', true, NOW());

-- Categorías de arte (según db.json)
INSERT INTO categorias (nombre) VALUES
('Arte Abstracto'),
('Paisajes'),
('Retratos'),
('Geométrico'),
('Naturaleza'),
('Minimalista'),
('Contemporáneo'),
('Texturizado');

-- Obras de arte (productos) basadas en db.json con artistaId y usuarioId
INSERT INTO productos (nombre_obra, descripcion, precio, stock, imagen, activo, destacado, 
                      artista, artista_id, usuario_id, tecnica, dimensiones, anio, estilo, fecha_creacion, fecha_actualizacion) VALUES

-- Productos del db.json adaptados
('Sueño de Paisaje', 'Paisaje onírico que captura la esencia de la naturaleza en un momento de tranquilidad absoluta. Pintura al óleo.', 
 40000.00, 1, 'https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=800&h=600&fit=crop&crop=center', 
 true, true, 'Carlos Mendoza', 2, 1, 'Óleo', '70x50 cm', 2023, 'Paisajista', NOW(), NOW()),

('Sinfonía Geométrica', 'Composición geométrica que explora la armonía entre líneas, ángulos y espacios negativos.', 
 41000.00, 2, 'https://images.unsplash.com/photo-1557804506-669a67965ba0?w=800&h=600&fit=crop&crop=center', 
 true, false, 'Roberto Klein', 4, 1, 'Acrílico', '80x80 cm', 2024, 'Moderno', NOW(), NOW()),

('Explosión Floral', 'Interpretación moderna de la naturaleza con colores intensos y formas orgánicas fluidas.', 
 35000.00, 1, 'https://images.unsplash.com/photo-1541961017774-22349e4a1262?w=800&h=600&fit=crop&crop=center', 
 true, false, 'Isabella Torres', 5, 1, 'Técnica mixta', '60x90 cm', 2023, null, NOW(), NOW()),

('Esencia Minimalista', 'Obra minimalista que encuentra la belleza en la simplicidad y el espacio negativo.', 
 48000.00, 5, 'https://images.unsplash.com/photo-1536924940846-227afb31e2a5?w=800&h=600&fit=crop&crop=center', 
 true, true, 'Diego Vargas', 6, 1, 'Óleo', '100x70 cm', 2024, null, NOW(), NOW()),

('Movimiento Contemporáneo', 'Expresión del movimiento y la energía contemporránea a través de pinceladas dinámicas.', 
 55000.00, 1, 'https://images.unsplash.com/photo-1549887534-1541e9326642?w=800&h=600&fit=crop&crop=center', 
 true, false, 'Lucia Fernández', 7, 1, 'Acrílico', '75x60 cm', 2024, 'Contemporáneo', NOW(), NOW()),

('Sueños Texturizados', 'Exploración de texturas y relieves que invitan al espectador a una experiencia táctil visual.', 
 42000.00, 1, 'https://images.unsplash.com/photo-1561049501-e1f4c5c59257?w=800&h=600&fit=crop&crop=center', 
 true, false, 'Miguel Santos', 8, 1, 'Técnica mixta con texturas', '65x85 cm', 2023, 'Moderno', NOW(), NOW()),

('Arte Abstracto Azul', 'Una descripción completa de esta obra abstracta con tonos azules dominantes.', 
 50000.00, 4, 'https://images.unsplash.com/photo-1539900492308-6bcbb7b54a19?q=80&w=1142&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 
 true, true, 'María González', 1, 1, 'Óleo', '60x80 cm', 2018, 'Abstracto', NOW(), NOW()),

('Paisaje Sereno', 'Un paisaje que transmite serenidad y paz interior.', 
 5000.00, 3, 'https://images.unsplash.com/photo-1757167301106-7c3bedf90ecd?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 
 true, false, 'María González', 1, 1, 'Óleo', '60x80 cm', 2017, null, NOW(), NOW()),

('Nueva Obra Minimalista', 'Descripción completa del producto minimalista moderno.', 
 12000.00, 2, 'https://plus.unsplash.com/premium_photo-1698001750831-dc8331d76fd8?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 
 true, false, 'María González', 1, 1, 'Óleo', '50x20 cm', 2019, null, NOW(), NOW()),

('Retrato Urbano', 'Arte urbano que refleja la vida de la ciudad.', 
 15000.00, 1, 'https://images.unsplash.com/photo-1731435265797-136ba0b95cda?q=80&w=876&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 
 true, false, 'Carlos Mendoza', 2, 1, 'Óleo', '50x20 cm', 2019, null, NOW(), NOW()),

('Obra Emergente', 'Arte para uade - obra de artista emergente.', 
 5600.00, 2, 'https://cdn.culturagenial.com/es/imagenes/arte-abstracto-og.jpg?class=ogImageWide', 
 true, false, 'Lea', 9, 3, 'Pincel', '60x60 cm', 2025, 'Abstracto', NOW(), NOW());

-- Asociaciones productos-categorías
-- Using subqueries to get the actual product IDs
INSERT INTO productos_categorias (producto_id, categoria_id) 
SELECT p.id, c.id 
FROM productos p, categorias c 
WHERE p.nombre_obra = 'Sueño de Paisaje' AND c.nombre = 'Paisajes';

INSERT INTO productos_categorias (producto_id, categoria_id) 
SELECT p.id, c.id 
FROM productos p, categorias c 
WHERE p.nombre_obra = 'Sinfonía Geométrica' AND c.nombre = 'Geométrico';

INSERT INTO productos_categorias (producto_id, categoria_id) 
SELECT p.id, c.id 
FROM productos p, categorias c 
WHERE p.nombre_obra = 'Explosión Floral' AND c.nombre = 'Naturaleza';

INSERT INTO productos_categorias (producto_id, categoria_id) 
SELECT p.id, c.id 
FROM productos p, categorias c 
WHERE p.nombre_obra = 'Esencia Minimalista' AND c.nombre = 'Minimalista';

INSERT INTO productos_categorias (producto_id, categoria_id) 
SELECT p.id, c.id 
FROM productos p, categorias c 
WHERE p.nombre_obra = 'Movimiento Contemporáneo' AND c.nombre = 'Contemporáneo';

INSERT INTO productos_categorias (producto_id, categoria_id) 
SELECT p.id, c.id 
FROM productos p, categorias c 
WHERE p.nombre_obra = 'Sueños Texturizados' AND c.nombre = 'Texturizado';

INSERT INTO productos_categorias (producto_id, categoria_id) 
SELECT p.id, c.id 
FROM productos p, categorias c 
WHERE p.nombre_obra = 'Arte Abstracto Azul' AND c.nombre = 'Arte Abstracto';

INSERT INTO productos_categorias (producto_id, categoria_id) 
SELECT p.id, c.id 
FROM productos p, categorias c 
WHERE p.nombre_obra = 'Paisaje Sereno' AND c.nombre = 'Paisajes';

INSERT INTO productos_categorias (producto_id, categoria_id) 
SELECT p.id, c.id 
FROM productos p, categorias c 
WHERE p.nombre_obra = 'Nueva Obra Minimalista' AND c.nombre = 'Minimalista';

INSERT INTO productos_categorias (producto_id, categoria_id) 
SELECT p.id, c.id 
FROM productos p, categorias c 
WHERE p.nombre_obra = 'Retrato Urbano' AND c.nombre = 'Retratos';

INSERT INTO productos_categorias (producto_id, categoria_id) 
SELECT p.id, c.id 
FROM productos p, categorias c 
WHERE p.nombre_obra = 'Obra Emergente' AND c.nombre = 'Arte Abstracto';

-- Consultas de verificación
SELECT 'Usuarios creados:' as info, COUNT(*) as cantidad FROM usuarios;
SELECT 'Artistas creados:' as info, COUNT(*) as cantidad FROM artistas;
SELECT 'Categorías creadas:' as info, COUNT(*) as cantidad FROM categorias;
SELECT 'Productos creados:' as info, COUNT(*) as cantidad FROM productos;
SELECT 'Asociaciones producto-categoría:' as info, COUNT(*) as cantidad FROM productos_categorias;

-- Mostrar productos con sus categorías y artistas
SELECT p.id, p.nombre_obra, p.artista, p.artista_id, p.usuario_id, p.precio, p.stock, p.destacado,
       p.tecnica, p.dimensiones, p.anio, p.estilo,
       GROUP_CONCAT(c.nombre SEPARATOR ', ') as categorias
FROM productos p
LEFT JOIN productos_categorias pc ON p.id = pc.producto_id
LEFT JOIN categorias c ON pc.categoria_id = c.id
GROUP BY p.id
ORDER BY p.destacado DESC, p.precio DESC;

-- Mostrar artistas
SELECT id, nombre, biografia, email, activo FROM artistas ORDER BY nombre;