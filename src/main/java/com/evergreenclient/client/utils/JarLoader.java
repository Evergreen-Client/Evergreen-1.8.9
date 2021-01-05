/*
 * Copyright (C) Evergreen [2020 - 2021]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under the certain conditions that can be found here
 * https://www.gnu.org/licenses/lgpl-3.0.en.html
 */

package com.evergreenclient.client.utils;

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
