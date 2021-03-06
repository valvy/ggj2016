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
package nl.hvanderheijden.prutengine;

import nl.hvanderheijden.prutengine.core.data.Shader;
import nl.hvanderheijden.prutengine.core.math.Vector3;
import nl.hvanderheijden.prutengine.core.math.Matrix4x4;
import nl.hvanderheijden.prutengine.core.math.Quaternion;
import nl.hvanderheijden.prutengine.exceptions.PrutEngineException;
import nl.hvanderheijden.prutengine.exceptions.ResourceNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;


import static org.lwjgl.opengl.ARBInternalformatQuery2.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;


/**
 * Manages the rendering of an gameobject.
 * @author Heiko van der Heijden
 */
public class Renderer {
    private final static Logger logger = LogManager.getLogger(Renderer.class.getName());
    /**
     * An reference to the opengl program
     */
    private final int program;
    /**
     * the reference to the 3d mesh
     */
    private final int mesh;
    /**
     * the reference to it's texture
     */
    private final int texture;
    
    private final Quaternion lastRot;
    private final Vector3<Float> lastSize;
    private final Vector3<Float> lastPosition;
    private final Matrix4x4 lastMat;



    /**
     * the reference to the mv_matrix shader 
     */
    private final int glPos;

    private Renderer(){
        throw new UnsupportedOperationException();
    }
    
    public Renderer(final String vShader,final String fShader,final String texture, final String meshName) throws PrutEngineException {

        final HashMap<String, Shader.Type> dat = new HashMap<>();
        dat.put(vShader, Shader.Type.Vertex_Shader);
        dat.put(fShader, Shader.Type.Fragment_Shader);
        this.program = AssetManager.loadProgram(dat);

        if(texture.equals("")){
            this.texture = -1;
        }else{
            this.texture = AssetManager.loadTexture(texture);
        }
        this.lastRot = new Quaternion();
        this.lastPosition = new Vector3<>(0f,0f,0f);
        this.lastSize = new Vector3<>(0f,0f,0f);
        this.lastMat = Matrix4x4.identityMatrix();
        this.mesh = AssetManager.loadMesh(meshName);
        this.glPos = glGetUniformLocation(AssetManager.getProgram(this.program), "mv_matrix");
        
    }

    public final int getProgram(){
        return this.program;
    }
    
    /**
     * Renders the various attributes on screen
     * @param size
     * @param position
     * @param rotation
     */
    public void render(final Vector3<Float> size, final Vector3<Float> position, final Quaternion rotation){

        try {
            boolean recalculate = false;
            if(!size.equals(lastSize)){
                this.lastSize.set(size);
                recalculate = true;
            }
            if(!this.lastPosition.equals(position)){
                this.lastPosition.set(position);
                recalculate = true;
            }
            if(!this.lastRot.equals(rotation)){
                this.lastRot.set(rotation);
                recalculate = true;
            }
            //Only do the calculations when it has been changed
            if(recalculate){
                Matrix4x4 mat = Matrix4x4.identityMatrix();
                mat = Matrix4x4.scale(mat, size); 
                Matrix4x4 pos = Matrix4x4.identityMatrix();
                pos.translate(position);
                mat = Matrix4x4.multiply(pos,mat);
                this.lastMat.set(Matrix4x4.multiply(mat, Quaternion.quaternionToMatrix(rotation)));
            }
            
            glUseProgram(AssetManager.getProgram(this.program));
            glBindVertexArray(AssetManager.getMeshVao(this.mesh));
           
            glEnableVertexAttribArray(0);
           
            glUniformMatrix4fv(this.glPos, true, this.lastMat.getRawData());
            if(this.texture != -1){
                glBindTexture(GL_TEXTURE_2D, AssetManager.getTexture(this.texture));
            }
            
            glDrawArrays(GL_TRIANGLES, 0,AssetManager.getMeshSize(this.mesh));
            glDisableVertexAttribArray(0);
            glBindVertexArray(0);
        } catch (ResourceNotFoundException ex) {
            logger.error(ex);
        }
    }
    
    /**
     * clean the various assets it used
     */
    public void destroy(){
        AssetManager.removeMesh(this.mesh);
        AssetManager.removeProgram(this.program);
        AssetManager.removeTexture(this.texture);
    }
}