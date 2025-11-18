package lua.register.gdx.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import lua.Lua;
import lua.LuaFunction;
import lua.LuaLibrary;
import lua.LuaState;
import lua.LuaTableType;

public class LuaGL20 {

    public static final String CLASSNAME = "com.badlogic.gdx.graphics.GL20";

    public static void register(Lua lua) {
        LuaState luaState = lua.getLuaState();

        LuaLibrary.registerClass(lua, CLASSNAME, true, false);

        LuaLibrary.setMetaClassInt(lua, LuaTableType.CLASS, CLASSNAME, "GL_COLOR_BUFFER_BIT", GL20.GL_COLOR_BUFFER_BIT);

        LuaLibrary.setMetaClassFunction(lua, LuaTableType.CLASS, CLASSNAME, "glClearColor", new LuaFunction() {

            @Override
            public int onCall(LuaState luaState) {
                int params = luaState.lua_gettop();
                boolean argError = false;
                if(params != 4) {
                    argError = true;
                }
                else {
                    if(luaState.lua_isnumber(1) == 0 || luaState.lua_isnumber(2) == 0 ||
                            luaState.lua_isnumber(3) == 0 || luaState.lua_isnumber(4) == 0) {
                        argError = true;
                    }
                }
                if(argError) {
                    luaState.luaL_error("Only accept 4 float arguments");
                    return 0;
                }

                float v1 = (float)luaState.lua_tonumber(1);
                float v2 = (float)luaState.lua_tonumber(2);
                float v3 = (float)luaState.lua_tonumber(3);
                float v4 = (float)luaState.lua_tonumber(4);
                Gdx.gl.glClearColor(v1, v2, v3, v4);
                return 0;
            }
        });

        LuaLibrary.setMetaClassFunction(lua, LuaTableType.CLASS, CLASSNAME, "glClear", new LuaFunction() {

            @Override
            public int onCall(LuaState luaState) {

                int params = luaState.lua_gettop();
                boolean argError = false;
                if(params != 1) {
                    argError = true;
                }
                else {
                    if(luaState.lua_isinteger(1) == 0) {
                        argError = true;
                    }
                }
                if(argError) {
                    luaState.luaL_error("Only accept a single int argument");
                    return 0;
                }

                int mask = luaState.lua_tointeger(1);
                Gdx.gl.glClear(mask);
                return 0;
            }
        });
    }
}