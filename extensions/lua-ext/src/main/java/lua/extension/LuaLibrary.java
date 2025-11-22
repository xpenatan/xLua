package lua.extension;

import java.util.List;
import lua.LuaFunction;
import lua.LuaState;
import lua.extension.register.ImportListener;
import lua.extension.register.LuaCreateClass;
import lua.extension.register.LuaIntEnumData;

public class LuaLibrary {

    public static final String LUA_J_ID = "java_id";
    private static final String INSTANCE = "Instance";
    private static final String CLASS = "Class";
    private static final String ENUM = "Enum";

    private static boolean SKIP_ERROR_LOG = false;

    private static final String t = "Lua does not contains java instance";

    public static int getJavaInstanceId(LuaState luaState) {
        if(luaState.lua_istable(1) != 0) {
            luaState.lua_getfield(1, LuaLibrary.LUA_J_ID);
            if(luaState.lua_isnil(-1) != 0) {
                luaState.lua_pop(1);
                luaState.luaL_error(t);
                return 0;
            }
            else {
                if(luaState.lua_isinteger(-1) == 0) {
                    luaState.lua_pop(1);
                    luaState.luaL_error(t);
                    return 0;
                }
                else {
                    int id = luaState.lua_tointeger(-1);
                    luaState.lua_pop(1);
                    return id;
                }
            }
        }
        luaState.luaL_error(t);
        return 0;
    }

    /**
     * Get or create the table and push to stack.
     * If parent exist, it will create or get as a child.
     * If parent don't exist, it will create a global table.
     */
    private static void getOrCreateTable(LuaExt lua, String table) {
        LuaState luaState = lua.luaState;
        boolean hasParentTable = luaState.lua_istable(-1) != 0;
        if(!hasParentTable) {
            luaState.lua_getglobal(table);
            hasParentTable = luaState.lua_istable(-1) != 0;
            if(!hasParentTable) {
                lua.luaState.lua_pop(1); // remove nill from stack
                luaState.lua_newtable();
                luaState.lua_setglobal(table);
                luaState.lua_getglobal(table);
            }
            return;
        }
        luaState.lua_getfield(-1, table);
        boolean containsTable = luaState.lua_istable(-1) != 0;
        if(!containsTable) {
            lua.luaState.lua_pop(1); // remove nill from stack
            luaState.lua_pushstring(table);
            luaState.lua_newtable();
            luaState.lua_settable(-3);
            luaState.lua_getfield(-1, table);
            //swap tables and pop the parent table so the child is at the top.
            luaState.lua_rotate(-2, 1);
            lua.luaState.lua_pop(1);
        }
    }

    private static void getOrCreateTables(LuaExt lua, String fullPath) {
        String[] split = fullPath.split("\\.");
        for(int i = 0; i < split.length; i++) {
            String name = split[i];
            getOrCreateTable(lua, name);
        }
    }

    public static void addLibrary(LuaExt lua, String fullPackage, LuaFunction function) {
        LuaState luaState = lua.luaState;
        String[] split = fullPackage.split("\\.");
        String lastItem = split[split.length - 1];
        String lastPath = "." + lastItem;
        String fullPath = fullPackage.replace(lastPath, "");
        getOrCreateTables(lua, fullPath);

        {
            // Add function to table on top of the stack
            lua.registerFunction(lastItem, function);
        }
        luaState.lua_pop(1); // remove table from stack
    }

    /**
     * Will create tables and keep the last table into stack
     */
    public static void getOrCreateClass(LuaExt lua, Class<?> clazz) {
        String name = clazz.getName();
        getOrCreateTables(lua, name);
    }

    /**
     * Get and push meta table to top of stack. Return false if it doesn't exist. <br>
     * It's your responsibility to pop the table from stack.
     */
    public static boolean getMetaClass(LuaExt lua, String className) {
        LuaState luaState = lua.luaState;
        luaState.luaL_getmetatable(className);
        if(luaState.lua_isnil(-1) != 0) {
            luaState.lua_pop(1); // Pop nil
            if(!SKIP_ERROR_LOG) {
                System.err.println("getMetaClass() don't exist: " + className);
            }
            SKIP_ERROR_LOG = false;
            return false;
        }
        SKIP_ERROR_LOG = false;
        return true;
    }

