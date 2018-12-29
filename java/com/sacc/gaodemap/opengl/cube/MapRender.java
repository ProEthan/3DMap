package com.sacc.gaodemap.opengl.cube;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.opengl.Matrix;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.CustomRenderer;
import com.amap.api.maps.model.LatLng;

import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MapRender implements CustomRenderer {

    private float[] translate_vector = new float[4];
    public static float SCALE = 0.005F;// 缩放暂时使用这个

    private LatLng center = new LatLng(39.90403, 116.407525);// 北京市经纬度

    private Cube cube;

    private Model man, arrow_1, arrow_2;

    private AMap aMap;

    float width, height;

    private Bitmap bitmap_man,bitmap_arrow1,bitmap_arrow2,bitmap_cube;

    private InputStream inputStream_arrow_1, inputStream_arrow_2, inputStream_man;

    private float[] mMatrixCurrent=     //原始矩阵
            {1,0,0,0,
                    0,1,0,0,
                    0,0,1,0,
                    0,0,0,1};
    private float[] mMatrixCurrent_cube1=     //原始矩阵
            {1,0,0,0,
                    0,1,0,0,
                    0,0,1,0,
                    0,0,0,1};
    private float[] mMatrixCurrent_cube2=     //原始矩阵
            {1,0,0,0,
                    0,1,0,0,
                    0,0,1,0,
                    0,0,0,1};

    public MapRender(AMap aMap, Bitmap bitmap_man, Bitmap bitmap_cube,Bitmap bitmap_arrow1,Bitmap bitmap_arrow2, InputStream inputStream_arrow_1, InputStream inputStream_arrow_2, InputStream inputStream_man) {
        this.aMap = aMap;
        this.bitmap_man=bitmap_man;
        this.bitmap_arrow1=bitmap_arrow1;
        this.bitmap_arrow2=bitmap_arrow2;
        this.bitmap_cube=bitmap_cube;
        this.inputStream_arrow_1=inputStream_arrow_1;
        this.inputStream_arrow_2=inputStream_arrow_2;
        this.inputStream_man=inputStream_man;
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center,15));

        mQueue=new LinkedList<float[]>();
    }

    float[] model = new float[16];
    float[] projection = new float[16];
    float[] view = new float[16];

    @Override
    public void onDrawFrame(GL10 gl) {

        // 摄影机坐标下物体的偏移
        PointF pointF = aMap.getProjection().toOpenGLLocation(center);
        // 设置投影矩阵
        projection = aMap.getProjectionMatrix();
        // 初始化摄影机矩阵
        view = aMap.getViewMatrix();
        // 设置摄影机坐标下物体的偏移矩阵
        Matrix.translateM(view, 0 , pointF.x , pointF.y  , 0);


        // --------------指向标1的模型矩阵------------------
        float[] mMatrixAdjustDirection_arrow1 = {1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1};
        Matrix.setIdentityM(model, 0);
        // 平移
        Matrix.translateM(model, 0 , -590.0f , -2080.0f  , -0.0f);
        // 调整方向
        Matrix.rotateM(mMatrixAdjustDirection_arrow1,0,-180.0f,1.0f, 0.0f, 0.0f);
        Matrix.multiplyMM(model,0,model,0,mMatrixAdjustDirection_arrow1,0);
        // 绕y轴旋转
        Matrix.rotateM(mMatrixCurrent,0,0.1f,0.0f, 1.0f, 0.0f);
        Matrix.multiplyMM(model,0,model,0,mMatrixCurrent,0);
        // 调整大小
        int scale = 3;
        Matrix.scaleM(model, 0 , scale, scale, scale);
        // -----------绘制----------
        arrow_2.drawES20(model,view,projection);


        // --------------指向标2的模型矩阵------------------
        float[] mMatrixAdjustDirection_arrow2 = {1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1};
        Matrix.setIdentityM(model, 0);
        // 平移
        Matrix.translateM(model, 0 , -520.0f , -1080.0f  , -0.0f);
        // 调整方向
        Matrix.rotateM(mMatrixAdjustDirection_arrow2,0,-180.0f,1.0f, 0.0f, 0.0f);
        Matrix.multiplyMM(model,0,model,0,mMatrixAdjustDirection_arrow2,0);
        // 绕y轴旋转
        Matrix.rotateM(mMatrixCurrent,0,0.1f,0.0f, 1.0f, 0.0f);
        Matrix.multiplyMM(model,0,model,0,mMatrixCurrent,0);
        // 调整大小
        scale = 3;
        Matrix.scaleM(model, 0 , scale, scale, scale);
        // --------绘制----------
        arrow_2.drawES20(model,view,projection);

        float[] mMatrixAdjustDirection_arrow3 = {1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1};
        Matrix.setIdentityM(model, 0);
        // 平移
        Matrix.translateM(model, 0 , -620.0f , -3080.0f  , -0.0f);
        // 调整方向
        Matrix.rotateM(mMatrixAdjustDirection_arrow3,0,-180.0f,1.0f, 0.0f, 0.0f);
        Matrix.multiplyMM(model,0,model,0,mMatrixAdjustDirection_arrow3,0);
        // 绕y轴旋转
        Matrix.rotateM(mMatrixCurrent,0,0.1f,0.0f, 1.0f, 0.0f);
        Matrix.multiplyMM(model,0,model,0,mMatrixCurrent,0);
        // 调整大小
        scale = 3;
        Matrix.scaleM(model, 0 , scale, scale, scale);
        // --------绘制----------
        arrow_2.drawES20(model,view,projection);

        float[] mMatrixAdjustDirection_arrow4 = {1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1};
        Matrix.setIdentityM(model, 0);
        // 平移
        Matrix.translateM(model, 0 , -620.0f , -3780.0f  , -0.0f);
        // 调整方向
        Matrix.rotateM(mMatrixAdjustDirection_arrow4,0,-180.0f,1.0f, 0.0f, 0.0f);
        Matrix.rotateM(mMatrixAdjustDirection_arrow4,0,90.0f,0.0f, 0.0f, 1.0f);
        Matrix.multiplyMM(model,0,model,0,mMatrixAdjustDirection_arrow4,0);
        // 绕y轴旋转
        Matrix.rotateM(mMatrixCurrent,0,0.1f,0.0f, 1.0f, 0.0f);
        Matrix.multiplyMM(model,0,model,0,mMatrixCurrent,0);
        // 调整大小
        scale = 3;
        Matrix.scaleM(model, 0 , scale, scale, scale);
        // --------绘制----------
        arrow_1.drawES20(model,view,projection);

        float[] mMatrixAdjustDirection_arrow5 = {1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1};
        Matrix.setIdentityM(model, 0);
        // 平移
        Matrix.translateM(model, 0 , -1620.0f , -3780.0f  , -0.0f);
        // 调整方向
        Matrix.rotateM(mMatrixAdjustDirection_arrow5,0,-180.0f,1.0f, 0.0f, 0.0f);
        Matrix.rotateM(mMatrixAdjustDirection_arrow5,0,90.0f,0.0f, 0.0f, 1.0f);
        Matrix.multiplyMM(model,0,model,0,mMatrixAdjustDirection_arrow5,0);
        // 绕y轴旋转
        Matrix.rotateM(mMatrixCurrent,0,0.1f,0.0f, 1.0f, 0.0f);
        Matrix.multiplyMM(model,0,model,0,mMatrixCurrent,0);
        // 调整大小
        scale = 3;
        Matrix.scaleM(model, 0 , scale, scale, scale);
        // --------绘制----------
        arrow_1.drawES20(model,view,projection);

        float[] mMatrixAdjustDirection_arrow6 = {1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1};
        Matrix.setIdentityM(model, 0);
        // 平移
        Matrix.translateM(model, 0 , -2620.0f , -3680.0f  , -0.0f);
        // 调整方向
        Matrix.rotateM(mMatrixAdjustDirection_arrow6,0,-180.0f,1.0f, 0.0f, 0.0f);
        Matrix.rotateM(mMatrixAdjustDirection_arrow6,0,90.0f,0.0f, 0.0f, 1.0f);
        Matrix.multiplyMM(model,0,model,0,mMatrixAdjustDirection_arrow6,0);
        // 绕y轴旋转
        Matrix.rotateM(mMatrixCurrent,0,0.1f,0.0f, 1.0f, 0.0f);
        Matrix.multiplyMM(model,0,model,0,mMatrixCurrent,0);
        // 调整大小
        scale = 3;
        Matrix.scaleM(model, 0 , scale, scale, scale);
        // --------绘制----------
        arrow_1.drawES20(model,view,projection);

        float[] mMatrixAdjustDirection_arrow7 = {1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1};
        Matrix.setIdentityM(model, 0);
        // 平移
        Matrix.translateM(model, 0 , -3620.0f , -3680.0f  , -0.0f);
        // 调整方向
        Matrix.rotateM(mMatrixAdjustDirection_arrow7,0,-180.0f,1.0f, 0.0f, 0.0f);
        Matrix.rotateM(mMatrixAdjustDirection_arrow7,0,90.0f,0.0f, 0.0f, 1.0f);
        Matrix.multiplyMM(model,0,model,0,mMatrixAdjustDirection_arrow7,0);
        // 绕y轴旋转
        Matrix.rotateM(mMatrixCurrent,0,0.1f,0.0f, 1.0f, 0.0f);
        Matrix.multiplyMM(model,0,model,0,mMatrixCurrent,0);
        // 调整大小
        scale = 3;
        Matrix.scaleM(model, 0 , scale, scale, scale);
        // --------绘制----------
        arrow_1.drawES20(model,view,projection);

        float[] mMatrixAdjustDirection_arrow8 = {1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1};
        Matrix.setIdentityM(model, 0);
        // 平移
        Matrix.translateM(model, 0 , -4620.0f , -3680.0f  , -0.0f);
        // 调整方向
        Matrix.rotateM(mMatrixAdjustDirection_arrow8,0,-180.0f,1.0f, 0.0f, 0.0f);
        Matrix.rotateM(mMatrixAdjustDirection_arrow8,0,90.0f,0.0f, 0.0f, 1.0f);
        Matrix.multiplyMM(model,0,model,0,mMatrixAdjustDirection_arrow8,0);
        // 绕y轴旋转
        Matrix.rotateM(mMatrixCurrent,0,0.1f,0.0f, 1.0f, 0.0f);
        Matrix.multiplyMM(model,0,model,0,mMatrixCurrent,0);
        // 调整大小
        scale = 3;
        Matrix.scaleM(model, 0 , scale, scale, scale);
        // --------绘制----------
        arrow_1.drawES20(model,view,projection);

        float[] mMatrixAdjustDirection_arrow9 = {1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1};
        Matrix.setIdentityM(model, 0);
        // 平移
        Matrix.translateM(model, 0 , -5620.0f , -3680.0f  , -0.0f);
        // 调整方向
        Matrix.rotateM(mMatrixAdjustDirection_arrow9,0,-180.0f,1.0f, 0.0f, 0.0f);
        Matrix.rotateM(mMatrixAdjustDirection_arrow9,0,90.0f,0.0f, 0.0f, 1.0f);
        Matrix.multiplyMM(model,0,model,0,mMatrixAdjustDirection_arrow9,0);
        // 绕y轴旋转
        Matrix.rotateM(mMatrixCurrent,0,0.1f,0.0f, 1.0f, 0.0f);
        Matrix.multiplyMM(model,0,model,0,mMatrixCurrent,0);
        // 调整大小
        scale = 3;
        Matrix.scaleM(model, 0 , scale, scale, scale);
        // --------绘制----------
        arrow_1.drawES20(model,view,projection);

        float[] mMatrixAdjustDirection_arrow10 = {1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1};
        Matrix.setIdentityM(model, 0);
        // 平移
        Matrix.translateM(model, 0 , -6620.0f , -3580.0f  , -0.0f);
        // 调整方向
        Matrix.rotateM(mMatrixAdjustDirection_arrow10,0,-180.0f,1.0f, 0.0f, 0.0f);
        Matrix.rotateM(mMatrixAdjustDirection_arrow10,0,90.0f,0.0f, 0.0f, 1.0f);
        Matrix.multiplyMM(model,0,model,0,mMatrixAdjustDirection_arrow10,0);
        // 绕y轴旋转
        Matrix.rotateM(mMatrixCurrent,0,0.1f,0.0f, 1.0f, 0.0f);
        Matrix.multiplyMM(model,0,model,0,mMatrixCurrent,0);
        // 调整大小
        scale = 3;
        Matrix.scaleM(model, 0 , scale, scale, scale);
        // --------绘制----------
        arrow_1.drawES20(model,view,projection);

        float[] mMatrixAdjustDirection_arrow11 = {1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1};
        Matrix.setIdentityM(model, 0);
        // 平移
        Matrix.translateM(model, 0 , -7500.0f , -3580.0f  , -0.0f);
        // 调整方向
        Matrix.rotateM(mMatrixAdjustDirection_arrow11,0,-180.0f,1.0f, 0.0f, 0.0f);
        Matrix.multiplyMM(model,0,model,0,mMatrixAdjustDirection_arrow11,0);
        // 绕y轴旋转
        Matrix.rotateM(mMatrixCurrent,0,0.1f,0.0f, 1.0f, 0.0f);
        Matrix.multiplyMM(model,0,model,0,mMatrixCurrent,0);
        // 调整大小
        scale = 3;
        Matrix.scaleM(model, 0 , scale, scale, scale);
        // -----------绘制----------
        arrow_1.drawES20(model,view,projection);


        // ------------------人的模型矩阵--------------------
        float[] mMatrixAdjustDirection_man = {1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1};
        Matrix.setIdentityM(model, 0);
        // 向x轴负方向平移
        Matrix.translateM(model, 0 , -500.0f , -370.0f  , -50.0f);
        // 调整方向
        Matrix.rotateM(mMatrixAdjustDirection_man,0,90.0f,1.0f, 0.0f, 0.0f);
        Matrix.multiplyMM(model,0,model,0,mMatrixAdjustDirection_man,0);
        // 调整大小
        scale = 10;
        Matrix.scaleM(model, 0 , scale, scale, scale);
        // -------绘制----------
        man.drawES20(model,view,projection);



        // --------------------绘制箱子------------------------
        // --------箱子1--------
        // 模型矩阵进行相应的变换
        Matrix.setIdentityM(model, 0);
        // 平移
        Matrix.translateM(model, 0 , -5000.0f , -6080.0f  , -0.0f);
        // 绕z轴旋转
        Matrix.rotateM(mMatrixCurrent_cube1,0,0.2f,0.0f, 0.0f, 1.0f);
        Matrix.multiplyMM(model,0,model,0,mMatrixCurrent_cube1,0);
        // 调整大小
        scale = 13;
        Matrix.scaleM(model, 0 , scale, scale, scale);
        cube.drawES20(model,view,projection);

        // --------箱子2--------
        // 模型矩阵进行相应的变换
        Matrix.setIdentityM(model, 0);
        // 平移
        Matrix.translateM(model, 0 , -3000.0f , -6080.0f  , -0.0f);
        // 绕z轴旋转
        Matrix.rotateM(mMatrixCurrent_cube1,0,0.2f,0.0f, 0.0f, 1.0f);
        Matrix.multiplyMM(model,0,model,0,mMatrixCurrent_cube1,0);
        // 调整大小
        scale = 13;
        Matrix.scaleM(model, 0 , scale, scale, scale);
        cube.drawES20(model,view,projection);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //创建cube
        cube = new Cube(0.2f,0.2f,0.2f, bitmap_cube);
        cube.initShader();

        arrow_1 = new Model(inputStream_arrow_1, bitmap_arrow1);
        arrow_1.initShader();

        arrow_2 = new Model(inputStream_arrow_2, bitmap_arrow2);
        arrow_2.initShader();

        man = new Model(inputStream_man, bitmap_man);
        man.initShader();

    }

    @Override
    public void OnMapReferencechanged() {

    }

    Queue<float[]> mQueue;     //变换矩阵队列

    //保护现场
    public void pushMatrix(){
        mQueue.offer(Arrays.copyOf(mMatrixCurrent,16));
    }

    //恢复现场
    public void popMatrix(){
        mMatrixCurrent=mQueue.poll();
    }

}
