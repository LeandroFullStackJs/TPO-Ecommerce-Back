package com.api.e_commerce.service;

import com.api.e_commerce.exception.InvalidDataException;
import com.api.e_commerce.exception.PrecioNegativoException;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Servicio para validaciones de datos de negocio
 */
@Service
public class ValidationService {

    // Patrones (expresiones regulares) para validaciones
    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");

    private static final Pattern PHONE_PATTERN =
        Pattern.compile("^[+]?[0-9]{10,15}$");

    // Patrón de URL ELIMINADO

    // Patrones para complejidad de contraseña
    private static final Pattern UPPERCASE_PATTERN = Pattern.compile(".*[A-Z].*");
    private static final Pattern LOWERCASE_PATTERN = Pattern.compile(".*[a-z].*");
    private static final Pattern DIGIT_PATTERN = Pattern.compile(".*\\d.*");
    private static final Pattern SYMBOL_PATTERN = Pattern.compile(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*");


    /**
     * Valida que un precio sea válido (no negativo)
     */
    public void validarPrecio(BigDecimal precio) {
        if (precio == null) {
            throw new InvalidDataException("precio", "null", "El precio no puede ser nulo");
        }
        if (precio.compareTo(BigDecimal.ZERO) < 0) {
            throw new PrecioNegativoException();
        }
    }

    /**
     * Valida que un precio sea válido (no negativo) - versión para Double
     */
    public void validarPrecio(Double precio) {
        if (precio == null) {
            throw new InvalidDataException("precio", "null", "El precio no puede ser nulo");
        }
        if (precio < 0) {
            throw new PrecioNegativoException();
        }
    }

    /**
     * Valida que el stock sea válido (no negativo)
     */
    public void validarStock(Integer stock) {
        if (stock == null) {
            throw new InvalidDataException("stock", "null", "El stock no puede ser nulo");
        }
        if (stock < 0) {
            throw new InvalidDataException("stock", stock.toString(), "El stock no puede ser negativo");
        }
    }

    /**
     * Valida formato de email
     */
    public void validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidDataException("email", "vacío", "El email no puede estar vacío");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidDataException("email", email, "El formato del email no es válido");
        }
    }

    /**
     * Valida que un texto no sea nulo o vacío
     */
    public void validarTextoNoVacio(String texto, String campo) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new InvalidDataException(campo, "vacío", "El campo " + campo + " no puede estar vacío");
        }
    }

    /**
     * Valida que un texto tenga una longitud mínima
     */
    public void validarLongitudMinima(String texto, String campo, int longitudMinima) {
        validarTextoNoVacio(texto, campo);
        if (texto.trim().length() < longitudMinima) {
            throw new InvalidDataException(campo, texto,
                "El campo " + campo + " debe tener al menos " + longitudMinima + " caracteres");
        }
    }

    /**
     * Valida que un texto no exceda una longitud máxima
     */
    public void validarLongitudMaxima(String texto, String campo, int longitudMaxima) {
        if (texto != null && texto.length() > longitudMaxima) {
            throw new InvalidDataException(campo, "[muy largo]", // Evitar mostrar textos largos en el error
                "El campo " + campo + " no puede exceder " + longitudMaxima + " caracteres");
        }
    }

    /**
     * Valida formato de teléfono
     */
    public void validarTelefono(String telefono) {
        if (telefono != null && !telefono.trim().isEmpty()) {
            // Elimina espacios antes de validar
            String telefonoLimpio = telefono.replaceAll("\\s", "");
            if (!PHONE_PATTERN.matcher(telefonoLimpio).matches()) {
                throw new InvalidDataException("telefono", telefono, "El formato del teléfono no es válido (solo números, opcionalmente '+' al inicio, 10-15 dígitos)");
            }
        }
    }

    /**
     * Valida que un ID sea válido (positivo)
     */
    public void validarId(Long id, String tipoEntidad) {
        if (id == null) {
            throw new InvalidDataException("id", "null", "El ID del " + tipoEntidad + " no puede ser nulo");
        }
        if (id <= 0) {
            throw new InvalidDataException("id", id.toString(), "El ID del " + tipoEntidad + " debe ser positivo");
        }
    }

    /**
     * Valida contraseña (longitud mínima y complejidad)
     */
    public void validarPassword(String password) {
        validarLongitudMinima(password, "password", 6); // Mantiene la longitud mínima de 6

        // Validaciones de complejidad (activas)
        if (!UPPERCASE_PATTERN.matcher(password).matches()) {
            throw new InvalidDataException("password", "****", "La contraseña debe contener al menos una mayúscula");
        }
        if (!LOWERCASE_PATTERN.matcher(password).matches()) {
            throw new InvalidDataException("password", "****", "La contraseña debe contener al menos una minúscula");
        }
        if (!DIGIT_PATTERN.matcher(password).matches()) {
            throw new InvalidDataException("password", "****", "La contraseña debe contener al menos un número");
        }
        // if (!SYMBOL_PATTERN.matcher(password).matches()) { // Descomenta si requieres símbolos
        //     throw new InvalidDataException("password", "****", "La contraseña debe contener al menos un símbolo");
        // }
    }

    /**
     * Valida año (rango razonable)
     */
    public void validarAnio(Integer anio) {
        if (anio != null) {
            int currentYear = java.time.Year.now().getValue();
            if (anio < 1000 || anio > currentYear + 1) { // Permite hasta el año siguiente por si acaso
                throw new InvalidDataException("año", anio.toString(),
                    "El año debe ser válido (ej: entre 1000 y " + (currentYear + 1) + ")");
            }
        }
    }

    /**
     * Valida que un valor pertenezca a una lista de valores permitidos.
     * Ignora mayúsculas/minúsculas para la comparación.
     */
    public void validarValorPermitido(String valor, String campo, List<String> valoresPermitidos) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new InvalidDataException(campo, "vacío", "El campo " + campo + " no puede estar vacío");
        }
        boolean encontrado = false;
        for (String permitido : valoresPermitidos) {
            if (permitido.equalsIgnoreCase(valor.trim())) {
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            throw new InvalidDataException(campo, valor, "El valor '" + valor + "' no es válido para " + campo +
                ". Valores permitidos: " + String.join(", ", valoresPermitidos));
        }
    }

    
}