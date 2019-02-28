package com.sunflower.framework.json;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MyBeanSerializerModifier extends BeanSerializerModifier {

	private JsonSerializer<Object> nullArrayJsonSerializer = new MyNullArrayJsonSerializer();

	private JsonSerializer<Object> nullStringJsonSerializer = new MyNullStringJsonSerializer();

	private JsonSerializer<Object> nullIntegerJsonSerializer = new MyNullIntegerJsonSerializer();

	public MyBeanSerializerModifier() {
	}

	@Override
	public List<BeanPropertyWriter> changeProperties(SerializationConfig config,
			BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
		Iterator var4 = beanProperties.iterator();

		while (var4.hasNext()) {
			BeanPropertyWriter writer = (BeanPropertyWriter) var4.next();
			if (this.isArrayType(writer)) {
				writer.assignNullSerializer(this.defaultNullArrayJsonSerializer());
			}

			if (this.isStringType(writer)) {
				writer.assignNullSerializer(this.defaultNullStringJsonSerializer());
			}

			if (this.isIntegerType(writer)) {
				writer.assignNullSerializer(this.defaultNullIntegerJsonSerializer());
			}
		}

		return beanProperties;
	}

	protected boolean isArrayType(BeanPropertyWriter writer) {
		JavaType type = writer.getType();
		return type.hasRawClass(List.class) || type.hasRawClass(Set.class);
	}

	protected boolean isStringType(BeanPropertyWriter writer) {
		JavaType type = writer.getType();
		return type.hasRawClass(String.class);
	}

	protected boolean isIntegerType(BeanPropertyWriter writer) {
		JavaType type = writer.getType();
		return type.hasRawClass(Integer.class);
	}

	protected JsonSerializer<Object> defaultNullArrayJsonSerializer() {
		return this.nullArrayJsonSerializer;
	}

	protected JsonSerializer<Object> defaultNullStringJsonSerializer() {
		return this.nullStringJsonSerializer;
	}

	protected JsonSerializer<Object> defaultNullIntegerJsonSerializer() {
		return this.nullIntegerJsonSerializer;
	}

}
