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
package nl.hvanderheijden.prutengine.core;


import nl.hvanderheijden.prutengine.Debug;
import nl.hvanderheijden.prutengine.GameObject;
import static java.lang.System.exit;
import java.util.ArrayList;
import static org.lwjgl.opengl.ARBImaging.GL_TABLE_TOO_LARGE;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.GL_INVALID_FRAMEBUFFER_OPERATION;
import static org.lwjgl.glfw.GLFW.glfwSetWindowTitle;
import static org.lwjgl.system.APIUtil.apiUnknownToken;

/**
 * the window of the app, manages the rendering.
 * @author Heiko van der Heijden
 */
public final class View {
    private long WINDOW;

    private View(){
        throw new UnsupportedOperationException();
    }

    /**
     * initializes the window 
     * @param window 
     */
    public View(long window){
        this.WINDOW = window;
        GL.createCapabilities();
        glEnable(GL_CULL_FACE);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDepthFunc(GL_LEQUAL);
        glFrontFace(GL_CW);
    }
    
    /**
     * sets the title of the window
     * @param title 
     */
    public void setWindowTitle(String title){
        glfwSetWindowTitle(WINDOW, title);
    }
    
    public static String getErrorString(int errorCode) {
	switch ( errorCode ) {
            case GL_NO_ERROR:
                return "No error";
            case GL_INVALID_ENUM:
                return "Enum argument out of range";
            case GL_INVALID_VALUE:
		return "Numeric argument out of range";
            case GL_INVALID_OPERATION:
                return "Operation illegal in current state";
            case GL_STACK_OVERFLOW:
		return "Command would cause a stack overflow";
            case GL_STACK_UNDERFLOW:
                return "Command would cause a stack underflow";
            case GL_OUT_OF_MEMORY:
		return "Not enough memory left to execute command";
            case GL_INVALID_FRAMEBUFFER_OPERATION:
		return "Framebuffer object is not complete";
            case GL_TABLE_TOO_LARGE:
		return "The specified table is too large";
            default:
		return apiUnknownToken(errorCode);
	}     
    }
    
    /**
     * Draws the various gameobjects
     * @param obj 
     */
    public void draw(ArrayList<GameObject> obj){
        glClearColor(0.0f, 0f, 0f, 0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        obj.stream().forEach(GameObject::draw);
        
        int error = glGetError();
        if(error != GL_NO_ERROR){
            while(error != GL_NO_ERROR){
                Debug.log(getErrorString(error));
                error = glGetError();
            }

            exit(-1);
        }
    }
    
    public void destroy(){}
}