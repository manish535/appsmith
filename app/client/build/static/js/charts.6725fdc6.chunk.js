(this.webpackJsonpappsmith=this.webpackJsonpappsmith||[]).push([[8],{1097:function(t,a,r){"use strict";r.r(a);var e=r(4),n=r(17),o=r(18),i=r(21),c=r(20),s=r(22),h=r(15),l=r(0),u=r.n(l),p=r(16),g=r(38),C=r(11),d=r.n(C);function f(){var t=Object(h.a)(["\n  border: none;\n  border-radius: ",";\n  height: 100%;\n  width: 100%;\n  background: white;\n  box-shadow: 0 1px 1px 0 rgba(60,75,100,.14),0 2px 1px -1px rgba(60,75,100,.12),0 1px 3px 0 rgba(60,75,100,.2);\n  position: relative;\n  ",";\n  padding: 10px 0 0 0;\n}"]);return f=function(){return t},t}var v=r(1084),m=r(1085),b=r(1086);m(v),b(v),v.options.creditLabel=!1;var D=p.default.div(f(),(function(t){return"".concat(t.theme.radii[1],"px")}),(function(t){return t.isVisible?"":g.o})),y=function(t){function a(){var t,r;Object(n.a)(this,a);for(var o=arguments.length,s=new Array(o),h=0;h<o;h++)s[h]=arguments[h];return(r=Object(i.a)(this,(t=Object(c.a)(a)).call.apply(t,[this].concat(s)))).chartInstance=new v,r.getChartType=function(){var t=r.props,a=t.chartType,e=t.allowHorizontalScroll,n=t.chartData.length>1;switch(a){case"PIE_CHART":return"pie2d";case"LINE_CHART":return e?"scrollline2d":n?"msline":"line";case"BAR_CHART":return e?"scrollBar2D":n?"msbar2d":"bar2d";case"COLUMN_CHART":return e?"scrollColumn2D":n?"mscolumn2d":"column2d";case"AREA_CHART":return e?"scrollarea2d":n?"msarea":"area2d";default:return e?"scrollColumn2D":"mscolumn2d"}},r.getChartData=function(){var t=r.props.chartData;if(0===t.length)return[{label:"",value:""}];var a=t[0].data;return 0===a.length?[{label:"",value:""}]:a.map((function(t){return{label:t.x,value:t.y}}))},r.getChartCategoriesMutliSeries=function(t){for(var a=[],r=0;r<t.length;r++)for(var e=t[r].data,n=0;n<e.length;n++){var o=e[n].x;a.includes(o)||a.push(o)}return a},r.getChartCategories=function(t){var a=r.getChartCategoriesMutliSeries(t);return 0===a.length?{label:""}:a.map((function(t){return{label:t}}))},r.getSeriesChartData=function(t,a){var r={};if(0===t.length)return[{value:""}];for(var e=0;e<t.length;e++){var n=t[e];r[n.x]=n.y}return a.map((function(t){return{value:r[t]?r[t]:null}}))},r.getChartDataset=function(t){var a=r.getChartCategoriesMutliSeries(t);return t.map((function(t){var e=r.getSeriesChartData(t.data,a);return{seriesName:t.seriesName,data:e}}))},r.getChartConfig=function(){return{caption:r.props.chartName,xAxisName:r.props.xAxisName,yAxisName:r.props.yAxisName,theme:"fusion",captionAlignment:"left",captionHorizontalPadding:10,alignCaptionWithCanvas:0}},r.getChartDataSource=function(){return r.props.chartData.length<=1||"PIE_CHART"===r.props.chartType?{chart:r.getChartConfig(),data:r.getChartData()}:{chart:r.getChartConfig(),categories:[{category:r.getChartCategories(r.props.chartData)}],dataset:r.getChartDataset(r.props.chartData)}},r.getScrollChartDataSource=function(){var t=r.getChartConfig();return{chart:Object(e.a)({},t,{scrollheight:"10",showvalues:"1",numVisiblePlot:"5",flatScrollBars:"1"}),categories:[{category:r.getChartCategories(r.props.chartData)}],dataset:r.getChartDataset(r.props.chartData)}},r.createGraph=function(){var t=r.props.allowHorizontalScroll&&"PIE_CHART"!==r.props.chartType?r.getScrollChartDataSource():r.getChartDataSource(),a={type:r.getChartType(),renderAt:r.props.widgetId+"chart-container",width:"100%",height:"100%",dataFormat:"json",dataSource:t};r.chartInstance=new v(a)},r}return Object(s.a)(a,t),Object(o.a)(a,[{key:"componentDidMount",value:function(){var t=this;this.createGraph(),v.ready((function(){t.chartInstance.render()}))}},{key:"componentDidUpdate",value:function(t){if(!d.a.isEqual(t,this.props)){var a=this.getChartType();this.chartInstance.chartType(a),this.props.allowHorizontalScroll&&"PIE_CHART"!==this.props.chartType?this.chartInstance.setChartData(this.getScrollChartDataSource()):this.chartInstance.setChartData(this.getChartDataSource())}}},{key:"render",value:function(){return u.a.createElement(D,Object.assign({},this.props,{id:this.props.widgetId+"chart-container"}))}}]),a}(u.a.Component);a.default=y}}]);
//# sourceMappingURL=charts.6725fdc6.chunk.js.map