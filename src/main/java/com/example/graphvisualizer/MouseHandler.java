package com.example.graphvisualizer;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

// https://stackoverflow.com/a/41080735
public class MouseHandler implements EventHandler<MouseEvent> {
    private final EventHandler<MouseEvent> onClickedEventHandler;
    private final EventHandler<MouseEvent> onDraggedEventHandler;

    private boolean dragging = false;

    public MouseHandler(EventHandler<MouseEvent> onClickedEventHandler, EventHandler<MouseEvent> onDraggedEventHandler) {
        this.onClickedEventHandler = onClickedEventHandler;
        this.onDraggedEventHandler = onDraggedEventHandler;
    }

    @Override
    public void handle(MouseEvent event) {
        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            dragging = false;
        }
        else if (event.getEventType() == MouseEvent.DRAG_DETECTED) {
            dragging = true;
        }
        else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
            //maybe filter on dragging (== true)
            onDraggedEventHandler.handle(event);
        }
        else if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
            if (!dragging) {
                onClickedEventHandler.handle(event);
            }
        }

    }
}
