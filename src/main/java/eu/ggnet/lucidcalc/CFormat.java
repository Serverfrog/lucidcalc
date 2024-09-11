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
package eu.ggnet.lucidcalc;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.*;
import java.util.Objects;

/**
 * The Format for any TemplateElement.
 * The Hierarchy is as follows (First overrides last):
 * <ul>
 * <li>Cell</li>
 * <li>Line</li>
 * <li>Column</li>
 * <li>Sheet</li>
 * <li>Document</li>
 * <li><i>Default</i></li>
 * </ul>
 * Null values mean no override.
 */
@AllArgsConstructor
public class CFormat {

    @Getter
    private final String name;

    @Getter
    private final Integer size;

    @Getter
    private final FontStyle style;

    @Getter
    private final Color foreground;

    @Getter
    private final Color background;

    @Getter
    private final HorizontalAlignment horizontalAlignment;

    @Getter
    private final VerticalAlignment verticalAlignment;

    @Getter
    private final Representation representation;

    @Getter
    private final CBorder border;

    private final Boolean wrap;


    public CFormat(FontStyle style, Color foreground, Color background, HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment) {
        this(null, null, style, foreground, background, horizontalAlignment, verticalAlignment, null, null, null);
    }

    public CFormat(FontStyle style, Color foreground, Color background, HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment, Representation representation, CBorder border) {
        this(null, null, style, foreground, background, horizontalAlignment, verticalAlignment, representation, border, null);
    }

    public CFormat(FontStyle style, Color foreground, Color background, HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment, Representation representation) {
        this(null, null, style, foreground, background, horizontalAlignment, verticalAlignment, representation, null, null);
    }

    public CFormat(FontStyle style, Color foreground, Color background, HorizontalAlignment horizontalAlignment, CBorder border) {
        this(null, null, style, foreground, background, horizontalAlignment, null, null, border, null);
    }

    public CFormat(FontStyle style, Representation representation) {
        this(null, null, style, null, null, null, null, representation, null, null);
    }

    public CFormat(Color foreground, Color background) {
        this(null, null, null, foreground, background, null, null, null, null, null);
    }

    public CFormat(Color foreground, Color background, HorizontalAlignment horizontalAlignment) {
        this(null, null, null, foreground, background, horizontalAlignment, null, null, null, null);
    }

    public CFormat(Color foreground, Color background, CBorder border) {
        this(null, null, null, foreground, background, null, null, null, border, null);
    }

    public CFormat(String name, int size) {
        this(name, size, null, null, null, null, null, null, null, null);
    }

    public CFormat(String name, int size, CBorder border) {
        this(name, size, null, null, null, null, null, null, border, null);
    }

    public CFormat(FontStyle fontStyle) {
        this(null, null, fontStyle, null, null, null, null, null, null, null);
    }

    public CFormat(CBorder border) {
        this(null, null, null, null, null, null, null, null, border, null);
    }

    public CFormat(HorizontalAlignment horizontalAlignment, Representation representation) {
        this(null, null, null, null, null, horizontalAlignment, null, representation, null, null);
    }

    public CFormat(HorizontalAlignment horizontalAlignment) {
        this(null, null, null, null, null, horizontalAlignment, null, null, null, null);
    }

    public CFormat(HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment) {
        this(null, null, null, null, null, horizontalAlignment, verticalAlignment, null, null, null);
    }

    public CFormat(HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment, CBorder border) {
        this(null, null, null, null, null, horizontalAlignment, verticalAlignment, null, border, null);
    }

    public CFormat(HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment, Boolean wrap) {
        this(null, null, null, null, null, horizontalAlignment, verticalAlignment, null, null, wrap);
    }

    public CFormat(HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment, CBorder border, Boolean wrap) {
        this(null, null, null, null, null, horizontalAlignment, verticalAlignment, null, border, wrap);
    }

    public CFormat(HorizontalAlignment horizontalAlignment, VerticalAlignment verticalAlignment, Representation representation, CBorder border, Boolean wrap) {
        this(null, null, null, null, null, horizontalAlignment, verticalAlignment, representation, border, wrap);
    }

