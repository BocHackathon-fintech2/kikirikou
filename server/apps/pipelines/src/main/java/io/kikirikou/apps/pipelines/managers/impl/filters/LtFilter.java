package io.kikirikou.apps.pipelines.managers.impl.filters;

import org.apache.tapestry5.ioc.services.TypeCoercer;

public class LtFilter extends AbstractCompareFilter {
		
	public LtFilter(TypeCoercer typeCoercer) {
		super(typeCoercer);
	}
	
	@Override
	public Boolean apply(Object s, Object o) {
		return compare(s, o) < 0;
	}
}