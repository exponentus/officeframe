import { Component, Output, EventEmitter } from '@angular/core';

import { AppService } from '@nb/core';

@Component({
    selector: 'nb-sign-in',
    template: `
        <form class="sign-in" (submit)="login($event)" ngNoForm ngNativeValidate>
            <h1>{{'sign_in' | translate}}</h1>
            <label class="login">
                <i class="fa fa-user"></i>
                <input type="text" name="login" value="" trim [disabled]="appService.loading"
                    [(ngModel)]="model.login" placeholder="{{'user' | translate}}" required (focus)="resetAuthFail()" />
            </label>
            <label class="pwd">
                <i class="fa fa-lock"></i>
                <input type="password" name="pwd" value="" trim [disabled]="appService.loading"
                    [(ngModel)]="model.pwd" placeholder="{{'password' | translate}}" required (focus)="resetAuthFail()" />
            </label>
            <p class="auth-failed alert-error" *ngIf="appService.authFailed">{{'authorization_was_failed' | translate}}</p>
            <button class="btn" type="submit" [disabled]="appService.loading">{{'login' | translate}}</button>
            <div class="clearfix"></div>
        </form>
    `
})
export class SignInComponent {
    model = { login: '', pwd: '' };

    constructor(public appService: AppService) { }

    resetAuthFail() {
        this.appService.authFailed = false;
    }

    login(e: Event) {
        e.preventDefault();
        if (this.model.login.trim() && this.model.pwd.trim()) {
            this.appService.login(this.model.login.trim(), this.model.pwd.trim());
        }
    }
}
