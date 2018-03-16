import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NbCommonModule } from '@nb/core';

import { INTEGRATION_ROUTES } from './integration.routes';
import { IntegrationService } from './integration.service';

import { IntegrationContainerComponent } from './components/container';
import { ServiceDetailsComponent } from './components/service/service-details';

@NgModule({
    declarations: [
        IntegrationContainerComponent,
        ServiceDetailsComponent
    ],
    imports: [
        NbCommonModule,
        RouterModule.forChild(INTEGRATION_ROUTES)
    ],
    providers: [
        IntegrationService
    ]
})
export class IntegrationModule { }
