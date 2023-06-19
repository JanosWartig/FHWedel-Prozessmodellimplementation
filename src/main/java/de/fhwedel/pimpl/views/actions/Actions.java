package de.fhwedel.pimpl.views.actions;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Service
public class Actions {

    private List<Consumer<LocalDate>> listenersRequestOrderView = new ArrayList<>();

    public void registerRequestSearchOrder(Consumer<LocalDate> c) {
        listenersRequestOrderView.add(c);
    }

    public void triggerRequestOrderView(LocalDate date) {
        for(Consumer<LocalDate> c : listenersRequestOrderView) c.accept(date);
    }

}
