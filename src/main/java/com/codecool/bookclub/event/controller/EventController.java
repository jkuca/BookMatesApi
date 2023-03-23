package com.codecool.bookclub.event.controller;

import com.codecool.bookclub.book.model.Book;
import com.codecool.bookclub.event.model.Event;
import com.codecool.bookclub.event.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EventController {


    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

//    @GetMapping("/books/{book_id}/events")
//    public List<Event> getEventsForBook(@PathVariable("book_id") long bookId){
//        return eventService.getEventById(id);
//        return new ArrayList<>();
//    }

    @GetMapping("/event/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Event> getEventById(@PathVariable("id") long id) {
        Event event = eventService.getEventById(id);


        if (event == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(event, HttpStatus.OK);
        }
    }
    @GetMapping("/events")
    public List<Event> getALlEvents(){
        return eventService.getAllEvents();
    }



    @PostMapping("/events")
    public void addEvent(@RequestBody Event event){
        eventService.addEvent(event.getTitle(), event.getDescription(), event.getEventDate(), event.getMaxParticipants(), event.getUrl());
    }


    @PostMapping("/books/{book_id}/new-event")
    public boolean createEvent(@PathVariable("book_id") long bookId, @RequestBody Event event){
        return false;
    }

    @PutMapping("events/{event_id}")
    public void updateEventById (@PathVariable("event_id") long eventId, @RequestBody Event event) {
        eventService.updateEventById(eventId,event);
    }

    @DeleteMapping("events/{event_id}")
    public void deleteEventById (@PathVariable("event_id") long eventId){
         eventService.deleteEventById(eventId);
    }


}
