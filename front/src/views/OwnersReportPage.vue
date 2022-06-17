<template>
    <div v-if="showSearch">
        <div class="container">
            <span class="d-flex justify-content-left">
                <h1>Check your bussines here.. &#160; &#160;</h1>
                <img src="../assets/icons8-chart-64.png">
            </span>
            <div class="row">
                <div class="col-3">
                    <label>Report kind</label>
                    <select class="form-select mycombo" aria-label="Default select example" @change="graphSelected">
                        <option v-for="kind in graphKinds" :key="kind" :value="kind">{{kind}}</option>
                    </select>
                </div>
                <div class="col-3">
                    <label>Period</label>
                    <select class="form-select mycombo" aria-label="Default select example" @change="periodSelected">
                        <option v-for="period in timePeriods" :key="period" :value="period">{{period}}</option>
                    </select>
                </div>
                <div class="col-3">
                    <label>Month if monthly</label>
                    <select class="form-select mycombo" aria-label="Default select example" @change="monthSelected">
                        <option v-for="month in allMonths" :key="month" :value="month">{{month}}</option>
                    </select>
                </div>
                <div class="col-3">
                    <div class="my-btn-div">
                        <button type="button" id="show-btn" class="btn btn-success" @click="show()">Show</button>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <div class="chart-div" v-if="showBarChart()">
                        <GChart
                            type="ColumnChart"
                            :data="chartData"
                            :options="chartOptionsBar.chart"
                        />
                    </div>
                </div>
            </div>
            <div id="note-div">
                <label>*Note: Chart can include predictions for revenue and occupancy due to reservations that are upcoming.</label>
            </div>
        </div>
    </div>
    <div v-else>
        <h3>You don't have permission to visit this page!</h3>
    </div>
    <ErrorPopUp v-show="errorPoup" 
        @close = "errorPoup = false"
        :mess = "errMsg"
    /> 
</template>

<script>
import { GChart } from "vue-google-charts";
import ErrorPopUp from '../components/ErrorPopUp.vue'
import axios from 'axios'

export default {
    name: "OwnersReportPage",
    components: {
        GChart, ErrorPopUp
    },
    data() {
        return {
            showSearch: false,
            roleURL: '',
            searchRole: '',
            selectedPeriod: '',
            selectedGraph: '',
            selectedMonth: 'January',

            graphKinds: ['-Select graph type-', 'Occupancy', 'Revenue'],
            timePeriods: ['-Select period of time-', 'Weekly', 'Monthly', 'Yearly'],
            allMonths: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],

            errorPoup: false,
            errMsg: '',

            showClicked: false,
            chartData: [],
            chartOptionsBar: {
                chart: {
                    title: "",
                    subtitle: ""
                }
            }
        };
    },
    methods: {
        graphSelected(event){
            this.showClicked = false
            this.selectedGraph = event.target.value;
        },
        periodSelected(event){
            this.showClicked = false
            this.selectedPeriod = event.target.value;
        },
        monthSelected(event){
            this.showClicked = false
            this.selectedMonth = event.target.value;
        },
        show() {
            if ((this.selectedGraph === "-Select graph type-") || (this.selectedPeriod === "-Select period of time-")) {
                this.errorPoup = true 
                this.errMsg = 'You need to select valid graph and period'
            }
            this.setChartDataForBar()

            axios.get("api/" + this.roleURL + "/get-chart-data/" + this.selectedGraph + "/" + this.selectedPeriod + "/" + this.selectedMonth, {headers: {'authorization': window.localStorage.getItem("token") }})
                    .then((response) => {
                        for (let i = 1; i < response.data.length; i++) {
                            response.data[i][1] = parseFloat(response.data[i][1]);
                        }
                        this.chartData = response.data
                        this.showClicked = true
                    })
                    .catch(err => {
                            if (err.response.status === 404){
                                this.errMessage = "Owner with that email doesn't!";
                                this.errorPopUpVisible = true;
                            } 
                            else if (err.response.status === 401) {
                                this.errMessage = "You are not authorized!";
                                this.errorPopUpVisible = true;
                            }
                            else if (err.response.status === 422) {
                                this.errMessage = "Data is wrong or missing.";
                                this.errorPopUpVisible = true;
                            } else {
                                this.errMessage = "Uups! Something went wrong...";
                                this.errorPopUpVisible = true;
                            }
            });
            
        },
        showBarChart(){
            if ((this.selectedGraph === 'Occupancy' || this.selectedGraph === 'Revenue')
             && (this.selectedPeriod === 'Yearly' || this.selectedPeriod === 'Weekly' || this.selectedPeriod === 'Monthly') && this.showClicked)
                return true;
            else return false;
        },
        setChartDataForBar() {
            if (this.selectedGraph === 'Occupancy' && this.selectedPeriod === 'Weekly')
                this.chartOptionsBar.chart.title = "Weekly occupancy of your bussines(in days) for current week(monday-sunday)."
            else if (this.selectedGraph === 'Occupancy' && this.selectedPeriod === 'Monthly')
                this.chartOptionsBar.chart.title = "Monthly occupancy of your bussines(in days) for " + this.selectedMonth + "."
            else if (this.selectedGraph === 'Revenue' && this.selectedPeriod === 'Weekly')
                this.chartOptionsBar.chart.title = "Weekly revenue of your bussines(in euros) for current week(monday-sunday)."
            else if (this.selectedGraph === 'Revenue' && this.selectedPeriod === 'Monthly')
                this.chartOptionsBar.chart.title = "Monthly revenue of your bussines(in euros) for " + this.selectedMonth + "."
            else if (this.selectedGraph === 'Occupancy')
            {
                this.chartOptionsBar.chart.title = "Yearly occupancy of your bussines(in days) for current year."
                this.chartOptionsBar.chart.subtitle = "Occupancy of your rentals"
            } 
            else 
            {
                this.chartOptionsBar.chart.title = "Yearly revenue of your bussines(in euros) for current year."
                this.chartOptionsBar.chart.subtitle = "Revenue of your rentals"
            }
        }
    },
    mounted() {
        this.searchRole = window.localStorage.getItem("userRole")
        if (this.searchRole === "COTTAGE_OWNER") {
            this.roleURL = "cottage-owner"
            this.showSearch = true
        } else if (this.searchRole === "INSTRUCTOR"){
            this.roleURL = "fishingInstructor"
            this.showSearch = true
        } else if (this.searchRole === "BOAT_OWNER") {
            this.roleURL = "boat-owner"
            this.showSearch = true
        } else {
            this.showSearch = false
        }
    }
}
</script>

<style scoped>

    h3 {
       margin-top: 10%;
    }

    .container {
        margin-top: 5% !important;
    }

    .mycombo {
        outline: none !important;
        border:1px solid rgba(51, 92, 80, 0.8);
        box-shadow: 0 0 10px rgba(51, 92, 80, 0.8);
    }

    .mycombo:focus {
        border:1px solid rgba(51, 92, 80, 0.8);
        box-shadow: 0 0 10px rgba(51, 92, 80, 0.8);
    }

    /*Za prikaz dugmeta na dnu div-a */
    .my-btn-div {
        display: flex;
        height: 100%;
    }

    button {
        display: inline-block;
        align-self: flex-end;
    }

    h1 {
        color: rgba(51, 92, 80, 0.8);
        margin-bottom: 2%;
        margin-top: 2%;
        display: inline-block !important;
    }

    img {
        margin-top: 2%;
        height: 45px;
    }

    .chart-div {
        margin-top: 10%;
    }

    #note-div label {
        margin-top: 25%;
        color: red;
        font-size: 8px !important;
    }

</style>