/*
 * Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
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
package com.amazon.ai;

import com.amazon.ai.engine.Engine;
import com.amazon.ai.ndarray.types.DataDesc;
import com.amazon.ai.ndarray.types.DataType;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * The <code>Model</code> interface provides model loading functionality.
 *
 * <p>Users can use this to load the model and apply it for {@link com.amazon.ai.training.Trainer}
 * and {@link com.amazon.ai.inference.Predictor} for Training and Inference jobs.
 */
public interface Model {

    /**
     * Load model from a String, e.g: ./res-152.
     *
     * <p>Please provide the model name/prefix
     *
     * @param modelPath Path to the model, include the model name
     * @return {@link Model} object
     * @throws IOException IO exception happened in loading
     */
    static Model loadModel(String modelPath) throws IOException {
        return loadModel(modelPath, -1);
    }

    /**
     * Load the model from a String with epoch provided, e.g ./res-152 2.
     *
     * <p>It will try to find the model like res-152-0002.param
     *
     * @param modelPath Path to the model, include the model name
     * @param epoch number of epoch of the model
     * @return {@link Model} object
     * @throws IOException IO exception happened in loading
     */
    static Model loadModel(String modelPath, int epoch) throws IOException {
        File file = new File(modelPath);
        String modelName = file.getName();
        return loadModel(file, modelName, epoch);
    }

    /**
     * load the model from the File
     *
     * @param modelPath File object point to a path
     * @return {@link Model} object
     * @throws IOException IO exception happened in loading
     */
    static Model loadModel(File modelPath) throws IOException {
        return loadModel(modelPath, modelPath.getName(), -1);
    }

    /**
     * load the model from the File and the given name
     *
     * @param modelPath Diretory/prefix of the file
     * @param modelName model file name or assigned name
     * @return {@link Model} object
     * @throws IOException IO exception happened in loading
     */
    static Model loadModel(File modelPath, String modelName) throws IOException {
        return loadModel(modelPath, modelName, -1);
    }

    /**
     * Load the model from a File object with name and epoch provided
     *
     * @param modelPath Diretory/prefix of the file
     * @param modelName model file name or assigned name
     * @param epoch number of epoch of the model
     * @return {@link Model} object
     * @throws IOException IO exception happened in loading
     */
    static Model loadModel(File modelPath, String modelName, int epoch) throws IOException {
        return Engine.getInstance().loadModel(modelPath, modelName, epoch);
    }
    /**
     * Get the input descriptor of the model.
     *
     * <p>It contains the information that can be extracted from the model, usually name, shape,
     * layout and DataType.
     *
     * @return Array of {@link DataDesc}
     */
    DataDesc[] describeInput();

    /**
     * Get the output descriptor of the model.
     *
     * <p>It contains the output information that can be obtained from the model
     *
     * @return Array of {@link DataDesc}
     */
    DataDesc[] describeOutput();

    /**
     * Finds a artifact resource with a given name in the model.
     *
     * @param artifactName name of the desired artifact
     * @return A {@link java.net.URL} object or {@code null} if no artifact with this name is found
     * @throws IOException if an error occurs during loading resource
     */
    URL getResource(String artifactName) throws IOException;

    /**
     * Finds a artifact resource with a given name in the model.
     *
     * @param artifactName name of the desired artifact
     * @return A {@link java.io.InputStream} object or {@code null} if no resource with this name is
     *     found
     * @throws IOException if an error occurs during loading resource
     */
    InputStream getResourceAsStream(String artifactName) throws IOException;

    /**
     * Cast the model to support different precision level.
     *
     * <p>For example, you can cast the precision from Float to Int
     *
     * @param dataType the target dataType you would like to cast to
     * @return A model with the down casting parameters
     */
    Model cast(DataType dataType);
}
