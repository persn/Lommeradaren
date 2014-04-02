package no.kystverket.lommeradaren.camera.augmented.opengl.texture;

import no.kystverket.lommeradaren.camera.augmented.opengl.text.AttribVariable;
import no.kystverket.lommeradaren.camera.augmented.opengl.text.Program;

public class MarkerProgram extends Program{

	private static final AttribVariable[] programVariables = {
		AttribVariable.A_Position, AttribVariable.A_TexCoordinate,
		AttribVariable.A_MVPMatrixIndex };
	
	 private final String vertexShaderCode =
	            "uniform mat4 uMVPMatrix;" +
	            "attribute vec4 vPosition;" +
	            "void main() {" +
	            "  gl_Position = uMVPMatrix * vPosition;" +
	            "}";
	 
	 private final String fragmentShaderCode =
	            "precision mediump float;" +
	            "uniform vec4 vColor;" +
	            "void main() {" +
	            "  gl_FragColor = vColor;" +
	            "}";
	 
	 @Override
		public void init() {
			super.init(vertexShaderCode, fragmentShaderCode, programVariables);
		}
	
}
