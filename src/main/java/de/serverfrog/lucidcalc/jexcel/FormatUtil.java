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
import de.serverfrog.lucidcalc.CFormat;
import jxl.biff.DisplayFormat;
import jxl.format.Alignment;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.DateFormat;
import jxl.write.NumberFormat;
import jxl.write.NumberFormats;

import java.awt.*;

/**
 * FormatUtil for the JExcelOffice. This utility class must be handled special. Because the JExcelApi keeps some activities static, it must be ensured,
 * that every object created for a Workbook will be dropped and recreated for another workbook. To ensure this, an instance of the utility class is needed.
 */
public class FormatUtil {

    private DateFormat shortDate;

    private NumberFormat currencyEuro;

    public Colour discover(Color color) {
        for (Colour colour : Colour.getAllColours()) {
            if (colour.getDefaultRGB().getBlue() == color.getBlue()
                && colour.getDefaultRGB().getRed() == color.getRed()
                && colour.getDefaultRGB().getGreen() == color.getGreen()) {
                return colour;
            }
        }
        return Colour.UNKNOWN;
    }

    public Alignment discover(CFormat.HorizontalAlignment horizontalAlignment) {
        return switch (horizontalAlignment) {
            case CENTER -> Alignment.CENTRE;
            case LEFT -> Alignment.LEFT;
            case RIGHT -> Alignment.RIGHT;
        };
    }

    public VerticalAlignment discover(CFormat.VerticalAlignment verticalAlignment) {
        return switch (verticalAlignment) {
            case TOP -> VerticalAlignment.TOP;
            case MIDDLE -> VerticalAlignment.CENTRE;
            case BOTTOM -> VerticalAlignment.BOTTOM;
        };
    }

    public DisplayFormat discover(CFormat.Representation representation) {
        return switch (representation) {
            case DEFAULT -> NumberFormats.DEFAULT;
            case TEXT -> NumberFormats.TEXT;
            case PERCENT_INTEGER -> NumberFormats.PERCENT_INTEGER;
            case PERCENT_FLOAT -> NumberFormats.PERCENT_FLOAT;
            case SHORT_DATE -> {
                if (shortDate == null) shortDate = new DateFormat("dd.MM.yy");
                yield shortDate;
            }
            case CURRENCY_EURO -> {
                if (currencyEuro == null) currencyEuro = new NumberFormat("#,#00.00 \u20AC", NumberFormat.COMPLEX_FORMAT);
                yield currencyEuro;
            }
        };
    }

    public BorderLineStyle discover(CBorder.LineStyle lineStyle) {
        if (lineStyle == null) return BorderLineStyle.NONE;
        // This just works, because I use the same order.
        return BorderLineStyle.getStyle(lineStyle.ordinal());
    }
}
