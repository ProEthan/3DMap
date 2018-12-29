package com.sacc.gaodemap.opengl.cube;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.view.View;
import android.widget.Button;

import com.sacc.gaodemap.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Cube {
    public static final float TEXTURE_NO_ROTATION[] = {
            0.0f, 0.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 1.0f,
            0.0f, 0.0f,

            0.0f, 0.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 1.0f,
            0.0f, 0.0f,

            1.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 1.0f,
            0.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,

            1.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 1.0f,
            0.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,

            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 0.0f,
            0.0f, 0.0f,
            0.0f, 1.0f,

            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 0.0f,
            0.0f, 0.0f,
            0.0f, 1.0f
    };



    public Cube(float width, float height, float depth, Bitmap bitmap)  {

        // 地图坐标系比较大，将值放大以免太小看不见
        width *=  200;
        height *= 200;
        depth *=  200;


        width /= 2;
        height /= 2;

        //尽量不要让z轴有负数出现
        float vertices1[] = {
                -width, -height, 2 * depth,
                width, -height, 2 * depth,
                width,  height, 2 * depth,
                width,  height, 2 * depth,
                -width,  height, 2 * depth,
                -width, -height, 2 * depth,

                -width, -height,  3 * depth,
                width, -height,  3 * depth,
                width,  height,  3 * depth,
                width,  height,  3 * depth,
                -width,  height,  3 * depth,
                -width, -height,  3 * depth,

                -width,  height,  3 * depth,
                -width,  height, 2 * depth,
                -width, -height, 2 * depth,
                -width, -height, 2 * depth,
                -width, -height,  3 * depth,
                -width,  height,  3 * depth,

                width,  height,  3 * depth,
                width,  height, 2 * depth,
                width, -height, 2 * depth,
                width, -height, 2 * depth,
                width, -height,  3 * depth,
                width,  height,  3 * depth,

                -width, -height, 2 * depth,
                width, -height, 2 * depth,
                width, -height,  3 * depth,
                width, -height,  3 * depth,
                -width, -height,  3 * depth,
                -width, -height, 2 * depth,

                -width,  height, 2 * depth,
                width,  height, 2 * depth,
                width,  height,  3 * depth,
                width,  height,  3 * depth,
                -width,  height,  3 * depth,
                -width,  height, 2 * depth,

        };

        textureId = OpenGLUtils.loadTexture(bitmap, textureId, true); // 加载纹理


        // 纹理数组缓冲器
        mGLTextureBuffer = ByteBuffer.allocateDirect(TEXTURE_NO_ROTATION.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mGLTextureBuffer.put(TEXTURE_NO_ROTATION).position(0);

        // 顶点数组缓冲器
        vertextBuffer = ByteBuffer.allocateDirect(vertices1.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertextBuffer.put(vertices1).position(0);

    }



    private FloatBuffer vertextBuffer;
    private FloatBuffer mGLTextureBuffer;

    private int textureId = -1;
    protected int mGLProgId;
    protected int mGLAttribPosition;
    protected int mGLUniformTexture;
    protected int mGLAttribTextureCoordinate;

    private Bitmap bitmap;

    private int mMatrixHandler;



    class MyShader {


        String vertexShader =
                "attribute vec3 position;\n" + // 顶点着色器的顶点坐标,由外部程序传入
                "attribute vec4 inputTextureCoordinate;\n" + // 传入的纹理坐标
                "uniform mat4 model;"+
                "uniform mat4 view;"+
                "uniform mat4 projection;"+
                " \n" +
                "varying vec2 textureCoordinate;\n" +
                " \n" +
                "void main()\n" +
                "{\n" +
                "    vec3 FragPos = vec3(model * vec4(position, 1.0));\n"+
                "    gl_Position = projection * view * vec4(FragPos, 1.0);\n" +
                "    textureCoordinate = inputTextureCoordinate.xy;\n" + // 最终顶点位置
                "}";

        String fragmentShader =
                "varying highp vec2 textureCoordinate;\n" + // 最终顶点位置，上面顶点着色器的varying变量会传递到这里
                " \n" +
                "uniform sampler2D inputImageTexture;\n" + // 外部传入的图片纹理 即代表整张图片的数据
                " \n" +
                "void main()\n" +
                "{\n" +
                "     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);\n" +  // 调用函数 进行纹理贴图
                "}";

        public void create() {

            mGLProgId = OpenGLUtils.loadProgram(vertexShader, fragmentShader); // 编译链接着色器，创建着色器程序
            mGLAttribPosition = GLES20.glGetAttribLocation(mGLProgId, "position"); // 顶点着色器的顶点坐标
            mGLUniformTexture = GLES20.glGetUniformLocation(mGLProgId, "inputImageTexture"); // 传入的图片纹理
            mGLAttribTextureCoordinate = GLES20.glGetAttribLocation(mGLProgId, "inputTextureCoordinate"); // 顶点着色器的纹理坐标

        }
    }

    MyShader shader;

    public void initShader() {
        shader = new MyShader();
        shader.create();
    }

    public void drawES20(float[] model, float[] view, float[] projection) {

        GLES20.glUseProgram(mGLProgId);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        int mModelHandler = GLES20.glGetUniformLocation(mGLProgId,"model");
        GLES20.glUniformMatrix4fv(mModelHandler,1,false, model, 0);
        int mViewHandler = GLES20.glGetUniformLocation(mGLProgId,"view");
        GLES20.glUniformMatrix4fv(mViewHandler,1,false, view, 0);
        int mProjectionHandler = GLES20.glGetUniformLocation(mGLProgId,"projection");
        GLES20.glUniformMatrix4fv(mProjectionHandler,1,false, projection, 0);

        // 顶点着色器的顶点坐标
        vertextBuffer.position(0);GLES20.glEnableVertexAttribArray(mGLAttribPosition);
        GLES20.glVertexAttribPointer(mGLAttribPosition, 3, GLES20.GL_FLOAT, false, 0, vertextBuffer);

        // 顶点着色器的纹理坐标
        mGLTextureBuffer.position(0);GLES20.glEnableVertexAttribArray(mGLAttribTextureCoordinate);
        GLES20.glVertexAttribPointer(mGLAttribTextureCoordinate, 2, GLES20.GL_FLOAT, false, 0, mGLTextureBuffer);

        // 传入的图片纹理
        if (textureId != OpenGLUtils.NO_TEXTURE) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
            GLES20.glUniform1i(mGLUniformTexture, 0);
        }

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 36);

        GLES20.glDisableVertexAttribArray(mGLAttribPosition);
        GLES20.glDisableVertexAttribArray(mGLAttribTextureCoordinate);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

}
