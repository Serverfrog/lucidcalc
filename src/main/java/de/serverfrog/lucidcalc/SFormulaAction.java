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

import java.util.Arrays;

/**
 * This is a shortcut to add a Formula to a table directly as action
 *
 * @author oliver.guenther
 */
@SuppressWarnings("unused")
public class SFormulaAction extends SFormula implements SAction<Object> {

    /**
     * <p>Constructor for SFormulaAction.</p>
     *
     * @param elems a {@link java.lang.Object} object
     */
    public SFormulaAction(Object... elems) {
        super(elems);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unused")
    @Override
    public Object getValue(int relativeColumnIndex, int relativeRowIndex, int absoluteColumnIndex, int absoluteRowIndex, Object lineModel) {
        Object[] elements = Arrays.copyOf(super.getElements(), super.getElements().length);
        for (int i = 0; i < elements.length; i++) {
            Object elem = elements[i];
            if (elem instanceof final SSelfRowReference selfRow) {
                elements[i] = new CCellReferenceAdapter(absoluteRowIndex, selfRow.columnIndex());
            }
        }
        return new SFormula(elements);
    }

    /** {@inheritDoc} */
    @Override
    public CFormat getFormat(int relativeColumnIndex, int relativeRowIndex, int absoluteColumnIndex, int absoluteRowIndex, Object lineModel) {
        return null;
    }
}
