package io.kikirikou.apps.pipelines.managers.impl.filters;

import org.apache.tapestry5.ioc.services.TypeCoercer;

public class EqualsFilter extends AbstractCompareFilter {
	
	
	public EqualsFilter(TypeCoercer typeCoercer) {
		super(typeCoercer);
	}
	
	@Override
	public Boolean apply(Object s, Object o) {
		return this.compare(s, o) == 0;
	}
}