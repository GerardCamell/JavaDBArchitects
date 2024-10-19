package JavaDBArchitects.test;

import JavaDBArchitects.modelo.Datos;
import JavaDBArchitects.modelo.Socio;
import JavaDBArchitects.modelo.Seguro;
import JavaDBArchitects.modelo.Federacion;
import JavaDBArchitects.controlador.excepciones.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DatosTest {

    @BeforeEach
    void setUp() {
        // Se ejecuta antes de cada prueba. Aquí podrías inicializar objetos o datos que necesites para tus pruebas.
    }

    @Test
    void testRegistrarSocio() throws SocioYaExisteException, SeguroInvalidoException, TipoSocioInvalidoException {
        // Crear una lista de parámetros para el socio
        List<Object> parametros = new ArrayList<>();
        parametros.add(0);  // Tipo de socio: Estandar
        parametros.add("001");  // Número de socio
        parametros.add("John Doe");  // Nombre del socio
        parametros.add("12345678A");  // NIF
        parametros.add(new Seguro("Básico", 50));  // Seguro

        // Llamar al método de Datos para registrar el socio
        boolean resultado = Datos.registrarSocio(parametros);

        // Asegurarse de que el resultado sea true (registro exitoso)
        assertTrue(resultado);
    }

    @Test
    void testEliminarSocio() throws SocioNoExisteException, SocioConInscripcionesException, SocioYaExisteException, SeguroInvalidoException, TipoSocioInvalidoException {
        // Primero, registrar un socio
        List<Object> parametros = new ArrayList<>();
        parametros.add(0);  // Tipo de socio: Estandar
        parametros.add("002");  // Número de socio
        parametros.add("Jane Doe");  // Nombre del socio
        parametros.add("87654321B");  // NIF
        parametros.add(new Seguro("Completo", 100));  // Seguro
        Datos.registrarSocio(parametros);

        // Luego, intentar eliminar el socio
        boolean resultado = Datos.eliminarSocio("002");

        // Asegurarse de que el socio se haya eliminado correctamente
        assertTrue(resultado);
    }

    @Test
    void testRegistrarExcursion() throws ExcursionYaExisteException {
        // Crear una lista de parámetros para la excursión
        List<Object> parametros = new ArrayList<>();
        parametros.add("EXC001");  // Código de la excursión
        parametros.add("Excursión al Montaña");  // Descripción
        parametros.add(new Date());  // Fecha
        parametros.add(3);  // Número de días
        parametros.add(150.0f);  // Precio

        // Llamar al método de Datos para registrar la excursión
        boolean resultado = Datos.registrarExcursion(parametros);

        // Asegurarse de que el resultado sea true (registro exitoso)
        assertTrue(resultado);
    }

    @Test
    void testRegistrarInscripcion() throws SocioNoExisteException, ExcursionNoExisteException, FechaInvalidaException, InscripcionYaExisteException, SeguroInvalidoException, TipoSocioInvalidoException, SocioYaExisteException, ExcursionYaExisteException {
        // Registrar primero un socio y una excursión
        List<Object> parametrosSocio = new ArrayList<>();
        parametrosSocio.add(0);  // Tipo de socio: Estandar
        parametrosSocio.add("003");  // Número de socio
        parametrosSocio.add("Alex Smith");  // Nombre del socio
        parametrosSocio.add("98765432C");  // NIF
        parametrosSocio.add(new Seguro("Básico", 50));  // Seguro
        Datos.registrarSocio(parametrosSocio);

        List<Object> parametrosExcursion = new ArrayList<>();
        parametrosExcursion.add("EXC002");  // Código de la excursión
        parametrosExcursion.add("Excursión a la playa");  // Descripción
        parametrosExcursion.add(new Date());  // Fecha
        parametrosExcursion.add(2);  // Número de días
        parametrosExcursion.add(100.0f);  // Precio
        Datos.registrarExcursion(parametrosExcursion);

        // Ahora, registrar la inscripción del socio a la excursión
        List<Object> parametrosInscripcion = new ArrayList<>();
        parametrosInscripcion.add("003");  // Número de socio
        parametrosInscripcion.add("EXC002");  // Código de la excursión
        parametrosInscripcion.add(new Date());  // Fecha de inscripción

        boolean resultado = Datos.registrarInscripcion(parametrosInscripcion);

        // Asegurarse de que la inscripción se haya registrado correctamente
        assertTrue(resultado);
    }
}
