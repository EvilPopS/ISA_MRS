<template>
    <FullCalendar :options="calendarOptions"/>
</template>

<script>
import '@fullcalendar/core/vdom'
import FullCalendar from '@fullcalendar/vue3'
import dayGridPlugin from '@fullcalendar/daygrid'
import interactionPlugin from '@fullcalendar/interaction'

export default {
    name : "CalendarComponent",
    components : { FullCalendar

    },
    props : {
        reservations : Array,
    },

    data (){return {
        calendarOptions: {
        plugins: [ dayGridPlugin, interactionPlugin ],
        initialView: 'dayGridMonth',
        events: []
      },

    }},


    created(){

        for(let i = 0; i < this.reservations.length; i++){
            let col = "red"
            if (this.reservations[i].reservationKind === "Action reservation")
                col = "yellow"
            else if (this.reservations[i].reservationKind === "Reservation")
                col = "green"

            this.calendarOptions.events.push({
                title : this.reservations[i].reservationKind,
                start : new Date(this.reservations[i].startDate),
                end : new Date(this.reservations[i].endDate),
                color : col
            })
        }

    }
}
</script>
