package io.kikirikou.modules.boc.modules;


import io.kikirikou.modules.boc.managers.decl.BocManager;
import io.kikirikou.modules.boc.managers.decl.BocManagerDecorator;
import io.kikirikou.modules.boc.managers.impl.BocManagerImpl;
import io.kikirikou.modules.boc.other.BocSymbolConstants;
import io.kikirikou.modules.persistence.managers.decl.QueryFactoryProvider;
import io.kikirikou.modules.persistence.modules.PersistenceModule;
import org.apache.commons.lang3.StringUtils;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.Decorate;
import org.apache.tapestry5.ioc.annotations.ImportModule;
import org.apache.tapestry5.ioc.services.FactoryDefaults;
import org.apache.tapestry5.ioc.services.SymbolProvider;

@ImportModule(value = {PersistenceModule.class})
public class BocModule {

    public static void bind(ServiceBinder binder) {
        binder.bind(BocManager.class,BocManagerImpl.class);
    }


    @Contribute(SymbolProvider.class)
    @FactoryDefaults
    public static void contributeFactoryDefaults(MappedConfiguration<String, Object> configuration) {
        configuration.add(BocSymbolConstants.CLIENT_ID, StringUtils.EMPTY);
        configuration.add(BocSymbolConstants.CLIENT_SECRET,StringUtils.EMPTY);
        configuration.add(BocSymbolConstants.BASE_URL, "https://sandbox-apis.bankofcyprus.com/df-boc-org-sb/sb/psd2");
    }

    @Decorate(serviceInterface=BocManager.class)
    public static BocManager decorateBocManager(BocManager delegate,QueryFactoryProvider queryFactoryProvider) {
        return new BocManagerDecorator(delegate,queryFactoryProvider);
    }
/*
    @Contribute(StartupManager.class)
    public static void contributeStartupManager(OrderedConfiguration<Runnable> configuration, QueryFactoryProvider queryFactoryProvider) {
        Random random = new Random();
        TransactionType[] transactionType = TransactionType.values();
        JSONObject original = new JSONObject("{  \n" +
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
        configuration.add("test", new Runnable() {
            @Override
            public void run() {
                Stream.of("351012345671","351092345672","351012345673","351012345674").forEach(new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        queryFactoryProvider.build(new Function<CustomDSLContext, Void>() {
                            @Override
                            public Void apply(CustomDSLContext customDSLContext) {
                                IntStream.range(0,100).mapToObj(new IntFunction<JSONObject>() {
                                    @Override
                                    public JSONObject apply(int i) {
                                        LocalDate date = LocalDate.now().minusDays(0 + random.nextInt(30));
                                        BigDecimal randomAmount = BigDecimal.valueOf(1 + Math.random() * (1000 - 1)).setScale(2, RoundingMode.HALF_EVEN);
                                        JSONObject copy = original.copy();
                                        copy.getJSONObject("transactionAmount").put("amount", randomAmount);
                                        copy.put("transactionType", transactionType[0 + random.nextInt(transactionType.length)].name());
                                        copy.put("postingDate",date.toString());
                                        copy.put("valueDate",date.toString());
                                        return copy;
                                    }
                                }).forEach(new Consumer<JSONObject>() {
                                    @Override
                                    public void accept(JSONObject jsonObject) {
                                        customDSLContext.insertInto(TRANSACTIONS).set(TRANSACTIONS.ACCOUNT_ID,s).set(TRANSACTIONS.TRANSACTION,jsonObject).execute();
                                    }
                                });


                                return null;
                            }
                        });
                    }
                });

            }
        });
    }*/
}
