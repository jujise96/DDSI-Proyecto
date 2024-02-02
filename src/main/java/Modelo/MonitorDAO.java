/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author jujis
 */
public class MonitorDAO {

    Session sesion;
    PreparedStatement ps = null;
    Scanner sc = new Scanner(System.in);

    public MonitorDAO(Session sesion) {
        this.sesion = sesion;
    }

    public ArrayList<Monitor> listaMonitores() throws Exception {

        Transaction transaccion = sesion.beginTransaction(); // Iniciamos transaccion con hibernate

        Query consulta = sesion.createNativeQuery("SELECT * FROM MONITOR M", Monitor.class);//obtenemos todos los monitores de la BD
        ArrayList<Monitor> monitores = (ArrayList<Monitor>) consulta.list();//almacenamos la lista de monitores en un array

        rellenarTablaMonitorPantalla(monitores);

        transaccion.commit();//confirmamos la transaccion con la BD

        return monitores;

    }

    public ArrayList<Monitor> listaMonitoresLetra(String Letra) throws Exception {

        Transaction transaccion = sesion.beginTransaction(); // Iniciamos transaccion con hibernate
        Letra += "%";
        Query consulta = sesion.createNativeQuery("SELECT * FROM MONITOR M "
                + "WHERE NOMBRE LIKE :letra", Monitor.class).setParameter("letra", Letra); //obtenemos todos los monitores de la BD
        ArrayList<Monitor> monitores = (ArrayList<Monitor>) consulta.list(); //almacenamos la lista de monitores en un array

        transaccion.commit(); //confirmamos la transaccion con la BD

        return monitores;

    }

    public void AltaMonitor(Monitor monitor) throws SQLException {

        Transaction transaccion = sesion.beginTransaction(); // Iniciamos transaccion con hibernate

        sesion.save(monitor); //Incorporamos el monitor en la BD
        transaccion.commit(); //confirmamos la transaccion con la BD

    }

    public boolean BajaMonitor(String codigo) throws SQLException {

        Transaction transaccion = sesion.beginTransaction(); // Iniciamos transaccion con hibernate
        boolean BajaSatisfactoria = false;

        try {
            Monitor monitor = sesion.get(Monitor.class, codigo);

            sesion.delete(monitor); //Eliminamos el monitor indicado de la BD
            transaccion.commit(); //confirmamos la transaccion con la BD
            BajaSatisfactoria = true; // confirmamos el exito al dar de baja

        } catch (Exception e) {
            transaccion.rollback(); //deshacemos los cambios
            System.out.println("Error al dar de de Baja al monitor.\n\n" + e.getMessage());
        }

        return BajaSatisfactoria; //retornamos si la baja fue o no fue satisfactoria

    }

    public void UpdateMonitor(Monitor monitor) throws SQLException {

        Monitor mon = sesion.get(Monitor.class, monitor.getCodMonitor()); // obtenemos el monitor indicado mediante el codigo monitor

        //obtenemos los datos del monitor y los incorporamos al monitor de la BD
        mon.setActividadesResponsable(monitor.getActividadesResponsable());
        mon.setCodMonitor(monitor.getCodMonitor());
        mon.setCorreo(monitor.getCorreo());
        mon.setDni(monitor.getDni());
        mon.setFechaEntrada(monitor.getFechaEntrada());
        mon.setNick(monitor.getNick());
        mon.setNombre(monitor.getNombre());
        mon.setTelefono(monitor.getTelefono());

        Transaction transaccion = sesion.beginTransaction(); // Iniciamos transaccion con hibernate
        sesion.save(mon); //Actualizamos el monitor en la BD

        transaccion.commit(); //confirmamos la transaccion con la BD

    }

