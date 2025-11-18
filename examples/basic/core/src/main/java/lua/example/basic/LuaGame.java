package lua.example.basic;

import com.badlogic.gdx.Game;

public class LuaGame extends Game {

    @Override
    public void create() {
        setScreen(new LuaInitScreen(this));
    }
}
