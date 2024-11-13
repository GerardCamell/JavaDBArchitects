package JavaDBArchitects.modelo.dao;

import JavaDBArchitects.modelo.*;
import JavaDBArchitects.modelo.conexion.DatabaseConnection;
import JavaDBArchitects.controlador.excepciones.*;
import JavaDBArchitects.vista.MenuPrincipal;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

import static JavaDBArchitects.modelo.conexion.DatabaseConnection.getConnection;


public class SocioDAO {

    // Método para agregar un nuevo socio
    public void addSocio(Socio socio) throws SocioYaExisteException {
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);  // Iniciar transacción

            // Verificar si el socio ya existe
            if (socioExiste(socio.getNumeroSocio())) {
                throw new SocioYaExisteException("El socio con número " + socio.getNumeroSocio() + " ya existe.");
            }

            Integer numSocioPadre = null;

            // Verificar tipo de socio y realizar operaciones específicas
            if (socio instanceof Estandar) {
                TipoSeguro tipoSeguro = ((Estandar) socio).getTipoSeguro();
                if (tipoSeguro == null) {
                    throw new IllegalArgumentException("Error: El socio estándar debe tener un tipo de seguro definido como BASICO o COMPLETO.");
                }
            } else if (socio instanceof Federado) {
                Federacion federacion = ((Federado) socio).getFederacion();
                Integer idFederacion = federacion.getId_federacion();
                String nombreFederacion = federacion.getNombre();

                if (!federacionExiste(idFederacion, connection)) {
                    insertarFederacionConId(idFederacion, nombreFederacion, connection);
                }
                socio.setIdFederacion(idFederacion);
            } else if (socio instanceof Infantil) {
                numSocioPadre = socio.getIdSocioPadre();
                if (numSocioPadre != null && !socioExiste(numSocioPadre)) {
                    throw new IllegalArgumentException("Error: El socio padre o madre con ID " + numSocioPadre + " no existe.");
                }
            }

            // Proceder con la inserción del socio, incluyendo cuota_mensual
            String query = "INSERT INTO Socios (numeroSocio, nombre, tipo_socio, nif, tipo_seguro, id_federacion, id_socio_padre, cuota_mensual) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, socio.getNumeroSocio());
                preparedStatement.setString(2, socio.getNombre());
                preparedStatement.setString(3, socio.getTipoSocio());
                preparedStatement.setString(4, socio.getNif());

                if (socio instanceof Estandar) {
                    preparedStatement.setString(5, ((Estandar) socio).getTipoSeguro().name());
                } else {
                    preparedStatement.setNull(5, Types.VARCHAR);
                }

                if (socio instanceof Federado) {
                    preparedStatement.setObject(6, socio.getIdFederacion(), Types.INTEGER);
                } else {
                    preparedStatement.setNull(6, Types.INTEGER);
                }

                preparedStatement.setObject(7, numSocioPadre, Types.INTEGER);
                preparedStatement.setBigDecimal(8, socio.getCuotaMensual());

                preparedStatement.executeUpdate();
                connection.commit();
                System.out.println("Socio insertado correctamente.");
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Metodo registrarSocio mediante procedimiento almacenado (Transaccion)

    public static void registrarSocioPA(String nombre, int tipoSocio, String nif, int idFederacion, Integer idSocioPadre, Object extra) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/producto3", "root", "Gecabo13bcn24021")) {
            // Desactivamos el auto-commit para iniciar la transacción
            conn.setAutoCommit(false);