    public boolean ExisteMonitor(String codigo) {

        Query consulta = sesion.createNativeQuery("SELECT CODMONITOR FROM MONITOR "
                + "WHERE CODMONITOR = :cod", Monitor.class).setParameter("cod", codigo); // seleccionamos el codmonitor de un monitor por medio del codmonitor que inciamos al metodo

        List resultado = consulta.list(); // almacenamos la lista de los resultados
        if (resultado.isEmpty()) {//en caso de existir una coincidencia indicamos verdadero en caso contrario falso
            return false;
        } else {
            return true;
        }

    }

    public Monitor BuscarMonitorPorCodigo(String CodigoMonitor) {
        Query consulta = sesion.createNamedQuery("Monitor.findByCodMonitor", Monitor.class).setParameter("codMonitor", CodigoMonitor); // obtenemos el monitor por medio del parametro codmonitor indicado al metodo

        Monitor resultado = (Monitor) consulta.getSingleResult(); //almacenamos el monitor obtenido
        return resultado;
    }

    public ArrayList<Monitor> listaMonitoresResponsables() throws Exception {

        System.out.println("Indique el codigo de la actividad deseaada: ");
        String Scodigo = sc.nextLine(); //obtenemos el codigo de la actividad deseada
        Transaction transaccion = sesion.beginTransaction(); // Iniciamos transaccion con hibernate

        Query consulta = sesion.createQuery("SELECT m FROM Monitor m JOIN m.actividadesResponsable a "
                + "WHERE a.idActividad = :idActividad").setParameter("idActividad", Scodigo);//obtenemos todos los monitores de la BD los cuales sean responsables de una actividad especificada
        
        ArrayList<Monitor> monitores = (ArrayList<Monitor>) consulta.list(); //almacenamos la lista de monitores en un array

        transaccion.commit(); //confirmamos la transaccion con la BD

        rellenarTablaMonitorPantalla(monitores); //dibujamos por pantalla los monitores obtenidos

        return monitores;

    }

    public void rellenarTablaMonitorPantalla(ArrayList<Monitor> monitores) {

        System.out.println(monitores.size());

        for (int i = 0; i < monitores.size(); i++) { //iteramos por el array de monitres

            //en caso de existir el dato deseado se muestra por pantalla
            if (monitores.get(i).getCodMonitor() != null) {
                System.out.println("Número de Monitor: " + monitores.get(i).getCodMonitor());
            }

            if (monitores.get(i).getNombre() != null) {
                System.out.println("Nombre: " + monitores.get(i).getNombre());
            }

            if (monitores.get(i).getDni() != null) {
                System.out.println("DNI: " + monitores.get(i).getDni());
            }

            if (monitores.get(i).getTelefono() != null) {
                System.out.println("Teléfono: " + monitores.get(i).getTelefono());
            }

            if (monitores.get(i).getCorreo() != null) {
                System.out.println("Correo: " + monitores.get(i).getCorreo());
            }

            if (monitores.get(i).getFechaEntrada() != null) {
                System.out.println("Fecha de Entrada: " + monitores.get(i).getFechaEntrada());
            }

            if (monitores.get(i).getNick() != null) {
                System.out.println("Nick: " + monitores.get(i).getNick());
            }

        }
    }

    public String SiguienteCodigo() {
        Query consulta = sesion.createNativeQuery("SELECT MAX(codMonitor) FROM MONITOR");//obtenemos el codigo mayor de los monitores en la BD
        String aux = (String) consulta.getSingleResult(); //almacenamos el codigo mayor

        aux = aux.replaceAll("[^0-9]", ""); // obtenemos la parte numerica
        int Nmonitor = Integer.parseInt(aux) + 1; //lo convertimos a integer y aumentamos en 1 el valor 
        String Smonitor = ""; //inicializamos el siguiente codigo monitor
        if (Nmonitor < 10) {//lo formateamos
            Smonitor = "00" + Nmonitor;
        } else if (Nmonitor < 100) {
            Smonitor = "0" + Nmonitor;
        }

        String numeroSocio = "M" + Smonitor; // incorporamos la etiqueta de monitor al codigo

        return numeroSocio; // retornamos el siguiente codigo de monitor al mayor ocupado
    }

}
