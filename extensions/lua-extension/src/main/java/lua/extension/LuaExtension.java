package lua.extension;

import com.badlogic.gdx.utils.IntMap;
import com.github.xpenatan.jParser.idl.IDLBase;
import java.util.HashMap;
import lua.LuaFunction;
import lua.LuaState;
import lua.idl.helper.IDLString;
import lua.extension.register.DefaultImportFunction;
import lua.extension.register.ImportListener;

public class LuaExtension {

    LuaState luaState;

    private final HashMap<String, ImportListener> importMap;

    public IntMap<Object> luaJavaInstances;

    public LuaExtension() {
        importMap = new HashMap<>();
        luaState = new LuaState();
        luaState.createContext();
        luaJavaInstances = new IntMap<>();

        register(this);
    }

    public void dispose() {
        luaState.destroyContext();
    }

    public LuaState getLuaState() {
        return luaState;
    }

    public void registerGlobalFunction(String name, LuaFunction function) {
        luaState.lua_pushcfunction(function);
        luaState.lua_setglobal(name);
    }

    /**
     *  Register function to table on top of the stack
     */
    public boolean registerFunction(String name, LuaFunction function) {
        if(luaState.lua_istable(-1) != 0) {
            luaState.lua_pushcfunction(function);
            luaState.lua_setfield(-2, name);
            return true;
        }
        return false;
    }

    public String dumpTable() {
        return dumpTable("", luaState);
    }

    public String dumpTable(String table) {
        luaState.lua_getglobal(table);
        return dumpTable(table, luaState);
    }

    public String dumpStack() {
        return dumpStack(luaState);
    }

    public LuaErrorStatus buildScript(String script) {
        LuaErrorStatus status = LuaErrorStatus.get();
        luaState.luaL_loadstring(script);
        status.code = luaState.lua_pcall(0, 0, 0);
        status.error = "";
        if(!status.isValid()) {
            IDLString idlString = luaState.lua_tostring(-1);
            status.error = idlString.c_str();
        }
        return status;
    }

    /**
     * Call function
     */
    public LuaErrorStatus callFunction(int nargs, int nresults, int msgh) {
        LuaErrorStatus status = LuaErrorStatus.get();
        status.code = luaState.lua_pcall(nargs, nresults, msgh);
        status.error = "";
        if(!status.isValid()) {
            IDLString idlString = luaState.lua_tostring(-1);
            status.error = idlString.c_str();
        }
        return status;
    }

    void addObjectInstance(int key, Object object) {
        luaJavaInstances.put(object.hashCode(), object);
    }

    boolean removeObjectInstance(int hash) {
        Object remove = luaJavaInstances.remove(hash);
        return remove != null;
    }

    public void addImportListener(String key, ImportListener importListener) {
        importMap.put(key, importListener);
    }

    public void removeImportListener(String key) {
        importMap.remove(key);
    }

    void register(LuaExtension lua) {
        LuaFunction function = new DefaultImportFunction() {
            @Override
            public int onImport(LuaState luaState) {
                String importParam = luaState.lua_tostring(-1).c_str();
                ImportListener importListener = importMap.get(importParam);
                if(importListener != null) {
                    luaState.lua_pop(-1);
                    return importListener.onImport(luaState);
                }
                else {
                    luaState.luaL_error("Import param is not registered: " + importParam);
                }
                return 0;
            }
        };
        LuaLibrary.addLibrary(lua, "java.import", function);
    }


    /**
     * Return the table log on top of the stack
     */
    public static String dumpTable(String table, LuaState luaState) {
        String dumpTable = "";
        int top = luaState.lua_gettop();
        boolean containsTable = luaState.lua_istable(-1) != 0;
        if(containsTable) {
            IDLString idlString = luaState.dumpTable();
            String s = idlString.c_str();
            IDLBase addr = luaState.lua_topointer(-1);
            dumpTable += "### BEGIN TABLE ###\nTable: " + table + " - Addr: " + addr + "\n"+ s + "### END TABLE ###";
        }
        else {
            dumpTable = table + " is not a table";
        }

        return dumpTable;
    }

    public static String dumpStack(LuaState luaState) {
        IDLString idlString = luaState.dumpStack();
        String s = idlString.c_str();
        return "### BEGIN STACK ###\n" + s + "### END STACK ###";
    }
}
