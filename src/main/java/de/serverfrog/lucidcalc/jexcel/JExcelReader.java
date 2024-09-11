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

import de.serverfrog.lucidcalc.LucidCalcReader;
import de.serverfrog.lucidcalc.exception.ObjectCreationException;
import de.serverfrog.lucidcalc.exception.ParsingException;
import jxl.Cell;
import jxl.DateCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.read.biff.BlankCell;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 * The most KIS tool for reading plain calc docs.
 * If not other configured, the defaults are
 * <ul>
 * <li>headline = true</li>
 * <li>trim = true</li>
 * <li>columns = {0}</li>
 * <li>all Types are String</li>
 * </ul>
 *
 * @author oliver.guenther
 */
// TODO: Unittest
@Slf4j
@SuppressWarnings("unused")
public class JExcelReader implements LucidCalcReader {

    private enum ImplementedClasses {
        STRING(String.class),
        DOUBLE(Double.class),
        INTEGER(Integer.class),
        DATE(Date.class),
        ;

        private final Class<?> clazz;

        ImplementedClasses(final Class<?> clazz) {
            this.clazz = clazz;
        }

        public static ImplementedClasses getForClass(final Class<?> clazz) {
            return Arrays.stream(ImplementedClasses.values()).filter(implementedClasses -> implementedClasses.clazz == clazz)
                .findFirst().orElseThrow(UnsupportedOperationException::new);
        }
    }

    /**
     * Indicator to overjump the first line
     */
    private boolean headline;

    /**
     * Indicator to trim all strings on read.
     */
    private boolean trim;

    /**
     * the columns to be read
     * (e.g. {0,1,5} => resultlist has 3 elements 1 => column 0, 2 => column 1, 3 => column 5)
     */
    private final Map<Integer, Class<?>> columns;

    private final List<String> errors;

