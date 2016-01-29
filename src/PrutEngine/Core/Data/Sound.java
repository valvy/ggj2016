/*
 * Copyright (c) 2016, quget
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package PrutEngine.Core.Data;
import PrutEngine.Debug;
import java.io.BufferedInputStream;
import javax.sound.sampled.AudioInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
/**
 *
 * @author quget
 */
public class Sound extends Resource
{
    public int soundID;
    private AudioInputStream audioInputStream = null;
    private BufferedInputStream bufferedInputStream;
    InputStream inputStream ;
    public AudioFormat audioFormat;
    public byte[] data;
    public int buffSize;
    private Clip clip;
    public Sound(String fileLocation,int position)
    {   
        super(fileLocation,position);
        try
        {
            File scrFile = new File(fileLocation);
            inputStream = new FileInputStream(scrFile);
            audioInputStream = AudioSystem.getAudioInputStream(scrFile);
           // bufferedInputStream = new BufferedInputStream(audioInputStream);
            buffSize = audioInputStream.available() - 1;
            audioFormat = audioInputStream.getFormat();
            data = new byte[(int)audioInputStream.getFrameLength() * audioFormat.getFrameSize()];
            byte[] buf = new byte[buffSize];
            for (int i=0; i<data.length; i+=buffSize) 
            {
                int r = audioInputStream.read(buf, 0, buffSize);
                if (i+r >= data.length) 
                {
                    r = i + r - data.length;
                }
                System.arraycopy(buf, 0, data, i, r);
            }
            audioInputStream.close();
          // bufferedInputStream.close();
           // audioInputStream = new AudioInputStream(bufferedInputStream, audioInputStream.getFormat(), audioInputStream.getFrameLength());
        }
        catch(Exception e)
        {
             System.err.println(e.getMessage());
        }
    }
    public void PlaySound()
    {
        try
        {
            clip = AudioSystem.getClip();
            clip.open(audioFormat,data, 0, buffSize - 20000);
            clip.start();
        }
        catch(Exception e)
        {
            System.err.println(e.getMessage());
        }
    }
    public int getSound()
    {
        return this.soundID;
    }
    @Override
    public void destroy() 
    {
        
    }
}
