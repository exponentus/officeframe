import { Attachment, User, Observer } from '@nb/core';
import { Comment } from './comment';

export class Topic {
    id: string;
    isNew: boolean = true;
    editable: boolean = false;
    kind: string;
    author: any;
    authorId: string;
    regDate: Date;
    url: string;
    apiUrl: string;
    //
    status: any = 'DRAFT';
    title: string;
    body: string;
    tags: any[];
    observers: Observer[];
    attachments: Attachment[];
    comments: Comment[];

    static convertToDto(m: Topic): any {
        return {
            status: m.status,
            title: m.title || '',
            body: m.body || '',
            tags: m.tags ? m.tags.map(it => { return { id: it.id }; }) : null,
            observers: m.observers ? m.observers.map(it => { return { employee: { id: it.employee.id } }; }) : [],
            attachments: Attachment.convertToDtoList(m.attachments)
        };
    }
}
