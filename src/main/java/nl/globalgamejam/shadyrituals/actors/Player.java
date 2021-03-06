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
package nl.globalgamejam.shadyrituals.actors;

import nl.globalgamejam.shadyrituals.GameScene;
import nl.globalgamejam.shadyrituals.BaseConnection;
import nl.globalgamejam.shadyrituals.BaseConnection.ConnectedPlayer;
import nl.hvanderheijden.prutengine.Application;
import nl.hvanderheijden.prutengine.SettingsManager;
import nl.hvanderheijden.prutengine.core.math.PrutMath;
import static org.lwjgl.glfw.GLFW.*;

import nl.hvanderheijden.prutengine.core.math.Vector2;
import nl.hvanderheijden.prutengine.core.math.Vector3;
import nl.hvanderheijden.prutengine.exceptions.PrutEngineException;

import java.util.ArrayList;
import java.util.List;

/**
 * The player
 * @author Wander
 */
public class Player extends Actor
{
    private final GameScene gameScene;
    private final List<ScoreCube> scoreCubes;
    private final float scoreCubeXStep = 1.5f;
    private float scoreCubeX;
    private final Vector3<Float> lastPos;
    private Element lastElement;



    public Player(GameScene gameScene)
    {
        super(new Vector3<>(PrutMath.random(-100, 100),PrutMath.random(-100, 100), -10f));
        this.scoreCubes = new ArrayList<>();
        this.lastPos = new Vector3<>(0f, 0f, 0f);
        this.setSize(new Vector3<>(2f, 2f, 2f));
        this.gameScene = gameScene;
        float changeTimer = (float) Math.random() * 10f + 1f;
    }
    
    public void AddScore()
    {
        ScoreCube scoreCube = new ScoreCube(new Vector3<>(scoreCubeX, 0f, 0f));
        scoreCubes.add(scoreCube);
        gameScene.addGameObjectRealTime(scoreCube);
        scoreCubeX += scoreCubeXStep;
    }
    
    public void ResetScore()
    {
        for (final ScoreCube scoreCube : scoreCubes) {
            gameScene.destroy(scoreCube);
        }
        scoreCubes.removeAll(scoreCubes);
        scoreCubeX = 0;
    }
    
    @Override
    public void update(float tpf) throws PrutEngineException {
        final Vector2<Integer> worldSize = SettingsManager.getInstance().getWorld_size();
        final Vector3<Float> nPos = new Vector3<>(this.getPosition());
        if(this.getPosition().x > worldSize.x){nPos.x = (float)worldSize.x;
        }
        if(this.getPosition().x < -worldSize.x){
            nPos.x = -(float)worldSize.x;
        }

        if(this.getPosition().y > worldSize.y){
            nPos.y = (float)worldSize.y;
        }
        if(this.getPosition().y < -worldSize.y){
            nPos.y = -(float)worldSize.y;
        }

        this.setPosition(nPos);
        int lineCountY = 0;
        int lineCountX = 1;
        for(int i = 0; i < scoreCubes.size(); i++)
        {
            Vector3<Float> cubePosition = new Vector3<>(0f, 0f, 0f);
            if(i % 17 == 0)
            {
                lineCountY ++;
                lineCountX = 0;
            }
            cubePosition.x = (-gameScene.getCamera().getPosition().x - 12f) + (lineCountX * scoreCubeXStep);
            cubePosition.y = -gameScene.getCamera().getPosition().y + 14 - ( scoreCubeXStep * lineCountY);// * lineCount);
            lineCountX ++;
            scoreCubes.get(i).setPosition(cubePosition);
        }

        PlayerInput(tpf);
        super.update(tpf);
        
        if(!lastPos.equals(this.getPosition()) || lastElement != this.currentElement)
        {
            BaseConnection.getInstance().notifyWorld(
                new ConnectedPlayer(
                    BaseConnection.getInstance().getIdName(),
                    this.getPosition(),
                    this.currentElement));
            lastPos.set(this.getPosition());
            lastElement = this.currentElement;
        } 
    }
    
    public void PlayerInput(float tpf)
    {
        final Vector3<Float> movePos = new Vector3<>(0f, 0f, 0f);
        int moveKeyCount = 0;

        if(Application.getInstance().getPrutKeyBoard().GetState(GLFW_KEY_W) == GLFW_REPEAT || Application.getInstance().getPrutKeyBoard().GetState(GLFW_KEY_UP) == GLFW_REPEAT)
        {
            movePos.y = 1f;
        }
        if(Application.getInstance().getPrutKeyBoard().GetState(GLFW_KEY_A) == GLFW_REPEAT || Application.getInstance().getPrutKeyBoard().GetState(GLFW_KEY_LEFT) == GLFW_REPEAT)
        {
            movePos.x = -1f;
        }      
        if(Application.getInstance().getPrutKeyBoard().GetState(GLFW_KEY_S) == GLFW_REPEAT || Application.getInstance().getPrutKeyBoard().GetState(GLFW_KEY_DOWN) == GLFW_REPEAT)
        {
            movePos.y = -1f;
        }  
        if(Application.getInstance().getPrutKeyBoard().GetState(GLFW_KEY_D) == GLFW_REPEAT || Application.getInstance().getPrutKeyBoard().GetState(GLFW_KEY_RIGHT) == GLFW_REPEAT)
        {
            movePos.x = 1f;
        }
        translate(movePos,speed * tpf);
    }
    
    @Override
    public void onCollision(CollideAble collideWith) throws PrutEngineException {
        if(collideWith instanceof Actor)
        {
            Actor otherActor = (Actor)collideWith;
            switch(this.currentElement)
            {
                case Sphere:
                    if(otherActor.currentElement == Element.Cube)
                       AddScore();
                    break;
                case Torus:
                    if(otherActor.currentElement == Element.Sphere)
                       AddScore();
                    break;
                case Cube:
                    if(otherActor.currentElement == Element.Torus)
                       AddScore();
                    break;
            }
        }
        super.onCollision(collideWith);
    }
    
    @Override
    protected void Die() throws PrutEngineException
    {
        super.Die();
        ResetScore();
        this.gameScene.shakeScreen();
    }
}