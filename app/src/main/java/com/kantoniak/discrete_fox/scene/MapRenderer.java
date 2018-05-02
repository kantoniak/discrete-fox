package com.kantoniak.discrete_fox.scene;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import com.kantoniak.discrete_fox.Country;

import org.rajawali3d.Object3D;
import org.rajawali3d.cameras.Camera;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.math.vector.Vector2;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Plane;
import org.rajawali3d.renderer.Renderer;
import org.rajawali3d.util.ObjectColorPicker;
import org.rajawali3d.util.OnObjectPickedListener;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MapRenderer extends Renderer implements OnObjectPickedListener {

    private static final Vector2 MAP_SIZE = new Vector2(2.438f, 2.889f);
    private static final Vector2 MAP_CORRECTION = new Vector2(-0.01f, -0.01f);

    private final Context context;
    private final Map map;
    private final AssetLoader loader;
    private RenderingDelegate renderingDelegate;

    private ObjectColorPicker objectPicker;
    private Plane mapBase;

    boolean visible = true;

    public MapRenderer(Context context, Map map) {
        super(context);
        this.context = context;
        this.map = map;
        this.loader = new AssetLoader(context, mTextureManager);
    }

    public void setRenderingDelegate(RenderingDelegate renderingDelegate) {
        this.renderingDelegate = renderingDelegate;
    }

    @Override
    public void onRenderSurfaceCreated(EGLConfig config, GL10 gl, int width, int height) {
        super.onRenderSurfaceCreated(config, gl, width, height);
        if (renderingDelegate != null) {
            renderingDelegate.onSurfaceCreated();
        }
    }

    @Override
    public void onRenderSurfaceSizeChanged(GL10 gl, int width, int height) {
        super.onRenderSurfaceSizeChanged(gl, width, height);
        if (renderingDelegate != null) {
            renderingDelegate.onSurfaceChanged(width, height);
        }
    }

    @Override
    protected void initScene() {
        setupObjectPicker();

        mapBase = new Plane();
        mapBase.setDoubleSided(true);
        mapBase.setScale(MAP_SIZE.getX(), 1, MAP_SIZE.getY());
        mapBase.setPosition(MAP_CORRECTION.getX() * 0.5f, 0, -MAP_CORRECTION.getY() * 0.5f);
        mapBase.rotate(Vector3.Axis.X, +90);
        mapBase.setTransparent(true);
        getCurrentScene().addChild(mapBase);

        try {
            Material mapBaseMaterial = new Material();
            mapBaseMaterial.setColor(0x00000000);
            mapBaseMaterial.addTexture(mTextureManager.addTexture(loader.loadTexture("map_background")));
            mapBase.setMaterial(mapBaseMaterial);
        } catch (ATexture.TextureException e) {
            e.printStackTrace();
        }

        showMap(false);
    }

    public void addCountryInstance(CountryInstance countryInstance) {
        Vector3 worldOffset = new Vector3(-MapRenderer.MAP_SIZE.getX(), 0, MapRenderer.MAP_SIZE.getY()).multiply(0.5f);

        countryInstance.initPositions(worldOffset, map.getCountryMiddle(countryInstance.getCountry()));
        countryInstance.registerObject(getCurrentScene(), objectPicker);
        countryInstance.resetState();
        map.addCountryInstance(countryInstance);
    }

    private void updateVisibility() {
        if (mapBase != null) {
            mapBase.setVisible(visible);
        }
        map.setVisible(visible);
    }

    private void setupObjectPicker() {
        objectPicker = new ObjectColorPicker(this);
        objectPicker.setOnObjectPickedListener(this);
    }

    @Override
    public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            objectPicker.getObjectAt(event.getX(), event.getY());
        }
    }

    @Override
    public void onObjectPicked(@NonNull Object3D object) {
        for (java.util.Map.Entry<Country, CountryInstance> entry: map.getCountries().entrySet()) {
            if (entry.getValue().containsObject(object)) {
                entry.getValue().onPicked();
            }
        }
    }

    @Override
    public void onNoObjectPicked() {
        // Do nothing
    }

    public void setCamera(Camera camera) {
        getCurrentScene().addAndSwitchCamera(camera);
    }

    public AssetLoader getLoader() {
        return loader;
    }

    public Map getMap() {
        return map;
    }

    public void showMap(boolean show) {
        this.visible = show;
        updateVisibility();
    }
}
