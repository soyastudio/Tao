package soya.framework.nezha.pipeline;

import com.google.common.eventbus.Subscribe;

public class ServiceExceptionHandler implements ServiceEventListener<ServiceExceptionEvent> {

    @Subscribe
    public void onEvent(ServiceExceptionEvent serviceExceptionEvent) {

    }
}