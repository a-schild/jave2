/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.schild.jave;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author a.schild
 */
public class PListener  implements EncoderProgressListener
    {
    private MultimediaInfo _info= null;
    private final List<String> _messages= new LinkedList<>();
    private final List<Integer> _progress= new LinkedList<>();

    @Override
    public void sourceInfo(MultimediaInfo info) {
        _info= info;
    }

    @Override
    public void progress(int permil) {
        _progress.add(permil);
    }

    @Override
    public void message(String message) {
        _messages.add(message);
    }

    /**
     * @return the _info
     */
    public MultimediaInfo getInfo() {
        return _info;
    }

    /**
     * @return the _messages
     */
    public List<String> getMessages() {
        return _messages;
    }

    /**
     * @return the _progress
     */
    public List<Integer> getProgress() {
        return _progress;
    }

}
