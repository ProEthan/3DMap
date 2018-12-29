package com.sacc.gaodemap.opengl.cube;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.opengl.GLES20;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static javax.microedition.khronos.opengles.GL10.GL_TRIANGLE_STRIP;
import static javax.microedition.khronos.opengles.GL10.GL_UNSIGNED_SHORT;

public class Model {
    public Model(InputStream in, Bitmap bitmap){

        this.in = in;

        this.textureId = OpenGLUtils.loadTexture(bitmap, this.textureId, true); // 加载纹理

        loadFromFile();

        // 纹理数组缓冲器
        this.mGLTextureBuffer = ByteBuffer.allocateDirect(this.texCoords.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        this.mGLTextureBuffer.put(this.texCoords).position(0);

        // 顶点数组缓冲器
        this.vertextBuffer = ByteBuffer.allocateDirect(this.vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        this.vertextBuffer.put(this.vertices).position(0);

        // 法向量数组缓冲器
        this.normalBuffer = ByteBuffer.allocateDirect(this.normals.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        this.normalBuffer.put(this.normals).position(0);

//        // 索引数组缓冲器
//        ByteBuffer cc= ByteBuffer.allocateDirect(index.length*2);
//        cc.order(ByteOrder.nativeOrder());
//        indexBuffer=cc.asShortBuffer();
//        indexBuffer.put(index);
//        indexBuffer.position(0);

        System.out.println(a);
    }

    private FloatBuffer vertextBuffer;
    private FloatBuffer mGLTextureBuffer;
    private FloatBuffer normalBuffer;
    private ShortBuffer indexBuffer;

    private InputStream in;

    private int textureId = -1;

    private int mGLProgId;

    private int mGLAttribPosition;
    private int mGLUniformTexture;
    private int mGLAttribTextureCoordinate;
    private int mGLAttributeNormal;

    private int mMatrixHandler;

    private float[] vertices;
    private float[] texCoords;
    private float[] normals;
//    private short[] index;

    private int a;

    class Shader {

        String vertexShader =
                        "attribute vec3 aPos;\n" + // 顶点着色器的顶点坐标,由外部程序传入
                        "attribute vec2 aTexCoords;\n" + // 传入的纹理坐标
                        "attribute vec3 aNormal;"+
                        " \n"+
                        "uniform mat4 model;"+
                        "uniform mat4 view;"+
                        "uniform mat4 projection;"+
                        " \n" +
                        "varying highp vec2 TexCoords;\n" +
                        "varying vec3 Normal;\n" +
                        "varying vec3 FragPos;\n" +
                        " \n" +
                        "void main()\n" +
                        "{\n" +
                        "    FragPos = vec3(model * vec4(aPos, 1.0));\n"+
                        "    TexCoords = aTexCoords;\n"+
                        "    Normal = mat3(model) * aNormal;\n" +
                        "    gl_Position = projection * view * vec4(FragPos, 1.0);\n" + // 最终顶点位置
                        "}";

        String fragmentShader =
                        "varying highp vec2 TexCoords;\n" +
                        "varying vec3 Normal;\n" +
                        "varying vec3 FragPos;\n" +
                        " \n" +
                        "uniform sampler2D inputImageTexture;\n" + // 外部传入的图片纹理 即代表整张图片的数据
                        "uniform vec3 lightPos;\n" +
                        "uniform vec3 lightColor;\n" +
                        " \n" +
                        "void main()\n" +
                        "{\n" +
                        "       vec3 ambient = (0.2, 0.1, 0.1) * texture2D(inputImageTexture, TexCoords).rgb;\n" +
                        "       vec3 norm = normalize(Normal);\n" +
                        "       vec3 lightDir = normalize(lightPos - FragPos);\n" +
                        "       float diff = max(0.2 * dot(norm, lightDir), 0.5);\n" +
                        "       vec3 diffuse = (0.5, 0.5, 0.5) * diff * texture2D(inputImageTexture, TexCoords).rgb;;\n" +
                        "       vec3 result = ambient + diffuse;\n" +
                        "       gl_FragColor = vec4(result, 1.0);\n" +
                        "}";

        public void create() {

            mGLProgId = OpenGLUtils.loadProgram(vertexShader, fragmentShader); // 编译链接着色器，创建着色器程序

            mGLAttribPosition = GLES20.glGetAttribLocation(mGLProgId, "aPos"); // 顶点着色器的顶点坐标
            mGLAttribTextureCoordinate = GLES20.glGetAttribLocation(mGLProgId, "aTexCoords"); // 顶点着色器的纹理坐标
            mGLAttributeNormal = GLES20.glGetAttribLocation(mGLProgId, "aNormal"); // 顶点着色器的法向量坐标

            mGLUniformTexture = GLES20.glGetUniformLocation(mGLProgId, "inputImageTexture"); // 传入的图片纹理

        }
    }

    Shader shader;

    public void initShader() {
        shader = new Shader();
        shader.create();
    }

    private final float[] lightPos = {0.0f, 0.0f, 100.0f};
    private final float[] lightColor = {0.2f, 0.2f, 0.2f};

    public void drawES20(float[] model, float[] view, float[] projection) {

        GLES20.glUseProgram(mGLProgId);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);


        int lightPosHandler = GLES20.glGetUniformLocation(mGLProgId,"lightPos");
        GLES20.glUniform3f(lightPosHandler,200.0f, -100.0f, 2000.0f);
        int lightColorHandler = GLES20.glGetUniformLocation(mGLProgId,"lightColor");
        GLES20.glUniform3f(lightColorHandler,0.2f, 0.2f, 0.2f);

        int mModelHandler = GLES20.glGetUniformLocation(mGLProgId,"model");
        GLES20.glUniformMatrix4fv(mModelHandler,1,false, model, 0);
        int mViewHandler = GLES20.glGetUniformLocation(mGLProgId,"view");
        GLES20.glUniformMatrix4fv(mViewHandler,1,false, view, 0);
        int mProjectionHandler = GLES20.glGetUniformLocation(mGLProgId,"projection");
        GLES20.glUniformMatrix4fv(mProjectionHandler,1,false, projection, 0);



        // 顶点着色器的顶点坐标
        vertextBuffer.position(0);
        GLES20.glEnableVertexAttribArray(mGLAttribPosition);
        GLES20.glVertexAttribPointer(mGLAttribPosition, 3, GLES20.GL_FLOAT, false, 0, vertextBuffer);

        // 顶点着色器的纹理坐标
        mGLTextureBuffer.position(0);
        GLES20.glEnableVertexAttribArray(mGLAttribTextureCoordinate);
        GLES20.glVertexAttribPointer(mGLAttribTextureCoordinate, 2, GLES20.GL_FLOAT, false, 0, mGLTextureBuffer);

        // 顶点着色器的法向量坐标
        normalBuffer.position(0);
        GLES20.glEnableVertexAttribArray(mGLAttributeNormal);
        GLES20.glVertexAttribPointer(mGLAttributeNormal, 3, GLES20.GL_FLOAT, false, 0, normalBuffer);

        // 传入的图片纹理
        if (textureId != OpenGLUtils.NO_TEXTURE) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
            GLES20.glUniform1i(mGLUniformTexture, 0);
        }

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertices.length / 3);
//        GLES20.glDrawElements(GLES20.GL_TRIANGLES, index.length, GLES20.GL_UNSIGNED_SHORT, indexBuffer);

        GLES20.glDisableVertexAttribArray(mGLAttribPosition);
        GLES20.glDisableVertexAttribArray(mGLAttribTextureCoordinate);
        GLES20.glDisableVertexAttribArray(mGLAttributeNormal);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    public void loadFromFile() {
        //原始顶点坐标列表--直接从obj文件中加载
        ArrayList<Float> alv = new ArrayList<Float>();
        //法向量坐标
        ArrayList<Float> aln = new ArrayList<Float>();
        //顶点组装面索引列表--根据面的信息从文件中加载
        ArrayList<Short> alFaceIndex = new ArrayList<>();

        //结果顶点坐标列表--按面组织好
        ArrayList<Float> alvResult = new ArrayList<Float>();
        //结果法向量坐标列表--按面组织好
        ArrayList<Float> alnResult = new ArrayList<Float>();

        //原始纹理坐标列表
        ArrayList<Float> alt = new ArrayList<Float>();
        //结果纹理坐标列表
        ArrayList<Float> altResult = new ArrayList<Float>();

        try {
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(isr);
            String temps = null;

            //扫面文件，根据行类型的不同执行不同的处理逻辑
            while ((temps = br.readLine()) != null) {
                //用空格分割行中的各个组成部分
                String[] tempsa = temps.split("[ ]+");
                if (tempsa[0].trim().equals("v")) {//此行为顶点坐标
                    //若为顶点坐标行则提取出此顶点的XYZ坐标添加到原始顶点坐标列表中
                    alv.add(Float.parseFloat(tempsa[1]));
                    alv.add(Float.parseFloat(tempsa[2]));
                    alv.add(Float.parseFloat(tempsa[3]));
                }else if(tempsa[0].trim().equals("vn")){
                    // 若为法向量坐标
                    aln.add(Float.parseFloat(tempsa[1]));
                    aln.add(Float.parseFloat(tempsa[2]));
                    aln.add(Float.parseFloat(tempsa[3]));
                }
                else if (tempsa[0].trim().equals("vt")) {//此行为纹理坐标行
                    //若为纹理坐标行则提取ST坐标并添加进原始纹理坐标列表中
                    alt.add(Float.parseFloat(tempsa[1]));
                    alt.add(Float.parseFloat(tempsa[2]));
                } else if (tempsa[0].trim().equals("f")) {//此行为三角形面
                    /*
                     *若为三角形面行则根据 组成面的顶点的索引从原始顶点坐标列表中
                     *提取相应的顶点坐标值添加到结果顶点坐标列表中，同时根据三个
                     *顶点的坐标计算出此面的法向量并添加到平均前各个索引对应的点
                     *的法向量集合组成的Map中
                     */

                    short[] index = new short[3];//三个顶点索引值的数组

                    //计算第0个顶点的索引，并获取此顶点的XYZ三个坐标
                    index[0] = (short) (Integer.parseInt(tempsa[1].split("/")[0]) - 1);
                    float x0 = alv.get(3 * index[0]);
                    float y0 = alv.get(3 * index[0] + 1);
                    float z0 = alv.get(3 * index[0] + 2);
                    alvResult.add(x0);
                    alvResult.add(y0);
                    alvResult.add(z0);

                    //计算第1个顶点的索引，并获取此顶点的XYZ三个坐标
                    index[1] = (short) (Integer.parseInt(tempsa[2].split("/")[0]) - 1);
                    float x1 = alv.get(3 * index[1]);
                    float y1 = alv.get(3 * index[1] + 1);
                    float z1 = alv.get(3 * index[1] + 2);
                    alvResult.add(x1);
                    alvResult.add(y1);
                    alvResult.add(z1);

                    //计算第2个顶点的索引，并获取此顶点的XYZ三个坐标
                    index[2] = (short) (Integer.parseInt(tempsa[3].split("/")[0]) - 1);
                    float x2 = alv.get(3 * index[2]);
                    float y2 = alv.get(3 * index[2] + 1);
                    float z2 = alv.get(3 * index[2] + 2);
                    alvResult.add(x2);
                    alvResult.add(y2);
                    alvResult.add(z2);

                    //记录此面的顶点索引
                    alFaceIndex.add(index[0]);
                    alFaceIndex.add(index[1]);
                    alFaceIndex.add(index[2]);



                    //将纹理坐标组织到结果纹理坐标列表中
                    //第1个顶点的纹理坐标
                    int indexTex = Integer.parseInt(tempsa[1].split("/")[1]) - 1;
                    altResult.add(alt.get(indexTex * 2));
                    altResult.add(alt.get(indexTex * 2 + 1));
                    //第2个顶点的纹理坐标
                    indexTex = Integer.parseInt(tempsa[2].split("/")[1]) - 1;
                    altResult.add(alt.get(indexTex * 2));
                    altResult.add(alt.get(indexTex * 2 + 1));
                    //第3个顶点的纹理坐标
                    indexTex = Integer.parseInt(tempsa[3].split("/")[1]) - 1;
                    altResult.add(alt.get(indexTex * 2));
                    altResult.add(alt.get(indexTex * 2 + 1));


                    // 获取结果法向量坐标
                    // 第1个顶点的法向量坐标
                    int indexNor = Integer.parseInt(tempsa[1].split("/")[2]) - 1;
                    alnResult.add(aln.get(indexNor * 3));
                    alnResult.add(aln.get(indexNor * 3 + 1));
                    alnResult.add(aln.get(indexNor * 3 + 2));
                    // 第2个顶点的法向量坐标
                    indexNor = Integer.parseInt(tempsa[2].split("/")[2]) - 1;
                    alnResult.add(aln.get(indexNor * 3));
                    alnResult.add(aln.get(indexNor * 3 + 1));
                    alnResult.add(aln.get(indexNor * 3 + 2));
                    // 第3个顶点的法向量坐标
                    indexNor = Integer.parseInt(tempsa[3].split("/")[2]) - 1;
                    alnResult.add(aln.get(indexNor * 3));
                    alnResult.add(aln.get(indexNor * 3 + 1));
                    alnResult.add(aln.get(indexNor * 3 + 2));
                }
            }

            a=2000;

            //生成顶点数组
            int size = alvResult.size();
            float[] vXYZ = new float[size];
            for (int i = 0; i < size; i++) {
                vXYZ[i] = alvResult.get(i) * 2.0f;
            }
            this.vertices = vXYZ;
            System.out.println("vertices:"+this.vertices.length);
            System.out.println("---------------------------------------------------------------------------");

            //生成纹理数组
            size = altResult.size();
            float[] tST = new float[size];
            for (int i = 0; i < size; i++) {
                tST[i] = altResult.get(i);
            }
            this.texCoords = tST;
            System.out.println("texCoords:"+this.texCoords.length);
            System.out.println("---------------------------------------------------------------------------");

            // 生成法向量数组
            size = alnResult.size();
            float[] nXYZ = new float[size];
            for(int i=0;i<size;i++){
                nXYZ[i]=alnResult.get(i);
            }
            this.normals = nXYZ;
            System.out.println("normals:"+this.normals.length);
            System.out.println("---------------------------------------------------------------------------");

//            // 生成索引数组
//            size = alFaceIndex.size();
//            short[] indexES = new short[size];
//            for(int i=0;i<size;i++){
//                indexES[i]=alFaceIndex.get(i);
//            }
//            this.index = indexES;
//            System.out.println("index:"+this.index.length);

            //创建3D物体对象
        } catch (Exception e) {
            Log.d("load error", "load error");
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
            e.printStackTrace();
        }
    }

}
