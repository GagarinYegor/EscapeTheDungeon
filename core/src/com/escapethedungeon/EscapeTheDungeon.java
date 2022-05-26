package com.escapethedungeon;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;

public class EscapeTheDungeon extends ApplicationAdapter {
	int width, height, camera_move_right, camera_move_left, camera_move_up, camera_move_down, exp, max_exp, lvl, moves;
	float size, horisontal_otstup, vertical_otstup, left_border_x, left_border_y, right_border_x, right_border_y,
		  up_border_x, up_border_y, down_border_x, down_border_y;
	boolean menu_open, is_attack, is_hod;
	public static final int SCR_WIDTH = 960, SCR_HEIGHT = 540;
	public static final float  speed = 4;
	OrthographicCamera camera;
	SpriteBatch level_one, inventory;
	Texture wall_right, wall_up, wall_down, wall_left,
			corner_right_up, corner_right_down, corner_left_up, corner_left_down,
			floor, zombie, mage_right, mage_left, attack_btn_img, activ_attack_btn_img, column,
			corner_and_wall_down_right, corner_and_wall_up_right, corner_and_wall_down_left, corner_and_wall_up_left,
			activ_lever, passiv_lever, space, closed_horizontal_door,open_horizontal_door, border,
			waiting_btn_img, closed_vertical_door, open_vertical_door;
	Cage [][] cages;
	Zombie [] zombies;
	Player player;
	Button attack_btn, waiting_btn;
	BitmapFont font;
	Lever [] levers;
	Music music;

	boolean moveable(int x, int y){
		if(cages[x][y].moveable) return true;
		else return false;
	}

	void hit_monster(Zombie zombie){
		zombie.hit(player.damage);
		if (zombie.health<=0){
			cages[zombie.x][zombie.y].moveable = true;
			zombie.move(9, 6);
			zombie.kill();
			exp_count(5);
		}
	}

	void exp_count(int n){
		exp += n;
		if (exp>=max_exp){
			lvl+=1;
			player.damage+=2;
			max_exp=exp*2;
			player.max_health+=20;
			player.health = player.max_health;
		}
	}

	void move_monster(){
		for (Zombie zombie: zombies){
			if (zombie.health > 0 && Math.abs(zombie.x - player.x) < 9 && Math.abs(zombie.y - player.y) < 7){
				boolean hod = true;
				if (hod && Math.abs(zombie.x - player.x) <= 1 && Math.abs(zombie.x - player.x) >= 0 && Math.abs(zombie.y - player.y) <= 1 && Math.abs(zombie.y - player.y) >= 0){
					player.hit(zombie.damage);
					hod = false;
					cages[zombie.x][zombie.y].moveable = false;
				}
				if (hod && zombie.x == player.x && zombie.y < player.y){
					cages[zombie.x][zombie.y].moveable = true;
					if (moveable(zombie.x, zombie.y+1)) zombie.move(0, 1);
					else if (moveable(zombie.x+1, zombie.y)) zombie.move(1, 0);
					else if (moveable(zombie.x-1, zombie.y)) zombie.move(-1, 0);
					hod = false;
					cages[zombie.x][zombie.y].moveable = false;
				}
				if (hod && zombie.x == player.x && zombie.y > player.y){
					cages[zombie.x][zombie.y].moveable = true;
					if (moveable(zombie.x, zombie.y-1)) zombie.move(0, -1);
					else if (moveable(zombie.x+1, zombie.y)) zombie.move(1, 0);
					else if (moveable(zombie.x-1, zombie.y)) zombie.move(-1, 0);
					hod = false;
					cages[zombie.x][zombie.y].moveable = false;
				}
				if (hod && zombie.x > player.x && zombie.y == player.y){
					cages[zombie.x][zombie.y].moveable = true;
					if (moveable(zombie.x-1, zombie.y)) zombie.move(-1, 0);
					else if (moveable(zombie.x, zombie.y+1)) zombie.move(0, 1);
					else if (moveable(zombie.x, zombie.y-1)) zombie.move(0, -1);
					hod = false;
					cages[zombie.x][zombie.y].moveable = false;
				}
				if (hod && zombie.x < player.x && zombie.y == player.y){
					cages[zombie.x][zombie.y].moveable = true;
					if (moveable(zombie.x+1, zombie.y)) zombie.move(+1, 0);
					else if (moveable(zombie.x, zombie.y+1)) zombie.move(0, 1);
					else if (moveable(zombie.x, zombie.y-1)) zombie.move(0, -1);
					hod = false;
					cages[zombie.x][zombie.y].moveable = false;
				}
				if (hod && zombie.x < player.x && zombie.y < player.y){
					cages[zombie.x][zombie.y].moveable = true;
					if (Math.abs(zombie.x - player.x) < Math.abs(zombie.y- player.y)){
						if (moveable(zombie.x, zombie.y+1)) zombie.move(0, 1);
						else if (moveable(zombie.x+1, zombie.y)) zombie.move(1, 0);
						else if (moveable(zombie.x+1, zombie.y+1)) zombie.move(1, 1);
					}
					else if (Math.abs(zombie.x - player.x) > Math.abs(zombie.y- player.y)){
						if (moveable(zombie.x+1, zombie.y)) zombie.move(1, 0);
						else if (moveable(zombie.x, zombie.y+1)) zombie.move(0, 1);
						else if (moveable(zombie.x+1, zombie.y+1)) zombie.move(1, 1);
					}
					else if (Math.abs(zombie.x - player.x) == Math.abs(zombie.y- player.y)){
						if (moveable(zombie.x+1, zombie.y+1)) zombie.move(1, 1);
						else if (moveable(zombie.x, zombie.y+1)) zombie.move(0, 1);
						else if (moveable(zombie.x+1, zombie.y)) zombie.move(1, 0);
					}
					hod = false;
					cages[zombie.x][zombie.y].moveable = false;
				}
				if (hod && zombie.x > player.x && zombie.y < player.y){
					cages[zombie.x][zombie.y].moveable = true;
					if (Math.abs(zombie.x - player.x) < Math.abs(zombie.y- player.y)){
						if (moveable(zombie.x, zombie.y+1)) zombie.move(0, 1);
						else if (moveable(zombie.x-1, zombie.y)) zombie.move(-1, 0);
						else if (moveable(zombie.x-1, zombie.y+1)) zombie.move(-1, 1);
					}
					else if (Math.abs(zombie.x - player.x) > Math.abs(zombie.y- player.y)){
						if (moveable(zombie.x-1, zombie.y)) zombie.move(-1, 0);
						else if (moveable(zombie.x, zombie.y+1)) zombie.move(0, 1);
						else if (moveable(zombie.x-1, zombie.y+1)) zombie.move(-1, 1);
					}
					else if (Math.abs(zombie.x - player.x) == Math.abs(zombie.y- player.y)){
						if (moveable(zombie.x-1, zombie.y+1)) zombie.move(-1, 1);
						else if (moveable(zombie.x, zombie.y+1)) zombie.move(0, 1);
						else if (moveable(zombie.x-1, zombie.y)) zombie.move(-1, 0);
					}
					hod = false;
					cages[zombie.x][zombie.y].moveable = false;
				}
				if (hod && zombie.x < player.x && zombie.y > player.y){
					cages[zombie.x][zombie.y].moveable = true;
					if (Math.abs(zombie.x - player.x) < Math.abs(zombie.y- player.y)){
						if (moveable(zombie.x, zombie.y-1)) zombie.move(0, -1);
						else if (moveable(zombie.x+1, zombie.y)) zombie.move(1, 0);
						else if (moveable(zombie.x+1, zombie.y-1)) zombie.move(1, -1);
					}
					else if (Math.abs(zombie.x - player.x) > Math.abs(zombie.y- player.y)){
						if (moveable(zombie.x+1, zombie.y)) zombie.move(1, 0);
						else if (moveable(zombie.x, zombie.y-1)) zombie.move(0, -1);
						else if (moveable(zombie.x+1, zombie.y-1)) zombie.move(1, -1);
					}
					else if (Math.abs(zombie.x - player.x) == Math.abs(zombie.y- player.y)){
						if (moveable(zombie.x+1, zombie.y-1)) zombie.move(1, -1);
						else if (moveable(zombie.x, zombie.y-1)) zombie.move(0, -1);
						else if (moveable(zombie.x+1, zombie.y)) zombie.move(1, 0);
					}
					hod = false;
					cages[zombie.x][zombie.y].moveable = false;
				}
				if (hod && zombie.x > player.x && zombie.y > player.y){
					cages[zombie.x][zombie.y].moveable = true;
					if (Math.abs(zombie.x - player.x) < Math.abs(zombie.y- player.y)){
						if (moveable(zombie.x, zombie.y-1)) zombie.move(0, -1);
						else if (moveable(zombie.x-1, zombie.y)) zombie.move(-1, 0);
						else if (moveable(zombie.x-1, zombie.y-1)) zombie.move(-1, -1);
					}
					else if (Math.abs(zombie.x - player.x) > Math.abs(zombie.y- player.y)){
						if (moveable(zombie.x-1, zombie.y)) zombie.move(-1, 0);
						else if (moveable(zombie.x, zombie.y-1)) zombie.move(0, -1);
						else if (moveable(zombie.x-1, zombie.y-1)) zombie.move(-1, -1);
					}
					else if (Math.abs(zombie.x - player.x) == Math.abs(zombie.y- player.y)){
						if (moveable(zombie.x-1, zombie.y-1)) zombie.move(-1, -1);
						else if (moveable(zombie.x, zombie.y-1)) zombie.move(0, -1);
						else if (moveable(zombie.x-1, zombie.y)) zombie.move(-1, 0);
					}
					hod = false;
					cages[zombie.x][zombie.y].moveable = false;
				}
				cages[zombie.x][zombie.y].moveable = false;
			}
		}
	}

