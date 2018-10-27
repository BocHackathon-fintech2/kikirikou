package io.kikirikou.apps.pipelines.managers.impl.filters;

import java.math.BigDecimal;

import org.apache.tapestry5.ioc.services.TypeCoercer;

import io.kikirikou.apps.pipelines.other.FilterProcessor;

public abstract class AbstractCompareFilter implements FilterProcessor {
	
	private final TypeCoercer typeCoercer;
	
	public AbstractCompareFilter(TypeCoercer typeCoercer) {
		this.typeCoercer = typeCoercer;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected int compare(Object s, Object o) {
		if (o instanceof Number) {
			BigDecimal a = typeCoercer.coerce(s, BigDecimal.class);
			BigDecimal b = typeCoercer.coerce(o, BigDecimal.class);
			
			return b.compareTo(a);
		} else if (o instanceof Comparable) {
			Object k = typeCoercer.coerce(s, o.getClass());

			return ((Comparable)o).compareTo(k);
		} else {
			throw new RuntimeException("Not comparable");
		}
	}
}