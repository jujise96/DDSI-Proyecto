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
public class SocioDAO {

    // Definición de variables de clase
    Session sesion = null;
    PreparedStatement ps = null;
    VistaSocio Vsocio = new VistaSocio();
    Scanner sc = new Scanner(System.in);

    public SocioDAO(Session sesion) {
        this.sesion = sesion;
    }

    public ArrayList<Socio> listaSociosHQL() throws SQLException {

        Transaction transaccion = sesion.beginTransaction(); // Iniciamos transaccion con hibernate

        Query consulta = sesion.createQuery("FROM Socio", Socio.class); // Consulta obtiene lista de Socios.
        
        ArrayList<Socio> socios = (ArrayList<Socio>) consulta.list(); // almacenamos los socios obtenidos en un array

        transaccion.commit(); //confirmamos la transaccion con la BD

        rellenarTablaSocios(socios); //llamamos al metodo para dibujar la tabla con los socios

        return socios;
    }

    public ArrayList<Socio> listaSociosSQLNativo() throws SQLException {

        Transaction transaccion = sesion.beginTransaction(); // Iniciamos transaccion con hibernate

        Query consulta = sesion.createNativeQuery("SELECT * FROM SOCIO", Socio.class); // Consulta obtiene lista de Socios.
        
        ArrayList<Socio> socios = (ArrayList<Socio>) consulta.list(); // almacenamos los socios obtenidos en un array
        
        transaccion.commit(); //confirmamos la transaccion con la BD

        rellenarTablaSocios(socios); //llamamos al metodo para dibujar la tabla con los socios

        return socios;

    }

    public ArrayList<Socio> listaSociosConsultaNombrada() throws SQLException {

        Transaction transaccion = sesion.beginTransaction(); // Iniciamos transaccion con hibernate

        Query consulta = sesion.createNamedQuery("Socio.findAll", Socio.class); // Consulta obtiene lista de Socios.
        
        ArrayList<Socio> socios = (ArrayList<Socio>) consulta.list(); // almacenamos los socios obtenidos en un array
        
        transaccion.commit(); //confirmamos la transaccion con la BD

        rellenarTablaSocios(socios); //llamamos al metodo para dibujar la tabla con los socios

        return socios;

    }

    public ArrayList<Object[]> listaSociosTelefonoyNombre() throws SQLException {

        Transaction transaccion = sesion.beginTransaction(); // Iniciamos transaccion con hibernate
        
        Query consulta = sesion.createNamedQuery("Socio.findNombreTelefono"); // Consulta obtiene lista de Socios con los parametros nombre y telefono unicamente.

        ArrayList<Object[]> Socios = (ArrayList<Object[]>) consulta.getResultList(); // almacenamos los socios obtenidos en un array
        
        transaccion.commit(); //confirmamos la transaccion con la BD

        rellenarTablaSocios(Socios, 1);//llamamos al metodo para dibujar la tabla con los socios - en este caso pasamos 1 para llamar al metodo que dibuja parcialmente

        return Socios;

    }

    public ArrayList<Object[]> listaSociosNombreyCategoria() throws SQLException {

        System.out.println("Indique la categoria deseaada: ");
        char Categoria = sc.nextLine().charAt(0); // recogemos el primer caracter de la frase
        
        Transaction transaccion = sesion.beginTransaction(); // Iniciamos transaccion con hibernate
        Query consulta = sesion.createNamedQuery("Socio.findNombreCategoria"); //lista de socios co nombre y categoria
        consulta.setParameter("categoria", Categoria); // indicamos el parametro categoria para la consulta
        ArrayList<Object[]> Socios = (ArrayList<Object[]>) consulta.getResultList(); // almacenamos los socios obtenidos en un array
        transaccion.commit(); /// //confirmamos la transaccion con la BD

        rellenarTablaSocios(Socios, 2); //llamamos al metodo para dibujar la tabla con los socios - en este caso pasamos 1 para llamar al metodo que dibuja parcialmente


        return Socios;

    }

