package JavaDBArchitects.modelo.dao;

import JavaDBArchitects.modelo.Inscripcion;
import JavaDBArchitects.modelo.Socio;
import JavaDBArchitects.modelo.Excursion;
import JavaDBArchitects.modelo.conexion.DatabaseConnection;
import JavaDBArchitects.controlador.excepciones.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static JavaDBArchitects.modelo.conexion.DatabaseConnection.getConnection;

public class InscripcionDAO {

    public void addInscripcion(Inscripcion inscripcion) throws InscripcionYaExisteException, FechaInvalidaException {
        if (inscripcionExiste(inscripcion.getSocio().getNumeroSocio(), inscripcion.getExcursion().getIdExcursion())) {
            throw new InscripcionYaExisteException("El socio ya está inscrito en esta excursión.");
        }

        // Obtener la fecha de la excursión desde la base de datos
        Date fechaExcursionSQL = obtenerFechaExcursion(inscripcion.getExcursion().getIdExcursion());
        if (fechaExcursionSQL == null) {
            throw new FechaInvalidaException("No se pudo encontrar la fecha de la excursión.");
        }

        // Convertimos las fechas a LocalDate para comparar
        LocalDate fechaInscripcion = inscripcion.getFecha_inscripcion().toLocalDate();
        LocalDate fechaExcursion = fechaExcursionSQL.toLocalDate();

        // Validación de la fecha de inscripción
        if (fechaInscripcion.isAfter(fechaExcursion)) {
            System.out.println("Error: La fecha de inscripción es posterior a la fecha de la excursión.");
            throw new FechaInvalidaException("La fecha de inscripción no puede ser posterior a la fecha de la excursión.");
        }

        System.out.println("Validación de fecha de inscripción superada. Procediendo a insertar inscripción.");

        String query = "INSERT INTO Inscripciones (id_socio, id_excursion, fecha_inscripcion) VALUES (?, ?, ?)";
        try (Connection connection = getConnection()) {
            // Iniciar la transacción
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setInt(1, inscripcion.getSocio().getNumeroSocio());
                preparedStatement.setString(2, inscripcion.getExcursion().getIdExcursion());
                preparedStatement.setDate(3, inscripcion.getFecha_inscripcion());

                preparedStatement.executeUpdate();

                // Obtener el id generado para la inscripción
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    inscripcion.setNumInscripcion(String.valueOf(generatedKeys.getInt(1)));
                }

                // Si queremos confirmar transacción
                connection.commit();
                System.out.println("Inscripción insertada correctamente en la base de datos.");

            } catch (SQLException e) {
                // Si algo va mal, volvemos para atras y la operación no se hace.
                connection.rollback();
                System.out.println("Error en la inserción de inscripción. La operación se ha revertido.");
                e.printStackTrace();
            } finally {
                // restablecemos
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Metodo para inscribir en una excursion mediante procedimiento almacenado (Transacción)

    public static void inscribirEnExcursionPA(int idSocio, String idExcursion, LocalDate fechaInscripcion) {
        String url = "jdbc:mysql://127.0.0.1:3306/producto3";
        String usuario = "root";
        String contraseña = "Gecabo13bcn24021";

        // Convertimos LocalDate a SQL Date para la base de datos
        java.sql.Date fechaSQL = java.sql.Date.valueOf(fechaInscripcion);

        // Iniciamos la conexión y la transacción
        try (Connection conn = DriverManager.getConnection(url, usuario, contraseña)) {
            // Iniciar la transacción
            conn.setAutoCommit(false);

            String sql = "{CALL inscribirEnExcursion(?, ?, ?)}";

            try (CallableStatement stmt = conn.prepareCall(sql)) {
                // Configurar los parámetros de entrada para el procedimiento almacenado
                stmt.setInt(1, idSocio);
                stmt.setString(2, idExcursion);
                stmt.setDate(3, fechaSQL);

                // Ejecutar el procedimiento
                stmt.executeUpdate();
                System.out.println("Inscripción registrada correctamente.");

                // Confirmar la transacción si no hubo errores
                conn.commit();
            } catch (SQLException e) {
                System.err.println("Error al inscribir en la excursión: " + e.getMessage());
                try {
                    // En caso de error, revertimos la transacción
                    conn.rollback();
                    System.out.println("La transacción se ha revertido debido a un error.");
                } catch (SQLException rollbackEx) {
                    System.err.println("Error al revertir la transacción: " + rollbackEx.getMessage());
                }
            }
        } catch (SQLException e) {
            System.err.println("Error de conexión a la base de datos: " + e.getMessage());
        }
    }


    // Método para obtener inscripciones de un socio específico
    public List<Inscripcion> getInscripcionesBySocio(int numeroSocio) {
        List<Inscripcion> inscripciones = new ArrayList<>();
        String query = "SELECT * FROM Inscripciones WHERE id_socio = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, numeroSocio);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String idInscripcion = resultSet.getString("id_inscripcion");
                int idSocio = resultSet.getInt("id_socio");
                String idExcursion = resultSet.getString("id_excursion");
                Date fechaInscripcion = resultSet.getDate("fecha_inscripcion");

                Socio socio = new SocioDAO().getSocioByNumero(idSocio);
                Excursion excursion = new ExcursionDAO().getExcursionById(idExcursion);

                Inscripcion inscripcion = new Inscripcion(idInscripcion, socio, excursion, fechaInscripcion);
                inscripciones.add(inscripcion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inscripciones;
    }


    // Método para obtener una inscripción por su número de inscripción
    public Inscripcion getInscripcionById(String numInscripcion) {
        String query = "SELECT * FROM Inscripciones WHERE id_inscripcion = ?";
        Inscripcion inscripcion = null;

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, numInscripcion);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String idInscripcion = resultSet.getString("id_inscripcion");
                int idSocio = resultSet.getInt("id_socio");
                String idExcursion = resultSet.getString("id_excursion");
                Date fechaInscripcion = resultSet.getDate("fecha_inscripcion");

                Socio socio = new SocioDAO().getSocioByNumero(idSocio); // Asegúrate de que este método exista en SocioDAO
                Excursion excursion = new ExcursionDAO().getExcursionById(idExcursion); // Asegúrate de que este método exista en ExcursionDAO

                inscripcion = new Inscripcion(idInscripcion, socio, excursion, fechaInscripcion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inscripcion;
    }


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

    // Método para obtener todas las inscripciones
    public List<Inscripcion> getAllInscripciones() {
        List<Inscripcion> inscripciones = new ArrayList<>();
        String query = "SELECT * FROM Inscripciones";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String idInscripcion = resultSet.getString("id_inscripcion");
                int idSocio = resultSet.getInt("id_socio");
                String idExcursion = resultSet.getString("id_excursion");
                Date fechaInscripcion = resultSet.getDate("fecha_inscripcion");

                Socio socio = new SocioDAO().getSocioByNumero(idSocio);
                Excursion excursion = new ExcursionDAO().getExcursionById(idExcursion);

                Inscripcion inscripcion = new Inscripcion(idInscripcion, socio, excursion, fechaInscripcion);
                inscripciones.add(inscripcion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inscripciones;
    }


    public boolean excursionTieneInscripciones(Excursion excursion) {
        String query = "SELECT COUNT(*) FROM Inscripciones WHERE id_excursion = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, excursion.getIdExcursion());
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    // Método para eliminar una inscripción por su número de inscripción
    public boolean eliminarInscripcion(String numInscripcion) throws InscripcionNoExisteException, CancelacionInvalidaException {
        String query = "DELETE FROM Inscripciones WHERE id_inscripcion = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Obtener la fecha de la excursión asociada a esta inscripción
            Date fechaExcursion = obtenerFechaExcursion(numInscripcion);

            // Comprobar si la excursión ya ha sido realizada (es anterior a la fecha actual)
            if (fechaExcursion != null && fechaExcursion.before(new java.sql.Date(System.currentTimeMillis()))) {
                throw new CancelacionInvalidaException("La excursión ya ha sido realizada y no puede cancelarse.");
            }

            preparedStatement.setString(1, numInscripcion);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 0) {
                throw new InscripcionNoExisteException("La inscripción con ID " + numInscripcion + " no existe.");
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //Metodo para eliminar una inscripción mediante procedimiento almacenado (Transacción)

    public static boolean eliminarInscripcionPA(int idInscripcion) {

        // Declarar la conexión y la llamada al procedimiento almacenado
        Connection conn = null;
        CallableStatement stmt = null;
        boolean eliminado = false;

        try {
            // Conectar a la base de datos
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/producto3", "root", "Gecabo13bcn24021");
            conn.setAutoCommit(false);  // Iniciar la transacción

            // Llamar al procedimiento almacenado
            String sql = "{CALL eliminarInscripcion(?)}";
            stmt = conn.prepareCall(sql);

            // Configurar el parámetro de entrada
            stmt.setInt(1, idInscripcion);

            // Ejecutar el procedimiento
            int rowsAffected = stmt.executeUpdate();

            // Confirmar la transacción si se eliminó alguna fila
            if (rowsAffected > 0) {
                conn.commit();
                eliminado = true;
                System.out.println("Inscripción eliminada correctamente.");
            } else {
                System.out.println("No se encontró ninguna inscripción con el ID proporcionado.");
                conn.rollback();  // Deshacer si no se eliminó ninguna fila
            }

        } catch (SQLException e) {
            // Manejar errores de SQL
            System.err.println("Error al eliminar inscripción: " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback();  // Deshacer en caso de error
                }
            } catch (SQLException rollbackEx) {
                System.err.println("Error al hacer rollback: " + rollbackEx.getMessage());
            }

        } finally {
            // Cerrar los recursos
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }

        return eliminado;
    }

    //Metodo para listar inscripciones mediante procedimiento almacenado

    public static void listarInscripcionesPA() {
        Connection conn = null;
        CallableStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/producto3", "root", "Gecabo13bcn24021");

            String sql = "{CALL listarInscripciones()}";
            stmt = conn.prepareCall(sql);

            // Ejecutar el procedimiento almacenado y obtener el resultado
            rs = stmt.executeQuery();

            // Procesar el resultado
            while (rs.next()) {
                int idInscripcion = rs.getInt("id_inscripcion");
                int idSocio = rs.getInt("id_socio");
                String nombreSocio = rs.getString("nombre_socio");
                String idExcursion = rs.getString("id_excursion");
                String descripcionExcursion = rs.getString("descripcion_excursion");
                String fechaInscripcion = rs.getDate("fecha_inscripcion").toString();

                System.out.println("ID Inscripción: " + idInscripcion);
                System.out.println("ID Socio: " + idSocio + " - Nombre Socio: " + nombreSocio);
                System.out.println("ID Excursión: " + idExcursion + " - Descripción Excursión: " + descripcionExcursion);
                System.out.println("Fecha Inscripción: " + fechaInscripcion);
                System.out.println("------------------------------------");
            }

        } catch (SQLException e) {
            System.err.println("Error al listar inscripciones: " + e.getMessage());
        } finally {
            // Cerrar los recursos
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
    }



            // Método auxiliar para obtener la fecha de una excursión asociada a una inscripción específica
    private Date obtenerFechaExcursion(String idExcursion) {
        String query = "SELECT fecha FROM Excursiones WHERE idExcursion = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, idExcursion);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Date fecha = resultSet.getDate("fecha");
                System.out.println("Fecha de excursión desde BD: " + fecha);
                return fecha;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    // Método para generar un número único de inscripción basado en el valor máximo de la tabla
    public int generarNumeroInscripcion() {
        String query = "SELECT MAX(CAST(id_inscripcion AS UNSIGNED)) FROM Inscripciones";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) + 1; // Devuelve el siguiente número disponible
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1; // Retorna 1 si no hay registros
    }
    public boolean inscripcionExiste(int numeroSocio, String idExcursion) {
        String query = "SELECT COUNT(*) FROM Inscripciones WHERE id_socio = ? AND id_excursion = ?";
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, numeroSocio);
            preparedStatement.setString(2, idExcursion);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}


