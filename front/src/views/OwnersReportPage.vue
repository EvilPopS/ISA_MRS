<template>
    <div v-if="showSearch">
        <div class="container">
            <span class="d-flex justify-content-left">
                <h1>Check your bussines here.. &#160; &#160;</h1>
                <img src="../assets/icons8-chart-64.png">
            </span>
            <div class="row">
                <div class="col-4">
                    <label>Report kind</label>
                    <select class="form-select mycombo" aria-label="Default select example" @change="graphSelected">
                        <option v-for="kind in graphKinds" :key="kind" :value="kind">{{kind}}</option>
                    </select>
                </div>
                <div class="col-4">
                    <label>Period</label>
                    <select class="form-select mycombo" aria-label="Default select example" @change="periodSelected">
                        <option v-for="period in timePeriods" :key="period" :value="period">{{period}}</option>
                    </select>
                </div>
                <div class="col-4">
                    <div class="my-btn-div">
                        <button type="button" id="show-btn" class="btn btn-success" @click="show()">Show</button>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <div class="chart-div" v-if="showPieChart()">
                        <GChart
                            type="PieChart"
                            :settings="{ packages: ['corechart'] }"
                            :data="chartData"
                            :options="chartOptionsPie.chart"
                        />
                    </div>
                    <div class="chart-div" v-if="showBarChart()">
                        <GChart
                            type="ColumnChart"
                            :data="chartData"
                            :options="chartOptionsBar.chart"
                        />
                    </div>
                </div>
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

            graphKinds: ['-Select graph type-', 'Occupancy', 'Revenue'],
            timePeriods: ['-Select period of time-', 'Weekly', 'Monthly', 'Yearly'],

            errorPoup: false,
            errMsg: '',

            showClicked: false,
            chartData: [
                ["Task", "Hours per Day"],
                ["Work", 11],
                ["Eat", 2],
                ["Commute", 2],
                ["Watch TV", 2],
                ["Sleep", 7]
            ],
            chartOptionsPie: {
                chart: {
                    title: "My Daily Activities",
                    pieHole: 0.4
                }
            },
            chartOptionsBar: {
                chart: {
                    title: "Company Performance",
                    subtitle: "Sales, Expenses, and Profit: 2014-2017"
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
        show() {
            this.showClicked = true
            if ((this.selectedGraph === "-Select graph type-") || (this.selectedPeriod === "-Select period of time-")) {
                this.errorPoup = true 
                this.errMsg = 'You need to select valid graph and period'
            }

        },
        showPieChart(){
            if ((this.selectedGraph === 'Occupancy' || this.selectedGraph === 'Revenue')
             && (this.selectedPeriod === 'Weekly' || this.selectedPeriod === 'Monthly') && this.showClicked)
                return true;
            else return false;
        },
        showBarChart(){
            if ((this.selectedGraph === 'Occupancy' || this.selectedGraph === 'Revenue')
             && (this.selectedPeriod === 'Yearly') && this.showClicked)
                return true;
            else return false;
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
        } else {
            //za boat
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

</style>