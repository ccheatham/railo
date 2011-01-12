package railo.runtime.img.filter;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.Kernel;

import railo.commons.color.ColorCaster;
import railo.runtime.engine.ThreadLocalPageContext;
import railo.runtime.exp.ExpressionException;
import railo.runtime.exp.FunctionException;
import railo.runtime.exp.PageException;
import railo.runtime.img.Image;
import railo.runtime.img.math.Function2D;
import railo.runtime.op.Caster;
import railo.runtime.type.Struct;
import railo.runtime.type.util.Type;

public class ImageFilterUtil {

	public static float toFloatValue(Object value, String argName) throws FunctionException {
		float res = Caster.toFloatValue(value,Float.NaN);
		if(Float.isNaN(res)) {
			throw new FunctionException(ThreadLocalPageContext.get(), "ImageFilter", 3, "parameters", msg(value,"float",argName));
		}
		return res;
	}

	public static int toIntValue(Object value, String argName) throws FunctionException {
		int res = Caster.toIntValue(value,Integer.MIN_VALUE);
		if(Integer.MIN_VALUE==res) {
			throw new FunctionException(ThreadLocalPageContext.get(), "ImageFilter", 3, "parameters", msg(value,"int",argName));
		}
		return res;
	}
	public static boolean toBooleanValue(Object value, String argName) throws FunctionException {
		Boolean res = Caster.toBoolean(value,null);
		if(res==null) {
			throw new FunctionException(ThreadLocalPageContext.get(), "ImageFilter", 3, "parameters", msg(value,"boolean",argName));
		}
		return res;
	}
	public static String toString(Object value, String argName) throws FunctionException {
		String res = Caster.toString(value,null);
		if(res==null) {
			throw new FunctionException(ThreadLocalPageContext.get(), "ImageFilter", 3, "parameters", msg(value,"String",argName));
		}
		return res;
	}
	


	public static BufferedImage toBufferedImage(Object o, String argName) throws PageException {
		return Image.toImage(o).getBufferedImage();
	}

	public static Colormap toColormap(Object value, String argName) throws FunctionException {
		if(value instanceof Colormap)
			return (Colormap) value;
		throw new FunctionException(ThreadLocalPageContext.get(), "ImageFilter", 3, "parameters", msg(value,"Colormap",argName)+" use function ImageColorMap to create a colormap");
	}
	
	public static Kernel toKernel(Object value, String argName) throws FunctionException {
		throw new FunctionException(ThreadLocalPageContext.get(), "ImageFilter", 3, "parameters", "type Kernel not supported yet!");
	}
	
	////

	public static Color toColor(Object value, String argName) throws PageException {
		if(value instanceof Color)
			return (Color) value;
		return ColorCaster.toColor(Caster.toString(value));
		
	}

	public static int[] toDimensions(Object value, String argName) throws FunctionException {
		throw new FunctionException(ThreadLocalPageContext.get(), "ImageFilter", 3, "parameters", "type int[] not supported yet!");
	}

	public static LightFilter.Material toLightFilter$Material(Object value, String argName) throws PageException {
		if(value instanceof LightFilter.Material)
			return (LightFilter.Material) value;
		
		Struct sct = Caster.toStruct(value,null);
		if(sct==null)
			throw new FunctionException(ThreadLocalPageContext.get(), "ImageFilter", 3, "parameters", msg(value,"Struct",argName));
		
		
		LightFilter.Material mat=new LightFilter.Material();
		Object o = sct.get("DiffuseColor",null);
		if(o==null)
			throw new FunctionException(ThreadLocalPageContext.get(), "ImageFilter", 3, "parameters", "missing key [DiffuseColor] in struct of parameter ["+argName+"]");
		mat.setDiffuseColor(ColorCaster.toColor(Caster.toString(o)).getRGB());
		o = sct.get("Opacity",null);
		if(o==null)
			throw new FunctionException(ThreadLocalPageContext.get(), "ImageFilter", 3, "parameters", "missing key [Opacity] in struct of parameter ["+argName+"]");
		mat.setOpacity((Caster.toFloatValue(o)));
		
		return mat;
	}

	public static Function2D toFunction2D(Object value, String argName) throws FunctionException {
		throw new FunctionException(ThreadLocalPageContext.get(), "ImageFilter", 3, "parameters", "type Function2D not supported yet!");
	}

	public static Point2D toPoint2D(Object value, String argName) throws FunctionException {
		throw new FunctionException(ThreadLocalPageContext.get(), "ImageFilter", 3, "parameters", "type AffineTransform not supported yet!");
	}

	public static AffineTransform toAffineTransform(Object value, String argName) throws FunctionException {
		throw new FunctionException(ThreadLocalPageContext.get(), "ImageFilter", 3, "parameters", "type BufferedImage not supported yet!");
	}

	public static Composite toComposite(Object value, String argName) throws FunctionException {
		throw new FunctionException(ThreadLocalPageContext.get(), "ImageFilter", 3, "parameters", "type Composite not supported yet!");
	}

	public static CurvesFilter.Curve[] toACurvesFilter$Curve(Object value, String argName) throws FunctionException {
		throw new FunctionException(ThreadLocalPageContext.get(), "ImageFilter", 3, "parameters", "type CurvesFilter.Curve[] not supported yet!");
	}

	public static CurvesFilter.Curve toCurvesFilter$Curve(Object value, String argName) throws FunctionException {
		throw new FunctionException(ThreadLocalPageContext.get(), "ImageFilter", 3, "parameters", "type CurvesFilter.Curve not supported yet!");
	}

	public static int[] toAInt(Object value, String argName) throws FunctionException {
		throw new FunctionException(ThreadLocalPageContext.get(), "ImageFilter", 3, "parameters", "type int[] not supported yet!");
	}

	public static float[] toAFloat(Object value, String argName) throws FunctionException {
		throw new FunctionException(ThreadLocalPageContext.get(), "ImageFilter", 3, "parameters", "type float[] not supported yet!");
	}

	public static int[][] toAAInt(Object value, String argName) throws FunctionException {
		throw new FunctionException(ThreadLocalPageContext.get(), "ImageFilter", 3, "parameters", "type int[][] not supported yet!");
	}

	public static WarpGrid toWarpGrid(Object value, String argName) throws FunctionException {
		throw new FunctionException(ThreadLocalPageContext.get(), "ImageFilter", 3, "parameters", "type WarpGrid not supported yet!");
	}

	public static FieldWarpFilter.Line[] toAFieldWarpFilter$Line(Object o, String string) throws FunctionException {
		throw new FunctionException(ThreadLocalPageContext.get(), "ImageFilter", 3, "parameters", "type WarpGrid not supported yet!");
	}
	
	
	
	

	private static String msg(Object value, String type, String argName) {
		return "Can't cast argument ["+argName+"] from type ["+Type.getName(value)+"] to a value of type ["+type+"]";
	}

	public static Font toFont(Object o, String string) {
		// TODO Auto-generated method stub
		return null;
	}

	public static Point toPoint(Object o, String string) {
		// TODO Auto-generated method stub
		return null;
	}





	private static float range(float value, int from, int to) throws ExpressionException {
		if(value>=from && value<=to)
			return value;
		throw new ExpressionException("["+Caster.toString(value)+"] is out of range, value must be between ["+Caster.toString(from)+"] and ["+Caster.toString(to)+"]");
	}


}