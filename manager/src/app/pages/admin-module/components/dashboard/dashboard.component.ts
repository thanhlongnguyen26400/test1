import { Component, OnInit } from '@angular/core';
import { INCOME_TYPE_CODE, STATUS_CODE } from 'app/constant/constant';
import { ApiService } from 'app/services/api.service';
import { HelperService } from 'app/services/helper.service';
import Chart from 'chart.js';
import * as moment from 'moment';


@Component({
  selector: 'dashboard-cmp',
  moduleId: module.id,
  templateUrl: 'dashboard.component.html'
})

export class DashboardComponent implements OnInit {

  public canvas: any;
  public ctx;
  public chartColor;
  public chartEmail;
  public chartHours;
  history_type: any[] = [{ color: '#e3e3e3', name: 'Income' }, { color: '#4acccd', name: 'Expenditure' }];
  summary = {
    income: 0,
    expenditure: 0,
    balance_month: 0,
    saving_money: 0,
    lastUpdate: new Date()
  }
  constructor(
    private apiService: ApiService,
    private helperService: HelperService
  ) { }

  ngOnInit() {
    this.getDashboardSummary();
  }
  configDataChartMonth(data) {
    const month = moment().format('MM');
    const dataset = {
      labels: [],
      income: [],
      expend: [],
      dummy: []
    }
    Object.keys(data).forEach(key => {
      const obj = data[key];
      dataset.labels.push(`${key}`);
      dataset.income.push(obj[INCOME_TYPE_CODE.INCOME]);
      dataset.expend.push(obj[INCOME_TYPE_CODE.EXPENDITURE]);
      dataset.dummy.push(10);
    });
    return dataset;
  }
  configChartMonth(data) {
    this.chartColor = "#FFFFFF";

    this.canvas = document.getElementById("chartHours");
    this.ctx = this.canvas.getContext("2d");
    const datasets = this.configDataChartMonth(data);
    // console.log('months', datasets);
    var dataFirst = {
      data: datasets.income,
      fill: false,
      borderColor: '#fbc658',
      backgroundColor: 'transparent',
      pointBorderColor: '#fbc658',
      pointRadius: 4,
      pointHoverRadius: 4,
      pointBorderWidth: 8,
      label: 'Thu nhập'
    };

    var dataSecond = {
      data: datasets.expend,
      fill: false,
      borderColor: '#51CACF',
      backgroundColor: 'transparent',
      pointBorderColor: '#51CACF',
      pointRadius: 4,
      pointHoverRadius: 4,
      pointBorderWidth: 8,
      label: 'Tiêu thụ'
    };
    var dataDummy = {
      data: datasets.dummy,
      fill: false,
      borderColor: 'transparent',
      backgroundColor: 'transparent',
      pointBorderColor: 'transparent',
      pointRadius: 0,
      pointHoverRadius: 0,
      pointBorderWidth: 0
    };
    var speedData = {
      labels: datasets.labels,
      datasets: [dataFirst, dataSecond]
    };

    var chartOptions = {
      legend: {
        display: false,
        position: 'top',
      },
      scales: {
        yAxes: [{
          display: true,
          ticks: {
            beginAtZero: true,
            suggestedMin: 0,
          }
        }]
      }
    };

    var lineChart = new Chart(this.ctx, {
      type: 'line',
      hover: false,
      data: speedData,
      options: chartOptions
    });
  }
  configDataPie(data, type) {
    const label = [], color = {
      background: [],
      border: []
    }
    const money = [];
    Object.keys(data).forEach(key => {
      const [history_type] = type.filter(x => x.id == key);
      label.push(`${history_type.name}${history_type.description ? ' - ' + history_type.description : ''}`);
      const color_add = this.helperService.addAlpha(history_type.color, 0.5);
      color.background.push(color_add);
      color.border.push(history_type.color)
      money.push(+data[key]);
    });
    const sum = money.reduce((prev, cur) => prev + cur, 0);
    if (sum == 0) {
      money.fill(1, 0);
    }
    return { label, color, money };
  }
  configPie(data, type) {
    //income
    const income = this.configDataPie(data[INCOME_TYPE_CODE.INCOME], type);
    const expend = this.configDataPie(data[INCOME_TYPE_CODE.EXPENDITURE], type);
    // console.log('income', income);
    console.log('expend', expend);
    if (income.label[0] != '') {
      this.history_type = type.map(x => { return { id: x.id, name: `${x.name}${x.description ? ' - ' + x.description : ''}`, color: x.color } });
    }
    this.canvas = document.getElementById("chartEmail");
    this.ctx = this.canvas.getContext("2d");
    this.chartEmail = new Chart(this.ctx, {
      type: 'pie',
      data: {
        labels: [...expend.label],
        datasets: [{
          label: "Tiêu thụ",
          pointRadius: 0,
          pointHoverRadius: 0,
          backgroundColor: expend.color.background,
          borderWidth: 1,
          borderColor: expend.color.border,
          data: expend.money
        }, {
          label: "Thu nhập",
          pointRadius: 0,
          pointHoverRadius: 0,
          backgroundColor: income.color.background,
          borderWidth: 1,
          borderColor: income.color.border,
          data: income.money,
          rotation: 225
        }]
      },

      options: {

        legend: {
          display: false
        },

        pieceLabel: {
          render: 'percentage',
          fontColor: ['white'],
          precision: 2
        },

        tooltips: {
          enabled: true
        },

        scales: {
          yAxes: [{

            ticks: {
              display: false
            },
            gridLines: {
              drawBorder: false,
              zeroLineColor: "transparent",
              color: 'rgba(255,255,255,0.05)'
            }

          }],

          xAxes: [{
            barPercentage: 1.6,
            gridLines: {
              drawBorder: false,
              color: 'rgba(255,255,255,0.1)',
              zeroLineColor: "transparent"
            },
            ticks: {
              display: false,
            }
          }]
        },
      }
    });
  }
  configDataChartYear(data) {
    const month = moment().format('MM');
    const dataset = {
      labels: [],
      income: {
        data: [],
        background: [],
        border: []
      },
      expend: {
        data: [],
        background: [],
        border: []
      },
      dummy: []
    }
    Object.keys(data).forEach(key => {
      const obj = data[key];
      dataset.labels.push(key);
      dataset.income.data.push(obj[INCOME_TYPE_CODE.INCOME]);
      dataset.income.background.push('rgba(255, 99, 132, 0.2)');
      dataset.income.border.push('rgba(255, 99, 132)');
      dataset.expend.data.push(obj[INCOME_TYPE_CODE.EXPENDITURE]);
      dataset.expend.background.push('rgba(255, 159, 64, 0.2)');
      dataset.expend.border.push('rgb(255, 159, 64)');
      dataset.dummy.push(10);
    });
    return dataset;
  }
  configChartYear(data) {
    var speedCanvas = document.getElementById("speedChart");
    const datasets = this.configDataChartYear(data);
    var income = {
      label: 'Thu nhập',
      data: datasets.income.data,
      backgroundColor: datasets.income.background,
      borderColor: datasets.income.border,
      borderWidth: 1
    }
    var expend = {
      label: 'Tiêu thụ',
      data: datasets.expend.data,
      backgroundColor: datasets.expend.background,
      borderColor: datasets.expend.border,
      borderWidth: 1
    }
    // var dummy = {
    //   data: datasets.dummy,
    //   backgroundColor: 'transparent',
    //   borderColor: 'transparent',
    //   borderWidth: 1,
    //   showTooltips: false
    // }
    var speedData = {
      labels: datasets.labels,
      datasets: [income, expend]
    };

    var chartOptions = {
      scales: {
        yAxes: [{
          display: true,
          ticks: {
            beginAtZero: true,
            suggestedMin: 0,
          }
        }]
      }
    };

    var lineChart = new Chart(speedCanvas, {
      type: 'bar',
      hover: false,
      data: speedData,
      options: chartOptions
    });
  }
  getDashboardSummary() {
    this.apiService.getSummary().subscribe((res: any) => {
      if (res['code'] == STATUS_CODE.SUCCESS) {
        // console.log(res);
        const { graph, line_chart, pie, ...left_tover } = res.data;
        const data = res.data;
        this.configChartYear(graph);
        this.configChartMonth(line_chart);
        this.configPie(pie, data.history_type);
        this.summary = left_tover;
      }
    });
  }
}
