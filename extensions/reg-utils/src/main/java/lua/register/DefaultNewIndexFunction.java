package lua.register;

import lua.LuaFunction;
import lua.LuaState;

public class DefaultNewIndexFunction extends LuaFunction {

    @Override
    public int onCall(LuaState luaState) {
        luaState.luaL_error("Class modification is not allowed");
//        //Check if key is string
//        if(luaState.lua_isstring(-2) != 0) {
//            String key = luaState.lua_tostring(-2).c_str();
//            if(key.equals("x")) {
//                // allow only x
//                luaState.lua_rawset(-3);
//            }
//        }
        return 0;
    }
}
