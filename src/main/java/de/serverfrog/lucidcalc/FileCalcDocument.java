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

import de.serverfrog.lucidcalc.exception.FileException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * A simple CalcDocument supported by a file, overrides any existing file with the same name
 *
 * @author oliver.guenther
 */
@Slf4j
@SuppressWarnings("unused")
public class FileCalcDocument extends CCalcDocument {

    private File file;

    private final String fileName;

    /**
     * <p>Constructor for FileCalcDocument.</p>
     *
     * @param fileName a {@link java.lang.String} object
     */
    public FileCalcDocument(String fileName) {
        this.fileName = Objects.requireNonNull(fileName, "Filename must not be null");
    }

    /** {@inheritDoc} */
    @Override
    public File getFile() {
        if (file == null) {
            file = new File(fileName);
            if (file.exists()) {
                try {
                    Files.delete(Paths.get(file.toURI()));
                } catch (IOException e) {
                    throw new FileException(e);
                }
            }
        }
        return file;
    }
}
