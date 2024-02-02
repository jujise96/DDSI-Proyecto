/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Actividad;
import Modelo.ActividadDAO;
import Modelo.Socio;
import Modelo.SocioDAO;
import Modelo.UtilTablasActividad;
import Modelo.UtilTablasSocio;
import Vista.NuevoSocioActividad;
import Vista.VistaActividades;
import Vista.VistaMensaje;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import org.hibernate.Session;

/**
 *
 * @author jujis
 */
class ControladorActividades implements ActionListener {

    private SocioDAO sDAO;
    private ActividadDAO aDAO;

    private String codigo;
    private int filaSeleccionada;

    private Vista.VistaActividades vActividades;

    private UtilTablasSocio uTablasS = new UtilTablasSocio();
    private UtilTablasActividad uTablasA = new UtilTablasActividad();

    private Vista.VistaMensaje vMensaje = new VistaMensaje();

    private NuevoSocioActividad nuevosocioactividad = new NuevoSocioActividad();

    public ControladorActividades(Session sesion, VistaActividades vActividades) {

        this.vActividades = vActividades;

        aDAO = new ActividadDAO(sesion);
        sDAO = new SocioDAO(sesion);

        uTablasA.dibujarTablaActividad(vActividades);
        try {
            pideActividades();
        } catch (Exception ex) {
            vMensaje.mostrarmensaje("Error", "No se han podido obtener las actividades", ex.getMessage());
        }

        addListeners();

    }

    private void pideActividades() throws Exception {

        ArrayList<Actividad> lactividad = aDAO.listaActividades();

        uTablasA.vaciarTablaActividad();
        uTablasA.rellenarTablaActividad(lactividad);

    }

    private void pidenoSociosenActividad(String codigo) {

        ArrayList<Socio> lnosocioactividad = sDAO.listarnoSociosenActividad(codigo);

        uTablasS.vaciarTablaSociosActividad();
        uTablasS.rellenarTablaSocioActividad(lnosocioactividad);

    }

    private void pideSociosenActividad(String codigo) {

        ArrayList<Socio> lsocioactividad = sDAO.listarSociosenActividad(codigo);

        uTablasS.vaciarTablaSociosActividad();
        uTablasS.rellenarTablaSocioActividad(lsocioactividad);

    }

