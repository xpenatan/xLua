package lua.register.gdx.graphics;

import com.badlogic.gdx.Gdx;
import lua.Lua;
import lua.LuaFunction;
import lua.LuaLibrary;
import lua.LuaState;
import lua.LuaTableType;

public class LuaGraphics {

    public static final String CLASSNAME = "com.badlogic.gdx.Graphics";

    public static void register(Lua lua) {
        LuaState luaState = lua.getLuaState();

        LuaLibrary.registerClass(lua, CLASSNAME, false, false);

        LuaLibrary.setMetaClassFunction(lua, LuaTableType.CLASS, CLASSNAME, "getWidth", new LuaFunction() {
            @Override
            public int onCall(LuaState luaState) {
                luaState.lua_pushnumber(Gdx.graphics.getWidth());
                return 1;
            }
        });

        LuaLibrary.setMetaClassFunction(lua, LuaTableType.CLASS, CLASSNAME, "getHeight", new LuaFunction() {
            @Override
            public int onCall(LuaState luaState) {
                luaState.lua_pushnumber(Gdx.graphics.getHeight());
                return 1;
            }
        });
    }
}