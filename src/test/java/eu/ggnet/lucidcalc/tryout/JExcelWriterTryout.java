/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.ggnet.lucidcalc.tryout;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import eu.ggnet.lucidcalc.jexcel.JExcelWriterTest;

/**
 *
 * @author oliver.guenther
 */
public class JExcelWriterTryout {
    
    public static void main(String[] args) throws IOException {
        File file = JExcelWriterTest.generateDemoTableAsTempFile();
        Desktop.getDesktop().open(file);
    }
    
}
