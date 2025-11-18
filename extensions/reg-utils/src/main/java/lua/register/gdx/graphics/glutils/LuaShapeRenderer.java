package lua.register.gdx.graphics.glutils;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import java.util.ArrayList;
import lua.register.DefaultNewIndexFunction;
import lua.Lua;
import lua.LuaFunction;
import lua.LuaLibrary;
import lua.LuaState;
import lua.LuaTableType;
import lua.register.LuaCreateClass;
import lua.register.LuaIntEnumData;

public class LuaShapeRenderer {

    public static void register(Lua lua) {
        LuaState luaState = lua.getLuaState();

        String rendererClassName = "com.badlogic.gdx.graphics.glutils.ShapeRenderer";

        LuaLibrary.registerClass(lua, rendererClassName, true, true);
        LuaLibrary.registerMetaClassTableNewIndex(lua, LuaTableType.CLASS, rendererClassName, new DefaultNewIndexFunction());
        LuaLibrary.registerMetaClassTableNewIndex(lua, LuaTableType.CLASS_INSTANCE, rendererClassName, new DefaultNewIndexFunction());

        ShapeRenderer.ShapeType filled = ShapeRenderer.ShapeType.Filled;

        ArrayList<LuaIntEnumData> enumList = new ArrayList<>();
        LuaIntEnumData.addEnum(enumList, "Point", ShapeRenderer.ShapeType.Point.ordinal());
        LuaIntEnumData.addEnum(enumList, "Line", ShapeRenderer.ShapeType.Line.ordinal());
        LuaIntEnumData.addEnum(enumList, "Filled", ShapeRenderer.ShapeType.Filled.ordinal());
        LuaLibrary.createEnumTable(lua, enumList);
        LuaLibrary.setMetaClassTable(lua, LuaTableType.CLASS, rendererClassName, "ShapeType");

        LuaLibrary.setClassNewFunction(lua, rendererClassName, new LuaCreateClass() {
            @Override
            public Object onCreateClass(LuaState luaState) {
                return new ShapeRenderer();
            }
        });

        LuaLibrary.setMetaClassFunction(lua, LuaTableType.CLASS_INSTANCE, rendererClassName, "Begin", new LuaFunction() {
            @Override
            public int onCall(LuaState luaState) {
                Object javaInstance = LuaLibrary.getJavaInstance(lua, luaState);
                if(javaInstance == null) {
                    return 0;
                }
                if(!(javaInstance instanceof ShapeRenderer)) {
                    luaState.luaL_error("Object is not instance of ShapeRenderer");
                }

                if(luaState.lua_gettop() != 2 || luaState.lua_isinteger(2) == 0) {
                    luaState.luaL_error("Should only contains 1 int parameter");
                    return 0;
                }
                //TODO change int to a table so its possible to know if its really a enum
                int enumValue = luaState.lua_tointeger(2);
                ShapeRenderer.ShapeType value = ShapeRenderer.ShapeType.values()[enumValue];
                ShapeRenderer renderer = (ShapeRenderer)javaInstance;
                renderer.begin(value);
                return 0;
            }
        });

        LuaLibrary.setMetaClassFunction(lua, LuaTableType.CLASS_INSTANCE, rendererClassName, "End", new LuaFunction() {
            @Override
            public int onCall(LuaState luaState) {
                Object javaInstance = LuaLibrary.getJavaInstance(lua, luaState);
                if(javaInstance == null) {
                    return 0;
                }
                if(!(javaInstance instanceof ShapeRenderer)) {
                    luaState.luaL_error("Object is not instance of ShapeRenderer");
                }
                ShapeRenderer renderer = (ShapeRenderer)javaInstance;
                renderer.end();
                return 0;
            }
        });

        LuaLibrary.setMetaClassFunction(lua, LuaTableType.CLASS_INSTANCE, rendererClassName, "setColor", new LuaFunction() {

            @Override
            public int onCall(LuaState luaState) {
                Object javaInstance = LuaLibrary.getJavaInstance(lua, luaState);
                if(javaInstance == null) {
                    return 0;
                }
                if(!(javaInstance instanceof ShapeRenderer)) {
                    luaState.luaL_error("Object is not instance of ShapeRenderer");
                }
                ShapeRenderer renderer = (ShapeRenderer)javaInstance;

                int params = luaState.lua_gettop();
                boolean argError = false;
                if(params != 5) {
                    argError = true;
                }
                else {
                    if(luaState.lua_isnumber(2) == 0 || luaState.lua_isnumber(3) == 0 ||
                            luaState.lua_isnumber(4) == 0 || luaState.lua_isnumber(5) == 0) {
                        argError = true;
                    }
                }
                if(argError) {
                    luaState.luaL_error("Only accept 4 float arguments");
                    return 0;
                }

                float v1 = (float)luaState.lua_tonumber(2);
                float v2 = (float)luaState.lua_tonumber(3);
                float v3 = (float)luaState.lua_tonumber(4);
                float v4 = (float)luaState.lua_tonumber(5);
                renderer.setColor(v1, v2, v3, v4);
                return 0;
            }
        });

        LuaLibrary.setMetaClassFunction(lua, LuaTableType.CLASS_INSTANCE, rendererClassName, "rect", new LuaFunction() {

            @Override
            public int onCall(LuaState luaState) {
                Object javaInstance = LuaLibrary.getJavaInstance(lua, luaState);
                if(javaInstance == null) {
                    return 0;
                }
                if(!(javaInstance instanceof ShapeRenderer)) {
                    luaState.luaL_error("Object is not instance of ShapeRenderer");
                }
                ShapeRenderer renderer = (ShapeRenderer)javaInstance;

                int params = luaState.lua_gettop();
                boolean argError = false;
                if(params != 5) {
                    argError = true;
                }
                else {
                    if(luaState.lua_isnumber(2) == 0 || luaState.lua_isnumber(3) == 0 ||
                            luaState.lua_isnumber(4) == 0 || luaState.lua_isnumber(5) == 0) {
                        argError = true;
                    }
                }
                if(argError) {
                    luaState.luaL_error("Only accept 4 float arguments");
                    return 0;
                }

                float v1 = (float)luaState.lua_tonumber(2);
                float v2 = (float)luaState.lua_tonumber(3);
                float v3 = (float)luaState.lua_tonumber(4);
                float v4 = (float)luaState.lua_tonumber(5);
                renderer.rect(v1, v2, v3, v4);
                return 0;
            }
        });

    }
}
