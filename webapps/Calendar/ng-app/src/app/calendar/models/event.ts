import { IEntity, DATE_TIME_FORMAT } from '@nb/core';
import { mdFormat } from '@nb/core';
import { Observer } from '@nb/core';
import { Tag } from './tag';
import { Reminder } from './reminder';

export class Event {
    id: string = '';
    kind: string = 'event';
    editable: boolean;
    //
    title: string;
    eventTime: string;
    priority: string;
    reminder: Reminder;
    description: string;
    relatedURL: string;
    tags: Tag[];
    observers: Observer[];

    static convertToDto(m: Event) {
        return {
            id: m.id,
            title: m.title,
            eventTime: mdFormat(m.eventTime, DATE_TIME_FORMAT),
            priority: m.priority,
            reminder: m.reminder ? { id: m.reminder.id } : null,
            description: m.description,
            relatedURL: m.relatedURL,
            tags: Tag.convertToDtoList(m.tags),
            observers: Observer.convertToDtoList(m.observers)
        };
    }
}
