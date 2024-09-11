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
import lombok.Setter;

/**
 * <p>CSheet class.</p>
 *
 * @author oliver.guenther
 */
@SuppressWarnings("unused")
public class CSheet extends CCellComposite {

    @Setter
    @Getter
    private boolean showGridLines = true;

    @Setter
    @Getter
    private String name;

    private int endRowIndex = 0;

    /**
     * <p>Constructor for CSheet.</p>
     *
     * @param name a {@link java.lang.String} object
     * @param containers a {@link de.serverfrog.lucidcalc.IDynamicCellContainer} object
     */
    public CSheet(String name, IDynamicCellContainer... containers) {
        this(name);
        for (IDynamicCellContainer container : containers) {
            addBelow(container);
        }
    }

    /**
     * The name of the sheet.
     * <p>
     *
     * @param name of the sheet.
     */
    public CSheet(String name) {
        this.name = name;
    }

    /**
     * <p>Constructor for CSheet.</p>
     *
     * @param name a {@link java.lang.String} object
     * @param columnsizes a {@link java.lang.Integer} object
     */
    public CSheet(String name, Integer... columnsizes) {
        this.name = name;
        if (columnsizes == null) return;
        for (int i = 0; i < columnsizes.length; i++) {
            Integer size = columnsizes[i];
            columnViews.add(new CColumnView(i, size));
        }
    }

    /**
     * <p>Constructor for CSheet.</p>
     */
    public CSheet() {
        this("NoName");
    }

    /**
     * <p>addBelow.</p>
     *
     * @param columnDelta a int
     * @param rowDelta a int
     * @param container a {@link de.serverfrog.lucidcalc.IDynamicCellContainer} object
     */
    public final void addBelow(int columnDelta, int rowDelta, IDynamicCellContainer container) {
        CCellComposite composite = container.shiftTo(columnDelta, endRowIndex + rowDelta);
        // TODO: Very simple inference of column sizes
        if (columnViews.isEmpty() && composite.getColumnViews() != null) columnViews = composite.getColumnViews();
        if (composite.getRowViews() != null) rowViews.addAll(composite.getRowViews());
        cells.addAll(composite.getCells());
        endRowIndex += rowDelta + container.getRowCount();
    }

    /**
     * <p>addBelow.</p>
     *
     * @param container a {@link de.serverfrog.lucidcalc.IDynamicCellContainer} object
     */
    public final void addBelow(IDynamicCellContainer container) {
        addBelow(0, 0, container);
    }
}