    /**
     * Get and push class table to top of stack. Return false if it doesn't exist. <br>
     * It's your responsibility to pop the table from stack.
     */
    public static boolean getMetaClassTable(LuaExt lua, LuaTableType type, String className) {
        if(getMetaClass(lua, className)) {
            LuaState luaState = lua.luaState;
            if(type == LuaTableType.CLASS) {
                luaState.lua_getfield(-1, CLASS);
                if(luaState.lua_istable(-1) != 0) {
                    luaState.lua_remove(-2); // Remove metatable
                    return true;
                }
                else {
                    luaState.lua_pop(1); // pop nill
                }
            }
            else if(type == LuaTableType.CLASS_INSTANCE) {
                luaState.lua_getfield(-1, INSTANCE);
                if(luaState.lua_istable(-1) != 0) {
                    luaState.lua_remove(-2); // Remove metatable
                    return true;
                }
                else {
                    luaState.lua_pop(1); // pop nill
                }
            }
            else if(type == LuaTableType.ENUM) {
                luaState.lua_getfield(-1, ENUM);
                if(luaState.lua_istable(-1) != 0) {
                    luaState.lua_remove(-2); // Remove metatable
                    return true;
                }
                else {
                    luaState.lua_pop(1); // pop nill
                }
            }
            luaState.lua_pop(1); // pop metaclass
        }
        System.err.println("getMetaClassTable() don't exist: " + className);
        return false;
    }

    /**
     * Create enum table and push to stack.
     */
    public static boolean createEnumTable(LuaExt lua, List<LuaIntEnumData> enumList) {
        if(enumList == null || enumList.isEmpty())
            return false;
        LuaState luaState = lua.luaState;
        luaState.lua_newtable();

        for(int i = 0; i < enumList.size(); i++) {
            LuaIntEnumData enumData = enumList.get(i);
            luaState.lua_pushinteger(enumData.value);
            luaState.lua_setfield(-2, enumData.key);
        }

        lua.registerFunction("__newindex", new LuaFunction() {
            @Override
            public int onCall(LuaState luaState) {
                luaState.luaL_error("Creating new Enum value is not allowed");
                return 0;
            }
        });

        lua.registerFunction("__index", new LuaFunction() {
            @Override
            public int onCall(LuaState luaState) {
                luaState.luaL_error("Changing Enum value is not allowed");
                return 0;
            }
        });

        return true;
    }

    /**
     * Set a table variable to template or class table. The table variable must be on top of the stack.
     * Will pop table variable if it's true.
     */
    public static boolean setMetaClassTable(LuaExt lua, LuaTableType type, String className, String variableName) {
        LuaState luaState = lua.luaState;
        if(luaState.lua_istable(-1) != 0) {
            if(getMetaClassTable(lua, type, className)) {
                luaState.lua_pushvalue(-2); // copy variable table reference and add to top of stack
                luaState.lua_setfield(-2, variableName);
                luaState.lua_pop(2); // pop MetaClass and table variable
                return true;
            }
        }
        return false;
    }

    /**
     * Set a string variable to template or class table
     */
    public static boolean setMetaClassString(LuaExt lua, LuaTableType type, String className, String variableName, String value) {
        LuaState luaState = lua.luaState;
        if(getMetaClassTable(lua, type, className)) {
            luaState.lua_pushstring(value);
            luaState.lua_setfield(-2, variableName);
            luaState.lua_pop(1);
            return true;
        }
        return false;
    }

    /**
     * Set a int variable to template or class table
     */
    public static boolean setMetaClassInt(LuaExt lua, LuaTableType type, String className, String variableName, int value) {
        LuaState luaState = lua.luaState;
        if(getMetaClassTable(lua, type, className)) {
            luaState.lua_pushinteger(value);
            luaState.lua_setfield(-2, variableName);
            luaState.lua_pop(1);
            return true;
        }
        return false;
    }

    /**
     * Set a float variable to template or class table
     */
    public static boolean setMetaClassFloat(LuaExt lua, LuaTableType type, String className, String variableName, float value) {
        LuaState luaState = lua.luaState;
        if(getMetaClassTable(lua, type, className)) {
            luaState.lua_pushnumber(value);
            luaState.lua_setfield(-2, variableName);
            luaState.lua_pop(1);
            return true;
        }
        return false;
    }

    /**
     * Add class function. MetaClass must exist
     */
    public static boolean setMetaClassFunction(LuaExt lua, LuaTableType type, String className, String functionName, LuaFunction function) {
        LuaState luaState = lua.luaState;
        if(getMetaClassTable(lua, type, className)) {
            if(lua.registerFunction(functionName, function)) {
                luaState.lua_pop(1);
                return true;
            }
        }
        return false;
    }

    /**
     * Will register new method to class.
     * Must create MetaClass Template first
     */
    private static boolean setClassNewFunction(LuaExt lua, String className, LuaCreateClass listener) {
        if(getMetaClassTable(lua, LuaTableType.CLASS, className)) {
            // Put new function inside class table
            {
                lua.registerFunction("new", new LuaFunction() {
                    @Override
                    public int onCall(LuaState luaState) {
                        int javaInstanceId = listener.onCreateJavaInstance(luaState);
                        createInstanceObject(lua, className, javaInstanceId);
                        return 1;
                    }
                });
            }
            lua.luaState.lua_pop(1); // pop metatable

            return true;
        }
        return false;
    }

    public static boolean registerMetaClassTableNewIndex(LuaExt lua, LuaTableType type, String className, LuaFunction luaFunction) {
        if(getMetaClassTable(lua, type, className)) {
            if(lua.registerFunction("__newindex", luaFunction)) {
                lua.luaState.lua_pop(1);
                return true;
            }
        }
        return false;
    }

