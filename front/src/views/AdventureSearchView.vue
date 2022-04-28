<template>
    <div>
        <SearchBar
        @search-adventure="searchAdventure"
        /> 
        <AdventuresView 
        :searchMode="true"
        :searchedAdventures="this.searchedAdventures"
        :key="this.searchedAdventures" 

        
        />
        
    </div>
     
</template>


<script>
import axios from 'axios'
import SearchBar from '../components/SearchBar.vue'
import AdventuresView from '../views/AdventuresView.vue'

export default {
    name : 'AdventureSearchView',
    components : {
        SearchBar,
        AdventuresView
    },

    data () {
        return {
            searchedAdventures : []
        }
    },


    methods  : {
        searchAdventure({adventureName}){
            axios.get('api/fishingInstructor/instructor@gmail.com/searchAdventure?adventureName=' + adventureName).then((res) => {
                console.log(res.data)
                this.searchedAdventures = res.data;
            }).catch((error) => {
                console.log(error);
            })
        }
    }
}

</script>
