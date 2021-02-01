package carpet_extension.script;

import carpet.script.Expression;
import carpet.script.value.StringValue;
import carpet_extension.ExampleExtension;

public class ExampleScarpetFunctions {
    public static void apply(Expression expr){
        expr.addLazyFunction(ExampleExtension.identifier, 0, (c, t, lv) ->{
            return (_c, _t)-> StringValue.of(ExampleExtension.fancyName);
        });
    }
}
