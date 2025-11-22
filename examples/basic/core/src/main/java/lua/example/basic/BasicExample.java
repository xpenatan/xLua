package lua.example.basic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.github.xpenatan.jParser.idl.IDLBase;
//import imgui.ImGui;
//import imgui.ImTemp;
//import imgui.enums.ImGuiCond;
//import imgui.extension.textedit.LanguageDefinitionId;
//import imgui.extension.textedit.TextEditor;
//import imgui.idl.helper.IDLInt;
//import imgui.idl.helper.IDLTemp;
import lua.extension.LuaErrorStatus;
import lua.extension.LuaExt;
import lua.LuaState;
import lua.example.basic.register.gdx.LuaGdx;

public class BasicExample extends ScreenAdapter {

//    private TextEditor editor;
    private SpriteBatch batch;
    public LuaExt lua;
    boolean luaError;

    ShapeRenderer renderer = new ShapeRenderer();

    @Override
    public void show() {
        super.show();
//        editor = new TextEditor();

//        LanguageDefinitionId languageDefinition = LanguageDefinitionId.Lua;
//        editor.SetLanguageDefinition(languageDefinition);

        String code = Gdx.files.internal("data/script.lua").readString();
//        editor.SetText(code);
        lua = new LuaExt();

        LuaState luaState = lua.getLuaState();

        LuaGdx luaGdx = new LuaGdx();

        luaGdx.register(lua);

        buildScript(code);
    }

    int moveState = 1;
    // 1 = RIGHT
    // 2 = TOP
    // 3 = LEFT
    // 4 = BOTTOM

    float recX = 50;
    float recY = 50;


    @Override
    public void render(float delta) {
        ScreenUtils.clear(1f, 1f, 1f, 1f);
        update();
    }

    public void update() {
        if(!luaError) {
            LuaState luaState = lua.getLuaState();
            // Call function onRender if exist
            luaState.lua_getglobal("render");
            if(luaState.lua_isnil(-1) != 0 || luaState.lua_isfunction(-1) == 0) {
                //Pop because it's not a function
                luaState.lua_pop(1);
                luaError = true;
            }
            else {
                LuaErrorStatus status = lua.callFunction(0, 0, 0);
                if(!status.isValid()) {
                    String errorText = status.getError();
                    System.err.println(errorText);
                    luaError = true;
                }
            }
        }
    }

//    void renderEditText() {
//        IDLInt ind1 = IDLTemp.Int_1(0);
//        IDLInt ind2 = IDLTemp.Int_2(0);
//        editor.GetCursorPosition(ind1, ind2);
//
//        int cursorX = ind1.getValue();
//        int cursorY = ind2.getValue();
//
//        ImGui.SetNextWindowSize(ImTemp.ImVec2_1(600, 500), ImGuiCond.Once);
//        ImGui.Begin("Editor");
//        String text = "\t" + (cursorX + 1) + "/" + (cursorY + 1) + " " + editor.GetLineCount() + " | " + (editor.CanUndo() ? "*" : " ") + " | " + editor.GetLanguageDefinitionName().c_str();
//        if(ImGui.Button("Build")) {
//            String scriptText = editor.GetText().c_str();
//            buildScript(scriptText);
//        }
//        ImGui.SameLine();
//        ImGui.Text(text);
//        editor.Render("Title");
//        ImGui.End();
//    }

    public void buildScript(String scriptCode) {
        LuaErrorStatus status = lua.buildScript(scriptCode);
        IDLBase pointer = lua.getLuaState().lua_topointer(-1);
//        String s = lua.dumpTable("ShapeRenderer");
        String ShapeRenderer = lua.dumpTable("ShapeRenderer");
        String renderer = lua.dumpTable("renderer");
        String GL20 = lua.dumpTable("GL20");
        String Gdx = lua.dumpTable("Gdx");
        luaError = false;
        if(!status.isValid()) {
            String errorText = status.getError();
            System.err.println(errorText);
            luaError = true;
        }
    }
}