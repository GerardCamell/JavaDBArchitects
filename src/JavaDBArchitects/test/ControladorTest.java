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
        String numeroSocio = "001";
        String nombre = "John Doe";
        int tipoSocio = 0;
        String NIF = "12345678A";
        Seguro seguro = new Seguro("Básico", 50);

        Controlador.registrarSocio(numeroSocio, nombre, tipoSocio, NIF, seguro);

        Socio socio = ListaSocios.getSocio(numeroSocio);
        assertNotNull(socio);
        assertEquals("John Doe", socio.getNombre());
        assertTrue(socio instanceof Estandar);
    }

    @Test
    void testRegistrarSocioDuplicado() {
        String numeroSocio = "001";
        String nombre = "John Doe";
        int tipoSocio = 0;
        String NIF = "12345678A";
        Seguro seguro = new Seguro("Básico", 50);

        Controlador.registrarSocio(numeroSocio, nombre, tipoSocio, NIF, seguro);

        // Intentar registrar el mismo socio nuevamente
        Controlador.registrarSocio(numeroSocio, nombre, tipoSocio, NIF, seguro);

        // Verificar que solo exista un socio registrado
        assertEquals(1, ListaSocios.getSocios().size());
    }

    @Test
    void testEliminarSocioExitoso() {
        try {
            List<Object> parametros = new ArrayList<>();
            parametros.add(0);
            parametros.add("002");
            parametros.add("Jane Doe");
            parametros.add("87654321B");
            parametros.add(new Seguro("Completo", 100));
            Datos.registrarSocio(parametros);

            Controlador.eliminarSocio("002");

            Socio socio = ListaSocios.getSocio("002");
            assertNull(socio);
        } catch (Exception e) {
            fail("No se esperaba una excepción.");
        }
    }

    @Test
    void testEliminarSocioConInscripciones() {
        try {
            // Registrar socio
            List<Object> parametrosSocio = new ArrayList<>();
            parametrosSocio.add(0);
            parametrosSocio.add("003");
            parametrosSocio.add("Alex Smith");
            parametrosSocio.add("98765432C");
            parametrosSocio.add(new Seguro("Básico", 50));
            Datos.registrarSocio(parametrosSocio);

            // Registrar excursión
            List<Object> parametrosExcursion = new ArrayList<>();
            parametrosExcursion.add("EXC001");
            parametrosExcursion.add("Excursión a la montaña");
            parametrosExcursion.add(java.sql.Date.valueOf(LocalDate.now().plusDays(10)));
            parametrosExcursion.add(2);
            parametrosExcursion.add(100.0f);
            Datos.registrarExcursion(parametrosExcursion);

            // Inscribir en la excursión
            Controlador.inscribirEnExcursion("003", "EXC001", LocalDate.now());

            // Intentar eliminar el socio (debería fallar porque tiene inscripciones)
            Controlador.eliminarSocio("003");
            assertNotNull(ListaSocios.getSocio("003"));
        } catch (Exception e) {
            fail("No se esperaba una excepción.");
        }
    }

    @Test
    void testRegistrarExcursionExitoso() {
        String codigo = "EXC002";
        String descripcion = "Excursión al lago";
        LocalDate fecha = LocalDate.now().plusDays(5);
        int numeroDias = 2;
        float precio = 100.0f;

        try {
            Controlador.registrarExcursion(codigo, descripcion, fecha, numeroDias, precio);
        } catch (ExcursionYaExisteException e) {
            fail("No debería haber lanzado una excepción");
        }

        Excursion excursion = ListaExcursiones.getExcursion(codigo);
        assertNotNull(excursion);
        assertEquals("Excursión al lago", excursion.getDescripcion());
    }

    @Test
    void testInscribirEnExcursionExitoso() {
        try {
            List<Object> parametrosSocio = new ArrayList<>();
            parametrosSocio.add(0);
            parametrosSocio.add("004");
            parametrosSocio.add("Linda Smith");
            parametrosSocio.add("56473829D");
            parametrosSocio.add(new Seguro("Completo", 90));
            Datos.registrarSocio(parametrosSocio);

            List<Object> parametrosExcursion = new ArrayList<>();
            parametrosExcursion.add("EXC005");
            parametrosExcursion.add("Excursión a la playa");
            parametrosExcursion.add(java.sql.Date.valueOf(LocalDate.now().plusDays(4)));
            parametrosExcursion.add(1);
            parametrosExcursion.add(60.0f);
            Datos.registrarExcursion(parametrosExcursion);

            Controlador.inscribirEnExcursion("004", "EXC005", LocalDate.now());

            boolean inscripcionExiste = ListaInscripciones.inscripcionExiste("004", "EXC005");
            assertTrue(inscripcionExiste);
        } catch (Exception e) {
            fail("No se esperaba una excepción.");
        }
    }



    @Test
    void testModificarDatosSocioExitoso() {
        try {
            List<Object> parametrosSocio = new ArrayList<>();
            parametrosSocio.add(0);
            parametrosSocio.add("006");
            parametrosSocio.add("Tom Hardy");
            parametrosSocio.add("98765432A");
            parametrosSocio.add(new Seguro("Completo", 70));
            Datos.registrarSocio(parametrosSocio);

            Controlador.modificarDatosSocio("006", "Tom Hardy Jr.");

            Socio socio = ListaSocios.getSocio("006");
            assertNotNull(socio);
            assertEquals("Tom Hardy Jr.", socio.getNombre());
        } catch (Exception e) {
            fail("No se esperaba una excepción.");
        }
    }
}

