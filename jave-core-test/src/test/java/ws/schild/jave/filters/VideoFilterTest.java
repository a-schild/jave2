/*
 * JAVE - A Java Audio/Video Encoder (based on FFMPEG)
 * 
 * Copyright (C) 2018- Andre Schild
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
package ws.schild.jave.filters;

import ws.schild.jave.*;
import java.io.File;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *
 * @author a.schild
 */
public class VideoFilterTest extends AMediaTest{
    
    public VideoFilterTest() {
        super(null, "VideoFilterTest");
    }

    @Test
    public void testVideoFilter1() throws Exception {
        System.out.println("testVideoFilter1");
        
        File source = new File(getResourceSourcePath(), "testfile3.wmv ");
        File target = new File(getResourceTargetPath(), "testVideoFilter1.mp4");
        if (target.exists())
        {
            target.delete();
        }
        VideoDrawtext vf= new VideoDrawtext("testVideoFilter1", 30, 30, "Arial", null, 30, new Color("ffffff"));
        vf.setShadow(new Color("000000"), 2, 2);
        VideoAttributes videoAttributes= new VideoAttributes();
        videoAttributes.addFilter(vf);
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setVideoAttributes(videoAttributes);
        Encoder encoder = new Encoder();
        encoder.encode(new MultimediaObject(source), target, attrs);
        assertTrue( target.exists(), "Output file missing");
    }

    @Test
    public void testVideoFilter2() throws Exception {
        System.out.println("testVideoFilter2");
        
        File source = new File(getResourceSourcePath(), "testfile3.wmv ");
        File target = new File(getResourceTargetPath(), "testVideoFilter2.mp4");
        if (target.exists())
        {
            target.delete();
        }
        VideoDrawtext vf= new VideoDrawtext("testVideoFilter2", 30, 30, "Arial", null, 30, new Color("ffffff", "44"));
        vf.setShadow(new Color("000000", "44"), 2, 2);
        VideoAttributes videoAttributes= new VideoAttributes();
        videoAttributes.addFilter(vf);
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setVideoAttributes(videoAttributes);
        Encoder encoder = new Encoder();
        encoder.encode(new MultimediaObject(source), target, attrs);
        assertTrue( target.exists(), "Output file missing");
    }

    @Test
    public void testVideoFilter3() throws Exception {
        System.out.println("testVideoFilter3");
        
        File source = new File(getResourceSourcePath(), "testfile3.wmv ");
        File target = new File(getResourceTargetPath(), "testVideoFilter3.mp4");
        if (target.exists())
        {
            target.delete();
        }
        VideoDrawtext vf= new VideoDrawtext("testVideoFilter3 <[]:=,> End of special chars", 30, 30, "Arial", null, 30, new Color("ffffff", "dd"));
        vf.setShadow(new Color("000000", "44"), 2, 2);
        VideoAttributes videoAttributes= new VideoAttributes();
        videoAttributes.addFilter(vf);
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setVideoAttributes(videoAttributes);
        Encoder encoder = new Encoder();
        encoder.encode(new MultimediaObject(source), target, attrs);
        assertTrue( target.exists(), "Output file missing");
    }


    @Test
    public void testVideoFilter4() throws Exception {
        System.out.println("testVideoFilter4");
        
        File source = new File(getResourceSourcePath(), "testfile3.wmv ");
        File target = new File(getResourceTargetPath(), "testVideoFilter4.mp4");
        if (target.exists())
        {
            target.delete();
        }
        VideoDrawtext vf= new VideoDrawtext("testVideoFilter4 center", -1, -1, "Arial", null, 30, new Color("ffffff", "44"));
        vf.setAddArgument("x=(w-text_w)/2:y=(h-text_h)/2");
        vf.setShadow(new Color("000000", "44"), 2, 2);
        VideoAttributes videoAttributes= new VideoAttributes();
        videoAttributes.addFilter(vf);
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setVideoAttributes(videoAttributes);
        Encoder encoder = new Encoder();
        encoder.encode(new MultimediaObject(source), target, attrs);
        assertTrue( target.exists(), "Output file missing");
    }
}
