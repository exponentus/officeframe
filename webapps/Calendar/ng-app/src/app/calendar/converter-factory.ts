import { IEntity, IDto, IDtoConverter } from '@nb/core';

import { Event, Reminder } from './models';

export function getConverter(entityKind: string): IDtoConverter {
    switch (entityKind) {
        case 'Event':
            return Event;
        case 'Reminder':
            return Reminder;
        default:
            return DefaultConverter;
    }
}

export function convertToDto(model: IEntity): IDto {
    return getConverter(model.kind).convertToDto(model);
}

export class DefaultConverter {
    static convertToDto(model: IEntity): IDto {
        return model;
    }
}
