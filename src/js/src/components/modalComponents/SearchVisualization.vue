<template>
  <div class="searchVisualizationContainer">
    <div class="searchVisualizationHeadline">
      <h2>Visualization of search result by domain</h2>
      <p>
        query: <span>{{ query }}</span>
      </p>
      <p v-if="searchAppliedFacets.length > 0">
        filters: <span>{{ searchAppliedFacets.join('') }}</span>
      </p>
    </div>
    <div v-show="!loading" key="d3-viz" class="visualized" />
    <div v-if="loading" key="spinner-viz" class="spinner" />
  </div>
</template>

<script>

import { requestService } from '../../services/RequestService'
import { mapState } from 'vuex'
import Visualization from './Visualization'

export default {
  name: 'SearchVisualization',
  data: () => ({
    loading:false
  }),
  computed: {
    ...mapState({
      query: state => state.Search.query,
      searchAppliedFacets: state => state.Search.searchAppliedFacets,
      solrSettings: state => state.Search.solrSettings,
    })
  },
// For test purposes
 /* watch: {
    loading: function(val) {
      console.log('LOADING CHANGED!')
    }
  }, */
  mounted () {
    this.loading = true
    Visualization.createVisualization(this.query, this.searchAppliedFacets, this.solrSettings).then(() =>{
    this.loading = false
    })
  }
}

</script>

    
