package com.escapethedungeon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Lever {
    public Texture img, activ_lever, passiv_lever;
    public int x, y, door_cord_x, door_cord_y;
    float size, real_x, real_y, horizontal_otstup, vertical_otstup;
    boolean moveable, is_horizontal;

    Lever(int x, int y, float size, int door_cord_x, int door_cord_y, float horizontal_otstup, float vertical_otstup, boolean is_horizontal) {
        this.x = x;
        this.y = y;
        activ_lever = new Texture("активированный рычаг.png");
        passiv_lever = new Texture("неактивированный рычаг.png");
        img = passiv_lever;
        this.size = size;
        real_x = this.x*size+horizontal_otstup;
        real_y = this.y*size+vertical_otstup;
        this.horizontal_otstup = horizontal_otstup;
        this.vertical_otstup = vertical_otstup;
        this.door_cord_x = door_cord_x;
        this.door_cord_y = door_cord_y;
        moveable = false;
        this.is_horizontal = is_horizontal;
    }

    void draw(Batch spritebatch, float size, float speed){
        spritebatch.draw(img, real_x, real_y, size, size);
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

    public void click(){
        if(img == passiv_lever){
            img = activ_lever;
            moveable = true;
        }
        else{
            img = passiv_lever;
            moveable = false;
        }
    }
}
