import { Routes } from '@angular/router';

import { ViewPage, UserProfileComponent, OfflinePage } from '@nb/core';

import { DataExportContainerComponent } from './components/container';
import { ReportProfileComponent } from './components/report-profile/report-profile';

export const DATA_EXPORT_ROUTES: Routes = [{
    path: 'DataExport', component: DataExportContainerComponent,
    children: [
        { path: '', redirectTo: 'report-profiles', pathMatch: 'full' },
        { path: 'index', redirectTo: 'report-profiles', pathMatch: 'full' },
        { path: 'offline', component: OfflinePage },
        { path: 'search', component: ViewPage },
        { path: 'user-profile', component: UserProfileComponent },
        { path: 'report-profiles/:id', component: ReportProfileComponent },
        { path: ':viewId', component: ViewPage }
    ]
}];
