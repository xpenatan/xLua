package lua.register.gdx;

import com.badlogic.gdx.Gdx;
import lua.Lua;
import lua.LuaLibrary;
import lua.LuaTableType;
import lua.register.gdx.graphics.LuaGL20;
import lua.register.gdx.graphics.LuaGraphics;
import lua.register.gdx.graphics.glutils.LuaShapeRenderer;

public class LuaGdx {
    public static final String CLASSNAME = "com.badlogic.gdx.Gdx";

    public static void register(Lua lua) {
        LuaGL20.register(lua);
        LuaGraphics.register(lua);
        LuaShapeRenderer.register(lua);


        LuaLibrary.registerClass(lua, LuaGdx.CLASSNAME, true, false);

        if(LuaLibrary.getMetaClassTable(lua, LuaTableType.CLASS, LuaGL20.CLASSNAME)) {
            LuaLibrary.setMetaClassTable(lua, LuaTableType.CLASS, LuaGdx.CLASSNAME, "gl");
        }

        if(LuaLibrary.getMetaClassTable(lua, LuaTableType.CLASS, LuaGraphics.CLASSNAME)) {
            LuaLibrary.setMetaClassTable(lua, LuaTableType.CLASS, LuaGdx.CLASSNAME, "graphics");
        }

//        LuaLibrary.setMetaClassFloat()
    }
}