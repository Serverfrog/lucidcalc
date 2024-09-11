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
 * <p>CCell class.</p>
 *
 * @author oliver.guenther
 */
@SuppressWarnings("unused")
public class CCell implements CCellReference {

    private final int columnIndex;
    private final int rowIndex;
    @Getter
    private final Object value;
    @Getter
    private final CFormat format;

    /**
     * <p>Constructor for CCell.</p>
     *
     * @param columnIndex a int
     * @param rowIndex a int
     * @param value a {@link java.lang.Object} object
     * @param format a {@link de.serverfrog.lucidcalc.CFormat} object
     */
    public CCell(int columnIndex, int rowIndex, Object value, CFormat format) {
        this.columnIndex = columnIndex;
        this.rowIndex = rowIndex;
        this.value = value;
        this.format = format;
    }

    /**
     * <p>Constructor for CCell.</p>
     *
     * @param columnIndex a int
     * @param rowIndex a int
     * @param value a {@link java.lang.Object} object
     */
    public CCell(int columnIndex, int rowIndex, Object value) {
        this(columnIndex,rowIndex, value, null);
    }

    /** {@inheritDoc} */
    @Override
    public int columnIndex() {
        return columnIndex;
    }

    /** {@inheritDoc} */
    @Override
    public int rowIndex() {
        return rowIndex;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final CCell other = (CCell) obj;
        if (this.rowIndex != other.rowIndex) return false;
        return this.columnIndex == other.columnIndex;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + this.rowIndex;
        hash = 43 * hash + this.columnIndex;
        return hash;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "CCell{" + "columnIndex=" + columnIndex + ", rowIndex=" + rowIndex + ", value=" + value + ", format=" + format + '}';
    }

}
