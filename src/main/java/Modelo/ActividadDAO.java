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
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author jujis
 */
public class ActividadDAO {

    Session sesion = null;
    PreparedStatement ps = null;
    VistaSocio VActividad = new VistaSocio();
    Scanner sc = new Scanner(System.in);

    public ActividadDAO(Session sesion) {
        this.sesion = sesion;
    }

    public ArrayList<Actividad> listaActividades() throws Exception {

        Transaction transaccion = sesion.beginTransaction(); // Iniciamos transaccion con hibernate

        Query consulta = sesion.createNamedQuery("Actividad.findAll", Actividad.class); //obtenemos todas las actividades de la BD
        
        ArrayList<Actividad> actividades = (ArrayList<Actividad>) consulta.list(); //almacenamos la lista de actividades en un array

        transaccion.commit(); //confirmamos la transaccion con la BD

        return actividades;

    }

    public void darAltaSocioEnActividad(String numeroSocio, String codigoActividad) {
        
        Transaction transaccion = sesion.beginTransaction(); // Iniciamos transaccion con hibernate
        
        Socio socio = sesion.get(Socio.class, numeroSocio); // obtenemos al socio indicado por el numero de socio
        Actividad actividad = sesion.get(Actividad.class, codigoActividad); // obtenemos la actividad indicada por e codigoactividad
        
        if (socio != null && actividad != null) { // comproamos que tanto el socio como la actividad no sean nulas
            // Crear la consulta SQL para insertar en la tabla REALIZA
            String sql = "INSERT INTO REALIZA (numeroSocio, idActividad) VALUES (:numeroSocio, :idActividad)";
            sesion.createSQLQuery(sql)
                    .setParameter("numeroSocio", numeroSocio)
                    .setParameter("idActividad", codigoActividad)
                    .executeUpdate();

            transaccion.commit();  //confirmamos la transaccion con la BD
            System.out.println("Socio con número " + numeroSocio + " dado de alta en la actividad " + codigoActividad);
        } else {
            System.out.println("No se encontró un socio con el número " + numeroSocio + " o una actividad con el código " + codigoActividad);
        }
    }

    public void darBajaSocioDeActividad(String numeroSocio, String codigoActividad) {

        Transaction transaccion = sesion.beginTransaction(); // Iniciamos transaccion con hibernate
        
        Socio socio = sesion.get(Socio.class, numeroSocio); //obtenemos el socio indicado por el numero de socio 
        Actividad actividad = sesion.get(Actividad.class, codigoActividad); //obtenemos la actividad indicada por el numero de actividad

        if (socio != null && actividad != null) { //comprobamos que existen la actividad y el socio
            // Crear la consulta SQL para eliminar la fila en la tabla REALIZA
            String sql = "DELETE FROM REALIZA WHERE numeroSocio = :numeroSocio AND idActividad = :idActividad";
            sesion.createSQLQuery(sql)
                    .setParameter("numeroSocio", numeroSocio)
                    .setParameter("idActividad", codigoActividad)
                    .executeUpdate();

            transaccion.commit(); //confirmamos la transaccion con la BD
            System.out.println("Socio con número " + numeroSocio + " dado de baja de la actividad " + codigoActividad);
        } else {
            System.out.println("No se encontró un socio con el número " + numeroSocio + " o una actividad con el código " + codigoActividad);
        }
    }

    public void mostrarSociosDeActividad() {
        System.out.println("Indicar el código de actividad");
        String codigoActividad = sc.nextLine(); // recogemos el codigo de la catividad deseada

        Transaction transaccion = sesion.beginTransaction();  // Iniciamos transaccion con hibernate

        // Crear la consulta SQL nativa para obtener los socios inscritos en la actividad
        String sql = "SELECT s.* FROM SOCIO s "
                + "JOIN REALIZA r ON s.NUMEROSOCIO = r.NUMEROSOCIO "
                + "WHERE r.IDACTIVIDAD = :codigoActividad";

        // Ejecutar la consulta y obtener los resultados
        List<Object[]> resultados = sesion.createNativeQuery(sql)
                .setParameter("codigoActividad", codigoActividad)
                .list();

        // Mostrar los resultados
        for (Object[] resultado : resultados) {
            String nombre = (String) resultado[1];
            String telefono = (String) resultado[4]; // Ajustar índice según la estructura de la tabla
            System.out.println("Nombre: " + nombre + ", Teléfono: " + telefono);
        }

        transaccion.commit();  //confirmamos la transaccion con la BD
    }

    public void AltaActividad(Actividad actividad) throws SQLException {

    }

    public void BajaActividad(String codigo) throws SQLException {

    }

    public void UpdateActividad(String aux, Actividad actividad) throws SQLException {

    }

    public boolean ExisteActividad(Actividad actividad) {

        return false;

    }

    public void rellenarTablaActividad(ArrayList<Actividad> actividades) {

        System.out.println(actividades.size());

        for (int i = 0; i < actividades.size(); i++) { //iteramos en la lista de actividades

            //comprobamos que existe los atributos y los dibujamos
            
            if (actividades.get(i).getIdActividad() != null) {
                System.out.println("Número de actividad: " + actividades.get(i).getIdActividad());
            }

            if (actividades.get(i).getNombre() != null) {
                System.out.println("Nombre: " + actividades.get(i).getNombre());
            }

            if (actividades.get(i).getDescripcion() != null) {
                System.out.println("Descripcion: " + actividades.get(i).getDescripcion());
            }

            if (actividades.get(i).getPrecioBaseMes() >= 0) {
                System.out.println("Precio base mes: " + actividades.get(i).getPrecioBaseMes());
            }

            if (actividades.get(i).getMonitorResponsable() != null) {
                System.out.println("Monitor: " + actividades.get(i).getMonitorResponsable());
            }

        }
    }

    public String SiguienteCodigo() {
        
        Query consulta = sesion.createNativeQuery("SELECT MAX(idActividad) FROM ACTIVIDAD"); // generamos la consulta para obtener el numero mas alto de identificador
        String aux = (String) consulta.getSingleResult(); // almacenamos el codigo

        aux = aux.replaceAll("[^0-9]", "");//nos quedamos con la parte numerica
        int NActividad = Integer.parseInt(aux) + 1;//lo convertimos a integer e incrementamos
        String SActividad = "";//incializamos el codigo
        
        if(NActividad<10){ //formateamos
            SActividad = "00"+NActividad;
        }else if(NActividad<100){
            SActividad = "0"+NActividad;
        }
        
        String numeroSocio = "A" + SActividad; // incorporamos el identificador del codigo

        return numeroSocio; // retornamos el codigo siguiente al mayor
    
        
    
    }

}
