/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import Vista.NuevoSocioActividad;
import Vista.VistaSocio;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author jujis
 */
public class UtilTablasSocio {

    DefaultTableModel modeloTablaSocio;
    DefaultTableModel modeloTablaSocioenActividad;

    public void dibujarTablaSocio(VistaSocio VSocio) {

        modeloTablaSocio = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        VSocio.TablaSocios.setModel(modeloTablaSocio);

        String[] columnasTabla = {"Socio", "Nombre", "DNI", "Fecha de nacimiento", "Telefono", "Correo", "Fecha de Alta","Cat."};
        modeloTablaSocio.setColumnIdentifiers(columnasTabla);

        //para no permitir el redimensionamiento de las columnas con el ratón
        VSocio.TablaSocios.getTableHeader().setResizingAllowed(false);
        VSocio.TablaSocios.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        // Asi se fija el ancho de las columnas
        VSocio.TablaSocios.getColumnModel().getColumn(0).setPreferredWidth(120);
        VSocio.TablaSocios.getColumnModel().getColumn(1).setPreferredWidth(200);
        VSocio.TablaSocios.getColumnModel().getColumn(2).setPreferredWidth(120);
        VSocio.TablaSocios.getColumnModel().getColumn(3).setPreferredWidth(160);
        VSocio.TablaSocios.getColumnModel().getColumn(4).setPreferredWidth(100);
        VSocio.TablaSocios.getColumnModel().getColumn(5).setPreferredWidth(300);
        VSocio.TablaSocios.getColumnModel().getColumn(6).setPreferredWidth(180);
        VSocio.TablaSocios.getColumnModel().getColumn(7).setPreferredWidth(90);

    }

    public void rellenarTablaSocio(ArrayList<Socio> socios) {

        Object[] fila = new Object[8];
        for (Socio socio : socios) {
            fila[0] = socio.getNumeroSocio();
            fila[1] = socio.getNombre();
            fila[2] = socio.getDni();
            fila[3] = socio.getFechaNacimiento();
            fila[4] = socio.getTelefono();
            fila[5] = socio.getCorreo();
            fila[6] = socio.getFechaEntrada();
            fila[7] = socio.getCategoria();
            modeloTablaSocio.addRow(fila);
        }

    }

    public void vaciarTablaSocios() {
        while (modeloTablaSocio.getRowCount() > 0) {

            modeloTablaSocio.removeRow(0);

        }
    }

    
    
    
    public void dibujarTablaSocioActividad(NuevoSocioActividad nuevosocioactividad) {

        modeloTablaSocioenActividad = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        nuevosocioactividad.TablaSociosActividad.setModel(modeloTablaSocioenActividad);

        String[] columnasTabla = {"Codigo de Socio", "Nombre de Socio"};
        modeloTablaSocioenActividad.setColumnIdentifiers(columnasTabla);

        //para no permitir el redimensionamiento de las columnas con el ratón
        nuevosocioactividad.TablaSociosActividad.getTableHeader().setResizingAllowed(false);
        nuevosocioactividad.TablaSociosActividad.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        // Asi se fija el ancho de las columnas
        nuevosocioactividad.TablaSociosActividad.getColumnModel().getColumn(0).setPreferredWidth(300);
        nuevosocioactividad.TablaSociosActividad.getColumnModel().getColumn(1).setPreferredWidth(300);

    }

    public void rellenarTablaSocioActividad(ArrayList<Socio> lnosocioactividad) {
     Object[] fila = new Object[2];
        for (Socio socio : lnosocioactividad) {
            fila[0] = socio.getNumeroSocio();
            fila[1] = socio.getNombre();
            modeloTablaSocioenActividad.addRow(fila);
        }   
        
    }

    public void vaciarTablaSociosActividad() {
        while (modeloTablaSocioenActividad.getRowCount() > 0) {

            modeloTablaSocioenActividad.removeRow(0);

        }
    }

}
