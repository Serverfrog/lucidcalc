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
package de.serverfrog.lucidcalc.jexcel;

import de.serverfrog.lucidcalc.CBorder;
import de.serverfrog.lucidcalc.CCalcDocument;
import de.serverfrog.lucidcalc.CCell;
import de.serverfrog.lucidcalc.CColumnView;
import de.serverfrog.lucidcalc.CFormat;
import de.serverfrog.lucidcalc.CRowView;
import de.serverfrog.lucidcalc.CSheet;
import de.serverfrog.lucidcalc.IFormula;
import de.serverfrog.lucidcalc.LucidCalcWriter;
import de.serverfrog.lucidcalc.exception.LibraryException;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.biff.formula.FormulaParser;
import jxl.format.Border;
import jxl.write.Blank;
import jxl.write.DateTime;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static de.serverfrog.lucidcalc.CBorder.LineStyle.NONE;
import static de.serverfrog.lucidcalc.CFormat.FontStyle.NORMAL;
import static de.serverfrog.lucidcalc.CFormat.HorizontalAlignment.LEFT;
import static de.serverfrog.lucidcalc.CFormat.Representation.DEFAULT;
import static de.serverfrog.lucidcalc.CFormat.VerticalAlignment.TOP;
import static java.awt.Color.BLACK;
import static java.awt.Color.GRAY;
import static java.awt.Color.WHITE;

/**
 * <p>JExcelWriter class.</p>
 *
 * @author oliver.guenther
 */
@Slf4j
@SuppressWarnings("unused")
public class JExcelWriter implements LucidCalcWriter {

    private final static CFormat DEFAULT_FORMAT = new CFormat("Verdana", 10, NORMAL, BLACK, WHITE, LEFT, TOP, DEFAULT,
        new CBorder(GRAY, NONE), false);

    /** {@inheritDoc} */
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

                processCells(csheet, formatCache, util, ws, jsheet);
            }
            workbook.write();
            workbook.close();
            return file;
        } catch (WriteException | IOException ex) {
            throw new LibraryException("Exception during document creation", ex);
        }
    }

    private static void processCells(final CSheet csheet, final Map<CFormat, WritableCellFormat> formatCache, final FormatUtil util, final WorkbookSettings ws, final WritableSheet jsheet) throws WriteException {
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
                    jformat = setNewFormat(cFormat, util);
                    formatCache.put(cFormat, jformat);
                }
            }

            final WritableCell jcell = getWritableCell(cCell, jformat, ws);
            jsheet.addCell(jcell);
        }
    }

    private static WritableCellFormat setNewFormat(final CFormat cFormat, final FormatUtil util) throws WriteException {
        WritableCellFormat jformat;
        WritableFont jfont = new WritableFont(WritableFont.createFont(cFormat.getName()));
        jfont.setPointSize(cFormat.getSize());
        jfont.setColour(util.discover(cFormat.getForeground()));
        switch (cFormat.getStyle()) {
            case BOLD:
                jfont.setBoldStyle(WritableFont.BOLD);
                break;
            case ITALIC:
                jfont.setItalic(true);
                break;
            case BOLD_ITALIC:
                jfont.setBoldStyle(WritableFont.BOLD);
                jfont.setItalic(true);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + cFormat.getStyle());
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
        return jformat;
    }

    private static WritableCell getWritableCell(final CCell cCell, final WritableCellFormat jformat, final WorkbookSettings ws) {
        Object elem = cCell.getValue();
        WritableCell jcell;
        switch (elem) {
            case null -> jcell = new Blank(cCell.columnIndex(), cCell.rowIndex(), jformat);
            case Number number -> {
                double value = Double.MIN_VALUE;
                switch (elem) {
                    case Integer i -> value = i.doubleValue();
                    case Long l -> value = l.doubleValue();
                    case Float v -> value = v.doubleValue();
                    case Double v -> value = v;
                    default -> log.warn("Type " + elem + " not supported");

                }
                // TODO The rest of the 8 datatypes
                jcell = new jxl.write.Number(cCell.columnIndex(), cCell.rowIndex(), value, jformat);
            }
            case Boolean b -> jcell = new jxl.write.Boolean(cCell.columnIndex(), cCell.rowIndex(), b, jformat);
            case Date date -> jcell = new DateTime(cCell.columnIndex(), cCell.rowIndex(), date, jformat);
            case final IFormula f -> {
                try {
                    FormulaParser parser = new FormulaParser(f.toRawFormula(), null, null, ws);
                    parser.parse();
                    jcell = new Formula(cCell.columnIndex(), cCell.rowIndex(), f.toRawFormula(), jformat);
                } catch (Exception e) {
                    jcell = new Label(cCell.columnIndex(), cCell.rowIndex(), "Error in Formula: " + f.toRawFormula() + " | " + e, jformat);
                }
            }
            default -> jcell = new Label(cCell.columnIndex(), cCell.rowIndex(), elem.toString(), jformat);
        }
        return jcell;
    }
}