    /**
     * Default constructor, sets, headline and trim = true.
     */
    public JExcelReader() {
        headline = true;
        trim = true;
        columns = new HashMap<>();
        errors = new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Add a new Column definition for the read.
     * TODO: You may not add a column with a lower number than all columns before, check or change
     */
    @Override
    public LucidCalcReader addColumn(int id, Class<?> type) {
        this.columns.put(id, type);
        return this;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Add new Column definition for the read, all types are String
     */
    @Override
    public void setColumns(int... columns) {
        for (int i : columns) {
            this.columns.put(i, String.class);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isHeadline() {
        return headline;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setHeadline(boolean headline) {
        this.headline = headline;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTrim() {
        return trim;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTrim(boolean trim) {
        this.trim = trim;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isError() {
        return !errors.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getErrors() {
        return errors;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Reads an xls file and returns the contests as List of Instances of Type U
     * TODO: Inference Mechanism only counts the Parameters, this can be done better and more secure.
     */
    @Override
    public <U> List<U> read(File file, Class<U> clazz) {
        return read(toWorkbook(file), clazz);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <U> List<U> read(InputStream is, Class<U> clazz) {
        return read(toWorkbook(is), clazz);
    }

    private <U> List<U> read(Workbook doc, Class<U> clazz) {
        Constructor<?> constructor = null;
        for (Constructor<?> c : clazz.getConstructors()) {
            if (c.getParameterTypes().length == columns.size()) {
                constructor = c;
                break;
            }
        }
        if (constructor == null) throw new ClassCastException(clazz + " doesnt have a constructor with " + columns.size() + " Parameters");
        List<U> result = new ArrayList<>();
        for (List<?> line : read(doc)) {
            try {
                log.debug("Creating Instance of {} with {}", clazz, line);
                U u = clazz.cast(constructor.newInstance(line.toArray()));
                result.add(u);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                List<Class<?>> clazzes = new ArrayList<>();
                for (Object object : line) {
                    clazzes.add(object == null ? null : object.getClass());
                }
                throw new ObjectCreationException("Could not create an Instance of " + clazz
                    + "\nParameters(size=" + line.size() + "):" + line
                    + "\nClasses(size=" + clazzes.size() + "):" + clazzes
                    + "\nConstructorTypes(size=" + constructor.getParameterTypes().length + "):" + Arrays.toString(constructor.getParameterTypes()), ex);
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Reads an xls file and returns the contests as List of Instances of Type U
     * <p>
     * TODO: Inference Mechanism only counts the Parameters, this can be done better and more secure.
     */
    @SuppressWarnings("unchecked")
    @Override
    public <U> List<U> read(File file, U instance) {
        return (List<U>) read(file, instance.getClass());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<List<?>> read(File file) {
        return read(toWorkbook(file));
    }

    private Workbook toWorkbook(File file) {
        try {
            return Workbook.getWorkbook(file);
        } catch (IOException | BiffException ex) {
            throw new ParsingException(ex);
        }
    }

    private Workbook toWorkbook(InputStream is) {
        try {
            return Workbook.getWorkbook(is);
        } catch (IOException | BiffException ex) {
            throw new ParsingException(ex);
        }
    }

    /**
     * Reads a xls file and returns the contents as list of lists.
     *
     * @param doc the file to be read
     * @return the list
     */
    // TODO: Check null file
    // TODO reduce complexity
    private List<List<?>> read(Workbook doc) {
        if (columns.isEmpty()) columns.put(0, String.class); // If no columns are defined, just read in the first as String

        List<List<?>> result = new ArrayList<>();
        Sheet sheet = doc.getSheet(0);
        for (int rowIndex = (headline ? 1 : 0); rowIndex < sheet.getRows(); rowIndex++) {
            if (isEmptyRow(sheet, rowIndex)) continue; // Ignore empty rows
            List<Object> line = new ArrayList<>();
            result.add(line);
            for (Integer columnIndex : new TreeSet<>(columns.keySet())) {
                Cell cell = sheet.getCell(columnIndex, rowIndex);
                if (log.isDebugEnabled()) {
                    log.debug("Reading Cell (c={},r={},type={})={}", columnIndex, rowIndex, cell.getClass().getSimpleName(), cell.getContents());
                }
                Class<?> type = columns.get(columnIndex);

                addLine(line, type, cell, rowIndex, columnIndex);
            }
        }
        doc.close();
        return result;
    }

    private boolean isEmptyRow(Sheet sheet, int i) {
        return columns.keySet().stream()
            .map(column -> sheet.getCell(column, i))
            .filter(c -> !(c instanceof BlankCell))
            .filter(c -> c.getContents() != null)
            .allMatch(c -> c.getContents()
                .trim().isEmpty());
    }

    private void addLine(final List<Object> line, final Class<?> type, final Cell cell, int rowIndex, int columnIndex) {
        if (cell instanceof BlankCell) {
            line.add(null);
            return;
        }
        final ImplementedClasses forClass = ImplementedClasses.getForClass(type);
        switch (forClass) {
            case STRING:
                String value = cell.getContents();
                if (trim && value != null) value = value.trim();
                line.add(value);
                break;
            case DOUBLE:
                line.add(parseDouble(cell, rowIndex, columnIndex));
                break;
            case INTEGER:
                line.add(parseInt(cell, rowIndex, columnIndex));
                break;
            case DATE:
                line.add(parseDate(cell, rowIndex, columnIndex));
                break;
        }
    }

    private double parseDouble(final Cell cell, final int rowIndex, final int columnIndex) {
        try {
            return ((NumberCell) cell).getValue();
        } catch (ClassCastException cce) {
            errors.add(createErrorMessage(rowIndex, columnIndex, cell.getContents(), cce.getMessage(), Double.class));
            return -9999999;
        }
    }

    private int parseInt(final Cell cell, final int rowIndex, final int columnIndex) {
        try {
            double value = ((NumberCell) cell).getValue();
            return (int) value;
        } catch (ClassCastException cce) {
            errors.add(createErrorMessage(rowIndex, columnIndex, cell.getContents(), cce.getMessage(), Integer.class));
            return -9999999;
        }
    }

    private Date parseDate(final Cell cell, final int rowIndex, final int columnIndex) {
        try {
            if (cell.getContents() == null || cell.getContents().isEmpty()) return null;
            else {
                return ((DateCell) cell).getDate();
            }
        } catch (ClassCastException cce) {
            errors.add(createErrorMessage(rowIndex, columnIndex, cell.getContents(), cce.getMessage(), Date.class));
            return null;
        }
    }

    private String createErrorMessage(int rowIndex, int columnIndex, String cellContent, String exceptionMessage, Class<?> clazz) {
        return "Error(row=" + rowIndex + ",column=" + columnIndex + ",value=" + cellContent + ") Type is not "
            + clazz.getName() + ", ErrorMessage(" + exceptionMessage + ")";
    }
}