    public CFormat(Representation representation) {
        this(null, null, null, null, null, null, null, representation, null, null);
    }

    /**
     * Fills values in the primary format which are null with values of the secondary format
     *
     * @param primary   the primary format
     * @param secondary the secondary format
     * @return a combination of both
     */
    public static CFormat combine(CFormat primary, CFormat secondary) {
        if (primary == null && secondary == null) return null;
        else if (secondary == null) return primary;
        else if (primary == null) return secondary;
        return primary.fillNull(secondary);
    }

    /**
     * Returns a new CFormat instance, which fills all null fields with values from the defaults.
     *
     * @param defaults the defaults to use on null fields
     * @return a new instance
     */
    public CFormat fillNull(CFormat defaults) {
        return new CFormat(
            (name != null ? name : defaults.name),
            (size != null ? size : defaults.size),
            (style != null ? style : defaults.style),
            (foreground != null ? foreground : defaults.foreground),
            (background != null ? background : defaults.background),
            (horizontalAlignment != null ? horizontalAlignment : defaults.horizontalAlignment),
            (verticalAlignment != null ? verticalAlignment : defaults.verticalAlignment),
            (representation != null ? representation : defaults.representation),
            (border != null ? border : defaults.border),
            (wrap != null ? wrap : defaults.wrap)
        );
    }

    public Boolean isWrap() {
        return wrap;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final CFormat other = (CFormat) obj;
        if (!Objects.equals(this.name, other.name)) return false;
        if (!Objects.equals(this.size, other.size)) return false;
        if (this.style != other.style) return false;
        if (!Objects.equals(this.foreground, other.foreground)) return false;
        if (!Objects.equals(this.background, other.background)) return false;
        if (this.horizontalAlignment != other.horizontalAlignment) return false;
        if (this.verticalAlignment != other.verticalAlignment) return false;
        if (this.representation != other.representation) return false;
        if (!Objects.equals(this.border, other.border)) return false;
        return Objects.equals(this.wrap, other.wrap);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 59 * hash + (this.size != null ? this.size.hashCode() : 0);
        hash = 59 * hash + (this.style != null ? this.style.hashCode() : 0);
        hash = 59 * hash + (this.foreground != null ? this.foreground.hashCode() : 0);
        hash = 59 * hash + (this.background != null ? this.background.hashCode() : 0);
        hash = 59 * hash + (this.horizontalAlignment != null ? this.horizontalAlignment.hashCode() : 0);
        hash = 59 * hash + (this.verticalAlignment != null ? this.verticalAlignment.hashCode() : 0);
        hash = 59 * hash + (this.representation != null ? this.representation.hashCode() : 0);
        hash = 59 * hash + (this.border != null ? this.border.hashCode() : 0);
        hash = 59 * hash + (this.wrap != null ? this.wrap.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "CFormat{" + "name=" + name + ", size=" + size + ", style=" + style + ", foreground=" + foreground + ", background=" + background + ", horizontalAlignment=" + horizontalAlignment + ", verticalAlignment=" + verticalAlignment + ", representation=" + representation + ", border=" + border + ", wrap=" + wrap + '}';
    }

    public enum FontStyle {

        /**
         * Bold Font Style
         */
        BOLD,
        /**
         * Bold and Italic Font Style
         */
        BOLD_ITALIC,
        /**
         * Italic Font Style
         */
        ITALIC,
        /**
         * Default Font Style
         */
        NORMAL

    }

    public enum HorizontalAlignment {

        /**
         * Align to Center
         */
        CENTER,
        /**
         * Align to Left
         */
        LEFT,
        /**
         * Align to Right
         */
        RIGHT

    }

    public enum VerticalAlignment {

        /**
         *
         */
        BOTTOM,
        /**
         *
         */
        MIDDLE,
        /**
         *
         */
        TOP

    }

    public enum Representation {

        DEFAULT,
        TEXT,
        PERCENT_INTEGER,
        PERCENT_FLOAT,
        SHORT_DATE,
        CURRENCY_EURO
    }
}
