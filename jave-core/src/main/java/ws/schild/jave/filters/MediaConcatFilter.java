/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.schild.jave.filters;

import ws.schild.jave.filters.Filter;

/**
 *
 * @author a.schild
 */
public class MediaConcatFilter extends Filter {

    /**
     * Concat all input files and map first video and audio stream
     * Fails when a media has no video or audio stream
     * 
     * @param nSources number of input sources
     */
    public MediaConcatFilter(int nSources) {
        super("concat");
        initFilter(nSources, true, true);
    }

    /**
     * Concat all input files and map first video and audio stream
     * Fails when a media has no video or audio stream
     * 
     * @param nSources number of input sources
     * @param concatVideo Should we output the video stream
     * @param concatAudio Should we output the audio stream
     */
    public MediaConcatFilter(int nSources, boolean concatVideo, boolean concatAudio) {
        super("concat");
        initFilter(nSources, concatVideo, concatAudio);
    }
    
    protected void initFilter(int nSources, boolean concatVideo, boolean concatAudio)
    {
        String destinationDescription= "";
        if (concatVideo && concatAudio)
        {
            destinationDescription=  "v=1:a=1";
        }
        else if (concatVideo)
        {
            destinationDescription=  "v=1:a=0";
        }
        else if (concatAudio)
        {
            destinationDescription=  "v=0:a=1";
        }
        initFilter(nSources, concatVideo, concatAudio, destinationDescription);
    }
    
    protected void initFilter(int nSources, 
            boolean concatVideo, 
            boolean concatAudio, 
            String destinationDescription)
    {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i < nSources; i++)
        {
            if (i > 0)
            {
                sb.append("] [");
            }
            sb.append(Integer.toString(i));
            if (concatVideo)
            {
                sb.append(":v:0");
                if (concatAudio)
                {
                    sb.append("] [").append(Integer.toString(i)).append(":a:0");
                }
            }
            else if (concatAudio)
            {
                sb.append(":a:0");
            }
        }
        addInputLabel(sb.toString());
        addNamedArgument("n", Integer.toString(nSources) + ":" + destinationDescription);
    }
}
