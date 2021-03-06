/*
 * Copyright (c) 2016, Heiko van der Heijden
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

package nl.hvanderheijden.prutengine.core.data;

import nl.hvanderheijden.prutengine.exceptions.ResourceNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

/**
 * The base class containing basic garbage collecting
 * And reference counting
 * @author Heiko van der Heijden
 */
public abstract class Resource {
    private final static Logger logger = LogManager.getLogger(Resource.class.getName());

    private final int memoryPosition;
    private final String dataLocation;
    private int amountOfRef;

    private Resource(){
        throw new UnsupportedOperationException();
    }

    public Resource(final String fileLocation, final int position){
        this.dataLocation = fileLocation;
        this.amountOfRef = 0;//Starting at one.. with a zero delete the resource
        this.memoryPosition = position;
    }

    public abstract void destroy();
    
    public String getDataLocation(){
	return this.dataLocation;
    }
    
    /**
     * Loads an ascii file
     * @param path
     * @return
     * @throws IOException 
     */
    protected String loadFile(final String path) throws ResourceNotFoundException {

        try(final InputStream str = Resource.class.getResourceAsStream(path)){
            final BufferedReader br = new BufferedReader(new InputStreamReader(str));
            String line;
            String result ="";
            while ((line = br.readLine()) != null) {
                result += line += '\n';
            }
            return result;
        } catch (final IOException ex){
            logger.error(ex);
            throw new ResourceNotFoundException(String.format("File : %s has not been found", path));
        }
    }


    /**
     * Removes an reference
     * And when the reference is null it requests to destroy itself
     * @return is the amount of references zero?
     */
    public boolean removeRef() {
        this.amountOfRef--;
        return amountOfRef == 0;
    }

    /**
     * Add an reference to the resource
     * @return the position it can use this resource
     */
    public int addRef(){
	    this.amountOfRef++;
        return this.memoryPosition;
    }

    /**
     * get it's unique position
     * @return the unique position
     */
    public int getMemoryPosition() {
	return this.memoryPosition;
    }
}