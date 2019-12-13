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

package com.samsungxr.simplesample;

import android.os.Bundle;

import com.samsungxr.SXRActivity;
import com.samsungxr.SXRAndroidResource;
import com.samsungxr.SXRCameraRig;
import com.samsungxr.SXRContext;
import com.samsungxr.SXRMain;
import com.samsungxr.SXRRenderData;
import com.samsungxr.SXRRenderPass;
import com.samsungxr.SXRScene;
import com.samsungxr.SXRNode;
import com.samsungxr.SXRTexture;

import java.io.IOException;

public class SampleActivity extends SXRActivity {

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setMain(new SampleMain());
    }

    private static class SampleMain extends SXRMain {
        @Override
        public void onInit(SXRContext sxrContext) throws IOException {
            SXRScene scene = sxrContext.getMainScene();
            scene.setBackgroundColor(0, 0, 0, 0);

            SXRTexture texture = sxrContext.getAssetLoader().loadTexture(new SXRAndroidResource(sxrContext, R.drawable.samsung_xr_512x512));

            // create a scene object (this constructor creates a rectangular scene
            // object that uses the standard texture shader
            SXRNode sceneObject = new SXRNode(sxrContext, 4.0f, 4.0f, texture);

            // set the scene object position
            sceneObject.getTransform().setPosition(0.0f, 0.0f, -3.0f);

            // add the scene object to the scene graph
            scene.addNode(sceneObject);

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

            float fov = scene.getMainCameraRig().getCenterCamera().getFovY()*(float)Math.PI/180.f;
            float planesize = 2.f*(float)Math.tan(fov/2.f);

            SXRNode video = new SXRNode(sxrContext,
                    100*planesize, 100*planesize,
                    sxrContext.getAssetLoader().loadTexture(new SXRAndroidResource(sxrContext, R.drawable.samsung_xr_512x512)));
            //video.getTransform().setPositionZ(-100.f);
            video.getTransform().setPositionX(-100.f);
            video.getTransform().setRotationByAxis(90.f, 0.f, 1.f, 0.f);
            video.setName("video");
            video.getRenderData()
                    .setRenderingOrder(SXRRenderData.SXRRenderingOrder.OVERLAY)
                    .setDepthTest(false)
                    .setRenderingOrder(50000)
                    //.setLayer(SXRRenderData.LayerType.Video)
                    .setCullFace(SXRRenderPass.SXRCullFaceEnum.None);
            //scene.getMainCameraRig().addChildObject(video);
            scene.addNode(video);
        }
    }
}
