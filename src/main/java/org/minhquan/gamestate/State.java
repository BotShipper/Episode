package org.minhquan.gamestate;

import lombok.Getter;
import org.minhquan.app.Game;

public class State {

    @Getter
    protected Game game;

    public State(Game game) {
        this.game = game;
    }
}
