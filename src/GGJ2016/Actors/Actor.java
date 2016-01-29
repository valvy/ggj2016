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
package GGJ2016.Actors;

import PrutEngine.Core.Math.Vector3;
import PrutEngine.GameObject;
import PrutEngine.Renderer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author quget
 */
public class Actor extends GameObject
{
    protected enum Element{
        Sphere,
        Cube,
        Torus,
    }
    
    protected Element currentElement;
    
    protected final float speed = 50;
    
    public Actor(Vector3<Float> startPos)
    {
        this.currentElement = Element.Sphere;
        this.setPosition(startPos);
    }
    
    protected void setupElement(Element element){
        this.currentElement = element;
        switch(this.currentElement){
            case Sphere:
            this.initRenderer("sphere.obj");
            return;
            case Cube:
            this.initRenderer("cube.obj");
            return;
        }
    }
    protected void initRenderer(String mesh){
        try {
            this.setRenderer(new Renderer(
                    "Assets/Shaders/UnShadedVertex.glsl",
                    "Assets/Shaders/UnshadedFragment.glsl",
                    "Assets/Textures/cube.bmp",
                    "Assets/Meshes/" + mesh     
            ));
        } catch (Exception ex) {
            Logger.getLogger(Actor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void update(float tpf) 
    {
        
    }
}