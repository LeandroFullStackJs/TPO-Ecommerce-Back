# 📝 Guía para Commitear y Compartir el Proyecto

## 🎯 Resumen del Estado Actual

Tu proyecto E-Commerce de Arte está **completamente funcional** con:
- ✅ Docker configurado (MySQL + Backend)
- ✅ Base de datos con datos completos de prueba
- ✅ API funcionando en puerto 8080
- ✅ 11 productos de arte, 5 artistas, 8 categorías
- ✅ Autenticación JWT implementada
- ✅ Scripts de inicio automático

## 🔄 Para Volver a Levantar Todo (Después de Apagar la PC)

### Método 1: Script Automático (Recomendado)
```powershell
# 1. Abrir PowerShell en el directorio del proyecto
cd C:\Users\[TU_USUARIO]\Desktop\TPO-Ecommerce-Back

# 2. Ejecutar script de inicio rápido
.\inicio-rapido.ps1

# 3. Si es la primera vez o quieres recargar datos:
.\cargar-datos.ps1
```

### Método 2: Manual
```powershell
# 1. Asegúrate de que Docker Desktop esté ejecutándose

# 2. Levantar servicios
docker-compose up -d

# 3. Verificar que funcione
curl http://localhost:8080/api/productos
```

## 🚀 Para Commitear y Compartir con Otros Desarrolladores

### 1. Preparar el Commit
```bash
# Agregar todos los archivos nuevos
git add .

# Hacer commit con mensaje descriptivo
git commit -m "feat: proyecto completo con Docker, MySQL y datos de prueba

- Configuración Docker completa (MySQL + Backend)
- Base de datos con 11 productos de arte
- 5 artistas con biografías completas
- 8 categorías de arte
- Scripts de inicio automático (inicio-rapido.ps1, cargar-datos.ps1)
- Autenticación JWT funcional
- CORS configurado para desarrollo
- Documentación completa en README-DESARROLLO.md

API funcionando en: http://localhost:8080/api
Usuarios de prueba: admin@arte.com / Admin123@"
```

### 2. Subir a GitHub
```bash
# Subir al repositorio remoto
git push origin main
```

## 👥 Para Que Otros Desarrolladores Usen el Proyecto

### Instrucciones para Nuevos Desarrolladores:

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/LeandroFullStackJs/TPO-Ecommerce-Back.git
   cd TPO-Ecommerce-Back
   ```

2. **Instalar prerrequisitos:**
   - Docker Desktop
   - Java 17 (opcional, ya que usamos Docker)

3. **Levantar el proyecto:**
   ```powershell
   # Método fácil - script automático
   .\inicio-rapido.ps1
   
   # Cargar datos (solo la primera vez)
   .\cargar-datos.ps1
   ```

4. **Verificar funcionamiento:**
   - API: http://localhost:8080/api/productos
   - Admin panel: admin@arte.com / Admin123@

## 📁 Archivos Importantes que se Incluirán en el Commit

### Archivos Principales:
- `docker-compose.yml` - Configuración de servicios Docker
- `Dockerfile` - Imagen del backend
- `datos_completos_arte.sql` - Datos completos de prueba
- `README-DESARROLLO.md` - Documentación completa
- `inicio-rapido.ps1` - Script de inicio automático
- `cargar-datos.ps1` - Script para cargar datos

### Configuración:
- `src/main/resources/application.properties` - Config desarrollo local
- `src/main/resources/application-docker.properties` - Config para Docker
- `.dockerignore` - Archivos ignorados por Docker
- `.gitignore` - Archivos ignorados por Git

## 🔧 Comandos Útiles Post-Commit

### Para desarrolladores que clonen el repo:

```powershell
# Ver estado de servicios
docker ps

# Ver logs
docker logs ecommerce_backend
docker logs ecommerce_mysql

# Reiniciar servicios
docker-compose restart

# Parar servicios
docker-compose down

# Reconstruir y levantar
docker-compose up --build -d
```

## 🌐 URLs y Puertos

- **API Backend**: http://localhost:8080
- **MySQL**: localhost:3307 (externo)
- **Documentación**: Ver README-DESARROLLO.md

## 🔐 Credenciales de Prueba

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

## 🎨 Datos Incluidos

- **8 Categorías**: Pintura, Escultura, Fotografía, Arte Digital, etc.
- **5 Artistas**: Con biografías y fotos de perfil
- **11 Productos**: Obras de arte con precios reales
- **Usuarios**: Admin y usuario de prueba
- **6 Productos destacados**: Para homepage

## ✅ Checklist Final

Antes de hacer push, asegúrate de que:

- [ ] `docker-compose up -d` funciona
- [ ] API responde: http://localhost:8080/api/productos
- [ ] Datos cargados correctamente (11 productos)
- [ ] Scripts de PowerShell funcionan
- [ ] README-DESARROLLO.md está actualizado
- [ ] .gitignore incluye archivos correctos
- [ ] No hay archivos sensibles o temporales

## 🚀 ¡Tu proyecto está listo para compartir!

Con este commit, cualquier desarrollador podrá:
1. Clonar el repo
2. Ejecutar `.\inicio-rapido.ps1`
3. Ejecutar `.\cargar-datos.ps1`
4. Tener la API funcionando completamente

**¡E-Commerce de Arte listo para producción!** 🎨✨