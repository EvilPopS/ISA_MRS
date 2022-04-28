<template>
    <div id="optionsBar" class="card flex-card">
        <TabNav
            :tabs="['Reservations history', 'Upcoming reservations']"
            :selected="selected"
            @selected="setSelected"
        >
        <Tab :isSelected="selected === 'Reservations history'">
            <ReservationsHistory :data="historyData"></ReservationsHistory>
        </Tab>

        <Tab :isSelected="selected === 'Upcoming reservations'">
            <UpcomingReservations :data="upcomingData"></UpcomingReservations>
        </Tab>


        </TabNav>
    </div>
</template>

<script>
import axios from 'axios';
import Tab from '../components/Tab'
import TabNav from '../components/TabNav'
import ReservationsHistory from '../components/ReservationsHistory'
import UpcomingReservations from '../components/UpcomingReservations'

export default {
    name: "AllReservations",
    components: {
        Tab, TabNav, ReservationsHistory, UpcomingReservations
    },
    data(){
        return {
            logged: "COTTAGE_OWNER",
            resData: [],
            historyData: [],
            upcomingData: [],
            selected: "Reservations history"
        }
    },
    methods: {
        setSelected(tab) {
            this.selected = tab;
        },
        getHistoryData(){
            let todaysDate = new Date().toISOString()
            for (let i = 0; i < this.resData.length; i++){
                if (this.resData[i].endTime <= todaysDate){
                    this.historyData.push(this.resData[i])
                }
            }
        },
        getUpcomingData(){
            let todaysDate = new Date().toISOString()
            for (let i = 0; i < this.resData.length; i++){
                if (this.resData[i].endTime > todaysDate){
                    this.upcomingData.push(this.resData[i])
                }
            }
        }
    },
    mounted(){
        //posto je univerzalna komponenta sada ce na osnovu uloge ulogovanog da se poziva razliciti ajax poziv
        //verovatno ce se uloga dobavljati iz LocalStorage-a
        if (this.logged === "COTTAGE_OWNER") {
            axios.get('api/cottage-owner/all-reservations/' + "srdjan@gmail.com").then((response) => {
                this.resData = response.data
                this.getHistoryData()
                this.getUpcomingData()
            }).catch((error) => {
                console.log('Error happened: ' + error.name)
            });
        }
    }
}
</script>

<style scoped>

</style>