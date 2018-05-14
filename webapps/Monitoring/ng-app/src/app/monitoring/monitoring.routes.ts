import { Routes } from '@angular/router';

import { AuthGuard, ViewPage, UserProfileComponent, OfflinePage } from '@nb/core';

import { ChartsPage } from './pages';
import { MonitoringContainerComponent } from './components';

export const MONITORING_URL_ROUTES: Routes = [{
    path: 'Monitoring', component: MonitoringContainerComponent, canActivate: [AuthGuard],
    children: [
        { path: '', redirectTo: 'user-activities', pathMatch: 'full' },
        { path: 'index', redirectTo: 'user-activities', pathMatch: 'full' },
        { path: 'offline', component: OfflinePage },
        { path: 'search', component: ViewPage },
        { path: 'user-profile', component: UserProfileComponent },
        { path: 'user-activities/count-of-records', component: ViewPage },
        { path: 'user-activities/count-of-records/chart', component: ChartsPage },
        { path: 'user-activities/last-visits', component: ViewPage },
        { path: ':viewId/s/:slug', component: ViewPage },
        { path: ':viewId', component: ViewPage }
    ]
}];