    public List<Socio> listaSociosActividad() {

        System.out.println("Indique el codigo de la actividad deseaada: ");
        String Scodigo = sc.nextLine(); // recogemos el codigo de la actividad

        Transaction transaccion = sesion.beginTransaction(); // Iniciamos transaccion con hibernate

        Query consulta = sesion.createNativeQuery("SELECT SOCIO.* FROM SOCIO JOIN REALIZA ON SOCIO.NUMEROSOCIO = REALIZA.NUMEROSOCIO "
                + "WHERE REALIZA.IDACTIVIDAD = :cod", Socio.class).setParameter("cod", Scodigo);  //recogemos los socios en una actividad apartir de un cod que sera el recogido Scodigo

        ArrayList<Socio> socios = (ArrayList<Socio>) consulta.list(); // almacenamos los socios obtenidos en un array

        transaccion.commit(); //confirmamos la transaccion con la BD

        System.out.println("Cantidad de Socios encontrados: " + socios.size()); 

        for (int i = 0; i < socios.size(); i++) { // iteramos para ir dibujando los datos
            Socio socio = socios.get(i);
            if (socio != null && socio.getNombre() != null) {
                System.out.println("Socio " + i + " con nombre: " + socio.getNombre()); // en caso de tener informacion que mostrar
            } else {
                System.out.println("Socio " + i + " tiene información nula."); //en caso deno tener informacion que mostrar
            }
        }

        return socios;

    }

    public void AltaSocio(Socio socio) {

        Transaction transaccion = sesion.beginTransaction(); // Iniciamos transaccion con hibernate
        
        sesion.save(socio);// Guardar el socio pasado al metodo en la base de datos
        
        transaccion.commit();//confirmamos la transaccion con la BD

    }

    public void bajaSocio(String numeroSocio) {

        Transaction transaccion = sesion.beginTransaction(); // Iniciamos transaccion con hibernate

        Socio socio = sesion.get(Socio.class, numeroSocio); //obtenemos el socio indicado por el numero de socio - sirve para saber si existe

        if (socio != null) { // si el socio existe
            sesion.delete(socio); // eliminamos al socio indicado en la sesion
            transaccion.commit(); //confirmamos la transaccion con la BD
            System.out.println("Socio con número " + numeroSocio + " eliminado de la base de datos.");
        } else {// si el socio no existe
            System.out.println("No se encontró un socio con el número " + numeroSocio + ".");
        }
    }

    public void actualizarCategoriaSocio() {

        Transaction transaccion = sesion.beginTransaction(); // Iniciamos transaccion con hibernate
        System.out.println("Introduzca el número de socio:");
        String numeroSocio = sc.nextLine(); // recogemos el numero del socio deseado

        Socio socio = sesion.get(Socio.class, numeroSocio); //obtenemos el socio indicado por el numero de socio - sirve para saber si existe

        if (socio != null) { //si existe el socio
            System.out.println("Introduzca la nueva categoría:");
            char nuevaCategoria = Character.toUpperCase(sc.nextLine().charAt(0)); //recogemos el primer caracter en mayuscula indicado por pantalla

            socio.setCategoria(nuevaCategoria); // almacenamos la nueva categoria del socio
            sesion.update(socio); // actualizamos la informacion del socio en la sesion

            transaccion.commit(); //confirmamos la transaccion con la BD
            System.out.println("Categoría del socio con número " + numeroSocio + " actualizada a: " + nuevaCategoria);
        } else { //si no existe el socio
            System.out.println("No se encontró un socio con el número " + numeroSocio + ".");
        }
    }

    public void UpdateSocio(Socio socio) throws SQLException {

        Socio soc = sesion.get(Socio.class, socio.getNumeroSocio()); //obtenemos al socio de la sesion recogiendo el numero de socio directamente del socio deseado

        // obtenemos los datos del socio pasado al metodo y se lo ponemos al socio de la sesion
        
        soc.setNumeroSocio(socio.getNumeroSocio());
        soc.setCategoria(socio.getCategoria());
        soc.setActividades(socio.getActividades());
        soc.setCorreo(socio.getCorreo());
        soc.setDni(socio.getDni());
        soc.setFechaEntrada(socio.getFechaEntrada());
        soc.setFechaNacimiento(socio.getFechaNacimiento());
        soc.setNombre(socio.getNombre());
        soc.setTelefono(socio.getTelefono());

        Transaction transaccion = sesion.beginTransaction(); // Iniciamos transaccion con hibernate
        sesion.save(soc); //almacenamos en la sesion al nuevo socio actualizado

        transaccion.commit(); //confirmamos la transaccion con la BD

    }

    public void mostrarActividadesDeSocio() {
        System.out.println("Indicar el número de socio:");
        String numeroSocio = sc.nextLine(); // recogemos el numero de socio a trarae

        Transaction transaccion = sesion.beginTransaction(); // Iniciamos transaccion con hibernate

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

        transaccion.commit(); //confirmamos la transaccion con la BD

    }

