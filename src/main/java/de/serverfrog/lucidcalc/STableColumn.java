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

import lombok.Getter;

/**
 * A great way to build a headline.
 * Template.getHeadLine().add(...).add(...).....
 *
 * @author oliver.guenther
 */
@Getter
@SuppressWarnings("unused")
public class STableColumn {

    private String head;
    private Integer size;
    @SuppressWarnings("rawtypes")
    private SAction action;
    private CFormat format;

    /**
     * <p>Constructor for STableColumn.</p>
     */
    public STableColumn() {
        this(null, null, null);
    }

    /**
     * <p>Constructor for STableColumn.</p>
     *
     * @param head a {@link java.lang.String} object
     */
    public STableColumn(String head) {
        this(head, null, null);
    }

    /**
     * <p>Constructor for STableColumn.</p>
     *
     * @param head a {@link java.lang.String} object
     * @param format a {@link de.serverfrog.lucidcalc.CFormat} object
     */
    public STableColumn(String head, CFormat format) {
        this(head, null, format);
    }

    /**
     * <p>Constructor for STableColumn.</p>
     *
     * @param head a {@link java.lang.String} object
     * @param size a {@link java.lang.Integer} object
     */
    public STableColumn(String head, Integer size) {
        this(head, size, null);
    }

    /**
     * <p>Constructor for STableColumn.</p>
     *
     * @param head a {@link java.lang.String} object
     * @param size a {@link java.lang.Integer} object
     * @param format a {@link de.serverfrog.lucidcalc.CFormat} object
     */
    public STableColumn(String head, Integer size, CFormat format) {
        this.head = head;
        this.size = size;
        this.format = format;
    }

    /**
     * <p>Setter for the field <code>format</code>.</p>
     *
     * @param format a {@link de.serverfrog.lucidcalc.CFormat} object
     * @return a {@link de.serverfrog.lucidcalc.STableColumn} object
     */
    public STableColumn setFormat(CFormat format) {
        this.format = format;
        return this;
    }

    /**
     * <p>Setter for the field <code>action</code>.</p>
     *
     * @param action a {@link de.serverfrog.lucidcalc.SAction} object
     * @return a {@link de.serverfrog.lucidcalc.STableColumn} object
     */
    public STableColumn setAction(@SuppressWarnings("rawtypes") SAction action) {
        this.action = action;
        return this;
    }

    /**
     * <p>Setter for the field <code>head</code>.</p>
     *
     * @param head a {@link java.lang.String} object
     * @return a {@link de.serverfrog.lucidcalc.STableColumn} object
     */
    public STableColumn setHead(String head) {
        this.head = head;
        return this;
    }

    /**
     * <p>Setter for the field <code>size</code>.</p>
     *
     * @param size a {@link java.lang.Integer} object
     * @return a {@link de.serverfrog.lucidcalc.STableColumn} object
     */
    public STableColumn setSize(Integer size) {
        this.size = size;
        return this;
    }

}
