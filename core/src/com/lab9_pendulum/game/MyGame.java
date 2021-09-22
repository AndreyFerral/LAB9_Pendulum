package com.lab9_pendulum.game;

import com.badlogic.gdx.Game;

public class MyGame extends Game {
	@Override
	// Вызывается при создании приложения
	public void create () {
		setScreen(new MainGameScreen());
	}
}
