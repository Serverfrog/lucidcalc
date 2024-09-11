package de.serverfrog.lucidcalc.apachepoi;

import de.serverfrog.lucidcalc.LucidCalcReader;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * <p>ApachePoiReader class.</p>
 *
 * @author Bastian Venz
 */
public class ApachePoiReader implements LucidCalcReader {
    /** {@inheritDoc} */
    @Override
    public LucidCalcReader addColumn(final int id, final Class<?> type) {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public List<String> getErrors() {
        return List.of();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isError() {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isHeadline() {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void setHeadline(final boolean headline) {
    //TODO needs to be implemented
    }

    /** {@inheritDoc} */
    @Override
    public boolean isTrim() {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void setTrim(final boolean trim) {
        //TODO needs to be implemented
    }

    /** {@inheritDoc} */
    @Override
    public <U> List<U> read(final File file, final Class<U> clazz) {
        return List.of();
    }

    /** {@inheritDoc} */
    @Override
    public <U> List<U> read(final InputStream is, final Class<U> clazz) {
        return List.of();
    }

    /** {@inheritDoc} */
    @Override
    public <U> List<U> read(final File file, final U instance) {
        return List.of();
    }

    /** {@inheritDoc} */
    @Override
    public List<List<?>> read(final File file) {
        return List.of();
    }

    /** {@inheritDoc} */
    @Override
    public void setColumns(final int... columns) {
        //TODO needs to be implemented
    }
}
