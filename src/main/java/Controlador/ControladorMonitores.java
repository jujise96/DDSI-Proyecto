/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Monitor;
import Modelo.MonitorDAO;
import Modelo.UtilTablasMonitor;
import Vista.ActualizarMonitor;
import Vista.NuevoMonitor;
import Vista.VistaMensaje;
import Vista.VistaMonitor;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.hibernate.Session;

/**
 *
 * @author jujis
 */
class ControladorMonitores implements ActionListener {

    private MonitorDAO mDAO;

    private String codigo;
    private String codigoMonitor;
    private int filaSeleccionada;
    String fechaString;
    Date fechaDate;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private UtilTablasMonitor uTablasM = new UtilTablasMonitor();

    private Vista.VistaMensaje vMensaje = new VistaMensaje();

    private NuevoMonitor nuevomonitor = new NuevoMonitor();
    private Monitor monitor = new Monitor();
    private VistaMonitor vMonitor;
    private ActualizarMonitor vActMonitor = new ActualizarMonitor();

    public ControladorMonitores(Session sesion, VistaMonitor vMonitor) {

        this.vMonitor = vMonitor;

        mDAO = new MonitorDAO(sesion);

        uTablasM.dibujarTablaMonitor(vMonitor);

        try {
            pideMonitores();

        } catch (Exception ex) {
            vMensaje.mostrarmensaje("Error", "No se han podido obtener los monitores", ex.getMessage());
        }

        addListeners();

    }

    private void pideMonitores() throws Exception {

        ArrayList<Monitor> lmonitores = mDAO.listaMonitores();
        uTablasM.vaciarTablaMonitores();
        uTablasM.rellenarTablaMonitor(lmonitores);

    }

