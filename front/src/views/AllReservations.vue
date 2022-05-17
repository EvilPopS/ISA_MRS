<template>
    <div v-if="showSearch">
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
    </div>
    <div v-else>
        <h3>You don't have permission to visit this page!</h3>
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
            searchRole: '',
            roleURL: '',
            showSearch: true,
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
        this.searchRole = window.localStorage.getItem("userRole")
        if (this.searchRole === "COTTAGE_OWNER"){
            this.roleURL = "cottage-owner"
            this.showSearh = true
        } else if (this.searchRole === "INSTRUCTOR"){
            this.roleURL = "fishingInstructor"
            this.showSearh = true
        } else if (this.searchRole === "BOAT_OWNER") {
            //za boat
        } else {
            this.showSearch = false
        }

        axios.get('api/' + this.roleURL + '/all-reservations', {headers: {'authorization': window.localStorage.getItem("token") }})
            .then((response) => {
                this.resData = response.data
                this.getHistoryData()
                this.getUpcomingData()
            }).catch((error) => {
                alert('Error happened: ' + error.data)
            });
    }
}
</script>

<style scoped>

</style>