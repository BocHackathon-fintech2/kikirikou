package io.kikirikou.apps.pipelines.managers.impl.filters;

import org.apache.tapestry5.ioc.services.TypeCoercer;

public class LteFilter extends AbstractCompareFilter {
		
	public LteFilter(TypeCoercer typeCoercer) {
		super(typeCoercer);
	}
	
	@Override
	public Boolean apply(Object s, Object o) {
		return compare(s, o) <= 0;
	}
}