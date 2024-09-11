package de.serverfrog.lucidcalc.apachepoi;

import de.serverfrog.lucidcalc.LucidCalcReader;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public class ApachePoiReader implements LucidCalcReader {
    @Override
    public LucidCalcReader addColumn(final int id, final Class<?> type) {
        return null;
    }

    @Override
    public List<String> getErrors() {
        return List.of();
    }

    @Override
    public boolean isError() {
        return false;
    }

    @Override
    public boolean isHeadline() {
        return false;
    }

    @Override
    public void setHeadline(final boolean headline) {

    }

    @Override
    public boolean isTrim() {
        return false;
    }

    @Override
    public void setTrim(final boolean trim) {

    }

    @Override
    public <U> List<U> read(final File file, final Class<U> clazz) {
        return List.of();
    }

    @Override
    public <U> List<U> read(final InputStream is, final Class<U> clazz) {
        return List.of();
    }

    @Override
    public <U> List<U> read(final File file, final U instance) {
        return List.of();
    }

    @Override
    public List<List<? extends Object>> read(final File file) {
        return List.of();
    }

    @Override
    public void setColumns(final int... columns) {

    }
}
