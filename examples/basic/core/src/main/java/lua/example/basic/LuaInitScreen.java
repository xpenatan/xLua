package lua.example.basic;

import com.badlogic.gdx.ScreenAdapter;
import lua.LuaLoader;

public class LuaInitScreen extends ScreenAdapter {

    private LuaGame game;

    private boolean init = false;

    public LuaInitScreen(LuaGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        LuaLoader.init((isSuccess, e) -> {
            if(isSuccess) {
                init = isSuccess;
//                ImGuiLoader.init((isSuccess1, e1) -> {
//                    init = isSuccess1;
//                    if(e1 != null) {
//                        e1.printStackTrace();
//                    }
//                });
            }
            if(e != null) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void render(float delta) {
        if(init) {
            init = false;
            game.setScreen(new BasicExample());
        }
    }
}
