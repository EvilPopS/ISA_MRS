<template>
    <div id="myModal" class="modal">
        <div class="modal-content">
            <button id="close_btn" @click="closeWindow()" class="close">X</button>
            <div class="container">
                <div class="row">
                    <div class="col-8">
                        <span>Calendar</span>
                            <div>
                                <CalendarComponent :reservations="this.reservations" :key="this.reservations"/>
                            </div>
                            
                    </div>
                    <div class="col-4">
                        <div id="optionsBar" class="card flex-card">
                            <TabNav
                                :tabs="['Regular', 'Action', 'Unavailability']"
                                :selected="selected"
                                @selected="setSelected"
                            >
                                <Tab :isSelected="selected === 'Regular'">
                                    <AddRegularRes
                                        @modal-closed = "closeWindow()"
                                        :rental="choosenRental"
                                    ></AddRegularRes>
                                </Tab>

                                <Tab :isSelected="selected === 'Action'">
                                    <AddActionRes
                                        @modal-closed = "closeWindow()"
                                        :rental="choosenRental"
                                    ></AddActionRes>
                                </Tab>

                                <Tab :isSelected="selected === 'Unavailability'">
                                    <AddUnvailablePeriod
                                        @modal-closed = "closeWindow()"
                                        :rental="choosenRental"
                                    ></AddUnvailablePeriod>
                                </Tab>

                            </TabNav>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>

// import '@fullcalendar/core/vdom'
// import FullCalendar from '@fullcalendar/vue3'
// import dayGridPlugin from '@fullcalendar/daygrid'
// import interactionPlugin from '@fullcalendar/interaction'


import CalendarComponent from '../components/CalendarComponent.vue'
import Tab from '../components/Tab'
import TabNav from '../components/TabNav'
import AddActionRes from '../components/AddActionRes.vue'
import AddRegularRes from '../components/AddRegularRes.vue'
import AddUnvailablePeriod from '../components/AddUnvailablePeriod.vue'
import axios from 'axios'

export default {
    name: "NewReservationsComponent",
    components: {
        Tab, TabNav, AddActionRes, AddRegularRes, AddUnvailablePeriod, CalendarComponent
    },
    props: {
        choosenRental: Object
    },
    data(){
        return {
            reservations : [],
            selected: "Regular",
    //         calendarOptions: {
    //     plugins: [ dayGridPlugin, interactionPlugin ],
    //     initialView: 'dayGridMonth',
    //     events: []
    //   },
            
            
      

        }
    },
    methods: {
        closeWindow : function(){
            this.$emit('modal-closed');
        },
        setSelected(tab) {
            this.selected = tab;
        }
    },

    mounted(){
        axios.get('api/rental/get-reservations-by-rental/' + this.choosenRental.id, {headers: {'authorization': window.localStorage.getItem("token") }})
                    .then((response) => {
                        this.reservations = response.data;
                    }
                ).catch((e) => {
                    console.log(e);
                });
        
    }
}
</script>

<style scoped>
    b{
        color: black;
    }

    #close_btn{
        height: 30px;
        width: 25px;
    }

    .modal {
        display: block; /* Hidden by default */
        position: fixed; /* Stay in place */
        z-index: 1; /* Sit on top */
        padding-top: 100px; /* Location of the box */
        left: 0;
        top: 0;
        width: 100%; /* Full width */
        height: 100%; /* Full height */
        overflow: auto; /* Enable scroll if needed */
        background-color: rgb(0,0,0); /* Fallback color */
        background-color: rgba(0,0,0,0.4); /* Black w/ opacity */
    }

    /* Modal Content */
    .modal-content {
        background-color: #fefefe;
        margin: auto;
        padding: 10px;
        border: 1px solid #888;
        width: 90%;
    }

    /* The Close Button */
    .close {
        color: #aaaaaa;
        float: right;
        font-size: 16px;
        font-weight: bold;
    }

    .close:hover,
    .close:focus {
        color: #000;
        text-decoration: none;
        cursor: pointer;
    }

    #tabNav {
        margin: 0px 0px 30px 0px !important; 
    }

</style>