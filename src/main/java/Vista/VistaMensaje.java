/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

import javax.swing.JOptionPane;

/**
 *
 * @author jujis
 */
public class VistaMensaje {
    
    public void mostrarmensaje(String Tipo, String titulo, String texto){

        
        if (Tipo.equals("Error")) {
            
            JOptionPane.showMessageDialog(null, texto, titulo, JOptionPane.ERROR_MESSAGE);
            
        }else if (Tipo.equals("Advertencia")) {
            
            JOptionPane.showMessageDialog(null, texto, titulo, JOptionPane.YES_NO_OPTION);
            
        }else{
            JOptionPane.showMessageDialog(null, texto, titulo, JOptionPane.INFORMATION_MESSAGE);
        }
        
    }
    
    public int mostrarmensaje(String titulo, String texto){
        
        int opcion = JOptionPane.showConfirmDialog(null, texto, titulo, JOptionPane.YES_NO_OPTION);

        return opcion;
    }
    
}
