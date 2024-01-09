/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import Vista.VistaMonitor;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author jujis
 */
public class UtilTablasMonitor {

    DefaultTableModel modeloTablaMonitores;

    public void dibujarTablaMonitor(VistaMonitor VMonitor) {

        modeloTablaMonitores = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        VMonitor.TablaMonitores.setModel(modeloTablaMonitores);

        String[] columnasTabla = {"Codigo", "Nombre", "DNI", "Telefono", "Correo", "Fecha Incorporación", "Nick"};
        modeloTablaMonitores.setColumnIdentifiers(columnasTabla);

        //para no permitir el redimensionamiento de las columnas con el ratón
        VMonitor.TablaMonitores.getTableHeader().setResizingAllowed(false);
        VMonitor.TablaMonitores.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        // Asi se fija el ancho de las columnas
        VMonitor.TablaMonitores.getColumnModel().getColumn(0).setPreferredWidth(40);
        VMonitor.TablaMonitores.getColumnModel().getColumn(1).setPreferredWidth(240);
        VMonitor.TablaMonitores.getColumnModel().getColumn(2).setPreferredWidth(70);
        VMonitor.TablaMonitores.getColumnModel().getColumn(3).setPreferredWidth(70);
        VMonitor.TablaMonitores.getColumnModel().getColumn(4).setPreferredWidth(200);
        VMonitor.TablaMonitores.getColumnModel().getColumn(5).setPreferredWidth(150);
        VMonitor.TablaMonitores.getColumnModel().getColumn(6).setPreferredWidth(60);

    }

    public void rellenarTablaMonitor(ArrayList<Monitor> monitores) {

        Object[] fila = new Object[7];
        for (Monitor monitor : monitores) {
            fila[0] = monitor.getCodMonitor();
            fila[1] = monitor.getNombre();
            fila[2] = monitor.getDni();
            fila[3] = monitor.getTelefono();
            fila[4] = monitor.getCorreo();
            fila[5] = monitor.getFechaEntrada();
            fila[6] = monitor.getNick();
            modeloTablaMonitores.addRow(fila);
        }        

    }

    public void vaciarTablaMonitores() {
        while (modeloTablaMonitores.getRowCount() > 0) {

            modeloTablaMonitores.removeRow(0);

        }
    }

}
