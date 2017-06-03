package com.feup.superslimefootball.view.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.feup.superslimefootball.SuperSlimeFootball;

public class WinnerMenuState extends MenuState {
    public WinnerMenuState(SuperSlimeFootball game) {
        super(game);
    }

    @Override
    public void drawButtons() {

        Texture facebook = this.game.getAssetManager().get("facebook.png", Texture.class);
        Texture twitter = this.game.getAssetManager().get("twitter.png", Texture.class);
        Texture congratulations = this.game.getAssetManager().get("expressions/Congratulationsyouwon.png", Texture.class);

        this.game.getBatch().draw(facebook, Gdx.graphics.getWidth()*(12.0f/13.0f), Gdx.graphics.getHeight()*(17.0f/20.0f));
        this.game.getBatch().draw(twitter, Gdx.graphics.getWidth()*(12.0f/13.0f), Gdx.graphics.getHeight()*(14.0f/20.0f));
        this.game.getBatch().draw(congratulations, Gdx.graphics.getWidth()*(3.0f/10.0f), Gdx.graphics.getHeight()*(14.0f/20.0f));

    }


    @Override
    public void handleMouse() {

    }
}
