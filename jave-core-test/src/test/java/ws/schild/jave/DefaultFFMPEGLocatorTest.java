/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.schild.jave;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author a.schild
 */
public class DefaultFFMPEGLocatorTest {
    
    public DefaultFFMPEGLocatorTest() {
    }

    @Test
    public void testFindExecutable() {
        DefaultFFMPEGLocator locator= new  DefaultFFMPEGLocator();
        String exePath= locator.getFFMPEGExecutablePath();
        assertNotNull("Native component not found", exePath);
    }
    
}
