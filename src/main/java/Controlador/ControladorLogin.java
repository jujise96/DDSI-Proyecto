/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Config.HibernateUtilMariaDB;
import Config.HibernateUtilOracle;
import Vista.VistaMensaje;
import Vista.vistaLogin;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.hibernate.SessionFactory;

/**
 *
 * @author jujis
 */
public class ControladorLogin implements ActionListener {

    private SessionFactory sessionFactory;

    String server;
    VistaMensaje vMensaje;
    vistaLogin vLogin;
    ControladorPrincipal controladorP;

    public ControladorLogin() {

        vMensaje = new VistaMensaje();
        vLogin = new vistaLogin();

        addListeners();

        vLogin.setLocationRelativeTo(null);
        vLogin.setVisible(true);

        vLogin.ListaDesplegable.setSelectedIndex(0);

    }

    private void conectarBD() {

        server = (String) (vLogin.ListaDesplegable.getSelectedItem());
        try {
            switch (server) {
                case "MariaDB":

                    sessionFactory = HibernateUtilMariaDB.getSessionFactory();
                    vMensaje.mostrarmensaje("", "Exito al cargar la BD", "Exito al cargar la base de datos " + server);

                    break;

                case "Oracle":

                    sessionFactory = HibernateUtilOracle.getSessionFactory();
                    vMensaje.mostrarmensaje("", "Exito al cargar la BD", "Exito al cargar la base de datos " + server);

                    break;

                default:
                    vMensaje.mostrarmensaje("Error", "Error al cargar la BD", "No se ha podido cargar la base de datos " + server);

            }
        } catch (ExceptionInInitializerError e) {

            vMensaje.mostrarmensaje("Error", "Error al cargar la BD", "No se ha podido cargar la base de datos " + server);
            
        }
    }

    private void desconectarBD() {

        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }

    }

    private void addListeners() {

        vLogin.BotonSalir.addActionListener(this);
        vLogin.BotonConectar.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Conectar":
            try {
                conectarBD();
            } catch (Exception ex) {
                vMensaje.mostrarmensaje("Error", "Error al conectar con la BD", "No se ha podido establecer la conexión con la base de datos.");
            } finally {
                vLogin.dispose();
                if (sessionFactory != null) {
                    controladorP = new ControladorPrincipal(sessionFactory.openSession());
                }
            }
            break;

            case "Salir":
                vLogin.dispose();
                System.exit(0);
                break;

            default:
                throw new AssertionError();
        }
    }

}
