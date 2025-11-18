package lua.register;

import lua.LuaState;

public interface LuaCreateClass {
    Object onCreateClass(LuaState luaState);
}
