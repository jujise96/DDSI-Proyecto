/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vista;

import java.awt.Color;



/**
 *
 * @author jujis
 */
public class VentanaPrincipal extends javax.swing.JFrame {

    /**
     * Creates new form VentanaPrincipal
     */
    public VentanaPrincipal() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        MenuPrincipal = new javax.swing.JMenuBar();
        MenuMonitores = new javax.swing.JMenu();
        MenuGestionMonitores = new javax.swing.JMenuItem();
        MenuCuotaMonitor = new javax.swing.JMenuItem();
        MenuSocios = new javax.swing.JMenu();
        MenuGestionSocios = new javax.swing.JMenuItem();
        MenuActividades = new javax.swing.JMenu();
        MenuGestionActividades = new javax.swing.JMenuItem();
        MainMenu = new javax.swing.JMenu();
        VolverMenuPrincipal = new javax.swing.JMenuItem();
        Salir = new javax.swing.JMenu();
        SalirApp = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("DDSI Juan Jimenez Serrano");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMinimumSize(new java.awt.Dimension(900, 550));

        MenuMonitores.setText("Monitores");
        MenuMonitores.setActionCommand("MenuMonitores");

        MenuGestionMonitores.setText("Gestión de Monitores");
        MenuGestionMonitores.setActionCommand("GestionMonitores");
        MenuMonitores.add(MenuGestionMonitores);

        MenuCuotaMonitor.setText("Cuota de Monitor");
        MenuCuotaMonitor.setActionCommand("CuotaMonitor");
        MenuMonitores.add(MenuCuotaMonitor);

        MenuPrincipal.add(MenuMonitores);

        MenuSocios.setText("Socios");
        MenuSocios.setActionCommand("MenuSocios");

        MenuGestionSocios.setText("Gestión de Socios");
        MenuGestionSocios.setActionCommand("GestionSocios");
        MenuGestionSocios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuGestionSociosActionPerformed(evt);
            }
        });
        MenuSocios.add(MenuGestionSocios);

        MenuPrincipal.add(MenuSocios);

        MenuActividades.setText("Actividades");

        MenuGestionActividades.setText("Gestión de Actividades");
        MenuGestionActividades.setActionCommand("GestionActividades");
        MenuActividades.add(MenuGestionActividades);

        MenuPrincipal.add(MenuActividades);

        MainMenu.setText("Menu principal");

        VolverMenuPrincipal.setText("Volver al Menu Principal");
        VolverMenuPrincipal.setActionCommand("VolverMenuPrincipal");
        MainMenu.add(VolverMenuPrincipal);

        MenuPrincipal.add(MainMenu);

        Salir.setText("Salir");
        Salir.setActionCommand("SalirDesplegable");

        SalirApp.setText("Salir de la aplicación");
        SalirApp.setActionCommand("Salir");
        SalirApp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SalirAppActionPerformed(evt);
            }
        });
        Salir.add(SalirApp);

        MenuPrincipal.add(Salir);

        setJMenuBar(MenuPrincipal);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1112, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 462, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void SalirAppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SalirAppActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SalirAppActionPerformed

    private void MenuGestionSociosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuGestionSociosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_MenuGestionSociosActionPerformed

    /**
     * @param args the command line arguments
     */
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu MainMenu;
    private javax.swing.JMenu MenuActividades;
    public javax.swing.JMenuItem MenuCuotaMonitor;
    public javax.swing.JMenuItem MenuGestionActividades;
    public javax.swing.JMenuItem MenuGestionMonitores;
    public javax.swing.JMenuItem MenuGestionSocios;
    private javax.swing.JMenu MenuMonitores;
    public javax.swing.JMenuBar MenuPrincipal;
    private javax.swing.JMenu MenuSocios;
    private javax.swing.JMenu Salir;
    public javax.swing.JMenuItem SalirApp;
    public javax.swing.JMenuItem VolverMenuPrincipal;
    // End of variables declaration//GEN-END:variables
}
