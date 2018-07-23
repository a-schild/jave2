/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.schild.jave;

import java.io.File;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

/**
 *
 * @author a.schild
 */
public class MultimediaObjectTest {
    
    public MultimediaObjectTest() {
    }

    /**
     * Test of getFile method, of class MultimediaObject.
     */
    @Test
    public void testGetFile() {
        System.out.println("getFile");
        File file = new File("src/test/resources/dance1.avi");
        MultimediaObject instance = new MultimediaObject(file);
        File expResult = file;
        File result = instance.getFile();
        assertEquals(expResult, result);
    }

    /**
     * Test of getInfo method, of class MultimediaObject.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetInfo() throws Exception {
        System.out.println("getInfo");
        File file = new File("src/test/resources/dance1.avi");
        MultimediaObject instance = new MultimediaObject(file);
        MultimediaInfo result = instance.getInfo();
        
        assertEquals("avi", result.getFormat());
        assertEquals(1530, result.getDuration());
        assertNull(result.getAudio());
        assertEquals("rawvideo", result.getVideo().getDecoder());
        assertEquals(320, result.getVideo().getSize().getWidth());
        assertEquals(240, result.getVideo().getSize().getHeight());
        assertEquals(4817, result.getVideo().getBitRate());
        assertEquals(15.0f, result.getVideo().getFrameRate(), 0);
    }
    
}
