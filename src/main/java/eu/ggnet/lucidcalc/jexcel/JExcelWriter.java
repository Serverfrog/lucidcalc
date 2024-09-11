/*
 * Copyright (C) 2014 GG-Net GmbH - Oliver Günther.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; If not, see <http://www.gnu.org/licenses/>.
 */
package eu.ggnet.lucidcalc.jexcel;

import eu.ggnet.lucidcalc.CBorder;
import eu.ggnet.lucidcalc.CCalcDocument;
import eu.ggnet.lucidcalc.CCell;
import eu.ggnet.lucidcalc.CColumnView;
import eu.ggnet.lucidcalc.CFormat;
import eu.ggnet.lucidcalc.CRowView;
import eu.ggnet.lucidcalc.CSheet;
import eu.ggnet.lucidcalc.IFormula;
import eu.ggnet.lucidcalc.LucidCalcWriter;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.biff.formula.FormulaParser;
import jxl.format.Border;
import jxl.write.Blank;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static eu.ggnet.lucidcalc.CBorder.LineStyle.NONE;
import static eu.ggnet.lucidcalc.CFormat.FontStyle.NORMAL;
import static eu.ggnet.lucidcalc.CFormat.HorizontalAlignment.LEFT;
import static eu.ggnet.lucidcalc.CFormat.Representation.DEFAULT;
import static eu.ggnet.lucidcalc.CFormat.VerticalAlignment.TOP;
import static java.awt.Color.BLACK;
import static java.awt.Color.GRAY;
import static java.awt.Color.WHITE;

/**
 *
 */
public class JExcelWriter implements LucidCalcWriter {

    private final static CFormat DEFAULT_FORMAT = new CFormat("Verdana", 10, NORMAL, BLACK, WHITE, LEFT, TOP, DEFAULT,
        new CBorder(GRAY, NONE), false);

    @Override
    public File write(CCalcDocument document) {
        if (document == null) {
            return null;
        }
//        validate(document); // was done via the hibernate validator.
        try {
            Map<CFormat, WritableCellFormat> formatCache = new HashMap<>();
            File file = document.getFile();
            WorkbookSettings ws = new WorkbookSettings();
            ws.setLocale(Locale.GERMANY);
            WritableWorkbook workbook = Workbook.createWorkbook(file, ws);
            FormatUtil util = new FormatUtil();

            for (int sheetIndex = 0; sheetIndex < document.getSheets().size(); sheetIndex++) {

                CSheet csheet = document.getSheets().get(sheetIndex);
                WritableSheet jsheet = workbook.createSheet(csheet.getName(), sheetIndex);
                jsheet.getSettings().setShowGridLines(csheet.isShowGridLines());
                for (CColumnView cColumn : csheet.getColumnViews()) {
                    jsheet.setColumnView(cColumn.columnIndex(), cColumn.size());
                }

                for (CRowView cRowView : csheet.getRowViews()) {
                    jsheet.setRowView(cRowView.rowIndex(), cRowView.size());
                }

                for (CCell cCell : csheet.getCells()) {
                    WritableCellFormat jformat;
                    CFormat cFormat = cCell.getFormat();
                    if (cFormat == null) {
                        jformat = WritableWorkbook.NORMAL_STYLE;
                    } else {
                        cFormat = cFormat.fillNull(DEFAULT_FORMAT);
                        if (formatCache.containsKey(cFormat)) {
                            jformat = formatCache.get(cFormat);
                        } else {
                            WritableFont jfont = new WritableFont(WritableFont.createFont(cFormat.getName()));
                            jfont.setPointSize(cFormat.getSize());
                            jfont.setColour(util.discover(cFormat.getForeground()));
                            switch (cFormat.getStyle()) {
                                case BOLD:
                                    jfont.setBoldStyle(jxl.write.WritableFont.BOLD);
                                    break;
                                case ITALIC:
                                    jfont.setItalic(true);
                                    break;
                                case BOLD_ITALIC:
                                    jfont.setBoldStyle(jxl.write.WritableFont.BOLD);
                                    jfont.setItalic(true);
                                    break;
                            }
                            jformat = new WritableCellFormat(jfont, util.discover(cFormat.getRepresentation()));
                            jformat.setBackground(util.discover(cFormat.getBackground()));
                            jformat.setAlignment(util.discover(cFormat.getHorizontalAlignment()));
                            jformat.setVerticalAlignment(util.discover(cFormat.getVerticalAlignment()));
                            if (cFormat.getBorder() != null) {
                                jformat.setBorder(Border.ALL, util.discover(cFormat.getBorder().getLineStyle()),
                                    util.discover(cFormat.getBorder().getColor()));
                            }
                            if (cFormat.isWrap() != null) {
                                jformat.setWrap(cFormat.isWrap());
                            }
                            formatCache.put(cFormat, jformat);
                        }
                    }

                    final WritableCell jcell = getWritableCell(cCell, jformat, ws);
                    jsheet.addCell(jcell);
                }
            }
            workbook.write();
            workbook.close();
            return file;
        } catch (WriteException | IOException ex) {
            throw new RuntimeException("Exception during document creation", ex);
        }
    }

    private static WritableCell getWritableCell(final CCell cCell, final WritableCellFormat jformat, final WorkbookSettings ws) {
        Object elem = cCell.getValue();
        WritableCell jcell;
        switch (elem) {
            case null -> jcell = new Blank(cCell.columnIndex(), cCell.rowIndex(), jformat);
            case Number number -> {
                double value = Double.MIN_VALUE;
                if (elem instanceof Integer) {
                    value = ((Integer) elem).doubleValue();
                } else if (elem instanceof Long) {
                    value = ((Long) elem).doubleValue();
                } else if (elem instanceof Float) {
                    value = ((Float) elem).doubleValue();
                } else if (elem instanceof Double) {
                    value = (Double) elem;
                }
                // TODO The rest of the 8 datatypes
                jcell = new jxl.write.Number(cCell.columnIndex(), cCell.rowIndex(), value, jformat);
            }
            case Boolean b -> jcell = new jxl.write.Boolean(cCell.columnIndex(), cCell.rowIndex(), b, jformat);
            case Date date -> jcell = new jxl.write.DateTime(cCell.columnIndex(), cCell.rowIndex(), date, jformat);
            case final IFormula f -> {
                try {
                    FormulaParser parser = new FormulaParser(f.toRawFormula(), null, null, ws);
                    parser.parse();
                    jcell = new jxl.write.Formula(cCell.columnIndex(), cCell.rowIndex(), f.toRawFormula(), jformat);
                } catch (Exception e) {
                    jcell = new jxl.write.Label(cCell.columnIndex(), cCell.rowIndex(), "Error in Formula: " + f.toRawFormula() + " | " + e, jformat);
                }
            }
            default -> jcell = new jxl.write.Label(cCell.columnIndex(), cCell.rowIndex(), elem.toString(), jformat);
        }
        return jcell;
    }
}
