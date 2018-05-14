import { Routes } from '@angular/router';

import { AuthGuard, ViewPage, UserProfileComponent, OfflinePage } from '@nb/core';

import { IntegrationContainerComponent } from './components/container';
import { ServiceDetailsComponent } from './components/service/service-details';

export const INTEGRATION_ROUTES: Routes = [{
    path: 'Integration', component: IntegrationContainerComponent, canActivate: [AuthGuard],
    children: [
        { path: '', redirectTo: 'services', pathMatch: 'full' },
        { path: 'index', redirectTo: 'services', pathMatch: 'full' },
        { path: 'offline', component: OfflinePage },
        { path: 'search', component: ViewPage },
        { path: 'user-profile', component: UserProfileComponent },
        { path: 'services/:clazz', component: ServiceDetailsComponent },
        { path: ':viewId', component: ViewPage }
    ]
}];
