package com.example.penview;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.glCreateProgram;

/**
 * @author Henry
 * @Date 2020-02-08  00:59
 * @Email 2427417167@qq.com
 */
public class Triangle {


    private final float[] projectionMatrix = new float[16];

    private final float[] viewMatrix = new float[16];

//    private final String vertexShaderCode =
//            // This matrix member variable provides a hook to manipulate
//            // the coordinates of the objects that use this vertex shader
//            "uniform mat4 uMVPMatrix;" +
//                    "attribute vec4 vPosition;" +
//                    "void main() {" +
//                    // the matrix must be included as a modifier of gl_Position
//                    // Note that the uMVPMatrix factor *must be first* in order
//                    // for the matrix multiplication product to be correct.
//                    "  gl_Position = uMVPMatrix * vPosition;" +
//                    "}";


    //todo 1st :Vertex Shader - OpenGL ES graphics code for rendering the vertices of a shape
    //attribute ：使用顶点数组封装每个顶点的数据，一般用于每个顶点都各不相同的变量，如顶点位置、颜色等
    //vec4：表示包含了4个浮点数的向量
    //Shaders contain OpenGL Shading Language (GLSL)
    // code that must be compiled prior to using it in the OpenGL ES
    //environment. To compile this code, create a utility method in your renderer class:
    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "}";

    //片段着色器
    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";
    private final int mProgram;

    //顶点的数量，这里是9/3=3个顶点
    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    //顶点的步态，即间隔，每个vertex为4个字节
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    private int positionHandle;
    private int colorHandle;




    //float占四字节
    FloatBuffer vertexBuffer;
    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static float triangleCoords[] = {   // in counterclockwise order:
            0.0f, 0.622008459f, 0.0f, // top
            -0.5f, -0.311004243f, 0.0f, // bottom left
            0.5f, -0.311004243f, 0.0f  // bottom right
    };

    // Set color with red, green, blue and alpha (opacity) values
    float color[] = {0f, 1f, 1f, 1.0f};
    private int vPMatrixHandle;


    public Triangle() {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                triangleCoords.length * 4);
        // use the device hardware's native byte order
        //大端和小端的模式选择
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        //square的时候使用的asShort,vertex的缓冲区的buffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        //添加刚才定义的顶点坐标，共9个点3*3
        vertexBuffer.put(triangleCoords);
        // set the buffer to read the first coordinate
        //设置读取第0个坐标
        vertexBuffer.position(0);

        //todo
        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);
        // // 创建 OpenGL 程序
        mProgram = glCreateProgram();//create empty opengl es program
        //add vertex shader to program
        //链接顶点着色器
        GLES20.glAttachShader(mProgram, vertexShader);
        //链接片段着色器
        GLES20.glAttachShader(mProgram, fragmentShader);
//Compiling OpenGL ES shaders and linking programs is
// expensive in terms of CPU cycles and processing time,
        //they only get created once and then cached for later use
        GLES20.glLinkProgram(mProgram);
    }

//    public void draw(float[] mvpMatrix){
//
//    }

    public void draw(float[] mvpMatrix) {
        // Add program to OpenGL ES environment
        ////使用着色器程序绘制图形
        GLES20.glUseProgram(mProgram);

        //当我们需要改变的东西如位置，颜色，矩阵变换等，
        // 都需要先获取这个变量的id，然后对这个变量id进行操作，就可以改变我们的绘制内容
        // get handle to vertex shader's vPosition member,得到shader的vPosition的值
        //getAttribLocation
//        /**
//         * attribute vec4 vPosition;" +
//         *                     "void main() {" +
//         *                     "  gl_Position = vPosition;" +
//         *                     "}
//         */
//        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
//        //enable a handle to the triangle vertices
//        ////启用顶点变量位置id，之后才能传入顶点位置
//        //使能三角顶点
        GLES20.glEnableVertexAttribArray(positionHandle);
//        //准备三角形的坐标数据
//        //prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);
//
//        vPMatrixHandle = glGetUniformLocation(mProgram,"uMVPMatrix");
//        glUniformMatrix4fv(vPMatrixHandle,1,false,mvpMatrix,0);
//        //get handle to vertex shader's vColor member
//        //得到顶点着色器的颜色分量的句柄
//        //getUniformLocation
//        /**
//         *    "precision mediump float;" +
//         *                     "uniform vec4 vColor;" +
//         *                     "void main() {" +
//         *                     "  gl_FragColor = vColor;" +
//         *                     "}";
//         */
        colorHandle= GLES20.glGetUniformLocation(mProgram,"vColor");
//
////        set color for drawing the triangle
        GLES20.glUniform4fv(colorHandle,1,color,0);
//
//        //draw triangle ,cout = 3
//        glDrawArrays(GL_TRIANGLES,0,vertexCount);
//
//        //disable vertex attr array
//        glDisableVertexAttribArray(positionHandle);
        // get handle to shape's transformation matrix
//        vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // Pass the projection and view transformation to the shader
//        GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0);

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionHandle);
    }


//    public void draw(float[] mvpMatrix) { // pass in the calculated transformation matrix
////    ...
//        GLES20.glUseProgram(mProgram);
//        // get handle to shape's transformation matrix
//        vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
//
//        // Pass the projection and view transformation to the shader
//        GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0);
//
//        // Draw the triangle
//        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
//
//        // Disable vertex array
//        GLES20.glDisableVertexAttribArray(positionHandle);
//    }
    public static int linkProgram(int vertexShaderId, int fragmentShaderId) {
        // 创建 OpenGL 程序 ID
        final int programObjectId = glCreateProgram();
        if (programObjectId == 0) {
            return 0;
        }
        // 链接上 顶点着色器
        GLES20.glAttachShader(programObjectId, vertexShaderId);
        // 链接上 片段着色器
        GLES20.glAttachShader(programObjectId, fragmentShaderId);
        // 链接着色器之后，链接 OpenGL 程序
        GLES20.glLinkProgram(programObjectId);
        final int[] linkStatus = new int[1];
        // 验证链接结果是否失败
        GLES20.glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] == 0) {
            // 失败则删除 OpenGL 程序
            GLES20.glDeleteProgram(programObjectId);
            return 0;
        }
        return programObjectId;
    }
}