    /**
     * Create a lua instance. Require MetaClass template table.
     */
    public static boolean createInstanceObject(LuaExt lua, String className, int javaInstanceId) {
        LuaState luaState = lua.luaState;

        if(javaInstanceId == 0) {
            luaState.luaL_error("Java instance id cannot be 0");
            return false;
        }

        luaState.lua_newtable();

        // Put hash key
        {
            luaState.lua_pushinteger(javaInstanceId);
            luaState.lua_setfield(-2, LUA_J_ID);
        }

        //Put metatable to the new created table
        {
            if(getMetaClassTable(lua, LuaTableType.CLASS_INSTANCE, className)) {
                luaState.lua_setmetatable(-2); // Set Template metatable to new table
            }
            else {
                return false;
            }
        }
        return true;
    }

    /**
     * Register MetaClass. Use lua code 'MYCLASS = java.import("class full path")' to obtain lua class object.
     * canInstantiate true means that this class can be created with new function or createInstanceObject.
     */
    public static boolean registerClass(LuaExt lua, String className, boolean addImport, LuaCreateClass instanceCreation) {
        LuaState luaState = lua.luaState;
        SKIP_ERROR_LOG = true;
        if(getMetaClass(lua, className)) {
            luaState.lua_pop(1);
            return false;
        }

        luaState.luaL_newmetatable(className);
        luaState.lua_pop(1);

        createMetaClassClass(lua, className);

        if(instanceCreation != null) {
            createMetaClassTemplate(lua, className, instanceCreation);
            setClassNewFunction(lua, className, instanceCreation);
        }

        if(addImport) {
            lua.addImportListener(className, new ImportListener() {
                @Override
                public int onImport(LuaState luaState) {
                    // Return a metatable
                    if(!getMetaClassTable(lua, LuaTableType.CLASS, className)) {
                        luaState.luaL_error("Metatable don't exist");
                    }
                    luaState.lua_pushvalue(-1);
                    luaState.lua_setmetatable(-2); // Set Template metatable to new table
                    return 1;
                }
            });
        }

        return true;
    }

    public static boolean registerEnum(LuaExt lua, String enumName) {
        LuaState luaState = lua.luaState;
        if(getMetaClass(lua, enumName)) {
            luaState.lua_pop(1);
            return false;
        }

        luaState.luaL_newmetatable(enumName);
        luaState.lua_pop(1);

        createMetaClassEnum(lua, enumName);

        return true;
    }

    private static void createMetaClassTemplate(LuaExt lua, String metaTable, LuaCreateClass instanceCreation) {
        LuaState luaState = lua.luaState;
        luaState.luaL_getmetatable(metaTable);

        {
            luaState.lua_newtable(); // Template table

            // mt.__index = mt
            {
                luaState.lua_pushvalue(-1); // copy Template reference
                luaState.lua_setfield(-2, "__index");
            }

            // Set private metatable
            {
                luaState.lua_pushstring("Private");
                luaState.lua_setfield(-2, "__metatable");
            }

            // Garbage collector
            {
                lua.registerFunction("__gc", new LuaFunction() {
                    @Override
                    public int onCall(LuaState luaState) {
                        luaState.lua_getfield(-1, LUA_J_ID);
                        if(luaState.lua_isinteger(-1) != 0) {
                            int javaInstanceId = luaState.lua_tointeger(-1);
                            instanceCreation.onDisposeJavaInstance(javaInstanceId);
                        }
                        return 0;
                    }
                });
            }
            luaState.lua_setfield(-2, INSTANCE); // set Template to metatable
        }

        luaState.lua_pop(1);
    }

    private static void createMetaClassClass(LuaExt lua, String metaTable) {
        LuaState luaState = lua.luaState;
        luaState.luaL_getmetatable(metaTable);

        {
            luaState.lua_newtable(); // Class table

            // mt.__index = mt
            {
                luaState.lua_pushvalue(-1); // copy MetaTable reference
                luaState.lua_setfield(-2, "__index");
            }

            // Set private metatable
            {
                luaState.lua_pushstring("Private");
                luaState.lua_setfield(-2, "__metatable");
            }

            luaState.lua_setfield(-2, CLASS); // set Class to metatable
        }

        luaState.lua_pop(1);
    }

    private static void createMetaClassEnum(LuaExt lua, String enumName) {
        LuaState luaState = lua.luaState;
        luaState.luaL_getmetatable(enumName);

        {
            luaState.lua_newtable(); // Enum table

            // mt.__index = mt
            {
                luaState.lua_pushvalue(-1); // copy MetaTable reference
                luaState.lua_setfield(-2, "__index");
            }

            // Set private metatable
            {
                luaState.lua_pushstring("Private");
                luaState.lua_setfield(-2, "__metatable");
            }

            luaState.lua_setfield(-2, ENUM); // set Enum to metatable
        }

        luaState.lua_pop(1);
    }
}