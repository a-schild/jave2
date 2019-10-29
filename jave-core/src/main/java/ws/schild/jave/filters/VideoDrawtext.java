/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.schild.jave.filters;

import java.io.File;
import ws.schild.jave.Color;
import ws.schild.jave.Utils;
import ws.schild.jave.VideoFilter;

/**
 * Add a text watermark in the video file
 * 
 * https://write.corbpie.com/how-to-do-a-text-watermark-in-ffmpeg/
 * -vf "drawtext=text='a watermark':x=10:y=H-th-10:fontfile=/pathto/font.ttf:fontsize=10:fontcolor=white:shadowcolor=black:shadowx=2:shadowy=2"
 * 
 * 
 * @author andre
 */
public class VideoDrawtext extends VideoFilter {

    /**
     * 
     * @param watermarkText Text to be used as watermark
     * @param posX  X Position of watermark text (From the left)
     * @param posY  Y Position of watermark text (From the top)
     * @param fontName Use this font (Can be null, but then we need a fontFile)
     * @param fontFile Truetype font file (Only required when fontName is NULL)
     * @param fontSize Font size
     * @param fontColor Color of font
     * @param shadowColor Color of shadow
     * @param shadowX X Position of shadow, relative to text
     * @param shadowY Y Position of shadow, relative to text
     */
    public VideoDrawtext(
            String watermarkText,
            int posX,
            int posY,
            String fontName,
            File fontFile,
            float fontSize,
            Color fontColor,
            Color shadowColor,
            int shadowX,
            int shadowY
            ) throws IllegalArgumentException
    {
        StringBuilder sb= new StringBuilder();
        sb.append("drawtext=text='");
        sb.append(Utils.escapeArgument(watermarkText));
        sb.append("':x=");
        sb.append(Integer.toString(posX));
        sb.append("':y=");
        sb.append(Integer.toString(posY));
        if (fontName != null)
        {
            sb.append(":font=");
            sb.append(fontName);
        }
        else if (fontFile != null)
        {
            sb.append(":fontfile=");
            sb.append(fontFile.getAbsoluteFile());
        }
        else
        {
            throw new IllegalArgumentException("Need either fontName or fontFile");
        }
        sb.append(":fontsize=");
        sb.append(Float.toString(fontSize));
        sb.append(":fontcolor=");
        sb.append(fontColor.getFfmpegColor());
        if (shadowColor != null)
        {
            sb.append(":shadowcolor");
            sb.append(shadowColor.getFfmpegColor());
            sb.append(":shadowx=");
            sb.append(Integer.toString(shadowX));
            sb.append(":shadowy=");
            sb.append(Integer.toString(shadowY));
        }
        
        setExpression(sb.toString());
    }
    
}
