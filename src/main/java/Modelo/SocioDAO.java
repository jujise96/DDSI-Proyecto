/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import Vista.VistaSocio;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.persistence.NoResultException;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author jujis
 */
public class SocioDAO {

    Session sesion = null;
    PreparedStatement ps = null;
    VistaSocio Vsocio = new VistaSocio();
    Scanner sc = new Scanner(System.in);

    public SocioDAO(Session sesion) {
        this.sesion = sesion;
    }

    public ArrayList<Socio> listaSociosHQL() throws SQLException {

        Transaction transaccion = sesion.beginTransaction();

        Query consulta = sesion.createQuery("FROM Socio", Socio.class);
        ArrayList<Socio> socios = (ArrayList<Socio>) consulta.list();

        transaccion.commit();

        rellenarTablaSocios(socios);

        return socios;
    }

    public ArrayList<Socio> listaSociosSQLNativo() throws SQLException {

        Transaction transaccion = sesion.beginTransaction();

        Query consulta = sesion.createNativeQuery("SELECT * FROM SOCIO", Socio.class);
        ArrayList<Socio> socios = (ArrayList<Socio>) consulta.list();

        transaccion.commit();

        rellenarTablaSocios(socios);

        return socios;

    }

    public ArrayList<Socio> listaSociosConsultaNombrada() throws SQLException {

        Transaction transaccion = sesion.beginTransaction();

        Query consulta = sesion.createNamedQuery("Socio.findAll", Socio.class);
        ArrayList<Socio> socios = (ArrayList<Socio>) consulta.list();

        transaccion.commit();

        rellenarTablaSocios(socios);

        return socios;

    }

    public ArrayList<Object[]> listaSociosTelefonoyNombre() throws SQLException {

        Transaction transaccion = sesion.beginTransaction();
        Query consulta = sesion.createNamedQuery("Socio.findNombreTelefono");
        ArrayList<Object[]> Socios = (ArrayList<Object[]>) consulta.getResultList();
        transaccion.commit();

        rellenarTablaSocios(Socios, 1);

        return Socios;

    }

    public ArrayList<Object[]> listaSociosNombreyCategoria() throws SQLException {

        System.out.println("Indique la categoria deseaada: ");
        char Categoria = sc.nextLine().charAt(0);

        Transaction transaccion = sesion.beginTransaction();
        Query consulta = sesion.createNamedQuery("Socio.findNombreCategoria");
        consulta.setParameter("categoria", Categoria);
        ArrayList<Object[]> Socios = (ArrayList<Object[]>) consulta.getResultList();
        transaccion.commit();

        rellenarTablaSocios(Socios, 2);

        return Socios;

    }

    public List<Socio> listaSociosActividad() {

        System.out.println("Indique el codigo de la actividad deseaada: ");
        String Scodigo = sc.nextLine();

        Transaction transaccion = sesion.beginTransaction();

        Query consulta = sesion.createNativeQuery("SELECT SOCIO.* FROM SOCIO JOIN REALIZA ON SOCIO.NUMEROSOCIO = REALIZA.NUMEROSOCIO "
                + "WHERE REALIZA.IDACTIVIDAD = :cod", Socio.class).setParameter("cod", Scodigo);

        ArrayList<Socio> socios = (ArrayList<Socio>) consulta.list();

        transaccion.commit();

        System.out.println("Cantidad de Socios encontrados: " + socios.size());

        for (int i = 0; i < socios.size(); i++) {
            Socio socio = socios.get(i);
            if (socio != null && socio.getNombre() != null) {
                System.out.println("Socio " + i + " con nombre: " + socio.getNombre());
            } else {
                System.out.println("Socio " + i + " tiene información nula.");
            }
        }

        return socios;

    }

