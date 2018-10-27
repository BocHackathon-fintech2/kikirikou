package io.kikirikou.apps.pipelines.managers.impl.filters;

import org.apache.tapestry5.ioc.services.TypeCoercer;

public class NotEqualsFilter extends EqualsFilter {
	
	public NotEqualsFilter(TypeCoercer typeCoercer) {
		super(typeCoercer);
	}
	
	@Override
	public Boolean apply(Object s, Object o) {
		return !super.apply(s, o);
	}
}