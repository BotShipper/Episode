package org.minhquan.gamestate;

import lombok.Getter;
import org.minhquan.app.Game;
import org.minhquan.ui.MenuButton;

import java.awt.event.MouseEvent;

public class State {

    @Getter
    protected Game game;

    public State(Game game) {
        this.game = game;
    }

    public boolean isIn(MouseEvent e, MenuButton mb) {
        return mb.getBounds().contains(e.getX(), e.getY());
    }
}
