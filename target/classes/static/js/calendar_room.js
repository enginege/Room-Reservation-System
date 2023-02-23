//Start and End date/time fields.
// const startDate = document.getElementById('start_datetime');
// const endDate = document.getElementById('end_datetime');

// Parse the URL to obtain the id of the current room to set the JSON feed url which feeds the calendar.
var path = window.location.pathname.split('/');
var room_id = path[path.length-1];
var path_url = `/reservation/room_reservation_json_feed/${room_id}`;

document.addEventListener('DOMContentLoaded', function() {
    var calendarEl = document.getElementById('calendar');

    var calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'timeGridWeek',
        // timeZone: 'Europe/Istanbul',
        // validRange: {
        //     start: toIsoString(dt).slice(0,-9)
        // },
        // editable: true,
        //height: 591,
        // slotMinTime:"07:00:00",
        // slotMaxTime:"18:30:00",
        // lang: 'tr',
        //eventOverlap: false,
        nowIndicator: true,
        allDaySlot: false,
        eventColor:"purple", //Rather than hardcoding the color, set the color of events to fetched colors from the database (different color codes should be assigned to each room).
        handleWindowResize: true,
        businessHours: {
            daysOfWeek: [ 1, 2, 3, 4, 5],
            startTime: '08:00',
            endTime: '17:30',
        },
        dayHeaderFormat: {
            weekday: 'short', day: 'numeric', month: 'numeric', omitCommas: true
        },
        headerToolbar: {
            left: 'prev,next today',
            center: 'title',
            right: 'timeGridWeek,timeGridDay'
        },
        events : {
            url : path_url,
            type: 'POST',
            error: function () {
                alert('there was an error while fetching!');
            }
        },

        // eventDragStart: function( info ) {
        //     var event = calendar.getEventById( info.event.id );
        //     event.remove();
        //     event.backgroundColor = 'blue';
        //     calendar.addEvent(event);
        // },

        //Triggered when dragging stops and the event has moved to a different day/time.
        eventDrop: function( eventDropInfo ) {
            if (eventDropInfo.event.start != null) {
                startDate.value = toIsoString(eventDropInfo.event.start).slice(0,-9);
            }
            if (eventDropInfo.event.end != null) {
                endDate.value = toIsoString(eventDropInfo.event.end).slice(0,-9);
            }
            endDate.min = toIsoString(eventDropInfo.event.start).slice(0,-9);
        },

        //Triggered when resizing stops and the event has changed in duration.
        eventResize: function( eventResizeInfo ) {
            // startDate.value = toIsoString(eventResizeInfo.event.start).slice(0,-9); //Start date/time never changes while resizing
            if (eventResizeInfo.event.end != null) {
                endDate.value = toIsoString(eventResizeInfo.event.end).slice(0,-9);
                endDate.min = toIsoString(eventResizeInfo.event.start).slice(0,-9);
            }
        },

        //Triggered when the user mouses over an event.
        eventMouseEnter: function(mouseEnterInfo ) {
            console.log(mouseEnterInfo .event.title);
            //For extendedProps {description: "Lecture", department: "BioChemistry"}
        }


        // extendedProps: {
        //     description: "description"
        // },
        // titleFormat: { // will produce something like "Tuesday, September 18, 2018"
        //     month: 'long',
        //     year: 'numeric',
        //     day: 'numeric',
        //     weekday: 'long'
        // },

        // eventClick: function(info) {
        //     alert('Event: ' + info.event.title);
        //     info.el.style.borderColor = 'red';
        // },

        // eventDidMount: function(info) {
        //     var tooltip = new Tooltip(info.el, {
        //         title: info.event.title,
        //         placement: 'top',
        //         trigger: 'hover',
        //         container: 'body'
        //     });
        // },

    });


    // Creating a mock event to better visualize the newly created reservation.
    // const tmpEventName = document.getElementById('meetingCategory');
    //
    // console.log()

    const tmpEvent = {id:"tmp",title:"New Reservation", start:startDate.value, end:"", color: 'gold', textColor: 'black' , editable: 'true'};

    var event = calendar.getEventById('tmp')
    if (event != null) {
        console.log(event.title);
        event.remove();
    }

    if (startDate.value !== "" && endDate.value !== "") {
        tmpEvent.start = startDate.value;
        tmpEvent.end = endDate.value;
        calendar.addEvent(tmpEvent);
        calendar.gotoDate(tmpEvent.start);
        console.log("Initial event");
    }


    startDate.addEventListener('change', () => {
        var event = calendar.getEventById('tmp')
        if(event==null) {
            tmpEvent.start = startDate.value;
            tmpEvent.end = endDate.value;
            calendar.addEvent(tmpEvent);
            calendar.gotoDate(tmpEvent.start);
            console.log("Created event");
        }
        else {
            // event.remove();
            // tmpEvent.start = startDate.value;
            // tmpEvent.end = endDate.value;
            // calendar.addEvent(tmpEvent);
            // calendar.gotoDate(tmpEvent.start);
            event.setStart(startDate.value);
            event.setEnd(endDate.value);
            calendar.gotoDate(event.start);
            console.log("Adjusted event");
        }
    });

    endDate.addEventListener('change', () => {
        var event = calendar.getEventById('tmp')
        if(event==null) {
            console.log(startDate.value);
            if (startDate.value !== "")
            {
                tmpEvent.start = startDate.value;
                tmpEvent.end = endDate.value;
                calendar.addEvent(tmpEvent);
                calendar.gotoDate(tmpEvent.start);
            }
            console.log("Created event");
        }
        else {
            // event.remove();
            // tmpEvent.start = startDate.value;
            // tmpEvent.end = endDate.value;
            // calendar.addEvent(tmpEvent);
            // calendar.gotoDate(tmpEvent.start);
            // console.log("Adjusted event");
            event.setStart(startDate.value);
            event.setEnd(endDate.value);
            calendar.gotoDate(event.start);
            console.log("Adjusted event");
        }

    });


    calendar.render();
    // calendar.updateSize();
});