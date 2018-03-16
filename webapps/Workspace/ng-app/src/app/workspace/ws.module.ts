import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NbCommonModule } from '@nb/core';

import { WS_ROUTES } from './ws.routes';
import { WorkspaceService } from './ws.service';
import { WorkspaceComponent } from './ws.component';
import { WorkspaceAppsComponent } from './ws-apps.component';
import { SignInComponent } from './sign-in.component';

@NgModule({
    declarations: [
        WorkspaceComponent,
        WorkspaceAppsComponent,
        SignInComponent
    ],
    imports: [
        NbCommonModule,
        RouterModule.forChild(WS_ROUTES)
    ],
    providers: [
        WorkspaceService
    ]
})
export class WorkspaceModule { }
