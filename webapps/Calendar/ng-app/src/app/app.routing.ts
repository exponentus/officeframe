import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AppService, WindowRef, ErrorPage } from '@nb/core';

import { CalendarModule } from './calendar/calendar.module';

const routes: Routes = [
    { path: '', redirectTo: '/Calendar', pathMatch: 'full' },
    { path: '404', component: ErrorPage, data: { code: '404', message: 'error_404' } },
    { path: '**', component: ErrorPage, data: { code: '404', message: 'error_404' } }
];

@NgModule({
    imports: [RouterModule.forRoot(routes, { useHash: true })],
    exports: [
        RouterModule,
        CalendarModule
    ]
})
export class AppRoutingModule {
    constructor(
        private windowRef: WindowRef,
        private appService: AppService
    ) {
        let wl = windowRef.nativeWindow.location;
        let appPath;
        try {
            appPath = '/' + wl.pathname.split('/')[1];
        } catch (e) {
            appPath = appService.workspaceUrl;
        }

        if (wl.hash.length < 3) { // '', '#', '#/'
            history.replaceState(null, null, location.pathname + '#' + appPath);
        }
    }
}
