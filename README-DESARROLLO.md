# 🎨 E-Commerce de Arte - Guía de Desarrollo

## 📋 Descripción del Proyecto

Este es un backend completo para un e-commerce de arte desarrollado con **Spring Boot 3.5.7** y **MySQL 8.0**. El proyecto incluye gestión de productos artísticos, artistas, categorías, usuarios, pedidos y autenticación JWT.

## 🛠️ Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.5.7**
- **MySQL 8.0** (usando Docker)
- **Spring Security + JWT**
- **Spring Data JPA**
- **Docker & Docker Compose**
- **Maven**

## 🚀 Configuración Inicial para Desarrolladores

### Prerrequisitos

1. **Docker Desktop** instalado y ejecutándose
2. **Java 17** instalado
3. **Git** instalado
4. **VS Code** o IDE de preferencia

### 📦 Clonación del Repositorio

```bash
git clone https://github.com/LeandroFullStackJs/TPO-Ecommerce-Back.git
cd TPO-Ecommerce-Back
```

## 🐳 Levantando el Proyecto con Docker

### 1. Levantar los Servicios

```powershell
# Construir y levantar todos los servicios
docker-compose up --build -d

# Alternativamente, si ya está construido:
docker-compose up -d
```

### 2. Verificar que los Contenedores Estén Funcionando

```powershell
docker ps
```

Deberías ver:
- `ecommerce_mysql` - Puerto 3307
- `ecommerce_backend` - Puerto 8080

### 3. Cargar Datos de Prueba (Solo la Primera Vez)

```powershell
# Copiar archivo SQL al contenedor
docker cp datos_completos_arte.sql ecommerce_mysql:/tmp/datos_completos_arte.sql

# Ejecutar el script SQL
docker exec -it ecommerce_mysql mysql -u root ecommerce_db -e "source /tmp/datos_completos_arte.sql"
```

### 4. Verificar que Todo Funcione

```powershell
# Probar la API
curl http://localhost:8080/api/productos
# O en PowerShell:
Invoke-RestMethod -Uri "http://localhost:8080/api/productos" -Method GET
```

## 🔄 Uso Diario del Proyecto

### Para Levantar el Proyecto (Después de Reiniciar la PC)

```powershell
# 1. Asegurarse de que Docker Desktop esté ejecutándose
# 2. Ir al directorio del proyecto
cd C:\Users\[TU_USUARIO]\Desktop\TPO-Ecommerce-Back

# 3. Levantar los servicios
docker-compose up -d

# 4. Verificar que esté funcionando
docker ps
curl http://localhost:8080/api/productos
```

### Para Detener el Proyecto

```powershell
# Detener todos los servicios
docker-compose down

# Si quieres eliminar también los volúmenes (CUIDADO: borra la base de datos)
docker-compose down -v
```

### Para Ver Logs

```powershell
# Ver logs del backend
docker logs ecommerce_backend

# Ver logs de MySQL
docker logs ecommerce_mysql

# Ver logs en tiempo real
docker logs -f ecommerce_backend
```

## 🗃️ Estructura de la Base de Datos

El proyecto incluye datos completos de prueba:

### Datos Incluidos:
- **8 Categorías** (Pintura, Escultura, Fotografía, Arte Digital, etc.)
- **5 Artistas** con biografías completas
- **2 Usuarios** (Admin y Usuario normal)
- **11 Obras de arte** con precios, descripciones e imágenes
- **6 Productos destacados**

### Usuarios de Prueba:
- **Admin**: `admin@arte.com` / Password: `Admin123@`
- **Usuario**: `galeria@arte.com` / Password: `Admin123@`

## 🌐 Endpoints Principales

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/productos` | Listar todos los productos |
| GET | `/api/productos/destacados` | Productos destacados |
| GET | `/api/productos/{id}` | Obtener producto por ID |
| GET | `/api/categorias` | Listar categorías |
| GET | `/api/artistas` | Listar artistas |
| POST | `/api/auth/login` | Iniciar sesión |
| POST | `/api/auth/register` | Registrar usuario |
| PUT | `/api/productos/{id}/decrementar-stock` | Decrementar stock |
| PUT | `/api/productos/{id}/incrementar-stock` | Incrementar stock |

### Ejemplo de Login:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "admin@arte.com", "password": "Admin123@"}'
```

## 🔧 Configuración del Proyecto

### Archivos de Configuración Importantes:

1. **`docker-compose.yml`** - Configuración de servicios Docker
2. **`application.properties`** - Configuración para desarrollo local
3. **`application-docker.properties`** - Configuración para Docker
4. **`datos_completos_arte.sql`** - Datos de prueba completos

### Puertos Utilizados:
- **Backend**: `8080`
- **MySQL**: `3307` (externo) → `3306` (interno del contenedor)

### Variables de Entorno en Docker:
- `SPRING_PROFILES_ACTIVE=docker`
- Configuración de MySQL automática

## 🐛 Solución de Problemas Comunes

### El contenedor no se inicia:
```powershell
# Ver logs para diagnosticar
docker logs ecommerce_backend
docker logs ecommerce_mysql

# Reconstruir desde cero
docker-compose down
docker-compose up --build -d
```

### Puerto 3307 ocupado:
```powershell
# Verificar qué está usando el puerto
netstat -ano | findstr :3307

# Cambiar el puerto en docker-compose.yml si es necesario
```

### Error de conexión a la base de datos:
```powershell
# Verificar que MySQL esté saludable
docker ps
# Debe mostrar "healthy" en el estado de ecommerce_mysql

# Esperar a que MySQL esté completamente iniciado
docker logs ecommerce_mysql
```

### Reconstruir la aplicación:
```powershell
# Compilar nuevamente
.\mvnw.cmd clean package -DskipTests

# Reconstruir y levantar
docker-compose up --build -d
```

## 📚 Desarrollo Local (Alternativa sin Docker)

Si prefieres desarrollar sin Docker:

1. **Instalar MySQL localmente** (XAMPP recomendado)
2. **Crear base de datos**: `ecommerce_db`
3. **Cambiar configuración** en `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
   ```
4. **Ejecutar aplicación**:
   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

## 🔄 Para Contribuir al Proyecto

### 1. Crear una Nueva Rama
```bash
git checkout -b feature/nueva-funcionalidad
```

### 2. Hacer Cambios y Commit
```bash
git add .
git commit -m "feat: agregar nueva funcionalidad"
```

### 3. Subir Cambios
```bash
git push origin feature/nueva-funcionalidad
```

### 4. Crear Pull Request en GitHub

## 📋 Checklist para Nuevos Desarrolladores

- [ ] Docker Desktop instalado y funcionando
- [ ] Repositorio clonado
- [ ] `docker-compose up -d` ejecutado exitosamente
- [ ] Datos cargados con `datos_completos_arte.sql`
- [ ] API respondiendo en `http://localhost:8080/api/productos`
- [ ] Configuración del IDE completada

## 🎯 Próximos Pasos

1. **Frontend**: Conectar con React/Vue/Angular
2. **Imágenes**: Implementar subida de archivos
3. **Pagos**: Integrar sistema de pagos
4. **Testing**: Agregar tests unitarios e integración
5. **Deploy**: Configurar CI/CD para producción

## 📞 Contacto

- **Desarrollador Principal**: Leandro
- **Repositorio**: https://github.com/LeandroFullStackJs/TPO-Ecommerce-Back

---

**¡Tu API de E-Commerce de Arte está lista para usar!** 🎨✨