/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ggj2016;

import PrutEngine.AssetManager;
import PrutEngine.Core.Data.Vector3;
import PrutEngine.Core.Math.Matrix4x4;
import PrutEngine.GameObject;
import PrutEngine.Renderer;
import PrutEngine.Scene;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.lwjgl.opengl.ARBImaging.GL_TABLE_TOO_LARGE;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.GL_INVALID_ENUM;
import static org.lwjgl.opengl.GL11.GL_INVALID_OPERATION;
import static org.lwjgl.opengl.GL11.GL_INVALID_VALUE;
import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.GL_OUT_OF_MEMORY;
import static org.lwjgl.opengl.GL11.GL_STACK_OVERFLOW;
import static org.lwjgl.opengl.GL11.GL_STACK_UNDERFLOW;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL30.GL_INVALID_FRAMEBUFFER_OPERATION;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.system.APIUtil.apiUnknownToken;
//import org.lwjgl.opengl.
/**
 *
 * @author heikovanderheijden
 */
public class ExampleScene extends Scene{

    public ExampleScene(){

    }
    
    @Override
    public void awake(){
        System.out.println("dsaf");
        this.addGameObject(new ExampleObject());

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
    
    @Override
    public void update(float tpf) {
        super.update(tpf);
        int error = GL11.glGetError();
        while(error != GL_NO_ERROR){
            System.out.println(getErrorString(error));
        }

    }

    @Override
    public void onQuit() {
   
    }
    
}
