package JavaDBArchitects.test;

import JavaDBArchitects.modelo.*;
import JavaDBArchitects.controlador.excepciones.*;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DatosTest {

    @Test
    public void testRegistrarSocioEstandarConSeguro() throws Exception {
        // Limpiar la lista de socios manualmente
        ListaSocios.getSocios().clear(); // Esto eliminará todos los socios registrados

        List<Object> parametros = new ArrayList<>();
        parametros.add(0); // Tipo de socio Estandar
        parametros.add("1234"); // Número de socio
        parametros.add("Juan Pérez"); // Nombre
        parametros.add("12345678A"); // NIF
        parametros.add(new Seguro("Seguro de Salud", 50.0f)); // Seguro

        assertTrue(Datos.registrarSocio(parametros), "El socio estándar debería registrarse correctamente.");
    }

    @Test
    public void testRegistrarSocioEstandarSinSeguro() {
        List<Object> parametros = new ArrayList<>();
        parametros.add(0); // Tipo de socio Estandar
        parametros.add("1234"); // Número de socio
        parametros.add("Juan Pérez"); // Nombre
        parametros.add("12345678A"); // NIF
        parametros.add(null); // Seguro nulo

        Exception exception = assertThrows(SeguroInvalidoException.class, () -> {
            Datos.registrarSocio(parametros);
        });

        assertEquals("El seguro es obligatorio para los socios estándar.", exception.getMessage());
    }


    @Test
    public void testEliminarSocioExistente() throws Exception {
        // Limpiar la lista de socios manualmente
        ListaSocios.getSocios().clear(); // Esto eliminará todos los socios registrados

        // Registrar el socio primero
        testRegistrarSocioEstandarConSeguro();

        assertTrue(Datos.eliminarSocio("1234"), "El socio debería eliminarse correctamente.");
    }

    @Test
    public void testRegistrarExcursion() throws Exception {
        List<Object> parametros = new ArrayList<>();
        parametros.add("EXC001"); // Código de excursión
        parametros.add("Excursión a la montaña"); // Descripción
        parametros.add(Date.valueOf(LocalDate.of(2024, 10, 30))); // Fecha
        parametros.add(3); // Número de días
        parametros.add(150.0f); // Precio

        assertTrue(Datos.registrarExcursion(parametros), "La excursión debería registrarse correctamente.");
    }

    @Test
    public void testRegistrarExcursionYaExistente() throws Exception {
        // Limpiar la lista de excursiones manualmente
        ListaExcursiones.getExcursiones().clear(); // Esto eliminará todas las excursiones registradas

        // Primero, registramos la excursión
        List<Object> parametros = new ArrayList<>();
        parametros.add("EXC001"); // Código de excursión
        parametros.add("Excursión a la montaña"); // Descripción
        parametros.add(Date.valueOf(LocalDate.of(2024, 10, 30))); // Fecha
        parametros.add(3); // Número de días
        parametros.add(150.0f); // Precio

        // Registra la excursión
        Datos.registrarExcursion(parametros);

        // Ahora intenta registrar la misma excursión nuevamente
        Exception exception = assertThrows(ExcursionYaExisteException.class, () -> {
            Datos.registrarExcursion(parametros); // Intentar registrar de nuevo la misma excursión
        });

        assertEquals("La excursión ya existe.", exception.getMessage());
    }

}
