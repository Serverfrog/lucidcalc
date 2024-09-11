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
package de.serverfrog.lucidcalc;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * <p>LucidCalcReader interface.</p>
 *
 * @author oliver.guenther
 */
@SuppressWarnings("unused")
public interface LucidCalcReader {

    /**
     * Add a new Column definition for the read.
     * TODO: You may not add a column with a lower number than all columns before, check or change
     *
     * @param id   the column id (0=A, 1=B, 2=C, ... )
     * @param type the Type of the column. Allowed Values: String,Double,Integer
     * @return the JExcelLucidCalcReader
     */
    LucidCalcReader addColumn(int id, Class<?> type);

    /**
     * <p>getErrors.</p>
     *
     * @return a {@link java.util.List} object
     */
    List<String> getErrors();

    /**
     * <p>isError.</p>
     *
     * @return a boolean
     */
    boolean isError();

    /**
     * <p>isHeadline.</p>
     *
     * @return a boolean
     */
    boolean isHeadline();

    /**
     * <p>setHeadline.</p>
     *
     * @param headline a boolean
     */
    void setHeadline(boolean headline);

    /**
     * <p>isTrim.</p>
     *
     * @return a boolean
     */
    boolean isTrim();

    /**
     * <p>setTrim.</p>
     *
     * @param trim a boolean
     */
    void setTrim(boolean trim);

    /**
     * Reads an xls file and returns the contests as List of Instances of Type U
     * <p>
     * TODO: Inference Mechanism only counts the Parameters, this can be done better and more secure.
     *
     * @param <U>   the type
     * @param file  the file to be read
     * @param clazz the clazz to build the container Instance.
     * @return the list
     */
    <U> List<U> read(File file, Class<U> clazz);

    /**
     * <p>read.</p>
     *
     * @param is a {@link java.io.InputStream} object
     * @param clazz a {@link java.lang.Class} object
     * @param <U> a U class
     * @return a {@link java.util.List} object
     */
    <U> List<U> read(InputStream is, Class<U> clazz);

    /**
     * Reads an xls file and returns the contests as List of Instances of Type U
     * <p>
     * TODO: Inference Mechanism only counts the Parameters, this can be done better and more secure.
     *
     * @param <U>      the type
     * @param file     the file to be read
     * @param instance the instance to infer the type
     * @return the list
     */
    <U> List<U> read(File file, U instance);

    /**
     * <p>read.</p>
     *
     * @param file a {@link java.io.File} object
     * @return a {@link java.util.List} object
     */
    List<List<?>> read(File file);

    /**
     * Add new Column definition for the read, all types are String
     *
     * @param columns the columns
     */
    @SuppressWarnings("unused")
    void setColumns(int... columns);
}
