<template>
  <div>
    <el-row>
      <el-col :span="12">
        <div id="main" style="width: 500px; height: 400px"> </div>
      </el-col>
      <el-col :span="12">
        <div id="pie" style="width: 500px; height: 400px"> </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts';

export default {
  name: "Home",
  data() {
    return {}
  },
  mounted() {
    var option = {
      title: {
        text: "Membership Statistics by Quarter",
        subtext: "Line Chart",
        left: "center"
      },
      xAxis: {
        type: 'category',
        data: ["1st quarter", "2nd quarter", "3rd quarter", "4th quarter"]
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          data: [],
          type: 'line'
        },
        {
          data: [],
          type: 'bar'
        }
      ]
    };

    // pie charts
    var pieOption = {
      title: {
        text: 'Membership Statistics by Quarter',
        subtext: 'Pie Chart',
        left: 'center'
      },
      tooltip: {
        trigger: 'item'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      series: [
        {
          type: 'pie',
          radius: '50%',
          data: [],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
    };

    var chartDom = document.getElementById('main');
    var myChart = echarts.init(chartDom);

    var pieDom = document.getElementById('pie');
    var pieChart = echarts.init(pieDom);

    this.request.get("http://localhost:9090/echarts/members").then(res => {
      //option.xAxis.data = res.data.x;
      option.series[0].data = res.data;
      option.series[1].data = res.data;
      myChart.setOption(option);
      pieOption.series[0].data = [
        {name: "1st quarter", value : res.data[0]},
        {name: "2nd quarter", value : res.data[1]},
        {name: "3rd quarter", value : res.data[2]},
        {name: "4th quarter", value : res.data[3]},
      ]
      pieChart.setOption(pieOption)
    })

  }
}
</script>

<style scoped>

</style>
