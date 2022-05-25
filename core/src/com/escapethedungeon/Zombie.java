package com.escapethedungeon;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Zombie {
    public int x, y;
    int health, max_health;
    int damage = 20;
    float size, real_x, real_y, horizontal_otstup, vertical_otstup;
    Texture img, ghost;
    BitmapFont font;
    Zombie(int x, int y, Texture img, float size, float horizontal_otstup, float vertical_otstup){
        this.x = x;
        this.y = y;
        this.img = img;
        this.size = size;
        real_x = this.x*size+horizontal_otstup;
        real_y = this.y*size+vertical_otstup;
        this.horizontal_otstup = horizontal_otstup;
        this.vertical_otstup = vertical_otstup;
        font = new BitmapFont();
        font.setColor(Color.RED);
        health = 100;
        max_health = 100;
        ghost = new Texture("ничего.png");
    }
    void move(int x, int y){
        this.x += x;
        this.y += y;
    }
    void draw(Batch spritebatch, float size, float speed){
        if (health>0) {
            spritebatch.draw(img, real_x, real_y, size, size);
            font.draw(spritebatch, health + "/" + max_health, real_x + size / 5, real_y + size - 1 / 2 * size / 10);
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
    }
    void change_img(Texture img){
        this.img = img;
    }
    void hit(int damage){
        health -= damage;
        if (health<=0){
            kill();
        }
    }
    void kill(){
        change_img(ghost);
    }
}
