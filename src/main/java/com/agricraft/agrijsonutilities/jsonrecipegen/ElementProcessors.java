package com.agricraft.agrijsonutilities.jsonrecipegen;

import com.agricraft.agrijsonutilities.jsonrecipegen.processors.*;
import com.google.common.collect.Maps;

import java.util.Map;

public class ElementProcessors {
    private static final Map<String, IJsonElementProcessor> processors = Maps.newHashMap();

    public static void register(IJsonElementProcessor processor) {
        processors.put(processor.type(), processor);
    }

    public static IJsonElementProcessor getProcessor(String type) {
        return processors.get(type);
    }

    static {
        register(new ProcessorCopy());
        register(new ProcessorGrowthStatFactor());
        register(new ProcessorGrowthTicks());
        register(new ProcessorLoadedModConditions());
        register(new ProcessorPlantId());
    }
}
