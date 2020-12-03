/*
 * Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package ai.djl.paddlepaddle.jna;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.Shape;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

public class JnaUtilsTest {

    @Test
    public void testNDArrayCreation() {
        if (!System.getProperty("os.name").startsWith("Mac")) {
            throw new SkipException("Only macOS is supported.");
        }
        try (NDManager manager = NDManager.newBaseManager(null, "PaddlePaddle")) {
            NDArray array = manager.zeros(new Shape(1, 2));
            float[] expected = new float[] {0, 0};
            Assert.assertEquals(array.toFloatArray(), expected);
            array = manager.ones(new Shape(1, 2));
            expected = new float[] {1, 1};
            Assert.assertEquals(array.toFloatArray(), expected);
            expected = new float[] {1, 3, 2};
            array = manager.create(expected);
            Assert.assertEquals(array.toFloatArray(), expected);
        }
    }
}
