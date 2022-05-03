<template>
  <div ref="map-root"
       :style="styleObject">
  </div>
</template>

<script>
  import View from 'ol/View'
  import Map from 'ol/Map'
  import TileLayer from 'ol/layer/Tile'
  import OSM from 'ol/source/OSM'
  import Feature from 'ol/Feature'
  import Point from 'ol/geom/Point'
  import * as proj from 'ol/proj'
  import Style from 'ol/style/Style'
  import Icon from 'ol/style/Icon'
  import * as layer from 'ol/layer'
  import Vector from 'ol/source/Vector'
  
  import 'ol/ol.css'

  import { transform } from 'ol/proj';

  export default {
    name: 'MapContainer',
    components: {},
    props: {
        coordinates: Array,
        mapHeight: String
    },

    data() {
        return {
            styleObject: {
                width: '100%',
                height: this.mapHeight + 'px'
            },
            lon: null,
            lat: null,
            map: null,
            markerFeature: null,
            map: null,
            vectorLayer: null,

            newCordinates: this.coordinates
        }
    },

    mounted() {
        setTimeout(this.initMap, 150);
    },

    methods: {
        initMap() {
            this.markerFeature = null
            this.vectorLayer = null
            this.map = null
            this.$refs['map-root'].innerHTML = ""

            this.markerFeature = new Feature({
                geometry: new Point(proj.fromLonLat([this.newCordinates[0], this.newCordinates[1]])),
            });

            this.markerFeature.setStyle(new Style({
                image: new Icon({
                    scale: 1,
                    src: 'https://nominatim.openstreetmap.org/ui/build/images/marker-icon.png'
                })
            }));


            this.vectorLayer = new layer.Vector({
                source: new Vector({
                    features: [this.markerFeature],
					wrapX: true,
                }),
				wrapX: false,
            });

            const center = proj.transform(this.coordinates, 'EPSG:4326', 'EPSG:3857');
            
            var vm = this;
            this.map = new Map({
                target: this.$refs['map-root'],
                layers: [
                    new TileLayer({
                        source: new OSM({wrapX:false})
                    }),
                    this.vectorLayer
                ],
                view: new View({
                    zoom: 15,
                    center: proj.transform([ this.newCordinates[0], this.newCordinates[1]], 'EPSG:4326', 'EPSG:3857'),
                }),
            }).on('click', function(evt){
                let lonlat = transform(evt.coordinate,'EPSG:3857', 'EPSG:4326');
                
                vm.newCordinates = []
                vm.newCordinates.push(lonlat[0])
                vm.newCordinates.push(lonlat[1])
                /* Kod koji pokusava bez pravljenja iznova, ali ne radi
                vm.markerFeature.geometry = new Point(proj.fromLonLat([vm.newCordinates[0], vm.newCordinates[1]]))
                vm.vectorLayer.source =  new Vector({
                    features: [vm.markerFeature],
					wrapX: true,
                })

                vm.map.layers = [
                    new TileLayer({
                        source: new OSM({wrapX:false})
                    }),
                    vm.vectorLayer
                ]
                vm.map.view = new View({
                    zoom: 12,
                    center: proj.transform([ vm.newCordinates[0], vm.newCordinates[1]], 'EPSG:4326', 'EPSG:3857'),
                }) */
                vm.$emit('changed-location', lonlat[0], lonlat[1])
                vm.initMap()
                });
        
        }
    }
  }
</script>