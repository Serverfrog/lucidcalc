/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.serverfrog.lucidcalc.jexcel;

import de.serverfrog.lucidcalc.CBorder;
import de.serverfrog.lucidcalc.CCalcDocument;
import de.serverfrog.lucidcalc.CFormat;
import de.serverfrog.lucidcalc.CSheet;
import de.serverfrog.lucidcalc.LucidCalc;
import de.serverfrog.lucidcalc.LucidCalcWriter;
import de.serverfrog.lucidcalc.SBlock;
import de.serverfrog.lucidcalc.SCell;
import de.serverfrog.lucidcalc.SFormula;
import de.serverfrog.lucidcalc.STable;
import de.serverfrog.lucidcalc.STableColumn;
import de.serverfrog.lucidcalc.STableModelList;
import de.serverfrog.lucidcalc.SUtil;
import de.serverfrog.lucidcalc.TempCalcDocument;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static de.serverfrog.lucidcalc.CFormat.FontStyle.BOLD;
import static de.serverfrog.lucidcalc.CFormat.HorizontalAlignment.CENTER;
import static de.serverfrog.lucidcalc.CFormat.HorizontalAlignment.LEFT;
import static de.serverfrog.lucidcalc.CFormat.HorizontalAlignment.RIGHT;
import static de.serverfrog.lucidcalc.CFormat.Representation.CURRENCY_EURO;
import static de.serverfrog.lucidcalc.CFormat.Representation.PERCENT_FLOAT;
import static de.serverfrog.lucidcalc.SUtil.selfRowReference;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author oliver.guenther
 */
public class LucidCalcWriterTest {

    private static final Random R = new Random();


    public static final CFormat EURO = CFormat.builder().horizontalAlignment(RIGHT).representation(CURRENCY_EURO).build();

    public static class SResult {

        public SBlock block;

        public SCell sum1;

        public SCell sum2;

        public SCell sum3;

        public SCell sum4;
    }

    @ParameterizedTest
    @EnumSource(LucidCalc.Backend.class)
    void verifyCreationOfDemoTable(LucidCalc.Backend backend) {
        File tempFile = generateDemoTableAsTempFile(backend);
        assertThat(tempFile).exists(); // TODO: A read of the excel file an verification of the contents would be nice.
        tempFile.deleteOnExit();
    }

    public static File generateDemoTableAsTempFile(LucidCalc.Backend backend) {


        STable one = new STable();
        one.setTableFormat(CFormat.builder().name("Verdana").size(10)
            .border(CBorder.builder().color(Color.BLACK).lineStyle(CBorder.LineStyle.THIN).build()).build());
        one.setHeadlineFormat(CFormat.builder().style(BOLD).background(Color.BLACK).foreground(Color.YELLOW).horizontalAlignment(CENTER)
            .verticalAlignment(CFormat.VerticalAlignment.MIDDLE).build());
        one.add(new STableColumn("A String", 20));
        one.add(new STableColumn("A Date", 10, CFormat.builder().representation(CFormat.Representation.SHORT_DATE).build()));
        one.add(new STableColumn("An Integer", 15));
        one.add(new STableColumn("Double I", 15, EURO));
        one.add(new STableColumn("Double II", 15, EURO));
        one.add(new STableColumn("Double%", 12, CFormat.builder().horizontalAlignment(CFormat.HorizontalAlignment.RIGHT)
            .representation(CFormat.Representation.PERCENT_FLOAT).build())
            .setAction(SUtil.getSELF_ROW()));
        one.setModel(new STableModelList<>(model(20)));
        STable two = new STable(one);
        two.setModel(new STableModelList<>(model(5)));

        SResult oneSummary = createSummary(one);
        SResult twoSummary = createSummary(two);

        SBlock summary = new SBlock();

        summary.setFormat(CFormat.builder().style(BOLD).background(Color.BLACK).foreground(Color.YELLOW).horizontalAlignment(RIGHT)
            .border(new CBorder(Color.BLACK)).build());
        SCell sum3 = new SCell(new SFormula(oneSummary.sum1, "+", twoSummary.sum1), EURO);
        SCell sum4 = new SCell(new SFormula(oneSummary.sum2, "+", twoSummary.sum2), EURO);
        summary.add("Sum", CFormat.builder().background(Color.BLUE).foreground(Color.WHITE).horizontalAlignment(LEFT).build(),
            sum3,
            sum4,
            new SFormula(oneSummary.sum3, "+", twoSummary.sum3), EURO);

        CSheet sheet = new CSheet("DemoSheet");
        sheet.setShowGridLines(false);
        sheet.addBelow(new SBlock("DemoTable I", CFormat.builder().style(BOLD).build(), false));
        sheet.addBelow(one);
        sheet.addBelow(1, 1, oneSummary.block);
        sheet.addBelow(new SBlock("DemoTable II", CFormat.builder().style(BOLD).build(), true));
        sheet.addBelow(two);
        sheet.addBelow(1, 1, twoSummary.block);
        sheet.addBelow(1, 1, summary);

        CCalcDocument doc = new TempCalcDocument("DemoFile_");
        doc.add(sheet);
        final LucidCalcWriter writer = LucidCalc.createWriter(backend);
        assertThat(writer).isNotNull();
        return writer.write(doc);
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
                R.ints(1, 10000).findAny().orElseThrow(),
                R.doubles(1, 5000).findAny().orElseThrow(),
                R.doubles(1, 4000).findAny().orElseThrow(),
                new SFormula(selfRowReference(3), "/", selfRowReference(4))
            });
        }
        return r;
    }

    /**
     * Create the Summary Block at the End.
     * <p/>
     *
     * @param table The Stable where all the data exist.
     * @return a SResult Block with the Summary.
     */
    private static SResult createSummary(STable table) {
        SResult r = new SResult();
        r.block = new SBlock();
        r.block.setFormat(CFormat.builder().style(CFormat.FontStyle.BOLD).background(Color.BLACK).foreground(Color.YELLOW)
            .horizontalAlignment(CFormat.HorizontalAlignment.RIGHT).border(new CBorder(Color.BLACK)).build());
        r.sum1 = new SCell(new SFormula("SUMME(", table.getCellFirstRow(2), ":", table.getCellLastRow(2), ")"));
        r.sum2 = new SCell(new SFormula("SUMME(", table.getCellFirstRow(3), ":", table.getCellLastRow(3), ")"), EURO);
        r.sum3 = new SCell(new SFormula("SUMME(", table.getCellFirstRow(4), ":", table.getCellLastRow(4), ")"), EURO);
        r.sum4 = new SCell(new SFormula("SUMME(", table.getCellFirstRow(5), ":", table.getCellLastRow(5), ")",
            "/ANZAHL(", table.getCellFirstRow(5), ":", table.getCellLastRow(5), ")"),
            CFormat.builder().horizontalAlignment(RIGHT).representation(PERCENT_FLOAT).build());
        r.block.add("Summe",
            CFormat.builder().background(Color.BLUE).foreground(Color.WHITE).horizontalAlignment(CFormat.HorizontalAlignment.LEFT).build(),
            r.sum1, r.sum2, r.sum3, r.sum4);
        return r;
    }

}
