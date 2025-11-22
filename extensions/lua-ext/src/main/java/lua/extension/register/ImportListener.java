package lua.extension.register;

import lua.LuaState;

public interface ImportListener {
    int onImport(LuaState luaState);
}