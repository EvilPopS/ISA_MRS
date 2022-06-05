<template>
    
    <Reports :data="reports"
    @show-confirm-rejection-dialog = "showConfirmRejectionDialog"
    @show-confirm-allowing-dialog  = "showConfirmAllowingDialog"
    
    ></Reports>

    <div v-if="modalVisible">
        <AnswerOnReportModal
        @modal-closed = "modalVisible=false"
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
            reports: 
                [
                    {
                        message : "Nije mi se svidela usluga jer nije bilo zanimljivo.",
                        sentTime : "01/06/2022 11:11",
                        isNegative : true,
                        hasShowedUp : true,
                        senderName : 'Marko Markovic',
                        isAnswered : false,

                    },{
                        message : "Nije mi se svidela usluga jer nije bilo zanimljivo.",
                        sentTime : "02/06/2022 21:21",
                        isNegative : true,
                        hasShowedUp : true,
                        senderName : 'Milos Milosevic',
                        isAnswered : false,
                    }
                ],
                modalVisible : false,
                ignoreTitle : 'Are you sure?',
                ignoreMessage : 'Report is going to be ignored',
                confirmationPopUpVisible : false


            }

        
    },

    methods : {
        showConfirmAllowingDialog : function(report){
            this.modalVisible = true;
            this.reports = this.reports.filter(item => item != report);
            report.isAnswered = true;
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
    }
}

</script>