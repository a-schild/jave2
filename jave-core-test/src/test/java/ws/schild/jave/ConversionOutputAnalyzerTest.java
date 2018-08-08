/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.schild.jave;

import com.google.common.io.LineReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author a.schild
 */
public class ConversionOutputAnalyzerTest {
    
    public ConversionOutputAnalyzerTest() {
    }

    /**
     * Test of getFile method, of class MultimediaObject.
     */
    @Test
    public void testAnalyzeNewLine1() {
        System.out.println("analyzeNewLine 1");
        File file = new File("src/test/resources/testoutput1.log");
        ConversionOutputAnalyzer oa1= new ConversionOutputAnalyzer(0, null);
        
        try
        {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader streamReader = new InputStreamReader(fis, "UTF-8");
            LineReader reader = new LineReader(streamReader);
            String sLine = null;
            while ((sLine = reader.readLine()) != null)
            {
                oa1.analyzeNewLine(sLine);
            }
            String result= oa1.getLastWarning();
            String expResult= null;
            assertEquals(expResult, result);
        }
        catch (IOException ioError)
        {
            System.out.println("IO error "+ioError.getMessage());
            ioError.printStackTrace();
            throw new AssertionError("IO error "+ioError.getMessage());
        }
        catch (EncoderException enError)
        {
            System.out.println("Encoder error "+enError.getMessage());
            enError.printStackTrace();
            throw new AssertionError("Encoder error "+enError.getMessage());
        }
    }
    
}
