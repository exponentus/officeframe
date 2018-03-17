import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NbCommonModule, NBChartModule } from '@nb/core';

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
        NBChartModule,
        RouterModule.forChild(MONITORING_URL_ROUTES)
    ],
    providers: [
        MonitoringService
    ]
})
export class MonitoringModule { }
