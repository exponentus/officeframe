import { Injectable } from '@angular/core';

import { DataService } from '@nb/core';
import { IEntity, IDto, IEntityService } from '@nb/core';
import { createApiUrl } from '@nb/core';
import { convertToDto } from './converter-factory';
import { CALENDAR_URL } from './constants';

@Injectable()
export class CalendarService implements IEntityService<IEntity> {

    constructor(
        private dataService: DataService
    ) { }

    fetchUrl(url: string, params: any) {
        return this.dataService.apiGet(createApiUrl(url), params);
    }

    fetchCalendarEvents(params: { eventStart: string, eventEnd: string, limit: number }) {
        return this.dataService.apiGet(CALENDAR_URL.API_EVENT, params);
    }

    convertToDto(model: IEntity): IDto {
        return convertToDto(model);
    }
}
