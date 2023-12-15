package it.pagopa.interop.signalhub.persister.logging;

import org.slf4j.MDC;
import reactor.core.publisher.Signal;
import reactor.util.context.Context;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import static it.pagopa.interop.signalhub.persister.utils.Const.TRACE_ID_KEY;

public class MdcContextLifter implements Consumer<Signal<?>> {

    @Override
    public void accept(Signal<?> signal) {
        if (!signal.isOnComplete() && !signal.isOnError()) {
            Optional<Map.Entry<Object, Object>> context = signal.getContextView().stream()
                    .filter(cxt -> cxt.getKey().equals(TRACE_ID_KEY))
                    .findFirst();

            context.ifPresent(ctx -> MDC.put(TRACE_ID_KEY, (String)ctx.getValue()));
        } else {
            MDC.clear();
        }
    }

    public static void setContextToMdc(Context context) {
        context.stream().forEach(entry -> {
            if (entry.getKey().equals(TRACE_ID_KEY)){
                MDC.put(TRACE_ID_KEY, (String) entry.getValue());
            }
        });
    }

    public static void clearMdc(){
        MDC.clear();
    }

}
