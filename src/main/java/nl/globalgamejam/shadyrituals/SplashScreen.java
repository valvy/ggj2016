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
package nl.globalgamejam.shadyrituals;

import nl.globalgamejam.shadyrituals.actors.SplashBackground;
import nl.hvanderheijden.prutengine.Application;
import nl.hvanderheijden.prutengine.AssetManager;
import nl.hvanderheijden.prutengine.Scene;
import nl.hvanderheijden.prutengine.exceptions.PrutEngineException;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

/**
 * Shows the splashscreen
 * @author Wander & eddy
 */
public class SplashScreen extends Scene{
    @Override
    public void awake() {
        this.addGameObject(new SplashBackground());
    }
    
    @Override
    public void update(float tpf) throws PrutEngineException {
        super.update(tpf);
        if(Application.getInstance().getKeyboardKey(GLFW_KEY_SPACE) == GLFW_PRESS
         ||Application.getInstance().getKeyboardKey(GLFW_KEY_ENTER) == GLFW_PRESS){
            AssetManager.clearProgramsBuffer();
            AssetManager.clearShaderBuffer();
            Application.getInstance().loadScene(new MenuScene());
        }
    }
}