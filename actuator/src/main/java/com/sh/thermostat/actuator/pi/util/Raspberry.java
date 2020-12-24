package com.sh.thermostat.actuator.pi.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Raspberry {

    public static boolean debug = false;

    /**
     * check if this java vm runs on a raspberry PI
     * https://stackoverflow.com/questions/37053271/the-ideal-way-to-detect-a-raspberry-pi-from-java-jar
     *
     * @return true if this is running on a Raspbian Linux
     */
    public static boolean isPi() {
        String osRelease = osRelease();
        return osRelease != null && osRelease.contains("Raspbian");
    }

    /**
     * read the first line from the given file
     *
     * @param file
     * @return the first line
     */
    public static String readFirstLine(File file) {
        String firstLine = null;
        try {
            if (file.canRead()) {
                FileInputStream fis = new FileInputStream(file);
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(fis));
                firstLine = bufferedReader.readLine();
                fis.close();
            }
        } catch (Throwable th) {
            if (debug)
                th.printStackTrace();
        }
        return firstLine;
    }

    /**
     * get the operating System release
     *
     * @return the first line from /etc/os-release or null
     */
    public static String osRelease() {
        String os = System.getProperty("os.name");
        if (os.startsWith("Linux")) {
            final File osRelease = new File("/etc", "os-release");
            return readFirstLine(osRelease);
        }
        return null;
    }

    /**
     * entry point to call from command line
     */
    public static void main(String args[]) {
        if (isPi()) {
            System.out.println(osRelease());
        }
    }
}