    public boolean AltaSocio(Socio socio) {

        Transaction transaccion = sesion.beginTransaction();

        // Guardar el socio en la base de datos
        try {
            sesion.save(socio);
            transaccion.commit();
            return true;
        } catch (Exception e) {
            transaccion.rollback();
            System.out.println("Error al dar de alta al socio.\n\n" + e.getMessage());
            return false;
        }

    }

    public void bajaSocio(String numeroSocio) {

        Transaction transaccion = sesion.beginTransaction();

        Socio socio = sesion.get(Socio.class, numeroSocio);

        if (socio != null) {
            sesion.delete(socio);
            transaccion.commit();
            System.out.println("Socio con número " + numeroSocio + " eliminado de la base de datos.");
        } else {
            System.out.println("No se encontró un socio con el número " + numeroSocio + ".");
        }
    }

    public void actualizarCategoriaSocio() {

        Transaction transaccion = sesion.beginTransaction();
        System.out.println("Introduzca el número de socio:");
        String numeroSocio = sc.nextLine();

        Socio socio = sesion.get(Socio.class, numeroSocio);

        if (socio != null) {
            System.out.println("Introduzca la nueva categoría:");
            char nuevaCategoria = Character.toUpperCase(sc.nextLine().charAt(0));

            socio.setCategoria(nuevaCategoria);
            sesion.update(socio);

            transaccion.commit();
            System.out.println("Categoría del socio con número " + numeroSocio + " actualizada a: " + nuevaCategoria);
        } else {
            System.out.println("No se encontró un socio con el número " + numeroSocio + ".");
        }
    }

    public void UpdateSocio(Socio socio) throws SQLException {
        
        Socio soc = sesion.get(Socio.class, socio.getNumeroSocio());

        soc.setNumeroSocio(socio.getNumeroSocio());
        soc.setCategoria(socio.getCategoria());
        soc.setActividades(socio.getActividades());
        soc.setCorreo(socio.getCorreo());
        soc.setDni(socio.getDni());
        soc.setFechaEntrada(socio.getFechaEntrada());
        soc.setFechaNacimiento(socio.getFechaNacimiento());
        soc.setNombre(socio.getNombre());
        soc.setTelefono(socio.getTelefono());

        Transaction transaccion = sesion.beginTransaction();
        sesion.save(soc);

        transaccion.commit();

    }

    public void mostrarActividadesDeSocio() {
        System.out.println("Indicar el número de socio:");
        String numeroSocio = sc.nextLine();

        Transaction transaccion = sesion.beginTransaction();

        // Crear la consulta SQL nativa para obtener las actividades inscritas por el socio
        String sql = "SELECT a.* FROM ACTIVIDAD a "
                + "JOIN REALIZA r ON a.IDACTIVIDAD = r.IDACTIVIDAD "
                + "WHERE r.NUMEROSOCIO = :numeroSocio";

        // Ejecutar la consulta y obtener los resultados
        List<Actividad> actividades = sesion.createNativeQuery(sql, Actividad.class)
                .setParameter("numeroSocio", numeroSocio)
                .list();

        // Mostrar los resultados
        for (int i = 0; i < actividades.size(); i++) {
            System.out.println("Nombre de la actividad: " + actividades.get(i).getNombre() + ", Precio: " + actividades.get(i).getPrecioBaseMes());
        }

        transaccion.commit();

    }

    public boolean ExisteSocio(String numeroSocio) {
        Query consulta = sesion.createNativeQuery("SELECT COUNT(*) FROM SOCIO WHERE numeroSocio = :numeroSocio");
        consulta.setParameter("numeroSocio", numeroSocio);

        int count = ((Number) consulta.getSingleResult()).intValue();
        return count > 0;
    }

    public Socio SocioporNumeroSocio(String numeroSocio) {
        Query consulta = sesion.createNamedQuery("Socio.findByNumeroSocio", Socio.class).setParameter("numeroSocio", numeroSocio);

        Socio socio = (Socio) consulta.getSingleResult();
        return socio;
    }

