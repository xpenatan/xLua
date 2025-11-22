package lua.example.basic.register.gdx;

import lua.extension.LuaExt;
import lua.extension.LuaLibrary;
import lua.extension.LuaTableType;
import lua.example.basic.register.gdx.graphics.LuaGL20;
import lua.example.basic.register.gdx.graphics.LuaGraphics;
import lua.example.basic.register.gdx.graphics.glutils.LuaShapeRenderer;

public class LuaGdx {
    public static final String CLASSNAME = "com.badlogic.gdx.Gdx";

    public static void register(LuaExt lua) {
        LuaGL20.register(lua);
        LuaGraphics.register(lua);
        LuaShapeRenderer.register(lua);


        LuaLibrary.registerClass(lua, LuaGdx.CLASSNAME, true, null);

        if(LuaLibrary.getMetaClassTable(lua, LuaTableType.CLASS, LuaGL20.CLASSNAME)) {
            LuaLibrary.setMetaClassTable(lua, LuaTableType.CLASS, LuaGdx.CLASSNAME, "gl");
        }

        if(LuaLibrary.getMetaClassTable(lua, LuaTableType.CLASS, LuaGraphics.CLASSNAME)) {
            LuaLibrary.setMetaClassTable(lua, LuaTableType.CLASS, LuaGdx.CLASSNAME, "graphics");
        }

//        LuaLibrary.setMetaClassFloat()
    }
}