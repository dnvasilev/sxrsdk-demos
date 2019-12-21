/* Copyright 2015 Samsung Electronics Co., LTD
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.samsungxr.complexscene;

import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRImportSettings;
import com.samsungxr.SXRMain;
import com.samsungxr.SXRMaterial;
import com.samsungxr.SXRMesh;
import com.samsungxr.SXRPerspectiveCamera;
import com.samsungxr.SXRRenderData;
import com.samsungxr.SXRRenderPass;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRNode;
import com.samsungxr.SXRTexture;
import com.samsungxr.SxrViewManager;
import com.samsungxr.utility.Log;

import java.io.IOException;
import java.util.EnumSet;

import static com.samsungxr.SXRImportSettings.NO_LIGHTING;

public class SampleMain extends SXRMain {

    @Override
    public SplashMode getSplashMode() {
        return SplashMode.NONE;
    }

    @Override
    public void onInit(SXRContext sxrContext) throws IOException {
        // set background color
        SXRTexture videoTexture = sxrContext.getAssetLoader().loadTexture(new SXRAndroidResource(sxrContext, R.drawable.samsung_xr_512x512));
        SXRScene scene = sxrContext.getMainScene();
        scene.setBackgroundColor(1, 1, 1, 1);
        scene.setFrustumCulling(false);

        float NORMAL_CURSOR_SIZE = 0.4f;
        float CURSOR_Z_POSITION = -9.0f;
        int CURSOR_RENDER_ORDER = 100000;

        SXRNode cursor = new SXRNode(sxrContext,
                NORMAL_CURSOR_SIZE, NORMAL_CURSOR_SIZE,
                sxrContext.getAssetLoader().loadTexture(new SXRAndroidResource(sxrContext, "cursor_idle.png")));
        cursor.getTransform().setPositionZ(CURSOR_Z_POSITION);
        cursor.setName("cursor");
        cursor.getRenderData()
                .setRenderingOrder(SXRRenderData.SXRRenderingOrder.OVERLAY)
                .setDepthTest(false)
                .setRenderingOrder(CURSOR_RENDER_ORDER)
                .setLayer(SXRRenderData.LayerType.Cursor);
        scene.getMainCameraRig().addChildObject(cursor);

        SXRNode logo = new SXRNode(sxrContext, 1, 1, videoTexture);
        logo.getTransform().setPosition(0,0,-5);
        logo.setName("logo");
        scene.addNode(logo);


        if(false) {
            float fov = scene.getMainCameraRig().getCenterCamera().getFovY()*(float)Math.PI/180.f;
            float planesize = 2.f*(float)Math.tan(fov/2.f);
            SXRNode video = new SXRNode(sxrContext,
                1000*planesize, 1000*planesize,
                sxrContext.getAssetLoader().loadTexture(new SXRAndroidResource(sxrContext, R.drawable.samsung_xr_512x512)));
        video.getTransform().setPositionZ(-1000.f);
        //video.getTransform().setRotationByAxis(90.f, 0.f, 1.f, 0.f);
        video.setName("video");
        video.getRenderData()
                .setRenderingOrder(SXRRenderData.SXRRenderingOrder.OVERLAY)
                .setDepthTest(false)
                .setRenderingOrder(50000)
                .setLayer(SXRRenderData.LayerType.Video)
                .setCullFace(SXRRenderPass.SXRCullFaceEnum.None);
        scene.getMainCameraRig().addChildObject(video);
        } else {
            int id = videoTexture.getId();
            while ( id == 0) {
                Log.e("sxr-complexscene","texid = %d", id);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException c) {
                }
                id = videoTexture.getId();
            }
            Log.e("sxr-complexscene","texid = %d", id);

            ((SxrViewManager)sxrContext).setVideoTextureId(id);
        }

        try {
            EnumSet<SXRImportSettings> settings = SXRImportSettings.getRecommendedSettingsWith(EnumSet.of(NO_LIGHTING));
            SXRMesh mesh = sxrContext.getAssetLoader().loadMesh(
                    new SXRAndroidResource(sxrContext, "bunny.obj"),
                    settings);

            final int OBJECTS_CNT = 8;
            for (int x=-OBJECTS_CNT; x<=OBJECTS_CNT; ++x) {
                for (int y=-OBJECTS_CNT; y<=OBJECTS_CNT; ++y) {
                    SXRNode sceneObject = getColorMesh(1.0f, mesh);
                    sceneObject.setName("bunny"+x+""+y);
                    sceneObject.getTransform().setPosition(1.0f*x, 1.0f*y, -7.5f);
                    sceneObject.getTransform().setScale(0.5f, 0.5f, 1.0f);
                    scene.addNode(sceneObject);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private SXRNode getColorMesh(float scale, SXRMesh mesh) {
        SXRMaterial material = new SXRMaterial(getSXRContext(), SXRMaterial.SXRShaderType.Color.ID);
        material.setColor(1.0f, 0.0f, 1.0f);

        SXRNode meshObject = new SXRNode(getSXRContext(), mesh);
        meshObject.getTransform().setScale(scale, scale, scale);
        meshObject.getRenderData().setMaterial(material);

        return meshObject;
    }
}
