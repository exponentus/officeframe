import { Observer } from '@nb/core';

export class Reminder {
    id: string = '';
    kind: string = 'event';
    editable: boolean;
    //
    title: string;
    description: string;
    reminderType: 'UNKNOWN' | 'SILENT' | 'E_MAIL';
    observers: Observer[];

    static convertToDto(m: Reminder) {
        return {
            id: m.id,
            title: m.title,
            description: m.description,
            reminderType: m.reminderType,
            observers: Observer.convertToDtoList(m.observers)
        };
    }
}