            String sql = "{CALL registrarSocio(?, ?, ?, ?, ?, ?)}";
            try (CallableStatement stmt = conn.prepareCall(sql)) {
                // Establecemos los parámetros de entrada del procedimiento almacenado
                stmt.setString(1, nombre);
                stmt.setString(2, obtenerTipoSocio(tipoSocio));
                stmt.setString(3, nif);
                stmt.setInt(4, idFederacion);
                stmt.setObject(5, idSocioPadre);
                stmt.setString(6, obtenerTipoSeguro(extra));

                // Ejecutar el procedimiento
                stmt.executeUpdate();

                // Confirmamos la transacción
                conn.commit();
                System.out.println("Socio registrado correctamente.");
            } catch (SQLException e) {
                // Si hay un error, revertimos la transacción
                if (conn != null) {
                    try {
                        conn.rollback();
                        System.err.println("Transacción revertida. Error al ejecutar el procedimiento: " + e.getMessage());
                    } catch (SQLException rollbackEx) {
                        System.err.println("Error al hacer rollback: " + rollbackEx.getMessage());
                    }
                }
            } finally {
                // Restauramos el auto-commit
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error de conexión: " + e.getMessage());
        }
    }


/* //Sin transacción
    public static void registrarSocioPA(String nombre, int tipoSocio, String nif, int idFederacion, Integer idSocioPadre, Object extra) {
        // Aquí realizas la llamada al procedimiento almacenado en MySQL
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/producto3", "root", "Gecabo13bcn24021")) {
            String sql = "{CALL registrarSocio(?, ?, ?, ?, ?, ?)}";
            try (CallableStatement stmt = conn.prepareCall(sql)) {
                // Establecemos los parámetros de entrada del procedimiento almacenado
                stmt.setString(1, nombre);
                stmt.setString(2, obtenerTipoSocio(tipoSocio));  // Convierte el tipoSocio en el valor adecuado (ESTANDAR, FEDERADO, INFANTIL)
                stmt.setString(3, nif);
                stmt.setInt(4, idFederacion);
                stmt.setObject(5, idSocioPadre);  // Puede ser null si no es federado
                stmt.setString(6, obtenerTipoSeguro(extra));  // Obtén el tipo de seguro si aplica

                // Ejecutar el procedimiento
                stmt.executeUpdate();
                System.out.println("Socio registrado correctamente.");
            } catch (SQLException e) {
                System.err.println("Error al ejecutar el procedimiento: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.err.println("Error de conexión: " + e.getMessage());
        }
    }

    */

    private static String obtenerTipoSocio(int tipoSocio) {
        // Mapear el valor numérico a una cadena ENUM de tipoSocio
        switch (tipoSocio) {
            case 0:
                return "ESTANDAR";
            case 1:
                return "FEDERADO";
            case 2:
                return "INFANTIL";
            default:
                throw new IllegalArgumentException("Tipo de socio inválido.");
        }
    }

    private static String obtenerTipoSeguro(Object extra) {
        // Si el socio es Estandar, tenemos un seguro asociado
        if (extra instanceof Seguro) {
            Seguro seguro = (Seguro) extra;
            return seguro.getTipo().toString();  // Devuelve "BASICO" o "COMPLETO"
        }
        return null;  // No hay seguro si el socio no es Estandar
    }

    // Método para insertar federación con un ID específico si no existe
    private void insertarFederacionConId(Integer idFederacion, String nombreFederacion, Connection connection) throws SQLException {
        String query = "INSERT INTO federaciones (id_federacion, nombre) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, idFederacion);
            preparedStatement.setString(2, nombreFederacion);
            preparedStatement.executeUpdate();
            System.out.println("Federación insertada con ID " + idFederacion + " y Nombre = " + nombreFederacion);
        }
    }

    // Método para verificar si una federación existe
    private boolean federacionExiste(Integer idFederacion, Connection connection) throws SQLException {
        String query = "SELECT COUNT(*) FROM federaciones WHERE id_federacion = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, idFederacion);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        }
        return false;
    }

    // Método para eliminar un socio
    public boolean deleteSocio(int numeroSocio) throws SocioNoExisteException, SocioConInscripcionesException {
        if (socioTieneInscripciones(numeroSocio)) {
            throw new SocioConInscripcionesException("El socio con número " + numeroSocio + " tiene inscripciones activas y no puede ser eliminado.");
        }

        String query = "DELETE FROM Socios WHERE numeroSocio = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, numeroSocio);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 0) {
                throw new SocioNoExisteException("El socio con número " + numeroSocio + " no existe.");
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //Metodo para eliminar socio mediante procedimiento almacenado (Transacción)

    public static void eliminarSocioPA(int idSocio) {
        String url = "jdbc:mysql://127.0.0.1:3306/producto3";
        String usuario = "root";
        String contraseña = "Gecabo13bcn24021";

        try (Connection conn = DriverManager.getConnection(url, usuario, contraseña)) {
            conn.setAutoCommit(false);  // Iniciar transacción

            String sql = "{CALL eliminarSocio(?)}";

            try (CallableStatement stmt = conn.prepareCall(sql)) {
                stmt.setInt(1, idSocio);  // Configurar el parámetro idSocio

                stmt.executeUpdate();  // Ejecutar el procedimiento
                conn.commit();  // Confirmar la transacción
                System.out.println("Socio eliminado correctamente.");
            } catch (SQLException e) {
                System.err.println("Error al eliminar el socio: " + e.getMessage());
                conn.rollback();  // Revertir la transacción en caso de error
            }
        } catch (SQLException e) {
            System.err.println("Error de conexión a la base de datos: " + e.getMessage());
        }
    }


    // Método para verificar si un socio tiene inscripciones activas
    public boolean socioTieneInscripciones(int numeroSocio) {
        String query = "SELECT COUNT(*) FROM Inscripciones WHERE id_socio = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, numeroSocio);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Método para verificar si un socio existe
    public boolean socioExiste(int numeroSocio) {
        String query = "SELECT COUNT(*) FROM Socios WHERE numeroSocio = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, numeroSocio);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Método para obtener un socio por su número de socio
    public Socio getSocioByNumero(int numeroSocio) {
        String query = "SELECT * FROM Socios WHERE numeroSocio = ?";
        Socio socio = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, numeroSocio);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                socio = mapResultSetToSocio(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return socio;
    }

    // Método para obtener todos los socios
    public List<Socio> getAllSocios() {
        List<Socio> socios = new ArrayList<>();
        String query = "SELECT * FROM Socios";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                socios.add(mapResultSetToSocio(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return socios;
    }

    // Método para actualizar los datos de un socio
    public void updateSocio(Socio socio) {
        String query = "UPDATE Socios SET nombre = ?, cuota_mensual = ? WHERE numeroSocio = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, socio.getNombre());
            preparedStatement.setBigDecimal(2, socio.getCuotaMensual());
            preparedStatement.setInt(3, socio.getNumeroSocio());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para listar socios por tipo
    public List<Socio> listarSociosPorTipo(int tipoSocio) {
        List<Socio> socios = new ArrayList<>();
        String tipo = tipoSocio == 0 ? "ESTANDAR" : tipoSocio == 1 ? "FEDERADO" : "INFANTIL";
        String query = "SELECT * FROM Socios WHERE tipo_socio = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, tipo);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                socios.add(mapResultSetToSocio(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return socios;
    }

    // Método para listar socios por tipo mediante procedimiento almacenado (Transaccion)

    public static List<Socio> listarSociosPorTipoPA(int tipoSocio) {
        List<Socio> socios = new ArrayList<>();
        String tipo = tipoSocio == 0 ? "ESTANDAR" : tipoSocio == 1 ? "FEDERADO" : "INFANTIL";

        String query = "{CALL listarSociosPorTipo(?)}"; // Llamada al procedimiento almacenado

        Connection connection = null;
        CallableStatement callableStatement = null;

        try {
            connection = getConnection();
            connection.setAutoCommit(false); // Desactivar auto commit para controlar la transacción

            // Establecer el parámetro del procedimiento almacenado
            callableStatement = connection.prepareCall(query);
            callableStatement.setString(1, tipo);

            // Ejecutar el procedimiento y obtener los resultados
            ResultSet resultSet = callableStatement.executeQuery();

            // Procesar los resultados
            while (resultSet.next()) {
                socios.add(mapResultSetToSocio(resultSet)); // Asumiendo que mapResultSetToSocio() existe
            }

            // Confirmar los cambios si la transacción fue exitosa (aunque no modificamos datos en este caso)
            connection.commit();
        } catch (SQLException e) {
            // Si ocurre un error, hacer rollback para deshacer la transacción
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            e.printStackTrace();
        } finally {
            // Asegurarse de que los recursos se cierren
            if (callableStatement != null) {
                try {
                    callableStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.setAutoCommit(true); // Restaurar el auto commit
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return socios;
    }

    private static Socio mapResultSetToSocio(ResultSet resultSet) throws SQLException {
        int numeroSocio = resultSet.getInt("numeroSocio");
        String nombre = resultSet.getString("nombre");
        String tipoSocio = resultSet.getString("tipo_socio");
        String nif = resultSet.getString("nif");
        BigDecimal cuotaMensual = resultSet.getBigDecimal("cuota_mensual");

        TipoSeguro tipoSeguro = resultSet.getString("tipo_seguro") != null ? TipoSeguro.valueOf(resultSet.getString("tipo_seguro")) : null;
        Integer idFederacion = resultSet.getObject("id_federacion", Integer.class);
        Integer idSocioPadre = resultSet.getObject("id_socio_padre", Integer.class);

        switch (tipoSocio) {
            case "ESTANDAR":
                Seguro seguro = tipoSeguro != null ? new Seguro(tipoSeguro, 0.0f) : null;
                return new Estandar(numeroSocio, nombre, nif, seguro, cuotaMensual);

            case "FEDERADO":
                Federacion federacion = new FederacionDAO().getFederacionById(idFederacion);
                return new Federado(numeroSocio, nombre, nif, federacion, cuotaMensual);

            case "INFANTIL":
                return new Infantil(numeroSocio, nombre, idSocioPadre, cuotaMensual);

            default:
                throw new SQLException("Tipo de socio desconocido: " + tipoSocio);
        }
    }

    //Metodo para modificar los datos de un socio mediante procedimiento almacenado (Transacción)


    public static void modificarDatosSocioPA(int numeroSocio, String nuevoNombre) {
        Connection conn = null;
        CallableStatement stmt = null;
        try {
            conn = getConnection(); // Método para obtener la conexión a la base de datos
            conn.setAutoCommit(false); // Desactivar auto commit para manejar transacciones manualmente

            // Preparar la llamada al procedimiento almacenado
            stmt = conn.prepareCall("{CALL modificarDatosSocio(?, ?)}");
            stmt.setInt(1, numeroSocio);
            stmt.setString(2, nuevoNombre);

            // Ejecutar el procedimiento
            stmt.executeUpdate();

            // Si todo es correcto, hacer commit para confirmar los cambios
            conn.commit();
            MenuPrincipal.mostrarMensaje("Datos del socio actualizados con éxito.");
        } catch (SQLException e) {
            // Si ocurre un error, hacer rollback para deshacer la transacción
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    MenuPrincipal.mostrarError("Error al deshacer los cambios: " + ex.getMessage());
                }
            }

            // Manejo de excepciones
            if (e.getMessage().contains("El socio no existe")) {
                MenuPrincipal.mostrarError("Error: El socio no existe.");
            } else {
                MenuPrincipal.mostrarError("Error al actualizar los datos del socio: " + e.getMessage());
            }
        } finally {
            // Asegurarse de que la conexión se cierre correctamente
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    MenuPrincipal.mostrarError("Error al cerrar el statement: " + e.getMessage());
                }
            }
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Restaurar el auto commit
                    conn.close();
                } catch (SQLException e) {
                    MenuPrincipal.mostrarError("Error al cerrar la conexión: " + e.getMessage());
                }
            }
        }
    }
}
