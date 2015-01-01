package com.kaptan.freezefiretag.entities;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;


public class Button extends TextButton
{
    private final Board board;

    public Button(String text, Skin skin, final Board board)
    {
        super(text, skin);
        this.board = board;
        addListener(new InputListener()
        {
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button)
            {
                board.switchTurn();
                return true;
            }
        });
    }
}
