# Imagen base con Java 17
FROM eclipse-temurin:17-jdk

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el jar desde el host al contenedor
COPY target/e_commerce-0.0.1-SNAPSHOT.jar app.jar

# Exponemos el puerto 8080 (o el que use tu app)
EXPOSE 8080

# Comando de inicio con perfil de Docker
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]
