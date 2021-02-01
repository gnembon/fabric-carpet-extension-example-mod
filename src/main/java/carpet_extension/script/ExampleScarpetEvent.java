package carpet_extension.script;

import carpet.script.CarpetEventServer.Event;
import carpet.script.value.StringValue;
import carpet_extension.ExampleExtension;

import java.util.Collections;

/**
 * This is useful to add your own events to scarpet. You can rename the {@code onExampleEvent} event and use your own args
 * of course. It is currently not called anywhere, but you can call it whenever you want your scarpet {@code __on_example_event(args)}
 * event to trigger
 */

public class ExampleScarpetEvent extends Event {

    public static void noop() {} //to load events before scripts

    public ExampleScarpetEvent(String name, int reqArgs, boolean isGlobalOnly) {
        super(name, reqArgs, isGlobalOnly);
    }

    public void onExampleEvent(String what_happened){}

    public static ExampleScarpetEvent EXAMPLE_EVENT = new ExampleScarpetEvent("example_event", 1, false){
        @Override
        public void onExampleEvent(String what_happened){
            this.handler.call(
                    ()-> Collections.singletonList(StringValue.of(what_happened)),
                    ()-> ExampleExtension.minecraft_server.getCommandSource()
            );
        }
    };

}
