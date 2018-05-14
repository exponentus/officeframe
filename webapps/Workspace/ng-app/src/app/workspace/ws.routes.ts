import { Routes } from '@angular/router';

import { UserProfileComponent, AboutPage } from '@nb/core';

import { WorkspaceComponent } from './ws.component';
import { WorkspaceAppsComponent } from './ws-apps.component';

export const WS_ROUTES: Routes = [{
    path: 'Workspace', component: WorkspaceComponent,
    children: [
        { path: '', component: WorkspaceAppsComponent },
        { path: 'index', component: WorkspaceAppsComponent },
        { path: 'user-profile', component: UserProfileComponent },
        { path: 'about', component: AboutPage }
    ]
}];
