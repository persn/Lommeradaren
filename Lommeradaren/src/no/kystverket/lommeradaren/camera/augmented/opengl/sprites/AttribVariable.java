package no.kystverket.lommeradaren.camera.augmented.opengl.sprites;

public enum AttribVariable {
	A_POSITION(1, "a_Position"), A_TEX_COORDINATE(2, "a_TexCoordinate"), A_MVP_MATRIX_INDEX(
			3, "a_MVPMatrixIndex");

	private int mHandle;
	private String mName;

	private AttribVariable(int handle, String name) {
		mHandle = handle;
		mName = name;
	}

	public int getHandle() {
		return mHandle;
	}

	public String getName() {
		return mName;
	}
}