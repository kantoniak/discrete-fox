package pl.gov.stat.tapthemap;

import android.content.Context;
import android.content.res.Resources;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import pl.gov.stat.tapthemap.Country;

import static pl.gov.stat.tapthemap.scene.AssetLoader.getCountryBaseObjName;
import static pl.gov.stat.tapthemap.scene.AssetLoader.getCountryNameDrawableName;
import static pl.gov.stat.tapthemap.scene.AssetLoader.getCountryTopObjName;
import static org.junit.Assert.assertNotEquals;

@RunWith(AndroidJUnit4.class)
public class AssetExistenceTest {
    @Test
    public void containCountryAssets() {
        Context context = InstrumentationRegistry.getTargetContext();
        Resources resources = context.getResources();
        String packageName = contextBuildConfig.APPLICATION_ID;

        Country.EU_CODES.forEach((String code) -> {
            final Country country = Country.Builder.fromEuCode(code);
            assertNotEquals(getCountryBaseObjName(country) + " missing", 0, resources.getIdentifier(getCountryBaseObjName(country), "raw", packageName));
            assertNotEquals(getCountryTopObjName(country) + " missing", 0, resources.getIdentifier(getCountryTopObjName(country), "raw", packageName));
            assertNotEquals(getCountryNameDrawableName(country) + " missing", 0, resources.getIdentifier(getCountryNameDrawableName(country), "drawable", packageName));
        });
    }
}
