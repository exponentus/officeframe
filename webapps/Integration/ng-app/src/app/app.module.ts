import { NgModule } from '@angular/core';

import { NbCoreModule, NbRootComponent } from '@nb/core';
import { AppRoutingModule } from './app.routing';

@NgModule({
    bootstrap: [NbRootComponent],
    imports: [
        NbCoreModule,
        AppRoutingModule
    ],
    exports: [
        NbCoreModule
    ]
})
export class AppModule { }
