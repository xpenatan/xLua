package lua;

public class LuaErrorStatus {
    private LuaErrorStatus() {}

    static LuaErrorStatus errorStatus = new LuaErrorStatus();
    public static LuaErrorStatus get() { errorStatus.reset(); return errorStatus; }

    int code;
    String error;

    public boolean isValid() {
        return code == 0;
    }

    public int getCode() {
        return code;
    }

    public String getError() {
        return error;
    }

    void reset() {
        code = 0;
        error = "";
    }
}