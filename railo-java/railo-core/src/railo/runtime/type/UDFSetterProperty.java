 package railo.runtime.type;

import railo.commons.lang.CFTypes;
import railo.commons.lang.StringUtil;
import railo.runtime.ComponentImpl;
import railo.runtime.PageContext;
import railo.runtime.component.Property;
import railo.runtime.exp.ExpressionException;
import railo.runtime.exp.PageException;
import railo.runtime.op.Caster;
import railo.runtime.op.Decision;
import railo.runtime.orm.hibernate.HBMCreator;
import railo.runtime.type.Collection.Key;
import railo.runtime.type.util.CollectionUtil;
import railo.runtime.type.util.KeyConstants;

public final class UDFSetterProperty extends UDFGSProperty {

	private static final Collection.Key VALIDATE_PARAMS = KeyImpl.intern("validateParams");
	private final Property prop;
	private final Key propName;
	private String validate;
	private Struct validateParams;

	public UDFSetterProperty(ComponentImpl component,Property prop) throws PageException {
		super(component,"set"+StringUtil.ucFirst(prop.getName()),new FunctionArgument[]{
			new FunctionArgumentImpl(
					KeyImpl.init(prop.getName()),
					prop.getType(),
					CFTypes.toShortStrict(prop.getType(),CFTypes.TYPE_UNKNOW),
					true)
		},CFTypes.TYPE_ANY,"wddx");
		
		
		this.prop=prop; 
		this.propName=KeyImpl.getInstance(prop.getName());
		
		this.validate=Caster.toString(prop.getDynamicAttributes().get(KeyConstants._validate,null),null);
		if(!StringUtil.isEmpty(validate,true)) {
			validate=validate.trim().toLowerCase();
			Object o = prop.getDynamicAttributes().get(VALIDATE_PARAMS,null);
			if(o!=null){
				if(Decision.isStruct(o))validateParams=Caster.toStruct(o);
				else {
					String str=Caster.toString(o);
					if(!StringUtil.isEmpty(str,true)) {
						validateParams=HBMCreator.convertToSimpleMap(str);
						if(validateParams==null)
							throw new ExpressionException("cannot parse string ["+str+"] as struct");
					}
				}
			}
		}
	} 

	/**
	 * @see railo.runtime.type.UDF#duplicate()
	 */
	public UDF duplicate(ComponentImpl c) {
		try {
			return new UDFSetterProperty(c,prop);
		} catch (PageException e) {
			return null;
		}
	}

	

	public UDF duplicate() {
		return duplicate(component);
	}
	/**
	 * @see railo.runtime.type.UDF#call(railo.runtime.PageContext, java.lang.Object[], boolean)
	 */
	public Object call(PageContext pageContext, Object[] args,boolean doIncludePath) throws PageException {
		if(args.length<1)
			throw new ExpressionException("The parameter "+prop.getName()+" to function "+getFunctionName()+" is required but was not passed in.");
		validate(validate,validateParams,args[0]);
		component.getComponentScope().set(propName, cast(this.arguments[0],args[0],1));
		return component;
	}

	/**
	 * @see railo.runtime.type.UDF#callWithNamedValues(railo.runtime.PageContext, railo.runtime.type.Struct, boolean)
	 */
	public Object callWithNamedValues(PageContext pageContext, Struct values,boolean doIncludePath) throws PageException {
		UDFImpl.argumentCollection(values,getFunctionArguments());
		Object value = values.get(propName,null);
		
		if(value==null){
			Key[] keys = CollectionUtil.keys(values);
			if(keys.length==1) {
				value=values.get(keys[0]);
			}
			else throw new ExpressionException("The parameter "+prop.getName()+" to function "+getFunctionName()+" is required but was not passed in.");
		}
		component.getComponentScope().set(propName, cast(arguments[0],value,1));
		return component;
	}

	/**
	 * @see railo.runtime.type.UDF#getDefaultValue(railo.runtime.PageContext, int)
	 */
	public Object getDefaultValue(PageContext pc, int index) throws PageException {
		return prop.getDefault();
	}

	/**
	 * @see railo.runtime.type.UDF#getReturnTypeAsString()
	 */
	public String getReturnTypeAsString() {
		return "any";
	}

	/**
	 * @see railo.runtime.type.UDF#implementation(railo.runtime.PageContext)
	 */
	public Object implementation(PageContext pageContext) throws Throwable {
		return null;
	}

}
