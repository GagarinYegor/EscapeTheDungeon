package com.escapethedungeon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Button {
    public int x, y;
    float size, real_x, real_y, horizontal_otstup, vertical_otstup;
    Texture img;
    Button(int x, int y, Texture img, float size, float horizontal_otstup, float vertical_otstup){
        this.x = x;
        this.y = y;
        this.img = img;
        this.size = size;
        real_x = this.x*size+horizontal_otstup;
        real_y = this.y*size+vertical_otstup;
        this.vertical_otstup = vertical_otstup;
        this.horizontal_otstup = horizontal_otstup;
    }
    void move(int x, int y){
        this.x += x;
        this.y += y;
    }
    void draw(Batch spritebatch, float size, float speed, float height, float width){
        spritebatch.draw(img, real_x, real_y, height, width);
        if (real_x<x*size+horizontal_otstup){
            if (real_x+speed<x*size+horizontal_otstup) {
                real_x += speed;
            }
            else real_x+=speed-(real_x+speed-(x*size+horizontal_otstup));
        }
        if (real_x>x*size+horizontal_otstup){
            if (real_x+speed>x*size+horizontal_otstup) {
                real_x -= speed;
            }
            else real_x-=speed-(real_x+speed-(x*size+horizontal_otstup));
        }
        if (real_y<y*size+vertical_otstup){
            if (real_y+speed<y*size+vertical_otstup) {
                real_y += speed;
            }
            else {
                real_y+=speed-(real_y+speed-(y*size+vertical_otstup));
            }
        }
        if (real_y>y*size+vertical_otstup){
            if (real_y+speed>y*size+vertical_otstup) {
                real_y -= speed;
            }
            else real_y-=speed-(real_y+speed-(y*size+vertical_otstup));
        }
    }
    void change_img(Texture img){
        this.img = img;
    }
}