    public void rellenarTablaSocios(ArrayList<Socio> Socios) {

        System.out.println(Socios.size());

        for (int i = 0; i < Socios.size(); i++) {

            if (Socios.get(i).getNumeroSocio() != null) {
                System.out.println("Número de Socio: " + Socios.get(i).getNumeroSocio());
            }

            if (Socios.get(i).getNombre() != null) {
                System.out.println("Nombre: " + Socios.get(i).getNombre());
            }

            if (Socios.get(i).getDni() != null) {
                System.out.println("DNI: " + Socios.get(i).getDni());
            }

            if (Socios.get(i).getFechaNacimiento() != null) {
                System.out.println("Fecha de Nacimiento: " + Socios.get(i).getFechaNacimiento());
            }

            if (Socios.get(i).getTelefono() != null) {
                System.out.println("Teléfono: " + Socios.get(i).getTelefono());
            }

            if (Socios.get(i).getCorreo() != null) {
                System.out.println("Correo: " + Socios.get(i).getCorreo());
            }

            if (Socios.get(i).getFechaEntrada() != null) {
                System.out.println("Fecha de Entrada: " + Socios.get(i).getFechaEntrada());
            }

            if (Socios.get(i).getCategoria() != null) {
                System.out.println("Categoría: " + Socios.get(i).getCategoria());
            }

        }
    }

    public void rellenarTablaSocios(ArrayList<Object[]> Socios, int diferencial) {
        if (diferencial == 1) {

            for (Object[] socio : Socios) {
                String valor1 = (String) socio[0];
                String valor2 = (String) socio[1];

                System.out.println("Nombre: " + valor1 + ", Teléfono: " + valor2);

            }
        }

        if (diferencial == 2) {
            for (Object[] socio : Socios) {
                String valor1 = (String) socio[0];
                char valor2 = (char) socio[1];

                System.out.println("Nombre: " + valor1 + ", categoria: " + valor2);
            }
        }

    }

    public String SiguienteCodigo() {
        Query consulta = sesion.createNativeQuery("SELECT MAX(numeroSocio) FROM SOCIO");
        String aux = (String) consulta.getSingleResult();

        aux = aux.replaceAll("[^0-9]", "");
        int Nsocio = Integer.parseInt(aux) + 1;
        String SSocio = "";
        
        if(Nsocio<10){
            SSocio = "00"+Nsocio;
        }else if(Nsocio<100){
            SSocio = "0"+Nsocio;
        }
        
        String numeroSocio = "S" + SSocio;

        return numeroSocio;
    }

    public ArrayList<Socio> listarSociosenActividad(String codigo) {
        Query consulta = sesion.createNativeQuery("SELECT S.* FROM SOCIO S "
                + "JOIN REALIZA R ON S.numeroSocio = R.numeroSocio AND R.idActividad = :idActividad ", Socio.class)
                .setParameter("idActividad", codigo);

        ArrayList<Socio> sociosEnActividad = (ArrayList<Socio>) consulta.getResultList();
        return sociosEnActividad;
    }

    public ArrayList<Socio> listarnoSociosenActividad(String codigo) {

        Query consulta = sesion.createNativeQuery("SELECT S.* FROM SOCIO S "
                + "LEFT JOIN REALIZA R ON S.numeroSocio = R.numeroSocio AND R.idActividad = :idActividad "
                + "WHERE R.numeroSocio IS NULL", Socio.class).setParameter("idActividad", codigo);

        ArrayList<Socio> sociosNoInscritos = (ArrayList<Socio>) consulta.getResultList();
        return sociosNoInscritos;
    }

    public ArrayList<Socio> SocioporNombre(String nombreSocio){
        
        Query consulta = sesion.createNamedQuery("Socio.findByNombre", Socio.class).setParameter("nombre", nombreSocio);

        ArrayList<Socio> socio = (ArrayList<Socio>) consulta.getResultList();
        return socio;

    }

}
