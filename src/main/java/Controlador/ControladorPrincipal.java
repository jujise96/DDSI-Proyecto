/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.hibernate.Session;
import Vista.*;
import java.awt.CardLayout;

/**
 *
 * @author jujis
 */
class ControladorPrincipal implements ActionListener {

    Session sesion;
    VentanaPrincipal vPrincipal = new VentanaPrincipal();
    private Vista.VistaMonitor vMonitor = new VistaMonitor();
    private Vista.VistaSocio vSocio = new VistaSocio();
    private Vista.VistaActividades vActividades = new VistaActividades();

    public ControladorPrincipal(Session sesion) {
        this.sesion = sesion;

        addListeners();

        vPrincipal.setLocationRelativeTo(null);
        vPrincipal.setVisible(true);
        vPrincipal.getContentPane().setLayout(new CardLayout());

        vMonitor.setVisible(false);
        vSocio.setVisible(false);
        vActividades.setVisible(false);
    }

    public void addListeners() {

        vPrincipal.SalirApp.addActionListener(this);
        vPrincipal.MenuGestionMonitores.addActionListener(this);
        vPrincipal.MenuGestionSocios.addActionListener(this);
        vPrincipal.MenuGestionActividades.addActionListener(this);
        vPrincipal.VolverMenuPrincipal.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()) {
            case "GestionMonitores":
                vPrincipal.remove(vSocio);
                vPrincipal.remove(vActividades);
                vPrincipal.add(vMonitor);
                vMonitor.setVisible(true);
                ControladorMonitores Cmonitores = new ControladorMonitores(sesion, vMonitor);
                break;

            case "GestionSocios":
                vPrincipal.remove(vMonitor);
                vPrincipal.add(vSocio);
                vPrincipal.remove(vActividades);
                vSocio.setVisible(true);
                ControladorSocio Csocio = new ControladorSocio(sesion, vSocio);
                break;

            case "GestionActividades":
                vPrincipal.remove(vSocio);
                vPrincipal.remove(vMonitor);
                vPrincipal.add(vActividades);
                vActividades.setVisible(true);
                ControladorActividades Cactividades = new ControladorActividades(sesion, vActividades);
                break;

            case "VolverMenuPrincipal":
                vMonitor.setVisible(false);
                vSocio.setVisible(false);
                vActividades.setVisible(false);
                
                vPrincipal.remove(vSocio);
                vPrincipal.remove(vActividades);
                vPrincipal.remove(vMonitor);
                break;

            case "Salir":
                vPrincipal.dispose();
                System.exit(0);
                break;

            default:
                throw new AssertionError();
        }

    }

}