	@Override
	public void create () {
		width = Gdx.app.getGraphics().getWidth();
		height = Gdx.app.getGraphics().getHeight();
		size = height / 7;
		System.out.println(size*10);
		horisontal_otstup = (width-size*10)/2;
		vertical_otstup = 0;
		if(horisontal_otstup < 0){
			horisontal_otstup = 0;
			size = width/10;
			vertical_otstup = (height - 7*size)/2;
		}
		left_border_x = 0;
		left_border_y = 0;
		right_border_x = width-horisontal_otstup;
		right_border_y = 0;
		up_border_x = 0;
		up_border_y = height-vertical_otstup;
		down_border_x = 0;
		down_border_y = 0;
		moves=0;
		//music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
		//music.setLooping(true);
		//music.setVolume(1f);
		//music.play();

		camera = new OrthographicCamera(width, height);
		camera.setToOrtho(false, width, height);
		level_one = new SpriteBatch();
		inventory = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(Color.RED);
		is_hod = true;

		border = new Texture("Рамка.png");
		corner_right_up = new Texture("Правый верхний угол.png");
		corner_right_down = new Texture("Правый нижний угол.png");
		corner_left_up = new Texture("Левый верхний угол.png");
		corner_left_down = new Texture("Левый нижний угол.png");
		corner_and_wall_down_right = new Texture("правая нижняя стена с углом.png");
		corner_and_wall_up_right = new Texture("правая верхняя стена с углом.png");
		corner_and_wall_down_left = new Texture("левая нижняя стена с углом.png");
		corner_and_wall_up_left = new Texture("левая верхняя стена с углом.png");
		wall_right = new Texture("Стена справа.png");
		wall_up = new Texture("Стена сверху.png");
		wall_down = new Texture("Стена снизу.png");
		wall_left = new Texture("Стена слева.png");
		floor = new Texture("Пол.png");
		zombie = new Texture("Зомби вправо.png");
		mage_right = new Texture("Маг вправо.png");
		mage_left = new Texture("Маг влево.png");
		attack_btn_img = new Texture("Кнопка атаки.png");
		activ_attack_btn_img = new Texture("Активная кнопка атаки.png");
		waiting_btn_img = new Texture("Ожидание.png");
		column = new Texture("Колонна.png");
		activ_lever = new Texture("активированный рычаг.png");
		passiv_lever = new Texture("неактивированный рычаг.png");
		space = new Texture("пробел.png");
		closed_horizontal_door = new Texture("Закрытые горизонтальные двери.png");
		open_horizontal_door = new Texture("Открытые горизонтальные двери.png");
		closed_vertical_door = new Texture("Закрытые вертикальные двери.png");
		open_vertical_door = new Texture("Открытые вертикальные двери.png");

		camera_move_up = 0;
		camera_move_down = 0;
		camera_move_right = 0;
		camera_move_left = 0;

		zombies = new Zombie[16];
		zombies[0] = new Zombie(8, 8, zombie, size, horisontal_otstup, vertical_otstup);
		zombies[1] = new Zombie(7, 1, zombie, size, horisontal_otstup, vertical_otstup);
		zombies[2] = new Zombie(14, 2, zombie, size, horisontal_otstup, vertical_otstup);
		zombies[3] = new Zombie(20, 2, zombie, size, horisontal_otstup, vertical_otstup);
		zombies[4] = new Zombie(16, 9, zombie, size, horisontal_otstup, vertical_otstup);
		zombies[5] = new Zombie(20, 9, zombie, size, horisontal_otstup, vertical_otstup);
		zombies[6] = new Zombie(16, 13, zombie, size, horisontal_otstup, vertical_otstup);
		zombies[7] = new Zombie(20, 13, zombie, size, horisontal_otstup, vertical_otstup);
		zombies[8] = new Zombie(8, 14, zombie, size, horisontal_otstup, vertical_otstup);
		zombies[9] = new Zombie(1, 9, zombie, size, horisontal_otstup, vertical_otstup);
		zombies[10] = new Zombie(11, 1, zombie, size, horisontal_otstup, vertical_otstup);
		zombies[11] = new Zombie(23, 1, zombie, size, horisontal_otstup, vertical_otstup);
		zombies[12] = new Zombie(5, 23, zombie, size, horisontal_otstup, vertical_otstup);
		zombies[13] = new Zombie(21, 18, zombie, size, horisontal_otstup, vertical_otstup);
		zombies[14] = new Zombie(1, 30, zombie, size, horisontal_otstup, vertical_otstup);
		zombies[15] = new Zombie(18, 21, zombie, size, horisontal_otstup, vertical_otstup);

		player = new Player(4,3, mage_right, size, 100, horisontal_otstup, vertical_otstup);

		attack_btn = new Button(9, 2, attack_btn_img, size, horisontal_otstup, vertical_otstup);
		waiting_btn = new Button(9, 1, waiting_btn_img, size, horisontal_otstup, vertical_otstup);

		levers = new Lever[11];
		levers[0] = new Lever(8, 3, size, 4, 6, horisontal_otstup, vertical_otstup, true);
		levers[1] = new Lever(7, 8, size, 18, 16, horisontal_otstup, vertical_otstup, true);
		levers[2] = new Lever(4, 23, size, 23, 11, horisontal_otstup, vertical_otstup, false);
		levers[3] = new Lever(1, 8, size, 14, 3, horisontal_otstup, vertical_otstup, true);
		levers[4] = new Lever(5, 14, size, 8, 25, horisontal_otstup, vertical_otstup, true);
		levers[5] = new Lever(14, 1, size, 20, 3, horisontal_otstup, vertical_otstup, true);
		levers[6] = new Lever(20, 1, size, 17, 3, horisontal_otstup, vertical_otstup, true);
		levers[7] = new Lever(17, 1, size, 2, 26, horisontal_otstup, vertical_otstup, false);
		levers[8] = new Lever(1, 29, size, 11, 17, horisontal_otstup, vertical_otstup, true);
		levers[9] = new Lever(15, 27, size, 10, 20, horisontal_otstup, vertical_otstup, false);
		levers[10] = new Lever(17, 24, size, 1, 21, horisontal_otstup, vertical_otstup, true);

		cages = new Cage[32][32];
		exp = 0;
		max_exp = 5;
		lvl = 1;

		//size = 40;

		cages[0][0] = new Cage(0, 0, false, corner_right_up);
		cages[0][1] = new Cage(0, 1, false, wall_right);
		cages[0][2] = new Cage(0, 2, false, wall_right);
		cages[0][3] = new Cage(0, 3, false, wall_right);
		cages[0][4] = new Cage(0, 4, false, wall_right);
		cages[0][5] = new Cage(0, 5, false, wall_right);
		cages[0][6] = new Cage(0, 6, false, corner_right_down);
		cages[0][7] = new Cage(0, 7, false, corner_right_up);
		cages[0][8] = new Cage(0, 8, false, wall_right);
		cages[0][9] = new Cage(0, 9, false, wall_right);
		cages[0][10] = new Cage(0, 10, false, wall_right);
		cages[0][11] = new Cage(0, 11, false, wall_right);
		cages[0][12] = new Cage(0, 12, false, wall_right);
		cages[0][13] = new Cage(0, 13, false, wall_right);
		cages[0][14] = new Cage(0, 14, false, wall_right);
		cages[0][15] = new Cage(0, 15, false, wall_right);
		cages[0][16] = new Cage(0, 16, false, wall_right);
		cages[0][17] = new Cage(0, 17, false, wall_right);
		cages[0][18] = new Cage(0, 18, false, corner_right_down);
		cages[0][19] = new Cage(0, 19, false, corner_right_up);
		cages[0][20] = new Cage(0, 20, false, wall_right);
		cages[0][21] = new Cage(0, 21, false, wall_right);
		cages[0][22] = new Cage(0, 22, false, wall_right);
		cages[0][23] = new Cage(0, 23, false, wall_right);
		cages[0][24] = new Cage(0, 24, false, wall_right);
		cages[0][25] = new Cage(0, 25, false, wall_right);
		cages[0][26] = new Cage(0, 26, false, wall_right);
		cages[0][27] = new Cage(0, 27, false, corner_right_down);
		cages[0][28] = new Cage(0, 28, false, corner_right_up);
		cages[0][29] = new Cage(0, 29, false, wall_right);
		cages[0][30] = new Cage(0, 30, false, wall_right);
		cages[0][31] = new Cage(0, 31, false, corner_right_down);

		cages[1][0] = new Cage(1, 0, false, wall_up);
		cages[1][1] = new Cage(1, 1,true, floor);
		cages[1][2] = new Cage(1, 2,true, floor);
		cages[1][3] = new Cage(1, 3,true, floor);
		cages[1][4] = new Cage(1, 4,true, floor);
		cages[1][5] = new Cage(1, 5,true, floor);
		cages[1][6] = new Cage(1, 6,false, wall_down);
		cages[1][7] = new Cage(1, 7, false, wall_up);
		cages[1][8] = new Cage(1, 8, false, floor);
		cages[1][9] = new Cage(1, 9, true, floor);
		cages[1][10] = new Cage(1, 10, true, floor);
		cages[1][11] = new Cage(1, 11, true, floor);
		cages[1][12] = new Cage(1, 12, true, floor);
		cages[1][13] = new Cage(1, 13, true, floor);
		cages[1][14] = new Cage(1, 14, true, floor);
		cages[1][15] = new Cage(1, 15, true, floor);
		cages[1][16] = new Cage(1, 16, true, floor);
		cages[1][17] = new Cage(1, 17, true, floor);
		cages[1][18] = new Cage(1, 18, false, wall_down);
		cages[1][19] = new Cage(1, 19, false, wall_up);
		cages[1][20] = new Cage(1, 20, true, floor);
		cages[1][21] = new Cage(1, 21, false, closed_horizontal_door);
		cages[1][22] = new Cage(1, 22, true, floor);
		cages[1][23] = new Cage(1, 23, true, floor);
		cages[1][24] = new Cage(1, 24, true, floor);
		cages[1][25] = new Cage(1, 25, true, floor);
		cages[1][26] = new Cage(1, 26, true, floor);
		cages[1][27] = new Cage(1, 27, false, wall_down);
		cages[1][28] = new Cage(1, 28, false, wall_up);
		cages[1][29] = new Cage(1, 29, false, floor);
		cages[1][30] = new Cage(1, 30, true, floor);
		cages[1][31] = new Cage(1, 31, false, wall_down);

		cages[2][0] = new Cage(2, 0, false, wall_up);
		cages[2][1] = new Cage(2, 1, true, floor);
		cages[2][2] = new Cage(2, 2, true, floor);
		cages[2][3] = new Cage(2, 3, true, floor);
		cages[2][4] = new Cage(2, 4, true, floor);
		cages[2][5] = new Cage(2, 5, true, floor);
		cages[2][6] = new Cage(2, 6, false, wall_down);
		cages[2][7] = new Cage(2, 7, false, corner_left_up);
		cages[2][8] = new Cage(2, 8, false, wall_left);
		cages[2][9] = new Cage(2, 9, false, wall_left);
		cages[2][10] = new Cage(2, 10, false, wall_left);
		cages[2][11] = new Cage(2, 11, false, wall_left);
		cages[2][12] = new Cage(2, 12, false, wall_left);
		cages[2][13] = new Cage(2, 13, false, corner_and_wall_up_left);
		cages[2][14] = new Cage(2, 14, true, floor);
		cages[2][15] = new Cage(2, 15, false, corner_and_wall_down_left);
		cages[2][16] = new Cage(2, 16, false, corner_and_wall_up_left);
		cages[2][17] = new Cage(2, 17, true, floor);
		cages[2][18] = new Cage(2, 18, false, wall_down);
		cages[2][19] = new Cage(2, 19, false, wall_up);
		cages[2][20] = new Cage(2, 20, true, floor);
		cages[2][21] = new Cage(2, 21, false, corner_and_wall_down_left);
		cages[2][22] = new Cage(2, 22, false, wall_left);
		cages[2][23] = new Cage(2, 23, false, wall_left);
		cages[2][24] = new Cage(2, 24, false, wall_left);
		cages[2][25] = new Cage(2, 25, false, corner_and_wall_up_left);
		cages[2][26] = new Cage(2, 26, false, closed_vertical_door);
		cages[2][27] = new Cage(2, 27, false, wall_down);
		cages[2][28] = new Cage(2, 28, false, corner_left_up);
		cages[2][29] = new Cage(2, 29, false, corner_and_wall_up_left);
		cages[2][30] = new Cage(2, 30, true, floor);
		cages[2][31] = new Cage(2, 31, false, wall_down);

		cages[3][0] = new Cage(3, 0, false, wall_up);
		cages[3][1] = new Cage(3, 1, true, floor);
		cages[3][2] = new Cage(3, 2, true, floor);
		cages[3][3] = new Cage(3, 3, true, floor);
		cages[3][4] = new Cage(3, 4, true, floor);
		cages[3][5] = new Cage(3, 5, true, floor);
		cages[3][6] = new Cage(3, 6, false, corner_and_wall_down_right);
		cages[3][7] = new Cage(3, 7, false, wall_right);
		cages[3][8] = new Cage(3, 8, false, wall_right);
		cages[3][9] = new Cage(3, 9, false, wall_right);
		cages[3][10] = new Cage(3, 10, false, wall_right);
		cages[3][11] = new Cage(3, 11, false, wall_right);
		cages[3][12] = new Cage(3, 12, false, corner_right_down);
		cages[3][13] = new Cage(3, 13, false, wall_up);
		cages[3][14] = new Cage(3, 14, true, floor);
		cages[3][15] = new Cage(3, 15, false, wall_down);
		cages[3][16] = new Cage(3, 16, false, wall_up);
		cages[3][17] = new Cage(3, 17, true, floor);
		cages[3][18] = new Cage(3, 18, false, wall_down);
		cages[3][19] = new Cage(3, 19, false, wall_up);
		cages[3][20] = new Cage(3, 20, true, floor);
		cages[3][21] = new Cage(3, 21, false, wall_down);
		cages[3][22] = new Cage(3, 22, false, corner_right_up);
		cages[3][23] = new Cage(3, 23, false, wall_right);
		cages[3][24] = new Cage(3, 24, false, corner_right_down);
		cages[3][25] = new Cage(3, 25, false, wall_up);
		cages[3][26] = new Cage(3, 26, true, floor);
		cages[3][27] = new Cage(3, 27, false, wall_down);
		cages[3][28] = new Cage(3, 28, false, space);
		cages[3][29] = new Cage(3, 29, false, wall_up);
		cages[3][30] = new Cage(3, 30, true, floor);
		cages[3][31] = new Cage(3, 31, false, wall_down);

		cages[4][0] = new Cage(4, 0, false, wall_up);
		cages[4][1] = new Cage(4, 1, true, floor);
		cages[4][2] = new Cage(4, 2, true, floor);
		cages[4][3] = new Cage(4, 3, true, floor);
		cages[4][4] = new Cage(4, 4, true, floor);
		cages[4][5] = new Cage(4, 5, true, floor);
		cages[4][6] = new Cage(4, 6, false, closed_horizontal_door);
		cages[4][7] = new Cage(4, 7, true, floor);
		cages[4][8] = new Cage(4, 8, true, floor);
		cages[4][9] = new Cage(4, 9, true, floor);
		cages[4][10] = new Cage(4, 10, true, floor);
		cages[4][11] = new Cage(4, 11, true, floor);
		cages[4][12] = new Cage(4, 12, false, wall_down);
		cages[4][13] = new Cage(4, 13, false, wall_up);
		cages[4][14] = new Cage(4, 14, true, floor);
		cages[4][15] = new Cage(4, 15, false, wall_down);
		cages[4][16] = new Cage(4, 16, false, wall_up);
		cages[4][17] = new Cage(4, 17, true, floor);
		cages[4][18] = new Cage(4, 18, false, wall_down);
		cages[4][19] = new Cage(4, 19, false, wall_up);
		cages[4][20] = new Cage(4, 20, true, floor);
		cages[4][21] = new Cage(4, 21, false, wall_down);
		cages[4][22] = new Cage(4, 22, false, wall_up);
		cages[4][23] = new Cage(4, 23, false, floor);
		cages[4][24] = new Cage(4, 24, false, wall_down);
		cages[4][25] = new Cage(4, 25, false, wall_up);
		cages[4][26] = new Cage(4, 26, true, floor);
		cages[4][27] = new Cage(4, 27, false, wall_down);
		cages[4][28] = new Cage(4, 28, false, space);
		cages[4][29] = new Cage(4, 29, false, wall_up);
		cages[4][30] = new Cage(4, 30, true, floor);
		cages[4][31] = new Cage(4, 31, false, wall_down);

		cages[5][0] = new Cage(5, 0, false, wall_up);
		cages[5][1] = new Cage(5, 1, true, floor);
		cages[5][2] = new Cage(5, 2, true, floor);
		cages[5][3] = new Cage(5, 3, true, floor);
		cages[5][4] = new Cage(5, 4, true, floor);
		cages[5][5] = new Cage(5, 5, true, floor);
		cages[5][6] = new Cage(5, 6, false, corner_and_wall_down_left);
		cages[5][7] = new Cage(5, 7, false, wall_left);
		cages[5][8] = new Cage(5, 8, false, wall_left);
		cages[5][9] = new Cage(5, 9, false, wall_left);
		cages[5][10] = new Cage(5, 10, false, corner_and_wall_up_left);
		cages[5][11] = new Cage(5, 11, true, floor);
		cages[5][12] = new Cage(5, 12, false, wall_down);
		cages[5][13] = new Cage(5, 13, false, wall_up);
		cages[5][14] = new Cage(5, 14, false, floor);
		cages[5][15] = new Cage(5, 15, false, wall_down);
		cages[5][16] = new Cage(5, 16, false, wall_up);
		cages[5][17] = new Cage(5, 17, true, floor);
		cages[5][18] = new Cage(5, 18, false, wall_down);
		cages[5][19] = new Cage(5, 19, false, wall_up);
		cages[5][20] = new Cage(5, 20, true, floor);
		cages[5][21] = new Cage(5, 21, false, wall_down);
		cages[5][22] = new Cage(5, 22, false, wall_up);
		cages[5][23] = new Cage(5, 23, true, floor);
		cages[5][24] = new Cage(5, 24, false, wall_down);
		cages[5][25] = new Cage(5, 25, false, wall_up);
		cages[5][26] = new Cage(5, 26, true, floor);
		cages[5][27] = new Cage(5, 27, false, wall_down);
		cages[5][28] = new Cage(5, 28, false, space);
		cages[5][29] = new Cage(5, 29, false, wall_up);
		cages[5][30] = new Cage(5, 30, true, floor);
		cages[5][31] = new Cage(5, 31, false, wall_down);

		cages[6][0] = new Cage(6, 0, false, wall_up);
		cages[6][1] = new Cage(6, 1, true, floor);
		cages[6][2] = new Cage(6, 2, false, column);
		cages[6][3] = new Cage(6, 3, true, floor);
		cages[6][4] = new Cage(6, 4, false, column);
		cages[6][5] = new Cage(6, 5, true, floor);
		cages[6][6] = new Cage(6, 6, false, wall_down);
		cages[6][7] = new Cage(6, 7, false, corner_right_up);
		cages[6][8] = new Cage(6, 8, false, wall_right);
		cages[6][9] = new Cage(6, 9, false, corner_right_down);
		cages[6][10] = new Cage(6, 10, false, wall_up);
		cages[6][11] = new Cage(6, 11, true, floor);
		cages[6][12] = new Cage(6, 12, false, wall_down);
		cages[6][13] = new Cage(6, 13, false, corner_left_up);
		cages[6][14] = new Cage(6, 14, false, wall_left);
		cages[6][15] = new Cage(6, 15, false, corner_left_down);
		cages[6][16] = new Cage(6, 16, false, wall_up);
		cages[6][17] = new Cage(6, 17, true, floor);
		cages[6][18] = new Cage(6, 18, false, wall_down);
		cages[6][19] = new Cage(6, 19, false, wall_up);
		cages[6][20] = new Cage(6, 20, true, floor);
		cages[6][21] = new Cage(6, 21, false, wall_down);
		cages[6][22] = new Cage(6, 22, false, wall_up);
		cages[6][23] = new Cage(6, 23, true, floor);
		cages[6][24] = new Cage(6, 24, false, wall_down);
		cages[6][25] = new Cage(6, 25, false, wall_up);
		cages[6][26] = new Cage(6, 26, true, floor);
		cages[6][27] = new Cage(6, 27, false, wall_down);
		cages[6][28] = new Cage(6, 28, false, space);
		cages[6][29] = new Cage(6, 29, false, wall_up);
		cages[6][30] = new Cage(6, 30, true, floor);
		cages[6][31] = new Cage(6, 31, false, wall_down);

		cages[7][0] = new Cage(7, 0, false, wall_up);
		cages[7][1] = new Cage(7, 1, true, floor);
		cages[7][2] = new Cage(7, 2, true, floor);
		cages[7][3] = new Cage(7, 3, true, floor);
		cages[7][4] = new Cage(7, 4, true, floor);
		cages[7][5] = new Cage(7, 5, true, floor);
		cages[7][6] = new Cage(7, 6, false, wall_down);
		cages[7][7] = new Cage(7, 7, false, wall_up);
		cages[7][8] = new Cage(7, 8, false, floor);
		cages[7][9] = new Cage(7, 9, false, wall_down);
		cages[7][10] = new Cage(7, 10, false, wall_up);
		cages[7][11] = new Cage(7, 11, true, floor);
		cages[7][12] = new Cage(7, 12, false, wall_down);
		cages[7][13] = new Cage(7, 13, false, corner_right_up);
		cages[7][14] = new Cage(7, 14, false, wall_right);
		cages[7][15] = new Cage(7, 15, false, wall_right);
		cages[7][16] = new Cage(7, 16, false, corner_and_wall_up_right);
		cages[7][17] = new Cage(7, 17, true, floor);
		cages[7][18] = new Cage(7, 18, false, wall_down);
		cages[7][19] = new Cage(7, 19, false, wall_up);
		cages[7][20] = new Cage(7, 20, true, floor);
		cages[7][21] = new Cage(7, 21, false, wall_down);
		cages[7][22] = new Cage(7, 22, false, wall_up);
		cages[7][23] = new Cage(7, 23, true, floor);
		cages[7][24] = new Cage(7, 24, false, corner_and_wall_down_right);
		cages[7][25] = new Cage(7, 25, false, corner_and_wall_up_right);
		cages[7][26] = new Cage(7, 26, true, floor);
		cages[7][27] = new Cage(7, 27, false, wall_down);
		cages[7][28] = new Cage(7, 28, false, space);
		cages[7][29] = new Cage(7, 29, false, wall_up);
		cages[7][30] = new Cage(7, 30, true, floor);
		cages[7][31] = new Cage(7, 31, false, wall_down);

		cages[8][0] = new Cage(8, 0, false, corner_left_up);
		cages[8][1] = new Cage(8, 1, false, wall_left);
		cages[8][2] = new Cage(8, 2, false, corner_and_wall_up_left);
		cages[8][3] = new Cage(8, 3, false, floor);
		cages[8][4] = new Cage(8, 4, false, corner_and_wall_down_left);
		cages[8][5] = new Cage(8, 5, false, wall_left);
		cages[8][6] = new Cage(8, 6, false, corner_left_down);
		cages[8][7] = new Cage(8, 7, false, wall_up);
		cages[8][8] = new Cage(8, 8, true, floor);
		cages[8][9] = new Cage(8, 9, false, wall_down);
		cages[8][10] = new Cage(8, 10, false, wall_up);
		cages[8][11] = new Cage(8, 11, true, floor);
		cages[8][12] = new Cage(8, 12, false, wall_down);
		cages[8][13] = new Cage(8, 13, false, wall_up);
		cages[8][14] = new Cage(8, 14, true, floor);
		cages[8][15] = new Cage(8, 15, true, floor);
		cages[8][16] = new Cage(8, 16, true, floor);
		cages[8][17] = new Cage(8, 17, true, floor);
		cages[8][18] = new Cage(8, 18, false, wall_down);
		cages[8][19] = new Cage(8, 19, false, wall_up);
		cages[8][20] = new Cage(8, 20, true, floor);
		cages[8][21] = new Cage(8, 21, false, wall_down);
		cages[8][22] = new Cage(8, 22, false, wall_up);
		cages[8][23] = new Cage(8, 23, true, floor);
		cages[8][24] = new Cage(8, 24, true, floor);
		cages[8][25] = new Cage(8, 25, false, closed_horizontal_door);
		cages[8][26] = new Cage(8, 26, true, floor);
		cages[8][27] = new Cage(8, 27, false, wall_down);
		cages[8][28] = new Cage(8, 28, false, space);
		cages[8][29] = new Cage(8, 29, false, wall_up);
		cages[8][30] = new Cage(8, 30, true, floor);
		cages[8][31] = new Cage(8, 31, false, wall_down);

		cages[9][0] = new Cage(9, 0, false, space);
		cages[9][1] = new Cage(9, 1, false, space);
		cages[9][2] = new Cage(9, 2, false, corner_left_up);
		cages[9][3] = new Cage(9, 3, false, wall_left);
		cages[9][4] = new Cage(9, 4, false, corner_left_down);
		cages[9][5] = new Cage(9, 5, false, space);
		cages[9][6] = new Cage(9, 6, false, space);
		cages[9][7] = new Cage(9, 7, false, wall_up);
		cages[9][8] = new Cage(9, 8, true, floor);
		cages[9][9] = new Cage(9, 9, false, wall_down);
		cages[9][10] = new Cage(9, 10, false, wall_up);
		cages[9][11] = new Cage(9, 11, true, floor);
		cages[9][12] = new Cage(9, 12, false, wall_down);
		cages[9][13] = new Cage(9, 13, false, wall_up);
		cages[9][14] = new Cage(9, 14, true, floor);
		cages[9][15] = new Cage(9, 15, false, corner_and_wall_down_left);
		cages[9][16] = new Cage(9, 16, false, wall_left);
		cages[9][17] = new Cage(9, 17, false, wall_left);
		cages[9][18] = new Cage(9, 18, false, corner_left_down);
		cages[9][19] = new Cage(9, 19, false, wall_up);
		cages[9][20] = new Cage(9, 20, true, floor);
		cages[9][21] = new Cage(9, 21, false, wall_down);
		cages[9][22] = new Cage(9, 22, false, corner_left_up);
		cages[9][23] = new Cage(9, 23, false, wall_left);
		cages[9][24] = new Cage(9, 24, false, wall_left);
		cages[9][25] = new Cage(9, 25, false, wall_left);
		cages[9][26] = new Cage(9, 26, false, wall_left);
		cages[9][27] = new Cage(9, 27, false, corner_left_down);
		cages[9][28] = new Cage(9, 28, false, space);
		cages[9][29] = new Cage(9, 29, false, wall_up);
		cages[9][30] = new Cage(9, 30, true, floor);
		cages[9][31] = new Cage(9, 31, false, wall_down);

		cages[10][0] = new Cage(10, 0, false, corner_right_up);
		cages[10][1] = new Cage(10, 1,false, wall_right);
		cages[10][2] = new Cage(10, 2,false, wall_right);
		cages[10][3] = new Cage(10, 3,false, wall_right);
		cages[10][4] = new Cage(10, 4,false, wall_right);
		cages[10][5] = new Cage(10, 5,false, wall_right);
		cages[10][6] = new Cage(10, 6,false, wall_right);
		cages[10][7] = new Cage(10, 7, false, corner_and_wall_up_right);
		cages[10][8] = new Cage(10, 8, true, floor);
		cages[10][9] = new Cage(10, 9, false, wall_down);
		cages[10][10] = new Cage(10, 10, false, wall_up);
		cages[10][11] = new Cage(10, 11, true, floor);
		cages[10][12] = new Cage(10, 12, false, wall_down);
		cages[10][13] = new Cage(10, 13, false, wall_up);
		cages[10][14] = new Cage(10, 14, true, floor);
		cages[10][15] = new Cage(10, 15, false, corner_and_wall_down_right);
		cages[10][16] = new Cage(10, 16, false, wall_right);
		cages[10][17] = new Cage(10, 17, false, wall_right);
		cages[10][18] = new Cage(10, 18, false, wall_right);
		cages[10][19] = new Cage(10, 19, false, corner_and_wall_up_right);
		cages[10][20] = new Cage(10, 20, false, closed_vertical_door);
		cages[10][21] = new Cage(10, 21, false, corner_and_wall_down_right);
		cages[10][22] = new Cage(10, 22, false, wall_right);
		cages[10][23] = new Cage(10, 23, false, wall_right);
		cages[10][24] = new Cage(10, 24, false, wall_right);
		cages[10][25] = new Cage(10, 25, false, wall_right);
		cages[10][26] = new Cage(10, 26, false, wall_right);
		cages[10][27] = new Cage(10, 27, false, wall_right);
		cages[10][28] = new Cage(10, 28, false, wall_right);
		cages[10][29] = new Cage(10, 29, false, corner_and_wall_up_right);
		cages[10][30] = new Cage(10, 30, true, floor);
		cages[10][31] = new Cage(10, 31, false, wall_down);

		cages[11][0] = new Cage(11, 0, false, wall_up);
		cages[11][1] = new Cage(11, 1,true, floor);
		cages[11][2] = new Cage(11, 2,true, floor);
		cages[11][3] = new Cage(11, 3,true, floor);
		cages[11][4] = new Cage(11, 4,true, floor);
		cages[11][5] = new Cage(11, 5,true, floor);
		cages[11][6] = new Cage(11, 6,true, floor);
		cages[11][7] = new Cage(11, 7, true, floor);
		cages[11][8] = new Cage(11, 8, true, floor);
		cages[11][9] = new Cage(11, 9, false, wall_down);
		cages[11][10] = new Cage(11, 10, false, wall_up);
		cages[11][11] = new Cage(11, 11, true, floor);
		cages[11][12] = new Cage(11, 12, false, wall_down);
		cages[11][13] = new Cage(11, 13, false, wall_up);
		cages[11][14] = new Cage(11, 14, true, floor);
		cages[11][15] = new Cage(11, 15, true, floor);
		cages[11][16] = new Cage(11, 16, true, floor);
		cages[11][17] = new Cage(11, 17, false, closed_horizontal_door);
		cages[11][18] = new Cage(11, 18, true, floor);
		cages[11][19] = new Cage(11, 19, true, floor);
		cages[11][20] = new Cage(11, 20, true, floor);
		cages[11][21] = new Cage(11, 21, true, floor);
		cages[11][22] = new Cage(11, 22, true, floor);
		cages[11][23] = new Cage(11, 23, true, floor);
		cages[11][24] = new Cage(11, 24, true, floor);
		cages[11][25] = new Cage(11, 25, true, floor);
		cages[11][26] = new Cage(11, 26, true, floor);
		cages[11][27] = new Cage(11, 27, true, floor);
		cages[11][28] = new Cage(11, 28, true, floor);
		cages[11][29] = new Cage(11, 29, true, floor);
		cages[11][30] = new Cage(11, 30, true, floor);
		cages[11][31] = new Cage(11, 31, false, wall_down);

		cages[12][0] = new Cage(12, 0, false, corner_left_up);
		cages[12][1] = new Cage(12, 1,false, wall_left);
		cages[12][2] = new Cage(12, 2,false, wall_left);
		cages[12][3] = new Cage(12, 3,false, corner_and_wall_up_left);
		cages[12][4] = new Cage(12, 4,true, floor);
		cages[12][5] = new Cage(12, 5,false, corner_and_wall_down_left);
		cages[12][6] = new Cage(12, 6,false, wall_left);
		cages[12][7] = new Cage(12, 7, false, wall_left);
		cages[12][8] = new Cage(12, 8, false, wall_left);
		cages[12][9] = new Cage(12, 9, false, corner_left_down);
		cages[12][10] = new Cage(12, 10, false, wall_up);
		cages[12][11] = new Cage(12, 11, true, floor);
		cages[12][12] = new Cage(12, 12, false, wall_down);
		cages[12][13] = new Cage(12, 13, false, corner_left_up);
		cages[12][14] = new Cage(12, 14, false, wall_left);
		cages[12][15] = new Cage(12, 15, false, wall_left);
		cages[12][16] = new Cage(12, 16, false, wall_left);
		cages[12][17] = new Cage(12, 17, false, corner_and_wall_up_left);
		cages[12][18] = new Cage(12, 18, true, floor);
		cages[12][19] = new Cage(12, 19, false, corner_and_wall_down_left);
		cages[12][20] = new Cage(12, 20, false, wall_left);
		cages[12][21] = new Cage(12, 21, false, wall_left);
		cages[12][22] = new Cage(12, 22, false, wall_left);
		cages[12][23] = new Cage(12, 23, false, wall_left);
		cages[12][24] = new Cage(12, 24, false, wall_left);
		cages[12][25] = new Cage(12, 25, false, wall_left);
		cages[12][26] = new Cage(12, 26, false, wall_left);
		cages[12][27] = new Cage(12, 27, false, wall_left);
		cages[12][28] = new Cage(12, 28, false, wall_left);
		cages[12][29] = new Cage(12, 29, false, corner_and_wall_up_left);
		cages[12][30] = new Cage(12, 30, true, floor);
		cages[12][31] = new Cage(12, 31, false, wall_down);

		cages[13][0] = new Cage(13, 0, false, corner_right_up);
		cages[13][1] = new Cage(13, 1,false, wall_right);
		cages[13][2] = new Cage(13, 2,false, wall_right);
		cages[13][3] = new Cage(13, 3,false, corner_and_wall_up_right);
		cages[13][4] = new Cage(13, 4,true, floor);
		cages[13][5] = new Cage(13, 5,false, wall_down);
		cages[13][6] = new Cage(13, 6,false, corner_right_up);
		cages[13][7] = new Cage(13, 7, false, wall_right);
		cages[13][8] = new Cage(13, 8, false, wall_right);
		cages[13][9] = new Cage(13, 9, false, wall_right);
		cages[13][10] = new Cage(13, 10, false, corner_and_wall_up_right);
		cages[13][11] = new Cage(13, 11, true, floor);
		cages[13][12] = new Cage(13, 12, false, corner_and_wall_down_right);
		cages[13][13] = new Cage(13, 13, false, wall_right);
		cages[13][14] = new Cage(13, 14, false, wall_right);
		cages[13][15] = new Cage(13, 15, false, wall_right);
		cages[13][16] = new Cage(13, 16, false, corner_right_down);
		cages[13][17] = new Cage(13, 17, false, wall_up);
		cages[13][18] = new Cage(13, 18, true, floor);
		cages[13][19] = new Cage(13, 19, false, wall_down);
		cages[13][20] = new Cage(13, 20, false, corner_right_up);
		cages[13][21] = new Cage(13, 21, false, wall_right);
		cages[13][22] = new Cage(13, 22, false, wall_right);
		cages[13][23] = new Cage(13, 23, false, wall_right);
		cages[13][24] = new Cage(13, 24, false, wall_right);
		cages[13][25] = new Cage(13, 25, false, wall_right);
		cages[13][26] = new Cage(13, 26, false, wall_right);
		cages[13][27] = new Cage(13, 27, false, wall_right);
		cages[13][28] = new Cage(13, 28, false, wall_right);
		cages[13][29] = new Cage(13, 29, false, corner_and_wall_up_right);
		cages[13][30] = new Cage(13, 30, true, floor);
		cages[13][31] = new Cage(13, 31, false, wall_down);

		cages[14][0] = new Cage(14, 0, false, wall_up);
		cages[14][1] = new Cage(14, 1,false, floor);
		cages[14][2] = new Cage(14, 2,true, floor);
		cages[14][3] = new Cage(14, 3,false, closed_horizontal_door);
		cages[14][4] = new Cage(14, 4,true, floor);
		cages[14][5] = new Cage(14, 5,false, wall_down);
		cages[14][6] = new Cage(14, 6,false, wall_up);
		cages[14][7] = new Cage(14, 7, true, floor);
		cages[14][8] = new Cage(14, 8, true, floor);
		cages[14][9] = new Cage(14, 9, true, floor);
		cages[14][10] = new Cage(14, 10, true, floor);
		cages[14][11] = new Cage(14, 11, true, floor);
		cages[14][12] = new Cage(14, 12, true, floor);
		cages[14][13] = new Cage(14, 13, true, floor);
		cages[14][14] = new Cage(14, 14, true, floor);
		cages[14][15] = new Cage(14, 15, true, floor);
		cages[14][16] = new Cage(14, 16, false, wall_down);
		cages[14][17] = new Cage(14, 17, false, wall_up);
		cages[14][18] = new Cage(14, 18, true, floor);
		cages[14][19] = new Cage(14, 19, false, wall_down);
		cages[14][20] = new Cage(14, 20, false, wall_up);
		cages[14][21] = new Cage(14, 21, true, floor);
		cages[14][22] = new Cage(14, 22, true, floor);
		cages[14][23] = new Cage(14, 23, true, floor);
		cages[14][24] = new Cage(14, 24, true, floor);
		cages[14][25] = new Cage(14, 25, true, floor);
		cages[14][26] = new Cage(14, 26, true, floor);
		cages[14][27] = new Cage(14, 27, true, floor);
		cages[14][28] = new Cage(14, 28, true, floor);
		cages[14][29] = new Cage(14, 29, true, floor);
		cages[14][30] = new Cage(14, 30, true, floor);
		cages[14][31] = new Cage(14, 31, false, wall_down);

		cages[15][0] = new Cage(15, 0, false, corner_left_up);
		cages[15][1] = new Cage(15, 1,false, wall_left);
		cages[15][2] = new Cage(15, 2,false, wall_left);
		cages[15][3] = new Cage(15, 3,false, corner_and_wall_up_left);
		cages[15][4] = new Cage(15, 4,true, floor);
		cages[15][5] = new Cage(15, 5,false, wall_down);
		cages[15][6] = new Cage(15, 6,false, wall_up);
		cages[15][7] = new Cage(15, 7, true, floor);
		cages[15][8] = new Cage(15, 8, false, column);
		cages[15][9] = new Cage(15, 9, true, floor);
		cages[15][10] = new Cage(15, 10, true, floor);
		cages[15][11] = new Cage(15, 11, true, floor);
		cages[15][12] = new Cage(15, 12, true, floor);
		cages[15][13] = new Cage(15, 13, true, floor);
		cages[15][14] = new Cage(15, 14, false, column);
		cages[15][15] = new Cage(15, 15, true, floor);
		cages[15][16] = new Cage(15, 16, false, wall_down);
		cages[15][17] = new Cage(15, 17, false, wall_up);
		cages[15][18] = new Cage(15, 18, true, floor);
		cages[15][19] = new Cage(15, 19, false, wall_down);
		cages[15][20] = new Cage(15, 20, false, wall_up);
		cages[15][21] = new Cage(15, 21, true, floor);
		cages[15][22] = new Cage(15, 22, false, corner_and_wall_down_left);
		cages[15][23] = new Cage(15, 23, false, wall_left);
		cages[15][24] = new Cage(15, 24, false, wall_left);
		cages[15][25] = new Cage(15, 25, false, wall_left);
		cages[15][26] = new Cage(15, 26, false, corner_and_wall_up_left);
		cages[15][27] = new Cage(15, 27, false, floor);
		cages[15][28] = new Cage(15, 28, false, corner_and_wall_down_left);
		cages[15][29] = new Cage(15, 29, false, corner_and_wall_up_left);
		cages[15][30] = new Cage(15, 30, true, floor);
		cages[15][31] = new Cage(15, 31, false, wall_down);

		cages[16][0] = new Cage(16, 0, false, corner_right_up);
		cages[16][1] = new Cage(16, 1,false, wall_right);
		cages[16][2] = new Cage(16, 2,false, wall_right);
		cages[16][3] = new Cage(16, 3,false, corner_and_wall_up_right);
		cages[16][4] = new Cage(16, 4,true, floor);
		cages[16][5] = new Cage(16, 5,false, wall_down);
		cages[16][6] = new Cage(16, 6,false, wall_up);
		cages[16][7] = new Cage(16, 7, true, floor);
		cages[16][8] = new Cage(16, 8, true, floor);
		cages[16][9] = new Cage(16, 9, true, floor);
		cages[16][10] = new Cage(16, 10, true, floor);
		cages[16][11] = new Cage(16, 11, true, floor);
		cages[16][12] = new Cage(16, 12, true, floor);
		cages[16][13] = new Cage(16, 13, true, floor);
		cages[16][14] = new Cage(16, 14, true, floor);
		cages[16][15] = new Cage(16, 15, true, floor);
		cages[16][16] = new Cage(16, 16, false, wall_down);
		cages[16][17] = new Cage(16, 17, false, wall_up);
		cages[16][18] = new Cage(16, 18, true, floor);
		cages[16][19] = new Cage(16, 19, false, wall_down);
		cages[16][20] = new Cage(16, 20, false, wall_up);
		cages[16][21] = new Cage(16, 21, true, floor);
		cages[16][22] = new Cage(16, 22, false, wall_down);
		cages[16][23] = new Cage(16, 23, false, corner_right_up);
		cages[16][24] = new Cage(16, 24, false, wall_right);
		cages[16][25] = new Cage(16, 25, false, corner_right_down);
		cages[16][26] = new Cage(16, 26, false, corner_left_up);
		cages[16][27] = new Cage(16, 27, false, wall_left);
		cages[16][28] = new Cage(16, 28, false, corner_left_down);
		cages[16][29] = new Cage(16, 29, false, wall_up);
		cages[16][30] = new Cage(16, 30, true, floor);
		cages[16][31] = new Cage(16, 31, false, wall_down);

		cages[17][0] = new Cage(17, 0, false, wall_up);
		cages[17][1] = new Cage(17, 1,false, floor);
		cages[17][2] = new Cage(17, 2,true, floor);
		cages[17][3] = new Cage(17, 3,false, closed_horizontal_door);
		cages[17][4] = new Cage(17, 4,true, floor);
		cages[17][5] = new Cage(17, 5,false, corner_and_wall_down_right);
		cages[17][6] = new Cage(17, 6,false, corner_and_wall_up_right);
		cages[17][7] = new Cage(17, 7, true, floor);
		cages[17][8] = new Cage(17, 8, false, column);
		cages[17][9] = new Cage(17, 9, true, floor);
		cages[17][10] = new Cage(17, 10, true, floor);
		cages[17][11] = new Cage(17, 11, true, floor);
		cages[17][12] = new Cage(17, 12, true, floor);
		cages[17][13] = new Cage(17, 13, true, floor);
		cages[17][14] = new Cage(17, 14, false, column);
		cages[17][15] = new Cage(17, 15, true, floor);
		cages[17][16] = new Cage(17, 16, false, corner_and_wall_down_right);
		cages[17][17] = new Cage(17, 17, false, corner_and_wall_up_right);
		cages[17][18] = new Cage(17, 18, true, floor);
		cages[17][19] = new Cage(17, 19, false, wall_down);
		cages[17][20] = new Cage(17, 20, false, wall_up);
		cages[17][21] = new Cage(17, 21, true, floor);
		cages[17][22] = new Cage(17, 22, false, corner_and_wall_down_right);
		cages[17][23] = new Cage(17, 23, false, corner_and_wall_up_right);
		cages[17][24] = new Cage(17, 24, false, floor);
		cages[17][25] = new Cage(17, 25, false, corner_and_wall_down_right);
		cages[17][26] = new Cage(17, 26, false, wall_right);
		cages[17][27] = new Cage(17, 27, false, wall_right);
		cages[17][28] = new Cage(17, 28, false, wall_right);
		cages[17][29] = new Cage(17, 29, false, corner_and_wall_up_right);
		cages[17][30] = new Cage(17, 30, true, floor);
		cages[17][31] = new Cage(17, 31, false, wall_down);

		cages[18][0] = new Cage(18, 0, false, corner_left_up);
		cages[18][1] = new Cage(18, 1,false, wall_left);
		cages[18][2] = new Cage(18, 2,false, wall_left);
		cages[18][3] = new Cage(18, 3,false, corner_and_wall_up_left);
		cages[18][4] = new Cage(18, 4,true, floor);
		cages[18][5] = new Cage(18, 5,true, floor);
		cages[18][6] = new Cage(18, 6,true, floor);
		cages[18][7] = new Cage(18, 7, true, floor);
		cages[18][8] = new Cage(18, 8, true, floor);
		cages[18][9] = new Cage(18, 9, true, floor);
		cages[18][10] = new Cage(18, 10, true, floor);
		cages[18][11] = new Cage(18, 11, true, floor);
		cages[18][12] = new Cage(18, 12, true, floor);
		cages[18][13] = new Cage(18, 13, true, floor);
		cages[18][14] = new Cage(18, 14, true, floor);
		cages[18][15] = new Cage(18, 15, true, floor);
		cages[18][16] = new Cage(18, 16, false, closed_horizontal_door);
		cages[18][17] = new Cage(18, 17, true, floor);
		cages[18][18] = new Cage(18, 18, true, floor);
		cages[18][19] = new Cage(18, 19, false, wall_down);
		cages[18][20] = new Cage(18, 20, false, wall_up);
		cages[18][21] = new Cage(18, 21, true, floor);
		cages[18][22] = new Cage(18, 22, true, floor);
		cages[18][23] = new Cage(18, 23, true, floor);
		cages[18][24] = new Cage(18, 24, true, floor);
		cages[18][25] = new Cage(18, 25, true, floor);
		cages[18][26] = new Cage(18, 26, true, floor);
		cages[18][27] = new Cage(18, 27, true, floor);
		cages[18][28] = new Cage(18, 28, true, floor);
		cages[18][29] = new Cage(18, 29, true, floor);
		cages[18][30] = new Cage(18, 30, true, floor);
		cages[18][31] = new Cage(18, 31, false, wall_down);

		cages[19][0] = new Cage(19, 0, false, corner_right_up);
		cages[19][1] = new Cage(19, 1,false, wall_right);
		cages[19][2] = new Cage(19, 2,false, wall_right);
		cages[19][3] = new Cage(19, 3,false, corner_and_wall_up_right);
		cages[19][4] = new Cage(19, 4,true, floor);
		cages[19][5] = new Cage(19, 5,false, corner_and_wall_down_left);
		cages[19][6] = new Cage(19, 6,false, corner_and_wall_up_left);
		cages[19][7] = new Cage(19, 7, true, floor);
		cages[19][8] = new Cage(19, 8, false, column);
		cages[19][9] = new Cage(19, 9, true, floor);
		cages[19][10] = new Cage(19, 10, true, floor);
		cages[19][11] = new Cage(19, 11, true, floor);
		cages[19][12] = new Cage(19, 12, true, floor);
		cages[19][13] = new Cage(19, 13, true, floor);
		cages[19][14] = new Cage(19, 14, false, column);
		cages[19][15] = new Cage(19, 15, true, floor);
		cages[19][16] = new Cage(19, 16, false, corner_and_wall_down_left);
		cages[19][17] = new Cage(19, 17, false, corner_and_wall_up_left);
		cages[19][18] = new Cage(19, 18, true, floor);
		cages[19][19] = new Cage(19, 19, false, wall_down);
		cages[19][20] = new Cage(19, 20, false, corner_left_up);
		cages[19][21] = new Cage(19, 21, false, wall_left);
		cages[19][22] = new Cage(19, 22, false, wall_left);
		cages[19][23] = new Cage(19, 23, false, wall_left);
		cages[19][24] = new Cage(19, 24, false, wall_left);
		cages[19][25] = new Cage(19, 25, false, wall_left);
		cages[19][26] = new Cage(19, 26, false, wall_left);
		cages[19][27] = new Cage(19, 27, false, wall_left);
		cages[19][28] = new Cage(19, 28, false, wall_left);
		cages[19][29] = new Cage(19, 29, false, corner_and_wall_up_left);
		cages[19][30] = new Cage(19, 30, true, floor);
		cages[19][31] = new Cage(19, 31, false, wall_down);

		cages[20][0] = new Cage(20, 0, false, wall_up);
		cages[20][1] = new Cage(20, 1,false, floor);
		cages[20][2] = new Cage(20, 2,true, floor);
		cages[20][3] = new Cage(20, 3,false, closed_horizontal_door);
		cages[20][4] = new Cage(20, 4,true, floor);
		cages[20][5] = new Cage(20, 5,false, wall_down);
		cages[20][6] = new Cage(20, 6,false, wall_up);
		cages[20][7] = new Cage(20, 7, true, floor);
		cages[20][8] = new Cage(20, 8, true, floor);
		cages[20][9] = new Cage(20, 9, true, floor);
		cages[20][10] = new Cage(20, 10, true, floor);
		cages[20][11] = new Cage(20, 11, true, floor);
		cages[20][12] = new Cage(20, 12, true, floor);
		cages[20][13] = new Cage(20, 13, true, floor);
		cages[20][14] = new Cage(20, 14, true, floor);
		cages[20][15] = new Cage(20, 15, true, floor);
		cages[20][16] = new Cage(20, 16, false, wall_down);
		cages[20][17] = new Cage(20, 17, false, wall_up);
		cages[20][18] = new Cage(20, 18, true, floor);
		cages[20][19] = new Cage(20, 19, false, corner_and_wall_down_right);
		cages[20][20] = new Cage(20, 20, false, wall_right);
		cages[20][21] = new Cage(20, 21, false, wall_right);
		cages[20][22] = new Cage(20, 22, false, wall_right);
		cages[20][23] = new Cage(20, 23, false, wall_right);
		cages[20][24] = new Cage(20, 24, false, wall_right);
		cages[20][25] = new Cage(20, 25, false, wall_right);
		cages[20][26] = new Cage(20, 26, false, wall_right);
		cages[20][27] = new Cage(20, 27, false, wall_right);
		cages[20][28] = new Cage(20, 28, false, wall_right);
		cages[20][29] = new Cage(20, 29, false, corner_and_wall_up_right);
		cages[20][30] = new Cage(20, 30, true, floor);
		cages[20][31] = new Cage(20, 31, false, wall_down);

		cages[21][0] = new Cage(21, 0, false, corner_left_up);
		cages[21][1] = new Cage(21, 1,false, wall_left);
		cages[21][2] = new Cage(21, 2,false, wall_left);
		cages[21][3] = new Cage(21, 3,false, corner_and_wall_up_left);
		cages[21][4] = new Cage(21, 4,true, floor);
		cages[21][5] = new Cage(21, 5,false, wall_down);
		cages[21][6] = new Cage(21, 6,false, wall_up);
		cages[21][7] = new Cage(21, 7, true, floor);
		cages[21][8] = new Cage(21, 8, false, column);
		cages[21][9] = new Cage(21, 9, true, floor);
		cages[21][10] = new Cage(21, 10, true, floor);
		cages[21][11] = new Cage(21, 11, true, floor);
		cages[21][12] = new Cage(21, 12, true, floor);
		cages[21][13] = new Cage(21, 13, true, floor);
		cages[21][14] = new Cage(21, 14, false, column);
		cages[21][15] = new Cage(21, 15, true, floor);
		cages[21][16] = new Cage(21, 16, false, wall_down);
		cages[21][17] = new Cage(21, 17, false, wall_up);
		cages[21][18] = new Cage(21, 18, true, floor);
		cages[21][19] = new Cage(21, 19, true, floor);
		cages[21][20] = new Cage(21, 20, true, floor);
		cages[21][21] = new Cage(21, 21, true, floor);
		cages[21][22] = new Cage(21, 22, true, floor);
		cages[21][23] = new Cage(21, 23, true, floor);
		cages[21][24] = new Cage(21, 24, true, floor);
		cages[21][25] = new Cage(21, 25, true, floor);
		cages[21][26] = new Cage(21, 26, true, floor);
		cages[21][27] = new Cage(21, 27, true, floor);
		cages[21][28] = new Cage(21, 28, true, floor);
		cages[21][29] = new Cage(21, 29, true, floor);
		cages[21][30] = new Cage(21, 30, true, floor);
		cages[21][31] = new Cage(21, 31, false, wall_down);

		cages[22][0] = new Cage(22, 0, false, corner_right_up);
		cages[22][1] = new Cage(22, 1,false, wall_right);
		cages[22][2] = new Cage(22, 2,false, wall_right);
		cages[22][3] = new Cage(22, 3,false, corner_and_wall_up_right);
		cages[22][4] = new Cage(22, 4,true, floor);
		cages[22][5] = new Cage(22, 5,false, wall_down);
		cages[22][6] = new Cage(22, 6,false, wall_up);
		cages[22][7] = new Cage(22, 7, true, floor);
		cages[22][8] = new Cage(22, 8, true, floor);
		cages[22][9] = new Cage(22, 9, true, floor);
		cages[22][10] = new Cage(22, 10, true, floor);
		cages[22][11] = new Cage(22, 11, true, floor);
		cages[22][12] = new Cage(22, 12, true, floor);
		cages[22][13] = new Cage(22, 13, true, floor);
		cages[22][14] = new Cage(22, 14, true, floor);
		cages[22][15] = new Cage(22, 15, true, floor);
		cages[22][16] = new Cage(22, 16, false, wall_down);
		cages[22][17] = new Cage(22, 17, false, corner_left_up);
		cages[22][18] = new Cage(22, 18, false, wall_left);
		cages[22][19] = new Cage(22, 19, false, wall_left);
		cages[22][20] = new Cage(22, 20, false, wall_left);
		cages[22][21] = new Cage(22, 21, false, wall_left);
		cages[22][22] = new Cage(22, 22, false, wall_left);
		cages[22][23] = new Cage(22, 23, false, wall_left);
		cages[22][24] = new Cage(22, 24, false, wall_left);
		cages[22][25] = new Cage(22, 25, false, wall_left);
		cages[22][26] = new Cage(22, 26, false, wall_left);
		cages[22][27] = new Cage(22, 27, false, wall_left);
		cages[22][28] = new Cage(22, 28, false, wall_left);
		cages[22][29] = new Cage(22, 29, false, wall_left);
		cages[22][30] = new Cage(22, 30, false, wall_left);
		cages[22][31] = new Cage(22, 31, false, corner_left_down);

		cages[23][0] = new Cage(23, 0, false, wall_up);
		cages[23][1] = new Cage(23, 1,true, floor);
		cages[23][2] = new Cage(23, 2,true, floor);
		cages[23][3] = new Cage(23, 3,true, floor);
		cages[23][4] = new Cage(23, 4,true, floor);
		cages[23][5] = new Cage(23, 5,false, wall_down);
		cages[23][6] = new Cage(23, 6,false, corner_left_up);
		cages[23][7] = new Cage(23, 7, false, wall_left);
		cages[23][8] = new Cage(23, 8, false, wall_left);
		cages[23][9] = new Cage(23, 9, false, wall_left);
		cages[23][10] = new Cage(23, 10, false, corner_and_wall_up_left);
		cages[23][11] = new Cage(23, 11, false, closed_vertical_door);
		cages[23][12] = new Cage(23, 12, false, corner_and_wall_down_left);
		cages[23][13] = new Cage(23, 13, false, wall_left);
		cages[23][14] = new Cage(23, 14, false, wall_left);
		cages[23][15] = new Cage(23, 15, false, wall_left);
		cages[23][16] = new Cage(23, 16, false, corner_left_down);
		cages[23][17] = new Cage(23, 17, false, corner_right_up);
		cages[23][18] = new Cage(23, 18, false, wall_right);
		cages[23][19] = new Cage(23, 19, false, wall_right);
		cages[23][20] = new Cage(23, 20, false, wall_right);
		cages[23][21] = new Cage(23, 21, false, wall_right);
		cages[23][22] = new Cage(23, 22, false, wall_right);
		cages[23][23] = new Cage(23, 23, false, wall_right);
		cages[23][24] = new Cage(23, 24, false, wall_right);
		cages[23][25] = new Cage(23, 25, false, wall_right);
		cages[23][26] = new Cage(23, 26, false, wall_right);
		cages[23][27] = new Cage(23, 27, false, wall_right);
		cages[23][28] = new Cage(23, 28, false, wall_right);
		cages[23][29] = new Cage(23, 29, false, wall_right);
		cages[23][30] = new Cage(23, 30, false, wall_right);
		cages[23][31] = new Cage(23, 31, false, corner_right_down);

		cages[24][0] = new Cage(24, 0, false, corner_left_up);
		cages[24][1] = new Cage(24, 1,false, wall_left);
		cages[24][2] = new Cage(24, 2,false, wall_left);
		cages[24][3] = new Cage(24, 3,false, wall_left);
		cages[24][4] = new Cage(24, 4,false, wall_left);
		cages[24][5] = new Cage(24, 5,false, corner_left_down);
		cages[24][6] = new Cage(24, 6,false, space);
		cages[24][7] = new Cage(24, 7, false, space);
		cages[24][8] = new Cage(24, 8, false, space);
		cages[24][9] = new Cage(24, 9, false, space);
		cages[24][10] = new Cage(24, 10, false, wall_up);
		cages[24][11] = new Cage(24, 11, true, floor);
		cages[24][12] = new Cage(24, 12, false, wall_down);
		cages[24][13] = new Cage(24, 13, false, space);
		cages[24][14] = new Cage(24, 14, false, space);
		cages[24][15] = new Cage(24, 15, false, space);
		cages[24][16] = new Cage(24, 16, false, space);
		cages[24][17] = new Cage(24, 17, false, wall_up);
		cages[24][18] = new Cage(24, 18, true, floor);
		cages[24][19] = new Cage(24, 19, true, floor);
		cages[24][20] = new Cage(24, 20, true, floor);
		cages[24][21] = new Cage(24, 21, true, floor);
		cages[24][22] = new Cage(24, 22, true, floor);
		cages[24][23] = new Cage(24, 23, true, floor);
		cages[24][24] = new Cage(24, 24, true, floor);
		cages[24][25] = new Cage(24, 25, true, floor);
		cages[24][26] = new Cage(24, 26, true, floor);
		cages[24][27] = new Cage(24, 27, true, floor);
		cages[24][28] = new Cage(24, 28, true, floor);
		cages[24][29] = new Cage(24, 29, true, floor);
		cages[24][30] = new Cage(24, 30, true, floor);
		cages[24][31] = new Cage(24, 31, false, wall_down);

		cages[25][0] = new Cage(25, 0, false, corner_right_up);
		cages[25][1] = new Cage(25, 1,false, wall_right);
		cages[25][2] = new Cage(25, 2,false, wall_right);
		cages[25][3] = new Cage(25, 3,false, wall_right);
		cages[25][4] = new Cage(25, 4,false, wall_right);
		cages[25][5] = new Cage(25, 5,false, corner_right_down);
		cages[25][6] = new Cage(25, 6,false, space);
		cages[25][7] = new Cage(25, 7, false, space);
		cages[25][8] = new Cage(25, 8, false, space);
		cages[25][9] = new Cage(25, 9, false, space);
		cages[25][10] = new Cage(25, 10, false, wall_up);
		cages[25][11] = new Cage(25, 11, true, floor);
		cages[25][12] = new Cage(25, 12, false, wall_down);
		cages[25][13] = new Cage(25, 13, false, space);
		cages[25][14] = new Cage(25, 14, false, space);
		cages[25][15] = new Cage(25, 15, false, space);
		cages[25][16] = new Cage(25, 16, false, space);
		cages[25][17] = new Cage(25, 17, false, wall_up);
		cages[25][18] = new Cage(25, 18, true, floor);
		cages[25][19] = new Cage(25, 19, true, floor);
		cages[25][20] = new Cage(25, 20, true, floor);
		cages[25][21] = new Cage(25, 21, true, floor);
		cages[25][22] = new Cage(25, 22, true, floor);
		cages[25][23] = new Cage(25, 23, true, floor);
		cages[25][24] = new Cage(25, 24, true, floor);
		cages[25][25] = new Cage(25, 25, true, floor);
		cages[25][26] = new Cage(25, 26, true, floor);
		cages[25][27] = new Cage(25, 27, true, floor);
		cages[25][28] = new Cage(25, 28, true, floor);
		cages[25][29] = new Cage(25, 29, true, floor);
		cages[25][30] = new Cage(25, 30, true, floor);
		cages[25][31] = new Cage(25, 31, false, wall_down);

		cages[26][0] = new Cage(26, 0, false, wall_up);
		cages[26][1] = new Cage(26, 1,true, floor);
		cages[26][2] = new Cage(26, 2,true, floor);
		cages[26][3] = new Cage(26, 3,true, floor);
		cages[26][4] = new Cage(26, 4,true, floor);
		cages[26][5] = new Cage(26, 5,false, corner_and_wall_down_right);
		cages[26][6] = new Cage(26, 6,false, wall_right);
		cages[26][7] = new Cage(26, 7, false, wall_right);
		cages[26][8] = new Cage(26, 8, false, wall_right);
		cages[26][9] = new Cage(26, 9, false, wall_right);
		cages[26][10] = new Cage(26, 10, false, corner_and_wall_up_right);
		cages[26][11] = new Cage(26, 11, true, floor);
		cages[26][12] = new Cage(26, 12, false, corner_and_wall_down_right);
		cages[26][13] = new Cage(26, 13, false, wall_right);
		cages[26][14] = new Cage(26, 14, false, wall_right);
		cages[26][15] = new Cage(26, 15, false, wall_right);
		cages[26][16] = new Cage(26, 16, false, wall_right);
		cages[26][17] = new Cage(26, 17, false, corner_and_wall_up_right);
		cages[26][18] = new Cage(26, 18, true, floor);
		cages[26][19] = new Cage(26, 19, true, floor);
		cages[26][20] = new Cage(26, 20, true, floor);
		cages[26][21] = new Cage(26, 21, true, floor);
		cages[26][22] = new Cage(26, 22, true, floor);
		cages[26][23] = new Cage(26, 23, true, floor);
		cages[26][24] = new Cage(26, 24, true, floor);
		cages[26][25] = new Cage(26, 25, true, floor);
		cages[26][26] = new Cage(26, 26, true, floor);
		cages[26][27] = new Cage(26, 27, true, floor);
		cages[26][28] = new Cage(26, 28, true, floor);
		cages[26][29] = new Cage(26, 29, true, floor);
		cages[26][30] = new Cage(26, 30, true, floor);
		cages[26][31] = new Cage(26, 31, false, wall_down);

		cages[27][0] = new Cage(27, 0, true, floor);
		cages[27][1] = new Cage(27, 1,true, floor);
		cages[27][2] = new Cage(27, 2,true, floor);
		cages[27][3] = new Cage(27, 3,true, floor);
		cages[27][4] = new Cage(27, 4,true, floor);
		cages[27][5] = new Cage(27, 5,true, floor);
		cages[27][6] = new Cage(27, 6,true, floor);
		cages[27][7] = new Cage(27, 7, true, floor);
		cages[27][8] = new Cage(27, 8, true, floor);
		cages[27][9] = new Cage(27, 9, true, floor);
		cages[27][10] = new Cage(27, 10, true, floor);
		cages[27][11] = new Cage(27, 11, true, floor);
		cages[27][12] = new Cage(27, 12, true, floor);
		cages[27][13] = new Cage(27, 13, true, floor);
		cages[27][14] = new Cage(27, 14, true, floor);
		cages[27][15] = new Cage(27, 15, true, floor);
		cages[27][16] = new Cage(27, 16, true, floor);
		cages[27][17] = new Cage(27, 17, true, floor);
		cages[27][18] = new Cage(27, 18, true, floor);
		cages[27][19] = new Cage(27, 19, true, floor);
		cages[27][20] = new Cage(27, 20, true, floor);
		cages[27][21] = new Cage(27, 21, true, floor);
		cages[27][22] = new Cage(27, 22, true, floor);
		cages[27][23] = new Cage(27, 23, true, floor);
		cages[27][24] = new Cage(27, 24, true, floor);
		cages[27][25] = new Cage(27, 25, true, floor);
		cages[27][26] = new Cage(27, 26, true, floor);
		cages[27][27] = new Cage(27, 27, true, floor);
		cages[27][28] = new Cage(27, 28, true, floor);
		cages[27][29] = new Cage(27, 29, true, floor);
		cages[27][30] = new Cage(27, 30, true, floor);
		cages[27][31] = new Cage(27, 31, false, wall_down);

		cages[28][0] = new Cage(28, 0, false, wall_up);
		cages[28][1] = new Cage(28, 1,true, floor);
		cages[28][2] = new Cage(28, 2,true, floor);
		cages[28][3] = new Cage(28, 3,true, floor);
		cages[28][4] = new Cage(28, 4,true, floor);
		cages[28][5] = new Cage(28, 5,false, corner_and_wall_down_left);
		cages[28][6] = new Cage(28, 6,false, wall_left);
		cages[28][7] = new Cage(28, 7, false, wall_left);
		cages[28][8] = new Cage(28, 8, false, wall_left);
		cages[28][9] = new Cage(28, 9, false, wall_left);
		cages[28][10] = new Cage(28, 10, false, corner_and_wall_up_left);
		cages[28][11] = new Cage(28, 11, true, floor);
		cages[28][12] = new Cage(28, 12, false, corner_and_wall_down_left);
		cages[28][13] = new Cage(28, 13, false, wall_left);
		cages[28][14] = new Cage(28, 14, false, wall_left);
		cages[28][15] = new Cage(28, 15, false, wall_left);
		cages[28][16] = new Cage(28, 16, false, wall_left);
		cages[28][17] = new Cage(28, 17, false, corner_and_wall_up_left);
		cages[28][18] = new Cage(28, 18, true, floor);
		cages[28][19] = new Cage(28, 19, true, floor);
		cages[28][20] = new Cage(28, 20, true, floor);
		cages[28][21] = new Cage(28, 21, true, floor);
		cages[28][22] = new Cage(28, 22, true, floor);
		cages[28][23] = new Cage(28, 23, true, floor);
		cages[28][24] = new Cage(28, 24, true, floor);
		cages[28][25] = new Cage(28, 25, true, floor);
		cages[28][26] = new Cage(28, 26, true, floor);
		cages[28][27] = new Cage(28, 27, true, floor);
		cages[28][28] = new Cage(28, 28, true, floor);
		cages[28][29] = new Cage(28, 29, true, floor);
		cages[28][30] = new Cage(28, 30, true, floor);
		cages[28][31] = new Cage(28, 31, false, wall_down);

		cages[29][0] = new Cage(29, 0, false, corner_left_up);
		cages[29][1] = new Cage(29, 1,false, wall_left);
		cages[29][2] = new Cage(29, 2,false, wall_left);
		cages[29][3] = new Cage(29, 3,false, wall_left);
		cages[29][4] = new Cage(29, 4,false, wall_left);
		cages[29][5] = new Cage(29, 5,false, corner_left_down);
		cages[29][6] = new Cage(29, 6,false, corner_right_up);
		cages[29][7] = new Cage(29, 7, false, wall_right);
		cages[29][8] = new Cage(29, 8, false, wall_right);
		cages[29][9] = new Cage(29, 9, false, wall_right);
		cages[29][10] = new Cage(29, 10, false, corner_and_wall_up_right);
		cages[29][11] = new Cage(29, 11, true, floor);
		cages[29][12] = new Cage(29, 12, false, corner_and_wall_down_right);
		cages[29][13] = new Cage(29, 13, false, wall_right);
		cages[29][14] = new Cage(29, 14, false, wall_right);
		cages[29][15] = new Cage(29, 15, false, wall_right);
		cages[29][16] = new Cage(29, 16, false, corner_right_down);
		cages[29][17] = new Cage(29, 17, false, wall_up);
		cages[29][18] = new Cage(29, 18, true, floor);
		cages[29][19] = new Cage(29, 19, true, floor);
		cages[29][20] = new Cage(29, 20, true, floor);
		cages[29][21] = new Cage(29, 21, true, floor);
		cages[29][22] = new Cage(29, 22, true, floor);
		cages[29][23] = new Cage(29, 23, true, floor);
		cages[29][24] = new Cage(29, 24, true, floor);
		cages[29][25] = new Cage(29, 25, true, floor);
		cages[29][26] = new Cage(29, 26, true, floor);
		cages[29][27] = new Cage(29, 27, true, floor);
		cages[29][28] = new Cage(29, 28, true, floor);
		cages[29][29] = new Cage(29, 29, true, floor);
		cages[29][30] = new Cage(29, 30, true, floor);
		cages[29][31] = new Cage(29, 31, false, wall_down);

		cages[30][0] = new Cage(30, 0, false, space);
		cages[30][1] = new Cage(30, 1,false, space);
		cages[30][2] = new Cage(30, 2,false, space);
		cages[30][3] = new Cage(30, 3,false, space);
		cages[30][4] = new Cage(30, 4,false, space);
		cages[30][5] = new Cage(30, 5,false, space);
		cages[30][6] = new Cage(30, 6,false, wall_up);
		cages[30][7] = new Cage(30, 7, true, floor);
		cages[30][8] = new Cage(30, 8, true, floor);
		cages[30][9] = new Cage(30, 9, true, floor);
		cages[30][10] = new Cage(30, 10, true, floor);
		cages[30][11] = new Cage(30, 11, true, floor);
		cages[30][12] = new Cage(30, 12, true, floor);
		cages[30][13] = new Cage(30, 13, true, floor);
		cages[30][14] = new Cage(30, 14, true, floor);
		cages[30][15] = new Cage(30, 15, true, floor);
		cages[30][16] = new Cage(30, 16, false, wall_down);
		cages[30][17] = new Cage(30, 17, false, wall_up);
		cages[30][18] = new Cage(30, 18, true, floor);
		cages[30][19] = new Cage(30, 19, true, floor);
		cages[30][20] = new Cage(30, 20, true, floor);
		cages[30][21] = new Cage(30, 21, true, floor);
		cages[30][22] = new Cage(30, 22, true, floor);
		cages[30][23] = new Cage(30, 23, true, floor);
		cages[30][24] = new Cage(30, 24, true, floor);
		cages[30][25] = new Cage(30, 25, true, floor);
		cages[30][26] = new Cage(30, 26, true, floor);
		cages[30][27] = new Cage(30, 27, true, floor);
		cages[30][28] = new Cage(30, 28, true, floor);
		cages[30][29] = new Cage(30, 29, true, floor);
		cages[30][30] = new Cage(30, 30, true, floor);
		cages[30][31] = new Cage(30, 31, false, wall_down);

		cages[31][0] = new Cage(31, 0, false, space);
		cages[31][1] = new Cage(31, 1,false, space);
		cages[31][2] = new Cage(31, 2,false, space);
		cages[31][3] = new Cage(31, 3,false, space);
		cages[31][4] = new Cage(31, 4,false, space);
		cages[31][5] = new Cage(31, 5,false, space);
		cages[31][6] = new Cage(31, 6,false, corner_left_up);
		cages[31][7] = new Cage(31, 7, false, wall_left);
		cages[31][8] = new Cage(31, 8, false, wall_left);
		cages[31][9] = new Cage(31, 9, false, wall_left);
		cages[31][10] = new Cage(31, 10, false, wall_left);
		cages[31][11] = new Cage(31, 11, false, wall_left);
		cages[31][12] = new Cage(31, 12, false, wall_left);
		cages[31][13] = new Cage(31, 13, false, wall_left);
		cages[31][14] = new Cage(31, 14, false, wall_left);
		cages[31][15] = new Cage(31, 15, false, wall_left);
		cages[31][16] = new Cage(31, 16, false, corner_left_down);
		cages[31][17] = new Cage(31, 17, false, corner_left_up);
		cages[31][18] = new Cage(31, 18, false, wall_left);
		cages[31][19] = new Cage(31, 19, false, wall_left);
		cages[31][20] = new Cage(31, 20, false, wall_left);
		cages[31][21] = new Cage(31, 21, false, wall_left);
		cages[31][22] = new Cage(31, 22, false, wall_left);
		cages[31][23] = new Cage(31, 23, false, wall_left);
		cages[31][24] = new Cage(31, 24, false, wall_left);
		cages[31][25] = new Cage(31, 25, false, wall_left);
		cages[31][26] = new Cage(31, 26, false, wall_left);
		cages[31][27] = new Cage(31, 27, false, wall_left);
		cages[31][28] = new Cage(31, 28, false, wall_left);
		cages[31][29] = new Cage(31, 29, false, wall_left);
		cages[31][30] = new Cage(31, 30, false, wall_left);
		cages[31][31] = new Cage(31, 31, false, corner_left_down);
	}

