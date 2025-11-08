# 🎨 E-Commerce de Arte - Backend API

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen.svg)
![Java](https://img.shields.io/badge/Java-17-orange.svg)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)
![Docker](https://img.shields.io/badge/Docker-Ready-2496ED.svg)
![Maven](https://img.shields.io/badge/Maven-3.6+-red.svg)
![JWT](https://img.shields.io/badge/JWT-Security-yellow.svg)

Una API REST completa para un **E-Commerce de Arte** construida con **Spring Boot**, que incluye autenticación JWT, gestión de obras de arte, artistas, categorías, usuarios, pedidos y un sistema robusto de manejo de excepciones.

## ✨ Características Principales

- 🎨 **Gestión completa de obras de arte** con imágenes, artistas y categorías
- 🔐 **Autenticación JWT** con roles de usuario (ADMIN/USER)
- � **Docker Ready** - Configuración completa con Docker Compose
- 🗃️ **Base de datos completa** con datos de prueba incluidos
- 🌐 **CORS configurado** para desarrollo frontend
- 📦 **Stock management** para control de inventario
- 🔄 **Proxy de imágenes** integrado
- 📊 **Productos destacados** y filtros por categorías
- 🛡️ **Manejo robusto de excepciones** y validaciones

## 🛠️ Tecnologías

- **Java 17**
- **Spring Boot 3.5.7**
- **Spring Security + JWT**
- **Spring Data JPA**
- **MySQL 8.0**
- **Docker & Docker Compose**
- **Maven**
- **Hibernate**

## � Inicio Rápido

### Prerrequisitos
- **Docker Desktop** instalado y ejecutándose
- **Git** para clonar el repositorio

### 1. Clonar el Repositorio
```bash
git clone https://github.com/LeandroFullStackJs/TPO-Ecommerce-Back.git
cd TPO-Ecommerce-Back
```

### 2. Levantar con Docker (Recomendado)
```powershell
# Script automático de inicio
.\inicio-rapido.ps1

# O manualmente:
docker-compose up -d
```

### 3. Cargar Datos de Prueba
```powershell
# Script automático para cargar datos
.\cargar-datos.ps1
```

### 4. Verificar Funcionamiento
```bash
# Probar API
curl http://localhost:8080/api/productos

# Ver productos destacados
curl http://localhost:8080/api/productos/destacados
```

## 📋 Tabla de Contenidos

- [🚀 Características](#-características-principales)
- [🛠️ Tecnologías](#️-tecnologías)
- [🔌 Endpoints de la API](#-endpoints-de-la-api)
- [🛡️ Seguridad](#️-seguridad)
- [🎯 Testing](#-testing)
- [📊 Base de Datos](#-base-de-datos)
- [🔧 Scripts Utilitarios](#-scripts-utilitarios)
- [📖 Documentación](#-documentación)
- [🤝 Contribuir](#-contribuir)

## 🚀 Características

### ✅ **Funcionalidades Principales**
- 🔐 **Autenticación y Autorización JWT**
- 👤 **Gestión completa de usuarios**
- 📦 **CRUD de productos con categorías**
- 🎨 **Sistema de artistas**
- 🛒 **Gestión de pedidos y carritos**
- 📍 **Sistema de direcciones de envío**
- 🏷️ **Categorización de productos**

### ✅ **Características Técnicas**
- 🛡️ **Spring Security con JWT**
- 🔄 **CORS configurado para frontend**
- ⚡ **Manejo global de excepciones**
- 📝 **Validaciones personalizadas**
- 🔍 **Búsquedas y filtros avanzados**
- 📊 **Logging y monitoreo**
- 🎯 **Endpoints de testing**

## 🛠️ Tecnologías

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **Spring Boot** | 3.5.6 | Framework principal |
| **Spring Security** | 6.x | Seguridad y autenticación |
| **Spring Data JPA** | 3.x | Persistencia de datos |
| **Java** | 17+ | Lenguaje de programación |
| **MySQL** | 5.5+ | Base de datos principal |
| **H2** | 2.x | Base de datos para testing |
| **JWT (jjwt)** | 0.11.5 | Tokens de autenticación |
| **Lombok** | 1.18.x | Reducción de boilerplate |
| **Maven** | 3.6+ | Gestión de dependencias |
| **Hibernate** | 6.x | ORM |

## 🎨 Datos de Prueba Incluidos

El proyecto incluye un conjunto completo de datos de prueba para un e-commerce de arte:

### � Contenido de la Base de Datos:
- **8 Categorías** de arte (Pintura, Escultura, Fotografía, Arte Digital, etc.)
- **5 Artistas** con biografías completas e imágenes de perfil
- **11 Obras de arte** con descripciones detalladas, precios e imágenes
- **2 Usuarios** de prueba (Admin y Usuario regular)
- **6 Productos destacados** para la página principal

### 🔐 Usuarios de Prueba:
```json
{
  "admin": {
    "email": "admin@arte.com",
    "password": "Admin123@",
    "role": "ADMIN"
  },
  "usuario": {
    "email": "galeria@arte.com",
    "password": "Admin123@", 
    "role": "USER"
  }
}
```

### 🎯 Ejemplos de Productos:
- **"Sinfonía de Colores"** - Carlos Mendoza - $1,500 (Destacado)
- **"Formas en Movimiento"** - Carlos Mendoza - $3,500 (Escultura)
- **"Melodía Visual"** - María García - $2,200 (Expresionista)
- **"Visiones Digitales"** - Diego Silva - $1,200 (Arte Digital)
- Y más...

## 📦 Instalación
- ☕ Java 17 o superior
- 🐬 MySQL 5.5 o superior
- 📦 Maven 3.6 o superior
- 🔧 Git

### Clonar el Repositorio
```bash
git clone https://github.com/LeandroFullStackJs/TPO-Ecommerce-Back.git
cd TPO-Ecommerce-Back
```

### Instalación Automática
```bash
# Windows
.\mvnw.cmd clean install

# Linux/Mac
./mvnw clean install
```

## ⚙️ Configuración

### 1. Base de Datos MySQL
Crear la base de datos:
```sql
CREATE DATABASE ecommerce_db;
```

### 2. Configuración de Aplicación
Editar `src/main/resources/application.properties`:

```properties
# MySQL Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password

# JWT Configuration
jwt.secret=tu_clave_secreta_super_segura_de_al_menos_256_bits
jwt.expiration=86400000

# Server Configuration
server.port=8080
```

### 3. Variables de Entorno (Recomendado)
```bash
export DB_USERNAME=tu_usuario
export DB_PASSWORD=tu_password
export JWT_SECRET=tu_clave_secreta
```

### 4. Ejecutar la Aplicación
```bash
# Desarrollo
.\mvnw.cmd spring-boot:run

# Producción
java -jar target/e_commerce-0.0.1-SNAPSHOT.jar
```

### 5. Poblar Datos de Prueba
```bash
# Windows
.\agregar-datos.ps1

# Linux/Mac (con PowerShell Core)
pwsh -File agregar-datos.ps1
```

## 🔌 Endpoints de la API

### 🔐 **Autenticación**
```http
POST /api/auth/register     # Registro de usuarios
POST /api/auth/login        # Inicio de sesión
POST /api/auth/refresh      # Renovar token
```

### 👤 **Usuarios**
```http
GET    /api/usuarios        # Listar usuarios (ADMIN)
GET    /api/usuarios/{id}   # Obtener usuario
PUT    /api/usuarios/{id}   # Actualizar usuario
DELETE /api/usuarios/{id}   # Eliminar usuario (ADMIN)
```

### 📦 **Productos**
```http
GET    /api/productos                      # Listar productos
GET    /api/productos/{id}                 # Obtener producto
GET    /api/productos/buscar?q={query}     # Buscar productos
GET    /api/productos/destacados           # Productos destacados
GET    /api/productos/categoria/{id}       # Por categoría
POST   /api/productos                      # Crear producto (ADMIN)
PUT    /api/productos/{id}                 # Actualizar producto (ADMIN)
DELETE /api/productos/{id}                 # Eliminar producto (ADMIN)
```

### 🏷️ **Categorías**
```http
GET    /api/categorias      # Listar categorías
GET    /api/categorias/{id} # Obtener categoría
POST   /api/categorias      # Crear categoría (ADMIN)
PUT    /api/categorias/{id} # Actualizar categoría (ADMIN)
DELETE /api/categorias/{id} # Eliminar categoría (ADMIN)
```

### 🎨 **Artistas**
```http
GET    /api/artistas        # Listar artistas
GET    /api/artistas/{id}   # Obtener artista
GET    /api/artistas/buscar # Buscar artistas
POST   /api/artistas        # Crear artista (ADMIN)
PUT    /api/artistas/{id}   # Actualizar artista (ADMIN)
DELETE /api/artistas/{id}   # Eliminar artista (ADMIN)
```

### 🛒 **Pedidos**
```http
GET    /api/pedidos         # Listar pedidos del usuario
GET    /api/pedidos/{id}    # Obtener pedido específico
POST   /api/pedidos         # Crear nuevo pedido
PUT    /api/pedidos/{id}    # Actualizar estado (ADMIN)
DELETE /api/pedidos/{id}    # Cancelar pedido
```

### 📍 **Direcciones**
```http
GET    /api/direcciones     # Direcciones del usuario
POST   /api/direcciones     # Agregar dirección
PUT    /api/direcciones/{id} # Actualizar dirección
DELETE /api/direcciones/{id} # Eliminar dirección
```

### 🧪 **Testing (Solo Desarrollo)**
```http
GET    /api/demo/info                     # Información de endpoints
GET    /api/demo/producto-no-encontrado/{id} # Test 404
GET    /api/demo/validacion-id/{id}       # Test validación
POST   /api/demo/validar-precio?precio={p} # Test precio
POST   /api/demo/datos-duplicados?email={e} # Test duplicados
GET    /api/demo/argumento-invalido?texto= # Test argumentos
GET    /api/demo/error-interno            # Test error 500
```

## 🛡️ Seguridad

### Autenticación JWT
La API utiliza **JSON Web Tokens** para la autenticación:

1. **Registro/Login** → Obtener token
2. **Incluir en headers**: `Authorization: Bearer {token}`
3. **Token expira** en 24 horas (configurable)

### Roles de Usuario
- **USER**: Operaciones básicas (ver productos, realizar pedidos)
- **ADMIN**: Gestión completa del sistema

### Endpoints Públicos
- Visualización de productos y categorías
- Búsquedas públicas
- Registro y login
- Endpoints de testing (solo desarrollo)

### Configuración CORS
```java
// Configurado para frontend en localhost:3000
@CrossOrigin(origins = "http://localhost:3000")
```

## 🎯 Testing

### Endpoints de Testing
La aplicación incluye endpoints especiales para probar el manejo de excepciones:

```bash
# Probar todos los endpoints de testing
.\test-endpoints.ps1
```

### Tests Unitarios
```bash
# Ejecutar tests
.\mvnw.cmd test

# Tests con coverage
.\mvnw.cmd test jacoco:report
```

### Testing Manual
```bash
# Probar APIs específicas
.\test-all-apis.ps1
```

## 📊 Base de Datos

### Modelo de Entidades

```
├── Usuario (id, email, nombre, apellido, rol, password)
├── Producto (id, nombre, descripcion, precio, stock, imagen)
├── Categoria (id, nombre, descripcion)
├── Artista (id, nombre, biografia, imagen)
├── Pedido (id, fecha, estado, total, usuario_id)
├── Direccion (id, calle, ciudad, codigo_postal, usuario_id)
└── PedidoDetalle (pedido_id, producto_id, cantidad, precio)
```

### Configuración Hibernate
- **DDL**: `update` (crea/actualiza automáticamente)
- **SQL Logging**: Habilitado en desarrollo
- **Dialect**: MySQL optimizado

### Scripts de Datos
- `datos_prueba_arte.sql`: Datos de ejemplo
- `agregar-datos.ps1`: Script automatizado
- `datos-simple.ps1`: Datos básicos

## 🔧 Scripts Utilitarios

### Windows PowerShell
```bash
.\agregar-datos.ps1      # Poblar base de datos
.\test-endpoints.ps1     # Probar endpoints
.\test-all-apis.ps1      # Tests completos
.\datos-simple.ps1       # Datos básicos
```

### Linux/Mac
```bash
./add-data.sh           # Poblar base de datos
```

## 📖 Documentación

### Archivos de Documentación
- `INTEGRACION_FRONTEND.md`: Guía de integración con React
- `EXCEPTION_HANDLING.md`: Sistema de manejo de excepciones
- `PRUEBAS_COMPLETADAS.md`: Resultados de testing
- `HELP.md`: Guía de inicio rápido

### Manejo de Excepciones
La API implementa un sistema robusto de manejo de errores:

```json
{
  "timestamp": "2025-10-21T18:53:28",
  "status": 404,
  "error": "Producto No Encontrado",
  "message": "No se encontró el producto con id: 999",
  "path": "/api/productos/999",
  "details": null
}
```

### Códigos de Estado HTTP
- **200**: OK - Operación exitosa
- **201**: Created - Recurso creado
- **400**: Bad Request - Datos inválidos
- **401**: Unauthorized - No autenticado
- **403**: Forbidden - Sin permisos
- **404**: Not Found - Recurso no encontrado
- **409**: Conflict - Datos duplicados
- **500**: Internal Server Error - Error del servidor

## 🚀 Despliegue

### Desarrollo Local
```bash
# Clonar y configurar
git clone [repo-url]
cd TPO-Ecommerce-Back
.\mvnw.cmd spring-boot:run
```

### Producción
```bash
# Construir JAR
.\mvnw.cmd clean package

# Ejecutar
java -jar target/e_commerce-0.0.1-SNAPSHOT.jar
```

### Docker (Opcional)
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/e_commerce-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
```

## 📈 Características Avanzadas

### ✅ **Sistema de Validaciones**
- Validación de emails
- Precios no negativos
- IDs válidos
- Campos obligatorios

### ✅ **Logging Configurado**
- Logs de aplicación en `application-logging.properties`
- Niveles configurables
- Rotación automática

### ✅ **Manejo de Errores**
- Interceptación global con `@ControllerAdvice`
- Respuestas consistentes
- Logging de errores para debugging

### ✅ **Optimizaciones**
- Lazy loading configurado
- Conexiones de BD optimizadas
- Caching de consultas frecuentes

## 🤝 Contribuir

### Proceso de Contribución
1. Fork el proyecto
2. Crear feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push al branch (`git push origin feature/AmazingFeature`)
5. Abrir Pull Request

### Estándares de Código
- Seguir convenciones de Spring Boot
- Documentar métodos públicos
- Incluir tests para nuevas funcionalidades
- Mantener cobertura de tests > 80%

### Issues y Bugs
- Usar templates de GitHub Issues
- Incluir pasos para reproducir
- Especificar versión y entorno

## 📞 Soporte

### Contacto
- **Desarrollador**: Leandro
- **GitHub**: [@LeandroFullStackJs](https://github.com/LeandroFullStackJs)
- **Email**: [tu-email@ejemplo.com]

### Enlaces Útiles
- [Documentación Spring Boot](https://spring.io/projects/spring-boot)
- [JWT.io](https://jwt.io/)
- [Spring Security](https://spring.io/projects/spring-security)

---

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para detalles.

---

## 🎉 Estado del Proyecto

**✅ COMPLETAMENTE FUNCIONAL**
- ✅ API REST completa
- ✅ Autenticación JWT
- ✅ Base de datos configurada
- ✅ Sistema de excepciones
- ✅ Testing implementado
- ✅ Documentación completa
- ✅ Scripts de utilidad
- ✅ Integración frontend lista

**🚀 Próximas Características**
- [ ] API Documentation con Swagger
- [ ] Tests de integración completos
- [ ] Métricas con Actuator
- [ ] Deploy automatizado
- [ ] Caching con Redis
- [ ] Notificaciones por email

---
