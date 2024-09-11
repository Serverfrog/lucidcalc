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

import de.serverfrog.lucidcalc.apachepoi.ApachePoiReader;
import de.serverfrog.lucidcalc.apachepoi.ApachePoiWriter;
import de.serverfrog.lucidcalc.jexcel.JExcelReader;
import de.serverfrog.lucidcalc.jexcel.JExcelWriter;

/**
 * Generator
 */
public abstract class LucidCalc {

    private LucidCalc() {
    }

    /**
     * Create the specific Backend for the given Argument
     * @param backend is the Backend which should be used
     * @return the implemented LucidCalcWriter or an IllegalArgumentException if the Backend is not implemented
     */
    public static LucidCalcWriter createWriter(Backend backend) {
        return switch (backend) {
            case JEXCEL -> new JExcelWriter();
            case APACHE_POI -> new ApachePoiWriter();
            default -> throw new IllegalArgumentException("Unknown backend: " + backend);
        };
    }

    /**
     * Create the specific Backend for the given Argument
     * @param backend is the Backend which should be used
     * @return the implemented LucidCalcReader or an IllegalArgumentException if the Backend is not implemented
     */
    public static LucidCalcReader createReader(Backend backend) {
        return switch (backend) {
            case JEXCEL -> new JExcelReader();
            case APACHE_POI -> new ApachePoiReader();
            default -> throw new IllegalArgumentException("Unknown backend: " + backend);
        };
    }

    public enum Backend {

        JEXCEL,
        APACHE_POI
    }
}
