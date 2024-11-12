package JavaDBArchitects.modelo.dao;

import JavaDBArchitects.modelo.*;
import JavaDBArchitects.modelo.conexion.DatabaseConnection;
import JavaDBArchitects.controlador.excepciones.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;


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

    // Método para verificar si un socio existe
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

    // Método para obtener todos los socios
    public List<Socio> getAllSocios() {
        List<Socio> socios = new ArrayList<>();
        String query = "SELECT * FROM Socios";

        try (Connection connection = DatabaseConnection.getConnection();
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
        try (Connection connection = DatabaseConnection.getConnection();
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

    private Socio mapResultSetToSocio(ResultSet resultSet) throws SQLException {
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
}
