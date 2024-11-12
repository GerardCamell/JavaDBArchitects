package JavaDBArchitects.modelo.dao;

public class DAOFactory {
    public static SocioDAO getSocioDAO() {
        return new SocioDAO();
    }

    public static SeguroDAO getSeguroDAO() {
        return new SeguroDAO();
    }

    public static ExcursionDAO getExcursionDAO() {
        return new ExcursionDAO();
    }

    public static FederacionDAO getFederacion() {
        return new FederacionDAO();
    }

    public static InscripcionDAO getInscripcionDAO() {
        return new InscripcionDAO();
    }
}
