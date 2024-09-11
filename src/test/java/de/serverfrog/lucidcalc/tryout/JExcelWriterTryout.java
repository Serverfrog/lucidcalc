/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.serverfrog.lucidcalc.tryout;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import de.serverfrog.lucidcalc.LucidCalc;
import de.serverfrog.lucidcalc.jexcel.LucidCalcWriterTest;

/**
 *
 * @author oliver.guenther
 */
public class JExcelWriterTryout {

    public static void main(String[] args) throws IOException {
        File file = LucidCalcWriterTest.generateDemoTableAsTempFile(LucidCalc.Backend.JEXCEL);
        Desktop.getDesktop().open(file);
    }

}
