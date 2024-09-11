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

import lombok.Setter;

/**
 * CellReference implementation there the row can be changed by an Action
 *
 * @author oliver.guenther
 */
public class SSelfRowReference implements CCellReference {

    private final int columnIndex;
    @Setter
    private int rowIndex;

    /**
     * <p>Constructor for SSelfRowReference.</p>
     *
     * @param columnIndex a int
     */
    public SSelfRowReference(int columnIndex) {
        this.columnIndex = columnIndex;
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

}
