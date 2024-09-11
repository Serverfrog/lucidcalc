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

import de.serverfrog.lucidcalc.exception.ReflectionUsageException;
import lombok.AccessLevel;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Util class that contains some useful Action implementations.
 *
 * @author oliver.guenther
 */
@SuppressWarnings("unused")
public class SUtil {

    private SUtil() {
        throw new IllegalAccessError("Utility class");
    }

    @Getter(AccessLevel.PUBLIC)
    private static final SAction<?> SELF_ROW = new SAction<>() {

        @Override
        public Object getValue(int relativeColumnIndex, int relativeRowIndex, int absoluteColumnIndex, int absoluteRowIndex, Object lineModel) {
            if (!(lineModel instanceof Object[])) {
                return "LineModel not of type Object[]";
            }
            Object o = ((Object[]) lineModel)[relativeColumnIndex];
            if (!(o instanceof SFormula)) {
                return "o.getClass != SFormula, but " + o;
            }
            for (Object elem : ((SFormula) o).getElements()) {
                if (elem instanceof SSelfRowReference sselfrowreference) {
                    sselfrowreference.setRowIndex(absoluteRowIndex);
                }
            }
            return o;
        }

        @Override
        public CFormat getFormat(int relativeColumnIndex, int relativeRowIndex, int absoluteColumnIndex, int absoluteRowIndex, Object lineModel) {
            return null;
        }
    };

    @Getter(AccessLevel.PUBLIC)
    private static final SAction<?> NULL = new SAction<>() {

        @Override
        public Object getValue(int relativeColumnIndex, int relativeRowIndex, int absoluteColumnIndex, int absoluteRowIndex, Object lineModel) {
            return null;
        }

        @Override
        public CFormat getFormat(int relativeColumnIndex, int relativeRowIndex, int absoluteColumnIndex, int absoluteRowIndex, Object lineModel) {
            return null;
        }
    };

    /**
     * <p>getBeanProperty.</p>
     *
     * @param name a {@link java.lang.String} object
     * @return a {@link de.serverfrog.lucidcalc.SAction} object
     */
    public static SAction<?> getBeanProperty(String name) {
        return new BeanProperty<>(name);
    }


    /**
     * <p>getConstant.</p>
     *
     * @param constant a {@link java.lang.Object} object
     * @return a {@link de.serverfrog.lucidcalc.SAction} object
     */
    public static SAction<?> getConstant(final Object constant) {
        return new SAction<>() {

            @Override
            public Object getValue(int relativeColumnIndex, int relativeRowIndex, int absoluteColumnIndex, int absoluteRowIndex, Object lineModel) {
                return constant;
            }

            @Override
            public CFormat getFormat(int relativeColumnIndex, int relativeRowIndex, int absoluteColumnIndex, int absoluteRowIndex, Object lineModel) {
                return null;
            }
        };
    }

    /**
     * Creates new {@link de.serverfrog.lucidcalc.SSelfRowReference}
     *
     * @param column the column index
     * @return new {@link de.serverfrog.lucidcalc.SSelfRowReference}
     */
    public static SSelfRowReference selfRowReference(int column) {
        return new SSelfRowReference(column);
    }

    private static class BeanProperty<T> implements SAction<T> {

        private final String propertyName;

        public BeanProperty(String property) {
            this.propertyName = property;
        }

        @Override
        public Object getValue(int relativeColumnIndex, int relativeRowIndex, int absoluteColumnIndex, int absoluteRowIndex, T lineModel) {
            return chainedValue(propertyName, lineModel);
        }

        private Object chainedValue(String propertyChain, Object main) {
            if (!propertyChain.contains(".")) {
                return invoke(propertyChain, main);
            }
            int dot = propertyChain.indexOf(".");
            String property = propertyChain.substring(0, dot);
            return chainedValue(propertyChain.substring(dot + 1), invoke(property, main));
        }

        @SuppressWarnings({"java:S3011","java:S1141","java:S1141"})
        private Object invoke(String property, Object main) {
            Method m;
            try {
                try {
                    m = main.getClass().getMethod(
                        "get"
                            + property.substring(0, 1).toUpperCase()
                            + property.substring(1));
                } catch (NoSuchMethodException ex) {
                    try {
                        // Trying "is" in the case of booleans
                        m = main.getClass().getMethod(
                            "is"
                                + property.substring(0, 1).toUpperCase()
                                + property.substring(1));
                    } catch (Exception ex1) {
                        throw new ReflectionUsageException("Exception during invoke().getMethod()", ex1);
                    }
                }
                m.setAccessible(true);
                return m.invoke(main);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException ex) {
                throw new ReflectionUsageException("Exception during invoke()", ex);
            }
        }

        @SuppressWarnings("unused")
        @Override
        public CFormat getFormat(int relativeColumnIndex, int relativeRowIndex, int absoluteColumnIndex, int absoluteRowIndex, T lineModel) {
            return null;
        }
    }
}
