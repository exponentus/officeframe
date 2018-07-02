import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NbCommonModule, NbEntityCreationDetailsModule } from '@nb/core';

import { REFERENCE_ROUTES } from './reference.routes';
import { ReferenceService } from './reference.service';

import { ReferenceContainerComponent } from './components/container';
import { ReferenceFormComponent } from './components/form';
import { ApprovalRouteFormComponent } from './components/approval-route/approval-route-form';
import { ApprovalRouteBlocksComponent } from './components/approval-route/route-blocks';

@NgModule({
    declarations: [
        ReferenceContainerComponent,
        ReferenceFormComponent,
        ApprovalRouteFormComponent,
        ApprovalRouteBlocksComponent
    ],
    imports: [
        NbCommonModule,
        NbEntityCreationDetailsModule,
        RouterModule.forChild(REFERENCE_ROUTES)
    ],
    providers: [
        ReferenceService
    ]
})
export class ReferenceModule { }
