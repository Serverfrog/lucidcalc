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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Abstract CCalcDocument class.</p>
 *
 * @author oliver.guenther
 */
@Getter
public abstract class CCalcDocument {

    //    @Valid
//    @NotNull
    private final List<CSheet> sheets;

    /**
     * <p>Constructor for CCalcDocument.</p>
     */
    protected CCalcDocument() {
        sheets = new ArrayList<>();
    }

    /**
     * <p>add.</p>
     *
     * @param sheet a {@link de.serverfrog.lucidcalc.CSheet} object
     * @return a {@link de.serverfrog.lucidcalc.CCalcDocument} object
     */
    public CCalcDocument add(CSheet sheet) {
        sheets.add(sheet);
        return this;
    }

    /**
     * <p>getFile.</p>
     *
     * @return a {@link java.io.File} object
     */
    public abstract File getFile();

}
