package com.api.e_commerce.exception;

import com.api.e_commerce.dto.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;

/**
 * Manejador global de excepciones para toda la aplicación
 * Proporciona respuestas consistentes y logging apropiado para todas las excepciones
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ==================== EXCEPCIONES DE NEGOCIO ====================

    @ExceptionHandler(ProductoNotFoundException.class)
    public ResponseEntity<ErrorResponse> manejarProductoNoEncontrado(
            ProductoNotFoundException ex, WebRequest request) {
        logger.warn("Producto no encontrado: {}", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            "Producto No Encontrado",
            ex.getMessage(),
            request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsuarioNotFoundException.class)
    public ResponseEntity<ErrorResponse> manejarUsuarioNoEncontrado(
            UsuarioNotFoundException ex, WebRequest request) {
        logger.warn("Usuario no encontrado: {}", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            "Usuario No Encontrado",
            ex.getMessage(),
            request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ArtistaNotFoundException.class)
    public ResponseEntity<ErrorResponse> manejarArtistaNoEncontrado(
            ArtistaNotFoundException ex, WebRequest request) {
        logger.warn("Artista no encontrado: {}", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            "Artista No Encontrado",
            ex.getMessage(),
            request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CategoriaNotFoundException.class)
    public ResponseEntity<ErrorResponse> manejarCategoriaNoEncontrada(
            CategoriaNotFoundException ex, WebRequest request) {
        logger.warn("Categoría no encontrada: {}", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            "Categoría No Encontrada",
            ex.getMessage(),
            request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PedidoNotFoundException.class)
    public ResponseEntity<ErrorResponse> manejarPedidoNoEncontrado(
            PedidoNotFoundException ex, WebRequest request) {
        logger.warn("Pedido no encontrado: {}", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            "Pedido No Encontrado",
            ex.getMessage(),
            request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateDataException.class)
    public ResponseEntity<ErrorResponse> manejarDatosDuplicados(
            DuplicateDataException ex, WebRequest request) {
        logger.warn("Datos duplicados: {}", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.CONFLICT.value(),
            "Datos Duplicados",
            ex.getMessage(),
            request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ErrorResponse> manejarStockInsuficiente(
            InsufficientStockException ex, WebRequest request) {
        logger.warn("Stock insuficiente: {}", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Stock Insuficiente",
            ex.getMessage(),
            request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PrecioNegativoException.class)
    public ResponseEntity<ErrorResponse> manejarPrecioNegativo(
            PrecioNegativoException ex, WebRequest request) {
        logger.warn("Precio negativo: {}", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Precio Inválido",
            ex.getMessage(),
            request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<ErrorResponse> manejarDatosInvalidos(
            InvalidDataException ex, WebRequest request) {
        logger.warn("Datos inválidos: {}", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Datos Inválidos",
            ex.getMessage(),
            request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // ==================== EXCEPCIONES DE SEGURIDAD ====================

    // Manejar AuthenticationException personalizada
    @ExceptionHandler(com.api.e_commerce.exception.AuthenticationException.class)
    public ResponseEntity<ErrorResponse> manejarAutenticacionPersonalizada(
            com.api.e_commerce.exception.AuthenticationException ex, WebRequest request) {
        logger.warn("Error de autenticación personalizada: {}", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.UNAUTHORIZED.value(),
            "Error de Autenticación",
            ex.getMessage(),
            request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
    
    // Manejar AuthenticationException de Spring Security
    @ExceptionHandler(org.springframework.security.core.AuthenticationException.class)
    public ResponseEntity<ErrorResponse> manejarAutenticacionSpring(
            org.springframework.security.core.AuthenticationException ex, WebRequest request) {
        logger.warn("Error de autenticación Spring Security: {}", ex.getMessage());
        
        String mensaje = "Email o contraseña incorrectos";
        if (ex.getCause() != null) {
            mensaje = ex.getCause().getMessage();
        } else if (ex.getMessage() != null && !ex.getMessage().isEmpty()) {
            mensaje = ex.getMessage();
        }
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.UNAUTHORIZED.value(),
            "Error de Autenticación",
            mensaje,
            request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> manejarCredencialesInvalidas(
            BadCredentialsException ex, WebRequest request) {
        logger.warn("Credenciales inválidas: {}", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.UNAUTHORIZED.value(),
            "Credenciales Inválidas",
            "Email o contraseña incorrectos",
            request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> manejarUsuarioNoEncontradoSecurity(
            UsernameNotFoundException ex, WebRequest request) {
        logger.warn("Usuario no encontrado en autenticación: {}", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.UNAUTHORIZED.value(),
            "Usuario No Encontrado",
            "Email o contraseña incorrectos",
            request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    // --- MANEJADOR PARA ACCESO DENEGADO (403) ---
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> manejarAccesoDenegado(
            AccessDeniedException ex, WebRequest request) {
        logger.warn("Acceso denegado: {}", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.FORBIDDEN.value(), // 403 Forbidden
            "Acceso Denegado",
            "No tienes permiso para realizar esta acción.",
            request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    // --- MANEJADOR PARA ERRORES DE JWT (401) ---
    @ExceptionHandler({ ExpiredJwtException.class, SignatureException.class, MalformedJwtException.class })
    public ResponseEntity<ErrorResponse> manejarErroresJwt(Exception ex, WebRequest request) {
        logger.warn("Error de token JWT: {}", ex.getMessage());
        String mensaje = "El token de sesión no es válido.";
        if (ex instanceof ExpiredJwtException) {
            mensaje = "Tu sesión ha expirado. Por favor, inicia sesión de nuevo.";
        } else if (ex instanceof SignatureException) {
            mensaje = "El token de sesión tiene una firma inválida.";
        } else if (ex instanceof MalformedJwtException) {
            mensaje = "El token de sesión está mal formado.";
        }
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.UNAUTHORIZED.value(), // 401 Unauthorized
            "Token Inválido",
            mensaje,
            request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }


    // ==================== EXCEPCIONES DE VALIDACIÓN ====================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> manejarValidacionArgumentos(
            MethodArgumentNotValidException ex, WebRequest request) {
        logger.warn("Error de validación: {}", ex.getMessage());

        List<String> details = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            details.add(error.getField() + ": " + error.getDefaultMessage());
        }
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Error de Validación",
            "Los datos proporcionados no son válidos",
            request.getDescription(false).replace("uri=", ""),
            details
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> manejarArgumentoInvalido(
            IllegalArgumentException ex, WebRequest request) {
        logger.warn("Argumento inválido: {}", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Argumento Inválido",
            ex.getMessage(),
            request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> manejarTipoArgumentoIncorrecto(
            MethodArgumentTypeMismatchException ex, WebRequest request) {
        logger.warn("Tipo de argumento incorrecto: {}", ex.getMessage());
        
        Class<?> requiredType = ex.getRequiredType();
        String typeName = (requiredType != null) ? requiredType.getSimpleName() : "tipo requerido";
        
        String message = String.format("El parámetro '%s' debe ser de tipo %s", 
            ex.getName(), typeName);
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Tipo de Dato Incorrecto",
            message,
            request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> manejarMensajeNoLegible(
            HttpMessageNotReadableException ex, WebRequest request) {
        logger.warn("Mensaje HTTP no legible: {}", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Formato de Datos Incorrecto",
            "El formato de los datos enviados no es válido. Verifique el JSON",
            request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // ==================== EXCEPCIONES DE BASE DE DATOS ====================

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> manejarViolacionIntegridad(
            DataIntegrityViolationException ex, WebRequest request) {
        logger.error("Violación de integridad de datos: {}", ex.getMessage());
        
        String message = "Error de integridad de datos. Posible conflicto con datos existentes";
        if (ex.getMessage().contains("Duplicate")) {
            message = "Ya existe un registro con estos datos";
        } else if (ex.getMessage().contains("foreign key")) {
            message = "No se puede completar la operación debido a referencias de datos";
        }
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.CONFLICT.value(),
            "Error de Integridad de Datos",
            message,
            request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    // ==================== EXCEPCIÓN GENERAL (Fallback) ====================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> manejarErroresGenerales(
            Exception ex, WebRequest request) {
        logger.error("Error interno no controlado: ", ex);
        
        // Mensaje más descriptivo que incluya información útil
        String mensaje = "Ha ocurrido un error interno";
        if (ex.getMessage() != null && !ex.getMessage().isEmpty()) {
            mensaje = ex.getMessage();
        }
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Error Interno del Servidor",
            mensaje,
            request.getDescription(false).replace("uri=", "")
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
