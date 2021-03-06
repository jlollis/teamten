/*
 *
 *    Copyright 2016 Lawrence Kesteloot
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

package com.teamten.tools;

import com.teamten.image.ImageUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Turns a photo into an engraved drawing. The photo should be fairly large (around 3000px across).
 */
public class Engraver {
    public static void main(String[] args) throws IOException {
        String inputFilename = args[0];
        String outputFilename = args[1];

        BufferedImage image = ImageUtils.load(inputFilename);

        com.teamten.image.Engraver engraver = new com.teamten.image.Engraver();
        image = engraver.run(image);

        ImageUtils.save(image, outputFilename);
    }
}