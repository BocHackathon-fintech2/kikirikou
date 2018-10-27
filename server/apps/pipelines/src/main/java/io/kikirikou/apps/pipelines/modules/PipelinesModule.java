package io.kikirikou.apps.pipelines.modules;

import io.kikirikou.apps.pipelines.enums.Aggregator;
import io.kikirikou.apps.pipelines.managers.decl.FilterManager;
import io.kikirikou.apps.pipelines.managers.decl.PipelineFactory;
import io.kikirikou.apps.pipelines.managers.impl.FilterManagerImpl;
import io.kikirikou.apps.pipelines.managers.impl.PipelineFactoryImpl;
import io.kikirikou.apps.pipelines.managers.impl.pipelinemodules.*;
import io.kikirikou.apps.pipelines.other.FilterProcessor;
import io.kikirikou.apps.pipelines.other.PipelineProcessor;
import io.kikirikou.modules.common.managers.decl.StartupManager;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.Local;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.services.FactoryDefaults;
import org.apache.tapestry5.ioc.services.SymbolProvider;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class PipelinesModule {
    public static void bind(ServiceBinder binder) {
        binder.bind(PipelineFactory.class, PipelineFactoryImpl.class);
        binder.bind(FilterManager.class, FilterManagerImpl.class);
    }

    @Contribute(SymbolProvider.class)
    @FactoryDefaults
    public static void contributeFactoryDefaults(MappedConfiguration<String, Object> configuration) {

    }

    @Contribute(PipelineFactory.class)
    public static void contributePipelineFactory(MappedConfiguration<String, PipelineProcessor> configuration) {
        configuration.addInstance("aggregate", Aggregate.class);
        configuration.addInstance("filter", Filter.class);
        configuration.addInstance("statement", Statement.class);
        configuration.addInstance("console", Console.class);
        configuration.addInstance("stringtable", StringTable.class);
    }

    @Contribute(FilterManager.class)
    public static void contributeFilterManager(MappedConfiguration<String, FilterProcessor> configuration) {
        configuration.add("gt", new FilterProcessor() {
            @Override
            public Boolean apply(String s, Object o) {
                BigDecimal bigDecimal = new BigDecimal(s);
                return ((BigDecimal)o).compareTo(bigDecimal) > 0;
            }
        });
    }

    @Contribute(StartupManager.class)
    public static void contributeStartupManager(OrderedConfiguration<Runnable> configuration,
                                                @Local PipelineFactory factory) {
        List<JSONObject> data = CollectionFactory.newList(
                new JSONObject("id", "a", "value1", 1, "value2", 4),
                new JSONObject("id", "b", "value1", 2.0, "value2", 5),
                new JSONObject("id", "c", "value1", new BigDecimal(4.6).setScale(2, RoundingMode.HALF_EVEN), "value2", 6),
                new JSONObject("id", "c", "value1", 4, "value2", 7)
        );

        List<JSONObject> config = CollectionFactory.newList(
                new JSONObject("type","console"),
                new JSONObject("type","statement","config",new JSONObject("from", LocalDate.now().toString(),"to",LocalDate.now().minusDays(7).toString(),"token","blah")),
                new JSONObject("type","console"),
                new JSONObject("type","aggregate","config",new JSONObject("columns",new JSONArray((Object)"value1","value2"),"groupBy","id","execute", Aggregator.SUM.name())),
                new JSONObject("type","stringtable","config",new JSONObject("columns",new JSONArray("key","value"))));
        /*new JSONObject("type","filter","config",new JSONObject("value","gt:30")));*/
                //new JSONObject("type","aggregate","config",new JSONObject("columns",new JSONArray((Object)"value"),"execute", Aggregator.SUM.name())));

        configuration.add("test", new Runnable() {
            @Override
            public void run() {
                System.out.println("Result is:" + factory.build(data.stream(),config).collect(Collectors.toList()));
            }
        },"after:*");
    }
}
