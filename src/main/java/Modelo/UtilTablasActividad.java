/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import Vista.VistaActividades;
import Vista.VistaMonitor;
import Vista.VistaSocio;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author jujis
 */
public class UtilTablasActividad {

    DefaultTableModel modeloTablaActividad;

    public void dibujarTablaActividad(VistaActividades VActividad) {

        modeloTablaActividad = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        VActividad.TablaActividad.setModel(modeloTablaActividad);

        String[] columnasTabla = {"Codigo", "Nombre", "Descripción", "Precio Base Mes", "Codigo Monitor"};
        modeloTablaActividad.setColumnIdentifiers(columnasTabla);

        //para no permitir el redimensionamiento de las columnas con el ratón
        VActividad.TablaActividad.getTableHeader().setResizingAllowed(false);
        VActividad.TablaActividad.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        // Asi se fija el ancho de las columnas
        VActividad.TablaActividad.getColumnModel().getColumn(0).setPreferredWidth(150);
        VActividad.TablaActividad.getColumnModel().getColumn(1).setPreferredWidth(150);
        VActividad.TablaActividad.getColumnModel().getColumn(2).setPreferredWidth(150);
        VActividad.TablaActividad.getColumnModel().getColumn(3).setPreferredWidth(150);
        VActividad.TablaActividad.getColumnModel().getColumn(4).setPreferredWidth(150);

    }

    public void rellenarTablaActividad(ArrayList<Actividad> actividades) {

        Object[] fila = new Object[5];

        for (Actividad actividad : actividades) {

            fila[0] = actividad.getIdActividad();
            System.out.println(actividad.getIdActividad());
            fila[1] = actividad.getNombre();
            System.out.println(actividad.getNombre());
            fila[2] = actividad.getDescripcion();
            System.out.println(actividad.getDescripcion());
            fila[3] = actividad.getPrecioBaseMes();
            System.out.println(actividad.getPrecioBaseMes());
            fila[4] = actividad.getMonitorResponsable();
            System.out.println(actividad.getMonitorResponsable());
            modeloTablaActividad.addRow(fila);

        }

    }

    public void vaciarTablaActividad() {
        while (modeloTablaActividad.getRowCount() > 0) {

            modeloTablaActividad.removeRow(0);

        }
    }

}
