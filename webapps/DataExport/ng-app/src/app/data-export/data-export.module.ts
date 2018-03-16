import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NbCommonModule } from '@nb/core';

import { DATA_EXPORT_ROUTES } from './data-export.routes';
import { DataExportService } from './data-export.service';

import { DataExportContainerComponent } from './components/container';
import { ReportProfileComponent } from './components/report-profile/report-profile';

@NgModule({
    declarations: [
        DataExportContainerComponent,
        ReportProfileComponent
    ],
    imports: [
        NbCommonModule,
        RouterModule.forChild(DATA_EXPORT_ROUTES)
    ],
    providers: [
        DataExportService
    ]
})
export class DataExportModule { }
