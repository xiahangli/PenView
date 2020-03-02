package com.example.penview;

import android.graphics.Point;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;

/**
 * @author Henry
 * @Date 2020-02-08  00:09
 * @Email 2427417167@qq.com
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {
    public volatile float mAngle;
    private final float[] viewMatrix = new float[16];
    // vPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] vPMatrix = new float[16];



    public float getAngle() {
        return mAngle;
    }

    public void setAngle(float angle) {
        mAngle = angle;
    }


    /**
     * The system calls this method once, when creating the GLSurfaceView. Use this method to perform actions
     * that need to happen only once, such as setting OpenGL environment parameters or initializing OpenGL graphic objects.
     * @param gl
     * @param config
     */
    private Triangle mTriagle;
    private Point mPoint;
    Square mSquare;
    private final float[] projectionMatrix = new float[16];
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //  // Set the background frame color
        glClearColor(1f, 0, 0, 1f);
        //initialize triangle
        mTriagle = new Triangle();
//        mSquare = new Square();
//        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        // 在 onSurfaceCreated 里面初始化，否则会报线程错误
//        mPoint = new Point(mContext);
        // 绑定相应的顶点数据
//        mPoint.bindData();
        // 在 onSurfaceCreated 里面初始化，否则会报线程错误
//        mPoint = new Point(mContext);
//        // 绑定相应的顶点数据
//        mPoint.bindData();
    }

    /**
     * The system calls this method when the GLSurfaceView geometry changes,
     * including changes in size of the GLSurfaceView or orientation of the device screen
     * For example, the system calls this method when the device changes from portrait to landscape orientation
     * Use this method to respond to changes in the GLSurfaceView container.
     * @param gl
     * @param width
     * @param height
     */
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //  // 确定视口大小
        gl.glViewport(0, 0, width, height);
        /**
         * Create a projection matrix using the geometry of the device screen
         * in order to recalculate object coordinates so they are drawn with correct proportions
         */
        // make adjustments for screen ratio
        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
//        Matrix.frustumM(projectionMatrix,0,-ratio,ratio,-1,1,3,7);

//        gl.glMatrixMode(GL10.GL_PROJECTION);        // set matrix to projection mode
//        gl.glLoadIdentity();//reset matrix to its' default state
//        gl.glFrustumf(-ratio,ratio,-1,1,3,7);//apply the projection matrix
    }

    /**
     * The system calls this method on each redraw of the GLSurfaceView.
     * Use this method as the primary execution point for drawing (and re-drawing) graphic objects.
     * @param gl
     */
    @Override
    public void onDrawFrame(GL10 gl) {
        // Redraw background color
        // 清屏
        //GL_COLOR_BUFFER_BIT
        glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // enable face culling feature
//        gl.glEnable(GL10.GL_CULL_FACE);
// specify which faces to not draw
//        gl.glCullFace(GL10.GL_BACK);

//        float[] scratch = new float[16];
        // Set GL_MODELVIEW transformation mode
//        gl.glMatrixMode(GL10.GL_MODELVIEW);
//        gl.glLoadIdentity();

        //你的视角
//        GLU.gluLookAt(gl, 0, 0, -5, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
//        Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0);
//        mTriangle.draw(scratch);
        // Set the camera position (View matrix)
//        Matrix.setLookAtM(viewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Set the camera position (View matrix)
//        Matrix.setLookAtM(viewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
//
//        // Calculate the projection and view transformation
//        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0);


        // Draw shape

        float[] scratch = new float[16];

        // Create a rotation for the triangle
        // long time = SystemClock.uptimeMillis() % 4000L;
        // float angle = 0.090f * ((int) time);
        Matrix.setRotateM(rotationMatrix, 0, mAngle, 0, 0, -1.0f);

        // Combine the rotation matrix with the projection and camera view
        // Note that the vPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0);

        // Draw triangle
        mTriagle.draw(scratch);
    }
    private float[] rotationMatrix = new float[16];

    /**
     * projection matrix
     * camera view matrix
     */

    /**
     * 编译着色器
     * utility for compiling vertexShaderCode &
     * @param type
     * @param shaderCode
     * @return
     */
    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        //根据type穿件对顶的shapder脚本解析工具
        int shaderObjectId = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        //解析脚本
        GLES20.glShaderSource(shaderObjectId, shaderCode);

        // 以下为验证编译结果是否失败
        final int[] compileStatsu = new int[1];
        //glGetShaderiv函数比较通用，在着色器阶段和 OpenGL 程序阶段都会通过它来验证结果
//        GLES20.glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatsu, 0);
//        if ((compileStatsu[0] == 0)) {
//            // 失败则删除
//            GLES20.glDeleteShader(shaderObjectId);
//            return 0;
//        }
        //编译片段着色器
        GLES20.glCompileShader(shaderObjectId);

        return shaderObjectId;
    }
//    // 编译顶点着色器
//    public static int compileVertexShader(String shaderCode) {
//        return compileShader(GL_VERTEX_SHADER, shaderCode);
//    }
}
