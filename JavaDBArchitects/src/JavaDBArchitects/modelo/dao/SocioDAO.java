package JavaDBArchitects.modelo.dao;

import JavaDBArchitects.modelo.*;
import JavaDBArchitects.modelo.conexion.DatabaseConnection;
import JavaDBArchitects.controlador.excepciones.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SocioDAO {

    // Método para agregar un nuevo socio
    public void addSocio(Socio socio) throws SocioYaExisteException {
        try (Connection connection = DatabaseConnection.getConnection()) {
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
                // Obtener el ID de la federación proporcionado por el usuario
                Federacion federacion = ((Federado) socio).getFederacion();
                Integer idFederacion = federacion.getId_federacion();
                String nombreFederacion = federacion.getNombre();

                // Verificar si la federación con ese ID existe; si no, crearla con ese mismo ID
                if (!federacionExiste(idFederacion, connection)) {
                    insertarFederacionConId(idFederacion, nombreFederacion, connection);  // Inserta la federación con el ID proporcionado
                }
                // Asegurarse de que el socio tenga el ID de federación correcto
                socio.setIdFederacion(idFederacion);
            } else if (socio instanceof Infantil) {
                numSocioPadre = socio.getIdSocioPadre();
                if (numSocioPadre != null && !socioExiste(numSocioPadre)) {
                    throw new IllegalArgumentException("Error: El socio padre o madre con ID " + numSocioPadre + " no existe.");
                }
            }

            // Proceder con la inserción del socio
            String query = "INSERT INTO Socios (numeroSocio, nombre, tipo_socio, nif, tipo_seguro, id_federacion, id_socio_padre) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, socio.getNumeroSocio());
                preparedStatement.setString(2, socio.getNombre());
                preparedStatement.setString(3, socio.getTipoSocio());
                preparedStatement.setString(4, socio.getNif());

                // Si es ESTANDAR, establecemos el tipo de seguro con el nombre del Enum
                if (socio instanceof Estandar) {
                    preparedStatement.setString(5, ((Estandar) socio).getTipoSeguro().name());
                } else {
                    preparedStatement.setNull(5, Types.VARCHAR);
                }

                // Si es FEDERADO, incluimos el id de federación proporcionado
                if (socio instanceof Federado) {
                    preparedStatement.setObject(6, socio.getIdFederacion(), Types.INTEGER);
                } else {
                    preparedStatement.setNull(6, Types.INTEGER);
                }

                // Si es INFANTIL, vinculamos el numSocioPadre, de lo contrario, lo dejamos nulo
                preparedStatement.setObject(7, numSocioPadre, Types.INTEGER);

                // Ejecutar la inserción del socio
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


    //Metodo registrarSocio mediante procedimiento almacenado

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

    private static String obtenerTipoSocio(int tipoSocio) {
        // Mapear el valor numérico a una cadena ENUM de tipoSocio
        switch (tipoSocio) {
            case 0: return "ESTANDAR";
            case 1: return "FEDERADO";
            case 2: return "INFANTIL";
            default: throw new IllegalArgumentException("Tipo de socio inválido.");
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
                return resultSet.getInt(1) > 0;  // Devuelve true si existe
            }
        }
        return false;
    }


    // Método para eliminar un socio por número de socio, lanzando SocioConInscripcionesException si tiene inscripciones activas
    public boolean deleteSocio(int numeroSocio) throws SocioNoExisteException, SocioConInscripcionesException {
        if (socioTieneInscripciones(numeroSocio)) {
            throw new SocioConInscripcionesException("El socio con número " + numeroSocio + " tiene inscripciones activas y no puede ser eliminado.");
        }

        String query = "DELETE FROM Socios WHERE numeroSocio = ?";
        try (Connection connection = DatabaseConnection.getConnection();
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

    // Método para verificar si un socio tiene inscripciones activas
    public boolean socioTieneInscripciones(int numeroSocio) {
        String query = "SELECT COUNT(*) FROM Inscripciones WHERE id_socio = ?";
        try (Connection connection = DatabaseConnection.getConnection();
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


    public void deleteAllSocios() {
        String query = "DELETE FROM Socios";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean socioExiste(int numeroSocio) {
        String query = "SELECT COUNT(*) FROM Socios WHERE numeroSocio = ?";
        try (Connection connection = DatabaseConnection.getConnection();
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
        try (Connection connection = DatabaseConnection.getConnection();
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

    public List<Socio> getAllSocios() {
        List<Socio> socios = new ArrayList<>();
        String query = "SELECT * FROM Socios";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int numeroSocio = resultSet.getInt("numeroSocio");
                String nombre = resultSet.getString("nombre");
                String NIF = resultSet.getString("NIF");
                int tipoSocio = resultSet.getInt("tipo_socio");

                Socio socio;
                switch (tipoSocio) {
                    case 0: // Estandar
                        // Convertir el tipo de seguro de String a TipoSeguro
                        TipoSeguro tipoSeguro = TipoSeguro.valueOf(resultSet.getString("seguro_tipo"));
                        float precioSeguro = resultSet.getFloat("seguro_precio");
                        Seguro seguro = new Seguro(tipoSeguro, precioSeguro);

                        socio = new Estandar(numeroSocio, nombre, NIF, seguro);
                        break;

                    case 1: // Federado
                        int idFederacion = resultSet.getInt("id_federacion");
                        String nombreFederacion = resultSet.getString("federacion_nombre");
                        Federacion federacion = new Federacion(idFederacion, nombreFederacion);

                        socio = new Federado(numeroSocio, nombre, NIF, federacion);
                        break;

                    case 2: // Infantil
                        int numeroSocioPadreOMadre = resultSet.getInt("numeroSocio_padre");
                        socio = new Infantil(numeroSocio, nombre, numeroSocioPadreOMadre);
                        break;

                    default:
                        throw new SQLException("Tipo de socio desconocido: " + tipoSocio);
                }

                socios.add(socio);


                socios.add(socio);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return socios;
    }

    public void updateSocio(Socio socio) {
        String query = "UPDATE Socios SET nombre = ? WHERE numeroSocio = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, socio.getNombre());
            preparedStatement.setInt(2, socio.getNumeroSocio());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Socio> listarSociosPorTipo(int tipoSocio) {
        List<Socio> socios = new ArrayList<>();
        String tipo = tipoSocio == 0 ? "ESTANDAR" : tipoSocio == 1 ? "FEDERADO" : "INFANTIL";
        String query = "SELECT * FROM Socios WHERE tipo_socio = ?";

        try (Connection connection = DatabaseConnection.getConnection();
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

    // Método para mapear el ResultSet a un objeto Socio
    private Socio mapResultSetToSocio(ResultSet resultSet) throws SQLException {
        int numeroSocio = resultSet.getInt("numeroSocio");
        String nombre = resultSet.getString("nombre");
        String tipoSocio = resultSet.getString("tipo_socio");
        String nif = resultSet.getString("nif");

        TipoSeguro tipoSeguro = resultSet.getString("tipo_seguro") != null ? TipoSeguro.valueOf(resultSet.getString("tipo_seguro")) : null;
        Integer idFederacion = resultSet.getObject("id_federacion", Integer.class);
        Integer idSocioPadre = resultSet.getObject("id_socio_padre", Integer.class);

        switch (tipoSocio) {
            case "ESTANDAR":
                Seguro seguro = tipoSeguro != null ? new Seguro(tipoSeguro, 0.0f) : null; // No intentamos acceder a "precio" aquí
                return new Estandar(numeroSocio, nombre, nif, seguro);
            case "FEDERADO":
                Federacion federacion = new FederacionDAO().getFederacionById(idFederacion);
                return new Federado(numeroSocio, nombre, nif, federacion);
            case "INFANTIL":
                return new Infantil(numeroSocio, nombre, idSocioPadre);
            default:
                throw new SQLException("Tipo de socio desconocido: " + tipoSocio);
        }
    }

}