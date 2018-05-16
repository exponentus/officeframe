import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Observable } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { saveAs } from 'file-saver';

import {
    IAction, IEntity, IDto, IEntityService,
    AppService, DataService, NotificationService,
    createApiUrl
} from '@nb/core';

import { DATA_EXPORT_URL } from './constants';
import { ReportProfile } from './models';
import { convertToDto } from './converter-factory';

@Injectable()
export class DataExportService implements IEntityService<IEntity> {

    errors: any = {};

    constructor(
        private ngxTranslate: TranslateService,
        private notifyService: NotificationService,
        private appService: AppService,
        private dataService: DataService
    ) { }

    fetchUrl(webUrl: string, params: any) {
        return this.dataService.apiGet(createApiUrl(webUrl), params);
    }

    convertToDto(model: IEntity): IDto {
        return convertToDto(model);
    }

    generateReport(action: IAction, model: ReportProfile, params = {}) {
        let noty = this.notifyService.process(this.ngxTranslate.instant('report_generation')).show();
        let url = `${DATA_EXPORT_URL.API_REPORT_PROFILES}/action/${action.url}`;
        return this.dataService.apiPostDownload(url, convertToDto(model))
            .map(response => {
                let disposition = response.headers.get('content-disposition');
                let filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
                let matches = filenameRegex.exec(disposition);
                let filename: string = '';
                if (matches != null && matches[1]) {
                    if (matches[1].indexOf("'") != -1) {
                        filename = matches[1].split("'")[1].replace(/['"]/g, '');
                    } else {
                        filename = matches[1].replace(/['"]/g, '');
                    }
                }
                let ldi = filename.lastIndexOf('.');
                let fileExt = filename.substring(ldi + 1);
                let blob = new Blob([response.body], { type: 'application/' + fileExt });

                saveAs(blob, filename);
                return response;
            })
            .catch(err => {
                let fileAsTextObservable = new Observable<string>(observer => {
                    const reader = new FileReader();
                    reader.onload = (e) => {
                        let responseText = (<any>e.target).result;

                        observer.next(responseText);
                        observer.complete();
                    };
                    const errMsg = reader.readAsText(err.error, 'utf-8');
                });

                return fileAsTextObservable
                    .pipe(switchMap(errMsgJsonAsText => {
                        let _err = JSON.parse(errMsgJsonAsText);
                        this.handleRequestError(_err, 3000);
                        return Observable.throw(_err);
                    }));
            })
            .finally(() => noty.remove());
    }

    handleMessages(option: { messages: string[], isError?: boolean, delay?: number }) {
        if (option.messages) {
            option.messages.forEach(msg => {
                if (msg) {
                    if (option.isError) {
                        if (option.delay) {
                            this.notifyService.error(this.ngxTranslate.instant(msg)).show().remove(option.delay);
                        } else {
                            this.notifyService.error(this.ngxTranslate.instant(msg)).show();
                        }
                    } else {
                        this.notifyService.success(this.ngxTranslate.instant(msg)).show().remove(option.delay || 3000);
                    }
                }
            });
        }
    }

    handleRequestError(error: any = {}, delay: number = 0) {
        this.errors = {};

        this.handleMessages({ messages: error.message, isError: true, delay });

        if (error.payload && error.payload.error && error.payload.error.type === 'INCONSISTENT_DATA') {
            for (let err of error.payload.error.errors) {
                this.errors[err.field] = {
                    message: err.message,
                    error: err.error
                };
            }
        }

        return error;
    }
}
