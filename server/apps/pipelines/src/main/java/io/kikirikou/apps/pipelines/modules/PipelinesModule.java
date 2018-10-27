package io.kikirikou.apps.pipelines.modules;

import io.kikirikou.apps.pipelines.managers.decl.FilterManager;
import io.kikirikou.apps.pipelines.managers.decl.PipelineExecutor;
import io.kikirikou.apps.pipelines.managers.decl.PipelineFactory;
import io.kikirikou.apps.pipelines.managers.impl.FilterManagerImpl;
import io.kikirikou.apps.pipelines.managers.impl.PipelineExecutorImpl;
import io.kikirikou.apps.pipelines.managers.impl.PipelineFactoryImpl;
import io.kikirikou.apps.pipelines.managers.impl.filters.*;
import io.kikirikou.apps.pipelines.managers.impl.pipelinemodules.*;
import io.kikirikou.apps.pipelines.other.FilterProcessor;
import io.kikirikou.apps.pipelines.other.PipelineProcessor;
import io.kikirikou.modules.boc.managers.decl.BocManager;
import io.kikirikou.modules.bootstrap.other.constants.BootstrapConstants;
import io.kikirikou.modules.common.managers.decl.StartupManager;
import io.kikirkou.modules.resteasy.managers.decl.RestEasyResourceLocator;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.services.FactoryDefaults;
import org.apache.tapestry5.ioc.services.SymbolProvider;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;

import java.time.LocalDate;
import java.util.function.Consumer;

public class PipelinesModule {
    public static void bind(ServiceBinder binder) {
        binder.bind(PipelineExecutor.class, PipelineExecutorImpl.class);
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
        configuration.addInstance(">", GtFilter.class);
        configuration.addInstance(">=", GteFilter.class);
        configuration.addInstance("<", LtFilter.class);
        configuration.addInstance("<=", LteFilter.class);
        configuration.addInstance("=", EqualsFilter.class);
        configuration.addInstance("!=", NotEqualsFilter.class);
    }

    @Contribute(RestEasyResourceLocator.class)
    public static void contributeRestEasyResourceLocator(@Symbol(BootstrapConstants.APP_PACKAGE) String applicationPackage,
                                                         OrderedConfiguration<Object> configuration) {
        configuration.add("application", applicationPackage + ".rest");
    }

    @Contribute(StartupManager.class)
    public static void contributeStartupManager(OrderedConfiguration<Runnable> configuration, BocManager bocManager) {
        configuration.add("boc", () -> bocManager.getStatement("351012345671",
                "AAIkNDU1MDk0NDUtNjdlOS00NzZiLWI5NDYtZjY2N2M2ZDQ2NWE5eeXilzC_EsAyj5jsJSPksSc8FH9zFEt6o6TyM38NqEZpGM-K1WBlk5wwHuvHPVkWa1wYMDL5p9uPKFfSx3GRe1NOKd5p8QWmJo4Tx64Af5Cc8UPMZnyWpVITpAUjvqi6XtwqXHpYHrPFLyVUr3ks-w",
                "Subid000001-1540656099736",
                LocalDate.now().minusYears(2),
                LocalDate.now()).ifPresent(new Consumer<JSONArray>() {
            @Override
            public void accept(JSONArray jsonObject) {
                System.out.println("Response is:" + jsonObject);
            }
        }));
    }
}
