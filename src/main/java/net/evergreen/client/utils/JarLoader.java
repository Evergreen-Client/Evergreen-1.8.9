/*
 * Copyright [2020] [Evergreen]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.evergreen.client.utils;

import net.minecraft.crash.CrashReport;
import net.minecraft.util.ReportedException;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;

public class JarLoader {

    /**
     * Loads a certain class from a jar
     *
     * @author isXander
     * @param file File location of the jar
     * @param pathToClass what class we want to load (e.g. co.uk.isxander.SomeClass)
     * @return instance of class
     */
    public static Object addToClasspath(File file, String pathToClass) {
        //https://stackoverflow.com/a/60775/12974941
        try {
            URLClassLoader child = new URLClassLoader(
                    new URL[]{ file.toURI().toURL() },
                    JarLoader.class.getClassLoader()
            );
            Class<?> classToLoad = Class.forName(pathToClass, true, child);
            return classToLoad.newInstance();
        }
        catch (ClassNotFoundException e) {
            throw new ReportedException(new CrashReport("Tried to load non-existant class: " + pathToClass
                    + "\nThis is probably because you didn't specify a valid java class in methods.txt", e));
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to load jar");
        }
    }

    public static void addToClasspath(File file) {
        // https://stackoverflow.com/a/60775/12974941
        try {
            URLClassLoader child = new URLClassLoader(
                    new URL[]{ file.toURI().toURL() },
                    JarLoader.class.getClassLoader()
            );
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets input stream from resource
     *
     * @param file file to jar
     * @param pathToResource resource path within jar
     * @return resource input stream
     */
    public static InputStream getResourceStream(File file, String pathToResource) {
        try {
            URLClassLoader child = new URLClassLoader(
                    new URL[]{ file.toURI().toURL() },
                    JarLoader.class.getClassLoader()
            );
            return child.getResourceAsStream(pathToResource);
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to load jar");
        }
    }

}
