document.addEventListener('DOMContentLoaded', function() {
    var calendarEl = document.getElementById('calendar');

    var calendar = new FullCalendar.Calendar(calendarEl, {
        // timeZone: 'Europe/Istanbul',
        initialView: 'timeGridWeek',
        nowIndicator: true,
        allDaySlot: false,
        //height: 591,
        // slotMinTime:"07:00:00",
        // slotMaxTime:"18:30:00",
        eventColor:"purple",
        // lang: 'tr',
        handleWindowResize: true,
        //eventOverlap: false,
        businessHours: {
            daysOfWeek: [ 1, 2, 3, 4, 5],
            startTime: '08:00',
            endTime: '17:30',
        },
        dayHeaderFormat: {
            weekday: 'short', day: 'numeric', month: 'numeric', omitCommas: true
        },
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

        headerToolbar: {
            left: 'prev,next today',
            center: 'title',
            right: 'timeGridWeek,timeGridDay'
        },
        // events:  [
        //     {
        //         "title": "Event 1",
        //         "start": "2022-08-16T09:00:00",
        //         "end": "2022-08-16T18:00:00"
        //     },
        //     {
        //         "title": "Event 2",
        //         "start": "2022-08-16T11:00:00",
        //         "end": "2022-08-16"
        //     }
        // ]

        events : {
            url : '/reservation/overview_json_feed',
            type: 'POST',
            error: function () {
                alert('there was an error while fetching!');
            }
        }
    });



    calendar.render();
    // calendar.updateSize();
});