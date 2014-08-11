package edu.bc.kimahc.draw;

import java.nio.FloatBuffer;

import android.opengl.GLES20;

public class FFTStrat extends DrawStrat{

	private float[] abuf;
	public FFTStrat(FloatBuffer vertexBuffer, int bufLen) {
		super(vertexBuffer, bufLen);
		abuf = new float[audioBufferLength*2];
	}

	public int setVertexCount(int drawBufferLength) {
		return drawBufferLength;
	}
	
	public float setLineWidth(int horizontalZoom) {
		return 120f/(horizontalZoom+20f)+2;
	}


	public FloatBuffer setDrawBuffers(float[] buf, int drawBufferLength, int horizontalZoom,
			int verticalZoom, int tab) {
		//System.out.println("abuf length = " + abuf.length);
		//System.out.println("buf length = " + buf.length);
		if(buf.length == audioBufferLength/2){
			for(int i = 0; i < drawBufferLength/2; i=i+1){
				abuf[4*i] = (float)(i * 4f)/ (float)drawBufferLength - 1f;		//bottom x
				abuf[4*i+1] = -1f;												//bottom y
				abuf[4*i+2] = (float)(i * 4f)/ (float)drawBufferLength - 1f;	//upper x
				abuf[4*i+3] = buf[i]/(verticalZoom+1)-1f;						//upper y
				//System.out.println(fft.getBand(i/2));
			}
	
			synchronized(vertexBuffer){
				vertexBuffer.clear();
		        vertexBuffer.put(abuf).flip();
		        vertexBuffer.position(0);
			}
		}
		return vertexBuffer;
	}

	public void draw(int vertexCount) {
		GLES20.glDrawArrays(GLES20.GL_LINES, 0, vertexCount);
	}
}
