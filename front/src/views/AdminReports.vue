<template>
    
    <Reports :data="reports"
    @show-confirm-rejection-dialog = "showConfirmRejectionDialog"
    @show-confirm-allowing-dialog  = "showConfirmAllowingDialog"
    
    ></Reports>

    <div v-if="modalVisible">
        <AnswerOnReportModal
        @modal-closed = "modalVisible=false"
        @succ-pop-up-closed = "hideAnsweredOrIngored"
        :clientFullName="clickedReport.clientName"
        :ownerFullName="clickedReport.ownerName"
        :clientEmail="clickedReport.clientEmail"
        :ownerEmail="clickedReport.ownerEmail"
        :reportId="clickedReport.id"
         />
    </div>


    <div v-if="confirmationPopUpVisible">
    <ConfirmationPopUp
    :title="ignoreTitle"
    :message="ignoreMessage"
    @modal-closed = "confirmationPopUpVisible = false"
    @confirmed-event = "ingoreReport" 
    />
    </div>
    <SuccessPopUp v-show="succPopUpVisible"
        @close = "succPopUpVisible = false"
        :mess = succMessage
    />
    <ErrorPopUp v-show="errorPopUpVisible" 
        @close = closeErrorPopUp
        :mess = errMessage
    /> 

    
    
        
</template>

<script>
import axios from 'axios'
import Reports from '../components/Reports.vue'
import SuccessPopUp from '../components/SuccessPopUp.vue'
import ErrorPopUp from '../components/ErrorPopUp.vue'
import AnswerOnReportModal from '../components/AnswerOnReportModal.vue'

export default {
    name : 'AdminReports',
    components : {
        Reports, SuccessPopUp, ErrorPopUp, AnswerOnReportModal
    },  
    data () {
        return {
                reports: [], 
                modalVisible : false,
                ignoreTitle : 'Are you sure?',
                ignoreMessage : 'Report is going to be ignored',
                confirmationPopUpVisible : false,
                clickedReport : '',


            }

        
    },

    methods : {
        showConfirmAllowingDialog : function(report){
            this.modalVisible = true;
            this.clickedReport = report;


        },

        hideAnsweredOrIgnored(){
            this.reports = this.reports.filter(item => item != report);
            this.clickedReport.isAnswered = true;
            this.reports.push();
        },
        
        showConfirmRejectionDialog : function(report){
            this.confirmationPopUpVisible = true;
            this.reports = this.reports.filter(item => item != report);
            this.reports.push();
            
        },
        ignoreReport(){
            console.log("...")  ;
        }
    },

    mounted () {
        axios.get('api/admin/reports', {headers: {'authorization': window.localStorage.getItem("token") }}).then((response) => {
            this.reports = response.data;


    }).catch((e) => {
        this.errMessage = e;
        this.errorPopUpVisible = true;
    });
        
    }
}

</script>