    private void addListeners() {

        vMonitor.NuevoMonitor.addActionListener(this);
        vMonitor.BajaMonitor1.addActionListener(this);
        vMonitor.UpdateMonitor.addActionListener(this);

        nuevomonitor.AceptarNuevoMonitor.addActionListener(this);
        nuevomonitor.CancelarNuevoMonitor.addActionListener(this);

        vActMonitor.AceptarActualizaMonitor.addActionListener(this);
        vActMonitor.CancelarActualizaMonitor.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()) {

            case "NuevoMonitor" -> {
                codigo = mDAO.SiguienteCodigo();
                nuevomonitor.codigo.setText(codigo);

                nuevomonitor.setLocationRelativeTo(null);
                nuevomonitor.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
                nuevomonitor.setResizable(false);
                nuevomonitor.setVisible(true);
            }

            case "AceptarNuevoMonitor" -> {
                monitor.setCodMonitor(nuevomonitor.codigo.getText());
                monitor.setNombre(nuevomonitor.nombre.getText());
                monitor.setDni(nuevomonitor.DNI.getText());
                monitor.setTelefono(nuevomonitor.Telefono.getText());
                monitor.setCorreo(nuevomonitor.Correo.getText());
                if (nuevomonitor.Fechaentrada.getDate() != null) {
                    fechaDate = nuevomonitor.Fechaentrada.getDate();
                    fechaString = dateFormat.format(fechaDate);
                } else {
                    fechaString = "";
                }
                monitor.setFechaEntrada(fechaString);
                monitor.setNick(nuevomonitor.Nick.getText());
                if (!monitor.getCodMonitor().isEmpty() && !monitor.getNombre().isEmpty() && !monitor.getDni().isEmpty() && !monitor.getTelefono().isEmpty() && !monitor.getCorreo().isEmpty() && !monitor.getFechaEntrada().isEmpty() && !monitor.getNick().isEmpty()) {
                    if (validarDNI(monitor.getDni())) {
                        if (validarCorreo(monitor.getCorreo())) {
                            if (validarTelefono(monitor.getTelefono())) {
                                if (validarFechaEntrada(monitor.getFechaEntrada())) {

                                    try {
                                        mDAO.AltaMonitor(monitor);
                                        vMensaje.mostrarmensaje("", "El monitor se ha insertado con exito", "Socio " + monitor.getNombre() + " insertado en la BD");
                                    } catch (SQLException ex) {
                                        vMensaje.mostrarmensaje("Error", "Error al insertar Monitor", ex.getMessage());
                                        nuevomonitor.dispose();
                                    }

                                    nuevomonitor.dispose();
                                } else {
                                    vMensaje.mostrarmensaje("Error", "Error al insertar Monitor", "La fecha de entrada debe ser anterior a la fecha actual");
                                }
                            } else {
                                vMensaje.mostrarmensaje("Error", "Error al insertar Monitor", "El campo \"teléfono\" sólo admite cadenas de 9 dígitos");
                            }
                        } else {
                            vMensaje.mostrarmensaje("Error", "Error al insertar Monitor", "El campo \"correo\" sólo admite patrones válidos (al menos xxx@xxx)");

                        }
                    } else {
                        vMensaje.mostrarmensaje("Error", "Error al insertar Monitor", "El campo \"DNI\" sólo admite cadenas de 8 dígitos y una letra mayúscula");

                    }

                } else {
                    vMensaje.mostrarmensaje("Error", "Error al insertar Monitor", "Debe rellenar todos los campos para poder introducir al socio");
                }
                {
                    try {
                        pideMonitores();
                    } catch (Exception ex) {
                        Logger.getLogger(ControladorPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                        vMensaje.mostrarmensaje("Error", "No se puedo Actualizar la vista", ex.getMessage());
                    }
                }
                nuevomonitor.codigo.setText("");
                nuevomonitor.nombre.setText("");
                nuevomonitor.DNI.setText("");
                nuevomonitor.Telefono.setText("");
                nuevomonitor.Correo.setText("");
                nuevomonitor.Nick.setText("");
                fechaDate = new Date();
                nuevomonitor.Fechaentrada.setDate(fechaDate);
                nuevomonitor.dispose();
            }

            case "CancelarNuevoMonitor" -> {
                nuevomonitor.codigo.setText("");
                nuevomonitor.nombre.setText("");
                nuevomonitor.DNI.setText("");
                nuevomonitor.Telefono.setText("");
                nuevomonitor.Correo.setText("");
                nuevomonitor.Nick.setText("");
                fechaDate = new Date();
                nuevomonitor.Fechaentrada.setDate(fechaDate);
                nuevomonitor.dispose();
            }

            case "BajaMonitor" -> {
                filaSeleccionada = vMonitor.TablaMonitores.getSelectedRow();
                if (filaSeleccionada == -1) {
                    vMensaje.mostrarmensaje("Advertencia", "Selecciona un monitor", "Por favor, selecciona un monitor de la tabla.");
                } else {
                    codigoMonitor = vMonitor.TablaMonitores.getValueAt(filaSeleccionada, 0).toString();

                    // Confirmar la baja del monitor con el usuario
                    int opcion = vMensaje.mostrarmensaje("¿Estás seguro?", "¿Estás seguro de dar de baja a este monitor? \n Selecciona una opción");

                    if (opcion == 0) {
                        // Realizar la baja del monitor en la base de datos
                        try {
                            mDAO.BajaMonitor(codigoMonitor);
                            vMensaje.mostrarmensaje("Información", "Monitor dado de baja", "El monitor se ha dado de baja exitosamente.");

                            // Actualizar la tabla de monitores después de la baja
                            pideMonitores();
                        } catch (SQLException ex) {
                            vMensaje.mostrarmensaje("Error", "Error al dar de baja al monitor", ex.getMessage());
                        } catch (Exception ex) {
                            vMensaje.mostrarmensaje("Error", "Error al actualizar a los monitor", ex.getMessage());
                        }
                    }
                }
            }

            case "UpdateMonitor" -> {
                filaSeleccionada = vMonitor.TablaMonitores.getSelectedRow();
                if (filaSeleccionada == -1) {
                    vMensaje.mostrarmensaje("Advertencia", "Selecciona un monitor", "Por favor, selecciona un monitor de la tabla.");
                } else {
                    codigoMonitor = vMonitor.TablaMonitores.getValueAt(filaSeleccionada, 0).toString();

                    vActMonitor.codigo.setText(codigoMonitor);
                    vActMonitor.nombre.setText(vMonitor.TablaMonitores.getValueAt(filaSeleccionada, 1).toString());
                    vActMonitor.DNI.setText(vMonitor.TablaMonitores.getValueAt(filaSeleccionada, 2).toString());
                    vActMonitor.Telefono.setText(vMonitor.TablaMonitores.getValueAt(filaSeleccionada, 3).toString());
                    vActMonitor.Correo.setText(vMonitor.TablaMonitores.getValueAt(filaSeleccionada, 4).toString());
                    vActMonitor.Nick.setText(vMonitor.TablaMonitores.getValueAt(filaSeleccionada, 6).toString());

                    fechaString = vMonitor.TablaMonitores.getValueAt(filaSeleccionada, 5).toString();
                    try {
                        fechaDate = dateFormat.parse(fechaString);
                        vActMonitor.Fechaentrada.setDate(fechaDate);
                    } catch (ParseException ex) {
                        vMensaje.mostrarmensaje("Error", "Error al transformar la fecha", ex.getMessage());
                    }

                    vActMonitor.setLocationRelativeTo(null);
                    vActMonitor.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
                    vActMonitor.setResizable(false);
                    vActMonitor.setVisible(true);
                }
            }

            case "AceptarActualizaMonitor" -> {
                monitor.setCodMonitor(vActMonitor.codigo.getText());
                monitor.setNombre(vActMonitor.nombre.getText());
                monitor.setDni(vActMonitor.DNI.getText());
                monitor.setTelefono(vActMonitor.Telefono.getText());
                monitor.setCorreo(vActMonitor.Correo.getText());
                if (vActMonitor.Fechaentrada.getDate() != null) {
                    fechaDate = vActMonitor.Fechaentrada.getDate();
                    fechaString = dateFormat.format(fechaDate);
                } else {
                    fechaString = "";
                }
                monitor.setFechaEntrada(fechaString);
                monitor.setNick(vActMonitor.Nick.getText());
                if (!monitor.getCodMonitor().isEmpty() && !monitor.getNombre().isEmpty() && !monitor.getDni().isEmpty() && !monitor.getTelefono().isEmpty() && !monitor.getCorreo().isEmpty() && !monitor.getFechaEntrada().isEmpty() && !monitor.getNick().isEmpty()) {
                    if (validarDNI(monitor.getDni())) {
                        if (validarCorreo(monitor.getCorreo())) {
                            if (validarTelefono(monitor.getTelefono())) {
                                if (validarFechaEntrada(monitor.getFechaEntrada())) {

                                    try {
                                        mDAO.UpdateMonitor(monitor);
                                        vMensaje.mostrarmensaje("", "El monitor se ha actualizado con exito", "monitor " + monitor.getNombre() + " actualizado en la BD");

                                    } catch (SQLException ex) {
                                        vMensaje.mostrarmensaje("Error", "Error al actualizar Monitor " + monitor.getNombre(), ex.getMessage());
                                    }

                                    vActMonitor.dispose();
                                } else {
                                    vMensaje.mostrarmensaje("Error", "Error al insertar Monitor", "La fecha de entrada debe ser anterior a la fecha actual");
                                }
                            } else {
                                vMensaje.mostrarmensaje("Error", "Error al insertar Monitor", "El campo \"telefono\" sÃ³lo admite cadenas de 9 di­gitos");
                            }
                        } else {
                            vMensaje.mostrarmensaje("Error", "Error al insertar Monitor", "El campo \"correo\" sÃ³lo admite patrones validos (al menos xxx@xxx)");

                        }
                    } else {
                        vMensaje.mostrarmensaje("Error", "Error al insertar Monitor", "El campo \"DNI\" sÃ³lo admite cadenas de 8 di­gitos y una letra mayuscula");

                    }

                } else {
                    vMensaje.mostrarmensaje("Error", "Error al insertar Monitor", "Debe rellenar todos los campos para poder introducir al socio");
                }
                {
                    try {
                        pideMonitores();
                    } catch (Exception ex) {
                        Logger.getLogger(ControladorPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                        vMensaje.mostrarmensaje("Error", "No se puedo Actualizar la vista", ex.getMessage());
                    }
                }
            }

            case "CancelarActualizaMonitor" ->
                vActMonitor.dispose();

            default ->
                throw new AssertionError();
        }

    }

    private boolean validarDNI(String dni) {
        String regex = "\\d{8}[A-Z]";
        return Pattern.matches(regex, dni);

    }

    private boolean validarCorreo(String correo) {
        String regex = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b";
        return Pattern.matches(regex, correo);
    }

    private boolean validarTelefono(String telefono) {
        String regex = "\\d{9}";
        return Pattern.matches(regex, telefono);
    }

    private boolean validarFechaEntrada(String fechaEntrada) {
        try {
            Date fechaEntradaDate = dateFormat.parse(fechaEntrada);
            Date fechaActual = new Date();

            return fechaEntradaDate.before(fechaActual);
        } catch (ParseException e) {
            return false;
        }
    }

}
