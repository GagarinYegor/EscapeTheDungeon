package com.escapethedungeon;

import com.badlogic.gdx.graphics.Texture;

public class Cage {
    public boolean moveable;
    public Texture img;
    public int x, y;
    Cage(int x, int y, boolean moveable, Texture img){
        this.x = x;
        this.y = y;
        this.moveable = moveable;
        this.img = img;
    }
    void change_img(Texture img){
        this.img = img;
    }
}
