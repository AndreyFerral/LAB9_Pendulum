package com.lab9_pendulum.game;

import com.badlogic.gdx.Game;

public class MyGame extends Game {
	//SpriteBatch batch;
	//Texture img;
	
	@Override
	// Вызывается при создании приложения
	public void create () {
		setScreen(new MainGameScreen());
		
		//batch = new SpriteBatch();
		//img = new Texture("badlogic.jpg");
	}

	/*
	@Override
	// Вызывается при рисовке чего-либо
	public void render () {

		// Очищаем игровое поле
		ScreenUtils.clear(1, 0, 0, 1);

		// Рисование
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
	 */

}
