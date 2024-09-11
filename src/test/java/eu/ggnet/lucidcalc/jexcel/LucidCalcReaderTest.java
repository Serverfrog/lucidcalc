/*
 * Copyright (C) 2014 GG-Net GmbH
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package eu.ggnet.lucidcalc.jexcel;

import java.io.File;
import java.util.*;

import eu.ggnet.lucidcalc.LucidCalc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import eu.ggnet.lucidcalc.LucidCalcReader;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class LucidCalcReaderTest {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SampleBean {

        private String f1;

        private Double f2;

        private Integer f3;

        private Double f4;
    }

    public static final File TEST_DATA = new File("target/test-classes/JExcelReaderTestData.xls");

    /**
     * Test of read method, of class JExcelLucidCalcReader.
     */
    @ParameterizedTest
    @EnumSource(LucidCalc.Backend.class)
    void testReadFile() {
        LucidCalc.Backend backend = LucidCalc.Backend.JEXCEL;
        LucidCalcReader reader = LucidCalc.createReader(backend);
        reader.addColumn(0, String.class).addColumn(1, Double.class).addColumn(2, Integer.class).addColumn(3, Double.class);
        List<List<? extends Object>> expResult = new ArrayList<>();
        expResult.add(Arrays.asList("AAA", 1.3, 1, 10.0));
        expResult.add(Arrays.asList("BBB", 1.4, 2, 1.5));
        expResult.add(Arrays.asList("CCC", 1.5, 3, 2.49));
        expResult.add(Arrays.asList("DDD", 3.55, 4, 55.33));
        List result = reader.read(TEST_DATA);
        assertEquals(expResult, result);
        assertFalse(reader.isError());
        assertEquals(0, reader.getErrors().size());
    }

    /**
     * Test of read method, of class JExcelLucidCalcReader.
     */
    @ParameterizedTest
    @EnumSource(LucidCalc.Backend.class)
    void testReadFileGenericType(LucidCalc.Backend backend) {
        LucidCalcReader reader = LucidCalc.createReader(backend);
        reader.addColumn(0, String.class).addColumn(1, Double.class).addColumn(2, Integer.class).addColumn(3, Double.class);
        List<SampleBean> expResult = new ArrayList<>();
        expResult.add(new SampleBean("AAA", 1.3, 1, 10.0));
        expResult.add(new SampleBean("BBB", 1.4, 2, 1.5));
        expResult.add(new SampleBean("CCC", 1.5, 3, 2.49));
        expResult.add(new SampleBean("DDD", 3.55, 4, 55.33));
        List<SampleBean> result = reader.read(TEST_DATA, new SampleBean());
        assertEquals(expResult, result);
        assertFalse(reader.isError());
        assertEquals(0, reader.getErrors().size());
    }
}
