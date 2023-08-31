package com.projekpbo.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;

public class Animation {
    ArrayList<TextureRegion> frames = new ArrayList<>();
    long lastFrameTime = 0;
    long frameDuration = 300;
    int currentFrame = 0;
    Animation(Texture sheet, int width, int height) {
        for(int i = 0; i < sheet.getWidth()/width; i++) {
            frames.add(new TextureRegion(sheet, i*width, 0, width, height));
        }
    }
    TextureRegion animate() {
        if(lastFrameTime == 0) {
            currentFrame = -1;
        }
        if(TimeUtils.millis() - lastFrameTime > frameDuration) {
            lastFrameTime = TimeUtils.millis();
            if(currentFrame >= frames.size()-1) {
                currentFrame = 0;
            } else {
                currentFrame++;
            }
        }
        return frames.get(currentFrame);
    }
}
