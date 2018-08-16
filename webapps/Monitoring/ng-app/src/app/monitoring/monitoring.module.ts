import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NbCommonModule } from '@nb/core';
import { NbChartModule } from '@nb/components';

import { MONITORING_URL_ROUTES } from './monitoring.routes';
import { MonitoringService } from './monitoring.service';

import { ChartsPage } from './pages';
import { MonitoringContainerComponent, RecordsCountChartComponent } from './components';

@NgModule({
    declarations: [
        MonitoringContainerComponent,
        ChartsPage,
        RecordsCountChartComponent
    ],
    imports: [
        NbCommonModule,
        NbChartModule,
        RouterModule.forChild(MONITORING_URL_ROUTES)
    ],
    providers: [
        MonitoringService
    ]
})
export class MonitoringModule { }
