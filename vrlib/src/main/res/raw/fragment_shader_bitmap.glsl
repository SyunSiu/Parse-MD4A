precision mediump float;       	// Set the default precision to medium. We don't need as high of a
								// precision in the fragment shader.



uniform sampler2D u_Texture;

varying vec2 v_TexCoordinate;   // Interpolated texture coordinate per fragment.



//uniform vec3 u_LightPos;       	// The position of the light in eye space.



void main(){
    gl_FragColor =  texture2D(u_Texture, v_TexCoordinate) * vec4(0.0, 0.9, 1.0, 1.0f);
}                                                                     	

