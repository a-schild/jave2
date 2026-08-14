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
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
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
 * ffmpeg executable and plug it in JAVE with a custom {@link
 * FFMPEGProcess}
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
        if (!dirFolder.exists()) {
            LOG.debug(
                    "Creating jave temp folder to place executables in <{}>", dirFolder.getAbsolutePath());
            dirFolder.mkdirs();
        } else {
            LOG.debug("Jave temp folder exists in <{}>", dirFolder.getAbsolutePath());
        }

        // -----------------ffmpeg executable export on disk.-----------------------------
        String suffix = isWindows ? ".exe" : (isMac ? "-osx" : "");
        String arch = System.getProperty("os.arch");

        // File
        File ffmpegFile = new File(dirFolder, "ffmpeg-" + arch + "-" + Version.getVersion() + suffix);
        LOG.debug("Executable path: {}", ffmpegFile.getAbsolutePath());

        synchronized (DefaultFFMPEGLocator.class) {
            // Check the version of existing .exe file
            if (ffmpegFile.exists()) {
                // OK, already present
                LOG.debug("Executable exists in <{}>", ffmpegFile.getAbsolutePath());
            } else {
                LOG.debug("Need to copy executable to <{}>", ffmpegFile.getAbsolutePath());
                copyFile("ffmpeg-" + arch + suffix, ffmpegFile);
            }

            // Need a chmod?
            if (!isWindows) {
                makeExecutable(ffmpegFile);
            }
        }

        // Everything seems okay
        path = ffmpegFile.getAbsolutePath();
        if (ffmpegFile.exists()) {
            LOG.debug("ffmpeg executable found: {}", path);
        } else {
            LOG.error("ffmpeg executable NOT found: {}", path);
        }
    }

    @Override
    public String getExecutablePath() {
        return path;
    }

    /**
     * Makes the extracted ffmpeg binary executable, for everyone, and does not return until it
     * really is.
     *
     * <p>This used to hand the job to {@code /bin/chmod} through {@link Runtime#exec(String[])}.
     * That call only starts the process, it does not wait for it, so the caller was free to run
     * ffmpeg while chmod had not finished yet, and the run then died with
     * {@code error=13, Permission denied}. {@link File#setExecutable(boolean, boolean)} does the
     * same thing in process and has already happened once it returns, so there is no window to
     * lose the race in.
     *
     * <p>chmod is still there as a fallback, for the file systems where the java call cannot do
     * it, and this time it is waited for.
     *
     * @param ffmpegFile The extracted executable.
     */
    private void makeExecutable(File ffmpegFile) {
        if (ffmpegFile.setExecutable(true, false) && ffmpegFile.canExecute()) {
            LOG.debug("Executable bit set on <{}>", ffmpegFile.getAbsolutePath());
            return;
        }

        LOG.debug(
                "Could not set the executable bit on <{}> directly, falling back to chmod",
                ffmpegFile.getAbsolutePath());
        try {
            Process chmod =
                    Runtime.getRuntime()
                            .exec(new String[]{"/bin/chmod", "755", ffmpegFile.getAbsolutePath()});
            int exitCode = chmod.waitFor();
            if (exitCode != 0) {
                LOG.error("chmod on <{}> returned {}", ffmpegFile.getAbsolutePath(), exitCode);
            }
        } catch (IOException e) {
            LOG.error("Error setting executable via chmod", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOG.error("Interrupted while waiting for chmod", e);
        }

        if (!ffmpegFile.canExecute()) {
            LOG.error(
                    "ffmpeg is still not executable after chmod: <{}>, running it will fail",
                    ffmpegFile.getAbsolutePath());
        }
    }

    /**
     * Copies a file bundled in the package to the supplied destination.
     *
     * @param path The name of the bundled file.
     * @param dest The destination.
     * @throws RuntimeException If an unexpected error occurs.
     */
    private void copyFile(String path, File dest) {
        String resourceName = "nativebin/" + path;
        try {
            LOG.debug("Copy from resource <{}> to target <{}>", resourceName, dest.getAbsolutePath());
            InputStream is = getClass().getResourceAsStream(resourceName);
            if (is == null) {
                // Use this for Java 9+ only if required
                resourceName = "ws/schild/jave/nativebin/" + path;
                LOG.debug(
                        "Alternative copy from SystemResourceAsStream <{}> to target <{}>",
                        resourceName,
                        dest.getAbsolutePath());
                is = ClassLoader.getSystemResourceAsStream(resourceName);
            }
            if (is == null) {
                // Use this for spring boot with different class loaders
                resourceName = "ws/schild/jave/nativebin/" + path;
                LOG.debug(
                        "Alternative copy from Thread.currentThread().getContextClassLoader() <{}> to target <{}>",
                        resourceName,
                        dest.getAbsolutePath());
                ClassLoader classloader = Thread.currentThread().getContextClassLoader();
                is = classloader.getResourceAsStream(resourceName);
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
            LOG.error(
                    "Could not find ffmpeg executable for {} is the correct platform jar included?",
                    resourceName);
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
    /**
     * Writes the bundled executable to its final place in one step.
     *
     * <p>Copying straight onto the target path is not safe when several jvms start at the same
     * time. The file becomes visible as soon as the copy begins, so the other jvm, which only
     * checks whether the path exists, happily runs a binary that is still half written. The
     * synchronized block around the caller cannot help there, it only covers one jvm.
     *
     * <p>So the content goes to a temporary file next to the target, gets its executable bit while
     * nobody can see it, and is then moved into place atomically. Whoever finds the target path
     * therefore finds a complete and runnable binary.
     *
     * @param source The bundled executable.
     * @param destination The path it has to end up at.
     * @return Whether the executable is in place.
     */
    private boolean copy(InputStream source, String destination) {
        Path target = Paths.get(destination);
        Path temp = null;

        try {
            temp = Files.createTempFile(target.getParent(), target.getFileName().toString(), ".tmp");
            Files.copy(source, temp, StandardCopyOption.REPLACE_EXISTING);
            temp.toFile().setExecutable(true, false);

            try {
                Files.move(
                        temp, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                LOG.debug("Atomic move not available on this file system, moving without it");
                Files.move(temp, target, StandardCopyOption.REPLACE_EXISTING);
            }
            temp = null;
            return true;
        } catch (IOException ex) {
            LOG.error("Cannot write file " + destination, ex);
            return false;
        } finally {
            if (temp != null) {
                try {
                    Files.deleteIfExists(temp);
                } catch (IOException e) {
                    LOG.debug("Could not remove the temporary file <{}>", temp, e);
                }
            }
        }
    }

    @Override
    public ProcessWrapper createExecutor() {
        return new FFMPEGProcess(getExecutablePath());
    }
}
