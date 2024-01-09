/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 *
 * @author jujis
 */
public class VistaInfoBD {
    
    public void infoMetadatos (DatabaseMetaData dbmd) throws SQLException{
        System.out.println("Metadatos de la Base de Datos");
        System.out.println(dbmd.getDatabaseProductName());
        System.out.println(dbmd.getDatabaseMajorVersion());
        System.out.println(dbmd.getURL());
        System.out.println(dbmd.getDriverName());
        System.out.println(dbmd.getDriverVersion());
        System.out.println(dbmd.getUserName());
        System.out.println(dbmd.getCatalogTerm());
    }
    
}