	@Override
	public void render () {
		//height = Gdx.app.getGraphics().getHeight();
		//width = Gdx.app.getGraphics().getWidth();
		//size = height/7;
		ScreenUtils.clear(93 / 255.0F, 93 / 255.0F, 93 / 255.0F, 1);
		//ScreenUtils.clear(0, 0, 0, 1);
		level_one.setProjectionMatrix(camera.combined);
		level_one.begin();

		for (Cage [] cage_x: cages){
			for (Cage cage_y: cage_x){
				level_one.draw(cage_y.img, horisontal_otstup+cage_y.x*size, vertical_otstup+cage_y.y*size, size, size);
				//font.draw(level_one, cage_y.x+":"+cage_y.y, horisontal_otstup+cage_y.x*size, vertical_otstup+cage_y.y*size+size);
			}
		}

		for (Zombie zombie:zombies){
			zombie.draw(level_one, size, speed);
		}

		for (Lever lever:levers){
			lever.draw(level_one, size, speed);
		}

		player.draw(level_one, size, speed);

		level_one.draw(border, left_border_x, left_border_y, horisontal_otstup, height);
		level_one.draw(border, right_border_x, right_border_y, horisontal_otstup, height);

		level_one.draw(border, up_border_x, up_border_y, width, vertical_otstup);
		level_one.draw(border, down_border_x, down_border_y, width, vertical_otstup);

		attack_btn.draw(level_one, size, speed, size, 2*size);
		waiting_btn.draw(level_one, size, speed, size, size);

		if (camera_move_right > 0){
			camera.translate(speed, 0);
			left_border_x+=speed;
			right_border_x+=speed;
			up_border_x+=speed;
			down_border_x+=speed;
			camera_move_right -= speed;
			if (camera_move_right == 0) is_hod = true;
		}
		if (speed > camera_move_right && camera_move_right > 0){
			camera.translate(camera_move_right, 0);
			left_border_x+=camera_move_right;
			right_border_x+=camera_move_right;
			up_border_x+=camera_move_right;
			down_border_x+=camera_move_right;
			camera_move_right = 0;
			is_hod = true;
		}
		if (camera_move_left > 0){
			camera.translate(-speed, 0);
			left_border_x-=speed;
			right_border_x-=speed;
			up_border_x-=speed;
			down_border_x-=speed;
			camera_move_left -= speed;
			if (camera_move_left == 0) is_hod = true;
		}
		if (speed > camera_move_left && camera_move_left > 0){
			camera.translate(-camera_move_left, 0);
			left_border_x-=camera_move_left;
			right_border_x-=camera_move_left;
			up_border_x-=camera_move_left;
			down_border_x-=camera_move_left;
			camera_move_left = 0;
			is_hod = true;
		}
		if (camera_move_up > 0){
			camera.translate(0, speed);
			left_border_y+=speed;
			right_border_y+=speed;
			up_border_y+=speed;
			down_border_y+=speed;
			camera_move_up -= speed;
			if (camera_move_up == 0) is_hod = true;
		}
		if (speed > camera_move_up && camera_move_up > 0){
			camera.translate(0, camera_move_up);
			left_border_y+=camera_move_up;
			right_border_y+=camera_move_up;
			up_border_y+=camera_move_up;
			down_border_y+=camera_move_up;
			camera_move_up = 0;
			is_hod = true;
		}
		if (camera_move_down > 0){
			camera.translate(0, -speed);
			left_border_y-=speed;
			right_border_y-=speed;
			up_border_y-=speed;
			down_border_y-=speed;
			camera_move_down -= speed;
			if (camera_move_down == 0) is_hod = true;
		}
		if (speed > camera_move_down && camera_move_down > 0){
			camera.translate(0, -camera_move_down);
			left_border_y-=camera_move_down;
			right_border_y-=camera_move_down;
			up_border_y-=camera_move_down;
			down_border_y-=camera_move_down;
			camera_move_down = 0;
			is_hod = true;
		}

		if (Gdx.input.justTouched()){
			int touch_x = (int)((Gdx.input.getX()-horisontal_otstup) / size);
			int touch_y = (int)((height - (vertical_otstup+Gdx.input.getY())) / size);
			if (!menu_open) {
				if(!is_attack){
					if (is_hod && touch_x == 5 && touch_y == 3) { // Движение вправо
						player.change_img(mage_right);
						if (moveable(player.x + 1, player.y)) {
							camera_move_right += size;
							cages[player.x][player.y].moveable = true;
							hod(1, 0);
							cages[player.x][player.y].moveable = false;
						}
					}
					if (is_hod && touch_x == 3 && touch_y == 3) { // Движение влево
						player.change_img(mage_left);
						if (moveable(player.x - 1, player.y)) {
							camera_move_left += size;
							cages[player.x][player.y].moveable = true;
							hod(-1, 0);
							cages[player.x][player.y].moveable = false;
						}
					}
					if (is_hod && touch_x == 4 && touch_y == 4) { // Движение вверх
						if (moveable(player.x, player.y + 1)) {
							camera_move_up += size;
							cages[player.x][player.y].moveable = true;
							hod(0, 1);
							cages[player.x][player.y].moveable = false;
						}
					}
					if (is_hod && touch_x == 4 && touch_y == 2) { // Движение вниз
						if (moveable(player.x, player.y - 1)) {
							camera_move_down += size;
							cages[player.x][player.y].moveable = true;
							hod(0, -1);
							cages[player.x][player.y].moveable = false;
						}
					}
				}
				else{
					if (touch_x == 5 && touch_y == 3) { // Атака вправо
					    player.change_img(mage_right);
						for (Zombie zombie: zombies){
							if(zombie.x == player.x+1 && zombie.y == player.y){
								hit_monster(zombie);
								move_monster();
							}
						}
						for (Lever lever: levers){
							if(lever.x == player.x+1 && lever.y == player.y){
								lever.click();
								if (lever.is_horizontal) {
									if (lever.moveable) {
										cages[lever.door_cord_x][lever.door_cord_y].change_img(open_horizontal_door);
										cages[lever.door_cord_x][lever.door_cord_y].moveable = true;
									} else {
										cages[lever.door_cord_x][lever.door_cord_y].change_img(closed_horizontal_door);
										cages[lever.door_cord_x][lever.door_cord_y].moveable = false;
									}
								}
								else {
									if (lever.moveable) {
										cages[lever.door_cord_x][lever.door_cord_y].change_img(open_vertical_door);
										cages[lever.door_cord_x][lever.door_cord_y].moveable = true;
									} else {
										cages[lever.door_cord_x][lever.door_cord_y].change_img(closed_vertical_door);
										cages[lever.door_cord_x][lever.door_cord_y].moveable = false;
									}
								}
							}
						}
						is_attack = false;
						attack_btn.change_img(attack_btn_img);
					}
					if (touch_x == 3 && touch_y == 3) { // Атака влево
						player.change_img(mage_left);
						for (Zombie zombie: zombies){
							if(zombie.x == player.x-1 && zombie.y == player.y){
								hit_monster(zombie);
								move_monster();
							}
						}
						for (Lever lever: levers){
							if(lever.x == player.x-1 && lever.y == player.y){
								lever.click();
								if (lever.is_horizontal) {
									if (lever.moveable) {
										cages[lever.door_cord_x][lever.door_cord_y].change_img(open_horizontal_door);
										cages[lever.door_cord_x][lever.door_cord_y].moveable = true;
									} else {
										cages[lever.door_cord_x][lever.door_cord_y].change_img(closed_horizontal_door);
										cages[lever.door_cord_x][lever.door_cord_y].moveable = false;
									}
								}
								else {
									if (lever.moveable) {
										cages[lever.door_cord_x][lever.door_cord_y].change_img(open_vertical_door);
										cages[lever.door_cord_x][lever.door_cord_y].moveable = true;
									} else {
										cages[lever.door_cord_x][lever.door_cord_y].change_img(closed_vertical_door);
										cages[lever.door_cord_x][lever.door_cord_y].moveable = false;
									}
								}
							}
						}
						is_attack = false;
						attack_btn.change_img(attack_btn_img);
					}
					if (touch_x == 4 && touch_y == 4) { // Атака вверх
						for (Zombie zombie: zombies){
							if(zombie.x == player.x && zombie.y == player.y+1){
								hit_monster(zombie);
								move_monster();
							}
						}
						for (Lever lever: levers){
							if(lever.x == player.x && lever.y == player.y+1){
								lever.click();
								if (lever.is_horizontal) {
									if (lever.moveable) {
										cages[lever.door_cord_x][lever.door_cord_y].change_img(open_horizontal_door);
										cages[lever.door_cord_x][lever.door_cord_y].moveable = true;
									} else {
										cages[lever.door_cord_x][lever.door_cord_y].change_img(closed_horizontal_door);
										cages[lever.door_cord_x][lever.door_cord_y].moveable = false;
									}
								}
								else {
									if (lever.moveable) {
										cages[lever.door_cord_x][lever.door_cord_y].change_img(open_vertical_door);
										cages[lever.door_cord_x][lever.door_cord_y].moveable = true;
									} else {
										cages[lever.door_cord_x][lever.door_cord_y].change_img(closed_vertical_door);
										cages[lever.door_cord_x][lever.door_cord_y].moveable = false;
									}
								}
							}
						}
						is_attack = false;
						attack_btn.change_img(attack_btn_img);
					}
					if (touch_x == 4 && touch_y == 2) { // Атака вниз
						for (Zombie zombie: zombies){
							if(zombie.x == player.x && zombie.y == player.y-1){
								hit_monster(zombie);
								move_monster();
							}
						}
						for (Lever lever: levers){
							if(lever.x == player.x && lever.y == player.y-1){
								lever.click();
								if (lever.is_horizontal) {
									if (lever.moveable) {
										cages[lever.door_cord_x][lever.door_cord_y].change_img(open_horizontal_door);
										cages[lever.door_cord_x][lever.door_cord_y].moveable = true;
									} else {
										cages[lever.door_cord_x][lever.door_cord_y].change_img(closed_horizontal_door);
										cages[lever.door_cord_x][lever.door_cord_y].moveable = false;
									}
								}
								else {
									if (lever.moveable) {
										cages[lever.door_cord_x][lever.door_cord_y].change_img(open_vertical_door);
										cages[lever.door_cord_x][lever.door_cord_y].moveable = true;
									} else {
										cages[lever.door_cord_x][lever.door_cord_y].change_img(closed_vertical_door);
										cages[lever.door_cord_x][lever.door_cord_y].moveable = false;
									}
								}
							}
						}
						is_attack = false;
						attack_btn.change_img(attack_btn_img);
					}
				}
				if (touch_x == 9 && (touch_y == 2 | touch_y == 3)){ // Нажатие кнопки атаки
					is_attack = !is_attack;
					if (is_attack){
						attack_btn.change_img(activ_attack_btn_img);
					}
					else {
						attack_btn.change_img(attack_btn_img);
					}
				}
			}
			if (touch_x == 9 && touch_y == 1){ // Нажатие пропуска хода
				hod(0, 0);
				is_hod = true;
			}
		}

		font.draw(level_one, "Hp: " + player.health + " / " + player.max_health + "\n" + "Lvl: " + lvl + "\n" + "Exp: " +
				exp + "/" + max_exp + "\n" + "Damage: " + player.damage + "\n" + "Moves: " +
				moves, attack_btn.real_x, attack_btn.real_y+4*size);
		camera.update();
		level_one.end();
		if (menu_open){
			inventory.begin();
			inventory.end();
		}

		if (player.health<=0){
			dispose();
		}
	}

