package lua.extension.register;

import lua.LuaState;

public interface LuaCreateClass {
    /**
     * Create a Java instance and return its Id. The id will be used to execute methods on the instance.
     */
    int onCreateJavaInstance(LuaState luaState);
    void onDisposeJavaInstance(int javaInstanceId);
}
