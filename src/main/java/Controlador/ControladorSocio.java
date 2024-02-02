/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Socio;
import Modelo.SocioDAO;
import Modelo.UtilTablasSocio;
import Vista.ActualizarSocio;
import Vista.NuevoSocio;
import Vista.VistaMensaje;
import Vista.VistaSocio;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.hibernate.Session;

/**
 *
 * @author jujis
 */
class ControladorSocio implements ActionListener {

    private SocioDAO sDAO;

    private String codigo;
    private int filaSeleccionada;
    String fechaString;
    Date fechaDate;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private VistaSocio vSocio;
    private ActualizarSocio vActSocio = new ActualizarSocio();

    private UtilTablasSocio uTablasS = new UtilTablasSocio();

    private Vista.VistaMensaje vMensaje = new VistaMensaje();

    private NuevoSocio nuevosocio = new NuevoSocio();
    private Socio socio = new Socio();

    public ControladorSocio(Session sesion, VistaSocio vSocio) {

        this.vSocio = vSocio;

        sDAO = new SocioDAO(sesion);

        uTablasS.dibujarTablaSocio(vSocio);

        try {
            pideSocios();
        } catch (Exception ex) {
            vMensaje.mostrarmensaje("Error", "No se han podido obtener los socios", ex.getMessage());
        }

        addListeners();

    }

    private void pideSocios() throws Exception {

        ArrayList<Socio> lsocios = sDAO.listaSociosHQL();
        uTablasS.vaciarTablaSocios();
        uTablasS.rellenarTablaSocio(lsocios);

    }

    private void pideSocios(ArrayList<Socio> socio) throws Exception {

        ArrayList<Socio> lsocios = socio;
        uTablasS.vaciarTablaSocios();
        uTablasS.rellenarTablaSocio(lsocios);

    }