	public void hod(int x, int y){
		if (player.health<player.max_health-5){
			player.health+=5;
		}
		else player.health=player.max_health;
		moves+=1;
		player.move(x, y);
		attack_btn.move(x, y);
		waiting_btn.move(x, y);
		move_monster();
		is_hod = false;
	}

	
	@Override
	public void dispose () {
		level_one.dispose();
		wall_right.dispose();
		wall_up.dispose();
		wall_down.dispose();
		wall_left.dispose();
		corner_right_up.dispose();
		corner_right_down.dispose();
		corner_left_up.dispose();
		corner_left_down.dispose();
		floor.dispose();
		zombie.dispose();
		mage_right.dispose();
		mage_left.dispose();
		attack_btn_img.dispose();
		activ_attack_btn_img.dispose();
		column.dispose();
		corner_and_wall_down_right.dispose();
		corner_and_wall_up_right.dispose();
		corner_and_wall_down_left.dispose();
		corner_and_wall_up_left.dispose();
		activ_lever.dispose();
		passiv_lever.dispose();
		space.dispose();
		closed_horizontal_door.dispose();
		open_horizontal_door.dispose();
		border.dispose();
		waiting_btn_img.dispose();
		closed_vertical_door.dispose();
		open_vertical_door.dispose();
		music.stop();
		music.dispose();
	}
}