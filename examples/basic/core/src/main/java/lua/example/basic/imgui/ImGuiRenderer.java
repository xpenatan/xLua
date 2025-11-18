package lua.example.basic.imgui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
//import imgui.ImDrawData;
//import imgui.ImGui;
//import imgui.ImGuiImpl;
//import imgui.enums.ImGuiConfigFlags;
//import imgui.ImGuiIO;
//import imgui.gdx.ImGuiGdxGLImpl;
//import imgui.gdx.ImGuiGdxInputMultiplexer;

public abstract class ImGuiRenderer extends ScreenAdapter {
//
//    private ImGuiImpl impl;
//
//    protected ImGuiGdxInputMultiplexer input;

    @Override
    public void show() {
//        ImGui.CreateContext();
//
//        ImGuiIO io = ImGui.GetIO();
//        io.set_ConfigFlags(ImGuiConfigFlags.DockingEnable);
//
//        input = new ImGuiGdxInputMultiplexer();
//        impl = new ImGuiGdxGLImpl();
//        Gdx.input.setInputProcessor(input);
    }

    @Override
    public void render(float delta) {
//        impl.newFrame();
//
//        renderImGui();
//
//        ImGui.Render();
//        ImDrawData drawData = ImGui.GetDrawData();
//        impl.render(drawData);
    }

    public abstract void renderImGui();

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
//        impl.dispose();
//        ImGui.DestroyContext();
    }
}