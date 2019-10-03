/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.ggnet.lucidcalc.jexcel;

import java.awt.Color;
import java.io.File;
import java.util.*;

import org.junit.Test;

import eu.ggnet.lucidcalc.*;

import static eu.ggnet.lucidcalc.CFormat.FontStyle.BOLD;
import static eu.ggnet.lucidcalc.CFormat.HorizontalAlignment.LEFT;
import static eu.ggnet.lucidcalc.CFormat.HorizontalAlignment.RIGHT;
import static eu.ggnet.lucidcalc.CFormat.Representation.CURRENCY_EURO;
import static eu.ggnet.lucidcalc.CFormat.Representation.PERCENT_FLOAT;
import static eu.ggnet.lucidcalc.SUtil.SR;
import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author oliver.guenther
 */
public class JExcelWriterTest {

    private final static Random R = new Random();

    public final static CFormat EURO = new CFormat(RIGHT, CURRENCY_EURO);

    public static class SResult {

        public SBlock block;

        public SCell sum1;

        public SCell sum2;

        public SCell sum3;

        public SCell sum4;
    }

    @Test
    public void verifyCreationOfDemoTable() {
        File tempFile = generateDemoTableAsTempFile();
        assertThat(tempFile).exists(); // TODO: A read of the excel file an verification of the contents would be nice.
        tempFile.deleteOnExit();
    }

    public static File generateDemoTableAsTempFile() {
        STable one = new STable();
        one.setTableFormat(new CFormat("Verdana", 10, new CBorder(Color.BLACK, CBorder.LineStyle.THIN)));
        one.setHeadlineFormat(new CFormat(CFormat.FontStyle.BOLD, Color.BLACK, Color.YELLOW, CFormat.HorizontalAlignment.CENTER, CFormat.VerticalAlignment.MIDDLE));
        one.add(new STableColumn("A String", 20));
        one.add(new STableColumn("A Date", 10, new CFormat(CFormat.Representation.SHORT_DATE)));
        one.add(new STableColumn("An Integer", 15));
        one.add(new STableColumn("Double I", 15, EURO));
        one.add(new STableColumn("Double II", 15, EURO));
        one.add(new STableColumn("Double%", 12, new CFormat(CFormat.HorizontalAlignment.RIGHT, CFormat.Representation.PERCENT_FLOAT)).setAction(SUtil.getSelfRow()));
        one.setModel(new STableModelList<>(model(20)));
        STable two = new STable(one);
        two.setModel(new STableModelList<>(model(5)));

        SResult oneSummary = createSummary(one);
        SResult twoSummary = createSummary(two);

        SBlock summary = new SBlock();

        summary.setFormat(new CFormat(BOLD, Color.BLACK, Color.YELLOW, RIGHT, new CBorder(Color.BLACK)));
        SCell sum3 = new SCell(new SFormula(oneSummary.sum1, "+", twoSummary.sum1), EURO);
        SCell sum4 = new SCell(new SFormula(oneSummary.sum2, "+", twoSummary.sum2), EURO);
        summary.add("Summe", new CFormat(Color.BLUE, Color.WHITE, LEFT),
                sum3,
                sum4,
                new SFormula(oneSummary.sum3, "+", twoSummary.sum3), EURO);

        CSheet sheet = new CSheet("DemoSheet");
        sheet.setShowGridLines(false);
        sheet.addBelow(new SBlock("DemoTable I", new CFormat(BOLD), false));
        sheet.addBelow(one);
        sheet.addBelow(1, 1, oneSummary.block);
        sheet.addBelow(new SBlock("DemoTable II", new CFormat(BOLD), true));
        sheet.addBelow(two);
        sheet.addBelow(1, 1, twoSummary.block);
        sheet.addBelow(1, 1, summary);

        CCalcDocument doc = new TempCalcDocument("DemoFile_");
        doc.add(sheet);

        return new JExcelLucidCalcWriter().write(doc);
    }

    /**
     * Returns a model with String, date, int, double, double.
     *
     * @param amount the amount of elements
     * @return the model
     */
    public static List<Object[]> model(int amount) {

        List<Object[]> r = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            r.add(new Object[]{
                "String:" + R.nextInt(),
                new Date(),
                R.ints(1, 10000).findAny().getAsInt(),
                R.doubles(1, 5000).findAny().getAsDouble(),
                R.doubles(1, 4000).findAny().getAsDouble(),
                new SFormula(SR(3), "/", SR(4))
            });
        }
        return r;
    }

    /**
     * Create the Summary Block at the End.
     * <p/>
     * @param table        The Stable where all the data exist.
     * @param startingDate the startnig date of the Report.
     * @param endingDate   the ending date of the Report.
     * @return a SResult Block with the Summary.
     */
    private static SResult createSummary(STable table) {
        SResult r = new SResult();
        r.block = new SBlock();
        r.block.setFormat(new CFormat(CFormat.FontStyle.BOLD, Color.BLACK, Color.YELLOW, CFormat.HorizontalAlignment.RIGHT, new CBorder(Color.BLACK)));
        r.sum1 = new SCell(new SFormula("SUMME(", table.getCellFirstRow(2), ":", table.getCellLastRow(2), ")"));
        r.sum2 = new SCell(new SFormula("SUMME(", table.getCellFirstRow(3), ":", table.getCellLastRow(3), ")"), EURO);
        r.sum3 = new SCell(new SFormula("SUMME(", table.getCellFirstRow(4), ":", table.getCellLastRow(4), ")"), EURO);
        r.sum4 = new SCell(new SFormula("SUMME(", table.getCellFirstRow(5), ":", table.getCellLastRow(5), ")", "/ANZAHL(", table.getCellFirstRow(5), ":", table.getCellLastRow(5), ")"), new CFormat(RIGHT, PERCENT_FLOAT));
        r.block.add("Summe", new CFormat(Color.BLUE, Color.WHITE, CFormat.HorizontalAlignment.LEFT), r.sum1, r.sum2, r.sum3, r.sum4);
        return r;
    }

}