    private void addListeners() {

        vActividades.AltaActividad.addActionListener(this);
        vActividades.BajaActividad.addActionListener(this);

        nuevosocioactividad.AltaSocioActividad.addActionListener(this);
        nuevosocioactividad.CancelarSocioActividad.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()) {

            case "AltaActividad" -> {
                nuevosocioactividad.AltaSocioActividad.setActionCommand("AltaSocioActividad");
                nuevosocioactividad.AltaSocioActividad.setText("Alta de Socio en Actividad");
                nuevosocioactividad.TituloMayor.setText("Alta en Actividad");

                filaSeleccionada = vActividades.TablaActividad.getSelectedRow();
                if (filaSeleccionada == -1) {
                    vMensaje.mostrarmensaje("Advertencia", "Selecciona una actividad", "Por favor, selecciona una actividad de la tabla.");
                } else {
                    uTablasS.dibujarTablaSocioActividad(nuevosocioactividad);
                    nuevosocioactividad.NombreActividadINF.setText(vActividades.TablaActividad.getValueAt(filaSeleccionada, 1).toString());
                    nuevosocioactividad.NumeroActividadINF.setText(vActividades.TablaActividad.getValueAt(filaSeleccionada, 0).toString());
                    codigo = vActividades.TablaActividad.getValueAt(filaSeleccionada, 0).toString();

                    try {
                        //sDAO.listarnoSociosenActividad(codigo);
                        pidenoSociosenActividad(codigo);

                    } catch (Exception ex) {
                        vMensaje.mostrarmensaje("Error", "Error al mostrar socios", ex.getMessage());
                    }

                    nuevosocioactividad.setLocationRelativeTo(null);
                    nuevosocioactividad.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
                    nuevosocioactividad.setResizable(false);
                    nuevosocioactividad.setVisible(true);

                }
            }

            case "AltaSocioActividad" -> {
                filaSeleccionada = nuevosocioactividad.TablaSociosActividad.getSelectedRow();
                if (filaSeleccionada == -1) {
                    vMensaje.mostrarmensaje("Advertencia", "Selecciona un socio", "Por favor, selecciona un socio de la tabla.");
                } else {
                    codigo = nuevosocioactividad.TablaSociosActividad.getValueAt(filaSeleccionada, 0).toString();

                    try {
                        aDAO.darAltaSocioEnActividad(codigo, nuevosocioactividad.NumeroActividadINF.getText());
                        vMensaje.mostrarmensaje("Info", "El socio ha sido Incorporado correctamente", "El socio con codigo: " + codigo + " ha sido incorporado en la actividad con codigo: " + nuevosocioactividad.NumeroActividadINF.getText());

                    } catch (Exception ex) {
                        vMensaje.mostrarmensaje("Error", "Error al mostrar socios", ex.getMessage());
                    }

                    nuevosocioactividad.dispose();

                }
            }

            case "BajaActividad" -> {
                nuevosocioactividad.AltaSocioActividad.setActionCommand("BajaSocioActividad");
                nuevosocioactividad.AltaSocioActividad.setText("Baja de Socio en Actividad");
                nuevosocioactividad.TituloMayor.setText("Baja en Actividad");

                filaSeleccionada = vActividades.TablaActividad.getSelectedRow();
                if (filaSeleccionada == -1) {
                    vMensaje.mostrarmensaje("Advertencia", "Selecciona una actividad", "Por favor, selecciona una actividad de la tabla.");
                } else {
                    uTablasS.dibujarTablaSocioActividad(nuevosocioactividad);
                    nuevosocioactividad.NombreActividadINF.setText(vActividades.TablaActividad.getValueAt(filaSeleccionada, 1).toString());
                    nuevosocioactividad.NumeroActividadINF.setText(vActividades.TablaActividad.getValueAt(filaSeleccionada, 0).toString());
                    codigo = vActividades.TablaActividad.getValueAt(filaSeleccionada, 0).toString();

                    try {
                        //sDAO.listarnoSociosenActividad(codigo);
                        pideSociosenActividad(codigo);

                    } catch (Exception ex) {
                        vMensaje.mostrarmensaje("Error", "Error al mostrar socios", ex.getMessage());
                    }

                    nuevosocioactividad.setLocationRelativeTo(null);
                    nuevosocioactividad.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
                    nuevosocioactividad.setResizable(false);
                    nuevosocioactividad.setVisible(true);

                }
            }

            case "BajaSocioActividad" -> {
                filaSeleccionada = nuevosocioactividad.TablaSociosActividad.getSelectedRow();
                if (filaSeleccionada == -1) {
                    vMensaje.mostrarmensaje("Advertencia", "Selecciona un socio", "Por favor, selecciona un socio de la tabla.");
                } else {
                    codigo = nuevosocioactividad.TablaSociosActividad.getValueAt(filaSeleccionada, 0).toString();

                    try {
                        aDAO.darBajaSocioDeActividad(codigo, nuevosocioactividad.NumeroActividadINF.getText());
                        vMensaje.mostrarmensaje("Info", "El socio ha sido dado de baja correctamente", "El socio con codigo: " + codigo + " ha sido dado de baja en la actividad con codigo: " + nuevosocioactividad.NumeroActividadINF.getText());

                    } catch (Exception ex) {
                        vMensaje.mostrarmensaje("Error", "Error al mostrar socios", ex.getMessage());
                    }

                    nuevosocioactividad.dispose();

                }
            }

            case "cancelaraltabajaActividad" ->
                nuevosocioactividad.dispose();

            default ->
                throw new AssertionError();
        }

    }
}
