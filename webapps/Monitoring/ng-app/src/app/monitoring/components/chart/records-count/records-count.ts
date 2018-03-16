import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { AppService } from '@nb/core';
import { stringToRGB } from '@nb/core';
import { MonitoringService } from '../../../monitoring.service';
import { UserActivity } from '../../../models';

interface IChartDataItem {
    actUser: string;
    count: number;
}

@Component({
    selector: 'monit-records-count-chart-cmp',
    templateUrl: './records-count.html',
    styleUrls: ['./records-count.css']
})
export class RecordsCountChartComponent {

    chartConfigBar: any;
    chartConfigPie: any;
    barChartHeight: number = 100;

    constructor(
        private appService: AppService,
        private monitService: MonitoringService
    ) { }

    ngOnInit() {
        this.loadData();
    }

    ngOnDestroy() {
        this.appService.hideLoadSpinner();
    }

    loadData() {
        this.appService.showLoadSpinner();

        this.monitService.fetchRecordsCount().subscribe(
            data => {
                this.chartConfigBar = this.createBarChartConfig(data.payload.viewpage.result);
                this.chartConfigPie = this.createPieChartConfig(data.payload.viewpage.result);
            },
            error => { },
            () => this.appService.hideLoadSpinner()
        );
    }

    createBarChartConfig(data: IChartDataItem[]): any {

        this.barChartHeight = data.length * 32;
        let minValue: number = Number.MAX_VALUE;
        data.forEach(it => {
            if (minValue > it.count) {
                minValue = it.count;
            }
        });

        let labels = data.map(it => `${it.actUser} (${it.count})`),
            datasets = [{
                label: '',
                data: data.map(it => it.count),
                fill: false,
                lineTension: 0,
                borderColor: 'rgb(54, 162, 235)',
                borderWidth: 1,
                backgroundColor: 'rgb(54, 162, 235)',
                pointBorderColor: 'rgb(54, 162, 235)',
                pointBackgroundColor: 'rgb(54, 162, 235)'
            }];

        if (!labels.length) {
            minValue = 0;
        } else if (minValue > 0) {
            minValue--;
        } else {
            minValue = 0;
        }

        let config = {
            type: 'horizontalBar',
            data: {
                labels: labels,
                datasets: datasets
            },
            options: {
                legend: { display: false, labels: { fontColor: 'black' } },
                responsive: true,
                maintainAspectRatio: false,
                spanGaps: false,
                elements: { line: { tension: 0 } },
                tension: -1,
                tooltips: { mode: 'index', intersect: false },
                hover: { mode: 'nearest', intersect: true },
                scales: {
                    yAxes: [{ ticks: { beginAtZero: true, min: minValue } }],
                    xAxes: [{ barPercentage: 0.3, categorySpacing: 0 }]
                }
            }
        };

        return config;
    }

    createPieChartConfig(data: IChartDataItem[]): any {

        let labels = data.map(it => `${it.actUser} (${it.count})`),
            datasets = [{
                label: '',
                data: data.map(it => it.count),
                fill: false,
                lineTension: 0,
                borderColor: data.map(it => stringToRGB(it.actUser)),
                borderWidth: 1,
                backgroundColor: data.map(it => stringToRGB(it.actUser)),
                pointBorderColor: data.map(it => stringToRGB(it.actUser)),
                pointBackgroundColor: data.map(it => stringToRGB(it.actUser))
            }];

        let config = {
            type: 'pie',
            data: {
                labels: labels,
                datasets: datasets
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                spanGaps: false,
                elements: { line: { tension: 0 } },
                tension: -1,
                tooltips: { mode: 'index', intersect: false },
                hover: { mode: 'nearest', intersect: true }
            }
        };

        return config;
    }
}
