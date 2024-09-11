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

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.awt.*;

/**
 * <p>CBorder class.</p>
 *
 * @author oliver.guenther
 */
@Getter
@ToString
@EqualsAndHashCode
@Builder
public class CBorder {

    private final Color color;
    private final LineStyle lineStyle;
    /**
     * <p>Constructor for CBorder.</p>
     *
     * @param color a {@link java.awt.Color} object
     * @param lineStyle a {@link de.serverfrog.lucidcalc.CBorder.LineStyle} object
     */
    public CBorder(Color color, LineStyle lineStyle) {
        this.lineStyle = lineStyle;
        this.color = color;
    }

    /**
     * <p>Constructor for CBorder.</p>
     *
     * @param color a {@link java.awt.Color} object
     */
    public CBorder(Color color) {
        this(color, LineStyle.THIN);
    }

    @SuppressWarnings("unused")
    public enum LineStyle {

        NONE, THIN, MEDIUM, DASHED, DOTTED, THICK, DOUBLE, HAIR, MEDIUM_DASHED, DASH_DOT, MEDIUM_DASH_DOT, DASH_DOT_DOT, MEDIUM_DASH_DOT_DOT, SLANTED_DASH_DOT
    }

}