    private void addListeners() {

        vSocio.AltaSocio.addActionListener(this);
        vSocio.BajaSocio.addActionListener(this);
        vSocio.UpdateSocio.addActionListener(this);
        vSocio.BuscarSocio.addActionListener(this);

        nuevosocio.AceptarNuevoSocio.addActionListener(this);
        nuevosocio.CancelarNuevoSocio.addActionListener(this);

        vActSocio.AceptarActualizarSocio.addActionListener(this);
        vActSocio.CancelarActualizarSocio.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()) {

            case "AltaSocio" -> {
                codigo = sDAO.SiguienteCodigo();
                nuevosocio.codigo.setText(codigo);

                nuevosocio.setLocationRelativeTo(null);
                nuevosocio.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
                nuevosocio.setResizable(false);
                nuevosocio.setVisible(true);
            }

            case "AceptarNuevoSocio" -> {
                socio.setNumeroSocio(nuevosocio.codigo.getText());
                socio.setNombre(nuevosocio.nombre.getText());
                socio.setDni(nuevosocio.DNI.getText());
                socio.setTelefono(nuevosocio.Telefono.getText());
                socio.setCorreo(nuevosocio.Correo.getText());
                if (!nuevosocio.categoria.getText().isEmpty()) {
                    socio.setCategoria(Character.toUpperCase(nuevosocio.categoria.getText().charAt(0)));
                }
                if (nuevosocio.FechaAlta.getDate() != null) {
                    fechaDate = nuevosocio.FechaAlta.getDate();
                    fechaString = dateFormat.format(fechaDate);
                } else {
                    fechaString = "";
                }
                socio.setFechaEntrada(fechaString);
                if (nuevosocio.FechaNacimiento.getDate() != null) {
                    fechaDate = nuevosocio.FechaNacimiento.getDate();
                    fechaString = dateFormat.format(fechaDate);
                } else {
                    fechaString = "";
                }
                socio.setFechaNacimiento(fechaString);
                if (!socio.getNumeroSocio().isEmpty() && !socio.getNombre().isEmpty() && !socio.getDni().isEmpty() && !socio.getTelefono().isEmpty() && !socio.getCorreo().isEmpty() && !socio.getFechaEntrada().isEmpty() && !socio.getFechaNacimiento().isEmpty() && !nuevosocio.categoria.getText().isEmpty()) {
                    if (validarDNI(socio.getDni())) {
                        if (validarCorreo(socio.getCorreo())) {
                            if (validarTelefono(socio.getTelefono())) {
                                if (validarFechaEntrada(socio.getFechaEntrada())) {
                                    if (validarEdadSocio(socio.getFechaNacimiento())) {

                                        try{
                                        sDAO.AltaSocio(socio);
                                        vMensaje.mostrarmensaje("", "El socio se ha insertado con exito", "Socio " + socio.getNombre() + " insertado en la BD");
                                        } catch (Exception ex){
                                        vMensaje.mostrarmensaje("Error", "Error al insertar socio", ex.getMessage());
                                        }
                                        nuevosocio.dispose();
                                    } else {
                                        vMensaje.mostrarmensaje("Error", "Error al insertar socio", "El socio debe ser mayor de 18 años");
                                    }
                                } else {
                                    vMensaje.mostrarmensaje("Error", "Error al insertar socio", "La fecha de entrada debe ser anterior a la fecha actual");
                                }
                            } else {
                                vMensaje.mostrarmensaje("Error", "Error al insertar socio", "El campo \"teléfono\" solo admite cadenas de 9 dígitos");
                            }
                        } else {
                            vMensaje.mostrarmensaje("Error", "Error al insertar socio", "El campo \"correo\" solo admite patrones válidos (al menos xxx@xxx)");
                        }
                    } else {
                        vMensaje.mostrarmensaje("Error", "Error al insertar socio", "El campo \"DNI\" solo admite cadenas de 8 dígitos y una letra mayúscula");
                    }

                } else {
                    vMensaje.mostrarmensaje("Error", "Error al insertar socio", "Debe rellenar todos los campos para poder introducir al socio");
                }
                {
                    try {
                        pideSocios();
                    } catch (Exception ex) {
                        Logger.getLogger(ControladorPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                        vMensaje.mostrarmensaje("Error", "No se puedo Actualizar la vista", ex.getMessage());
                    }
                }
                nuevosocio.codigo.setText("");
                nuevosocio.nombre.setText("");
                nuevosocio.DNI.setText("");
                nuevosocio.Telefono.setText("");
                nuevosocio.Correo.setText("");
                nuevosocio.categoria.setText("");
                fechaDate = new Date();
                nuevosocio.FechaAlta.setDate(fechaDate);
                nuevosocio.FechaNacimiento.setDate(fechaDate);
                nuevosocio.dispose();
            }

            case "CancelarNuevoSocio" -> {
                nuevosocio.codigo.setText("");
                nuevosocio.nombre.setText("");
                nuevosocio.DNI.setText("");
                nuevosocio.Telefono.setText("");
                nuevosocio.Correo.setText("");
                nuevosocio.categoria.setText("");
                fechaDate = new Date();
                nuevosocio.FechaAlta.setDate(fechaDate);
                nuevosocio.FechaNacimiento.setDate(fechaDate);
                nuevosocio.dispose();
            }

            case "BajaSocio" -> {
                filaSeleccionada = vSocio.TablaSocios.getSelectedRow();
                if (filaSeleccionada == -1) {
                    vMensaje.mostrarmensaje("Advertencia", "Selecciona un socio", "Por favor, selecciona un socio de la tabla.");
                } else {
                    codigo = vSocio.TablaSocios.getValueAt(filaSeleccionada, 0).toString();

                    // Confirmar la baja del monitor con el usuario
                    int opcion = vMensaje.mostrarmensaje("¿Estás seguro?", "¿Estás seguro de dar de baja a este socio? \n Selecciona una opción");

                    if (opcion == 0) {
                        // Realizar la baja del monitor en la base de datos
                        try {
                            sDAO.bajaSocio(codigo);
                            vMensaje.mostrarmensaje("Información", "socio dado de baja", "El socio se ha dado de baja exitosamente.");

                            // Actualizar la tabla de monitores después de la baja
                            pideSocios();
                        } catch (SQLException ex) {
                            vMensaje.mostrarmensaje("Error", "Error al dar de baja al socio", ex.getMessage());
                        } catch (Exception ex) {
                            vMensaje.mostrarmensaje("Error", "Error al actualizar a los socios", ex.getMessage());
                        }
                    }
                }
            }

            case "UpdateSocio" -> {
                filaSeleccionada = vSocio.TablaSocios.getSelectedRow();
                if (filaSeleccionada == -1) {
                    vMensaje.mostrarmensaje("Advertencia", "Selecciona un socio", "Por favor, selecciona un socio de la tabla.");
                } else {
                    codigo = vSocio.TablaSocios.getValueAt(filaSeleccionada, 0).toString();

                    vActSocio.codigo.setText(codigo);
                    vActSocio.nombre.setText(vSocio.TablaSocios.getValueAt(filaSeleccionada, 1).toString());
                    vActSocio.DNI.setText(vSocio.TablaSocios.getValueAt(filaSeleccionada, 2).toString());
                    vActSocio.Telefono.setText(vSocio.TablaSocios.getValueAt(filaSeleccionada, 4).toString());
                    vActSocio.Correo.setText(vSocio.TablaSocios.getValueAt(filaSeleccionada, 5).toString());
                    vActSocio.categoria.setText(vSocio.TablaSocios.getValueAt(filaSeleccionada, 7).toString());

                    fechaString = vSocio.TablaSocios.getValueAt(filaSeleccionada, 3).toString();
                    try {
                        fechaDate = dateFormat.parse(fechaString);
                        vActSocio.FechaNacimiento.setDate(fechaDate);
                    } catch (ParseException ex) {
                        vMensaje.mostrarmensaje("Error", "Error al transformar la fecha de nacimiento", ex.getMessage());
                    }

                    fechaString = vSocio.TablaSocios.getValueAt(filaSeleccionada, 6).toString();
                    try {
                        fechaDate = dateFormat.parse(fechaString);
                        vActSocio.FechaAlta.setDate(fechaDate);
                    } catch (ParseException ex) {
                        vMensaje.mostrarmensaje("Error", "Error al transformar la fecha de alta", ex.getMessage());
                    }

                    vActSocio.setLocationRelativeTo(null);
                    vActSocio.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
                    vActSocio.setResizable(false);
                    vActSocio.setVisible(true);
                }
            }

            case "AceptarActualizarSocio" -> {
                socio.setNumeroSocio(vActSocio.codigo.getText());
                socio.setNombre(vActSocio.nombre.getText());
                socio.setDni(vActSocio.DNI.getText());
                socio.setTelefono(vActSocio.Telefono.getText());
                socio.setCorreo(vActSocio.Correo.getText());
                if (!vActSocio.categoria.getText().isEmpty()) {
                    socio.setCategoria(Character.toUpperCase(vActSocio.categoria.getText().charAt(0)));
                }
                if (vActSocio.FechaAlta.getDate() != null) {
                    fechaDate = vActSocio.FechaAlta.getDate();
                    fechaString = dateFormat.format(fechaDate);
                } else {
                    fechaString = "";
                }
                socio.setFechaEntrada(fechaString);
                if (vActSocio.FechaNacimiento.getDate() != null) {
                    fechaDate = vActSocio.FechaNacimiento.getDate();
                    fechaString = dateFormat.format(fechaDate);
                } else {
                    fechaString = "";
                }
                socio.setFechaNacimiento(fechaString);
                if (!socio.getNumeroSocio().isEmpty() && !socio.getNombre().isEmpty() && !socio.getDni().isEmpty() && !socio.getTelefono().isEmpty() && !socio.getCorreo().isEmpty() && !socio.getFechaEntrada().isEmpty() && !socio.getFechaNacimiento().isEmpty() && !vActSocio.categoria.getText().isEmpty()) {
                    if (validarDNI(socio.getDni())) {
                        if (validarCorreo(socio.getCorreo())) {
                            if (validarTelefono(socio.getTelefono())) {
                                if (validarFechaEntrada(socio.getFechaEntrada())) {
                                    if (validarEdadSocio(socio.getFechaNacimiento())) {

                                        try {
                                            sDAO.UpdateSocio(socio);
                                            vMensaje.mostrarmensaje("", "El socio se ha actualizado con exito", "Socio " + socio.getNombre() + " actualizado en la BD");
                                        } catch (SQLException ex) {
                                            vMensaje.mostrarmensaje("Error", "El socio no se ha actualizado con exito", "Socio " + socio.getNombre() + " actualizado en la BD: " + ex.getMessage());
                                        }

                                        vActSocio.dispose();
                                    } else {
                                        vMensaje.mostrarmensaje("Error", "Error al actualizado socio", "El socio debe ser mayor de 18 aÃ±os");
                                    }
                                } else {
                                    vMensaje.mostrarmensaje("Error", "Error al actualizado socio", "La fecha de entrada debe ser anterior a la fecha actual");
                                }
                            } else {
                                vMensaje.mostrarmensaje("Error", "Error al actualizado socio", "El campo \"telefono\" solo admite cadenas de 9 di­gitos");
                            }
                        } else {
                            vMensaje.mostrarmensaje("Error", "Error al actualizado socio", "El campo \"correo\" solo admite patrones validos (al menos xxx@xxx)");
                        }
                    } else {
                        vMensaje.mostrarmensaje("Error", "Error al actualizado socio", "El campo \"DNI\" solo admite cadenas de 8 di­gitos y una letra mayuscula");
                    }

                } else {
                    vMensaje.mostrarmensaje("Error", "Error al actualizado socio", "Debe rellenar todos los campos para poder introducir al socio");
                }
                {
                    try {
                        pideSocios();
                    } catch (Exception ex) {
                        Logger.getLogger(ControladorPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                        vMensaje.mostrarmensaje("Error", "No se puedo Actualizar la vista", ex.getMessage());
                    }
                }
            }

            case "CancelarActualizarSocio" ->
                vActSocio.dispose();

            case "BuscarSocio" -> {
                if (vSocio.NombreSocioBusqueda.getText().isEmpty()) {
                    vMensaje.mostrarmensaje("Error", "Error al mostrar socios", "Debes indicar el nombre del socio");
                } else {
                    ArrayList<Socio> Asocio = sDAO.SocioporNombre(vSocio.NombreSocioBusqueda.getText());
                    try {
                        pideSocios(Asocio);
                    } catch (Exception ex) {
                        vMensaje.mostrarmensaje("Error", "Error al mostrar socios", "Se han producido errores al mostrar los socios");
                    }
                }
            }

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

    private boolean validarEdadSocio(String fechaNacimiento) {

        try {
            Date fechaNacimientoDate = dateFormat.parse(fechaNacimiento);
            Calendar calNacimiento = Calendar.getInstance();
            calNacimiento.setTime(fechaNacimientoDate);

            Calendar calMayorEdad = Calendar.getInstance();
            calMayorEdad.add(Calendar.YEAR, -18);

            return calNacimiento.before(calMayorEdad);
        } catch (ParseException e) {
            return false; // Si hay un error al parsear la fecha, consideramos que no es válida
        }

    }

}
