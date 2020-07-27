/*
 * JAVE - A Java Audio/Video Encoder (based on FFMPEG)
 * 
 * Copyright (C) 2008-2009 Carlo Pelliccia (www.sauronsoftware.it)
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
package ws.schild.jave.process.ffmpeg;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.schild.jave.Version;

import ws.schild.jave.process.ProcessLocator;
import ws.schild.jave.process.ProcessWrapper;

/**
 * The default ffmpeg executable locator, which exports on disk the ffmpeg
 * executable bundled with the library distributions. It should work both for
 * windows and many linux distributions. If it doesn't, try compiling your own
 * ffmpeg executable and plug it in JAVE with a custom {@link FFMPEGProcess}
 *
 * @author Carlo Pelliccia
 */
public class DefaultFFMPEGLocator implements ProcessLocator {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultFFMPEGLocator.class);

    /**
     * The ffmpeg executable file path.
     */
    private final String path;

    /**
     * It builds the default FFMPEGLocator, exporting the ffmpeg executable on a
     * temp file.
     */
    public DefaultFFMPEGLocator() {
        String os = System.getProperty("os.name").toLowerCase();
        boolean isWindows = os.contains("windows");
        boolean isMac = os.contains("mac");
        LOG.debug("Os name is <{}> isWindows: {} isMac: {}", os, isWindows, isMac);

        // Dir Folder
        File dirFolder = new File(System.getProperty("java.io.tmpdir"), "jave/");
        if (!dirFolder.exists())
        {
            LOG.debug("Creating jave temp folder to place executables in <{}>", dirFolder.getAbsolutePath());
            dirFolder.mkdirs();
        }
        else
        {
            LOG.debug("Jave temp folder exists in <{}>", dirFolder.getAbsolutePath());
        }

        // -----------------ffmpeg executable export on disk.-----------------------------
        String suffix = isWindows ? ".exe" : (isMac ? "-osx" : "");
        String arch = System.getProperty("os.arch");

        //File
        File ffmpegFile = new File(dirFolder, "ffmpeg-" + arch +"-"+Version.getVersion()+ suffix);
        LOG.debug("Executable path: {}", ffmpegFile.getAbsolutePath());

        //Check the version of existing .exe file
        if (ffmpegFile.exists())
        {
            // OK, already present
            LOG.debug("Executable exists in <{}>", ffmpegFile.getAbsolutePath());
        }
        else
        {
            LOG.debug("Need to copy executable to <{}>", ffmpegFile.getAbsolutePath());
            copyFile("ffmpeg-" + arch + suffix, ffmpegFile);
        }

        // Need a chmod?
        if (!isWindows)
        {
            try
            {
                Runtime.getRuntime().exec(new String[]
                {
                    "/bin/chmod", "755", ffmpegFile.getAbsolutePath()
                });
            } catch (IOException e)
            {
                LOG.error("Error setting executable via chmod", e);
            }
        }

        // Everything seems okay
        path = ffmpegFile.getAbsolutePath();
        LOG.debug("ffmpeg executable found: {}", path);
    }

    @Override
    public String getExecutablePath() {
        return path;
    }

    /**
     * Copies a file bundled in the package to the supplied destination.
     *
     * @param path The name of the bundled file.
     * @param dest The destination.
     * @throws RuntimeException If an unexpected error occurs.
     */
    private void copyFile(String path, File dest) {
       String resourceName= "nativebin/" + path;
        try {
            LOG.debug("Copy from resource <{}> to target <{}>", resourceName, dest.getAbsolutePath());
            InputStream is= getClass().getResourceAsStream(resourceName);
            if (is == null) {
                // Use this for Java 9+ only if required
                resourceName= "ws/schild/jave/nativebin/" + path;
                LOG.debug("Alternative copy from SystemResourceAsStream <{}> to target <{}>", resourceName, dest.getAbsolutePath());
                is= ClassLoader.getSystemResourceAsStream(resourceName);
            }
            if (is != null) {
                if (copy(is, dest.getAbsolutePath())) {
                    if (dest.exists()) {
                        LOG.debug("Target <{}> exists", dest.getAbsolutePath());
                    } else {
                        LOG.error("Target <{}> does not exist", dest.getAbsolutePath());
                    }
                } else {
                    LOG.error("Copy resource to target <{}> failed", dest.getAbsolutePath());
                }
                try {
                    is.close();
                } catch (IOException ioex) {
                    LOG.warn("Error in closing input stream", ioex);
                }
            } else {
                LOG.error("Could not find ffmpeg platform executable in resources for <{}>", resourceName);
            }
        } catch (NullPointerException ex) {
            LOG.error("Could not find ffmpeg executable for {} is the correct platform jar included?", resourceName);
            throw ex;
        }
    }

    /**
     * Copy a file from source to destination.
     *
     * @param source The name of the bundled file.
     * @param destination the destination
     * @return True if succeeded , False if not
     */
    private boolean copy(InputStream source, String destination) {
        boolean success = true;

        try {
            Files.copy(source, Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            LOG.error("Cannot write file " + destination, ex);
            success = false;
        }

        return success;
    }
    
    @Override
    public ProcessWrapper createExecutor() {
    	return new FFMPEGProcess(getExecutablePath());
    }
}
