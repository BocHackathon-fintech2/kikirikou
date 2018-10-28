package io.kikirikou.modules.boc.managers.impl;

import io.kikirikou.modules.boc.enums.TransactionType;
import io.kikirikou.modules.boc.managers.decl.BocManager;
import io.kikirikou.modules.persistence.managers.decl.QueryFactoryProvider;
import io.kikirikou.modules.persistence.other.CustomDSLContext;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.json.JSONObject;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

import static io.kikirikou.modules.persistence.jooq.Tables.TRANSACTIONS;

public class BocManagerDecorator implements BocManager {
	private static final JSONObject TRANSFER_TEMPLATE = new JSONObject("{  \n" +
			"   \"id\":\"12345\",\n" +
			"   \"dcInd\":\"CY123\",\n" +
			"   \"transactionAmount\":{  \n" +
			"      \"amount\":1000,\n" +
			"      \"currency\":\"EUR\"\n" +
			"   },\n" +
			"   \"description\":\"NEFT TRANSACTION\",\n" +
			"   \"postingDate\":\"20/11/2017\",\n" +
			"   \"valueDate\":\"20/12/2017\",\n" +
			"   \"transactionType\":\"NEFT\",\n" +
			"   \"merchant\":{  \n" +
			"      \"name\":\"DEMETRIS KOSTA\",\n" +
			"      \"address\":{  \n" +
			"         \"line1\":\"A-123\",\n" +
			"         \"line2\":\"APARTMENT\",\n" +
			"         \"line3\":\"STREET\",\n" +
			"         \"line4\":\"AREA\",\n" +
			"         \"city\":\"CITY\",\n" +
			"         \"postalcode\":\"CY-01\",\n" +
			"         \"state\":\"STATE\",\n" +
			"         \"country\":\"CYPRUS\"\n" +
			"      }\n" +
			"   },\n" +
			"   \"terminalId\":\"12345\"\n" +
			"}");
    private final QueryFactoryProvider queryFactoryProvider;

    public 	BocManagerDecorator(BocManager delegate,QueryFactoryProvider queryFactoryProvider) {
        this.queryFactoryProvider = queryFactoryProvider;
    }

    @Override
    public Optional<Collection<JSONObject>> getStatement(String accountId, String token, String subscriptionId, LocalDate from, LocalDate to) {
        return Optional.of(queryFactoryProvider.
                build((Function<CustomDSLContext, Collection<JSONObject>>) customDSLContext -> customDSLContext.select(TRANSACTIONS.TRANSACTION).
                from(TRANSACTIONS).
                where(TRANSACTIONS.ACCOUNT_ID.eq(accountId)).
                fetch(TRANSACTIONS.TRANSACTION)));
    }

	@Override
	public Optional<Collection<JSONObject>> createTransfer(String fromAccountId, String toAccountId,
			TransactionType transactionType, BigDecimal amount) {
		LocalDate date = LocalDate.now();
		JSONObject fromTransaction = TRANSFER_TEMPLATE.copy();
		fromTransaction.getJSONObject("transactionAmount").put("amount", amount);
		fromTransaction.put("transactionType", transactionType.name());
		fromTransaction.put("postingDate",date.toString());
		fromTransaction.put("valueDate",date.toString());
		
		JSONObject toTransaction = fromTransaction.copy();
		
		return Optional.of(queryFactoryProvider.build((Function<CustomDSLContext, Collection<JSONObject>>) customDSLContext -> {
			customDSLContext.insertInto(TRANSACTIONS).set(TRANSACTIONS.ACCOUNT_ID,fromAccountId).set(TRANSACTIONS.TRANSACTION,fromTransaction).execute();
			customDSLContext.insertInto(TRANSACTIONS).set(TRANSACTIONS.ACCOUNT_ID,toAccountId).set(TRANSACTIONS.TRANSACTION,toTransaction).execute();

			return CollectionFactory.newList(fromTransaction, toTransaction);
		}));
	}
}
