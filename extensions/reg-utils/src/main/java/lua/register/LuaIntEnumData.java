package lua.register;

import java.util.List;

public class LuaIntEnumData {
    public String key;
    public int value;

    public static void addEnum(List<LuaIntEnumData> enumDataList, String key, int value) {
        LuaIntEnumData data = new LuaIntEnumData();
        data.key = key;
        data.value = value;
        enumDataList.add(data);
    }
}