    public boolean ExisteSocio(String numeroSocio) {
        Query consulta = sesion.createNativeQuery("SELECT COUNT(*) FROM SOCIO WHERE numeroSocio = :numeroSocio"); //recgemos los socios con el numero de socio indicado
        consulta.setParameter("numeroSocio", numeroSocio); // indicamos el parametro como el pasado al metodo

        int count = ((Number) consulta.getSingleResult()).intValue(); // obtenemos el numero que tienen dicho numerosocio
        return count > 0; //retornamos la cantidad (0 no exite, 1 existe)
    }

    public Socio SocioporNumeroSocio(String numeroSocio) {
        Query consulta = sesion.createNamedQuery("Socio.findByNumeroSocio", Socio.class).setParameter("numeroSocio", numeroSocio); //recogemos el socio que tiene el numero de socio indicado

        Socio socio = (Socio) consulta.getSingleResult(); //obtenemos el socio
        return socio;
    }

    public void rellenarTablaSocios(ArrayList<Socio> Socios) {

        System.out.println(Socios.size());

        for (int i = 0; i < Socios.size(); i++) { //iteramos por la lista de socios

            //comprobamos que el campo tiene informacion y la mostramos por pantalla
            
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

            for (Object[] socio : Socios) { // dibujamos por pantalla la informacion del array siendo el primer valor el nombre y el segundo el telefono
                String valor1 = (String) socio[0];
                String valor2 = (String) socio[1];

                System.out.println("Nombre: " + valor1 + ", Teléfono: " + valor2);

            }
        }

        if (diferencial == 2) {
            for (Object[] socio : Socios) { // dibujamos por pantalla la informacion del array siendo el primer valor el nombre y el segundo la categoria
                String valor1 = (String) socio[0];
                char valor2 = (char) socio[1];

                System.out.println("Nombre: " + valor1 + ", categoria: " + valor2);
            }
        }

    }

    public String SiguienteCodigo() {
        Query consulta = sesion.createNativeQuery("SELECT MAX(numeroSocio) FROM SOCIO"); // obtenemos de la BD el numero de socio mas alto
        String aux = (String) consulta.getSingleResult(); // almacenamos el numero

        aux = aux.replaceAll("[^0-9]", ""); //obtenemos nada mas que la parte numerica
        int Nsocio = Integer.parseInt(aux) + 1; //pasamos la parte numerica a integer y aumentamos en 1
        String SSocio = ""; //inicializamos el numero de socio

        if (Nsocio < 10) { //formatemos el numero de socio
            SSocio = "00" + Nsocio;
        } else if (Nsocio < 100) {
            SSocio = "0" + Nsocio;
        }

        String numeroSocio = "S" + SSocio; // devolvemos el indicado a la parte numerica

        return numeroSocio; // retornamos el siguiente numero de socio
    }

    public ArrayList<Socio> listarSociosenActividad(String codigo) {
        Query consulta = sesion.createNativeQuery("SELECT S.* FROM SOCIO S "
                + "JOIN REALIZA R ON S.numeroSocio = R.numeroSocio AND R.idActividad = :idActividad ", Socio.class)
                .setParameter("idActividad", codigo); // obtenemos una lista de socios inscritos a la actividad indicada por el codigo de actividad pasado al metodo

        ArrayList<Socio> sociosEnActividad = (ArrayList<Socio>) consulta.getResultList(); // almacenamos la informacion
        return sociosEnActividad;
    }

    public ArrayList<Socio> listarnoSociosenActividad(String codigo) {

        Query consulta = sesion.createNativeQuery("SELECT S.* FROM SOCIO S "
                + "LEFT JOIN REALIZA R ON S.numeroSocio = R.numeroSocio AND R.idActividad = :idActividad "
                + "WHERE R.numeroSocio IS NULL", Socio.class).setParameter("idActividad", codigo); // obtenemos una lista de socios no inscritos a la actividad indicada por el codigo de actividad pasado al metodo


        ArrayList<Socio> sociosNoInscritos = (ArrayList<Socio>) consulta.getResultList(); // almacenamos la informacion
        return sociosNoInscritos;
    }

    public ArrayList<Socio> SocioporNombre(String nombreSocio) {

        Query consulta = sesion.createNamedQuery("Socio.findByAlgodelNombre", Socio.class).setParameter("nombre", "%" + nombreSocio + "%"); // Utilizamos '%' para buscar coincidencias parciales

        ArrayList<Socio> socio = (ArrayList<Socio>) consulta.getResultList(); // almacenamos la informacion
        return socio;

    }

}
