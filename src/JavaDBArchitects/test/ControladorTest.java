package JavaDBArchitects.test;

import JavaDBArchitects.controlador.Controlador;
import JavaDBArchitects.controlador.excepciones.*;
import JavaDBArchitects.modelo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ControladorTest {

    @BeforeEach
    void setUp() {
        // Limpiar las listas de datos antes de cada prueba
        ListaSocios.getSocios().clear();
        ListaExcursiones.getExcursiones().clear();
        ListaInscripciones.getInscripciones().clear();
    }

    @Test
    void testRegistrarSocioExitoso() {
        // Prueba para registrar un socio estándar
        String numeroSocio = "001";
        String nombre = "John Doe";
        int tipoSocio = 0;  // Estandar
        String NIF = "12345678A";
        Seguro seguro = new Seguro("Básico", 50);

        Controlador.registrarSocio(numeroSocio, nombre, tipoSocio, NIF, seguro);

        // Verificamos si el socio ha sido registrado correctamente
        Socio socio = ListaSocios.getSocio(numeroSocio);
        assertNotNull(socio);
        assertEquals("John Doe", socio.getNombre());
        assertTrue(socio instanceof Estandar);
    }

    @Test
    void testEliminarSocioExitoso() throws SocioYaExisteException, SeguroInvalidoException, TipoSocioInvalidoException, SocioNoExisteException, SocioConInscripcionesException {
        // Registrar un socio primero
        List<Object> parametros = new ArrayList<>();
        parametros.add(0);  // Tipo de socio: Estandar
        parametros.add("002");
        parametros.add("Jane Doe");
        parametros.add("87654321B");
        parametros.add(new Seguro("Completo", 100));
        Datos.registrarSocio(parametros);

        // Eliminar el socio a través del controlador
        Controlador.eliminarSocio("002");

        // Verificar que el socio ya no existe
        Socio socio = ListaSocios.getSocio("002");
        assertNull(socio);
    }

    @Test
    void testRegistrarExcursionExitoso() {
        String codigo = "EXC001";
        String descripcion = "Excursión a la montaña";
        LocalDate fecha = LocalDate.now().plusDays(10);  // Fecha futura
        int numeroDias = 2;
        float precio = 150.0f;

        // Llamamos al controlador para registrar la excursión
        Controlador.registrarExcursion(codigo, descripcion, fecha, numeroDias, precio);

        // Verificamos si la excursión ha sido registrada correctamente
        Excursion excursion = ListaExcursiones.getExcursion(codigo);
        assertNotNull(excursion);
        assertEquals("Excursión a la montaña", excursion.getDescripcion());
    }

    @Test
    void testEliminarExcursionExitoso() throws ExcursionYaExisteException, EliminarExcursionConInscripcionesException, ExcursionNoExisteException {
        // Registrar una excursión primero
        List<Object> parametrosExcursion = new ArrayList<>();
        parametrosExcursion.add("EXC002");
        parametrosExcursion.add("Excursión al lago");
        parametrosExcursion.add(java.sql.Date.valueOf(LocalDate.now().plusDays(5)));
        parametrosExcursion.add(2);
        parametrosExcursion.add(100.0f);
        Datos.registrarExcursion(parametrosExcursion);

        // Eliminar la excursión a través del controlador
        Controlador.eliminarExcursion("EXC002");

        // Verificar que la excursión ya no existe
        Excursion excursion = ListaExcursiones.getExcursion("EXC002");
        assertNull(excursion);
    }

    @Test
    void testInscribirEnExcursionExitoso() throws SocioYaExisteException, SeguroInvalidoException, TipoSocioInvalidoException, ExcursionYaExisteException, InscripcionYaExisteException, SocioNoExisteException, ExcursionNoExisteException, FechaInvalidaException {
        // Registrar un socio y una excursión primero
        List<Object> parametrosSocio = new ArrayList<>();
        parametrosSocio.add(0);  // Tipo de socio: Estandar
        parametrosSocio.add("003");
        parametrosSocio.add("Alex Smith");
        parametrosSocio.add("98765432C");
        parametrosSocio.add(new Seguro("Básico", 50));
        Datos.registrarSocio(parametrosSocio);

        List<Object> parametrosExcursion = new ArrayList<>();
        parametrosExcursion.add("EXC003");
        parametrosExcursion.add("Excursión al parque");
        parametrosExcursion.add(java.sql.Date.valueOf(LocalDate.now().plusDays(5)));
        parametrosExcursion.add(2);
        parametrosExcursion.add(100.0f);
        Datos.registrarExcursion(parametrosExcursion);

        // Inscribir al socio en la excursión
        Controlador.inscribirEnExcursion("003", "EXC003", LocalDate.now());

        // Verificar que la inscripción ha sido registrada
        boolean inscripcionExiste = ListaInscripciones.inscripcionExiste("003", "EXC003");
        assertTrue(inscripcionExiste);
    }

    @Test
    void testModificarDatosSocio() throws SocioYaExisteException, SeguroInvalidoException, TipoSocioInvalidoException {
        // Registrar un socio primero
        List<Object> parametrosSocio = new ArrayList<>();
        parametrosSocio.add(0);
        parametrosSocio.add("006");
        parametrosSocio.add("Mark Spencer");
        parametrosSocio.add("32165498F");
        parametrosSocio.add(new Seguro("Completo", 100));
        Datos.registrarSocio(parametrosSocio);

        // Modificar los datos del socio
        Controlador.modificarDatosSocio("006", "Mark Spencer Jr.");

        // Verificar que el nombre del socio se ha actualizado
        Socio socio = ListaSocios.getSocio("006");
        assertNotNull(socio);
        assertEquals("Mark Spencer Jr.", socio.getNombre());
    }

    @Test
    void testMostrarInscripcionesConFiltros() {
        // Agregar algunos datos de prueba para verificar los filtros
        // Aquí agregaremos directamente algunas inscripciones simuladas

        // Este test puede variar dependiendo de cómo implementaste la parte de filtros
    }
}
