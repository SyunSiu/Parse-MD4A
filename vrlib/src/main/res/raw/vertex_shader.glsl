


uniform mat4 u_MVPMatrix;		// A constant representing the combined model/view/projection matrix.

// 传入的量
attribute vec4 a_Position;		// Per-vertex position information we will pass in.
attribute vec2 a_TexCoordinate; // Per-vertex texture coordinate information we will pass in.

// 传递给片元着色器的量
varying vec2 v_TexCoordinate;   // This will be passed into the fragment shader.



//uniform mat4 u_MVMatrix;		// A constant representing the combined model/view matrix.

//attribute vec3 a_Normal;		// Per-vertex normal information we will pass in.
//attribute vec4 a_Color;		// Per-vertex color information we will pass in.

//varying vec3 v_Position;		// This will be passed into the fragment shader.
//varying vec4 v_Color;			// This will be passed into the fragment shader.
//varying vec3 v_Normal;		// This will be passed into the fragment shader.



void main(){

	// 将顶点坐标变换至视图空间     Transform the vertex into eye space.
//	v_Position = vec3(u_MVMatrix * a_Position);
		
	// 将纹理坐标传递给片元着色器    Pass through the texture coordinate.
	v_TexCoordinate = a_TexCoordinate;

    // 传递颜色值给片元着色器      Pass through the color.
	//v_Color = a_Color;

	// 将法向量方向转换至视图空间    Transform the normal's orientation into eye space.
    //v_Normal = vec3(u_MVMatrix * vec4(a_Normal, 0.0));

    // gl_Position 是一个内建变量，用来存储最终的顶点坐标。
	// gl_Position is a special variable used to store the final position.
	// 通过矩阵运算将最终的点归一化至屏幕坐标系中。
	// Multiply the vertex by the matrix to get the final point in normalized screen coordinates.
	gl_Position = u_MVPMatrix * a_Position;
}