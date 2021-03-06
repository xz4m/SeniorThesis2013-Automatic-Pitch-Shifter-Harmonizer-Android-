package edu.bc.kimahc.draw;

import java.nio.FloatBuffer;

import Music.Tones;
import android.opengl.GLES20;
import android.os.Bundle;

public class PitchStrat extends DrawStrat{

	private float[] abuf, pitchData;
	private float pitch = 0;
	private float white[] = { 1f, 1f, 1f, 1.0f };
	public PitchStrat(FloatBuffer vertexBuffer, int bufLen) {
		super(vertexBuffer, bufLen);
		abuf = new float[audioBufferLength];
	}

	public int setVertexCount(int drawBufferLength) {
		return 2+88*2;
	}

	public float setLineWidth(int horizontalZoom) {
		return 5;
	}
	
	public FloatBuffer setDrawBuffers(Bundle bundle, int drawBufferLength,
			int horizontalZoom, int verticalZoom, int tab) {	
		pitch = bundle.getFloat("freq");
		pitch = Tones.getTone(pitch);
		pitchData = bundle.getFloatArray("processed");
		if(pitchData == null){
			System.out.println("null bitch");
		}else
			System.out.println("pitchData");
			
			if(pitchData.length == 1){
				for(int i = 0; i < 2; i=i+1){ //1034
					abuf[i*2] = i-0.5f;		//x coord
					//abuf[i*2+1] = (buf[0] + 0.5f)/6f -1f;//y
					abuf[i*2+1] = (pitchData[0]-57)*(4f/(verticalZoom+1));//y
				}	
				//System.out.println("tone " + buf[0] + " abuf val = " + abuf[1]);
				for(int j = 0; j < 109-21; j++){
					abuf[4+j*4] = -1f;
					abuf[4+j*4+1] = (j+21-57f)*(4f/(verticalZoom+1));
					abuf[4+j*4+2] = 1f;
					abuf[4+j*4+3] = (j+21-57f)*(4f/(verticalZoom+1));
				}
				synchronized(vertexBuffer){
					vertexBuffer.clear();
			        vertexBuffer.put(abuf).flip();
			        vertexBuffer.position(0);
				}
			}
			return vertexBuffer;
	}
	
	public void draw(int vertexCount, int mColorHandle){
		GLES20.glDrawArrays(GLES20.GL_LINES, 0, 2);
		GLES20.glLineWidth(1f);
		GLES20.glUniform4fv(mColorHandle, 1, white, 0);
		GLES20.glDrawArrays(GLES20.GL_LINES, 2, vertexCount-2);
	}
	
    public float toneToPitch(float tone){ //returns pitch number from (-0.5 11.5] (0 is C, 11 is B)
    	float fractionalPart = tone-((long)tone); //isolates fraction
    	float integralPart = tone - fractionalPart; //isolates whole number
    	float pitch = integralPart % 12 + fractionalPart;
    	if (pitch > 11.5)
    		return pitch-12;
    	return pitch;
    }
}
